package net.sourceforge.simcpux.entity;

import java.io.Serializable;

/**
 * *****************************************************************************
 * 作者： woozy
 * 开发日期： 2016/11/8.
 * 模块功能：
 * *****************************************************************************
 */
public class OrderEntity implements Serializable{
    private String cardpan;
    private String ordertime;
    private String ordercode;
    private String orderstatus;

    public String getCardpan() {
        return cardpan;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public String getOrdercode() {
        return ordercode;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "cardpan='" + cardpan + '\'' +
                ", ordertime='" + ordertime + '\'' +
                ", ordercode='" + ordercode + '\'' +
                ", orderstatus='" + orderstatus + '\'' +
                '}';
    }
}
