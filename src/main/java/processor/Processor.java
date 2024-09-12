package processor;

import common.MessagingUtil;
import io.aeron.Aeron;
import io.aeron.Publication;
import io.aeron.Subscription;
import io.aeron.logbuffer.Header;
import model.*;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Processor implements Runnable {
    private final Aeron aeron;
    private final Subscription quoteSubscription;
    private final Subscription orderResponseSubscription;
    private final Publication orderPublication;  // Publication for sending Orders
    private final int processNumber;
    private final UnsafeBuffer orderBuffer = new UnsafeBuffer(new byte[256]);  // Pre-allocated buffer for orders
    private final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();  // SBE MessageHeader encoder
    private final OrderEncoder orderEncoder = new OrderEncoder();  // SBE Order encoder
    private final MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();  // SBE MessageHeader decoder
    private final QuoteDecoder quoteDecoder = new QuoteDecoder();  // SBE Quote decoder
    private final OrderResponseDecoder orderResponseDecoder = new OrderResponseDecoder();  // SBE OrderResponse decoder

    public Processor(Aeron aeron, int processNumber) {
        this.aeron = aeron;
        this.quoteSubscription = aeron.addSubscription(MessagingUtil.IPC_CHANNEL, MessagingUtil.QUOTE_STREAM_ID);
        this.orderResponseSubscription = aeron.addSubscription(MessagingUtil.IPC_CHANNEL, MessagingUtil.ORDER_RESPONSE_STREAM_ID);
        this.orderPublication = aeron.addPublication(MessagingUtil.IPC_CHANNEL, MessagingUtil.ORDER_STREAM_ID);  // Publication to send orders
        this.processNumber = processNumber;
        log("Processor initialized.", processNumber);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            quoteSubscription.poll(this::onQuoteReceived, 100);  // Poll for quotes
            orderResponseSubscription.poll(this::onOrderResponseReceived, 100);  // Poll for OrderResponses

            // Send an Order to the Publishers
            sendOrder();

            try {
                Thread.sleep(100);  // Simulate periodic order sending
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void sendOrder() {
        long clOrdId = ThreadLocalRandom.current().nextLong(1000, 10000);  // Generate a random client order ID
        byte side = (byte) (ThreadLocalRandom.current().nextBoolean() ? 'B' : 'S');  // Buy or Sell
        int quantity = 100 + ThreadLocalRandom.current().nextInt(901);
        float price = 100.00f + (120.00f - 100.00f) * ThreadLocalRandom.current().nextFloat();
        long timestamp = System.nanoTime();  // Capture the current time in nanoseconds

        // Encode the Order message
        messageHeaderEncoder.wrap(orderBuffer, 0)
                .blockLength(orderEncoder.sbeBlockLength())
                .templateId(orderEncoder.sbeTemplateId())
                .schemaId(orderEncoder.sbeSchemaId())
                .version(orderEncoder.sbeSchemaVersion());

        int headerLength = messageHeaderEncoder.encodedLength();
        orderEncoder.wrap(orderBuffer, headerLength)
                .publisherId(processNumber)
                .clOrdId(clOrdId)
                .timestamp(timestamp)
                .side(side)
                .quantity(quantity)
                .price(price);

        // Send the encoded order over Aeron
        long result = orderPublication.offer(orderBuffer, 0, headerLength + orderEncoder.encodedLength());
        if (result < 0) {
            log("Failed to send Order, result: " + result, processNumber);
        } else {
            log(String.format("Order sent: ClOrdId=%d, Side=%c, Quantity=%d, Price=%.2f", clOrdId, side, quantity, price), processNumber);
        }
    }

    private void onQuoteReceived(DirectBuffer buffer, int offset, int length, Header header) {
        messageHeaderDecoder.wrap(buffer, offset);
        int templateId = messageHeaderDecoder.templateId();
        offset += messageHeaderDecoder.encodedLength();

        if (templateId == QuoteDecoder.TEMPLATE_ID) {
            quoteDecoder.wrap(buffer, offset, messageHeaderDecoder.blockLength(), messageHeaderDecoder.version());

            // Extract all fields for logging
            long publisherId = quoteDecoder.publisherId();
            long timestamp = quoteDecoder.timestamp();
            String symbol = String.valueOf((char) quoteDecoder.symbol());
            float bidPrice = quoteDecoder.bidPrice();
            int bidSize = quoteDecoder.bidSize();
            float askPrice = quoteDecoder.askPrice();
            int askSize = quoteDecoder.askSize();
            boolean isIndicative = quoteDecoder.isIndicative() == 1;

            // Calculate latency
            long currentTimestamp = System.nanoTime();
            long latency = TimeUnit.NANOSECONDS.toMicros(currentTimestamp - timestamp);

            // Log the details of the Quote message
            log(String.format(
                    "Processed Quote: PublisherId=%d, Symbol=%s, BidPrice=%.2f, BidSize=%d, AskPrice=%.2f, AskSize=%d, Indicative=%b, Latency=%d microseconds",
                    publisherId, symbol, bidPrice, bidSize, askPrice, askSize, isIndicative, latency), processNumber);
        } else {
            log("Unknown template ID: " + templateId, processNumber);
        }
    }

    // Handle incoming OrderResponses
    private void onOrderResponseReceived(DirectBuffer buffer, int offset, int length, Header header) {
        messageHeaderDecoder.wrap(buffer, offset);
        int templateId = messageHeaderDecoder.templateId();
        offset += messageHeaderDecoder.encodedLength();

        if (templateId == OrderResponseDecoder.TEMPLATE_ID) {
            orderResponseDecoder.wrap(buffer, offset, messageHeaderDecoder.blockLength(), messageHeaderDecoder.version());

            // Extract all fields for logging
            long clOrdId = orderResponseDecoder.clOrdId();
            byte status = orderResponseDecoder.status();
            int filledQuantity = orderResponseDecoder.filledQuantity();
            float filledPrice = orderResponseDecoder.filledPrice();

            // Log the details of the OrderResponse message
            log(String.format(
                    "Received OrderResponse: ClOrdId=%d, Status=%c, FilledQuantity=%d, FilledPrice=%.2f",
                    clOrdId, (char) status, filledQuantity, filledPrice), processNumber);
        } else {
            log("Unknown template ID: " + templateId, processNumber);
        }
    }

    public static void log(String message, int processNumber) {
        String timestamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
        System.out.printf("[%s] [Processor-%d] %s%n", timestamp, processNumber, message);
    }
}
