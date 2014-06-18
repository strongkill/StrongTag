package net.strong.crypt;

/**
 * <p>Title: Strong Software Co,Java Development Lib</p>
 * <p>Description: Development Lib For Java</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @Date 2006-11-3
 * @author Strong Yuan
 * @version 1.0
 */

import java.security.*;

import javax.crypto.*;

public class Crypt {
	private static String Algorithm="DES";

//	定义 加密算法,可用 DES,DESede,Blowfish 
	static boolean debug = false;
	static{ Security.addProvider(new com.sun.crypto.provider.SunJCE());
	} //生成密钥, 注意此步骤时间比较长 
	public static byte[] getKey() throws Exception{ 
		KeyGenerator keygen = KeyGenerator.getInstance(Algorithm);
		SecretKey deskey = keygen.generateKey();
		if (debug) 
			System.out.println("生成密钥:"+byte2hex(deskey.getEncoded()));
		return deskey.getEncoded();
	} //加密 
	public static byte[] encode(byte[] input,byte[] key) throws Exception{
		SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key,Algorithm);
		if (debug){ 
			System.out.println("加密前的二进串:"+byte2hex(input));
			System.out.println("加密前的字符串:"+new String(input));
		}
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.ENCRYPT_MODE,deskey);
		byte[] cipherByte=c1.doFinal(input);
		if (debug) 
			System.out.println("加密后的二进串:"+byte2hex(cipherByte));
		return cipherByte;
	} //解密 
	public static byte[] decode(byte[] input,byte[] key) throws Exception{
		SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key,Algorithm);
		if (debug) 
			System.out.println("解密前的信息:"+byte2hex(input));
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.DECRYPT_MODE,deskey);
		byte[] clearByte=c1.doFinal(input);
		if (debug){ 
			System.out.println("解密后的二进串:"+byte2hex(clearByte));
			System.out.println("解密后的字符串:"+(new String(clearByte)));
		}
		return clearByte;
	} //md5()信息摘要, 不可逆 
	public static byte[] md5(byte[] input) throws Exception{ 
		java.security.MessageDigest alg=java.security.MessageDigest.getInstance("MD5");
		//or "SHA-1"
		if (debug){ 
			System.out.println("摘要前的二进串:"+byte2hex(input));
			System.out.println("摘要前的字符串:"+new String(input));
		}
		alg.update(input);
		byte[] digest = alg.digest();
		if (debug) 
			System.out.println("摘要后的二进串:"+byte2hex(digest));
		return digest;
	} //字节码转换成16进制字符串 
	public static String byte2hex(byte[] b) { 
		String hs="";
		String stmp="";
		for (int n=0; n<b.length;n++){ 
			stmp=(java.lang.Integer.toHexString(b[n] & 0XFF));
			hs = ( stmp.length() == 1 ) ? hs + "0" + stmp : hs + stmp;
			if (n<b.length-1) hs=hs+":";
		}
		return hs.toUpperCase();
	} 
	public static void main(String[] args) throws Exception{ // byte[] key = getKey();
		debug = true;
		byte[] key = "好好学习".getBytes();
		byte[] str = encode("测试加密".getBytes(),key);
		decode(str,key);
		// byte[] str="2E:60:77:87:8A:97:83:6C:1C:5D:46:16:B6:15:AB:EA";
		// decode("2E:60:77:87:8A:97:83:6C:1C:5D:46:16:B6:15:AB:EA",key);
		// encode("测试加密".getBytes(),key);
		// md5("测试加密".getBytes());
	} 
} 
