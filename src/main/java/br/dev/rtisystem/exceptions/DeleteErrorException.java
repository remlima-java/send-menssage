package br.dev.rtisystem.exceptions;

public class DeleteErrorException extends RuntimeException {
    public DeleteErrorException(Exception message) {
        super(message);
    }
}
