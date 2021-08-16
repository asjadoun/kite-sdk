package com.example.kite.exceptions;

public class IncompatibleSchemaException extends ValidationException {

    public IncompatibleSchemaException(String message) {
        super(message);
    }

    public IncompatibleSchemaException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncompatibleSchemaException(Throwable cause) {
        super(cause);
    }

    public static void check(boolean isValid, String message, Object... args) {
        if (!isValid) {
            String[] argStrings = new String[args.length];
            for (int i = 0; i < args.length; i += 1) {
                argStrings[i] = String.valueOf(args[i]);
            }
            throw new IncompatibleSchemaException(
                    String.format(String.valueOf(message), (Object[]) argStrings));
        }
    }
}
