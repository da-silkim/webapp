package com.bluenetworks.webapp.common;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;
import org.apache.commons.codec.binary.Base64;
import java.security.spec.AlgorithmParameterSpec;

public class AES256Cipher {

	 private static volatile AES256Cipher INSTANCE;

	 public static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

    public static String AES_Encode(String str, String key)	throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException,
	                        NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,	IllegalBlockSizeException, BadPaddingException {
	        byte[] textBytes = str.getBytes("UTF-8");
	        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
	        SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
	        Cipher cipher = null;
	        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
	        return Base64.encodeBase64String(cipher.doFinal(textBytes));
    }

	    
    public static String AES_Decode(String str, String key, byte[] ivBytes)	throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException,
	                        NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
	        byte[] textBytes =  Base64.decodeBase64(str.getBytes());
	        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
	        SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
	        return new String(cipher.doFinal(textBytes), "UTF-8");
	    }

	    public static String AES_Decode(String str, String key)	throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException,
	                        NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,	IllegalBlockSizeException, BadPaddingException {
	    return AES_Decode(str, key, ivBytes);
    }
	    
}
