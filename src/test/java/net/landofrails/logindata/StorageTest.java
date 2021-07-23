package net.landofrails.logindata;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StorageTest {

    @Test
    public void testCheckFile() throws IOException {
        File temp = File.createTempFile("landofrails_login_", ".tmp");
        temp.deleteOnExit();
        Storage storage = Storage.getInstance(temp);

        // Should be false, file exists but is empty.
        boolean loginStored = storage.hasLoginStored();
        Assert.assertFalse(loginStored);

        storage = null;
        temp.delete();
        temp = null;
    }

    @Test
    @Ignore
    public void testStoreData() throws IOException {
        Path temp = Files.createTempFile("landofrails_login", ".text");
        temp.toFile().mkdirs();
        temp.toFile().deleteOnExit();
        Storage storage = Storage.getInstance(temp.toFile());

        final String email = "daniel.schmidt@landofrails.net";
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
        String tempContentString = tempContent.stream().collect(Collectors.joining("\n"));

        // Get login
        String[] loginData = storage.getLogin(Optional.of(pin));

        // Check: Is decrypted correctly?
        Assert.assertEquals(loginData[0], email);
        Assert.assertEquals(loginData[1], password);

        storage = null;
        temp.toFile().delete();
        temp = null;
    }

}
