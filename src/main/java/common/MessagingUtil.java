package common;

/**
 * Utility class for messaging constants used in the Aeron messaging system.
 */
public class MessagingUtil {
    // Use IPC for local inter-process communication
    public static final String IPC_CHANNEL = "aeron:ipc?term-length=64m";

    public static final int QUOTE_STREAM_ID = 10;
    public static final int ORDER_STREAM_ID = 20;
    public static final int ORDER_RESPONSE_STREAM_ID = 30;


    public static final String AERON_DIRECTORY = "src/main/resources/aeron_directory.txt";
}
