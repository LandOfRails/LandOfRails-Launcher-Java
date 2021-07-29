package net.landofrails.logindata;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class StorageTest {

    @Test
    public void testCheckFile() throws IOException {
        Path temp = Files.createTempFile(null, null);
        temp.toFile().deleteOnExit();
        Storage storage = Storage.getInstance(temp);

        // Should be false, file exists but is empty.
        boolean loginStored = storage.hasLoginStored();
        Assert.assertFalse(loginStored);

        storage = null;
        temp.toFile().delete();
        temp = null;
    }

    @Test
    public void testStoreData() throws IOException {
        Path temp = Files.createTempFile(null, null);
        temp.toFile().mkdirs();
        temp.toFile().deleteOnExit();
        Storage storage = Storage.getInstance(temp);

        final String email = "danielxs01@email.de";
        final String password = "MyMinecraftPassword";
        final String pin = "A9Ö-";

        // Error: AccessDeniedException..
        storage.storeLogin(email, password, Optional.of(pin));

        // Check: Is login stored?
        boolean loginStored = storage.hasLoginStored();
        Assert.assertTrue(loginStored);

        // Check: Is encrypted data written to file?
        Assert.assertTrue(temp.toFile().length() > 0);

        // Save content of file for later
        final List<String> tempContent = Files.readAllLines(temp, StandardCharsets.UTF_8);

        // Get login
        String[] loginData = storage.getLogin(Optional.of(pin));

        // Check: Is decrypted correctly?
        Assert.assertEquals(loginData[0], email);
        Assert.assertEquals(loginData[1], password);

        System.out.printf("Email | Before: %s | After: %s%n", email, loginData[0]);
        System.out.printf("Password | Before: %s | After: %s%n", password, loginData[1]);

        // Save content of file
        final List<String> tempContent2 = Files.readAllLines(temp, StandardCharsets.UTF_8);

        // Check if file was changed
        Assert.assertThat(tempContent, CoreMatchers.is(CoreMatchers.not(tempContent2)));

        storage = null;
        temp.toFile().delete();
        temp = null;
    }

}
