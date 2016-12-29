package net.sourceforge.simcpux.entity;

import java.io.Serializable;

public class QCHistory implements Serializable{
    /**
     * 圈存消费历史记录实体
     */
    private static final long serialVersionUID = 2505837407272388225L;
    private String tradeNum;
    private String balanceAfter;
    private String balance;
    private String tradeType;
    private String tradeTermno;
    private String tradeDate;
    private String tradeTime;
    private String terminalNum;
    private String TAC;
    public String getTradeNum() {
        return tradeNum;
    }
    public void setTradeNum(String tradeNum) {
        this.tradeNum = tradeNum;
    }
    public String getBalanceAfter() {
        return balanceAfter;
    }
    public void setBalanceAfter(String balanceAfter) {
        this.balanceAfter = balanceAfter;
    }
    public String getBalance() {
        return balance;
    }
    public void setBalance(String balance) {
        this.balance = balance;
    }
    public String getTradeType() {
        return tradeType;
    }
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
    public String getTradeTermno() {
        return tradeTermno;
    }
    public void setTradeTermno(String tradeTermno) {
        this.tradeTermno = tradeTermno;
    }
    public String getTradeDate() {
        return tradeDate;
    }
    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }
    public String getTradeTime() {
        return tradeTime;
    }
    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }
    public String getTAC() {
        return TAC;
    }
    public void setTAC(String tAC) {
        TAC = tAC;
    }
    public String getTerminalNum() {
        return terminalNum;
    }
    public void setTerminalNum(String terminalNum) {
        this.terminalNum = terminalNum;
    }


}
