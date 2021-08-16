package com.example.kite.exceptions;

public class DatasetRecordException extends DatasetException {
    public DatasetRecordException(String message) {
        super(message);
    }

    public DatasetRecordException(String message, Throwable t) {
        super(message, t);
    }

    public static void check(boolean isValid, String message, Object... args) {
        if (!isValid) {
            String[] argStrings = new String[args.length];
            for (int i = 0; i < args.length; i += 1) {
                argStrings[i] = String.valueOf(args[i]);
            }
            throw new DatasetRecordException(
                    String.format(String.valueOf(message), (Object[]) argStrings));
        }
    }
}
