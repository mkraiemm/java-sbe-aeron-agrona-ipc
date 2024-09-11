package dropcopier;

import common.MessagingUtil;
import io.aeron.Aeron;
import io.aeron.Subscription;
import io.aeron.logbuffer.FragmentHandler;
import model.QuoteDecoder;  // Import SBE Decoder for Quote
import model.OrderResponseDecoder;  // Import SBE Decoder for OrderResponse
import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;

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
    private final UnsafeBuffer buffer = new UnsafeBuffer(new byte[256]);  // Buffer for decoding
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
        while (running) {
            quoteSubscription.poll(this::onQuoteReceived, 10);  // Poll for quotes
            orderResponseSubscription.poll(this::onOrderResponseReceived, 10);  // Poll for order responses
        }
    }

    /**
     * Handles the received Quote message by decoding it using SBE.
     */
    private void onQuoteReceived(DirectBuffer buffer, int offset, int length, io.aeron.logbuffer.Header header) {
        // Wrap the buffer and decode the quote message
        quoteDecoder.wrap(buffer, offset, length, 0);

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

        log(String.format("Quote received: Symbol=%s, BidPrice=%.2f, BidSize=%d, AskPrice=%.2f, AskSize=%d, Indicative=%b, Latency=%d microseconds",
                symbol, bidPrice, bidSize, askPrice, askSize, isIndicative, latency));
    }

    /**
     * Handles the received OrderResponse message by decoding it using SBE.
     */
    private void onOrderResponseReceived(DirectBuffer buffer, int offset, int length, io.aeron.logbuffer.Header header) {
        // Wrap the buffer and decode the order response message
        orderResponseDecoder.wrap(buffer, offset, length, 0);

        // Extract fields from the decoded message
        long publisherId = orderResponseDecoder.publisherId();
        long timestamp = orderResponseDecoder.timestamp();
        float price = orderResponseDecoder.price();
        int quantity = orderResponseDecoder.quantity();
        float filledPrice = orderResponseDecoder.filledPrice();
        int filledQuantity = orderResponseDecoder.filledQuantity();

        // Calculate latency
        long currentTimestamp = System.nanoTime();
        long latency = TimeUnit.NANOSECONDS.toMicros(currentTimestamp - timestamp);

        log(String.format("OrderResponse received: Price=%.2f, Quantity=%d, FilledPrice=%.2f, FilledQuantity=%d, Latency=%dµs",
                price, quantity, filledPrice, filledQuantity, latency));
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
