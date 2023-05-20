package com.reptile.jiaoji;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ExcelUtils {

    public static void main(String[] args) {
    }

    public static void write(ArrayList<HashMap<String, String>> mapList) {
        try {
            File file = new File("OCR/data.xlsx");

            // 定义一个工作薄（所有要写入excel的数据，都将保存在workbook中）
            XSSFWorkbook workbook = new XSSFWorkbook();

            // 创建一个sheet
            XSSFSheet sheet = workbook.createSheet("nan");

            for (int rowIndex = 0; rowIndex < mapList.size(); rowIndex++) {
                HashMap<String, String> map = mapList.get(rowIndex);

                // 接下来遍历要录入的数据（建议使用for，并且从第2行开始，也就是rowNumber为1，因为表头占了一行）
                XSSFRow row = sheet.createRow(rowIndex + 1);
                for (int i = 0; i < map.values().size(); i++) {
                    row.createCell(0).setCellValue(map.get("name"));
                    row.createCell(1).setCellValue(map.get("photo"));
                    row.createCell(2).setCellValue(map.get("where"));
                    row.createCell(3).setCellValue(map.get("nowWhere"));
                    row.createCell(4).setCellValue(map.get("gender"));
                    row.createCell(5).setCellValue(map.get("birthday"));
                    row.createCell(6).setCellValue(map.get("height"));
                    row.createCell(7).setCellValue(map.get("weight"));
                    row.createCell(8).setCellValue(map.get("edu"));
                    row.createCell(9).setCellValue(map.get("work"));
                    row.createCell(10).setCellValue(map.get("house"));
                    row.createCell(11).setCellValue(map.get("family"));
                    row.createCell(12).setCellValue(map.get("hobby"));
                    row.createCell(13).setCellValue(map.get("advantage"));

                    row.createCell(14).setCellValue(map.get("toAge"));
                    row.createCell(15).setCellValue(map.get("toHeight"));
                    row.createCell(16).setCellValue(map.get("toWeight"));
                    row.createCell(17).setCellValue(map.get("toHouse"));
                    row.createCell(18).setCellValue(map.get("toEdu"));
                    row.createCell(19).setCellValue(map.get("toFromWhere"));
                    row.createCell(20).setCellValue(map.get("toFamily"));
                    row.createCell(21).setCellValue(map.get("toShortcoming"));
                    row.createCell(22).setCellValue(map.get("toAdvantage"));
                    row.createCell(23).setCellValue(map.get("toMettle"));

                    row.createCell(25).setCellValue(map.get("id"));
                }
            }

            // 指定创建的excel文件名称
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
            // 执行写入操作
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<HashMap<String, Object>> get(String sheetName) {
        ArrayList<HashMap<String, Object>> mapList = new ArrayList<>();
        try {
            // 指定excel文件，创建缓存输入流
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream("OCR/data1.xlsx"));

            // 直接传入输入流即可，此时excel就已经解析了
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            // 选择要处理的sheet名称
            XSSFSheet sheet = workbook.getSheet(sheetName);

            // 第一行表头，单独处理

            HashMap<String, Object> map;
            // 迭代遍历sheet剩余的每一行
            for (int rowNum = 0; rowNum < sheet.getPhysicalNumberOfRows(); rowNum++) {
                map = new HashMap<>();
                if (rowNum == 0) { // 读取第一行（表头）
                    continue;
                } else { // 非表头（注意读取的时候要注意单元格内数据的格式，要使用正确的读取方法）
                    XSSFRow row = sheet.getRow(rowNum);
                    map.put("name", getWord(row, 0));
                    map.put("photo", photoStrToList(getWord(row, 1)));
                    map.put("where", getWord(row, 2));
                    map.put("nowWhere", getWord(row, 3));
                    String gender = getWord(row, 4);
                    map.put("gender", "男".equals(gender) ? 1 : 0);
                    map.put("birthday", getWord(row, 5));
                    map.put("height", getWord(row, 6));
                    map.put("weight", getWord(row, 7));
                    map.put("edu", getWord(row, 8));
                    map.put("work", getWord(row, 9));
                    map.put("house", getWord(row, 10));
                    map.put("family", getWord(row, 11));
                    map.put("hobby", getWord(row, 12));
                    map.put("advantage", getWord(row, 13));

                    map.put("toAge", getWord(row, 14));
                    map.put("toHeight", getWord(row, 15));
                    map.put("toWeight", getWord(row, 16));
                    map.put("toHouse", getWord(row, 17));
                    map.put("toEdu", getWord(row, 18));
                    map.put("toFromWhere", getWord(row, 19));
                    map.put("toFamily", getWord(row, 21));
                    map.put("toShortcoming", getWord(row, 21));
                    map.put("toAdvantage", getWord(row, 22));
                    map.put("toMettle", getWord(row, 23));
                }

                mapList.add(map);
            }

            workbook.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapList;
    }

    private static String getWord(XSSFRow row, int num) {
        XSSFCell cell = row.getCell(num);
        if (cell == null) {
            return "";
        } else {
            return cell.getStringCellValue();
        }
    }

    private static ArrayList<String> photoStrToList(String s) {
        ArrayList<String> list = new ArrayList<>();
        if (s.length() == 0) return list;

        String[] split = s.split(",");
        for (String item : split) {
            String photo = item.trim().replace("[", "").replace("]", "");
            list.add(photo);
        }

        return list;
    }

}
