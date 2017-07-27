package database.io;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class ExcelRW {
    String fileName = "";

    public void setPath(String fileName) {
        this.fileName = fileName;
    }

    public boolean fileExist() {
        if (this.fileName.equals("")) {
            return false;
        }
        File f = new File(this.fileName);
        if (f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;
    }

    public void createNewFile() throws IOException {
        if (this.fileName.equals("")) {
            return;
        }

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();
        setHeader(sheet);

        FileOutputStream fileOut = new FileOutputStream(this.fileName);
        wb.write(fileOut);
        fileOut.close();
    }

    protected void write(int column, String cellValue, boolean isNew) throws Exception {
        if (!fileExist()) {
            throw new FileNotFoundException();
        }
        FileInputStream fileIn = new FileInputStream(this.fileName);
        Workbook wb = new XSSFWorkbook(fileIn);
        fileIn.close();

        Sheet sheet = wb.getSheetAt(0);
        int lastRow = sheet.getLastRowNum();
        Row row = null;
        if (isNew) {
            row = sheet.createRow(lastRow + 1);
            row.createCell(0).setCellValue(lastRow + 1);
        } else {
            row = sheet.getRow(lastRow);
        }

        row.createCell(column).setCellValue(cellValue.toUpperCase());

        FileOutputStream fileOut = new FileOutputStream(this.fileName);
        wb.write(fileOut);
        fileOut.close();
    }

    protected Set<String> getMatricNumbers() throws Exception{
        Set<String> matricNumbers = new HashSet<String >();
        if (!fileExist()) {
            return matricNumbers;
        }

        FileInputStream fileIn = new FileInputStream(fileName);
        Workbook wb = new XSSFWorkbook(fileIn);
        Sheet sheet = wb.getSheetAt(0);
        int lastRow = sheet.getLastRowNum();
        int currentRow = 1;

        while (currentRow <= lastRow) {
            String matricNumber = sheet.getRow(currentRow).getCell(2).toString();
            matricNumbers.add(matricNumber);
            currentRow++;
        }

        return matricNumbers;
    }

    protected void setHeader(Sheet sheet) {
        Row row = sheet.createRow(0);

        row.createCell(0).setCellValue("S/N");
        row.createCell(1).setCellValue("Name");
        row.createCell(2).setCellValue("Matric Number");
        row.createCell(3).setCellValue("Faculty");
        row.createCell(4).setCellValue("TimeStamp");
        row.createCell(5).setCellValue("Details");

        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(4);
    }
}
