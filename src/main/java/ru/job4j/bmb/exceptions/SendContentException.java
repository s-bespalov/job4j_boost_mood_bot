package ru.job4j.bmb.exceptions;

public class SendContentException extends RuntimeException {
    public SendContentException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public SendContentException(Throwable cause) {
        super(cause);
    }

    public SendContentException() {

    }
}