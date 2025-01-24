package me.huangduo.hms.exceptions;

public class JsonDeserializationException extends RuntimeException {

    public JsonDeserializationException(Throwable cause) {
        super("Deserialization failure", cause);
    }

    public JsonDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }
}