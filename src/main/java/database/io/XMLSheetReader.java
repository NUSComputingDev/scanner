package database.io;

import java.util.logging.*;
import objects.Scan;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class XMLSheetReader {

    public ArrayList<Scan> getRegisteredScans(String filepath) throws IOException {

        ArrayList<Scan> entries = new ArrayList<>();

        URL url = getClass().getResource(filepath);
        FileInputStream file = new FileInputStream(new File(url.getPath()));

        //Get the workbook instance for XLS file
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        //Get first sheet from the workbook
        XSSFSheet sheet = workbook.getSheetAt(0);

        //Get iterator to all the rows in current sheet
        Iterator<Row> rowIterator = sheet.iterator();

        int rowCount = 0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (rowCount == 0) {
                checkHeader(row);
            } else {
                Scan currentLine = getEntry(row);
                entries.add(currentLine);
            }
            rowCount++;
        }

        return entries;
    }

    private void checkHeader(Row row) {

        boolean isHeader = false;

        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {

            Cell cell = cellIterator.next();

            switch (cell.getColumnIndex()) {
                case 0:
                    isHeader = true;
                    isHeader = isHeader && cell.getStringCellValue().toUpperCase().equals("S/N");
                    break;
                case 1:
                    isHeader = isHeader && cell.getStringCellValue().toUpperCase().equals("NAME");
                    break;
                case 2:
                    isHeader = isHeader && cell.getStringCellValue().toUpperCase().equals("MATRIC NUMBER");
                    break;
                case 3:
                    isHeader = isHeader && cell.getStringCellValue().toUpperCase().equals("ACCESS CODE");
                    break;
                case 4:
                    isHeader = isHeader && cell.getStringCellValue().toUpperCase().equals("TIMESTAMP");
                    break;
                case 5:
                    isHeader = isHeader && cell.getStringCellValue().toUpperCase().equals("DETAILS");
                    break;
                default:
                    break;
            }
        }

        if (!isHeader) {
            throw new RuntimeException("Error! Incorrect file.");
        }

    }

    public static void main(String[] args) throws IOException {
        XMLSheetReader reader = new XMLSheetReader();
        reader.getRegisteredScans("/documents/collected_list.xlsx");
    }

    public Scan getEntry(Row row) {
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {

            Cell cell = cellIterator.next();

            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    Logger.getLogger("").info(cell.getBooleanCellValue() + "\t\t");
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    Logger.getLogger("").info(cell.getNumericCellValue() + "\t\t");
                    break;
                case Cell.CELL_TYPE_STRING:
                    Logger.getLogger("").info(cell.getStringCellValue() + "\t\t");
                    break;
            }
        }
        return null;
    }
}
