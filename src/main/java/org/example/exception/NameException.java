package org.example.exception;

public class NameException extends RuntimeException {
    public NameException(String name) {
        super(String.format("Name not valid with %s", name));
    }
}
