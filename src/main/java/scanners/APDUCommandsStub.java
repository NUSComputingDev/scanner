package scanners;

import javax.smartcardio.CommandAPDU;

public class APDUCommandsStub {

    // For standard PC/SC Terminals
    public static final CommandAPDU APDUCOMMAND_READ_SERIAL_NUMBER = null;
    public static final CommandAPDU APDUCOMMAND_AUTHENTICATE_BLOCK_A = null;
    public static final CommandAPDU APDUCOMMAND_AUTHENTICATE_BLOCK_B = null;
    public static final CommandAPDU APDUCOMMAND_READ_BLOCK_A = null;
    public static final CommandAPDU APDUCOMMAND_READ_BLOCK_B = null;

    // For ACR122U Terminals
    public static final CommandAPDU APDUCOMMAND_READ_SERIAL_NUMBER_ACR = null;
    public static final byte[] APDUCOMMAND_AUTHENTICATE_BLOCK_A_ACR = null;
    public static final byte[] APDUCOMMAND_AUTHENTICATE_BLOCK_B_ACR = null;
    public static final CommandAPDU APDUCOMMAND_READ_BLOCK_A_ACR = null;
    public static final CommandAPDU APDUCOMMAND_READ_BLOCK_B_ACR = null;


    // FUNCTIONS //

    public static CommandAPDU getAPDUDecryptCommand(byte[] serialNumberDataArray, String keyA, String keyB) throws Exception {
        return null;
    }

    public static byte[] appendCommandBytesWithKey(byte[] serialNumberDataArray, byte[] partial, String keyA, String keyB) throws Exception {
        return null;
    }

    private static byte[] readKeyFile(String keyA, String keyB) {
        return null;
    }
}