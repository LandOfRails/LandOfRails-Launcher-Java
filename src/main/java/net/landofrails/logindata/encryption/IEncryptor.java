package net.landofrails.logindata.encryption;

import java.util.concurrent.ExecutionException;

public interface IEncryptor {

    public String encrypt(String text, String key, String salt) throws ExecutionException;

}
