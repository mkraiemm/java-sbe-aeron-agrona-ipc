package processor;

import common.MessagingUtil;
import io.aeron.Aeron;
import io.aeron.Subscription;
import io.aeron.logbuffer.Header;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Processor implements Runnable {
    private final Aeron aeron;
    private final Subscription quoteSubscription;
    private final int processNumber;

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
        String quoteMessage = buffer.getStringAscii(offset);
        String[] parts = quoteMessage.split(" ");

        // Extract the timestamp (last part of the message)
        if (parts.length < 2 || !isNumeric(parts[parts.length - 1])) {
            log("Invalid quote message format: " + quoteMessage, processNumber);
            return;  // Skip processing this message
        }

        // Get the timestamp when the quote was sent
        long sentTimestamp = Long.parseLong(parts[parts.length - 1]);
        long currentTimestamp = System.nanoTime();  // Get the current time in nanoseconds
        long latency = TimeUnit.NANOSECONDS.toMicros(currentTimestamp - sentTimestamp);  // Calculate latency in microseconds

        log("Processed quote: " + quoteMessage + " | Latency: " + latency + " microseconds", processNumber);

        // Continue processing if needed...
    }

    private boolean isNumeric(String str) {
        return str != null && str.matches("-?\\d+(\\.\\d+)?");
    }

    public static void log(String message, int processNumber) {
        String timestamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
        System.out.printf("[%s] [Processor-%d] %s%n", timestamp, processNumber, message);
    }
}
