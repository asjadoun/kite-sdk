package com.example.kite.exceptions;

public class ValidationException extends DatasetException {

    public ValidationException(String msg) {
        super(msg);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public ValidationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public static void check(boolean isValid, String message, Object... args) {
        if (!isValid) {
            String[] argStrings = new String[args.length];
            for (int i = 0; i < args.length; i += 1) {
                argStrings[i] = String.valueOf(args[i]);
            }
            throw new ValidationException(
                    String.format(String.valueOf(message), (Object[]) argStrings));
        }
    }
}
