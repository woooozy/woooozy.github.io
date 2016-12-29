package net.sourceforge.simcpux.manager;

import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;

/**
 * *****************************************************************************
 * 作者： woozy
 * 开发日期： 2016/10/14.
 * 模块功能：
 * *****************************************************************************
 */
public class CardManager {
    public static String[][] TECHLISTS;
    public static IntentFilter[] FILTERS;

    static {
        try {
            TECHLISTS = new String[][] { { IsoDep.class.getName() },
                    { NfcV.class.getName() }, { NfcF.class.getName() }, };

            FILTERS = new IntentFilter[] { new IntentFilter(
                    NfcAdapter.ACTION_TECH_DISCOVERED, "*/*") };
        } catch (Exception e) {
        }
    }


}
