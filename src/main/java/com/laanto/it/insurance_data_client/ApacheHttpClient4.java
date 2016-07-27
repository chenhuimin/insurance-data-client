package com.laanto.it.insurance_data_client;

import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.params.CookieSpecPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpProtocolParams;

import javax.net.ssl.SSLContext;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


/**
 * This class is used to create a new instance of Apache HttpClient and make initialization
 *
 * @author hui-min.chen@hp.com
 * @since 1.0.0
 */
public class ApacheHttpClient4 {

    private static final int CONNECTION_MAX_TOTAL = 800;
    private static final int CONNECTION_DEFAULT_MAX_PER_ROUTEL = 400;
    private static final String CLIENT_USER_AGENT = "Mozilla/5.0 (Linux; Android 5.0.2; MI 2 Build/LRX22G; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/45.0.2454.95 Mobile Safari/537.36WeiYi/2.1.0 AndroidApp";
    private static final int HTTP_CONNECTIONTIMEOUT = 30000;
    private static final int HTTP_SOCKET_TIMEOUT = 30000;
    private static DefaultHttpClient httpClient4 = null;

    /**
     * Create a HttpClient object and initialize it.
     *
     * @return DefaultHttpClient
     */

    public static DefaultHttpClient newInstance() {
        if (httpClient4 == null) {
            PoolingClientConnectionManager cxMgr = new PoolingClientConnectionManager(SchemeRegistryFactory.createDefault());
            cxMgr.setMaxTotal(CONNECTION_MAX_TOTAL);
            cxMgr.setDefaultMaxPerRoute(CONNECTION_DEFAULT_MAX_PER_ROUTEL);
            httpClient4 = new DefaultHttpClient(cxMgr);
            try {
                //Secure Protocol implementation.
                SSLContext ctx = SSLContext.getInstance("SSL");
                //Implementation of a trust manager for X509 certificates
                X509TrustManager tm = new X509TrustManager() {

                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }

                };
                ctx.init(null, new TrustManager[]{tm}, null);
                SSLSocketFactory ssf = new SSLSocketFactory(ctx);
                ssf.setHostnameVerifier(new AllowAllHostnameVerifier());
                ClientConnectionManager ccm = httpClient4.getConnectionManager();
                //register https protocol in httpclient's scheme registry
                SchemeRegistry sr = ccm.getSchemeRegistry();
                sr.register(new Scheme("https", 443, ssf));
            } catch (Exception e) {
                e.printStackTrace();
            }
            initializeHttpClient4(httpClient4);
        }
        return httpClient4;
    }

    /**
     * initialize the HttpClient
     *
     * @param httpClient
     */

    private static void initializeHttpClient4(DefaultHttpClient httpClient) {
        //set Redirect Strategy
        httpClient.setRedirectStrategy(new LaxRedirectStrategy());
        //set CookieStore
        //CookieStore cookieStore = new BasicCookieStore();
        //httpClient.setCookieStore(cookieStore);
        httpClient.getParams().setParameter(CookieSpecPNames.SINGLE_COOKIE_HEADER, Boolean.TRUE);
        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        httpClient.getParams().setParameter(HttpProtocolParams.USER_AGENT, CLIENT_USER_AGENT);
        //set connection timeout
        httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, HTTP_CONNECTIONTIMEOUT);
        //set read timeout
        httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, HTTP_SOCKET_TIMEOUT);

    }
}
