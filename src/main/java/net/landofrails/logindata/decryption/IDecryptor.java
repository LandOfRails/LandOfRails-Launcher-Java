package net.landofrails.logindata.decryption;

public interface IDecryptor {
    public String decrypt(String text, String key, String salt) throws ExDecryptionException;
}
