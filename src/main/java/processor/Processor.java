package processor;

import common.MessagingUtil;
import io.aeron.Aeron;
import io.aeron.Subscription;
import io.aeron.logbuffer.Header;
import model.QuoteDecoder;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Processor implements Runnable {
    private final Aeron aeron;
    private final Subscription quoteSubscription;
    private final int processNumber;
    private final UnsafeBuffer quoteBuffer = new UnsafeBuffer(new byte[256]); // Buffer for decoding
    private final QuoteDecoder quoteDecoder = new QuoteDecoder();  // SBE decoder

    public Processor(Aeron aeron, int processNumber) {
        this.aeron = aeron;
        this.quoteSubscription = aeron.addSubscription(MessagingUtil.IPC_CHANNEL, MessagingUtil.QUOTE_STREAM_ID);
        this.processNumber = processNumber;
        log("Processor initialized.", processNumber);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            quoteSubscription.poll(this::onQuoteReceived, 100);  // Poll for quotes
        }
    }

    private void onQuoteReceived(DirectBuffer buffer, int offset, int length, Header header) {
        // Wrap the buffer with the SBE decoder
        quoteDecoder.wrap(buffer, offset, length, 0);

        // Extract fields using the decoder
        long publisherId = quoteDecoder.publisherId();
        long timestamp = quoteDecoder.timestamp();
        String symbol = String.valueOf((char) quoteDecoder.symbol());
        float bidPrice = quoteDecoder.bidPrice();
        int bidSize = quoteDecoder.bidSize();
        float askPrice = quoteDecoder.askPrice();
        int askSize = quoteDecoder.askSize();
        boolean isIndicative = quoteDecoder.isIndicative() == 1;

        long currentTimestamp = System.nanoTime();  // Get the current time in nanoseconds
        long latency = TimeUnit.NANOSECONDS.toMicros(currentTimestamp - timestamp);  // Calculate latency in microseconds

        log("Processed Quote: Symbol=" + symbol +
                ", BidPrice=" + bidPrice + ", BidSize=" + bidSize +
                ", AskPrice=" + askPrice + ", AskSize=" + askSize +
                ", Indicative=" + isIndicative + ", Latency: " + latency + " microseconds", processNumber);

        // Continue processing if needed...
    }

    public static void log(String message, int processNumber) {
        String timestamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
        System.out.printf("[%s] [Processor-%d] %s%n", timestamp, processNumber, message);
    }
}
