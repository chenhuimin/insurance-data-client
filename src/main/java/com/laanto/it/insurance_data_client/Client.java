package com.laanto.it.insurance_data_client;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * Created by chenhuimin on 2016/7/25 0025.
 */
public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private HttpClient httpClient;

    public static void main(String[] args) {
        String url = "http://app.winbaoxian.com/planBook/calculate";
        //String url = "https://store.91baofeng.com/shop/statis/totalUV/qeLGp2Am";
        // String url = "https://p.bxr.im/agent/cases/calculate_rate?channel_id=1&outer_channel=1001001201&sso_id=3336162&customer%5Bins_gender%5D=%E5%A5%B3&customer%5Bins_age%5D=0&cal_type=0&product%5B%5D%5Bproduct_id%5D=57452ca703af7858ba000002&product%5B%5D%5Binsurance_amount%5D=10000&product%5B%5D%5Bpay_period%5D=10%E5%B9%B4%E4%BA%A4&product%5B%5D%5Binsurance_period%5D=%E8%87%B388%E5%B2%81&product%5B%5D%5Bproduct_id%5D=55b998c69161ec381d000004&theme=fenhong&customer%5Bcustomer_name%5D=";
        //executeHttpGetRequest(url);
        Client client = new Client(ApacheHttpClient4.newInstance());
        Map<String, String> nameValues = new HashMap<>();
//        nameValues.put("JSESSIONID", "B3D600DD1CD23ABAD3DD4EAAFB08418E");
//        nameValues.put("gr_user_id", "926eaf67-df71-4522-b69e-4b009c75c3bd");
        nameValues.put("token", "f8e55a3c635f4ef6b02ad17c20469c77");
//        nameValues.put("SERVERID", "40c64587d2f26f6bb62cb954d12bbc22|1469592028|1469589963");
//        nameValues.put("Hm_lvt_59c99e4444d9fb864780844a90b61aea", "1469521655");
//        nameValues.put("Hm_lpvt_59c99e4444d9fb864780844a90b61aea", "1469592078");
//        nameValues.put("gr_session_id_b9b061e151df5788", "066ae957-9911-435a-8548-45b54db60a1f");
        String json = "{\"baotype\":621,\"insuranceTypeId\":343,\"sex\":1,\"age\":2,\"idea\":-1,\"csex\":1,\"baoImgArr\":[\"logo_bnjkyb.jpg\",\"logo_addi_jkyb.png\"],\"additionalShow\":{\"yiwaiSH\":true,\"yiwaiYl\":true,\"zhuyuanJT\":true,\"zhuyuanYL\":true},\"years\":10,\"duration\":100,\"baoe621\":100000,\"callMethod\":1}";
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("jsonParameters", json));
        params.add(new BasicNameValuePair("baotype", "621"));
        params.add(new BasicNameValuePair("insuranceTypeId", "343"));
        params.add(new BasicNameValuePair("sex","1"));
        params.add(new BasicNameValuePair("age","2"));
        params.add(new BasicNameValuePair("idea","-1"));
        params.add(new BasicNameValuePair("csex","1"));
        params.add(new BasicNameValuePair("baoImgArr[]","logo_bnjkyb.jpg"));
        params.add(new BasicNameValuePair("baoImgArr[]","logo_addi_jkyb.png"));
        params.add(new BasicNameValuePair("additionalShow[yiwaiSH]","true"));
        params.add(new BasicNameValuePair("additionalShow[yiwaiYl]","true"));
        params.add(new BasicNameValuePair("additionalShow[zhuyuanJT]","true"));
        params.add(new BasicNameValuePair("additionalShow[zhuyuanYL]","true"));
        params.add(new BasicNameValuePair("years","10"));
        params.add(new BasicNameValuePair("duration","100"));
        params.add(new BasicNameValuePair("baoe621","100000"));
        params.add(new BasicNameValuePair("callMethod","1"));

        client.executeHttpPostRequest(url, params, nameValues, ".winbaoxian.com");
    }

    public Client() {
    }

    public Client(HttpClient httpClient) {
        this.httpClient = httpClient;

    }


    public Map<String, Object> executeHttpGetRequest(String url, Map<String, String> nameValues, String domain) {
        Map<String, Object> result = new HashMap<>();
        HttpGet httpGet = new HttpGet();
        HttpResponse httpResponse = null;
        httpGet.setHeader(HttpHeaders.ACCEPT, "application/json;charset=UTF-8");
        try {
            httpGet.setURI(new URI(url));
            httpResponse = httpClient.execute(httpGet, createLocalContext(nameValues, domain));
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            result.put("statusCode", statusCode);
            logger.info("httpResponse返回状态码：{}", statusCode);
            if (statusCode == HttpStatus.SC_OK) {
                try {
                    String rawJson = EntityUtils.toString(httpResponse.getEntity(), Charset.forName("utf-8"));
                    logger.info(rawJson);
                    JSONObject jsonObject = JSONObject.fromObject(rawJson);
                    if (jsonObject != null) {
                        result.put("jsonObject", jsonObject);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            //release httpResponse stream
            try {
                EntityUtils.consume(httpResponse.getEntity());
                //release connection
                if (httpGet != null) {
                    httpGet.releaseConnection();
                }
            } catch (IOException e) {
                logger.error("Close http reponse entity's content stream failed", e);
            }
        } catch (IOException e) {
            e.printStackTrace();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<String, Object> executeHttpPostRequest(String url, List<NameValuePair> params, Map<String, String> nameValues, String domain) {
        Map<String, Object> result = new HashMap<>();
        HttpPost httpPost = new HttpPost();
        HttpResponse httpResponse = null;
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
        try {
            httpPost.setURI(new URI(url));
//            String encoderJson = URLEncoder.encode(json, HTTP.UTF_8);
//            StringEntity se = new StringEntity(json);
//            se.setContentType("text/json");
//            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//            httpPost.setEntity(se);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf-8");
            httpPost.setEntity(formEntity);
            httpResponse = httpClient.execute(httpPost, createLocalContext(nameValues, domain));
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            result.put("statusCode", statusCode);
            logger.info("httpResponse返回状态码：{}", statusCode);
            if (statusCode == HttpStatus.SC_OK) {
                try {
                    String rawJson = EntityUtils.toString(httpResponse.getEntity(), Charset.forName("utf-8"));
                    logger.info(rawJson);
                    JSONObject jsonObject = JSONObject.fromObject(rawJson);
                    if (jsonObject != null) {
                        result.put("jsonObject", jsonObject);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            //release httpResponse stream
            try {
                EntityUtils.consume(httpResponse.getEntity());
                //release connection
                if (httpPost != null) {
                    httpPost.releaseConnection();
                }
            } catch (IOException e) {
                logger.error("Close http reponse entity's content stream failed", e);
            }
        } catch (IOException e) {
            e.printStackTrace();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return result;

    }

    private HttpContext createLocalContext(Map<String, String> nameValues, String domain) {

        // Create a local instance of cookie store
        CookieStore cookieStore = new BasicCookieStore();
        // Create local HTTP context
        HttpContext localContext = new BasicHttpContext();
        // Bind custom cookie store to the local context
        if (nameValues != null && !nameValues.isEmpty()) {
            for (Map.Entry<String, String> entry : nameValues.entrySet()) {
                cookieStore.addCookie(createCookie(entry.getKey(), entry.getValue(), domain));
            }
        }
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        return localContext;

    }

    private BasicClientCookie createCookie(String name, String value, String domain) {
        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(value)) {
            org.apache.http.impl.cookie.BasicClientCookie cookie = new org.apache.http.impl.cookie.BasicClientCookie(name, value);
            cookie.setVersion(1);
            cookie.setPath("/");
            cookie.setDomain(domain);
            return cookie;
        } else {
            return null;
        }
    }

}
