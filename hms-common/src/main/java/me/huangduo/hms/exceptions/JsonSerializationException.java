package me.huangduo.hms.exceptions;

public class JsonSerializationException extends RuntimeException {

    public JsonSerializationException(Throwable cause) {
        super("Serialization failure", cause);
    }

    public JsonSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}