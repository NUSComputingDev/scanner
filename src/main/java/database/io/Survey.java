package database.io;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.HashMap;

public class Survey extends ExcelRW {
    public HashMap<String, String> surveyList = new HashMap<>();

    public void updateList() {
        if (!super.fileExist()) {
            return;
        }

        try {
            FileInputStream fileIn = new FileInputStream(super.fileName);
            Workbook wb = new XSSFWorkbook(fileIn);
            Sheet sheet = wb.getSheetAt(0);
            int lastRow = sheet.getLastRowNum();
            int currentRow = 1;

            while (currentRow <= lastRow) {
                String name = sheet.getRow(currentRow).getCell(1).toString();
                String matricNumber = sheet.getRow(currentRow).getCell(2).toString();
                surveyList.put(matricNumber, name);
                currentRow++;
            }
        } catch (Exception e) {
            return;
        }
    }

    public boolean doneSurvey(String matricNumber) {
        return surveyList.containsKey(matricNumber);
    }

    public String getName(String matricNumber) {
        return surveyList.get(matricNumber);
    }
}
