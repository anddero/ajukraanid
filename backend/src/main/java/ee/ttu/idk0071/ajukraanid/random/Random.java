package ee.ttu.idk0071.ajukraanid.random;

import java.util.Base64;

public class Random extends java.util.Random {
    public String nextBase64UrlSafeString(int byteCount) {
        byte[] bytes = new byte[byteCount];
        super.nextBytes(bytes);
        return Base64.getUrlEncoder().encodeToString(bytes);
    }
}
