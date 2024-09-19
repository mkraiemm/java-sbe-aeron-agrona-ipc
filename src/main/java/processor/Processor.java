package processor;

import common.ConcreteIdGenerator;
import common.MessagingUtil;
import io.aeron.Aeron;
import io.aeron.Publication;
import io.aeron.Subscription;
import io.aeron.logbuffer.Header;
import model.*;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

public class Processor implements Runnable {
    private final Aeron aeron;
    private final Subscription quoteSubscription;
    private final Subscription orderResponseSubscription;
    private final Publication orderPublication;
    private final int processNumber;
    private final UnsafeBuffer orderBuffer = new UnsafeBuffer(new byte[512]);
    private final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
    private final OrderEncoder orderEncoder = new OrderEncoder();
    private final MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();
    private final QuoteDecoder quoteDecoder = new QuoteDecoder();
    private final OrderResponseDecoder orderResponseDecoder = new OrderResponseDecoder();
    private final ConcreteIdGenerator idGenerator = new ConcreteIdGenerator();

    private int quoteCounter = 0;
    private final Quote lastQuote = new Quote();

    public Processor(Aeron aeron, int processNumber) {
        this.aeron = aeron;
        this.quoteSubscription = aeron.addSubscription(MessagingUtil.QUOTE_CHANNEL, MessagingUtil.QUOTE_STREAM_ID);
        this.orderResponseSubscription = aeron.addSubscription(MessagingUtil.ORDER_RESPONSE_CHANNEL, MessagingUtil.ORDER_RESPONSE_STREAM_ID);
        this.orderPublication = aeron.addPublication(MessagingUtil.ORDER_CHANNEL, MessagingUtil.ORDER_STREAM_ID);
        this.processNumber = processNumber;
        log("Processor initialized.", processNumber);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            quoteSubscription.poll(this::onQuoteReceived, 100);
            orderResponseSubscription.poll(this::onOrderResponseReceived, 100);

            // Sleep briefly to prevent tight loop
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void onQuoteReceived(DirectBuffer buffer, int offset, int length, Header header) {
        long receiveTime = System.nanoTime();
        messageHeaderDecoder.wrap(buffer, offset);
        offset += messageHeaderDecoder.encodedLength();

        quoteDecoder.wrap(buffer, offset, messageHeaderDecoder.blockLength(), messageHeaderDecoder.version());

        long sentTime = quoteDecoder.timestamp(); // Timestamp when the message was sent

        // Calculate latency
        long latency = receiveTime - sentTime;

        // Log latency
        log(String.format("Quote received with latency: %d microseconds", latency / 1000), processNumber);  // Convert to microseconds

        quoteCounter++;

        // Extract necessary fields for order
        lastQuote.setSymbol(extractSymbol(quoteDecoder));
        lastQuote.setBidPrice(quoteDecoder.bidPrice());
        lastQuote.setBidSize(quoteDecoder.bidSize());
        lastQuote.setAskPrice(quoteDecoder.askPrice());
        lastQuote.setAskSize(quoteDecoder.askSize());
        lastQuote.setTimestamp(quoteDecoder.timestamp());

        // Send an Order every 50 Quotes received
        if (quoteCounter >= 50) {
            sendOrder();
            quoteCounter = 0;
        }
    }

    private void sendOrder() {
        long clOrdId = idGenerator.nextId();
        OrderSide side = ThreadLocalRandom.current().nextBoolean() ? OrderSide.BUY : OrderSide.SELL;
        int quantity = lastQuote.getBidSize();
        float price = lastQuote.getBidPrice();
        long timestamp = System.nanoTime();
        TimeInForce tif = TimeInForce.DAY;

        // Encode the Order message
        messageHeaderEncoder.wrap(orderBuffer, 0)
                .blockLength(orderEncoder.sbeBlockLength())
                .templateId(orderEncoder.sbeTemplateId())
                .schemaId(orderEncoder.sbeSchemaId())
                .version(orderEncoder.sbeSchemaVersion());

        int headerLength = messageHeaderEncoder.encodedLength();
        orderEncoder.wrap(orderBuffer, headerLength)
                .publisherId(processNumber)
                .timestamp(timestamp)
                .clOrdId(clOrdId)
                .side((byte)side.getValue())
                .quantity(quantity)
                .price(price)
                .timeInForce((byte)tif.getValue());

        int messageLength = headerLength + orderEncoder.encodedLength();
        long result = orderPublication.offer(orderBuffer, 0, messageLength);
        if (result < 0) {
            log("Failed to send Order, result: " + result, processNumber);
        } else {
            log("Order sent: ClOrdId=" + clOrdId, processNumber);
        }
    }

    private void onOrderResponseReceived(DirectBuffer buffer, int offset, int length, Header header) {
        long receiveTime = System.nanoTime();
        messageHeaderDecoder.wrap(buffer, offset);
        offset += messageHeaderDecoder.encodedLength();

        orderResponseDecoder.wrap(buffer, offset, messageHeaderDecoder.blockLength(), messageHeaderDecoder.version());

        long sentTime = orderResponseDecoder.timestamp();

        long latency = receiveTime - sentTime;

        // Process OrderResponse if needed
        log(String.format("OrderResponse received: ClOrdId=%d with latency: %d microseconds", orderResponseDecoder.clOrdId(), latency / 1000), processNumber);  // Convert to microseconds
    }

    private String extractSymbol(QuoteDecoder decoder) {
        // Extract the symbol from the decoder without creating garbage
        byte[] symbolBytes = new byte[decoder.symbol()];
        for (int i = 0; i < symbolBytes.length; i++) {
            symbolBytes[i] = decoder.symbol();
        }
        return new String(symbolBytes, StandardCharsets.US_ASCII).trim();
    }

    public static void log(String message, int processNumber) {
        System.out.printf("[Processor-%d] %s%n", processNumber, message);
    }
}
