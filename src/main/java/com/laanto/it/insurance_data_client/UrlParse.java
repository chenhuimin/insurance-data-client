package com.laanto.it.insurance_data_client;


import com.lannto.it.imageserverclient.AliyunClient;
import com.lannto.it.imageserverclient.FileServiceUtils;
import jdk.nashorn.internal.runtime.ParserException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.security.provider.MD5;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by chenhuimin on 2016/8/4 0004.
 */
public class UrlParse {
    public void parser(String url) throws IOException {

        Map<String, String> imgMap = new HashMap<>();
        // 2. Or from an URL:
        Document doc = Jsoup.connect(url).timeout(10000).get();
        // Then select images inside it:
        Elements images = doc.select("img");

        AliyunClient aliyunClient = AliyunClient.getInstance();
        if (images != null && !images.isEmpty()) {
            for (Element img : images) {
                String dataSrc = img.attr("data-src");
                if (StringUtils.isNotBlank(dataSrc)) {
                    System.out.println(dataSrc);
                    System.out.println(DigestUtils.md5Hex(dataSrc));
                    imgMap.put(dataSrc, dataSrc);
                }
            }
            if (imgMap != null && !imgMap.isEmpty()) {
                for (Map.Entry<String, String> entry : imgMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    String aliyunImgUrl = saveToImgServer(value, aliyunClient);
                    if (StringUtils.isNotBlank(aliyunImgUrl)) {
                        imgMap.put(key, aliyunImgUrl);
                    }

                }
            }

            for (Element img : images) {
                if (img.hasAttr("data-src")) {
                    String dataSrc = img.attr("data-src");
                    img.attr("src", imgMap.get(dataSrc));
                    img.removeAttr("data-src");
                }
            }
        }

        System.out.println(doc.outerHtml());


    }

    private String saveToImgServer(String url, AliyunClient aliyunClient) {
        String imageUrl = null;
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = null;
        try {

            httpResponse = ApacheHttpClient4.newInstance().execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();
            long contentLength = httpEntity.getContentLength();
            String imgExt = getImgExtension(url);
            if (StringUtils.isNotBlank(imgExt)) {
                String urlPath = "zhiku_news_img/" + DigestUtils.md5Hex(url) + "." + imgExt;
                if (!aliyunClient.doesObjectExist(urlPath)) {
                    imageUrl = FileServiceUtils.uploadImageToImageServiceByHttp(urlPath, inputStream, contentLength);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageUrl;
    }

    private String getImgExtension(String url) {
        String ext = null;
        Pattern p = Pattern.compile("^(.*?)wx_fmt=(png|jpg|jpeg|gif){1}(.*)$");
        Matcher m = p.matcher(url);
        if (m.find()) {
            ext = m.group(2);
        }
        return ext;
    }


    public static void main(String[] args) throws IOException, ParseException {
        SimpleDateFormat isoSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat localSdf = new SimpleDateFormat("yyyy/MM/dd");
        String s1 = "2016-09-07";
        String s2 = "2016-09-07 11:11:11";
        System.out.println(isoSdf.parse(s1));

    }
}
