package net.strong.SendMail;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 
 *
 * <p>Title: SendMail.java</p>
 * <p>Description:SendMail</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company:  cn.qt </p>
 * @author Strong Yuan
 * @sina 2007-7-25
 * @version 1.0
 */
public class SendMail {

	protected ArrayList<String> attach;

	protected String smtpHost;

	protected String to;

	protected String from;

	protected String subject;

	protected String body;

	protected String username;

	protected String password;

	private String fromName = null; // 发信者别名

	protected static String body_charset = "text/html;charset=gb2312";// 邮件体的字符类型

	protected static Log log = LogFactory.getLog("SendMail");

	private String replyto;

	/**
	 * 设置内容的字符集
	 * 
	 * @param body_charset
	 *            字符集
	 */
	public void setBody_charset(String body_charset) {
		SendMail.body_charset = body_charset;
	}

	public String getBody_charset() {
		return SendMail.body_charset;
	}

	/**
	 * 添加附件
	 * 
	 * @param newAttach
	 *            附件的路径，附件须先上传到服务器，此路径时服务器中的路径
	 */
	public void addAttach(String newAttach) {
		attach.add(newAttach);
	}

	public void removeAttach() {
		attach.remove(attach.size());
	}

	public void removeAttach(int i) {
		attach.remove(i);
	}

	/**
	 * 设置SMTP服务器地址
	 * 
	 * @param newStmpHost
	 *            服务器地址
	 */
	public void setSmtpHost(String newStmpHost) {
		smtpHost = newStmpHost;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	/**
	 * 设置接收者EMAIL
	 * 
	 * @param newTo
	 *            接收者EMAIL
	 */
	public void setTo(String newTo) {
		to = newTo;
	}

	public String getTo() {
		return to;
	}

	/**
	 * 设置发送者EMAIL
	 * 
	 * @param newTo
	 *            发送者EMAIL
	 */
	public void setFrom(String newTo) {
		from = newTo;
	}

	public String getFrom() {
		return from;
	}

	public void setSubject(String newTo) {
		subject = newTo;
	}

	public String getSubject() {
		return subject;
	}

	/**
	 * 设置邮件体
	 * 
	 * @param newTo
	 *            邮件体
	 */
	public void setBody(String newTo) {
		body = newTo;
	}

	public String getBody() {
		return body;
	}

	/**
	 * 设置smtp用户名
	 * 
	 * @param newTo
	 *            smtp用户名
	 */
	public void setUsername(String newTo) {
		username = newTo;
	}

	public String getUsername() {
		return username;
	}

	/**
	 * 设置smtp密码
	 * 
	 * @param newTo
	 *            smtp密码
	 */
	public void setPassword(String newTo) {
		password = newTo;
	}

	public String getPassword() {
		return password;
	}

	public SendMail() {
		attach = new ArrayList<String>();
	}

	/**
	 * 本方法是非静态方法，所以使用时须先new一个对象，并设置smtp属性，发送及接收者EMAIL，主题，内容 附件（如有）等参数。
	 */

	public synchronized void send() {
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", smtpHost);
			props.put("mail.smtp.auth", "true");
			Session session = Session.getInstance(props,
					new Email_Autherticatorbean(username, password));
			MimeMessage msg = new MimeMessage(session);
			if (fromName != null && fromName.trim().length() > 0) {
				msg.setFrom(new InternetAddress(from, fromName));
			} else {
				msg.setFrom(new InternetAddress(from));
			}
			msg.setRecipients(javax.mail.Message.RecipientType.TO,
					InternetAddress.parse(to, false));
			msg.setSubject(subject);
			msg.setHeader("X-Mailer", "Mail");
			msg.setSentDate(new Date());
			if (replyto != null)
				msg.setReplyTo(InternetAddress.parse(replyto));

			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setContent(body, body_charset);
			MimeMultipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			if (!attach.isEmpty()) {
				MimeBodyPart mbpAttach[] = new MimeBodyPart[attach.size()];
				for (int i = 0; i < attach.size(); i++) {
					FileDataSource fds = new FileDataSource(attach.get(i)
							.toString());
					mbpAttach[i] = new MimeBodyPart();
					mbpAttach[i].setDataHandler(new DataHandler(fds));
					mbpAttach[i].setFileName(new String(fds.getName().getBytes(
					"GBK"), "iso8859-1"));
					mp.addBodyPart(mbpAttach[i]);
				}

			}
			msg.setContent(mp);
			Transport.send(msg);
			// System.out.println("Message sent OK.");
		} catch (javax.mail.SendFailedException e1) {
			log.error("to:" + to + ",smtp:" + smtpHost + "," + e1.getMessage());
		} catch (javax.mail.internet.AddressException ee) {
			log.error("to:" + to + ",smtp:" + smtpHost + "," + ee.getMessage());
		} catch (Exception e) {
			log.error("to:" + to + ",smtp:" + smtpHost + "," + e.getMessage());
		}
	}

	/**
	 * 静态发送邮件的方法,以gb2312字符集来发送
	 * 
	 * @param smtpServer
	 *            smtp服务器名
	 * @param to
	 *            对方的EMAIL地址
	 * @param from
	 *            发送者的EAMIL地址
	 * @param subject
	 *            主题
	 * @param body
	 *            内容
	 * @param username
	 *            smtp用户名
	 * @param password
	 *            smtp密码
	 * @param fromName
	 *            发送者别名
	 */
	public synchronized static boolean simpleSend(String smtpServer, String to,
			String from, String subject, String body, String username,
			String password, String fromName) {
		return simpleSend(smtpServer, to, from, subject, body, username,
				password, fromName, "text/html;charset=gb2312");
	}

	/**
	 * 静态发送邮件的方法
	 * 
	 * @param smtpServer
	 *            smtp服务器名
	 * @param to
	 *            对方的EMAIL地址
	 * @param from
	 *            发送者的EAMIL地址
	 * @param subject
	 *            主题
	 * @param body
	 *            内容
	 * @param username
	 *            smtp用户名
	 * @param password
	 *            smtp密码
	 * @param fromName
	 *            发送者别名
	 * @param body_charset
	 *            所用字符集
	 */
	
	public synchronized static boolean simpleSend(String smtpServer, String to,
			String from, String subject, String body, String username,
			String password, String fromName, String body_charset){
		return simpleSend(smtpServer,to,from,subject,body,username,password,fromName,body_charset,"true");
	}
	/**
	 * 静态发送邮件的方法
	 * 
	 * @param smtpServer smtp服务器名
	 * @param to 对方的EMAIL地址
	 * @param from 发送者的EAMIL地址
	 * @param subject 主题
	 * @param body 内容
	 * @param username smtp用户名
	 * @param password smtp密码
	 * @param fromName 发送者别名
	 * @param body_charset 所用字符集
	 * @param auth_type 是否需要smtp的Auth验证
	 *          
	 */
	public synchronized static boolean simpleSend(String smtpServer, String to,
			String from, String subject, String body, String username,
			String password, String fromName, String body_charset,String auth_type) {
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", smtpServer);
			props.put("mail.smtp.auth", auth_type);
			props.put("username",username);//这里是加入用户名   
			props.put("password",password);//这里是加入密码
			Session session = Session.getInstance(props,null/*
					(new Email_Autherticatorbean(username, password))*/);
			MimeMessage msg = new MimeMessage(session);
			if (fromName != null && fromName.trim().length() > 0) {
				msg.setFrom(new InternetAddress(from, fromName));
			} else {
				msg.setFrom(new InternetAddress(from));
			}
			msg.setRecipients(javax.mail.Message.RecipientType.TO,
					InternetAddress.parse(to, false));
			msg.setSubject(subject);
//			msg.setHeader("X-Mailer", "Mail");
			msg.setSentDate(new Date());
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setContent(body, body_charset);
			MimeMultipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			msg.setContent(mp);
			msg.saveChanges();
			Transport trans = session.getTransport("smtp");
			trans.connect(smtpServer, username, password);
			trans.sendMessage(msg, msg.getAllRecipients());
			trans.close();
			trans = null;
//			Transport.sen
			//Transport.send(msg);

			// System.out.println("Message sent OK.");
		} catch (javax.mail.SendFailedException e1) {
			log.error("to:" + to + ",smtp:" + smtpServer + ","
					+ e1.getMessage());
			return false;
		} catch (javax.mail.internet.AddressException ee) {
			log.error("to:" + to + ",smtp:" + smtpServer + ","
					+ ee.getMessage());
			return false;
		} catch (Exception e) {
			log.error("to:" + to + ",smtp:" + smtpServer);
			log.error(e.getMessage());
			return false;
			// e.printStackTrace();
		}
		return true;
	}

	/**
	 * 静态发送邮件的方法
	 * 
	 * @param smtpServer
	 *            smtp服务器名
	 * @param to
	 *            对方的EMAIL地址
	 * @param from
	 *            发送者的EAMIL地址
	 * @param subject
	 *            主题
	 * @param body
	 *            内容
	 * @param username
	 *            smtp用户名
	 * @param password
	 *            smtp密码
	 */
	public synchronized static boolean simpleSend(String smtpServer, String to,
			String from, String subject, String body, String username,
			String password) {
		return simpleSend(smtpServer,to,from,subject,body,username,password,from,"text/html;charset=gb2312");
	}

	public static void main(String args[]) {
		simpleSend("smtp.163.com", "strongkill@126.com", "strong.@163.com",
				"hello", "hello", "strong.", "", "Strong",
		"text/html;charset=utf-8");
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getReplyto() {
		return replyto;
	}

	public void setReplyto(String replyto) {
		this.replyto = replyto;
	}

}
