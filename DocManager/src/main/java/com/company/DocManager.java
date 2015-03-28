package com.company;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocManager {

    public static File excelWorkerFile;
    public static File docPatternFile;
    public static File docResults;
    private static Map<Integer, String> keys;
    private static final DataFormatter FORMATTER = new DataFormatter();



    public ArrayList<Map<String, String>> readWorkerList(String filePath) {
        ArrayList<Map<String, String>> values = new ArrayList<Map<String, String>>();
        try {
            FileInputStream file = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(file);//get instance of workbook for file
            XSSFSheet sheet = workbook.getSheetAt(0);//get the first sheet
            //iterate through each row from the first sheet
            Iterator<Row> rowIterator = sheet.rowIterator();

            Row row0 = sheet.getRow(0);
            keys = new HashMap<Integer, String>();
            for(int i = 0; i < row0.getLastCellNum(); i++) {
                String rowName = row0.getCell(i).getStringCellValue();
                keys.put(i, rowName);
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();//return current element
                if(row.getRowNum() > 0) {
                    short lastCellNum = row.getLastCellNum();
                    Map<String, String> rowValues = new HashMap<String, String>();
                    for (int i = 0; i < lastCellNum; i++) {
                        String name = keys.get(i);
                        rowValues.put(name, getObject(row.getCell(i)));
                    }
                    values.add(rowValues);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Alarm! File is not found!");
        } catch (IOException e) {
            System.out.println("Alarm! IOException");
        }
        return values;
    }

    public void changePattern(String filePath, String fileOutPath, ArrayList<Map<String, String>> values) {
        try {
            FileInputStream file = new FileInputStream(filePath);
            XWPFDocument document = new XWPFDocument(file);
           // XWPFDocument resultsDoc = new XWPFDocument();
            //through arrayList of values that will replace
            for(Map<String, String> map : values) {
                //through the document which will be replaced
                for (XWPFParagraph p : document.getParagraphs()) {
                    Pattern pattern1 = Pattern.compile("[a-zA-Z_-]+");
                    for (XWPFRun r : p.getRuns()) {
                        int pos = r.getTextPosition();
                        String text = r.getText(pos);
                        Matcher matcher = pattern1.matcher(text);
                        if (matcher.matches()) {
                            Map<String, String> toReplace = new HashMap<String, String>();
                            while (matcher.find()) {
                                String replaceable = matcher.group(1);//that in {}, 0 - all with $ and {}
                                String replacing = map.get(replaceable);
                                if (replacing != null) {
                                    toReplace.put(replaceable, replacing);
                                }
                            }
                            for (Entry<String, String> entryToReplace : toReplace.entrySet()) {
                                text = text.replaceAll(entryToReplace.getKey(), entryToReplace.getValue());
                            }
                            r.setText(text, pos);
                        }
                    }
                    //XWPFParagraph newPar = resultsDoc.createParagraph();
                    //int newParPos = resultsDoc.getPosOfParagraph(newPar);
                    //resultsDoc.setParagraph(p, newParPos);
                }
                //todo add new pages for new doc
                //todo не проверять каждый раз ключи, они одинаковые для каждой строки
            }
            document.write(new FileOutputStream(fileOutPath));
        }catch (FileNotFoundException e) {
            System.out.println("Alarm! File is not found!");
        } catch (IOException e) {
            System.out.println("Alarm! IOException");
        }
    }

    private String getObject(Cell cell) {
        return FORMATTER.formatCellValue(cell);
    }

    public void showWorkers(ArrayList<Map<String, String>> workList) {
        int rowNumb = workList.size();
        for(int i = 0; i < rowNumb; i++){
            Map<String, String> row = workList.get(i);
            System.out.println(row.toString());
        }
    }
}
