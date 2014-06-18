package net.strong.taglib.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.strong.util.Helper;

//import net.strong.database.PoolBean;
/**
 *
 * <p>Title: 多级类别列表读取优化</p>
 * <p>Description:首先将多级类别列表读入List对象中，然后直接对Lit对象进行操作，
 * 本类主要用于菜单列表，下拉列表等多级列表中。 </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 */
public class ClassItems {
  private ArrayList<HashMap<String, Object>> itemList = null;
  public String idFieldName = "bas_id";
  public String parentFieldName = "parent_id";
  public HashMap<String, Object> nextMap = null; //下一个记录

  public boolean isLeaf = false; //是否直接找到指定的父ID的记录，否为通过p_p_id到到相应记录
  public boolean isNext = false;
  private int level = 0; //多级类别的深度

  public void clearData()
  {
    nextMap = null;
    Iterator<HashMap<String, Object>> iterator = ((ArrayList<HashMap<String, Object>>) itemList).iterator();
    if (iterator.hasNext()) {
      HashMap<String, Object> element = (HashMap<String, Object>)iterator.next();
      element.clear();
      element = null;
      iterator.remove();//移除列表中当前对象
    }
    iterator = null;
    itemList = null;
  }
  /**
   * 获取类别列表数据，将所有满足条件的数据先取出存入一个List对象中。
   * @param pool
   * @param sql
   */
/*  public void initData(PoolBean pool,String sql)
  {
    Connection con = null;
    try
    {
      con = pool.getConnection();
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      ResultSetMetaData rsmd = rs.getMetaData(); //获取数据元
      int colCount = rsmd.getColumnCount();//当前字段数
      if(itemList== null)
        itemList = new ArrayList();
      while(rs.next())
      {
        HashMap row = new HashMap();
//        HashMap row = new HashMap();
        for(int i=1;i<=colCount;i++)
        {
          //所有名称转换为小写方式
          String name = rsmd.getColumnName(i).toLowerCase() ;
          Object obj = rs.getObject(i);
          if(obj instanceof java.lang.String)
          {
            String t_str = null;
            if(obj == null)
              t_str = "";
            else
              t_str = String.valueOf(obj).trim();
            row.put(name.trim(),t_str);
          }
          else
          {
            row.put(name.trim(),obj);
          }
        }
        itemList.add(row);
        row = null;
      }
      rs.close();
      stmt.close();
      con.close();
    }
    catch(Exception e)
    {
      System.out.println("get classItem exception:"+e.getMessage());
    }
  }*/
  /**
   * 获取类别列表数据，将所有满足条件的数据先取出存入一个List对象中。
   * @param con
   * @param sql
   * @throws SQLException
   */
  public void initData(Connection con,String sql) throws SQLException
  {
		long cost_1 = System.currentTimeMillis();
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery(sql);
	long cost_2 = System.currentTimeMillis();
	if(cost_2-cost_1 >= 1000*3){
			System.out.println("*********************=== PAY ATTENTION:SLOW SQL(Cost:" +(cost_2-cost_1)+ " Millis) ===*********************");
		System.out.println(sql);
		System.out.println("**********************************************END*************************************************");
	}
    ResultSetMetaData rsmd = rs.getMetaData(); //获取数据元
    int colCount = rsmd.getColumnCount();//当前字段数
    if(itemList== null)
      itemList = new ArrayList<HashMap<String, Object>>();
    while(rs.next())
    {
      HashMap<String, Object> row = new HashMap<String, Object>();
      for(int i=1;i<=colCount;i++)
      {
        String name = rsmd.getColumnLabel(i).toLowerCase();
        if(name==null)
        	name = rsmd.getColumnName(i).toLowerCase() ;

        Object obj = rs.getObject(i);
        if(obj instanceof java.lang.String)
        {
          String t_str = null;
          if(obj == null)
            t_str = "";
          else
            t_str = String.valueOf(obj).trim();
          row.put(name.trim(),t_str);
        }
        else
        {
          row.put(name.trim(),obj);
        }
      }
      itemList.add(row);
      row = null;
    }
    Helper.cleanup(stmt,con,rs);
  }

  public HashMap<String, Object> getDataItem(String p_id,String p_p_id,String hasParent)
  {
    isNext = false;
    int list_count = itemList.size();
    HashMap<String, Object> result_map = null;
    if("true".equalsIgnoreCase(hasParent))
      return getDataItem(p_id,p_p_id);
    else
    {
      HashMap<String,Object> t_map = null;
      int id_remove = -1; //需要移除的记录的ID
      for(int i=0;i<list_count;i++)
      {
        t_map = (HashMap<String,Object>) itemList.get(i);
        String del = String.valueOf(t_map.get("del"));
        if("true".equals(del))
        {
          t_map = null;
          continue;
        }
        else
        {
          id_remove = i;
          result_map = t_map;
          break;
        }

      }
      if(id_remove>=0)// && is_leaf)
      {
        itemList.remove(id_remove);
        result_map.put("del","true");
        itemList.add(id_remove,result_map);
//      itemList.set(id_remove,result_map);
      }
      return result_map;
    }
  }

  /**
   * 获取多级列表中的某一记录
   * @param p_id　记录的父ID
   * @param p_p_id　记录的父ID的父ID，本参数是为了找不到p_id对应的记录时，找下一个p_p_id对应的记录
   * @return　以HashMap保存的记录数据
   */
  public HashMap<String, Object> getDataItem(String p_id,String p_p_id)
  {
    isNext = false;
    //int list_count = itemList.size();
    HashMap<String, Object> result_map = null;
    if(nextMap==null)
    {
      result_map = getItem(p_id,p_p_id);
    }
    else
    {
      result_map = nextMap; //当前所需取的记录即为上次的“nextMap"
    }
    //获取nextMap
    if(result_map == null)
      return null;

    String t_id = String.valueOf(result_map.get(idFieldName.toLowerCase() ));
//    isLeaf = isLeaf(t_id);
    String t_p_id = String.valueOf(result_map.get(parentFieldName.toLowerCase() ));
    String str_isleaf = String.valueOf(result_map.get("isLeaf"));
    isNext = true;
    if("true".equals(str_isleaf))
    {
      String tt_p_id = getPID(t_p_id);
      level--;
      HashMap<String, Object> t_map = null;
      int while_level = 0;
      //循环向上取数
      while((t_map = getItem(t_p_id,tt_p_id))==null)// || while_level < 10)
      {
        tt_p_id = getPID(tt_p_id);
        while_level ++;
        level --;
        t_map =null;
        if(while_level>10)
          break;
      }
      nextMap = t_map;
      t_map = null;
    }
    else
    {
      nextMap = getItem(t_id, t_p_id);
    }

    return result_map;
  }


  /**
   * 获取父ID为p_id的第一条记录，如果已不存在记录，则获取父ID为p_p_id的第一条记录
   * @param p_id
   * @param p_p_id
   * @return
   */

  private HashMap<String, Object> getItem(String p_id,String p_p_id)
  {
    //int list_count = itemList.size();
   // HashMap t_map = null;
    HashMap<String, Object> result_map = null;
    result_map = getItem(p_id);//获取父ID为p_id的第一条记录
    if (result_map == null) {
      //未找到所需记录，则表示已经没有父ID为p_id的记录了，则删除当前记录，并取出以p_p_id为父ID的记录
      result_map = getItem(p_p_id); //获取父ID为p_p_id的第一条记录

      if(result_map !=null)
      {
       // String t_id = String.valueOf(result_map.get(idFieldName.toLowerCase() ));
//        isLeaf = isLeaf(t_id);
        if("0".equals(p_p_id))
        {
          level = 1;
        }
        else
        {
//          level--;
        }
        result_map.put("level",String.valueOf(level));
      }
    }
    else
    {
      //String t_id = String.valueOf(result_map.get(idFieldName.toLowerCase() ));
      String t_p_id = String.valueOf(result_map.get(parentFieldName.toLowerCase() ));
//      isLeaf = isLeaf(t_id);
      //if(!isLeaf)
      if(p_id!=null && p_id.equals(t_p_id))
      {
        level++;
      }
      result_map.put("level",String.valueOf(level));
    }
    return result_map;
  }

  /**
   * 判断id对应的记录是否为叶子结点
   * @param id
   * @return
   */
  private boolean isLeaf(String id)
  {
    HashMap<String, Object> t_map = null;
    boolean result = true;
    int list_count = itemList.size();
    for(int i=0;i<list_count;i++)
    {
      t_map = (HashMap<String, Object>)itemList.get(i);
      String t_id = String.valueOf(t_map.get(parentFieldName.toLowerCase() ));
      if(t_id!=null && t_id.equals(id))
      {
        result = false;
        break;
      }
      t_map = null;
    }
    t_map = null;
    return result;
  }

  /**
   * 获取指定ID对应记录的PID
   * @param id
   * @return
   */
  private String getPID(String id)
  {
    if(id == null)
       return "0";
     int list_count = itemList.size();
     HashMap<String, Object> t_map = null;
     String result = "0";
     for(int i=0;i<list_count;i++)
     {
       t_map = (HashMap<String, Object>) itemList.get(i);
       String p_value = String.valueOf(t_map.get(idFieldName.toLowerCase() ));
       if(p_value !=null)
       {
         if(id.equals(p_value.trim() ))
         {
           //找到所选记录
           result = String.valueOf(t_map.get(parentFieldName.toLowerCase() ));
           break;
         }
       }
       t_map = null;
     }
     t_map = null;
     return result;
  }

  /**
   * 获取ID值等于id的记录所在的位置
   * @param id
   * @deprecated
   * @return
   */
  /*
  private int getItemIndex(String id)
  {
    if(id == null)
       return -1;
     int list_count = itemList.size();
     HashMap t_map = null;
     int result = -1;
     for(int i=0;i<list_count;i++)
     {
       t_map = (HashMap) itemList.get(i);

       //如果标志为已删除，则跳过
       String del = String.valueOf(t_map.get("del"));
       if("true".equals(del))
       {
         t_map = null;
         continue;
       }

       String p_value = String.valueOf(t_map.get(idFieldName.toLowerCase() ));
       if(p_value !=null)
       {
         if(id.equals(p_value.trim() ))
         {
           //找到所选记录
           result = i;
           break;
         }
       }
       t_map = null;
     }
     t_map = null;
     return result;
  }
*/
  /**
   * 获取父ID为p_id的第一条记录
   * @param p_id
   * @return
   */
  private HashMap<String,Object> getItem(String p_id)
  {
    if(p_id == null)
      return null;
    int list_count = itemList.size();
    HashMap<String,Object> t_map = null;
    HashMap<String, Object> result_map = null;
    int id_remove = -1; //需要移除的记录的ID
    boolean is_leaf = false;
    for(int i=0;i<list_count;i++)
    {
      t_map = (HashMap<String,Object>) itemList.get(i);

      //如果标志为删除，则跳过
      String del = String.valueOf(t_map.get("del"));
      if("true".equals(del))
      {
        t_map = null;
        continue;
      }

      String p_value = String.valueOf(t_map.get(parentFieldName.toLowerCase()));
      if(p_value !=null)
      {
        if(p_id.equals(p_value.trim() ))
        {
          //找到所选记录
          result_map = t_map;
          id_remove = i;
          String t_id = String.valueOf(t_map.get(idFieldName.toLowerCase() ));
          is_leaf = isLeaf(t_id);
          result_map.put("isLeaf",Boolean.valueOf(is_leaf));

          break;
          //在list中删除所选的记录
          //itemList.remove(i);
        }
      }
      t_map = null;
    }
    if(id_remove>=0)// && is_leaf)
    {
      itemList.remove(id_remove);
      result_map.put("del","true");
      itemList.add(id_remove,result_map);
//      itemList.set(id_remove,result_map);
    }
    t_map = null;
    return result_map;
  }
  public void setIdFieldName(String idFieldName) {
    this.idFieldName = idFieldName;
  }
  public void setParentFieldName(String parentFieldName) {
    this.parentFieldName = parentFieldName;
  }
}