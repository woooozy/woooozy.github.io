package net.tsz.afinal.http;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;

/**
 * 
 * **********************************************************<br>
 * 模块功能: <br>
 * 作 者: 薛龙<br>
 * 开发日期：2013-8-7 下午5:35:53
 * 单 位：武汉天喻信息 研发中心
 * 修改日期：<br>
 * 修改人：<br>
 * 修改说明：<br>
 * *********************************************************<br>
 */
public class SSLSocketFactoryEx extends SSLSocketFactory {
        
        SSLContext sslContext = SSLContext.getInstance("TLS");
        
        public SSLSocketFactoryEx(KeyStore truststore) 
                        throws NoSuchAlgorithmException, KeyManagementException,
                        KeyStoreException, UnrecoverableKeyException {
                super(truststore);
                
                TrustManager tm = new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {return null;}  
    
            @Override  
            public void checkClientTrusted(
                            java.security.cert.X509Certificate[] chain, String authType)
                                            throws java.security.cert.CertificateException {}  
    
            @Override  
            public void checkServerTrusted(
                            java.security.cert.X509Certificate[] chain, String authType)
                                            throws java.security.cert.CertificateException {}
        };  
        sslContext.init(null, new TrustManager[] { tm }, null);  
    }  
    
    @Override  
    public Socket createSocket(Socket socket, String host, int port,boolean autoClose) throws IOException, UnknownHostException {  
            return sslContext.getSocketFactory().createSocket(socket, host, port,autoClose);  
    }  
    
    @Override  
    public Socket createSocket() throws IOException {  
        return sslContext.getSocketFactory().createSocket();  
    }  
}
