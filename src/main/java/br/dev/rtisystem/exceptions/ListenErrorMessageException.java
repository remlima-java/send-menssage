package br.dev.rtisystem.exceptions;

public class ListenErrorMessageException extends RuntimeException {
    public ListenErrorMessageException(Exception message) {
        super(message);
    }
}
