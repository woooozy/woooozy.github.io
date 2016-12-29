package net.sourceforge.simcpux.utils;

import android.content.Context;
import android.content.SharedPreferences;

import net.sourceforge.simcpux.consts.ConstUtil;
import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * *****************************************************************************
 * 作者： woozy
 * 开发日期： 2016/10/11.
 * 模块功能：
 * *****************************************************************************
 */
public class CommonUtils {
    private static final String TAG = "CommonMethods";
    /* DISPLAY_WIDTH = 100 */
    final static float DISPLAY_WIDTH = 100;
    /* DISPLAY_HEIGHT = 160 */
    final static float DISPLAY_HEIGHT = 160;

    /**
     * byte字节数组转换Short类型（未严格测试）
     *
     * @param outBuf
     * @return
     */
    public static short bytesToShort(byte[] outBuf) {

        if (outBuf.length < 2) {
            return (short) (outBuf[0] < 0 ? outBuf[0] + 256 : outBuf[0]);
        } else {
            return (short) (((outBuf[0] < 0 ? outBuf[0] + 256 : outBuf[0]) << 8) + (outBuf[1] < 0 ? outBuf[1] + 256
                    : outBuf[1]));
        }

    }

    /**
     * 填充XX数据，如果结果数据块是8的倍数，不再进行追加,如果不是,追加0xXX到数据块的右边，直到数据块的长度是8的倍数。
     *
     * @param data
     *            待填充XX的数据
     * @return
     */
    public static String padding(String data, String inData) {
        int padlen = 8 - (data.length() / 2) % 8;
        if (padlen != 8) {
            String padstr = "";
            for (int i = 0; i < padlen; i++)
                padstr += inData;
            data += padstr;
            return data;
        } else {
            return data;
        }
    }

    /**
     * 填充80数据，首先在数据块的右边追加一个
     * '80',如果结果数据块是8的倍数，不再进行追加,如果不是,追加0x00到数据块的右边，直到数据块的长度是8的倍数。
     *
     * @param data
     *            待填充80的数据
     * @return
     */
    public static String padding80(String data) {
        int padlen = 8 - (data.length() / 2) % 8;
        String padstr = "";
        for (int i = 0; i < padlen - 1; i++)
            padstr += "00";
        data = data + "80" + padstr;
        return data;
    }

    /**
     * 生成16位的动态链接库鉴权十六进制随机数字符串
     *
     * @return String
     */
    public static String yieldHexRand() {
        StringBuffer strBufHexRand = new StringBuffer();
        Random rand = new Random(System.currentTimeMillis());
        int index;
        // 随机数字符
        char charArrayHexNum[] = { '1', '2', '3', '4', '5', '6', '7', '8', '9',
                '0', 'A', 'B', 'C', 'D', 'E', 'F' };
        for (int i = 0; i < 16; i++) {
            index = Math.abs(rand.nextInt()) % 16;
            if (i == 0) {
                while (charArrayHexNum[index] == '0') {
                    index = Math.abs(rand.nextInt()) % 16;
                }
            }
            strBufHexRand.append(charArrayHexNum[index]);
        }
        return strBufHexRand.toString();
    }

    /**
     * 分析类名
     *
     * @param strName
     *            String
     * @return String
     */
    public static String analyseClassName(String strName) {
        String strTemp = strName.substring(strName.lastIndexOf(".") + 1,
                strName.length());
        return strTemp.substring(strTemp.indexOf(" ") + 1, strTemp.length());
    }

    static public String convertInt2String(int n, int len) {
        String str = String.valueOf(n);
        int strLen = str.length();

        String zeros = "";
        for (int loop = len - strLen; loop > 0; loop--) {
            zeros += "0";
        }

        if (n >= 0) {
            return zeros + str;
        } else {
            return "-" + zeros + str.substring(1);
        }
    }

    static public double convertString2Double(String str, double defaultValue) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * yyyyMMddhhmmss author:吴正尧
     *
     * @return
     */
    public static String getDateTimeString() {
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat("yyyyMMddHHmmss").format(cal.getTime());

    }

    /**
     * yyyyMMdd author:吴正尧
     *
     * @return
     */
    public static String getDate() {
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());

    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return sb.toString();
    }

    /**
     * usage: str2bytes("0710BE8716FB"); it will return a byte array, just like
     * : b[0]=0x07;b[1]=0x10;...b[5]=0xfb;
     */
    public static byte[] str2bytes(String src) {
        if (src == null || src.length() == 0 || src.length() % 2 != 0) {
            return null;
        }
        int nSrcLen = src.length();
        byte byteArrayResult[] = new byte[nSrcLen / 2];
        StringBuffer strBufTemp = new StringBuffer(src);
        String strTemp;
        int i = 0;
        while (i < strBufTemp.length() - 1) {
            strTemp = src.substring(i, i + 2);
            byteArrayResult[i / 2] = (byte) Integer.parseInt(strTemp, 16);
            i += 2;
        }
        return byteArrayResult;
    }

    public static int strcpy(byte d[], byte s[], int from, int maxlen) {
        int i;
        for (i = 0; i < maxlen; i++) {
            d[i + from] = s[i];
        }

        d[i + from] = 0;
        return i;
    }

    public static int memcpy(byte d[], byte s[], int from, int maxlen) {
        int i;
        for (i = 0; i < maxlen; i++) {
            d[i + from] = s[i];
        }
        return i;
    }

    public static void BytesCopy(byte[] dest, byte[] source, int offset1,
                                 int offset2, int len) {
        for (int i = 0; i < len; i++) {
            dest[offset1 + i] = source[offset2 + i];
        }
    }

    /**
     * usage: input: n = 1000000000 ( n = 0x3B9ACA00) output: byte[0]:3b
     * byte[1]:9a byte[2]:ca byte[3]:00 notice: the scope of input integer is [
     * -2^32, 2^32-1] ; **In CMPP2.0,the typeof msg id is ULONG,so,need
     * ulong2Bytes***
     */
    public static byte[] int2Bytes(int n) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(n);
        return bb.array();
    }

    public static byte[] long2Bytes(long l) {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(l);
        return bb.array();
    }

    /**
     * 将整数转为16进行数后并以指定长度返回（当实际长度大于指定长度时只返回从末位开始指定长度的值）
     *
     * @param val
     *            int 待转换整数
     * @param len
     *            int 指定长度
     * @return String
     */
    public static String Int2HexStr(int val, int len) {
        String result = Integer.toHexString(val).toUpperCase();
        int r_len = result.length();
        if (r_len > len) {
            return result.substring(r_len - len, r_len);
        }
        if (r_len == len) {
            return result;
        }
        StringBuffer strBuff = new StringBuffer(result);
        for (int i = 0; i < len - r_len; i++) {
            strBuff.insert(0, '0');
        }
        return strBuff.toString();
    }

    public static String Long2HexStr(long val, int len) {
        String result = Long.toHexString(val).toUpperCase();
        int r_len = result.length();
        if (r_len > len) {
            return result.substring(r_len - len, r_len);
        }
        if (r_len == len) {
            return result;
        }
        StringBuffer strBuff = new StringBuffer(result);
        for (int i = 0; i < len - r_len; i++) {
            strBuff.insert(0, '0');
        }
        return strBuff.toString();
    }

    public static String getResString(Context context, int stringId) {
        return context.getResources().getString(stringId);
    }

    /**
     * 字符串转换为字节数组
     * <p>
     * stringToBytes("0710BE8716FB"); return: b[0]=0x07;b[1]=0x10;...b[5]=0xfb;
     */
    public static byte[] stringToBytes(String string) {
        if (string == null || string.length() == 0 || string.length() % 2 != 0) {
            return null;
        }
        int stringLen = string.length();
        byte byteArrayResult[] = new byte[stringLen / 2];
        StringBuffer sb = new StringBuffer(string);
        String strTemp;
        int i = 0;
        while (i < sb.length() - 1) {
            strTemp = string.substring(i, i + 2);
            byteArrayResult[i / 2] = (byte) Integer.parseInt(strTemp, 16);
            i += 2;
        }
        return byteArrayResult;
    }

    /**
     * 字节数组转为16进制
     *
     * @param bytes
     *            字节数组
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuffer buff = new StringBuffer();
        int len = bytes.length;
        for (int j = 0; j < len; j++) {
            if ((bytes[j] & 0xff) < 16) {
                buff.append('0');
            }
            buff.append(Integer.toHexString(bytes[j] & 0xff));
        }
        return buff.toString();
    }

    /**
     * ********************************************************************<br>
     * 方法功能：将用户圈存金额先转换为分，在把分转为16进制，再前补0组装为4字节圈存金额 如1元 为:"00000064" 参数说明：<br>
     * 作 者：杨明<br>
     * 开发日期：2013-9-18 上午11:53:56<br>
     * 修改日期：<br>
     * 修改人：<br>
     * 修改说明：<br>
     * ********************************************************************<br>
     */
    /**
     * 将长整数转为16进行数后并以指定长度返回（当实际长度大于指定长度时只返回从末位开始指定长度的值）
     *
     * @param val
     *            int 待转换长整数
     * @param len
     *            int 指定长度
     * @return String
     */
    public static String longToHex(long val, int len) {
        String result = Long.toHexString(val).toUpperCase();
        int rLen = result.length();
        if (rLen > len) {
            return result.substring(rLen - len, rLen);
        }
        if (rLen == len) {
            return result;
        }
        StringBuffer strBuff = new StringBuffer(result);
        for (int i = 0; i < len - rLen; i++) {
            strBuff.insert(0, '0');
        }
        return strBuff.toString();
    }

    public static String moneyHexToDouble(String hexMoney) {
        int balance = -1;
        double balanceDouble = 0.0;
        if (hexMoney != null) {
            balance = Integer.parseInt(hexMoney, 16);
            balanceDouble = (double) balance / 100;
        }
        return balanceDouble + "";
    }

    /**
     * author：赵文星 生成8个字节随机数
     *
     * @return
     */
    public static String getRand16() {
        StringBuffer strBufHexRand = new StringBuffer();
        Random rand = new Random(System.currentTimeMillis());
        int index;
        // 随机数字符
        char charArrayHexNum[] = { '1', '2', '3', '4', '5', '6', '7', '8', '9',
                '0' };
        for (int i = 0; i < 16; i++) {
            index = Math.abs(rand.nextInt()) % 10;
            if (i == 0) {
                while (charArrayHexNum[index] == '0') {
                    index = Math.abs(rand.nextInt()) % 10;
                }
            }
            strBufHexRand.append(charArrayHexNum[index]);
        }
        return strBufHexRand.toString();
    }

    /**
     * 字符转换从UTF-8到GBK
     *
     * @param gbkStr
     * @return
     */
    public static byte[] getUTF8toGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }
    public static void setPreferenceValue(Context context, String key,
                                          String value, int type) {
        SharedPreferences prefs = context.getSharedPreferences(
                ConstUtil.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        switch (type) {
            case ConstUtil.TYPE_INT:
                editor.putInt(key, Integer.parseInt(value));
                break;
            case ConstUtil.TYPE_FLOAT:
                editor.putFloat(key, Float.parseFloat(value));
                break;
            case ConstUtil.TYPE_STRING:
                editor.putString(key, value);
                break;
            case ConstUtil.TYPE_BOOLEAN:
                editor.putBoolean(key, Boolean.parseBoolean(value));
                break;
            case ConstUtil.TYPE_LONG:
                editor.putLong(key, Long.parseLong(value));
                break;
            case ConstUtil.TYPE_FIRSTSTART_BOOLEAN:
                editor.putBoolean(key, Boolean.parseBoolean(value));
                break;
        }
        editor.commit();
    }

    public static String getPreferenceValue(Context context, String key,
                                            int type) {
        String result = "";
        SharedPreferences prefs = context.getSharedPreferences(
                ConstUtil.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        switch (type) {
            case ConstUtil.TYPE_INT:
                result = prefs.getInt(key, 0) + "";
                break;
            case ConstUtil.TYPE_FLOAT:
                result = prefs.getFloat(key, 0) + "";
                break;
            case ConstUtil.TYPE_STRING:
                result = prefs.getString(key, "");
                break;
            case ConstUtil.TYPE_BOOLEAN:
                result = prefs.getBoolean(key, false) + "";
                break;
            case ConstUtil.TYPE_LONG:
                result = prefs.getLong(key, 0) + "";
                break;
            case ConstUtil.TYPE_FIRSTSTART_BOOLEAN:
                result = prefs.getBoolean(key, true) + "";
                break;

        }
        return result;
    }

    public static boolean isContains(Context context, String keys){
        boolean isContains=false;
        SharedPreferences prefs = context.getSharedPreferences(ConstUtil.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (prefs.contains(keys)) {
            isContains=true;
        }else {
            isContains=false;
        }
        return isContains;
    }

    public static String toStringHex1(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "ASCII");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    /**
     * 将16进制余额值转化为10进制以元为单位
     *
     * @param responseStr
     * @return
     */
    public static String convertResToBalance(String responseStr) {
        double balance = 0;
        String d = "0.00";
        try {
            // 去掉后面的9000
            String balanceStr = "";
            if (responseStr.endsWith("9000")) {
                balanceStr = responseStr.substring(0, 8);
            } else {
                balanceStr = responseStr;
            }
            Logger.e("16进制余额值为：  " + balanceStr);
            balance = Integer.parseInt(balanceStr, 16);
            Logger.e("10进制未处理的余额值为：  " + balance);
            // 解析出来是以分为单位，所以要除以100
            balance = (double) balance / 100;
            DecimalFormat df = new DecimalFormat("#####0.00"); // 保留小数点后两位
            d = df.format(balance);// double类型保留不到0.00位数，只能用string
            Logger.e( "10进制处理后的余额值为：  " + balance);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Logger.e( "转换余额异常 ");
        }
        return d;
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime
     *            设置的需要判断的时间 //格式如2012-09-08
     *
     *
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */

    // String pTime = "2012-03-12";
    public static String getWeek(String pTime) {

        String Week = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "六";
        }
        return Week;
    }


    //传入double型数值，四舍五入后保留两位小数
    public static String formatDouble(double data){
        BigDecimal bg = new BigDecimal(data);
        double formatData = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        //经过处理后double型一定带一位小数
        String formatDataStr = formatData+"";
        int formatDataStrLen = formatDataStr.length();
        int pointIndex = formatDataStr.indexOf('.');
        //如果小数点后面只有一位
        if(formatDataStrLen - pointIndex == 2){
            formatDataStr = formatDataStr + "0";
        }
        Logger.e("after format money is： " + formatDataStr);
        return formatDataStr;
    }

}
