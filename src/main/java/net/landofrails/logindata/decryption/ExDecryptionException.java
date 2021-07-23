package net.landofrails.logindata.decryption;

public class ExDecryptionException extends RuntimeException {

    public ExDecryptionException(Exception e) {
        this.initCause(e.getCause());
        this.setStackTrace(e.getStackTrace());
    }

}
