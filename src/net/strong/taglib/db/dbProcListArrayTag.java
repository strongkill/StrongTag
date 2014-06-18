package net.strong.taglib.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.jsp.JspException;

import net.strong.bean.Constants;
import net.strong.util.Helper;
import oracle.jdbc.OracleTypes;

//import net.strong.database.PoolBean;

/**
 * <p>Title:存储过程调用 </p>
 * <p>Description: 根据指定的参数调用存储过程，并将所得的数据集存
 * 于pageContext对象中</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author unascribed
 * @version 1.0
 */

public class dbProcListArrayTag extends dbTag {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String paramCount = "0"; //参数个数，最多五个
	protected String iscached = "true";
	protected String scopeType = "4";

	protected int scope_type = 4;

	private String procName = null;

	private String param1 = null;

	private String param2 = null;

	private String param3 = null;

	private String param4 = null;

	private String param5 = null;

	private String param6 = null;

	private String param7 = null;

	private String param8 = null;

	private String param[] = new String[8];

	public void release() {
		paramCount = "0";
		procName = null;
		scopeType = "4";
		scope_type = 4;

		param1 = null;
		param2 = null;
		param3 = null;
		param4 = null;
		param5 = null;
		param6 = null;
		param7 = null;
		param8 = null;
	}

	public void setScopeType(String scopeType) {
		this.scopeType = scopeType;
		if (scopeType != null) {
			scope_type = Integer.valueOf(scopeType).intValue();
			if (scope_type > 4)
				scope_type = 4;
			if (scope_type < 1)
				scope_type = 1;
		}
	}

	public String getScopeType() {
		return this.scopeType;
	}

	public void setParamCount(String paramCount) {
		this.paramCount = paramCount;
	}

	public String getParamCount() {
		return paramCount;
	}

	public void setProcName(String procName) {
		this.procName = procName;
	}

	public String getProcName() {
		return procName;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
		param[0] = param1;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
		param[1] = param2;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
		param[2] = param3;
	}

	public String getParam3() {
		return param3;
	}

	public void setParam4(String param4) {
		this.param4 = param4;
		param[3] = param4;
	}

	public String getParam4() {
		return param4;
	}

	public String getParam6() {
		return param6;
	}
	public void setParam5(String param5) {
		this.param5 = param5;
		param[4] = param5;
	}

	public String getParam5() {
		return param5;
	}
	public void setParam6(String param6) {
		this.param6 = param6;
		param[5] = param6;
	}

	public String getParam7() {
		return param7;
	}

	public void setParam7(String param7) {
		this.param7 = param7;
		param[6] = param7;
	}

	public String getParam8() {
		return param8;
	}

	public void setParam8(String param8) {
		this.param8 = param8;
		param[7] = param8;
	}

	public int doStartTag() throws JspException {
		dbType = Constants.getDataType(pageContext);
		return (SKIP_BODY);
	}

	public int doEndTag() throws JspException {
		if (Integer.valueOf(paramCount).intValue() > 8)
			throw new JspException("parameter too much,5 is most");

		StringBuffer sql = new StringBuffer();
		StringBuffer cached_key =new StringBuffer();

		Connection con = null;


		CallableStatement stmt = null;

		ResultSet rs = null;
		ArrayList<HashMap<String, Object>> rows = new ArrayList<HashMap<String, Object>>();

		try {
			if(dbType==1 || dbType==0){//sql 2000 mysql
				sql.append("{call ").append( procName);
				cached_key.append("{call ").append( procName);
				int pCount = 0;
				if ((pCount = Integer.valueOf(paramCount).intValue()) > 0) {
					sql.append("(");
					cached_key.append("(");
					for (int i = 1; i <= pCount; i++) {
						if(i==pCount){
							sql.append("?)");
							cached_key.append(param[i - 1]).append(")");
						}else{
							sql.append("?,");
							cached_key.append(param[i - 1]).append(",");
						}
					}
				}
				sql.append("}");
				cached_key.append("}");

				if("true".equalsIgnoreCase(iscached)){
					try{
						Object tmp = GetObject(cached_key.toString());
						if(tmp!=null){

							rows = (ArrayList<HashMap<String, Object>>)tmp;
						}
					}finally{

					}

				}
				if(rows.size()==0){

					con = getConnection();
					stmt = con.prepareCall(sql.toString());
					for (int j = 1; j <= pCount; j++) {
						stmt.setString(j, param[j - 1]);
					}
					rs = stmt.executeQuery();
					ResultSetMetaData rsmd = rs.getMetaData();
					int colCount = rsmd.getColumnCount();
					int index = 0;
					while (rs.next()) {
						HashMap<String, Object> row = new HashMap<String, Object>();
						row.put("INDEX", String.valueOf(index));
						row.put("index_1",String.valueOf(index+1));

						for (int i = 1; i <= colCount; i++) {
							String name = rsmd.getColumnLabel(i).toLowerCase();
							if(name==null)
								name = rsmd.getColumnName(i).toLowerCase();

							row.put(name, rs.getObject(i));
						}
						index++;
						rows.add(row);
					}
					if("true".equalsIgnoreCase(iscached)){
						try{
							SetObject(cached_key.toString(), rows);
						}finally{

						}

					}
				}
			}else if(dbType==2){

			}else if(dbType==3){ //Oracle
				sql.append("{call ").append( procName );
				cached_key.append("{call ").append(procName);
				int pCount = 0;
				if ((pCount = Integer.valueOf(paramCount).intValue()) > 0) {
					sql.append("(");
					cached_key.append("(");
					for (int i = 1; i <= pCount; i++) {
						sql.append("?,");
						cached_key.append(param[i-1]).append(",");
					}
					sql.append("?)");
					cached_key.append("?)");
				}else{
					sql.append("(?)");
					cached_key.append("(?)");
				}
				sql.append("}");
				cached_key.append("}");
				System.out.println(cached_key.toString());
				if("true".equalsIgnoreCase(iscached)){
					try{
						Object tmp = GetObject(cached_key.toString());
						if(tmp!=null){

							rows = (ArrayList<HashMap<String, Object>>)tmp;
						}
					}finally{

					}

				}
				if(rows.size()==0){
					con = getConnection();

					stmt = con.prepareCall(sql.toString());
					for (int j = 1; j <= pCount; j++) {
						stmt.setString(j, param[j - 1]);
					}
					stmt.registerOutParameter(pCount+1, OracleTypes.CURSOR);
					stmt.execute();
					rs = (ResultSet)stmt.getObject(pCount+1);

					ResultSetMetaData rsmd = rs.getMetaData();
					int colCount = rsmd.getColumnCount();
					int index = 0;
					while (rs.next()) {
						HashMap<String, Object> row = new HashMap<String, Object>();
						row.put("INDEX", String.valueOf(index));
						row.put("index_1",String.valueOf(index+1));

						for (int i = 1; i <= colCount; i++) {
							String name = rsmd.getColumnLabel(i).toLowerCase();
							if(name==null)
								name = rsmd.getColumnName(i).toLowerCase();

							row.put(name, rs.getObject(i));
						}
						index++;
						rows.add(row);
					}
					if("true".equalsIgnoreCase(iscached)){
						try{
							SetObject(cached_key.toString(), rows);
						}finally{

						}

					}
				}
			}
			Helper.cleanup(stmt, rs);
			Helper.cleanup(con);
		} catch (SQLException e) {
			Helper.cleanup(con);
			throw new JspException("database perform error : " + e.getMessage()+" SQL:" + sql);
		} finally {
			Helper.cleanup(con);
		}
		if(bussinessClass!=null && bussinessClass.length()>0){
			long[] pageinfo = {0,0,0};
			 ArrayList<HashMap<String, Object>> tmprows = BussinessProcess(bussinessClass, rows, pageinfo);
			 if(tmprows!=null && tmprows.size()>0)
				 rows = tmprows;
		}		
		
		pageContext.getRequest().setAttribute("rows", rows);

		return (EVAL_PAGE);
	}

	public static void main(String[] args){
		StringBuffer sql = new StringBuffer();
		StringBuffer cached_key =new StringBuffer();
		String procName=" afasdfasdfasdf ";
		String paramCount = "5";
		String param[] = {"1","2","3","4","5","6","7","8"};
		sql.append("{call ").append( procName);
		cached_key.append("{call ").append( procName);
		int pCount = 0;
		if ((pCount = Integer.valueOf(paramCount).intValue()) > 0) {
			sql.append("(");
			cached_key.append("(");
			for (int i = 1; i <= pCount; i++) {
				if(i==pCount){
					sql.append("?)");
					cached_key.append(param[i - 1]).append(")");
				}else{
					sql.append("?,");
					cached_key.append(param[i - 1]).append(",");
				}
			}
		}
		sql.append("}");
		cached_key.append("}");
		System.out.println(sql.toString());
		System.out.println(cached_key.toString());
	}
}