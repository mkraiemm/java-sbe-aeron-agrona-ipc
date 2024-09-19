package common;

public class MessagingUtil {
    public static final String QUOTE_CHANNEL = "aeron:udp?endpoint=224.0.1.1:40123";
    public static final String ORDER_CHANNEL = "aeron:udp?endpoint=224.0.1.1:40124";
    public static final String ORDER_RESPONSE_CHANNEL = "aeron:udp?endpoint=224.0.1.1:40125";

    public static final int QUOTE_STREAM_ID = 1;
    public static final int ORDER_STREAM_ID = 2;
    public static final int ORDER_RESPONSE_STREAM_ID = 3;

    public static final String AERON_DIRECTORY = "src/main/resources/aeron_directory.txt";
}

