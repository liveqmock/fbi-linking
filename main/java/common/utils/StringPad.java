package common.utils;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 12-4-13
 * Time: 下午3:19
 * To change this template use File | Settings | File Templates.
 */
public class StringPad {

    public static String leftPad4ChineseToByteLength(String srcStr, int totalByteLength, String padStr)  {
        if (srcStr == null) {
            return null;
        }
        int srcByteLength = srcStr.getBytes().length;

        if (padStr == null || "".equals(padStr)) {
            padStr = " ";
        } else if (padStr.getBytes().length > 1 || totalByteLength <= 0) {
            throw new RuntimeException("参数错误");
        }
        StringBuilder rtnStrBuilder = new StringBuilder();
        if (totalByteLength >= srcByteLength) {
            for (int i = 0; i < totalByteLength - srcByteLength; i++) {
                rtnStrBuilder.append(padStr);
            }
            rtnStrBuilder.append(srcStr);
        } else {
            byte[] rtnBytes = new byte[totalByteLength];
            try {
                System.arraycopy(srcStr.getBytes("GBK"), 0, rtnBytes, 0, totalByteLength);
                rtnStrBuilder.append(new String(rtnBytes, "GBK"));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return rtnStrBuilder.toString();
    }


    public static String rightPad4ChineseToByteLength(String srcStr, int totalByteLength, String padStr)  {
        if (srcStr == null) {
            return null;
        }
        int srcByteLength = srcStr.getBytes().length;

        if (padStr == null || "".equals(padStr)) {
            padStr = " ";
        } else if (padStr.getBytes().length > 1 || totalByteLength <= 0) {
            throw new RuntimeException("参数错误");
        }
        StringBuilder rtnStrBuilder = new StringBuilder();
        if (totalByteLength >= srcByteLength) {
            rtnStrBuilder.append(srcStr);
            for (int i = 0; i < totalByteLength - srcByteLength; i++) {
                rtnStrBuilder.append(padStr);
            }
        } else {
            byte[] rtnBytes = new byte[totalByteLength];
            try {
                System.arraycopy(srcStr.getBytes("GBK"), 0, rtnBytes, 0, totalByteLength);
                rtnStrBuilder.append(new String(rtnBytes, "GBK"));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return rtnStrBuilder.toString();
    }
}
