package publisher;
import common.MessagingUtil;
import io.aeron.Aeron;
import io.aeron.Publication;
import model.MessageHeaderEncoder;  // Import the SBE MessageHeader encoder
import model.QuoteEncoder;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.charset.StandardCharsets;
import java.util.Random;

import static processor.Processor.log;

public class Publisher implements Runnable {
    private final Aeron aeron;
    private final Publication publication;
    private final int processNumber;
    private final Random random = new Random();
    private UnsafeBuffer quoteBuffer = new UnsafeBuffer(new byte[256]); // Buffer for encoding SBE messages
    private final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder(); // SBE MessageHeader encoder
    private final QuoteEncoder quoteEncoder = new QuoteEncoder();  // SBE encoder


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
            double bidPrice = 100.00 + (120.00 - 100.00) * random.nextDouble();
            int bidSize = 100 + random.nextInt(901);
            double askPrice = 101.00 + (120.00 - 100.00) * random.nextDouble();
            int askSize = 100 + random.nextInt(901);

            long timestamp = System.nanoTime();  // Capture the current time in nanoseconds

            // Encode message header
            messageHeaderEncoder.wrap(quoteBuffer, 0)
                    .blockLength(quoteEncoder.sbeBlockLength())
                    .templateId(quoteEncoder.sbeTemplateId())
                    .schemaId(quoteEncoder.sbeSchemaId())
                    .version(quoteEncoder.sbeSchemaVersion());

            // Begin encoding the Quote message after the header
            int headerLength = messageHeaderEncoder.encodedLength();
            quoteEncoder.wrap(quoteBuffer, headerLength); // Wrap buffer after the header
            quoteEncoder.publisherId(processNumber)
                    .timestamp(timestamp)
                    .symbol(symbol.getBytes(StandardCharsets.US_ASCII)[0]) // Use the first byte of the array
                    .isIndicative((short) (false ? 1 : 0)) // Convert boolean to short
                    .bidPrice((float) bidPrice)
                    .bidSize(bidSize)
                    .askPrice((float) askPrice)
                    .askSize(askSize);

            // Send the encoded message over Aeron
            long result = publication.offer(quoteBuffer, 0, headerLength + quoteEncoder.encodedLength());
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
}
