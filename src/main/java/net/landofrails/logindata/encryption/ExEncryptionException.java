package net.landofrails.logindata.encryption;

public class ExEncryptionException extends RuntimeException {

    private Exception e;

    public ExEncryptionException(Exception e) {
        this.e = e;
    }

    @Override
    public void printStackTrace() {
        e.printStackTrace();
    }
}
