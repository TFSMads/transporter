package ml.volder.unikapi.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;

public class IOUtils {
    public static String toString(InputStream input, Charset encoding) throws IOException {
        StringBuilderWriter sw = new StringBuilderWriter();
        Throwable var3 = null;

        String var4;
        try {
            copy((InputStream)input, (Writer)sw, (Charset)encoding);
            var4 = sw.toString();
        } catch (Throwable var13) {
            var3 = var13;
            throw var13;
        } finally {
            if (sw != null) {
                if (var3 != null) {
                    try {
                        sw.close();
                    } catch (Throwable var12) {
                        var3.addSuppressed(var12);
                    }
                } else {
                    sw.close();
                }
            }

        }

        return var4;
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        return count > 2147483647L ? -1 : (int)count;
    }

    public static long copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        return copyLarge(input, output, new byte[bufferSize]);
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, 4096);
    }

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count;
        int n;
        for(count = 0L; -1 != (n = input.read(buffer)); count += (long)n) {
            output.write(buffer, 0, n);
        }

        return count;
    }

    public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length) throws IOException {
        return copyLarge(input, output, inputOffset, length, new byte[4096]);
    }

    public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length, byte[] buffer) throws IOException {
        if (inputOffset > 0L) {
            skipFully(input, inputOffset);
        }

        if (length == 0L) {
            return 0L;
        } else {
            int bufferLength = buffer.length;
            int bytesToRead = bufferLength;
            if (length > 0L && length < (long)bufferLength) {
                bytesToRead = (int)length;
            }

            long totalRead = 0L;

            int read;
            while(bytesToRead > 0 && -1 != (read = input.read(buffer, 0, bytesToRead))) {
                output.write(buffer, 0, read);
                totalRead += (long)read;
                if (length > 0L) {
                    bytesToRead = (int)Math.min(length - totalRead, (long)bufferLength);
                }
            }

            return totalRead;
        }
    }

    /** @deprecated */
    @Deprecated
    public static void copy(InputStream input, Writer output) throws IOException {
        copy(input, output, Charset.defaultCharset());
    }

    public static void copy(InputStream input, Writer output, Charset inputEncoding) throws IOException {
        InputStreamReader in = new InputStreamReader(input, Charsets.toCharset(inputEncoding));
        copy((Reader)in, (Writer)output);
    }

    public static void copy(InputStream input, Writer output, String inputEncoding) throws IOException {
        copy(input, output, Charsets.toCharset(inputEncoding));
    }

    public static int copy(Reader input, Writer output) throws IOException {
        long count = copyLarge(input, output);
        return count > 2147483647L ? -1 : (int)count;
    }

    public static long copyLarge(Reader input, Writer output) throws IOException {
        return copyLarge(input, output, new char[4096]);
    }

    public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
        long count;
        int n;
        for(count = 0L; -1 != (n = input.read(buffer)); count += (long)n) {
            output.write(buffer, 0, n);
        }

        return count;
    }

    public static long copyLarge(Reader input, Writer output, long inputOffset, long length) throws IOException {
        return copyLarge(input, output, inputOffset, length, new char[4096]);
    }

    public static long copyLarge(Reader input, Writer output, long inputOffset, long length, char[] buffer) throws IOException {
        if (inputOffset > 0L) {
            skipFully(input, inputOffset);
        }

        if (length == 0L) {
            return 0L;
        } else {
            int bytesToRead = buffer.length;
            if (length > 0L && length < (long)buffer.length) {
                bytesToRead = (int)length;
            }

            long totalRead = 0L;

            int read;
            while(bytesToRead > 0 && -1 != (read = input.read(buffer, 0, bytesToRead))) {
                output.write(buffer, 0, read);
                totalRead += (long)read;
                if (length > 0L) {
                    bytesToRead = (int)Math.min(length - totalRead, (long)buffer.length);
                }
            }

            return totalRead;
        }
    }

    /** @deprecated */
    @Deprecated
    public static void copy(Reader input, OutputStream output) throws IOException {
        copy(input, output, Charset.defaultCharset());
    }

    public static void copy(Reader input, OutputStream output, Charset outputEncoding) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(output, Charsets.toCharset(outputEncoding));
        copy((Reader)input, (Writer)out);
        out.flush();
    }

    private static char[] SKIP_CHAR_BUFFER;
    private static byte[] SKIP_BYTE_BUFFER;

    public static void copy(Reader input, OutputStream output, String outputEncoding) throws IOException {
        copy(input, output, Charsets.toCharset(outputEncoding));
    }

    public static long skip(InputStream input, long toSkip) throws IOException {
        if (toSkip < 0L) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        } else {
            if (SKIP_BYTE_BUFFER == null) {
                SKIP_BYTE_BUFFER = new byte[2048];
            }

            long remain;
            long n;
            for(remain = toSkip; remain > 0L; remain -= n) {
                n = (long)input.read(SKIP_BYTE_BUFFER, 0, (int)Math.min(remain, 2048L));
                if (n < 0L) {
                    break;
                }
            }

            return toSkip - remain;
        }
    }

    public static long skip(ReadableByteChannel input, long toSkip) throws IOException {
        if (toSkip < 0L) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        } else {
            ByteBuffer skipByteBuffer = ByteBuffer.allocate((int)Math.min(toSkip, 2048L));

            long remain;
            int n;
            for(remain = toSkip; remain > 0L; remain -= (long)n) {
                skipByteBuffer.position(0);
                skipByteBuffer.limit((int)Math.min(remain, 2048L));
                n = input.read(skipByteBuffer);
                if (n == -1) {
                    break;
                }
            }

            return toSkip - remain;
        }
    }

    public static long skip(Reader input, long toSkip) throws IOException {
        if (toSkip < 0L) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        } else {
            if (SKIP_CHAR_BUFFER == null) {
                SKIP_CHAR_BUFFER = new char[2048];
            }

            long remain;
            long n;
            for(remain = toSkip; remain > 0L; remain -= n) {
                n = (long)input.read(SKIP_CHAR_BUFFER, 0, (int)Math.min(remain, 2048L));
                if (n < 0L) {
                    break;
                }
            }

            return toSkip - remain;
        }
    }

    public static void skipFully(InputStream input, long toSkip) throws IOException {
        if (toSkip < 0L) {
            throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
        } else {
            long skipped = skip(input, toSkip);
            if (skipped != toSkip) {
                throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
            }
        }
    }

    public static void skipFully(ReadableByteChannel input, long toSkip) throws IOException {
        if (toSkip < 0L) {
            throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
        } else {
            long skipped = skip(input, toSkip);
            if (skipped != toSkip) {
                throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
            }
        }
    }

    public static void skipFully(Reader input, long toSkip) throws IOException {
        long skipped = skip(input, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Chars to skip: " + toSkip + " actual: " + skipped);
        }
    }

}
