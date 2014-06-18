package net.strong.bean;

import java.util.ArrayList;

public class Config {
	private int DataType = 0;
	private String proxool_alias_name = Constants.PORXOOL_ALIAS_PERFIX+Constants.DEFAULT_ALIAS_NAME;
	
	private String smtpServer = "localhost";
	private String smtpusername = "mail";
	private String smtppassword = "mail";
	private String default_email = "strong@localhost";
	private String DES_KEY ="123456";
	private String maxsecurefilesize = "3101024";
	private String real_path ;
	private String watermark_path =null;
	private ArrayList<String> proxool_pool =new ArrayList<String>();
	private String memcached_pools = null;
	private int memcachedTimeOutSecond = 30;
	private boolean isCached = false;
	
	private ArrayList<String> allowedExtensions;
	
	private ArrayList<String> deniedExtensions;
	
	private ArrayList<String> allowExtensionsVideo;
	
	public ArrayList<String> getAllowExtensionsVideo() {
		return allowExtensionsVideo;
	}
	public void setAllowExtensionsVideo(ArrayList<String> allowExtensionsVideo) {
		this.allowExtensionsVideo = allowExtensionsVideo;
	}
	public ArrayList<String> getDeniedExtensions() {
		return deniedExtensions;
	}
	public void setDeniedExtensions(ArrayList<String> deniedExtensions) {
		this.deniedExtensions = deniedExtensions;
	}

	public ArrayList<String> getAllowedExtensions() {
		return allowedExtensions;
	}
	public void setAllowedExtensions(ArrayList<String> allowedExtensions) {
		this.allowedExtensions = allowedExtensions;
	}
	public String getDES_KEY() {
		return DES_KEY;
	}
	public void setDES_KEY(String des_key) {
		DES_KEY = des_key;
	}
	public int getDataType() {
		return DataType;
	}
	public void setDataType(int dataType) {
		DataType = dataType;
	}
	public String getProxool_alias_name() {
		//System.out.println("from pageContext : "+proxool_alias_name);
		return proxool_alias_name;
	}
	public void setProxool_alias_name(String proxool_alias_name) {
		this.proxool_alias_name = proxool_alias_name;
	}
	public String getDefault_email() {
		return default_email;
	}
	public void setDefault_email(String default_email) {
		this.default_email = default_email;
	}
	public String getSmtpServer() {
		return smtpServer;
	}
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}
	public String getMaxsecurefilesize() {
		return maxsecurefilesize;
	}
	public void setMaxsecurefilesize(String maxsecurefilesize) {
		this.maxsecurefilesize = maxsecurefilesize;
	}
	public String getSmtppassword() {
		return smtppassword;
	}
	public void setSmtppassword(String smtppassword) {
		this.smtppassword = smtppassword;
	}
	public String getSmtpusername() {
		return smtpusername;
	}
	public void setSmtpusername(String smtpusername) {
		this.smtpusername = smtpusername;
	}
	public String getReal_path() {
		return real_path;
	}
	public void setReal_path(String real_path) {
		this.real_path = real_path;
	}
	public String getWatermark_path() {
		return watermark_path;
	}
	public void setWatermark_path(String watermark_path) {
		this.watermark_path = watermark_path;
	}
	public ArrayList<String> getProxool_pool() {
		return proxool_pool;
	}
	public void setProxool_pool(ArrayList<String> proxool_pool) {
		this.proxool_pool = proxool_pool;
	}
	public String getMemcached_pools() {
		return memcached_pools;
	}
	public void setMemcached_pools(String memcached_pools) {
		this.memcached_pools = memcached_pools;
	}
	public int getMemcachedTimeOutSecond() {
		return memcachedTimeOutSecond;
	}
	public void setMemcachedTimeOutSecond(int memcachedTimeOutSecond) {
		this.memcachedTimeOutSecond = memcachedTimeOutSecond;
	}
	public boolean isCached() {
		return isCached;
	}
	public void setisCached(boolean isCached) {
		this.isCached = isCached;
	}
	
}
