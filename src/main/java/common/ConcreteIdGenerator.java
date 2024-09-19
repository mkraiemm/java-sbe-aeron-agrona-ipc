package common;

import org.agrona.concurrent.IdGenerator;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ConcreteIdGenerator generates unique IDs using an AtomicLong counter.
 */
public class ConcreteIdGenerator implements IdGenerator {
    private final AtomicLong counter = new AtomicLong();

    @Override
    public long nextId() {
        return counter.incrementAndGet();
    }
}
