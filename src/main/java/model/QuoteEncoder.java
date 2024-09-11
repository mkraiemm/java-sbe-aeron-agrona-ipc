/* Generated SBE (Simple Binary Encoding) message codec. */
package model;

import org.agrona.MutableDirectBuffer;


/**
 * Market Quote
 */
@SuppressWarnings("all")
public final class QuoteEncoder
{
    public static final int BLOCK_LENGTH = 36;
    public static final int TEMPLATE_ID = 1;
    public static final int SCHEMA_ID = 91;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final QuoteEncoder parentMessage = this;
    private MutableDirectBuffer buffer;
    private int offset;
    private int limit;

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

    public MutableDirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public QuoteEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public QuoteEncoder wrapAndApplyHeader(
        final MutableDirectBuffer buffer, final int offset, final MessageHeaderEncoder headerEncoder)
    {
        headerEncoder
            .wrap(buffer, offset)
            .blockLength(BLOCK_LENGTH)
            .templateId(TEMPLATE_ID)
            .schemaId(SCHEMA_ID)
            .version(SCHEMA_VERSION);

        return wrap(buffer, offset + MessageHeaderEncoder.ENCODED_LENGTH);
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

    public QuoteEncoder publisherId(final long value)
    {
        buffer.putLong(offset + 0, value, BYTE_ORDER);
        return this;
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

    public QuoteEncoder timestamp(final long value)
    {
        buffer.putLong(offset + 8, value, BYTE_ORDER);
        return this;
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

    public QuoteEncoder symbol(final byte value)
    {
        buffer.putByte(offset + 16, value);
        return this;
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

    public QuoteEncoder isIndicative(final short value)
    {
        buffer.putByte(offset + 17, (byte)value);
        return this;
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

    public QuoteEncoder bidSize(final int value)
    {
        buffer.putInt(offset + 18, value, BYTE_ORDER);
        return this;
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

    public QuoteEncoder bidPrice(final float value)
    {
        buffer.putFloat(offset + 22, value, BYTE_ORDER);
        return this;
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

    public QuoteEncoder askPrice(final float value)
    {
        buffer.putFloat(offset + 26, value, BYTE_ORDER);
        return this;
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

    public QuoteEncoder askSize(final int value)
    {
        buffer.putInt(offset + 30, value, BYTE_ORDER);
        return this;
    }


    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        return appendTo(new StringBuilder()).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        if (null == buffer)
        {
            return builder;
        }

        final QuoteDecoder decoder = new QuoteDecoder();
        decoder.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return decoder.appendTo(builder);
    }
}
