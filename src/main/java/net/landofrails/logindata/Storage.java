package net.landofrails.logindata;

import net.landofrails.logindata.decryption.DecryptAES256;
import net.landofrails.logindata.encryption.EncryptAES256;
import net.landofrails.logindata.hashing.HashPin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public class Storage {

    private static final String LAUNCHERFOLDERNAME = ".landofrails";
    private static final String LOGINFILENAME = "login.secure";

    private static final String ENCRYPTIONSALT = "This8Salt4Should6Probably0Not2Be10Final";
    private static final String DEFAULTPIN = "abc123ZYXäöü";

    private static Storage instance = null;

    private Path loginFilePath = null;

    private Storage() {
        String homePath = System.getProperty("user.home");
        if (homePath != null) {
            File home = new File(homePath);
            if (home.exists()) {
                File launcherFolder = new File(home, LAUNCHERFOLDERNAME);
                launcherFolder.mkdirs();
                loginFilePath = new File(launcherFolder, LOGINFILENAME).toPath();
            }
        }
    }

    private Storage(Path loginFilePath) {
        this.loginFilePath = loginFilePath;
    }

    public static Storage getInstance() {
        if (instance == null)
            instance = new Storage();
        return instance;
    }

    public static Storage getInstance(Path loginFilePath) {
        return new Storage(loginFilePath);
    }

    public void storeLogin(String email, String password, Optional<String> optionalPin) {
        final String pin = optionalPin.orElse(DEFAULTPIN);

        //
        final byte[] salt = HashPin.generateSalt();
        // Hash pin
        final byte[] hashedPin = HashPin.generateSaltyHashFromPinAndSalt(pin, salt);
        // Encode pin
        final String baseHashedPin = Base64.getEncoder().withoutPadding().encodeToString(hashedPin);

        // Encrypt and Encode login with pin
        EncryptAES256 encryptor = new EncryptAES256();
        final String encryptedEmail = encryptor.encrypt(email, pin, ENCRYPTIONSALT);
        final String encryptedPassword = encryptor.encrypt(password, pin, ENCRYPTIONSALT);

        // Store hashed pin
        try {
            loginFilePath.toFile().mkdirs();
            if (loginFilePath.toFile().canWrite()) {
                Files.write(loginFilePath, baseHashedPin.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
                String e = "\n" + encryptedEmail;
                Files.write(loginFilePath, e.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                String p = "\n" + encryptedPassword;
                Files.write(loginFilePath, p.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            } else {
                throw new RuntimeException("Cant write");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public boolean hasLoginStored() {
        return loginFilePath != null && loginFilePath.toFile().exists() && loginFilePath.toFile().length() > 0;
    }

    public String[] getLogin(Optional<String> optionalPin) throws IllegalPinException {

        final String pin = optionalPin.orElse(DEFAULTPIN);

        // salt from stored hashed pin
        BufferedReader brTest = null;
        try {
            brTest = new BufferedReader(new FileReader(loginFilePath.toFile()));
            final String storedEncodedSaltyHashedPin = brTest.readLine();
            final byte[] storedSaltyHashedPin = Base64.getDecoder().decode(storedEncodedSaltyHashedPin);
            final byte[] salt = HashPin.getSaltFromSaltyHash(storedSaltyHashedPin);

            // hashed pin from plain pin
            final byte[] saltyHashedPin = HashPin.generateSaltyHashFromPinAndSalt(pin, salt);

            // check hashed pin with stored hashed pin
            if (Arrays.equals(saltyHashedPin, storedSaltyHashedPin)) {

                // get encrypted login
                final String encryptedEmail = brTest.readLine();
                final String encryptedPassword = brTest.readLine();

                // decrypt login with plain pin
                DecryptAES256 decryptor = new DecryptAES256();
                final String decryptedEmail = decryptor.decrypt(encryptedEmail, pin, ENCRYPTIONSALT);
                final String decryptedPassword = decryptor.decrypt(encryptedPassword, pin, ENCRYPTIONSALT);

                // encrypt login with plain pin (change encrypted data)
                storeLogin(decryptedEmail, decryptedPassword, Optional.of(pin));

                // return decrypted login
                return new String[]{decryptedEmail, decryptedPassword};

            } else {
                throw new IllegalPinException();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    public void delete() throws IOException {
        if (loginFilePath != null)
            Files.deleteIfExists(loginFilePath);
    }

}
