package com.elex.bigdata.uidencode;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * User: Z J Wu Date: 14-2-18 Time: 下午5:25 Package: com.elex.bigdata.uidencode
 */
public class AESProvider {
  private final int MAX = 128;
  private static AESProvider instance;
  private final int LENGTH = 128;
  private final String DEFAULT_CHARSET = "utf-8";
  private KeyGenerator KEY_GEN;
  private final String PASSWORD = "tdI4pLhxpJKPh3syt";
  private SecretKey secretKey;
  private SecretKeySpec key;

  private AESProvider() {
    try {
      KEY_GEN = KeyGenerator.getInstance("AES");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    KEY_GEN.init(LENGTH, new SecureRandom(PASSWORD.getBytes()));
    secretKey = KEY_GEN.generateKey();
    key = new SecretKeySpec(secretKey.getEncoded(), "AES");
  }

  public synchronized static AESProvider getInstance() throws Exception {
    if (instance == null) {
      instance = new AESProvider();
    }
    return instance;
  }

  public ExpandedBytes encode(String content) throws UIDEncodingDecodingException {
    String s = StringUtils.trimToNull(content);
    if (StringUtils.isBlank(content)) {
      throw new UIDEncodingDecodingException("Uid content is empty");
    }
    byte[] bytes1;
    try {
      bytes1 = s.getBytes(DEFAULT_CHARSET);
    } catch (UnsupportedEncodingException e) {
      throw new UIDEncodingDecodingException(e);
    }
    if (bytes1.length >= MAX) {
      throw new UIDEncodingDecodingException("Original uid is too big to express.");
    }
    byte[] bytes2;
    try {
      bytes2 = encrypt(bytes1);
    } catch (Exception e) {
      throw new UIDEncodingDecodingException(e);
    }
    return ExpandedBytes.build(bytes2, MAX);
  }

  public byte[] encrypt(byte[] content) throws Exception {
    try {
      byte[] enCodeFormat = key.getEncoded();
      SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.ENCRYPT_MODE, key);
      return cipher.doFinal(content);
    } catch (Exception e) {
      throw e;
    }
  }

  public byte[] decrypt(byte[] content) throws Exception {
    try {
      byte[] enCodeFormat = key.getEncoded();
      SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.DECRYPT_MODE, key);
      return cipher.doFinal(content);
    } catch (Exception e) {
      throw e;
    }
  }

  public String decode(ExpandedBytes ep) throws UIDEncodingDecodingException {

    byte[] bytes;
    try {
      bytes = decrypt(ep.shrink(MAX));
    } catch (Exception e) {
      throw new UIDEncodingDecodingException(e);
    }
    try {
      return new String(bytes, DEFAULT_CHARSET);
    } catch (UnsupportedEncodingException e) {
      throw new UIDEncodingDecodingException(e);
    }
  }

  public static void main(String[] args) throws Exception {
    String uid = RandomStringUtils.randomAlphanumeric(100);
    ExpandedBytes expandedBytes = AESProvider.getInstance().encode(uid);
    System.out.println(expandedBytes);
    String originalUID = AESProvider.getInstance().decode(expandedBytes);

    System.out.println("---------------------------------");
    System.out.println(uid);
    System.out.println(originalUID);
  }

}
