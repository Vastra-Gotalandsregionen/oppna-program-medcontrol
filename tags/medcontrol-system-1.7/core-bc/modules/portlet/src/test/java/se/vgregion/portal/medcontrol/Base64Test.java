package se.vgregion.portal.medcontrol;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * Created: 2012-01-05 09:23
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class Base64Test {

    @Test
    public void encode_byte_decode() {
        List<String> dataList = Arrays.asList("a", "ab", "abc", "abcd", "abcde", "abcdef", "abcdefg", "abcdefgh");

        List<byte[]> encoded = new ArrayList<byte[]>();
        for (String data : dataList) {
            encoded.add(Base64.encodeBase64URLSafe(data.getBytes()));
        }

        List<String> decoded = new ArrayList<String>();
        for (byte[] encData : encoded) {
            decoded.add(new String(Base64.decodeBase64(encData)));
        }

        for (int i=0; i<dataList.size(); i++) {
            assertEquals(dataList.get(i), decoded.get(i));
        }
    }

    @Test
    public void encode_String_decode() {
        List<String> dataList = Arrays.asList("a", "ab", "abc", "abcd", "abcde", "abcdef", "abcdefg", "abcdefgh");

        List<String> encoded = new ArrayList<String>();
        for (String data : dataList) {
            String enc = Base64.encodeBase64URLSafeString(data.getBytes());
            System.out.println(enc);
            encoded.add(enc);
        }

        List<String> decoded = new ArrayList<String>();
        for (String encData : encoded) {
            decoded.add(new String(Base64.decodeBase64(encData)));
        }

        for (int i=0; i<dataList.size(); i++) {
            assertEquals(dataList.get(i), decoded.get(i));
        }
    }

    @Test
    public void data() throws UnsupportedEncodingException {
        String data = "http://medcontrol.vgregion.se/medcontrol/Document" +
                ".aspx?type=VGR_Deviation&iid=809a353d-4aeb-4398-bbd8-cad389aef618&page=Context";
        String enc = Base64.encodeBase64URLSafeString(data.getBytes("UTF-8"));
        System.out.println(enc);
        String decode = new String(Base64.decodeBase64(enc));
        System.out.println(decode);
        assertEquals(data, decode);
    }
}
