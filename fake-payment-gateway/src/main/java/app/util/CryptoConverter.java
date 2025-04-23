package app.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Optional;

@Slf4j
public class CryptoConverter {
    private static final String ENC = "AES";
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    public static Optional<String> convertToDatabaseColumn(String attribute, String secretKey) {
        String value = null;
        try {
            Key key = new SecretKeySpec(secretKey.getBytes(), ENC);
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            value = Base64.getEncoder().encodeToString(c.doFinal(attribute.getBytes()));

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return Optional.ofNullable(value);
    }

    public static Optional<String> convertToEntityAttribute(String dbData, String secretKey) {
        String value = null;
        try {
            Key key = new SecretKeySpec(secretKey.getBytes(), ENC);
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            value = new String(c.doFinal(Base64.getDecoder().decode(dbData)));

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return Optional.ofNullable(value);
    }
}
