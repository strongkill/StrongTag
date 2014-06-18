package cn.qtone;
/**
 * 文章信息
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.strong.HttpServlet.BaseHttpServlet;
import net.strong.util.Helper;

public class TestServletTag extends BaseHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doPost(request,response);

	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		File f = new File("c:\\ip.txt");
		InputStream fi = new FileInputStream(f);
		InputStreamReader in = new InputStreamReader(fi,"utf-8");
		BufferedReader br = new BufferedReader(in);
		String line = null;
		Connection con = null;
		try {
			con = getDataSource().getConnection();
			PreparedStatement stmt = con.prepareStatement("insert into iptables set start_ip=?,end_ip=?,province=?,city=?");
			int count=0;
			while((line = br.readLine())!=null){
				String[] lines = line.split(" ");
				int serial = 0;
				String[] fields = new String[4];
				for(int i=0;i<lines.length;i++){
					String tmp = lines[i];
					if(tmp.trim().length()>0){
						if(serial>3){
							fields[3]+=" " + tmp;	
						}else{
							fields[serial] = tmp;
							serial ++ ;
						}
					}
				}
				stmt.setDouble(1, calc_ip(fields[0]));
				stmt.setDouble(2, calc_ip(fields[1]));
				stmt.setString(3, fields[2]);
				stmt.setString(4, fields[3]);
				stmt.addBatch();
				//stmt.addBatch("insert intp iptables set start_ip="++",end_ip="+calc_ip(fields[1])+",provice='"+fields[2]+"',city='"+fields[3]+"'");
				fields=null;

				count++;
				
				if(count>=2000){
					stmt.executeBatch();
					stmt.clearBatch();
					count=0;
				}
				
			}
			//if(stmt.)
			stmt.executeBatch();
			Helper.cleanup(stmt,con);
		} catch (Exception e) {
			Helper.cleanup(con);
			e.printStackTrace();
		}finally{
			Helper.cleanup(con);
		}
		f = null;
		/*
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		String realPath = request.getSession().getServletContext().getRealPath(
				"/");
		String url = null;
		String str = null;
		String page = request.getParameter("page");
		int curr_page =0 ;
		if(page!=null && page.length()>0)
			curr_page = Integer.parseInt(page);

		int per_page = 30;
		int prefix = curr_page*per_page;
		Template mytpl = new Template(realPath, "{", "}");// 模板解析对象
		url = "/html/test.htm";
		mytpl.setTpl("List", url);
		long posstime = System.currentTimeMillis();

		int maxpage = getJdbcTemplate().queryForInt("select count(*) from articles a,member m,article_type t where a.checked=1 and m.is_checked=1 and m.member_type=0  and a.article_type_id=t.article_type_id and a.member_id=m.member_id");
		System.out.println("finish getMaxPage : " + (System.currentTimeMillis()-posstime));
		List l =(List)getJdbcTemplate().queryForList("select a.member_id,a.article_id,a.article_subject,a.cr_date,m.truename,t.article_type_name,t.article_type_id,a.cr_date,a.view_count,a.reply_count from articles a,member m,article_type t where a.checked=1 and m.is_checked=1 and m.member_type=0  and a.article_type_id=t.article_type_id and a.member_id=m.member_id order by a.article_id desc limit "+prefix+","+per_page); 

		int i=0;
		while(l!=null && l.size()>0 && l.get(0)!=null){
			Map m =(Map)l.remove(0);

			StringBuffer bf = new StringBuffer();

			bf.append("<a href=\"/teacher_blog.jsp?type_id=").append(m.get("article_type_id")).append("\">").append(m.get("article_type_name")).append("</a>");
			mytpl.setCycParam("row", i, "type_name", bf.toString());

			bf = null;
			bf = new StringBuffer();

			bf.append("<a title=\"")
				.append(m.get("article_subject"))
				.append("-- 作者：")
				.append(m.get("truename"))
				.append(" 时间：")
				.append(m.get("cr_date").toString())
				.append("\" href=\"/blog_article_details.jsp?blog_id=")
				.append(m.get("member_id"))
				.append("&article_id=")
				.append(m.get("article_id"))
				.append("\">")
				.append(m.get("article_subject"))
				.append("</a>");

			mytpl.setCycParam("row", i, "subject", bf.toString());

			bf = null;
			bf = new StringBuffer();
			bf.append(m.get("reply_count"));
			bf.append("/");
			bf.append(m.get("view_count"));
			mytpl.setCycParam("row", i, "count", bf.toString());


			bf = null;
			bf = new StringBuffer();
			bf.append("<a title=\"")
				.append(m.get("truename"))
				.append("\" href=\"/blog/")
				.append(m.get("member_id"))
				.append("\">")
				.append(m.get("truename"))
				.append("</a>");

			mytpl.setCycParam("row", i, "truename",bf.toString());
			mytpl.setCycParam("row", i, "count", m.get("cr_date").toString());
			i++;
			m.clear();
			m = null;
		}*/
		/*
		Iterator it = l.iterator();
		int i = 0;	
		while (it.hasNext()) {
			String[] s = (String[]) it.next();
            mytpl.setCycParam("row",i,"num",s[0]);
            mytpl.setCycParam("row",i,"name",s[1]);
            mytpl.setCycParam("row",i,"sex",s[2]);
            i++;
		}
		 */	
		//-----------------------------
//		设置翻页代码	
		/*
		String strParames = "&totals=" + maxpage;
		Paginate pageMan = new Paginate(maxpage, per_page, curr_page);
		pageMan.setRoll(strParames, request);

		mytpl.setParam("page", pageMan.getRoll());	
		mytpl.parse("List");
		str = mytpl.getText("List");
		mytpl.clear();
		mytpl = null;
		PrintWriter out = response.getWriter();
		out.print((str == null) ? "" : str);
		out.flush();
		out.close();
		out = null;
		 */
	}

	private double calc_ip(String ip){
		if(ip==null)return 0;
		String[] ips = ip.split("\\.");
		double v = (double)Integer.parseInt(ips[0])*256*256*256+(double)Integer.parseInt(ips[1])*256*256+(double)Integer.parseInt(ips[2])*256+(double)Integer.parseInt(ips[3]);
		if(v<0)
			System.out.println(v);
		return v;
	}


}
