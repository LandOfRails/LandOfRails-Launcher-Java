package net.landofrails.logindata.decryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

public class DecryptAES256 implements IDecryptor {

    public static final String SECRETKEYFACTORYALGORITHM = "PBKDF2WithHmacSHA256";
    public static final String SECRETKEYSPECALGORITHM = "AES";
    public static final String CIPHERPADDING = "AES/CBC/PKCS5PADDING";

    @Override
    public String decrypt(String text, String secret_key, String salt) throws ExDecryptionException {
        try {

            byte[] combinedPayload = Base64.getDecoder().decode(text);
            byte[] iv = new byte[16];
            byte[] encryptedBytes = new byte[combinedPayload.length - iv.length];

            System.arraycopy(combinedPayload, 0, iv, 0, iv.length);
            System.arraycopy(combinedPayload, iv.length, encryptedBytes, 0, encryptedBytes.length);

            SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRETKEYFACTORYALGORITHM);
            KeySpec spec = new PBEKeySpec(secret_key.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), SECRETKEYSPECALGORITHM);

            Cipher cipher = Cipher.getInstance(CIPHERPADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

            return new String(cipher.doFinal(encryptedBytes));
        } catch (Exception e) {
            throw new ExDecryptionException(e);
        }
    }

}
