package com.elex.bigdata.uidencode;

/**
 * User: Z J Wu Date: 14-2-18 Time: 下午6:12 Package: com.elex.bigdata.uidencode
 */
public class UIDEncodingDecodingUtils {
  public static String binaryString2hexString(String bString) {
    if (bString == null || bString.equals("") || bString.length() % 8 != 0)
      return null;
    StringBuffer tmp = new StringBuffer();
    int iTmp = 0;
    for (int i = 0; i < bString.length(); i += 4) {
      iTmp = 0;
      for (int j = 0; j < 4; j++) {
        iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
      }
      tmp.append(Integer.toHexString(iTmp));
    }
    return tmp.toString();
  }

  public static String toStringBinary(final byte[] b, int off, int len) {
    StringBuilder result = new StringBuilder();
    for (int i = off; i < off + len; ++i) {
      int ch = b[i] & 0xFF;
      if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || " `~!@#$%^&*()-_=+[]{}|;:'\",.<>/?"
        .indexOf(ch) >= 0) {
        result.append((char) ch);
      } else {
        result.append(String.format("\\x%02X", ch));
      }
    }
    return result.toString();
  }

  public static String toStringBinary(final byte[] b) {
    return b == null ? null : toStringBinary(b, 0, b.length);
  }

}
