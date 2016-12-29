package net.sourceforge.simcpux.manager;

import android.nfc.tech.IsoDep;
import android.text.TextUtils;
import android.util.Log;

import net.sourceforge.simcpux.consts.AppConst;
import net.sourceforge.simcpux.entity.QCHistory;
import net.sourceforge.simcpux.utils.CommonUtils;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;

/**
 * *****************************************************************************
 * 作者： woozy
 * 开发日期： 2016/10/14.
 * 模块功能：
 * *****************************************************************************
 */
public class NfcCardOperateManager extends AbsCardManager {

    private static final String TAG = "NfcCardOperateManager";

    private IsoDep isodep;
    /**
     * 交易前余额
     */
    private String moneyBeforeTrade = "";

    /**
     * 卡计数器
     */
    private String cardCounter = "";

    /**
     * 伪随机数
     */
    private String randomNumber = "";

    /**
     * 消费初始化交易序号
     */
    private String trandNo = "";

    /**
     * MAC1
     */
    private String mac1 = "";

    public NfcCardOperateManager(IsoDep isodep) {
        this.isodep = isodep;
    }

    /**
     * 向卡片发送APDU指令
     *
     * @param apdu
     * @return
     * @throws IOException
     */
    // public byte[] sendApdu(byte[] apduByte) throws IOException {
    // byte[] resByte = isodep.transceive(apduByte);
    // return resByte;
    // }

    /**
     * 向卡片发送APDU指令
     *
     * @param apdu
     * @return
     * @throws IOException
     */
    public String sendApdu(String apdu) {
        try {
            if (!isodep.isConnected()) {
                isodep.connect();
            }
            byte[] resByte = isodep.transceive(CommonUtils.str2bytes(apdu));
            if (resByte != null) {
                return CommonUtils.bytesToHex(resByte);
            }
        } catch (Exception e) {
            // TODO: handle exception
            // mHandler.sendEmptyMessage(12);
            // EventBus.getDefault().post(new MessageEvent(12, ""));
        }

        return "";
    }

    /**
     * 获取卡片的余额
     *
     * @param cardAppAid
     * @return
     * @throws IOException
     */
    public String getCardBalance(String cardAppAid) throws IOException {
        String money = "0.00";
        String apdu = "";
        responseStr = sendApdu(cardAppAid);
        if (responseStr.endsWith("9000")) {

            apdu = "805C000204";
            responseStr = sendApdu(apdu);
            Log.e(TAG, "getBalance response: " + responseStr);
            if (responseStr.endsWith("9000")) {
                money = convertResToBalance(responseStr);
            }
        }
        // 2.断开卡片连接
//        closeiIsodep();
        return money;
    }

    public String getOOO5File(String cardAppid) throws IOException {
        String apdu = null;
        String result = "";
        responseStr = sendApdu(cardAppid);
        if (responseStr != null && responseStr.endsWith("9000")) {
            apdu = "00b0850000";
            responseStr = sendApdu(apdu);
            result = responseStr.substring(0, responseStr.length() - 4);
        }
        return result;
    }

    public String getOO15File(String cardAppid) throws IOException {
        String apdu = null;
        String result = "";
        responseStr = sendApdu(cardAppid);
        if (responseStr != null && responseStr.endsWith("9000")) {
            apdu = "00b0950000";
            responseStr = sendApdu(apdu);
            result = responseStr.substring(0, responseStr.length() - 4);
        }
        return result;
    }
    public String getOO1AFile(String cardAppid) throws IOException {
        String apdu = null;
        String result = "";
        responseStr = sendApdu(cardAppid);
        if (responseStr != null && responseStr.endsWith("9000")) {
            apdu = "00B20DD41E";
            responseStr = sendApdu(apdu);
            result = responseStr.substring(0, responseStr.length() - 4);
        }
        return result;
    }

    public String get8050File(String apduId) throws IOException {
        String result = "";
        // 圈存初始化 8050 00 02 0b + 01密钥标识符 + 金额 + 终端机编号
        Logger.e("8050发送指令----->" + apduId);
        responseStr = sendApdu(apduId);

        if (responseStr.endsWith("9000")) {
            Logger.e("8050收到返回----->" + responseStr);
            result = responseStr.substring(0, responseStr.length() - 4);
        }
        return result;
    }

    public String boardResult8052(String apdu) throws IOException {
        responseStr = sendApdu(apdu);
        Logger.e(apdu + "8052返回值-------->" + responseStr);
        if (responseStr.endsWith("9000")) {
            Logger.e("8052圈存成功");
            String tac = responseStr.substring(0, 12);
            Logger.e("-------8052> tac : " + tac);
            closeIsoDep();
            return tac;
        }
        return responseStr;
    }

    /**
     * 获取卡类型0005 author:吴正尧
     *
     * @param cardAppAid
     * @return
     * @throws IOException
     */
    public String[] getCardType(String cardAppAid) throws IOException {
        String apdu = null;
        String results[] = {};
        String cardNo = "";// 面卡卡号(印在卡面上的卡号)
        String cardType = "";
        String cardCityCd = "";
        String appVersionCd = "";
        String industryCd = "";
        String cardMasterCd = "";
        responseStr = sendApdu(cardAppAid);
        if (responseStr != null && responseStr.endsWith("9000")) {
            apdu = "00b0850000";
            responseStr = sendApdu(apdu);
            Logger.e(responseStr);
            if (responseStr.endsWith("9000")) {
                cardNo = responseStr.substring(40, 56);
                cardType = responseStr.substring(32, 36);
                cardCityCd = responseStr.substring(4, 8);
                appVersionCd = responseStr.substring(56, 60);
                industryCd = responseStr.substring(8, 12);
                cardMasterCd = responseStr.substring(0, 4);
                results = new String[]{
                        cardType, cardCityCd, appVersionCd,
                        industryCd, cardMasterCd, cardNo
                };
                Logger.e("------>卡号 : " + cardNo);
                Logger.e("卡类型" + cardType);
                Logger.e("城市代码" + cardCityCd);
                Logger.e("应用版本号" + appVersionCd);
                Logger.e("行业代码" + industryCd);
                Logger.e("发卡方代码" + cardMasterCd);
            }
        }
//        closeiIsodep();
        return results;

    }

    /**
     * 获取卡片的卡号印在卡面上的卡号0015
     *
     * @param cardAppAid
     * @return
     * @throws IOException
     */
    public String[] getCardNumberAndValidate(String cardAppAid)
            throws IOException {

        String cardvali = "";
        String cardOrderId = "";
        String apdu = null;
        String[] result = {};
        responseStr = sendApdu(cardAppAid);
        if (responseStr != null && responseStr.endsWith("9000")) {
            apdu = "00b095001E";
            responseStr = sendApdu(apdu);
            Logger.e(responseStr);
            if (responseStr.endsWith("9000")) {
                cardOrderId = responseStr.substring(21, 40);

                if (TextUtils.equals(AppConst.APP_ADF1, cardAppAid)) {
                    //=================本地应用CPU卡=====================================
                    cardvali = responseStr.substring(16, 24);
                } else if (TextUtils.equals(AppConst.APP_MF_JTB, cardAppAid)) {
                    //=================交通部CPU卡=====================================
                    cardvali = responseStr.substring(48, 56);
                }
                Logger.e("------>有效期 : " + cardvali);
                Logger.e("卡片应用序列号(分散因子)" + cardOrderId);

            }
        }
        result = new String[]{cardvali, cardOrderId
        };
//        closeiIsodep();
        return result;
    }

    /**
     * 普通消费卡消费记录0018 author:吴正尧
     *
     * @param cardAppAid
     * @return
     * @throws IOException
     */
    public ArrayList<QCHistory> getQCHistory(String cardAppAid)
            throws IOException {
        ArrayList<QCHistory> QClist = new ArrayList<QCHistory>();
        responseStr = sendApdu(cardAppAid);
        if (responseStr.endsWith("9000")) {
            for (int i = 0; i < 10; i++) {// 读取最近10条交易记录
                String index = CommonUtils.Int2HexStr(i + 1, 2);
                responseStr = sendApdu("00b2" + index + "C417");
                if (responseStr.endsWith("9000")) {
                    QCHistory history = new QCHistory();
                    String tradeNum = responseStr.substring(0, 4);
                    String balance = responseStr.substring(10, 18);
                    balance = CommonUtils.convertResToBalance(balance);
                    String tradeType = responseStr.substring(19, 20);
                    String tradeDate = responseStr.substring(32, 40);
                    String tradeTime = responseStr.substring(40, 46);
                    String terminalNum = responseStr.substring(20, 32);
                    Logger.e("------>交易类型: " + tradeType);
                    if (!TextUtils.equals(tradeDate, "00000000")) {
                        history.setBalance(balance);
                        history.setTradeNum(tradeNum);
                        history.setTradeType(tradeType);
                        history.setTradeDate(tradeDate);
                        history.setTradeTime(tradeTime);
                        history.setTerminalNum(terminalNum);
                        QClist.add(history);
                    }

                }
            }

        } else {
            return null;
        }
        return QClist;

    }

    /**
     * 获取持卡人信息0016 author:吴正尧
     *
     * @param cardAppAid
     * @throws IOException
     */
    public String[] getCardUserInfo(String cardAppAid) throws IOException {
        String apdu = null;
        String cardHolder = "";
        String holderCredentialsType = "";
        String holderCredentialsCd = "";

        String[] result = {};
        responseStr = sendApdu(cardAppAid);
        if (responseStr != null && responseStr.endsWith("9000")) {
            apdu = "00b0960050";
            responseStr = sendApdu(apdu);
            Logger.e(responseStr);
            if (responseStr.endsWith("9000")) {
                cardHolder = responseStr.substring(4, 44);
                holderCredentialsType = responseStr.substring(108, 110);
                holderCredentialsCd = responseStr.substring(44, 108);
            }
        }
        result = new String[]{
                cardHolder, holderCredentialsType,
                holderCredentialsCd
        };
        closeIsoDep();
        return result;
    }

    /**
     * 发售信息文件001B author:吴正尧
     *
     * @param cardAppAid
     * @return
     * @throws IOException
     */
    public String[] getMasterInfo(String cardAppAid) throws IOException {
        String apdu = null;
        String operatorNo = "";
        String cardRechargeCount = "";
        String branchNo = "";

        String[] result = {};
        responseStr = sendApdu(cardAppAid);
        if (responseStr != null && responseStr.endsWith("9000")) {
            apdu = "00b09b0032";
            responseStr = sendApdu(apdu);
            Logger.e("001b:" + responseStr);
            if (responseStr.endsWith("9000")) {
                operatorNo = responseStr.substring(16, 24);
                cardRechargeCount = responseStr.substring(54, 58);
                branchNo = responseStr.substring(8, 16);
            }
        }
        result = new String[]{
                operatorNo, cardRechargeCount, branchNo
        };
        closeIsoDep();
        return result;

    }

    /**
     * 复合交易记录文件0017 author:吴正尧
     *
     * @param cardAppAid
     * @return
     * @throws IOException
     */
    public String[] MuitiTradeRecord(String cardAppAid) throws IOException {
        String apdu = null;
        String cityCode = "";
        String[] result = {};
        responseStr = sendApdu(cardAppAid);
        if (responseStr != null && responseStr.endsWith("9000")) {
            apdu = "00b097003C";
            responseStr = sendApdu(apdu);
            Logger.e("0017:" + responseStr);
            if (responseStr.endsWith("9000")) {
                if (TextUtils.equals(cardAppAid, AppConst.APP_MF_JTB)) {
                    cityCode = responseStr.substring(12, 16);
                } else {
                    cityCode = responseStr.substring(4, 8);
                }

                Logger.e("城市代码---->" + cityCode);
                result = new String[]{
                        cityCode
                };
            }
        }
//        closeiIsodep();
        return result;

    }

    /**
     * 8050返回6个字段 ：原交易金额，交易序号（计数器），密钥版本号，算法标识，伪随机数，mac1 author：赵文星
     *
     * @param money ,充值金额，单位为分
     * @param posId
     * @return
     * @throws IOException
     */
    public String[] getRecharge8050Resp(String money, String posId)
            throws IOException {
        String apdu = "00A4040008A000000632010105";
        responseStr = sendApdu(apdu);
        if (responseStr.endsWith("9000")) {
            // 执行成功不做操作
        } else {
            return new String[]{};
        }

        String moneyStr = (int) (Double.parseDouble(money)) + "";
        Logger.e("充值金额为:" + moneyStr);
        // 圈存初始化 8050 00 02 0b + 01密钥标识符 + 金额 + 终端机编号
        apdu = "805000020B" + "01"
                + CommonUtils.longToHex(Long.parseLong(moneyStr), 8) + posId;
        Logger.e("8050发送指令----->" + apdu);
        responseStr = sendApdu(apdu);

        if (responseStr.endsWith("9000")) {
            Logger.e("8050收到返回----->" + responseStr);
            // 返回值1-4字节为交易前余额
            moneyBeforeTrade = responseStr.substring(0, 8);
            // 5-6字节为卡计数器
            cardCounter = responseStr.substring(8, 12);
            String cardKeyVer = responseStr.substring(12, 14);
            String computtag = responseStr.substring(14, 16);
            // 9-12字节为伪随机数
            randomNumber = responseStr.substring(16, 24);
            // 13-16为MAC1
            mac1 = responseStr.substring(24, 32);
            Logger.e("-------8050> mac1 : " + mac1);
            Logger.e("-------8050> randomNumber : " + randomNumber);
            Logger.e("-------8050> moneyBeforeTrade : " + moneyBeforeTrade);
            Logger.e("-------8050> cardCounter : " + cardCounter);
            return new String[]{
                    moneyBeforeTrade, cardCounter, cardKeyVer,
                    computtag, randomNumber, mac1
            };
        }
        return new String[]{};
    }

    public String recharge8052(String txntime, String mac2) throws IOException {
        String apdu = "805200000b" + txntime + mac2;
        Logger.e(apdu);
        responseStr = sendApdu(apdu);
        Logger.e(apdu + "8052-------->" + responseStr);
        if (responseStr.endsWith("9000")) {
            Logger.e("8052圈存成功");
            String tac = responseStr.substring(0, 12);
            Logger.e("-------8052> tac : " + tac);
            closeIsoDep();
            return tac;
        }
        return responseStr;
    }

    /**
     * 获取建设部认证码 author:吴正尧
     *
     * @return
     * @throws IOException
     */
    public String getCardAuthNo() throws IOException {
        String apdu = "00a40000023f01";
        responseStr = sendApdu(apdu);
        if (responseStr.endsWith("9000")) {
            // 执行成功不做操作
        } else {
            return null;
        }
        apdu = "80ca000009";
        Logger.e("消费初始化指令---->" + apdu);
        responseStr = sendApdu(apdu);
        if (responseStr.endsWith("9000")) {
            Logger.e("建设部认证码指令返回值---->" + responseStr);
            responseStr = responseStr.substring(0, responseStr.length() - 4);
            return responseStr;
        }
        return null;
    }

    /**
     * 8050返回2个字段 ：随机数，卡片交易序号 author：吴正尧
     *
     * @param money
     * @param posid
     * @return
     * @throws IOException
     */
    public String[] getPay8050Resp(String money, String posid)
            throws IOException {
        String apdu = "00a40000023f01";
        responseStr = sendApdu(apdu);
        if (responseStr.endsWith("9000")) {
            // 执行成功不做操作
        } else {
            return null;
        }
        // 圈存初始化 8050 00 02 0b + 01密钥标识符 + 金额 + 终端机编号
        apdu = "805001020b" + "01"
                + CommonUtils.longToHex(Long.parseLong(money), 8) + posid;
        Logger.e("消费初始化指令---->" + apdu);
        responseStr = sendApdu(apdu);
        if (responseStr.endsWith("9000")) {
            Logger.e("消费初始化指令返回值---->" + responseStr);
            randomNumber = responseStr.substring(22, 30);
            trandNo = responseStr.substring(8, 12);
            return new String[]{
                    randomNumber, trandNo
            };
        }
        return null;
    }

    public String pay8054(String termxh, String txntime, String mac1)
            throws IOException {
        String apdu = "805401000f" + termxh + txntime + mac1;
        responseStr = sendApdu(apdu);
        Logger.e(apdu + "-------->" + responseStr);
        if (responseStr.endsWith("9000")) {
            Logger.e("8054消费成功");
            String tac = responseStr.substring(0, 12);
            Logger.e("-------> tac : " + tac);
            closeIsoDep();
            return tac;
        }
        return "";
    }

    public String RamdomNum() throws IOException {
        String apdu = "0084000004";
        Logger.e(apdu);
        responseStr = sendApdu(apdu);
        String random = responseStr.substring(0, responseStr.length() - 4);
        Logger.e("---------------------random--->" + random);
        return random;
    }
    public String RamdomNum2() throws IOException {
        String apdu = "0084000008";
        Logger.e(apdu);
        responseStr = sendApdu(apdu);
        String random = responseStr.substring(0, responseStr.length() - 4);
        Logger.e("---------------------random--->" + random);
        return random;
    }

    /**
     * 修改卡片有效期  作者：吴正尧
     *
     * @param apduId
     * @return
     * @throws IOException
     */
    public String recharge_yxrq(String apduId) throws IOException {
//        String apdu = AppConst.APP_MF_JTB;
        String txtReturn = "";
//        responseStr = sendApdu(apdu);
//        if (responseStr != null && responseStr.endsWith ("9000")) {
        Logger.e("------------发送的APDU-apdu--->" + apduId);
        txtReturn = sendApdu(apduId.toUpperCase());
        Logger.e("---------------------recharge_yxrq--->" + txtReturn);
//        }
        return txtReturn;

    }

    /**
     * 2603修改卡片年审有效期  作者：吴正尧
     *
     * @param apduId
     * @return
     * @throws IOException
     */
    public String limited_yxrq(String apduId) throws IOException {
//        String apdu = AppConst.APP_MF_JTB;
        String txtReturn = "";
//        responseStr = sendApdu(apdu);
//        if (responseStr != null && responseStr.endsWith("9000")) {
        Logger.e("------------发送的APDU-apdu--->" + apduId);
        txtReturn = sendApdu(apduId.toUpperCase());
        Logger.e("-------------------- limited_yxrq--->" + txtReturn);
//        }
//        closeIsoDep();
        return txtReturn;

    }

    /*
    * 防拔
    * */
    public String fangBa(String apduAId, String jylx, String transId, String successMoney, String balance) throws IOException {
        String txtReturn = "";
        responseStr = sendApdu(apduAId);
        if (responseStr.endsWith("9000")) {//=====================9000防拔监测成功==========================================
            String apdu = String.format("805a00%s02%s08", jylx, transId);
            responseStr = sendApdu(apdu);
            if (responseStr.endsWith("9000")) {
                // 返回值后4个字节
                String emac = responseStr.substring(8, 16);
                if (successMoney.equals(balance)) {
                    // 只要有tag存在就说明充值成功
                    if (!TextUtils.isEmpty(emac)) {
                        //成功不上送
                        txtReturn = "补登充值成功";
                        Logger.e("防拔检测写卡成功");
                    } else {
                        txtReturn = "充值未知，如有问题请联系客服";
                        // 上送未知
                        Logger.e("1 防拔 金额异常 补登充值，写卡成功 并上送未知...response =" + responseStr);
                    }
                }
            } else if (responseStr.toString().endsWith("9406")) {//=====================9406防拔监测失败==========================================
                if (balance.equals(balance)) {
                    Logger.e("防拔充值失败" + responseStr);
                    txtReturn = " 充值失败，可以在订单列表重复充值";
                    //Toast.makeText(TryActivity.this, "充值失败，可以在订单列表重复充值", Toast.LENGTH_LONG).show();
                    if (TextUtils.isEmpty(responseStr)) {
                        txtReturn = "";
                    }
                } else {
                    Logger.e("2 防吧检查 金额异常 补登充值，写卡失败 并上送未知...response =" + responseStr);
                }
            } else {
                txtReturn = "充值未知，如有问题请联系客服";
                Logger.e("3补登充值，写卡成功 并上送未知...response =" + responseStr);
            }
            closeIsoDep();
        } else {//=====================文件选择失败==========================================
            txtReturn = "选文件异常,请再次贴卡";
        }
        return txtReturn;

    }

    @Override
    public void closeIsoDep() {
        // TODO 关闭NFC连接
        try {
            isodep.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
