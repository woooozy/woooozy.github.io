
package net.sourceforge.simcpux.manager;

import net.sourceforge.simcpux.utils.CommonUtils;
import com.orhanobut.logger.Logger;

public abstract class AbsCardManager {
    private static final String TAG = "AbsCardManager";

    /** 存放卡片返回的数据域,分配了512字节的空间,数据域是多少字节就存多少字节 */
    protected byte[] resBuf = new byte[512];
    /** 存放卡片返回的数据域的长度(由于某种特殊原因用数组) */
    protected short[] resLenBuf = new short[1];
    /** byte[]类型的apdu命令 */
    protected byte[] apduBytes;
    /** apdu命令长度,以字节为单位 */
    protected short apduLength;
    /** 执行apdu后返回的数据域(不是整个报文) */
    protected String responseStr = "";

    /**
     * 用来关闭closeiIsodep的方
     */
    public abstract void closeIsoDep();

    /**
     * 将apdu指令由String类型转成byte[],并求得长度.分别作为jni方法的参数.
     * 
     * @param apdu
     */
    public void apduStrToBytes(String apdu) {
        apduBytes = CommonUtils.stringToBytes(apdu);
        apduLength = (short) apduBytes.length;
    }

    /**
     * 取出resBuf里面的数据域并转化成String
     */
    public void apduResponseBytesToStr() {
        byte[] data = new byte[resLenBuf[0]];
        System.arraycopy(resBuf, 0, data, 0, resLenBuf[0]);
        responseStr = CommonUtils.bytesToHex(data);
    }

    /**
     * ********************************************************************<br>
     * 方法功能：将查询余额指令获取到的返回值转换为十进制的余额信息<br>
     * 参数：查询余额指令获取到的返回值<br>
     * 参数说明：<br>
     * 作 者：杨明<br>
     * 开发日期：2013-6-24 下午2:48:01<br>
     * 修改日期：2015-10-21<br>
     * 修改人：吴正尧<br>
     * 修改说明：改为保留小数点后两位<br>
     * ********************************************************************<br>
     */
    public String convertResToBalance(String responseStr) {
        double balance = 0;
        try {
            // 去掉后面的9000
            String balanceStr = responseStr.substring(0, responseStr.length() - 4);
            Logger.e("16进制余额值为：  " + balanceStr);
            balance = Integer.parseInt(balanceStr, 16);
            Logger.e("10进制未处理的余额值为：  " + balance);
            balance =  balance / 100;

            Logger.e("10进制处理后的余额值为：  " + balance);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Logger.e("转换余额异常 ");
        }
        return balance+"";
    }

    public String getApduRes() {
        return responseStr;
    }

    public byte[] getResBytes() {
        return resBuf;
    }

    public short[] getResLen() {
        return resLenBuf;
    }

}
