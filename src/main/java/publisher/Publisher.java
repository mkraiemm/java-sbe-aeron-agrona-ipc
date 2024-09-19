package publisher;

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

public class Publisher implements Runnable {
    private final Aeron aeron;
    private final Publication quotePublication;
    private final Subscription orderSubscription;
    private final Publication orderResponsePublication;
    private final int processNumber;
    private final UnsafeBuffer quoteBuffer = new UnsafeBuffer(new byte[512]);
    private final UnsafeBuffer responseBuffer = new UnsafeBuffer(new byte[256]);
    private final byte[][] symbols;
    private final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
    private final QuoteEncoder quoteEncoder = new QuoteEncoder();
    private final OrderDecoder orderDecoder = new OrderDecoder();
    private final OrderResponseEncoder orderResponseEncoder = new OrderResponseEncoder();
    private final MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();
    private final ConcreteIdGenerator idGenerator = new ConcreteIdGenerator();

    public Publisher(Aeron aeron, int processNumber) {
        this.aeron = aeron;
        this.quotePublication = aeron.addPublication(MessagingUtil.QUOTE_CHANNEL, MessagingUtil.QUOTE_STREAM_ID);
        this.orderSubscription = aeron.addSubscription(MessagingUtil.ORDER_CHANNEL, MessagingUtil.ORDER_STREAM_ID);
        this.orderResponsePublication = aeron.addPublication(MessagingUtil.ORDER_RESPONSE_CHANNEL, MessagingUtil.ORDER_RESPONSE_STREAM_ID);
        this.processNumber = processNumber;

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
            sendQuote();

            orderSubscription.poll(this::handleOrder, 10);

            // Random sleep between 100 to 1,000,000 microseconds
            long sleepTimeMicros = ThreadLocalRandom.current().nextLong(100, 1_000_001);
            try {
                Thread.sleep(sleepTimeMicros / 1_000, (int) (sleepTimeMicros % 1_000) * 1_000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void sendQuote() {
        int symbolIndex = ThreadLocalRandom.current().nextInt(symbols.length);
        byte[] symbol = symbols[symbolIndex];

        float bidPrice = 100.00f + (120.00f - 100.00f) * ThreadLocalRandom.current().nextFloat();
        int bidSize = 100 + ThreadLocalRandom.current().nextInt(901);
        float askPrice = bidPrice + 0.01f + (0.5f) * ThreadLocalRandom.current().nextFloat();
        int askSize = 100 + ThreadLocalRandom.current().nextInt(901);

        long timestamp = System.nanoTime();

        messageHeaderEncoder.wrap(quoteBuffer, 0)
                .blockLength(quoteEncoder.sbeBlockLength())
                .templateId(quoteEncoder.sbeTemplateId())
                .schemaId(quoteEncoder.sbeSchemaId())
                .version(quoteEncoder.sbeSchemaVersion());

        int headerLength = messageHeaderEncoder.encodedLength();
        quoteEncoder.wrap(quoteBuffer, headerLength);

        quoteEncoder.publisherId(processNumber)
                .timestamp(timestamp)
                .isIndicative((short) 0)
                .bidSize(bidSize)
                .bidPrice(bidPrice)
                .askPrice(askPrice)
                .askSize(askSize);

        // Encode the symbol
        for (int i = 0; i < symbol.length; i++) {
            quoteEncoder.symbol(symbol[i]);
        }
        // Pad the rest of the symbol field with spaces
        for (int i = symbol.length; i < quoteEncoder.symbolEncodingLength(); i++) { //changed
            quoteEncoder.symbol((byte) i);
        }

        int messageLength = headerLength + quoteEncoder.encodedLength();
        long result = quotePublication.offer(quoteBuffer, 0, messageLength);
        if (result < 0) {
            log("Failed to send Quote, result: " + result, processNumber);
        } else {
            log("Quote sent successfully", processNumber);
        }
    }

    private void handleOrder(DirectBuffer buffer, int offset, int length, Header header) {
        messageHeaderDecoder.wrap(buffer, offset);
        offset += messageHeaderDecoder.encodedLength();

        orderDecoder.wrap(buffer, offset, messageHeaderDecoder.blockLength(), messageHeaderDecoder.version());

        long clOrdId = orderDecoder.clOrdId();
        boolean fillOrder = ThreadLocalRandom.current().nextBoolean();
        char status = fillOrder ? 'F' : 'R';

        // Prepare the order response message
        messageHeaderEncoder.wrap(responseBuffer, 0)
                .blockLength(orderResponseEncoder.sbeBlockLength())
                .templateId(orderResponseEncoder.sbeTemplateId())
                .schemaId(orderResponseEncoder.sbeSchemaId())
                .version(orderResponseEncoder.sbeSchemaVersion());

        int headerLength = messageHeaderEncoder.encodedLength();
        orderResponseEncoder.wrap(responseBuffer, headerLength);
        orderResponseEncoder.publisherId(processNumber)
                .timestamp(System.nanoTime())
                .clOrdId(clOrdId)
                .side(orderDecoder.side())
                .quantity(orderDecoder.quantity())
                .price(orderDecoder.price())
                .filledQuantity(fillOrder ? orderDecoder.quantity() : 0)
                .filledPrice(fillOrder ? orderDecoder.price() : 0)
                .status((byte) status);

        int messageLength = headerLength + orderResponseEncoder.encodedLength();
        long result = orderResponsePublication.offer(responseBuffer, 0, messageLength);
        if (result < 0) {
            log("Failed to send OrderResponse, result: " + result, processNumber);
        } else {
            log("OrderResponse sent", processNumber);
        }
    }

    public static void log(String message, int processNumber) {
        System.out.printf("[Publisher-%d] %s%n", processNumber, message);
    }
}
