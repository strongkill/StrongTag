package net.strong.crypt;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * <p>Title: PassportCrypt.java</p>
 * <p>Description:通行证(DES加密算法)加密类,通过 私有密匙 对字串加密生成 明匙 .</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company:  cn.qt </p>
 * @author Strong Yuan
 * @sina 2007-6-22
 * @version 1.1
 */
public class PassportCrypt {
	public static char sp_code = 03;

	/**
	 *
	 * @param txt byte[] 等待加密的原字串
	 * @param key byte[] 私有密匙(用于解密和加密)
	 * @return string 原字串经过私有密匙加密后的结果
	 */
	public static final byte[] passport_encrypt(byte[] txt, byte[] key) {
		int rand = new Double(Math.random()*32000).intValue();
		byte[] encrypt_key = new MD5().getMD5ofStr(rand+"").toLowerCase().getBytes();
		byte ctr = 0;

		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

		for (int i = 0, j = 0; i< txt.length; i++,j+=2) {
			ctr = ctr == encrypt_key.length ? 0 : ctr;
			byteOut.write(encrypt_key[ctr]);
			byteOut.write(txt[i]^ encrypt_key[ctr++]);
		}
		return Base64.encodeBytes(passport_encodeKey(byteOut.toByteArray(), key)).getBytes();
	}

	/**
	 *
	 * @param txt String 等待加密的原字串
	 * @param key String 私有密匙(用于解密和加密)
	 * @return string 原字串经过私有密匙加密后的结果
	 */

	public static final String passport_encrypt(String txt,String key){
		String str = new String(passport_encrypt(txt.getBytes(),key.getBytes()));
		str = str.replaceAll("\\+", "_"); // 修正＋在地址栏时的问题
		str = str.replaceAll("\n", "");
		return str;
	}

	/**
	 *
	 * @param txt String 等待加密的原字串
	 * @param key String 私有密匙(用于解密和加密)
	 * @param encoding String 编码
	 * @return string 原字串经过私有密匙加密后的结果
	 */
	public static final String passport_encrypt(String txt,String key,String encoding){
		String str = null;
		try{
			str = new String(passport_encrypt(txt.getBytes(encoding),key.getBytes()));
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		str = str.replaceAll("\\+", "_"); // 修正＋在地址栏时的问题
		str = str.replaceAll("\n", "");
		return str;
	}

	/**
	 *
	 * @param txt byte[] 等待解密的字串
	 * @param key byte[] 私有密匙(用于解密和加密)
	 * @return string 字串经过私有密匙解密后的结果
	 */
	public static final byte[] passport_decrypt(byte[] txt, byte[] key) {

		txt = passport_encodeKey(Base64.decode(txt, 0, txt.length), key);

		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		for (int i = 0; i< txt.length; i++) {
			byte md5 = txt[i];
			byteOut.write(txt[++i]^ md5);
		}
		return byteOut.toByteArray();
	}

	/**
	 *
	 * @param txt string 等待加密的原字串
	 * @param key string 私有密匙(用于解密和加密)
	 * @return string 字串经过私有密匙解密后的结果
	 */
	public static final String passport_decrypt(String txt, String key){
		txt = txt.replaceAll("_", "\\+"); //修正当＋号出现时的问题
		return new String(passport_decrypt(txt.getBytes(),key.getBytes()));
	}

	/**
	 *
	 * @param txt byte[] 等待加密的原字串
	 * @param key byte[] 私有密匙(用于解密和加密)
	 * @return string 字串经过私有密匙解密后的结果
	 */
	public static final byte[] passport_encodeKey(byte[] txt, byte[] encrypt_key) {

		encrypt_key = new MD5().getMD5ofStr(new String(encrypt_key)).toLowerCase().getBytes();
		byte ctr = 0;
		byte[] tmp = new byte[txt.length];

		for (int i = 0; i< txt.length; i++) {
			ctr = ctr == encrypt_key.length ? 0 : ctr;
			tmp[i]= (byte) (txt[i]^ encrypt_key[ctr++]);
		}
		return tmp;
	}


	/**
	 * @deprecated
	 * @param strAry
	 * @return
	 */
	public static final String passport_encode(String[] strAry){
		return "";

	}

	public static void main(String[] args) {
		
		//String DES_key ="!-@#$~%*^~?:"; //DES密匙
		//char a = 03;
		//long current = System.currentTimeMillis();
		//for(int i=0;i<100000;i++){
		String ab = passport_encrypt("3245 8345 2345 13124", "AYlouSNiVJE=");
		System.out.println(ab);
//		System.out.println(ab);
		ab =passport_decrypt("hTt6BHIkdAjQruTMRWTWbcKafjLwKZzB", "AYlouSNiVJE=");
		System.out.println(ab);
		//}
		//System.out.println(System.currentTimeMillis()-current);
	}
}
