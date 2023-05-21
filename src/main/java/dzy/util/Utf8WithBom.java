package dzy.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;

public class Utf8WithBom extends Charset {
    public static final String CANONICAL_NAME = "UTF-8+BOM";
    public static final String[] ALIASES = new String[]{"utf-8+bom", "utf8bom", "UTF8BOM"};

    protected Utf8WithBom() {
        super(CANONICAL_NAME, ALIASES);
    }

    @Override
    public boolean contains(Charset cs) {
        return cs instanceof Utf8WithBom || StandardCharsets.UTF_8.contains(cs);
    }

    @Override
    public boolean canEncode() {
        return false;
    }

    @Override
    public CharsetDecoder newDecoder() {
        var decoder = StandardCharsets.UTF_8.newDecoder();
        return new CharsetDecoder(this, decoder.averageCharsPerByte(), decoder.maxCharsPerByte()) {
            private boolean findBom = false;

            @Override
            protected CoderResult decodeLoop(ByteBuffer in, CharBuffer out) {
                if (findBom) {
                    return decoder.decode(in, out, false);
                } else if (in.remaining() < 3) {
                    return CoderResult.UNDERFLOW;
                } else {
                    var a = in.get(0) & 0xFF;
                    var b = in.get(1) & 0xFF;
                    var c = in.get(2) & 0xFF;
                    if (a == 0xEF && b == 0xBB && c == 0xBF) {
                        findBom = true;
                        in.position(3);
                        return decoder.decode(in, out, false);
                    } else {
                        return CoderResult.malformedForLength(3);
                    }
                }
            }

            @Override
            protected void implReset() {
                findBom = false;
            }
        };
    }

    @Override
    public CharsetEncoder newEncoder() {
        throw new UnsupportedOperationException();
    }
}
