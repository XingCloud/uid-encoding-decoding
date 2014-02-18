package com.elex.bigdata.uidencode;

/**
 * User: Z J Wu Date: 14-2-18 Time: 下午5:25 Package: com.elex.bigdata.uidencode
 */
public class ExpandedBytes {
  private int expandedLength;
  private byte[] bytes;

  public ExpandedBytes(int expandedLength, byte[] bytes) {
    this.expandedLength = expandedLength;
    this.bytes = bytes;
  }

  public static ExpandedBytes build(byte[] bytes, int bigSize) {
    int expandedLength = bigSize - bytes.length;
    byte[] bigBytes = new byte[bigSize];
    int size = bytes.length;
    System.arraycopy(bytes, 0, bigBytes, bigSize - size, size);
    return new ExpandedBytes(expandedLength, bigBytes);
  }

  public byte[] shrink(int bigSize) {
    int shrinkedLength = bigSize - expandedLength;
    byte[] newBytes = new byte[shrinkedLength];
    System.arraycopy(bytes, bigSize - shrinkedLength, newBytes, 0, shrinkedLength);
    return newBytes;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append('(');
    sb.append(UIDEncodingDecodingUtils.toStringBinary(bytes));
    sb.append(", ");
    sb.append(expandedLength);
    sb.append(')');
    return sb.toString();
  }
}
