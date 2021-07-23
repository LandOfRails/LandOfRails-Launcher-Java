package net.landofrails.logindata.encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class EncryptAES256 implements IEncryptor {

    public static final String SECRETKEYFACTORYALGORITHM = "PBKDF2WithHmacSHA256";
    public static final String SECRETKEYSPECALGORITHM = "AES";
    public static final String CIPHERPADDING = "AES/CBC/PKCS5PADDING";

    @Override
    public String encrypt(String text, String secret_key, String salt) throws ExEncryptionException {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRETKEYFACTORYALGORITHM);
            KeySpec spec = new PBEKeySpec(secret_key.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), SECRETKEYSPECALGORITHM);

            Cipher cipher = Cipher.getInstance(CIPHERPADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new SecureRandom());

            byte[] encryptedBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            byte[] iv = cipher.getIV();
            byte[] combinedPayload = new byte[encryptedBytes.length + iv.length];

            System.arraycopy(iv, 0, combinedPayload, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, combinedPayload, iv.length, encryptedBytes.length);

            return Base64.getEncoder()
                    .withoutPadding().encodeToString(combinedPayload);
        } catch (Exception e) {
            throw new ExEncryptionException(e);
        }
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

}
