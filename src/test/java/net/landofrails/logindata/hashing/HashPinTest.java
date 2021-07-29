package net.landofrails.logindata.hashing;

import org.junit.Assert;
import org.junit.Test;

public class HashPinTest {

    @Test
    public void testHashMethod() {

        final String pin = "234978";
        final byte[] salt = HashPin.generateSalt();

        final byte[] hash1 = HashPin.generateSaltyHashFromPinAndSalt(pin, salt);
        final byte[] hash2 = HashPin.generateSaltyHashFromPinAndSalt(pin, salt);

        Assert.assertArrayEquals(hash1, hash2);
        Assert.assertArrayEquals(HashPin.getSaltFromSaltyHash(hash1), HashPin.getSaltFromSaltyHash(hash2));

    }

}
