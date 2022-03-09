package br.jus.jfsp.nuit.contadoria.exception;

public class DataInvalidaException extends Exception {

    private static final long serialVersionUID = 9216993576619307601L;

    public DataInvalidaException() {
    }

    public DataInvalidaException(String message) {
        super(message);
    }

    public DataInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataInvalidaException(Throwable cause) {
        super(cause);
    }
}
