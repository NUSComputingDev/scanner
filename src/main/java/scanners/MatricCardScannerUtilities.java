package scanners;

import javax.smartcardio.Card;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;


/**
 * MatricCardScannerUtilities
 * <p>
 * MatricCardScannerUtilities is a card-scanner interface that suggests the essential implementations for scanning of
 * NUS Matriculation Cards.
 * </p>
 */
public interface MatricCardScannerUtilities {


    // Scanner Initialization Functions

    /**
     * getScanTerminal
     * <p>
     * getScanTerminal searches for existing NFC scanners that are connected to the workstation and has a card within
     * its NFC field. If no valid scanners are found, an exception will be thrown to indicate the failure.
     * </p>
     *
     * @return CardTerminal
     * @throws Exception
     */
    CardTerminal getScanTerminal() throws Exception;

    /**
     * getScanTerminal
     * <p>
     * getScanTerminal searches for existing NFC scanners that are connected to the workstation and has a card within
     * its NFC field. If no valid scanners are found, an exception will be thrown to indicate the failure.
     * </p>
     *
     * @param type is the name of the connection type / protocol
     * @param parameters is an Object that represents any arguments that are required to obtain the scan terminal
     * @param provider is the name of the service provider for the connection with the terminal
     * @return CardTerminal
     * @throws Exception
     */
    CardTerminal getScanTerminal(String type, Object parameters, String provider) throws Exception;


    // Generic Card I/O Functions

    /**
     * connectCard
     * <p>
     * connectCard initialises a connection between the scanning terminal and the NUS Matriculation / Staff Card.
     * </p>
     *
     * @param scanner is the CardTerminal that the card is in range with
     * @param protocol is the name of the connection protocol
     * @return Card
     * @throws Exception
     */
    Card connectCard(CardTerminal scanner, String protocol) throws Exception;

    /**
     * getCardATRCode
     * <p>
     * getCardATRCode reads the Answer-To-Reset code of NUS Matriculation / Staff Card.
     * </p>
     * @param matriculationCard is the NUS Matriculation / Staff card to read the ATR code from
     * @return String
     * @throws Exception
     */
    String getCardATRCode(Card matriculationCard) throws Exception;

    /**
     * authenticateAndReadBlock
     * <p>
     * authenticateAndReadBlock initialises a secure and authenticated session with the NUS Matriculation / Staff Card
     * and reads the data stored in the block specified.
     * </p>
     * @param matriculationCard is the NUS Matriculation / Staff Card to read from
     * @param block is the block number of the memory block to read
     * @return byte[]
     * @throws Exception
     */
    byte[] authenticateAndReadBlock(Card matriculationCard, int block) throws Exception;

    /**
     * disconnectCard
     * <p>
     * disconnectCard terminates the read/write session between the scanning terminal and the NUS Matriculation / Staff
     * Card.
     * </p>
     *
     * @param matriculationCard is the NUS Matriculation / Staff Card to disconnect from.
     * @return boolean
     * @throws Exception
     */
    boolean disconnectCard(Card matriculationCard) throws Exception;


    // Data Access Functions

    /**
     * getCardSerialNumber
     * <p>
     * getCardSerialNumber reads the serial number of the NUS Matriculation / Staff Card.
     * </p>
     *
     * @param matriculationCard is the NUS Matriculation / Staff Card to read from
     * @return String
     * @throws Exception
     */
    String getCardSerialNumber(Card matriculationCard) throws Exception;

    /**
     * getCardSerialNumberBytes
     * <p>
     * getCardSerialNumberBytes retrieves the raw bytes representation of the serial number of the NUS Matriculation /
     * Staff card.
     * </p>
     *
     * @param matriculationCard is the NUS Matriculation / Staff Card to read from
     * @return byte[]
     * @throws Exception
     */
    byte[] getCardSerialNumberBytes(Card matriculationCard) throws Exception;

    /**
     * getMatricNumber
     * <p>
     * getMatricNumber parses and reads the matriculation / staff number from a memory block.
     * </p>
     *
     * @param rawBlockData is the block of bytes where the matriculation / staff number is stored in
     * @return String
     * @throws Exception
     */
    String getMatricNumber(byte[] rawBlockData) throws Exception;

    /**
     * getAccessCode
     * <p>
     * getAccessCode parses and reads the access code from a memory block.
     * </p>
     *
     * @param rawBlockData is the block of bytes where the access code is stored in
     * @return String
     * @throws Exception
     */
    String getAccessCode(byte[] rawBlockData) throws Exception;

    // Utility Functions

    /**
     * parseBlockSubSequence
     * <p>
     * parseBlockSubSequence parses part of a block data into unsigned-integer (32-bits) format, in string
     * representation.
     * </p>
     *
     * @param rawBlockData is the data block to parse from
     * @param startIndex is the start of the segment to parse from
     * @param endIndex is the end of the segment to parse from
     * @return String
     * @throws Exception
     */
    String parseBlockSubSequence(byte[] rawBlockData, int startIndex, int endIndex) throws Exception;

    /**
     * bytesToHexString
     * <p>
     * bytesToHexString parses raw bytes to hex representation.
     * </p>
     *
     * @param byteArray is the series of bytes to parse from
     * @return String
     * @throws Exception
     */
    String bytesToHexString(byte[] byteArray) throws Exception;

    /**
     * bytesToLong
     * <p>
     * bytesToLong parses raw bytes to unsigned-int32 representation.
     * </p>
     *
     * @param byteArray is the series of bytes to parse from
     * @return Long
     * @throws Exception
     */
    Long bytesToLong(byte[] byteArray) throws Exception;

    /**
     * displayADPUExchange
     * <p>
     * displayADPUExchange displays the ADPUCommand and ADPUResponse exchanged in the session.
     * </p>
     *
     * @param commandMessage is the CommandADPU sent
     * @param responseMessage is the ResponseADPU received
     * @param title is the alias for the session or exchange segment
     * @throws Exception
     */
    void displayAPDUExchange(CommandAPDU commandMessage, ResponseAPDU responseMessage, String title) throws Exception;

    // TODO: Make log function for ADPU exchange

}
