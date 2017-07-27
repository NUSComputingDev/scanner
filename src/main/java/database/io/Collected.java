package database.io;

import objects.Scan;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Collected extends ExcelRW {
    private Set<String> collectionList = new HashSet<>();

    public Collected() {
        super.fileName = "collected_list.xlsx";
    }

    public void recordCollection(Scan card) {
        try {
            write(card.getFullName(), card.getMatricNumber(), card.getFaculty(), card.getDateTimeStamp());
        } catch (Exception e) {
            try {
                Logger logger = Logger.getLogger("log");
                FileHandler fh = new FileHandler("./scanner.log");
                logger.addHandler(fh);
                logger.severe(Arrays.toString(e.getStackTrace()));
            } catch (Exception except) {
                return;
            }
        }
    }

    private void write(String name, String matricNumber, String faculty, String timeStamp) throws Exception {
        if (!super.fileExist()) {
            super.createNewFile();
        }
        if (hasCollected(matricNumber)) {
            return;
        }

        super.write(1, name, true);
        super.write(2, matricNumber, false);
        super.write(3, faculty, false);
        super.write(4, timeStamp, false);

        collectionList.add(matricNumber.toUpperCase());
    }

    public boolean hasCollected(String matricNumber) {
        return collectionList.contains(matricNumber);
    }

    public void updateList() {
        try {
            Set<String> newSet = super.getMatricNumbers();
            collectionList.addAll(newSet);
        } catch (Exception e) {
            return;
        }
    }
}
