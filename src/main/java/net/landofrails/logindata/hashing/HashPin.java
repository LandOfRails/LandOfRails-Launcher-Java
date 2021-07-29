package net.landofrails.logindata.hashing;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class HashPin {

    private static final String HASHALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int SALTSIZE = 16;

    public static byte[] generateSalt() {
        byte[] salt = new byte[SALTSIZE];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    public static byte[] generateSaltyHashFromPinAndSalt(final String pin, final byte[] salt) {

        try {

            KeySpec spec = new PBEKeySpec(pin.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(HASHALGORITHM);

            byte[] hash = factory.generateSecret(spec).getEncoded();

            byte[] saltyHash = new byte[SALTSIZE + hash.length];
            System.arraycopy(salt, 0, saltyHash, 0, SALTSIZE);
            System.arraycopy(hash, 0, saltyHash, SALTSIZE, hash.length);

            return saltyHash;

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] getSaltFromSaltyHash(final byte[] saltyHash) {

        byte[] salt = new byte[SALTSIZE];
        System.arraycopy(saltyHash, 0, salt, 0, SALTSIZE);

        return salt;
    }

}
