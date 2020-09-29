package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

public class WriteExcel {
    @Test
    public void createExcel() throws Exception{
//        创建工作薄，内存中
        XSSFWorkbook workbook = new XSSFWorkbook();
//        创建工作薄
        XSSFSheet sht = workbook.createSheet("测试写excel");
//        在工作薄下创建行
        Row row = sht.createRow(0);//行的下标是从0开始
//        使用行创建单元格
        Cell cell = row.createCell(0);//单元格的下标也是从0开始，多个单元格合并成为1个单元格
//        给单元格赋值
//        表头
        cell.setCellValue("姓名");
        row.createCell(1).setCellValue("年龄");
        row.createCell(1).setCellValue("所在地");

        row = sht.createRow(1);
        row.createCell(0).setCellValue("小名");
        row.createCell(1).setCellValue(20);
        row.createCell(1).setCellValue("深圳");
//        保存工作薄，持久化本地硬盘里
        workbook.write(new FileOutputStream(new File("D:\\create.xlsx")));
//        关闭工作薄
        workbook.close();
    }
}
