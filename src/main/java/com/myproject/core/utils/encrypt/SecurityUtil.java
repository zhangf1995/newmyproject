package com.myproject.core.utils.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SecurityUtil {

    /**
     * 消息摘要
     *
     */
    public static class MessageDigestUtil {

        public static byte[] digest(String content, boolean isMd5) throws Exception {
            MessageDigest messageDigest = null;
            String algorithm = isMd5 ? "MD5" : "SHA";
            messageDigest = MessageDigest.getInstance(algorithm);
            return messageDigest.digest(content.getBytes());
        }

        public static byte[] digest1(String content, boolean isMd5) throws Exception {
            MessageDigest messageDigest = null;
            String algorithm = isMd5 ? "MD5" : "SHA";
            messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(content.getBytes());
            return messageDigest.digest();
        }
    }

    /**
     * 对称加密算法
     *
     */
    public static class AesUtil {
        private static final String ALGORITHM = "AES";
        private static final String DEFAULT_CHARSET = "UTF-8";

        /**
         * 生成秘钥
         * @return
         * @throws NoSuchAlgorithmException
         */
        public static String generaterKey() throws NoSuchAlgorithmException {  
            KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
            keygen.init(128, new SecureRandom()); // 16 字节 == 128 bit
            //            keygen.init(128, new SecureRandom(seedStr.getBytes())); // 随机因子一样，生成出来的秘钥会一样
            SecretKey secretKey = keygen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        }

        /**
         */
        public static SecretKeySpec getSecretKeySpec(String secretKeyStr){
            byte[] secretKey = Base64.getDecoder().decode(secretKeyStr);
            System.out.println(secretKey.length);
            return new SecretKeySpec(secretKey, ALGORITHM);
        }

        /**
         * 加密
         */
        public static String encrypt(String content,String secretKey) throws Exception{
            Key key = getSecretKeySpec(secretKey);
            Cipher cipher = Cipher.getInstance(ALGORITHM);// 创建密码器  
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content.getBytes(DEFAULT_CHARSET));
            return Base64.getEncoder().encodeToString(result);
        }

        /**
         * 解密
         */
        public static String decrypt(String content, String secretKey) throws Exception{  
            Key key = getSecretKeySpec(secretKey);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(content));  
            return new String(result);
        }  
    }

    /**
     * 非对称加密算法
     *
     */
    public static class RsaUtil {

        public static class RsaKeyPair {
            private String publicKey ="";
            private String privateKey ="";

            public RsaKeyPair(String publicKey, String privateKey) {
                super();
                this.publicKey = publicKey;
                this.privateKey = privateKey;
            }

            public String getPublicKey() {
                return publicKey;
            }
            public String getPrivateKey() {
                return privateKey;
            }
        }

        private static final String ALGORITHM = "RSA";
        private static final String ALGORITHMS_SHA1WithRSA = "SHA1WithRSA";
        private static final String ALGORITHMS_SHA256WithRSA = "SHA256WithRSA";
        private static final String DEFAULT_CHARSET = "UTF-8";
        private static String getAlgorithms(boolean isRsa2) {
            return isRsa2 ? ALGORITHMS_SHA256WithRSA : ALGORITHMS_SHA1WithRSA;
        }

        /**
         * 生成秘钥对
         * @return
         * @throws NoSuchAlgorithmException
         */
        public static RsaKeyPair generaterKeyPair() throws NoSuchAlgorithmException{  
            KeyPairGenerator keygen = KeyPairGenerator.getInstance(ALGORITHM);  
            SecureRandom random = new SecureRandom();  
            //            SecureRandom random = new SecureRandom(seedStr.getBytes()); // 随机因子一样，生成出来的秘钥会一样
            // 512位已被破解，用1024位,最好用2048位 
            keygen.initialize(2048, random);
            // 生成密钥对  
            KeyPair keyPair = keygen.generateKeyPair();  
            RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();  
            RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();   
            String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());  
            return new RsaKeyPair(publicKeyStr,privateKeyStr);
        }

        /**
         * 获取公钥
         * @param publicKey
         * @return
         * @throws Exception
         */
        public static RSAPublicKey getPublicKey(String publicKey) throws Exception{  
            byte[] keyBytes = Base64.getDecoder().decode(publicKey);  
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);  
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);  
            return (RSAPublicKey) keyFactory.generatePublic(spec);  
        }

        /**
         * 获取私钥
         * @param privateKey
         * @return
         * @throws NoSuchAlgorithmException 
         * @throws InvalidKeySpecException 
         * @throws Exception
         */
        public static RSAPrivateKey getPrivateKey(String privateKey) throws Exception{  
            byte[] keyBytes = Base64.getDecoder().decode(privateKey);  
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);  
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);  
            return (RSAPrivateKey) keyFactory.generatePrivate(spec);  
        }

        /**
         * 要私钥签名
         * @throws InvalidKeySpecException 
         * @throws Exception 
         */
        public static String sign(String content, String privateKey, boolean isRsa2) throws Exception {
            PrivateKey priKey = getPrivateKey(privateKey);
            Signature signature = Signature.getInstance(getAlgorithms(isRsa2));
            signature.initSign(priKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));
            byte[] signed = signature.sign();
            return Base64.getEncoder().encodeToString(signed);
        }

        /**
         * 要公钥签名
         */
        public static boolean verify(String content,String sign,String publicKey,boolean isRsa2) throws Exception {
            PublicKey pubKey = getPublicKey(publicKey);
            Signature signature = Signature.getInstance(getAlgorithms(isRsa2));
            signature.initVerify(pubKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));
            return signature.verify(Base64.getDecoder().decode(sign));
        }

        /**
         * 加密
         * @param input
         * @param pubOrPrikey
         * @return
         */
        public static String encrypt(String content, Key pubOrPrikey) throws Exception{
            Cipher cipher = null;
            cipher = Cipher.getInstance(ALGORITHM); 
            cipher.init(Cipher.ENCRYPT_MODE, pubOrPrikey);
            byte[] result = cipher.doFinal(content.getBytes(DEFAULT_CHARSET));
            return Base64.getEncoder().encodeToString(result);
        }

        /**
         * 解密
         * @param input
         * @param pubOrPrikey
         * @return
         */
        public static String decrypt(String content, Key pubOrPrikey) throws Exception {
            Cipher cipher = null;
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, pubOrPrikey);  
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(content));
            return new String(result);
        }
    }

}