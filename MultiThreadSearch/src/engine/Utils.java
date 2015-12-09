package engine;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.codec.digest.DigestUtils;

public class Utils
{

	public static String getMd5(File file){
		try {
			FileInputStream fis = new FileInputStream(file);
			String md5 = DigestUtils.md5Hex(fis);
			fis.close();
			
			return md5;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getSha256(File file){
		try {
			FileInputStream fis = new FileInputStream(file);
			String md5 = DigestUtils.sha256Hex(fis);
			fis.close();
			
			return md5;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static String getSha512(File file){
		try {
			FileInputStream fis = new FileInputStream(file);
			String sha512 = DigestUtils.sha512Hex(fis);
			fis.close();
			
			return sha512;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getSha1(File file){
		try {
			FileInputStream fis = new FileInputStream(file);
			String sha1 = DigestUtils.sha1Hex(fis);
			fis.close();
			
			return sha1;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
