package dropcopier;

import common.MessagingUtil;
import io.aeron.Aeron;
import io.aeron.Subscription;
import io.aeron.logbuffer.FragmentHandler;
import model.MessageHeaderDecoder;
import model.QuoteDecoder;
import model.OrderResponseDecoder;
import org.agrona.DirectBuffer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Represents a DropCopier that subscribes to quote and order response streams and logs the received messages.
 */
public class DropCopier implements Runnable {
    private final Aeron aeron;
    private final Subscription quoteSubscription;
    private final Subscription orderResponseSubscription;
    private final MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();  // Message header decoder
    private final QuoteDecoder quoteDecoder = new QuoteDecoder();  // SBE decoder for Quote
    private final OrderResponseDecoder orderResponseDecoder = new OrderResponseDecoder();  // SBE decoder for OrderResponse
    private volatile boolean running = true; // Control flag for the run loop

    /**
     * Constructor to initialize the DropCopier with Aeron context.
     *
     * @param aeron Aeron instance for communication
     */
    public DropCopier(Aeron aeron) {
        this.aeron = aeron;
        // Subscribe using the network channel for quotes and order responses
        this.quoteSubscription = aeron.addSubscription(MessagingUtil.IPC_CHANNEL, MessagingUtil.QUOTE_STREAM_ID);
        this.orderResponseSubscription = aeron.addSubscription(MessagingUtil.IPC_CHANNEL, MessagingUtil.ORDER_STREAM_ID);
    }

    /**
     * Run method to continuously poll for incoming messages.
     */
    @Override
    public void run() {
        FragmentHandler fragmentHandler = this::onMessageReceived;

        while (running) {
            quoteSubscription.poll(fragmentHandler, 10);  // Poll for quotes
            orderResponseSubscription.poll(fragmentHandler, 10);  // Poll for order responses
        }
    }

    /**
     * General message handler that decodes the Message Header and directs the message to the proper decoder.
     */
    private void onMessageReceived(DirectBuffer buffer, int offset, int length, io.aeron.logbuffer.Header header) {
        // Decode the message header first
        messageHeaderDecoder.wrap(buffer, offset);

        int templateId = messageHeaderDecoder.templateId();
        offset += messageHeaderDecoder.encodedLength(); // Move offset after the header

        // Determine which message type is being received
        if (templateId == QuoteDecoder.TEMPLATE_ID) {
            onQuoteReceived(buffer, offset);
        } else if (templateId == OrderResponseDecoder.TEMPLATE_ID) {
            onOrderResponseReceived(buffer, offset);
        } else {
            log("Unknown templateId: " + templateId);
        }
    }

    /**
     * Handles the received Quote message by decoding it using SBE.
     */
    private void onQuoteReceived(DirectBuffer buffer, int offset) {
        // Wrap the buffer and decode the quote message
        quoteDecoder.wrap(buffer, offset, quoteDecoder.sbeBlockLength(), quoteDecoder.sbeSchemaVersion());

        // Extract fields from the decoded message
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

        log(String.format("Quote received: PublisherId=%d, Symbol=%s, BidPrice=%.2f, BidSize=%d, AskPrice=%.2f, AskSize=%d, Indicative=%b, Latency=%d microseconds",
                publisherId, symbol, bidPrice, bidSize, askPrice, askSize, isIndicative, latency));
    }

    /**
     * Handles the received OrderResponse message by decoding it using SBE.
     */
    private void onOrderResponseReceived(DirectBuffer buffer, int offset) {
        // Wrap the buffer and decode the order response message
        orderResponseDecoder.wrap(buffer, offset, orderResponseDecoder.sbeBlockLength(), orderResponseDecoder.sbeSchemaVersion());

        // Extract fields from the decoded message
        long clOrdId = orderResponseDecoder.clOrdId();
        byte status = orderResponseDecoder.status();
        int filledQuantity = orderResponseDecoder.filledQuantity();
        float filledPrice = orderResponseDecoder.filledPrice();

        // Calculate latency
        long currentTimestamp = System.nanoTime();
        long latency = TimeUnit.NANOSECONDS.toMicros(currentTimestamp - orderResponseDecoder.timestamp());

        log(String.format("OrderResponse received: ClOrdId=%d, Status=%c, FilledQuantity=%d, FilledPrice=%.2f, Latency=%d microseconds",
                clOrdId, (char) status, filledQuantity, filledPrice, latency));
    }

    /**
     * Stops the DropCopier from running.
     */
    public void stop() {
        running = false;  // Set the flag to false to exit the run loop
    }

    /**
     * Static log method to include timestamp.
     *
     * @param message The message to log
     */
    private static void log(String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
        System.out.printf("[%s] [DropCopier] %s%n", timestamp, message);
    }
}
