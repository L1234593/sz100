package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.IOException;

public class ReadFormExcel {
    @Test
    public void readFromExcel() throws IOException {
//        创建工作薄，构建方法文件路径
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\read.xlsx");
//        获取工作表
        XSSFSheet sht = workbook.getSheetAt(0);
        // sht.getPhysicalNumberOfRows(); // 物理行数
        // sht.getLastRowNum(); // 最后一行的下标
        // fori遍历时使用getLastrowNum()
        // 遍历工作表获得行对象
        for (Row row : sht) {
//            遍历行对象获取单元格
            for (Cell cell : row) {
                int cellType = cell.getCellType();
                if (cell.CELL_TYPE_NUMERIC == cellType){
//                    数值类型的单元格
                    System.out.println(cell.getNumericCellValue()+",");
                }else {
//                    从单元格取值
                    System.out.println(cell.getStringCellValue()+",");
                }
            }
            System.out.println();
        }
//        关闭工作薄
        workbook.close();
    }
}
