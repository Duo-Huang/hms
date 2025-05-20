package me.huangduo.hms.exceptions;

public class JsonSerializationException extends RuntimeException {

    public JsonSerializationException(Throwable cause) {
        super("Json serialization failure", cause);
    }

    public JsonSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}