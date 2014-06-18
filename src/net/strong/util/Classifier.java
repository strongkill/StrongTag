/**
 *
 */
package net.strong.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.strong.hibernate.HibernateSessionFactory;
/**
 * 系统敏感词过敏,通过后台设定的敏感词库.以正则表达式的方式对输入的内容进行检测.
 * <p>Title: Classifier.java</p>
 * <p>Description:Classifier</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company:  cn.qt </p>
 * @author Strong Yuan
 * @sina 2007-6-21
 * @version 1.0
 */
public class Classifier {

	private static ArrayList<String> keywords = new ArrayList<String>();
	private static long timecross = System.currentTimeMillis();
	/**
	 * 加载数据库中最新的敏感词库
	 */
	private static synchronized void LoadKeyWords() {
		if(keywords.size()>0 && System.currentTimeMillis()-timecross <=60*30*1000)
			return;
		keywords.clear();
		Connection con = null;
		try{
			con = HibernateSessionFactory.getCurrentSession().connection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select word from classifier order by id desc");
			while(rs.next())
				keywords.add(rs.getString("word"));
			timecross = System.currentTimeMillis();
			Helper.cleanup(stmt, rs);
			//Helper.cleanup(con);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			HibernateSessionFactory.closeCurrentSession();
			//Helper.cleanup(con);
		}
	}
	/**
	 * 检查字符串中是否含有敏感词。
	 * @param txt
	 * @return 是否包含敏感性詞語
	 */
	public static boolean checkClassifier(String txt){
		if(txt==null || txt.length()==0)
			return false;
		if(keywords.size()==0 || System.currentTimeMillis()-timecross >60*30*1000)
			LoadKeyWords();
		boolean result = false;
		ArrayList<String> temp_l = (ArrayList<String>) keywords.clone();
		Matcher matcher = null;
		Pattern pattern = null;
		for(int i =0;i<temp_l.size();i++){
			pattern = Pattern.compile(temp_l.get(i));
			try{
				matcher = pattern.matcher(txt);
				if(matcher.find()){
					result = true;
					break;
				}
			}catch(Exception e){
				e.printStackTrace();
				result = true;
			}
		}
		temp_l.clear();
		temp_l = null;
		return result;
	}


	public static void main(String[] args) {
		String txt = "我们是专业的投票专家";
		//String txt = "3dfacggggccad5c";
		Matcher matcher = null;
		try{
		//Pattern pattern = Pattern.compile("法[.]轮[.]功");
			Pattern pattern = Pattern.compile("投.{0,10}票.{0,10}专.{0,10}家");//"[d-g].{8}c"
		matcher = pattern.matcher(txt);
		}catch(Exception e){
			e.printStackTrace();
		}
//		MatchResult rs = matcher.toMatchResult();
		System.out.println(matcher.find());
	}

}
