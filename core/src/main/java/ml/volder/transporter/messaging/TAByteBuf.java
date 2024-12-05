package ml.volder.transporter.messaging;

import io.netty.buffer.AbstractByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TAByteBuf extends UnpooledHeapByteBuf {

    private static final int MAX_LENGTH = 32767;
    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    protected TAByteBuf(ByteBufAllocator alloc, int initialCapacity, int maxCapacity) {
        super(alloc, initialCapacity, maxCapacity);
    }

    public static TAByteBuf create() {
        return create(256, Integer.MAX_VALUE);
    }

    public static TAByteBuf create(int initialCapacity) {
        return create(initialCapacity, Integer.MAX_VALUE);
    }

    public static TAByteBuf create(int initialCapacity, int maxCapacity) {
        validate(initialCapacity, maxCapacity);
        return new TAByteBuf(ByteBufAllocator.DEFAULT, initialCapacity, maxCapacity);
    }

    private static void validate(int initialCapacity, int maxCapacity) {
        if (maxCapacity == 0) {
            throw new IllegalArgumentException("maxCapacity: " + maxCapacity + " (expected greater than 0)");
        }
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("initialCapacity: " + initialCapacity + " (expectd: 0+)");
        }
        if (initialCapacity > maxCapacity) {
            throw new IllegalArgumentException(String.format(
                    "initialCapacity: %d (expected: not greater than maxCapacity(%d)",
                    initialCapacity, maxCapacity));
        }
    }

    public int readVarInt() {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = this.readByte();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        return value;
    }

    public long readVarLong() {
        long value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = readByte();
            value |= (long) (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 64) throw new RuntimeException("VarLong is too big");
        }

        return value;
    }

    public void writeVarInt(int value) {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                this.writeByte(value);
                return;
            }

            this.writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);

            value >>>= 7;
        }
    }

    public void writeVarLong(long value) {
        while (true) {
            if ((value & ~((long) SEGMENT_BITS)) == 0) {
                writeByte((int) value);
                return;
            }

            writeByte((int) ((value & SEGMENT_BITS) | CONTINUE_BIT));

            value >>>= 7;
        }
    }


    public String readUTF() {
        int stringByteLength = readVarInt();
        if (stringByteLength > MAX_LENGTH) {
            throw new IllegalArgumentException("The received encoded string buffer length is longer than maximum allowed (" + stringByteLength + " > " + MAX_LENGTH + ")");
        }

        if (stringByteLength < 0) {
            throw new IllegalArgumentException("The received encoded string buffer length is less than zero! Weird string!");
        }

        int remaining = this.readableBytes();
        if (stringByteLength > remaining) {
            throw new IllegalArgumentException("Not enough bytes in buffer, expected " + stringByteLength + ", but got " + remaining);
        }

        String decodedString = this.toString(this.readerIndex(), stringByteLength, StandardCharsets.UTF_8);
        this.skipBytes(stringByteLength);

        if (decodedString.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("The received string length is longer than maximum allowed (" + decodedString.length() + " > " + MAX_LENGTH + ")");
        }

        return decodedString;
    }

    public String[] readNullSeparatedUTF() {
        List<String> strings = new ArrayList<>();
        int startIndex = this.readerIndex();
        int length = 0;

        while (this.isReadable()) {
            byte b = this.readByte();

            if (b == 0x00) {
                String decodedString = this.toString(startIndex, length, StandardCharsets.UTF_8);
                strings.add(decodedString);
                startIndex = this.readerIndex();
                length = 0;
            } else {
                length++;
            }
        }

        return strings.toArray(new String[0]);
    }

    public void writeUTF(String val) {
        byte[] encodedByteArray = val.getBytes(StandardCharsets.UTF_8);
        if (encodedByteArray.length > MAX_LENGTH) {
            throw new IllegalArgumentException("String too big (was " + encodedByteArray.length + " bytes encoded, max " + MAX_LENGTH + ")");
        }

        writeVarInt(encodedByteArray.length);
        this.writeBytes(encodedByteArray);
    }

    public byte[] toByteArray() {
        this.resetReaderIndex();
        byte[] bytes = new byte[this.readableBytes()];
        this.readBytes(bytes);
        return bytes;
    }

}
