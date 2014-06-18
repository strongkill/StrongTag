package net.strong.excel.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.strong.excel.api.ExcelExport;
import net.strong.excel.api.ImportExcel;
import net.strong.excel.test.Testpojo;

public class Test {
	   public static void mainout(String[] args) throws Exception {  
	          
	        //构造一个模拟的List来测试，实际使用时，这个集合用从数据库中查出来  
	        List list = new ArrayList();  
	        for (int i = 0; i < 5000; i++) {  
	            Testpojo pojo = new Testpojo();  
	            pojo.setLoginname("登录名"+i);  
	            pojo.setUsername("用户名"+i);  
	            pojo.setMoney(new Long(1000+i));  
	            pojo.setCreatetime(new Date());  
	            pojo.setAge(28);  
	            list.add(pojo);  
	        }  
	        //构造输出对象，可以从response输出，直接向用户提供下载  
	        OutputStream out = new FileOutputStream("D:\\testOne.xls");  
	        //开始时间  
	        Long l = System.currentTimeMillis();  
	        //注意  
	        ExcelExport<Testpojo> ex = new ExcelExport<Testpojo>();  
	        //  
	        ex.exportExcel("测试", list, out);  
	        out.close();  
	        //结束时间  
	        Long s = System.currentTimeMillis();  
	        System.out.println("总共耗时：" + (s - l));  
	  
	    }  
	   
    public static void main(String[] args) {  
        ImportExcel<Testpojo> test = new ImportExcel(Testpojo.class);  
        File file = new File("D:\\testOne.xls");  
        Long befor = System.currentTimeMillis();  
        List<Testpojo> result = (ArrayList) test.importExcel(file);  
  
        Long after = System.currentTimeMillis();  
        System.out.println("此次操作共耗时：" + (after - befor) + "毫秒");  
         for (int i = 0; i < result.size(); i++) {  
             Testpojo testpojo=result.get(i);  
         System.out.println("导入的信息为："+testpojo.getLoginname()+  
                 "----"+testpojo.getAge()+"---"+testpojo.getMoney()+"-----"+testpojo.getCreatetime());  
         }  
  
        System.out.println("共转化为List的行数为：" + result.size());  
    }  
}
