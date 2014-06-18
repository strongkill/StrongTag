package net.strong.lang;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import oracle.sql.CLOB;

public class StMap {
	public static HashMap<String,Object> ResultSetToHashMap(ResultSet rs) throws Exception {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		if(rs!=null){
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rs.getMetaData().getColumnCount();
			if (rs.next()) {
				for (int i = 1; i <= colCount; i++) {
					String name = rsmd.getColumnLabel(i).toLowerCase();
					if (name == null || name.length() == 0)
						name = rsmd.getColumnName(i).toLowerCase();
					String type_name = rsmd.getColumnTypeName(i).toLowerCase();
					if ("text".equals(type_name)) {
						String tt_str = rs.getString(i);
						if (tt_str == null)
							tt_str = "";
						else
							tt_str = tt_str.trim();
						hm.put(name.trim(), tt_str);
					} else if ("clob".equalsIgnoreCase(type_name)) {
						try {
							CLOB tm = (CLOB) rs.getObject(i);
							String tmp = null;
							if (tm != null) {
								tmp = tm.getSubString(1, (int) tm.length());
								tm = null;
							}
							hm.put(name.trim(), tmp);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else {
						Object obj = rs.getObject(i);
						if (obj instanceof java.lang.String) {
							String t_str = null;
							t_str = String.valueOf(obj).trim();
							hm.put(name.trim(), t_str);
						} else {
							hm.put(name.trim(), obj);
						}
					}
				}
			}
		}
		return hm;
	}

	public static ArrayList<HashMap<String,Object>> ResultSetToList(ResultSet rs) throws Exception {
		ArrayList<HashMap<String, Object>> al = new ArrayList<HashMap<String, Object>>();
		if(rs!=null){
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rs.getMetaData().getColumnCount();
			HashMap<String, Object> hm = null;
			while (rs.next()) {
				hm = new HashMap<String, Object>();
				for (int i = 1; i <= colCount; i++) {
					String name = rsmd.getColumnLabel(i).toLowerCase();
					if (name == null || name.length() == 0)
						name = rsmd.getColumnName(i).toLowerCase();
					String type_name = rsmd.getColumnTypeName(i).toLowerCase();
					if ("text".equals(type_name)) {
						String tt_str = rs.getString(i);
						if (tt_str == null)
							tt_str = "";
						else
							tt_str = tt_str.trim();
						hm.put(name.trim(), tt_str);
					} else if ("clob".equalsIgnoreCase(type_name)) {
						try {
							CLOB tm = (CLOB) rs.getObject(i);
							String tmp = null;
							if (tm != null) {
								tmp = tm.getSubString(1, (int) tm.length());
								tm = null;
							}
							hm.put(name.trim(), tmp);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else {
						Object obj = rs.getObject(i);
						if (obj instanceof java.lang.String) {
							String t_str = null;
							t_str = String.valueOf(obj).trim();
							hm.put(name.trim(), t_str);
						} else {
							hm.put(name.trim(), obj);
						}
					}
				}
				al.add(hm);
			}
		}
		return al;
	}
}
