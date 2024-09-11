package common;


import org.agrona.concurrent.IdGenerator;

/** 
 * ConcreteIdGenerator implements the IdGenerator interface
 */
public class ConcreteIdGenerator implements IdGenerator {
    // Create an instance of IdGenerator to generate IDs
    private final IdGenerator idGenerator = new IdGenerator() {
        @Override
        public long nextId() {
            // Generate a new ID based on the current system time in milliseconds
            return System.currentTimeMillis();
        }
    };

    // Override the nextId method to provide ID generation
    @Override
    public long nextId() {
        // Generate a new ID based on the current system time in milliseconds
        return System.currentTimeMillis();
    }
}
