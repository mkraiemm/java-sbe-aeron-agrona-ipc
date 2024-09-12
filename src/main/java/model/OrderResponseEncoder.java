/* Generated SBE (Simple Binary Encoding) message codec. */
package model;

import org.agrona.MutableDirectBuffer;


/**
 * Order Response
 */
@SuppressWarnings("all")
public final class OrderResponseEncoder
{
    public static final int BLOCK_LENGTH = 42;
    public static final int TEMPLATE_ID = 3;
    public static final int SCHEMA_ID = 91;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final OrderResponseEncoder parentMessage = this;
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

    public OrderResponseEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public OrderResponseEncoder wrapAndApplyHeader(
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

    public OrderResponseEncoder publisherId(final long value)
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

    public OrderResponseEncoder timestamp(final long value)
    {
        buffer.putLong(offset + 8, value, BYTE_ORDER);
        return this;
    }


    public static int clOrdIdId()
    {
        return 3;
    }

    public static int clOrdIdSinceVersion()
    {
        return 0;
    }

    public static int clOrdIdEncodingOffset()
    {
        return 16;
    }

    public static int clOrdIdEncodingLength()
    {
        return 8;
    }

    public static String clOrdIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static long clOrdIdNullValue()
    {
        return -9223372036854775808L;
    }

    public static long clOrdIdMinValue()
    {
        return -9223372036854775807L;
    }

    public static long clOrdIdMaxValue()
    {
        return 9223372036854775807L;
    }

    public OrderResponseEncoder clOrdId(final long value)
    {
        buffer.putLong(offset + 16, value, BYTE_ORDER);
        return this;
    }


    public static int sideId()
    {
        return 4;
    }

    public static int sideSinceVersion()
    {
        return 0;
    }

    public static int sideEncodingOffset()
    {
        return 24;
    }

    public static int sideEncodingLength()
    {
        return 1;
    }

    public static String sideMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte sideNullValue()
    {
        return (byte)0;
    }

    public static byte sideMinValue()
    {
        return (byte)32;
    }

    public static byte sideMaxValue()
    {
        return (byte)126;
    }

    public OrderResponseEncoder side(final byte value)
    {
        buffer.putByte(offset + 24, value);
        return this;
    }


    public static int quantityId()
    {
        return 5;
    }

    public static int quantitySinceVersion()
    {
        return 0;
    }

    public static int quantityEncodingOffset()
    {
        return 25;
    }

    public static int quantityEncodingLength()
    {
        return 4;
    }

    public static String quantityMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static int quantityNullValue()
    {
        return -2147483648;
    }

    public static int quantityMinValue()
    {
        return -2147483647;
    }

    public static int quantityMaxValue()
    {
        return 2147483647;
    }

    public OrderResponseEncoder quantity(final int value)
    {
        buffer.putInt(offset + 25, value, BYTE_ORDER);
        return this;
    }


    public static int priceId()
    {
        return 6;
    }

    public static int priceSinceVersion()
    {
        return 0;
    }

    public static int priceEncodingOffset()
    {
        return 29;
    }

    public static int priceEncodingLength()
    {
        return 4;
    }

    public static String priceMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static float priceNullValue()
    {
        return Float.NaN;
    }

    public static float priceMinValue()
    {
        return -3.4028234663852886E38f;
    }

    public static float priceMaxValue()
    {
        return 3.4028234663852886E38f;
    }

    public OrderResponseEncoder price(final float value)
    {
        buffer.putFloat(offset + 29, value, BYTE_ORDER);
        return this;
    }


    public static int filledQuantityId()
    {
        return 7;
    }

    public static int filledQuantitySinceVersion()
    {
        return 0;
    }

    public static int filledQuantityEncodingOffset()
    {
        return 33;
    }

    public static int filledQuantityEncodingLength()
    {
        return 4;
    }

    public static String filledQuantityMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static int filledQuantityNullValue()
    {
        return -2147483648;
    }

    public static int filledQuantityMinValue()
    {
        return -2147483647;
    }

    public static int filledQuantityMaxValue()
    {
        return 2147483647;
    }

    public OrderResponseEncoder filledQuantity(final int value)
    {
        buffer.putInt(offset + 33, value, BYTE_ORDER);
        return this;
    }


    public static int filledPriceId()
    {
        return 8;
    }

    public static int filledPriceSinceVersion()
    {
        return 0;
    }

    public static int filledPriceEncodingOffset()
    {
        return 37;
    }

    public static int filledPriceEncodingLength()
    {
        return 4;
    }

    public static String filledPriceMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static float filledPriceNullValue()
    {
        return Float.NaN;
    }

    public static float filledPriceMinValue()
    {
        return -3.4028234663852886E38f;
    }

    public static float filledPriceMaxValue()
    {
        return 3.4028234663852886E38f;
    }

    public OrderResponseEncoder filledPrice(final float value)
    {
        buffer.putFloat(offset + 37, value, BYTE_ORDER);
        return this;
    }


    public static int statusId()
    {
        return 9;
    }

    public static int statusSinceVersion()
    {
        return 0;
    }

    public static int statusEncodingOffset()
    {
        return 41;
    }

    public static int statusEncodingLength()
    {
        return 1;
    }

    public static String statusMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte statusNullValue()
    {
        return (byte)0;
    }

    public static byte statusMinValue()
    {
        return (byte)32;
    }

    public static byte statusMaxValue()
    {
        return (byte)126;
    }

    public OrderResponseEncoder status(final byte value)
    {
        buffer.putByte(offset + 41, value);
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

        final OrderResponseDecoder decoder = new OrderResponseDecoder();
        decoder.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return decoder.appendTo(builder);
    }
}
