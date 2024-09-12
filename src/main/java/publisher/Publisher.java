package publisher;

import common.MessagingUtil;
import io.aeron.Aeron;
import io.aeron.Publication;
import io.aeron.Subscription;
import io.aeron.logbuffer.Header;
import model.MessageHeaderEncoder;
import model.MessageHeaderDecoder;
import model.OrderDecoder;
import model.OrderResponseEncoder;
import model.QuoteEncoder;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import static processor.Processor.log;

public class Publisher implements Runnable {
    private final Aeron aeron;
    private final Publication quotePublication;
    private final Subscription orderSubscription;
    private final Publication orderResponsePublication;
    private final int processNumber;
    private final UnsafeBuffer quoteBuffer = new UnsafeBuffer(new byte[256]);  // Pre-allocated buffer for quote messages
    private final UnsafeBuffer responseBuffer = new UnsafeBuffer(new byte[256]);  // Pre-allocated buffer for order responses
    private final byte[][] symbols;  // Store symbols as byte arrays to avoid String allocations
    private final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();  // SBE MessageHeader encoder
    private final QuoteEncoder quoteEncoder = new QuoteEncoder();  // SBE Quote encoder
    private final OrderDecoder orderDecoder = new OrderDecoder();  // SBE Order decoder
    private final OrderResponseEncoder orderResponseEncoder = new OrderResponseEncoder();  // SBE OrderResponse encoder
    private final MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();  // SBE MessageHeader decoder

    public Publisher(Aeron aeron, int processNumber) {
        this.aeron = aeron;
        this.quotePublication = aeron.addPublication(MessagingUtil.IPC_CHANNEL, MessagingUtil.QUOTE_STREAM_ID);
        this.orderSubscription = aeron.addSubscription(MessagingUtil.IPC_CHANNEL, MessagingUtil.ORDER_STREAM_ID);
        this.orderResponsePublication = aeron.addPublication(MessagingUtil.IPC_CHANNEL, MessagingUtil.ORDER_RESPONSE_STREAM_ID);
        this.processNumber = processNumber;

        // Precompute symbol byte arrays to avoid String allocations
        symbols = new byte[][]{
                "AAPL".getBytes(StandardCharsets.US_ASCII),
                "IBM".getBytes(StandardCharsets.US_ASCII),
                "MSFT".getBytes(StandardCharsets.US_ASCII)
        };

        log("Publisher initialized.", processNumber);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            // Prepare and send a quote
            sendQuote();

            // Poll for incoming orders and respond
            orderSubscription.poll(this::handleOrder, 10);

            try {
                Thread.sleep(10);  // Sleep to simulate periodic publishing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Handle sending a Quote message
    private void sendQuote() {
        int symbolIndex = ThreadLocalRandom.current().nextInt(symbols.length);
        byte[] symbol = symbols[symbolIndex];  // Use precomputed byte arrays for symbols

        // Use ThreadLocalRandom to avoid heap allocation
        double bidPrice = 100.00 + (120.00 - 100.00) * ThreadLocalRandom.current().nextDouble();
        int bidSize = 100 + ThreadLocalRandom.current().nextInt(901);
        double askPrice = 101.00 + (120.00 - 100.00) * ThreadLocalRandom.current().nextDouble();
        int askSize = 100 + ThreadLocalRandom.current().nextInt(901);

        long timestamp = System.nanoTime();  // Capture the current time in nanoseconds

        // Encode message header
        messageHeaderEncoder.wrap(quoteBuffer, 0)
                .blockLength(quoteEncoder.sbeBlockLength())
                .templateId(quoteEncoder.sbeTemplateId())
                .schemaId(quoteEncoder.sbeSchemaId())
                .version(quoteEncoder.sbeSchemaVersion());

        // Begin encoding the Quote message after the header
        int headerLength = messageHeaderEncoder.encodedLength();
        quoteEncoder.wrap(quoteBuffer, headerLength);  // Wrap buffer after the header
        quoteEncoder.publisherId(processNumber)
                .timestamp(timestamp)
                .symbol(symbol[0])  // Store symbol directly in buffer
                .isIndicative((short) 0)  // No boolean allocation, using short
                .bidPrice((float) bidPrice)
                .bidSize(bidSize)
                .askPrice((float) askPrice)
                .askSize(askSize);

        // Send the encoded message over Aeron
        long result = quotePublication.offer(quoteBuffer, 0, headerLength + quoteEncoder.encodedLength());
        if (result < 0) {
            log("Failed to send Quote, result: " + result, processNumber);
        } else {
            // Log the details of the quote
            log(String.format("Quote sent successfully: Symbol=%c, BidPrice=%.2f, BidSize=%d, AskPrice=%.2f, AskSize=%d, Timestamp=%d",
                    symbol[0], bidPrice, bidSize, askPrice, askSize, timestamp), processNumber);
        }
    }

    // Handle incoming orders and respond
    private void handleOrder(DirectBuffer buffer, int offset, int length, Header header) {
        messageHeaderDecoder.wrap(buffer, offset);
        offset += messageHeaderDecoder.encodedLength();

        orderDecoder.wrap(buffer, offset, messageHeaderDecoder.blockLength(), messageHeaderDecoder.version());

        long clOrdId = orderDecoder.clOrdId();
        boolean fillOrder = ThreadLocalRandom.current().nextBoolean();  // Decide if order is filled or rejected
        String status = fillOrder ? "FILLED" : "REJECTED";  // Order status

        // Prepare the order response message using SBE
        messageHeaderEncoder.wrap(responseBuffer, 0)
                .blockLength(orderResponseEncoder.sbeBlockLength())
                .templateId(orderResponseEncoder.sbeTemplateId())
                .schemaId(orderResponseEncoder.sbeSchemaId())
                .version(orderResponseEncoder.sbeSchemaVersion());

        // Begin encoding the OrderResponse message after the header
        int headerLength = messageHeaderEncoder.encodedLength();
        orderResponseEncoder.wrap(responseBuffer, headerLength);
        orderResponseEncoder.publisherId(processNumber)
                .clOrdId(clOrdId)
                .timestamp(System.nanoTime())  // Capture current timestamp
                .status((byte) (fillOrder ? 'F' : 'R'))  // Encode status directly
                .filledQuantity(fillOrder ? orderDecoder.quantity() : 0)
                .filledPrice(fillOrder ? orderDecoder.price() : 0);

        // Send the order response back
        long result = orderResponsePublication.offer(responseBuffer, 0, headerLength + orderResponseEncoder.encodedLength());
        if (result < 0) {
            log("Failed to send OrderResponse, result: " + result, processNumber);
        } else {
            // Log the details of the order response
            log(String.format("OrderResponse sent: ClOrdId=%d, Status=%s, FilledQuantity=%d, FilledPrice=%.2f",
                    clOrdId, status, fillOrder ? orderDecoder.quantity() : 0, fillOrder ? orderDecoder.price() : 0), processNumber);
        }
    }

    public static void log(String message, int processNumber) {
        String timestamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
        System.out.printf("[%s] [Publisher-%d] %s%n", timestamp, processNumber, message);
    }
}

