package processor;

import common.MessagingUtil;
import io.aeron.Aeron;
import io.aeron.Subscription;
import io.aeron.logbuffer.Header;
import model.MessageHeaderDecoder;
import model.QuoteDecoder;
import org.agrona.DirectBuffer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Processor implements Runnable {
    private final Aeron aeron;
    private final Subscription quoteSubscription;
    private final int processNumber;
    private final MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();  // SBE MessageHeader decoder
    private final QuoteDecoder quoteDecoder = new QuoteDecoder();  // SBE Quote decoder

    public Processor(Aeron aeron, int processNumber) {
        this.aeron = aeron;
        this.quoteSubscription = aeron.addSubscription(MessagingUtil.IPC_CHANNEL, MessagingUtil.QUOTE_STREAM_ID);
        this.processNumber = processNumber;
        log("Processor initialized.", processNumber);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            quoteSubscription.poll(this::onMessageReceived, 100);  // Poll for messages
        }
    }

    private void onMessageReceived(DirectBuffer buffer, int offset, int length, Header header) {
        // Decode the message header
        messageHeaderDecoder.wrap(buffer, offset);
        int templateId = messageHeaderDecoder.templateId();
        log("Received Template ID: " + templateId, processNumber);

        // Move the offset after the message header
        offset += messageHeaderDecoder.encodedLength();

        if (templateId == QuoteDecoder.TEMPLATE_ID) {
            quoteDecoder.wrap(buffer, offset, messageHeaderDecoder.blockLength(), messageHeaderDecoder.version());

            // Extract fields
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
        } else {
            log("Unknown template ID: " + templateId, processNumber);
        }
    }


    public static void log(String message, int processNumber) {
        String timestamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
        System.out.printf("[%s] [Processor-%d] %s%n", timestamp, processNumber, message);
    }
}
