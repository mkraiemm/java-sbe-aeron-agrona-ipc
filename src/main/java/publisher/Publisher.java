package publisher;

import common.MessagingUtil;
import io.aeron.Aeron;
import io.aeron.Publication;
import org.agrona.concurrent.UnsafeBuffer;

import java.util.Random;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Publisher implements Runnable {
    private final Aeron aeron;
    private final Publication publication;
    private final int processNumber;
    private final Random random = new Random();
    private UnsafeBuffer quoteBuffer = new UnsafeBuffer(new byte[1024]);

    public Publisher(Aeron aeron, int processNumber) {
        this.aeron = aeron;
        this.publication = aeron.addPublication(MessagingUtil.IPC_CHANNEL, MessagingUtil.QUOTE_STREAM_ID);
        this.processNumber = processNumber;
        log("Publisher initialized.", processNumber);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            String[] symbols = {"AAPL", "IBM", "MSFT"};
            String symbol = symbols[random.nextInt(symbols.length)];
            double price = 100.00 + (120.00 - 100.00) * random.nextDouble();
            int size = 100 + random.nextInt(901);

            long timestamp = System.nanoTime();  // Capture the current time in nanoseconds
            String message = String.format("Quote: %s Price: %.2f Size: %d Timestamp: %d", symbol, price, size, timestamp);

            quoteBuffer.putStringAscii(0, message);
            long result = publication.offer(quoteBuffer);  // Send the quote

            if (result < 0) {
                log("Failed to send Quote, result: " + result, processNumber);
            } else {
                log("Quote sent successfully, result: " + result, processNumber);
            }

            try {
                Thread.sleep(10);  // Sleep to simulate periodic publishing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void log(String message, int processNumber) {
        String timestamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
        System.out.printf("[%s] [Publisher-%d] %s%n", timestamp, processNumber, message);
    }
}
