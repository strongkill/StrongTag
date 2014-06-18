package net.strong.excel.api;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

  

public class ImportExcel<T> {  
    Class<T> clazz;  
  
    public ImportExcel(Class<T> clazz) {  
        this.clazz = clazz;  
    }  
  
    public Collection<T> importExcel(File file ,String...  pattern) {  
        Collection<T> dist = new ArrayList<T>();  
        try {  
            /** 
             * 类反射得到调用方法 
             */  
            // 得到目标目标类的所有的字段列表  
            Field filed[] = clazz.getDeclaredFields();  
            // 将所有标有Annotation的字段，也就是允许导入数据的字段,放入到一个map中  
            Map<String,Object> fieldmap = new HashMap<String,Object>();  
            // 循环读取所有字段  
            for (int i = 0; i < filed.length; i++) {  
                Field f = filed[i];  
                // 得到单个字段上的Annotation  
                ExcelAnnotation exa = f.getAnnotation(ExcelAnnotation.class);  
                // 如果标识了Annotationd的话  
                if (exa != null) {  
                    // 构造设置了Annotation的字段的Setter方法  
                    String fieldname = f.getName();  
                    String setMethodName = "set"  
                            + fieldname.substring(0, 1).toUpperCase()  
                            + fieldname.substring(1);  
                    // 构造调用的method，  
                    Method setMethod = clazz.getMethod(setMethodName,  
                            new Class[] { f.getType() });  
                    // 将这个method以Annotaion的名字为key来存入。  
                    fieldmap.put(exa.exportName(), setMethod);  
                }  
            }  
            /** 
             * excel的解析开始 
             */  
            // 将传入的File构造为FileInputStream;  
            FileInputStream in = new FileInputStream(file);  
            // // 得到工作表  
            HSSFWorkbook book = new HSSFWorkbook(in);  
            // // 得到第一页  
            HSSFSheet sheet = book.getSheetAt(0);  
            // // 得到第一面的所有行  
            Iterator<HSSFRow> row = sheet.rowIterator();  
  
            /** 
             * 标题解析 
             */  
            // 得到第一行，也就是标题行  
            HSSFRow title = row.next();  
            // 得到第一行的所有列  
            Iterator<HSSFCell> cellTitle = title.cellIterator();  
            // 将标题的文字内容放入到一个map中。  
            Map<Integer,String> titlemap = new HashMap<Integer,String>();  
            // 从标题第一列开始  
            int i = 0;  
            // 循环标题所有的列  
            while (cellTitle.hasNext()) {  
               HSSFCell cell = cellTitle.next();  
                String value = cell.getRichStringCellValue().getString();  
                titlemap.put(i, value);  
                i = i + 1;  
            }  
            /** 
             * 解析内容行 
             */  
            //用来格式化日期的DateFormat  
            SimpleDateFormat sf;  
            if(pattern.length<1)  
            {  
                sf=new SimpleDateFormat("yyyy-MM-dd");    
            }  
            else  
                sf=new SimpleDateFormat(pattern[0]);      
            while (row.hasNext()) {  
                // 标题下的第一行  
            	HSSFRow rown = row.next();  
  
                // 行的所有列  
                Iterator<HSSFCell> cellbody = rown.cellIterator();  
                // 得到传入类的实例  
                T tObject = clazz.newInstance();  
                int k = 0;  
                // 遍历一行的列  
                while (cellbody.hasNext()) {  
                    HSSFCell cell = cellbody.next();  
                    // 这里得到此列的对应的标题  
                    String titleString = (String) titlemap.get(k);  
                    // 如果这一列的标题和类中的某一列的Annotation相同，那么则调用此类的的set方法，进行设值  
                    if (fieldmap.containsKey(titleString)) {  
                        Method setMethod = (Method) fieldmap.get(titleString);  
                        //得到setter方法的参数  
                        Type[] ts = setMethod.getGenericParameterTypes();  
                        //只要一个参数  
                        String xclass = ts[0].toString();  
                        //判断参数类型  
                          
                      
                        if(xclass. equals("class java.lang.String"))  
                        {  
                            setMethod.invoke(tObject, cell.getRichStringCellValue().getString());  
                        }  
                        else if(xclass. equals("class java.util.Date"))  
                        {  
                            setMethod.invoke(tObject, sf.parse(cell.getRichStringCellValue().getString()));  
                        }  
                        else if(xclass. equals("class java.lang.Boolean"))  
                        {  
                            Boolean boolname=true;  
                            if(cell.getRichStringCellValue().getString().equals("否"))  
                                {  
                                boolname=false;  
                                }  
                            setMethod.invoke(tObject,boolname );  
                        }  
                        else if(xclass. equals("class java.lang.Integer"))  
                        {  
                            setMethod.invoke(tObject,new Integer( cell.getRichStringCellValue().getString()));  
                        }  
                          
                        else if(xclass. equals("class java.lang.Long"))  
                        {  
                            setMethod.invoke(tObject,new Long( cell.getRichStringCellValue().getString()));  
                        }  
                    }  
                    // 下一列  
                    k = k + 1;  
                }  
                dist.add(tObject);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
        return dist;  
    }  

}  