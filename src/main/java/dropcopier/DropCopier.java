package dropcopier;

import common.MessagingUtil;
import io.aeron.Aeron;
import io.aeron.Subscription;
import io.aeron.logbuffer.FragmentHandler;
import model.MessageHeaderDecoder;
import model.OrderResponseDecoder;
import model.QuoteDecoder;
import org.agrona.DirectBuffer;

public class DropCopier implements Runnable {
    private final Aeron aeron;
    private final Subscription quoteSubscription;
    private final Subscription orderResponseSubscription;
    private final MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();
    private final QuoteDecoder quoteDecoder = new QuoteDecoder();
    private final OrderResponseDecoder orderResponseDecoder = new OrderResponseDecoder();
    private volatile boolean running = true;

    public DropCopier(Aeron aeron) {
        this.aeron = aeron;
        this.quoteSubscription = aeron.addSubscription(MessagingUtil.QUOTE_CHANNEL, MessagingUtil.QUOTE_STREAM_ID);
        this.orderResponseSubscription = aeron.addSubscription(MessagingUtil.ORDER_RESPONSE_CHANNEL, MessagingUtil.ORDER_RESPONSE_STREAM_ID);
        log("DropCopier initialized.");
    }

    @Override
    public void run() {
        FragmentHandler fragmentHandler = this::onMessageReceived;

        while (running) {
            quoteSubscription.poll(fragmentHandler, 10);
            orderResponseSubscription.poll(fragmentHandler, 10);

            // Sleep briefly to prevent tight loop
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void onMessageReceived(DirectBuffer buffer, int offset, int length, io.aeron.logbuffer.Header header) {
        messageHeaderDecoder.wrap(buffer, offset);
        int templateId = messageHeaderDecoder.templateId();
        offset += messageHeaderDecoder.encodedLength();

        if (templateId == QuoteDecoder.TEMPLATE_ID) {
            onQuoteReceived(buffer, offset);
        } else if (templateId == OrderResponseDecoder.TEMPLATE_ID) {
            onOrderResponseReceived(buffer, offset);
        } else {
            log("Unknown templateId: " + templateId);
        }
    }

    private void onQuoteReceived(DirectBuffer buffer, int offset) {
        quoteDecoder.wrap(buffer, offset, quoteDecoder.sbeBlockLength(), quoteDecoder.sbeSchemaVersion());

        // Process the quote if necessary
        log("Quote received.");
    }

    private void onOrderResponseReceived(DirectBuffer buffer, int offset) {
        long receiveTime = System.nanoTime();

        orderResponseDecoder.wrap(buffer, offset, orderResponseDecoder.sbeBlockLength(), orderResponseDecoder.sbeSchemaVersion());

        long sentTime = orderResponseDecoder.timestamp();

        long latency = receiveTime - sentTime;

        log(String.format("OrderResponse received: ClOrdId=%d with latency: %d microseconds", orderResponseDecoder.clOrdId(), latency / 1000));  // Convert to microseconds
    }

    public void stop() {
        running = false;
    }

    private static void log(String message) {
        System.out.println("[DropCopier] " + message);
    }
}
