//package br.com.Core;
//
//import java.math.BigInteger;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
//public class BeanUtils {
//
//	public  String criptografa(String senha) {
//		MessageDigest md = null;
//		try {
//			md = MessageDigest.getInstance("MD5");
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//		BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
//		senha = hash.toString(16);
//		return senha;
//	}
//        
////    public String criptografa(String senha) {
////        try {
////            MessageDigest digest = MessageDigest.getInstance("MD5");
////            digest.update(senha.getBytes());
////            BASE64Encoder encoder = new BASE64Encoder();
////            return encoder.encode(digest.digest());
////        } catch (NoSuchAlgorithmException ns) {
////            ns.printStackTrace();
////        }
////        return senha;
////    }
//
//}
