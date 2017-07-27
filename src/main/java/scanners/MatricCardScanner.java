package scanners;

import logic.validation.Validity;
import objects.Scan;
import java.util.logging.*;
import javax.smartcardio.*;
import java.util.Arrays;
import java.util.List;

/**
 * MatricCardScanner
 * <p>
 * This is the scanner class that scans a matriculation card for particular fields as defined by the
 * MatricCardScannerUtilities interface. It detects an available scanner, then acquire the matriculation card that is
 * in contact. From the card, we initiate APDUCommand and APDUResponse session to read the matric card number and the faculty.
 * </p>
 * <p>
 * We do not support writing of data in this application, due to security concerns.
 * </p>
 */

public class MatricCardScanner implements MatricCardScannerUtilities {

    // To be connected to UI as User Input
    public String keyA = "";
    public String keyB = "";

    // PROTOCOLS //
    private static final String DEFAULT_PROTOCOL = "*";
    private static final String[] PROTOCOLS = {"T=0", "T=1", "T=CL", "*", "direct"};

    // MESSAGES //
    private static final String MESSAGE_DETECTED_TERMINAL = "\n[Occupied Terminal Detected] Terminal %1$d : %2$s";
    private static final String MESSAGE_CARD_ATR_OUTPUT = "\n[Card Connected] Card ATR Code: %1$s";
    private static final String MESSAGE_APDU_COMMAND_OUTPUT = "[APDU Communication] APDU Command Issued: >>> %1$s";
    private static final String MESSAGE_APDU_RESPONSE_OUTPUT_RAW = "[ADPU Communication] APDU Response Received: <<< %1$s";
    private static final String MESSAGE_APDU_RESPONSE_OUTPUT_STATUS = "        ~ [R.Status] %1$s";
    private static final String MESSAGE_APDU_RESPONSE_OUTPUT_DATA = "        ~ [R.Detail] %1$s";
    private static final String MESSAGE_CARD_SERIAL_NUMBER = "\n + [Serial Number Obtained] Card Serial Number: %1$s (32-bit Integer: %2$d)";
    private static final String MESSAGE_CARD_MATRIC_NUMBER = "\n + [Matriculation Number Obtained] Card Matriculation Number: %1$s";
    private static final String MESSAGE_CARD_ACCESS_CODE = "\n + [Access Code Obtained] Card Access Code: %1$s";
    private static final String MESSAGE_CARD_DISCONNECT_OUTPUT = "\n[Card Disconnected] Card ATR Code: %1$s";

    // MESSAGE MODIFIERS //
    private static final String FIELD_EMPTY = "---EMPTY---";
    private static final String FLAG_UNSUCCESSFUL = "---------------------------------------- [FLAG] UNSUCCESSFUL";

    // MESSAGE TITLES //
    private static final String FORMATTED_TITLE = "\n<<< %1$s >>>";
    private static final String TITLE_SEND_AUTHENTICATE_KEY = "SEND AUTHENTICATION KEY";
    private static final String TITLE_AUTHENTICATE_BLOCK_A = "AUTHENTICATING BLOCK A";
    private static final String TITLE_AUTHENTICATE_BLOCK_B = "AUTHENTICATING BLOCK B";
    private static final String TITLE_READ_BLOCK_A = "READING BLOCK A";
    private static final String TITLE_READ_BLOCK_B = "READING BLOCK B";
    private static final String TITLE_READ_SERIAL = "READING SERIAL NUMBER";

    // STATUSES //
    private static final String STATUS_NORMAL = "9000";

    // INDICES AND SIZES //
    private static final int ACCESS_CODE_START_INDEX = 4;
    private static final int ACCESS_CODE_END_INDEX = 6;
    private static final int SIZE_SERIAL_NUMBER = 4;
    private static final int FIXED_BYTE_SIZE = 4;

    // ACR //
    private static final String TERMINAL_NAMETAG_ACR = "ACR122U";
    private static final int ACR_BLOCK_TRUNCATION = 3;

    // VARIABLES //
    private static boolean isACRTerminal = false;
    private boolean initialised = false;

    // TODO: Can choose to handle singleton here

    // Constructor checks if scanner has been initialised
    public MatricCardScanner() {
        this.initialised = true;
    }

    public static void main(String[] args) {
        boolean scanned = false;
        MatricCardScanner scanner = new MatricCardScanner();

        while (!scanned) {
            try {
                scanner.scanCardInfo();
                scanned = true;
            } catch (Exception exception) {
                // TODO: Can utilise different catch clauses to handle different exceptions
                // Debug
                // exception.printStackTrace();
                continue;
            }
        }
    }

    /**
     *
     * Checks if password has been entered.
     */
    public Boolean isAuthenticationEmpty() {
        if (keyA.isEmpty() || keyB.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * scanCardInfo
     * <p>
     * scanCardInfo connects with a scan terminal to read and retrieve the card details.
     * </p>
     *
     * @throws Exception
     */
    public Scan scanCardInfo() throws Exception {

        // TODO: Change return type to CardInfo and wrap information within

        try {
            // Connect with terminal and card
//            MatricCardScanner scanner = new MatricCardScanner();
            CardTerminal terminal = getScanTerminal(null, null, null);

            // Check if terminal is ACR122U variant that uses PN532 connection
//            if (terminal.isCardPresent() && terminal.getName().contains(TERMINAL_NAMETAG_ACR)) {
//                isACRTerminal = true;
//            }
            isACRTerminal = true;

            Card matriculationCard = connectCard(terminal, DEFAULT_PROTOCOL);
            getCardSerialNumber(matriculationCard);

            // Authenticate and read from Block #1
            byte[] blockOne = authenticateAndReadBlock(matriculationCard, 1);
            String matricNumber = getMatricNumber(blockOne);


            // Authenticate and read from Block #2
            byte[] blockTwo = authenticateAndReadBlock(matriculationCard, 2);
            String accessCode = getAccessCode(blockTwo);

            // Disconnect card
            disconnectCard(matriculationCard);
            return new Scan(matricNumber, Validity.getHash(accessCode));

        } catch (Exception exception) {
            throw exception;
        }
    }

    @Override
    public CardTerminal getScanTerminal() throws Exception {

        try {


            // Finds the terminal(s) with card present
            TerminalFactory factory = TerminalFactory.getDefault();
            CardTerminals terminals = factory.terminals();
            List<CardTerminal> terminalList = terminals.list(CardTerminals.State.CARD_PRESENT);

            // Handles absence of terminals
            if (terminalList == null || terminalList.isEmpty()) {
                throw new MatricCardScannerExceptions.NoTerminalException();
            }

            // For debugging: outputs all the detected terminals
//            for (int i = 1; i <= terminalList.size(); ++i) {
//                CardTerminal terminal = terminalList.get(i - 1);
//                String detectedOutput = String.format(MESSAGE_DETECTED_TERMINAL, i, terminal.getName());
//                Logger.getLogger("").info(detectedOutput);
//            }

            // Handles multiple detected terminals
            if (terminalList.size() > 1) {
                throw new MatricCardScannerExceptions.MultiTerminalsException();
            }

            // Get the first (and only) terminal (that has a card present) in the list
            CardTerminal terminal = terminalList.get(0);

            return terminal;

        } catch (Exception exception) {
            // TODO: Logging

            throw exception;

        }
    }

    @Override
    public CardTerminal getScanTerminal(String type, Object parameters, String provider) throws Exception {

        try {

            // TODO: Filter the parameters: Type, Parameters, Provider

            TerminalFactory factory;

            // Handles cases when no parameters are specified
            if (type == null && parameters == null && provider == null) {
                return getScanTerminal();
            } else {
                factory = TerminalFactory.getInstance(type, parameters, provider);
            }

            // Finds the terminal(s) with card present
            CardTerminals terminals = factory.terminals();
            List<CardTerminal> terminalList = terminals.list(CardTerminals.State.CARD_PRESENT);

            // Handles absence of terminals
            if (terminalList == null || terminalList.isEmpty()) {
                throw new MatricCardScannerExceptions.NoTerminalException();
            }

            // For debugging: outputs all the detected terminals
            for (int i = 1; i <= terminalList.size(); i++) {
                CardTerminal terminal = terminalList.get(i - 1);
                String detectedOutput = String.format(MESSAGE_DETECTED_TERMINAL, i, terminal.getName());
                Logger.getLogger("").info(detectedOutput);
            }

            // Handles multiple detected terminals
            if (terminalList.size() > 1) {
                throw new MatricCardScannerExceptions.MultiTerminalsException();
            }

            // Get the first terminal (that has a card present) in the list
            CardTerminal terminal = terminalList.get(0);

            // TODO: Logging

            return terminal;

        } catch (Exception exception) {
            // TODO: Logging

            throw exception;

        }
    }

    @Override
    public Card connectCard(CardTerminal scanner, String protocol) throws Exception {

        try {
            boolean validProtocol = false;

            // Set protocol to default if field is empty
            if (protocol == null) {
                protocol = DEFAULT_PROTOCOL;
            }

            // Validate protocol field
            for (String proto : PROTOCOLS) {
                if (protocol.equals(proto)) {
                    validProtocol = true;
                }
            }

            // Handles invalid protocol(s)
            if (!validProtocol) {
                throw new MatricCardScannerExceptions.InvalidCardProtocolException();
            }

            // Establish connection with card
            Card matricCard = scanner.connect(protocol);
            String codeATR = getCardATRCode(matricCard);
//            String outputMessage = String.format(MESSAGE_CARD_ATR_OUTPUT, codeATR);
//            Logger.getLogger("").info(outputMessage);

            // TODO: Logging
            return matricCard;

        } catch (Exception exception) {
            // TODO: Logging
            throw exception;
        }

    }

    @Override
    public String getCardATRCode(Card matriculationCard) throws Exception {
        byte[] bytesATR = matriculationCard.getATR().getBytes();
        return bytesToHexString(bytesATR);

    }

    @Override
    public byte[] authenticateAndReadBlock(Card matriculationCard, int block) throws Exception {

        try {

            // Establish APDU Communication
            CardChannel channel = matriculationCard.getBasicChannel();

            // Obtain Card Serial Number
            byte[] serialNumber = getCardSerialNumberBytes(matriculationCard);

            CommandAPDU commandMessage;
            ResponseAPDU responseMessage;

            // Only load Authentication Key when standard PC/SC communication terminal is used
            if (!isACRTerminal) {
                // Send Authentication Command
                commandMessage = APDUCommands.getAPDUDecryptCommand(serialNumber, keyA, keyB);
                responseMessage = channel.transmit(commandMessage);
                displayAPDUExchange(commandMessage, responseMessage, TITLE_SEND_AUTHENTICATE_KEY);
            }

            byte[] responseBytes;

            switch (block) {

                // Get data for Block #1
                case 1:

                    // Build Authenticate Command for PN532 connection mode
                    if (isACRTerminal) {
                        // Append computed key
                        byte[] commandA = APDUCommands.appendCommandBytesWithKey(serialNumber, APDUCommands.APDUCOMMAND_AUTHENTICATE_BLOCK_A_ACR, keyA, keyB);

                        // Append UID
                        byte[] commandBytesA = new byte[commandA.length + serialNumber.length];
                        System.arraycopy(commandA, 0, commandBytesA, 0, commandA.length);
                        System.arraycopy(serialNumber, 0, commandBytesA, commandA.length, serialNumber.length);

                        // Create APDU Command
                        commandMessage = new CommandAPDU(commandBytesA);

                        // Use preset Authentication Command for standard connection
                    } else {
                        commandMessage = APDUCommands.APDUCOMMAND_AUTHENTICATE_BLOCK_A;
                    }

                    // Communicate Command
                    responseMessage = channel.transmit(commandMessage);
                    displayAPDUExchange(commandMessage, responseMessage, TITLE_AUTHENTICATE_BLOCK_A);


                    // Read

                    // Use modified Read Command for PN532 connection mode
                    if (isACRTerminal) {
                        commandMessage = APDUCommands.APDUCOMMAND_READ_BLOCK_A_ACR;

                        // Use standard Read Command for standard connection
                    } else {
                        commandMessage = APDUCommands.APDUCOMMAND_READ_BLOCK_A;
                    }

                    // Communicate Command
                    responseMessage = channel.transmit(commandMessage);
                    displayAPDUExchange(commandMessage, responseMessage, TITLE_READ_BLOCK_A);

                    // Obtain Response
                    responseBytes = responseMessage.getData();

                    // Trim response if neccessary
                    if (isACRTerminal) {
                        return Arrays.copyOfRange(responseBytes, ACR_BLOCK_TRUNCATION, responseBytes.length);
                    } else {
                        return responseBytes;
                    }

                    // Get data for Block #2
                case 2:

                    // Build Authenticate Command for PN532 connection mode
                    if (isACRTerminal) {

                        // Append computed key
                        byte[] commandB = APDUCommands.appendCommandBytesWithKey(serialNumber, APDUCommands.APDUCOMMAND_AUTHENTICATE_BLOCK_B_ACR, keyA, keyB);

                        // Append UID
                        byte[] commandBytesB = new byte[commandB.length + serialNumber.length];
                        System.arraycopy(commandB, 0, commandBytesB, 0, commandB.length);
                        System.arraycopy(serialNumber, 0, commandBytesB, commandB.length, serialNumber.length);

                        // Create APDU Command
                        commandMessage = new CommandAPDU(commandBytesB);

                        // Use preset Authentication Command for standard connection
                    } else {
                        commandMessage = APDUCommands.APDUCOMMAND_AUTHENTICATE_BLOCK_B;
                    }

                    // Communicate Command
                    responseMessage = channel.transmit(commandMessage);
                    displayAPDUExchange(commandMessage, responseMessage, TITLE_AUTHENTICATE_BLOCK_B);


                    // Read

                    // Use modified Read Command for PN532 connection mode
                    if (isACRTerminal) {
                        commandMessage = APDUCommands.APDUCOMMAND_READ_BLOCK_B_ACR;
                        // Use standard Read Command for standard connection
                    } else {
                        commandMessage = APDUCommands.APDUCOMMAND_READ_BLOCK_B;
                    }

                    // Communicate Command
                    responseMessage = channel.transmit(commandMessage);
                    displayAPDUExchange(commandMessage, responseMessage, TITLE_READ_BLOCK_B);

                    // Obtain Response
                    responseBytes = responseMessage.getData();

                    // Trim response if neccessary
                    if (isACRTerminal) {
                        return Arrays.copyOfRange(responseBytes, ACR_BLOCK_TRUNCATION, responseBytes.length);
                    } else {
                        return responseBytes;
                    }

                default:
                    throw new MatricCardScannerExceptions.InvalidBlockException();
            }

        } catch (Exception exception) {
//            Logger.getLogger("").info(exception.getStackTrace().toString());
            throw exception;
        }

    }

    @Override
    public boolean disconnectCard(Card matriculationCard) throws Exception {

        try {

            String codeATR = getCardATRCode(matriculationCard);
            matriculationCard.disconnect(true);

            String outputMessage = String.format(MESSAGE_CARD_DISCONNECT_OUTPUT, codeATR);
            Logger.getLogger("").info(outputMessage);

            // TODO: Logging

            return true;

        } catch (Exception exception) {

            // TODO: Logging

            throw exception;

        }
    }

    @Override
    public String getCardSerialNumber(Card matriculationCard) throws Exception {

        try {

            // Establish APDU Communication
            CardChannel channel = matriculationCard.getBasicChannel();

            // Prompt for card serial number
            CommandAPDU commandMessage;

            if (isACRTerminal) {
                commandMessage = APDUCommands.APDUCOMMAND_READ_SERIAL_NUMBER_ACR;
            } else {
                commandMessage = APDUCommands.APDUCOMMAND_READ_SERIAL_NUMBER;
            }

            ResponseAPDU responseMessage = channel.transmit(commandMessage);
            displayAPDUExchange(commandMessage, responseMessage, TITLE_READ_SERIAL);
            byte[] responseBytes = responseMessage.getData();

            // TODO: Filter response for serial number

            // Parse and retrieve serial number
            String serialNumber;
            long serialInt;
            if (isACRTerminal) {
                byte[] trimmedResponse = Arrays.copyOfRange(responseBytes, responseBytes.length - SIZE_SERIAL_NUMBER, responseBytes.length);
                serialNumber = bytesToHexString(trimmedResponse);
                serialInt = bytesToLong(trimmedResponse);
            } else {
                serialNumber = bytesToHexString(responseBytes);
                serialInt = bytesToLong(responseBytes);
            }

            String outputMessage = String.format(MESSAGE_CARD_SERIAL_NUMBER, serialNumber, serialInt);
            Logger.getLogger("").info(outputMessage);

            return serialNumber;

        } catch (Exception exception) {
            // TODO: Logging
            Logger.getLogger("").info(exception.getStackTrace().toString());
            throw exception;
        }

    }

    @Override
    public byte[] getCardSerialNumberBytes(Card matriculationCard) throws Exception {

        try {

            // Establish APDU Communication
            CardChannel channel = matriculationCard.getBasicChannel();

            // Prompt for card serial number
            CommandAPDU commandMessage;
            if (isACRTerminal) {
                commandMessage = APDUCommands.APDUCOMMAND_READ_SERIAL_NUMBER_ACR;
            } else {
                commandMessage = APDUCommands.APDUCOMMAND_READ_SERIAL_NUMBER;
            }

            ResponseAPDU responseMessage = channel.transmit(commandMessage);
            displayAPDUExchange(commandMessage, responseMessage, TITLE_READ_SERIAL);

            // TODO: Filter response for serial number

            byte[] responseBytes = responseMessage.getData();

            if (isACRTerminal) {
                responseBytes = Arrays.copyOfRange(responseBytes, responseBytes.length - SIZE_SERIAL_NUMBER, responseBytes.length);
            }

            return responseBytes;

        } catch (Exception exception) {

            // TODO: Logging

            throw exception;
        }

    }

    @Override
    public String getMatricNumber(byte[] rawBlockData) throws Exception {

        String matricNumber = "";

        // TODO: Use bytes to String

        for (byte unit : rawBlockData) {
            if (unit != (byte) 0) {
                byte[] array = {unit};
                matricNumber += new String(array);
            }
        }

        // TODO: Check Matric. Number validity (Handle Exceptions)

        String outputMessage = String.format(MESSAGE_CARD_MATRIC_NUMBER, matricNumber);
        Logger.getLogger("").info(outputMessage);

        // TODO: Logging

        return matricNumber;
    }

    @Override
    public String getAccessCode(byte[] rawBlockData) throws Exception {

        String accessCode = parseBlockSubSequence(rawBlockData, ACCESS_CODE_START_INDEX, ACCESS_CODE_END_INDEX);

        // TODO: validate accessCode and handle exceptions

        String outputMessage = String.format(MESSAGE_CARD_ACCESS_CODE, accessCode);
        Logger.getLogger("").info(outputMessage);

        // TODO: Logging

        return accessCode;

    }

    @Override
    public String parseBlockSubSequence(byte[] rawBlockData, int startIndex, int endIndex) throws Exception {
        try {
            String parsedString;
            byte[] binary = new byte[FIXED_BYTE_SIZE];
            byte[] subSequence = Arrays.copyOfRange(rawBlockData, startIndex, endIndex);

            int byteSize = endIndex - startIndex;
            System.arraycopy(subSequence, 0, binary, FIXED_BYTE_SIZE - byteSize, byteSize);
            parsedString = bytesToLong(binary).toString();

            return parsedString;

        } catch (Exception exception) {

            // TODO: Logging

            throw exception;
        }
    }

    @Override
    public String bytesToHexString(byte[] byteArray) throws Exception {

        // TODO: Handle exceptions

        final String HEX_DICTIONARY = "0123456789ABCDEF";
        char[] hexArray = HEX_DICTIONARY.toCharArray();
        char[] hexChars = new char[byteArray.length * 2];

        for (int i = 0; i < byteArray.length; ++i) {
            // Copy the byte
            int v = byteArray[i] & 0xFF;
            // Set first half (Most Significant Nibble)
            hexChars[i * 2] = hexArray[v >>> 4];
            // Set second half (Least Significant Nibble)
            hexChars[i * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    @Override
    public Long bytesToLong(byte[] byteArray) throws Exception {
        try {
            // Handles invalid byte array
            if (byteArray == null || byteArray.length != 4) {
                throw new MatricCardScannerExceptions.IllegalByteToLongConversionException();
            }

            long integer = 0;
            for (int i = 0; i < byteArray.length; ++i) {
                integer |= byteArray[i] & 0xFF;
                if (i < byteArray.length - 1) {
                    integer <<= 8;
                }
            }

            return integer;

        } catch (Exception exception) {

            // TODO: Logging

            throw exception;
        }
    }

    @Override
    public void displayAPDUExchange(CommandAPDU commandMessage, ResponseAPDU responseMessage, String title) throws Exception {

        String genTitle = "GENERAL";

        // Parse Command and Response
        String hexCommandMessage = bytesToHexString(commandMessage.getBytes());
        String hexResponseMessage = bytesToHexString(responseMessage.getBytes());
        String hexResponseStatusFlag = hexResponseMessage.substring(hexResponseMessage.length() - 4, hexResponseMessage.length());
        String responseData = bytesToHexString(responseMessage.getData());

        if (responseData == null || responseData.trim().length() == 0) {
            responseData = FIELD_EMPTY;
        }

        if (title != null || title.trim().length() == 0) {
            genTitle = title;
        }

        // Format Output
        String commandOutput = String.format(MESSAGE_APDU_COMMAND_OUTPUT, hexCommandMessage);
        String responseOutput = String.format(MESSAGE_APDU_RESPONSE_OUTPUT_RAW, hexResponseMessage);
        String responseStatusOutput = String.format(MESSAGE_APDU_RESPONSE_OUTPUT_STATUS, APDUStatusChecker.checkError(hexResponseStatusFlag));
        String responseDataOutput = String.format(MESSAGE_APDU_RESPONSE_OUTPUT_DATA, responseData);
        String titleOutput = String.format(FORMATTED_TITLE, genTitle);

        // Display Output
//        Logger.getLogger("").info(titleOutput);
//        Logger.getLogger("").info(commandOutput);
//        Logger.getLogger("").info(responseOutput);
//        Logger.getLogger("").info(responseStatusOutput);
//        Logger.getLogger("").info(responseDataOutput);

        if (!hexResponseStatusFlag.equals(STATUS_NORMAL)) {
            Logger.getLogger("").warning(FLAG_UNSUCCESSFUL);
        }

        // TODO: Logging

    }

}
