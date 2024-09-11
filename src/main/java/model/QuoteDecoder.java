/* Generated SBE (Simple Binary Encoding) message codec. */
package model;

import org.agrona.DirectBuffer;


/**
 * Market Quote
 */
@SuppressWarnings("all")
public final class QuoteDecoder
{
    public static final int BLOCK_LENGTH = 36;
    public static final int TEMPLATE_ID = 1;
    public static final int SCHEMA_ID = 91;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final QuoteDecoder parentMessage = this;
    private DirectBuffer buffer;
    private int offset;
    private int limit;
    int actingBlockLength;
    int actingVersion;

    public int sbeBlockLength()
    {
        return BLOCK_LENGTH;
    }

    public int sbeTemplateId()
    {
        return TEMPLATE_ID;
    }

    public int sbeSchemaId()
    {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion()
    {
        return SCHEMA_VERSION;
    }

    public String sbeSemanticType()
    {
        return "";
    }

    public DirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public QuoteDecoder wrap(
        final DirectBuffer buffer,
        final int offset,
        final int actingBlockLength,
        final int actingVersion)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;
        this.actingBlockLength = actingBlockLength;
        this.actingVersion = actingVersion;
        limit(offset + actingBlockLength);

        return this;
    }

    public QuoteDecoder wrapAndApplyHeader(
        final DirectBuffer buffer,
        final int offset,
        final MessageHeaderDecoder headerDecoder)
    {
        headerDecoder.wrap(buffer, offset);

        final int templateId = headerDecoder.templateId();
        if (TEMPLATE_ID != templateId)
        {
            throw new IllegalStateException("Invalid TEMPLATE_ID: " + templateId);
        }

        return wrap(
            buffer,
            offset + MessageHeaderDecoder.ENCODED_LENGTH,
            headerDecoder.blockLength(),
            headerDecoder.version());
    }

    public QuoteDecoder sbeRewind()
    {
        return wrap(buffer, offset, actingBlockLength, actingVersion);
    }

    public int sbeDecodedLength()
    {
        final int currentLimit = limit();
        sbeSkip();
        final int decodedLength = encodedLength();
        limit(currentLimit);

        return decodedLength;
    }

    public int actingVersion()
    {
        return actingVersion;
    }

    public int encodedLength()
    {
        return limit - offset;
    }

    public int limit()
    {
        return limit;
    }

    public void limit(final int limit)
    {
        this.limit = limit;
    }

    public static int publisherIdId()
    {
        return 1;
    }

    public static int publisherIdSinceVersion()
    {
        return 0;
    }

    public static int publisherIdEncodingOffset()
    {
        return 0;
    }

    public static int publisherIdEncodingLength()
    {
        return 8;
    }

    public static String publisherIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static long publisherIdNullValue()
    {
        return -9223372036854775808L;
    }

    public static long publisherIdMinValue()
    {
        return -9223372036854775807L;
    }

    public static long publisherIdMaxValue()
    {
        return 9223372036854775807L;
    }

    public long publisherId()
    {
        return buffer.getLong(offset + 0, BYTE_ORDER);
    }


    public static int timestampId()
    {
        return 2;
    }

    public static int timestampSinceVersion()
    {
        return 0;
    }

    public static int timestampEncodingOffset()
    {
        return 8;
    }

    public static int timestampEncodingLength()
    {
        return 8;
    }

    public static String timestampMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static long timestampNullValue()
    {
        return -9223372036854775808L;
    }

    public static long timestampMinValue()
    {
        return -9223372036854775807L;
    }

    public static long timestampMaxValue()
    {
        return 9223372036854775807L;
    }

    public long timestamp()
    {
        return buffer.getLong(offset + 8, BYTE_ORDER);
    }


    public static int symbolId()
    {
        return 3;
    }

    public static int symbolSinceVersion()
    {
        return 0;
    }

    public static int symbolEncodingOffset()
    {
        return 16;
    }

    public static int symbolEncodingLength()
    {
        return 1;
    }

    public static String symbolMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte symbolNullValue()
    {
        return (byte)0;
    }

    public static byte symbolMinValue()
    {
        return (byte)32;
    }

    public static byte symbolMaxValue()
    {
        return (byte)126;
    }

    public byte symbol()
    {
        return buffer.getByte(offset + 16);
    }


    public static int isIndicativeId()
    {
        return 4;
    }

    public static int isIndicativeSinceVersion()
    {
        return 0;
    }

    public static int isIndicativeEncodingOffset()
    {
        return 17;
    }

    public static int isIndicativeEncodingLength()
    {
        return 1;
    }

    public static String isIndicativeMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static short isIndicativeNullValue()
    {
        return (short)255;
    }

    public static short isIndicativeMinValue()
    {
        return (short)0;
    }

    public static short isIndicativeMaxValue()
    {
        return (short)254;
    }

    public short isIndicative()
    {
        return ((short)(buffer.getByte(offset + 17) & 0xFF));
    }


    public static int bidSizeId()
    {
        return 5;
    }

    public static int bidSizeSinceVersion()
    {
        return 0;
    }

    public static int bidSizeEncodingOffset()
    {
        return 18;
    }

    public static int bidSizeEncodingLength()
    {
        return 4;
    }

    public static String bidSizeMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static int bidSizeNullValue()
    {
        return -2147483648;
    }

    public static int bidSizeMinValue()
    {
        return -2147483647;
    }

    public static int bidSizeMaxValue()
    {
        return 2147483647;
    }

    public int bidSize()
    {
        return buffer.getInt(offset + 18, BYTE_ORDER);
    }


    public static int bidPriceId()
    {
        return 6;
    }

    public static int bidPriceSinceVersion()
    {
        return 0;
    }

    public static int bidPriceEncodingOffset()
    {
        return 22;
    }

    public static int bidPriceEncodingLength()
    {
        return 4;
    }

    public static String bidPriceMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static float bidPriceNullValue()
    {
        return Float.NaN;
    }

    public static float bidPriceMinValue()
    {
        return -3.4028234663852886E38f;
    }

    public static float bidPriceMaxValue()
    {
        return 3.4028234663852886E38f;
    }

    public float bidPrice()
    {
        return buffer.getFloat(offset + 22, BYTE_ORDER);
    }


    public static int askPriceId()
    {
        return 7;
    }

    public static int askPriceSinceVersion()
    {
        return 0;
    }

    public static int askPriceEncodingOffset()
    {
        return 26;
    }

    public static int askPriceEncodingLength()
    {
        return 4;
    }

    public static String askPriceMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static float askPriceNullValue()
    {
        return Float.NaN;
    }

    public static float askPriceMinValue()
    {
        return -3.4028234663852886E38f;
    }

    public static float askPriceMaxValue()
    {
        return 3.4028234663852886E38f;
    }

    public float askPrice()
    {
        return buffer.getFloat(offset + 26, BYTE_ORDER);
    }


    public static int askSizeId()
    {
        return 8;
    }

    public static int askSizeSinceVersion()
    {
        return 0;
    }

    public static int askSizeEncodingOffset()
    {
        return 30;
    }

    public static int askSizeEncodingLength()
    {
        return 4;
    }

    public static String askSizeMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static int askSizeNullValue()
    {
        return -2147483648;
    }

    public static int askSizeMinValue()
    {
        return -2147483647;
    }

    public static int askSizeMaxValue()
    {
        return 2147483647;
    }

    public int askSize()
    {
        return buffer.getInt(offset + 30, BYTE_ORDER);
    }


    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        final QuoteDecoder decoder = new QuoteDecoder();
        decoder.wrap(buffer, offset, actingBlockLength, actingVersion);

        return decoder.appendTo(new StringBuilder()).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        if (null == buffer)
        {
            return builder;
        }

        final int originalLimit = limit();
        limit(offset + actingBlockLength);
        builder.append("[Quote](sbeTemplateId=");
        builder.append(TEMPLATE_ID);
        builder.append("|sbeSchemaId=");
        builder.append(SCHEMA_ID);
        builder.append("|sbeSchemaVersion=");
        if (parentMessage.actingVersion != SCHEMA_VERSION)
        {
            builder.append(parentMessage.actingVersion);
            builder.append('/');
        }
        builder.append(SCHEMA_VERSION);
        builder.append("|sbeBlockLength=");
        if (actingBlockLength != BLOCK_LENGTH)
        {
            builder.append(actingBlockLength);
            builder.append('/');
        }
        builder.append(BLOCK_LENGTH);
        builder.append("):");
        builder.append("publisherId=");
        builder.append(this.publisherId());
        builder.append('|');
        builder.append("timestamp=");
        builder.append(this.timestamp());
        builder.append('|');
        builder.append("symbol=");
        builder.append(this.symbol());
        builder.append('|');
        builder.append("isIndicative=");
        builder.append(this.isIndicative());
        builder.append('|');
        builder.append("bidSize=");
        builder.append(this.bidSize());
        builder.append('|');
        builder.append("bidPrice=");
        builder.append(this.bidPrice());
        builder.append('|');
        builder.append("askPrice=");
        builder.append(this.askPrice());
        builder.append('|');
        builder.append("askSize=");
        builder.append(this.askSize());

        limit(originalLimit);

        return builder;
    }
    
    public QuoteDecoder sbeSkip()
    {
        sbeRewind();

        return this;
    }
}
