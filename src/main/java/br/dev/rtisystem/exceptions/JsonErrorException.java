package br.dev.rtisystem.exceptions;

public class JsonErrorException extends RuntimeException {
    public JsonErrorException(Exception message) {
        super(message);
    }
}
