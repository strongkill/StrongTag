package net.strong.excel.api;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
  
  

public class ExcelExport<T> {  
    //格式化日期  
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
    /** 
     *  
     * @param title 标题 
     * @param dataset 集合 
     * @param out  输出流 
     */  
    public void exportExcel(String title, Collection<T> dataset,  
            OutputStream out) {  
        // 声明一个工作薄  
        try {  
            //首先检查数据看是否是正确的  
            Iterator<T> its = dataset.iterator();  
            if(dataset==null||!its.hasNext()||title==null||out==null)  
            {  
                throw new Exception("传入的数据不对！");  
            }  
            //取得实际泛型类  
            T ts = its.next();  
            Class<? extends Object> tCls = ts.getClass();  
            HSSFWorkbook workbook = new HSSFWorkbook();  
            // 生成一个表格  
            HSSFSheet sheet = workbook.createSheet(title);  
            // 设置表格默认列宽度为15个字节  
            sheet.setDefaultColumnWidth((short) 15);// .setDefaultColumnWidth(15);  
            // 生成一个样式  
            HSSFCellStyle style = workbook.createCellStyle();  
            // 设置标题样式  
            style = ExcelStyle.setHeadStyle(workbook, style);  
  
            // 得到所有字段  
          
            Field filed[] = ts.getClass().getDeclaredFields();  
            // 标题  
            List<String> exportfieldtile = new ArrayList<String>();  
            // 导出的字段的get方法  
            List<Method> methodObj = new ArrayList<Method>();  
            // 遍历整个filed  
            for (int i = 0; i < filed.length; i++) {  
                Field f = filed[i];  
                ExcelAnnotation exa = f.getAnnotation(ExcelAnnotation.class);  
                // 如果设置了annottion  
                if (exa != null) {  
                    String exprot = exa.exportName();  
                    // 添加到标题  
                    exportfieldtile.add(exprot);  
                    // 添加到需要导出的字段的方法  
                    String fieldname = f.getName();  
                    String getMethodName = "get"  
                            + fieldname.substring(0, 1).toUpperCase()  
                            + fieldname.substring(1);  
                      
                    Method getMethod = tCls.getMethod(getMethodName,  
                            new Class[] {});  
                      
                      
                    methodObj.add(getMethod);  
                }  
            }  
            // 产生表格标题行  
            HSSFRow row = sheet.createRow(0);  
            for (short i = 0; i < exportfieldtile.size(); i++) {  
                HSSFCell cell = row.createCell(i);  
                cell.setCellStyle(style);  
                HSSFRichTextString text = new HSSFRichTextString(  
                        exportfieldtile.get(i));  
                cell.setCellValue(text);  
            }  
  
      
            int index = 0;  
              
            // 循环整个集合  
              
            while (its.hasNext()) {  
                //从第二行开始写，第一行是标题  
                index++;  
                row = sheet.createRow(index);  
                T t = its.next();  
                for (short k = 0; k < methodObj.size(); k++) {  
                    HSSFCell cell = row.createCell(k);  
                    Method getMethod=methodObj.get(k);  
                    Object value = getMethod.invoke(t, new Object[] {});  
                    String textValue = getValue(value);  
                    cell.setCellValue(new HSSFRichTextString(textValue));  
                }  
  
            }  
            workbook.write(out);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
    }  
  
    public String getValue(Object value) {  
        String textValue = "";  
        if (value == null)  
            return textValue;  
  
        if (value instanceof Boolean) {  
            boolean bValue = (Boolean) value;  
            textValue = "是";  
            if (!bValue) {  
                textValue = "否";  
            }  
        } else if (value instanceof Date) {  
            Date date = (Date) value;  
          
            textValue = sdf.format(date);  
        } else  
            textValue = value.toString();  
  
        return textValue;  
    }  
  
 
}  
