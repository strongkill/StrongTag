package net.strong.bean;

public class ProConstants {
	public static final String IF_ANSWER = "if.answer";
	public static final String IF_STATEMENT_NUMBER = "if.number";
	public static final String IF_STATEMENT_NOTEXISTS_ELSE = "if.else.exists";
	public static final String IF_STATEMENT_NOTEXISTS_ELSEIF = "if.elseif.exists";

	public static String PHOTO_FULL_UPLOAD_DIR = "/data/album/";

	public static String PHOTO_UPLOAD_DIR = "album";
	public static String DEFAULT_HOST = "1363.cn";
	/*public static Config cfg;

	public static Config getCfg() {
		return cfg;
	}

	public static void setCfg(Config cfg) {
		ProConstants.cfg = cfg;
	}*/

	public static String getPHOTO_FULL_UPLOAD_DIR() {
		return PHOTO_FULL_UPLOAD_DIR;
	}

	public static void setPHOTO_FULL_UPLOAD_DIR(String blog_photo_update_dir) {
		PHOTO_FULL_UPLOAD_DIR = blog_photo_update_dir;
	}

	public static String getDEFAULT_HOST() {
		return DEFAULT_HOST;
	}

	public static void setDEFAULT_HOST(String default_host) {
		DEFAULT_HOST = default_host;
	}

	public static String getPHOTO_UPLOAD_DIR() {
		return PHOTO_UPLOAD_DIR;
	}

	public static void setPHOTO_UPLOAD_DIR(String photo_upload_dir) {
		PHOTO_UPLOAD_DIR = photo_upload_dir;
	}
}
