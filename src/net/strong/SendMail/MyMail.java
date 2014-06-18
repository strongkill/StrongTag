package net.strong.SendMail;

import java.sql.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MyMail {
	private String smtpServer;
	private String username;
	private String password;
	private boolean ifAuth=false;
	private String from;
	private String displayName;
	private String to ;
	private String subject;
	private StringBuffer content;
	
	public static void main(String[] args){
		SendMail.simpleSend("1363.cn", "strongkill@126.com",
				"strong@1363.cn",
				"教师博客 -- 重新设置密码","密码", "strong","",
				"教师博客",
		"text/html; charset=UTF-8","false");
	}
	
	public void send(){   
        //HashMap<String, String> map=new HashMap<String, String>();   
        //map.put("state", "success");   
        //String message="邮件发送成功！";   
        Session session=null;    
        Properties props = System.getProperties();    
        props.put("mail.smtp.host", smtpServer);   
        if(ifAuth){ //服务器需要身份认证    
            props.put("mail.smtp.auth","true");       
            Email_Autherticatorbean smtpAuth=new Email_Autherticatorbean(username,password);   
            session=Session.getDefaultInstance(props, smtpAuth);     
        }else{   
            props.put("mail.smtp.auth","false");   
            session=Session.getDefaultInstance(props, null);   
        }   
        session.setDebug(true);    
        Transport trans = null;      
        try {    
            MimeMessage msg = new MimeMessage(session);   
            try{   
                Address from_address = new InternetAddress(from, displayName);   
                msg.setFrom(from_address);   
            }catch(java.io.UnsupportedEncodingException e){   
                e.printStackTrace();    
            }    
            InternetAddress[] address={new InternetAddress(to)};   
            msg.setRecipients(Message.RecipientType.TO,address);   
            msg.setSubject(subject);    
            Multipart mp = new MimeMultipart();   
            MimeBodyPart mbp = new MimeBodyPart();   
            mbp.setContent(content.toString(), "text/html;charset=gb2312");   
            mp.addBodyPart(mbp);      
            /*if(!file.isEmpty()){//有附件    
                Enumeration efile=file.elements();   
                while(efile.hasMoreElements()){     
                    mbp=new MimeBodyPart();    
                    filename=efile.nextElement().toString(); //选择出每一个附件名   
                    FileDataSource fds=new FileDataSource(filename); //得到数据源    
                    mbp.setDataHandler(new DataHandler(fds)); //得到附件本身并至入BodyPart   
                    mbp.setFileName(fds.getName());  //得到文件名同样至入BodyPart   
                    mp.addBodyPart(mbp);    
                }      
                file.removeAllElements();      
            }     */
            msg.setContent(mp); //Multipart加入到信件   
            msg.setSentDate(new Date(0));    //设置信件头的发送日期   
            //发送信件   
            msg.saveChanges();     
            trans = session.getTransport("smtp");   
            trans.connect(smtpServer, username, password);   
            trans.sendMessage(msg, msg.getAllRecipients());   
            trans.close();   
               
        }catch(AuthenticationFailedException e){       
//             map.put("state", "failed");   
             //message="邮件发送失败！错误原因：\n"+"身份验证错误!";   
             e.printStackTrace();     
        }catch (MessagingException e) {    
             //message="邮件发送失败！错误原因：\n"+e.getMessage();    
//             map.put("state", "failed");   
             e.printStackTrace();   
             Exception ex = null;   
             if ((ex = e.getNextException()) != null) {   
                 System.out.println(ex.toString());   
                 ex.printStackTrace();    
             }     
        }    
        //System.out.println("\n提示信息:"+message);    
        //map.put("message", message);   
        //return map;   
    }
}
