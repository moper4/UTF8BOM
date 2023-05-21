package dzy.util;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Utf8WithBomProvider extends CharsetProvider {
    private static final Charset CHARSET = new Utf8WithBom();
    private static final List<Charset> CHARSETS = List.of(CHARSET);
    private static final Set<String> CHARSETS_NAMES = Set.of(Utf8WithBom.ALIASES);

    @Override
    public Iterator<Charset> charsets() {
        return CHARSETS.iterator();
    }

    @Override
    public Charset charsetForName(String charsetName) {
        return CHARSETS_NAMES.contains(charsetName) ? CHARSET : null;
    }
}
