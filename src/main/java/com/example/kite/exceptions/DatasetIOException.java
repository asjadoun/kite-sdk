package com.example.kite.exceptions;

import java.io.IOException;

public class DatasetIOException extends DatasetException {

    private final IOException ioException;

    public DatasetIOException(String message, IOException root) {
        super(message, root);
        this.ioException = root;
    }

    public IOException getIOException() {
        return ioException;
    }
}
