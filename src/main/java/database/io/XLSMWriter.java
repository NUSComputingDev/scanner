package database.io;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class XLSMWriter {
    private String fileName = "";
    private Integer sheetNumber = 0;
    private Integer row;
    private Integer column;

    public void setFile(String fileName) {
        this.fileName = fileName;
    }

    public void setConfiguration(Integer sheetNumber, Integer row, Integer column) {
        this.sheetNumber = sheetNumber;
        this.row = row;
        this.column = column;
    }

    public boolean isValid() {
        if (sheetNumber == null || row == null || column == null || fileName.equals("")) {
            return false;
        }
        return true;
    }

    public void write(String matricNumber) throws Exception {
        try {
            File file = new File(fileName);
            FileInputStream inputStream = new FileInputStream(file);

            Workbook wb = new XSSFWorkbook(OPCPackage.open(inputStream));

            Sheet sheet = wb.getSheetAt(sheetNumber);
            Cell cell = sheet.getRow(row).getCell(column);
            cell.setCellValue(matricNumber);

            FileOutputStream fileOut = new FileOutputStream(fileName);
            wb.write(fileOut);
            fileOut.close();

        } catch (FileNotFoundException e) {
            throw e;
        } catch (InvalidFormatException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }
}
