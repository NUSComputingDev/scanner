package scanners;

/**
 * MatricCardScannerExceptions
 * <p>
 * MatricCardScannerExceptions is the exception class that holds all the custom exceptions and exception messages.
 * </p>
 */
public class MatricCardScannerExceptions {

    private static final String MESSAGE_NO_TERMINAL = "No readable terminals detected. " +
                                                      "Please try again or specify a type of scanner.";

    private static final String MESSAGE_MULTI_TERMINAL = "Multiple terminals detected. " +
                                                         "Please try again or specify a type of scanner.";

    private static final String MESSAGE_INVALID_CARD_PROTOCOL = "Invalid card protocol specified. " +
                                                                "Please try 'T=0' | 'T=1' | 'T=CL' | '*' ";

    private static final String MESSAGE_ILLEGAL_CONVERSION_BYTE_LONG = "The supplied byte array cannot be converted" +
                                                                       " into a unsigned number. Please try again.";

    private static final String MESSAGE_INVALID_ERROR_CODE = "This error code is of incorrect format. Please ensure" +
                                                             " error codes is a string of 4 hexadecimal values.";

    private static final String MESSAGE_INVALID_BLOCK = "This block number is unsupported. Only reading from block " +
                                                        "number 1 or 2 is supported.";

    private static final String MESSAGE_BAD_APDU_RESPONSE = "Unsuccessful operation. Refer to the ADPU Logs " +
                                                            "for more information.";


    // NoTerminalException is thrown when there are no suitable terminals detected.
    public static class NoTerminalException extends RuntimeException {
        private static final String defaultMsg = MESSAGE_NO_TERMINAL;

        public NoTerminalException(String message) {
            super(message);
        }

        public NoTerminalException() {
            super(defaultMsg);
        }
    }

    // MultiTerminalsException is thrown when there are multiple terminals detected and no decision can be made.
    public static class MultiTerminalsException extends RuntimeException {
        private static final String defaultMsg = MESSAGE_MULTI_TERMINAL;

        public MultiTerminalsException(String message) {
            super(message);
        }

        public MultiTerminalsException() {
            super(defaultMsg);
        }
    }

    // InvalidCardProtocolException is thrown when the specified card communication protocol is invalid.
    public static class InvalidCardProtocolException extends Exception {
        private static final String defaultMsg = MESSAGE_INVALID_CARD_PROTOCOL;

        public InvalidCardProtocolException(String message) {
            super(message);
        }

        public InvalidCardProtocolException() {
            super(defaultMsg);
        }
    }


    // IllegalByteToLongConversionException is thrown when the supplied byte array for conversion is incompatible.
    public static class IllegalByteToLongConversionException extends Exception {
        private static final String defaultMsg = MESSAGE_ILLEGAL_CONVERSION_BYTE_LONG;

        public IllegalByteToLongConversionException(String message) {
            super(message);
        }

        public IllegalByteToLongConversionException() {
            super(defaultMsg);
        }
    }

    // InvalidErrorCodeException is thrown when the supplied error code is not of 4 hexadecimal value.
    public static class InvalidErrorCodeException extends Exception {
        private static final String defaultMsg = MESSAGE_INVALID_ERROR_CODE;

        public InvalidErrorCodeException(String message) {
            super(message);
        }

        public InvalidErrorCodeException() {
            super(defaultMsg);
        }
    }

    // InvalidBlockException is thrown when the supplied block number is not supported for reading.
    public static class InvalidBlockException extends Exception {
        private static final String defaultMsg = MESSAGE_INVALID_BLOCK;

        public InvalidBlockException(String message) {
            super(message);
        }

        public InvalidBlockException() {
            super(defaultMsg);
        }
    }

    // BadAPDUResponseException is thrown when the APDUResponse is of an unsuccessful variant.
    public static class BadAPDUResponseException extends Exception {
        private static final String defaultMsg = MESSAGE_BAD_APDU_RESPONSE;

        public BadAPDUResponseException(String message) {
            super(message);
        }

        public BadAPDUResponseException() {
            super(defaultMsg);
        }
    }
}
