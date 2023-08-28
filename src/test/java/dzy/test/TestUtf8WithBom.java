package dzy.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class TestUtf8WithBom {
    private Charset charset;
    private String[] strings;

    @Before
    public void setup() {
        charset = Charset.forName("UTF8BOM");
        strings = new String[]{"", "Aa", "啊", "Hello 世界!"};
        Charset.availableCharsets();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEncode() {
        Assert.assertFalse(charset.canEncode());
        charset.newEncoder();
        Assert.fail("should not be hear");
    }

    @Test
    public void testContains() {
        Assert.assertTrue(charset.contains(StandardCharsets.UTF_8));
        Assert.assertTrue(charset.contains(StandardCharsets.ISO_8859_1));
    }

    @Test
    public void testBadDecode() {
        var s = new String("A".getBytes(StandardCharsets.UTF_8), charset);
        System.out.println(s);
        Assert.assertNotEquals("A", s);
    }

    @Test
    public void testDecodeArray() {
        for (var string : strings) {
            var bytes = addBom(string.getBytes(StandardCharsets.UTF_8));
            Assert.assertEquals(string, readByArray(bytes));
        }
    }

    @Test
    public void testDecodeStream() throws Exception {
        for (var string : strings) {
            var bytes = addBom(string.getBytes(StandardCharsets.UTF_8));
            Assert.assertEquals(string, readByStream(bytes));
        }
    }

    private byte[] addBom(byte[] b) {
        var bytes = new byte[b.length + 3];
        bytes[0] = (byte) 0xEF;
        bytes[1] = (byte) 0xBB;
        bytes[2] = (byte) 0xBF;
        System.arraycopy(b, 0, bytes, 3, b.length);
        return bytes;
    }

    private String readByArray(byte[] b) {
        return new String(b, charset);
    }

    private String readByStream(byte[] b) throws Exception {
        try (var reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(b), charset))) {
            return reader.lines().collect(Collectors.joining());
        }
    }
}
