package net.landofrails.logindata;

public class IllegalPinException extends RuntimeException {

    public IllegalPinException() {
        super("The pin was not correct.");
    }

}
