import dzy.util.Utf8WithBomProvider;

import java.nio.charset.spi.CharsetProvider;

module dzy.util.charset {
    provides CharsetProvider with Utf8WithBomProvider;
}