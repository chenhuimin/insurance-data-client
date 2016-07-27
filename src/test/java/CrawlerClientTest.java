import com.laanto.it.insurance_data_client.ApacheHttpClient4;
import com.laanto.it.insurance_data_client.Client;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by chenhuimin on 2016/7/25 0025.
 */
public class CrawlerClientTest {
    public static final Logger logger = LoggerFactory.getLogger(CrawlerClientTest.class);
    private Client client;
    private String domain = "";
    private Map<String, String> nameValues = new HashMap<>();
    private String templateUrl = "";
    private String jsonTemplate = "";
    private List<String> genderList;
    private List<String> ageList;
    private List<String> payPeriodList;
    private List<String> durationList;
    private List<String> condition1List;
    private String fileName = "data/bainian_jiankangyibailiangquan.csv";
    private String fileName2 = "data/bainian_jiankangyibaizhongji.csv";

//    @Before
//    public void setup() throws Exception {
//        client = new Client(ApacheHttpClient4.newInstance());
//        domain = ".bxr.im";
//        nameValues.put("_session_id", "797229bebd008bc69d3e1f8c573dbeb9");
////        templateUrl = "https://p.bxr.im/agent/cases/calculate_rate?channel_id=1&outer_channel=1001001001&sso_id=3336162" +
////                "&customer%5Bins_gender%5D=$INS_GENDER&customer%5Bins_age%5D=$INS_AGE&cal_type=0" +
////                "&product%5B%5D%5Bproduct_id%5D=572b0caf9161ec58ab000007&product%5B%5D%5Binsurance_amount%5D=10000" +
////                "&product%5B%5D%5Bpay_period%5D=$PAY_PERIOD" +
////                "&product%5B%5D%5Binsurance_period%5D=%E7%BB%88%E8%BA%AB" +
////                "&product%5B%5D%5Bcustom%5D%5B%E6%98%AF%E5%90%A6%E5%90%B8%E7%83%9F%5D=$CONDITION1" +
////                "&theme=fenhong&customer%5Bcustomer_name%5D=";
//        templateUrl = "https://p.bxr.im/agent/cases/calculate_rate?channel_id=1&outer_channel=1001001001&sso_id=3336162" +
//                "&customer%5Bins_gender%5D=$INS_GENDER&customer%5Bins_age%5D=$INS_AGE&cal_type=0" +
//                "&product%5B%5D%5Bproduct_id%5D=55110fa103af78335b000008&product%5B%5D%5Binsurance_amount%5D=10000" +
//                "&product%5B%5D%5Bpay_period%5D=$PAY_PERIOD" +
//                "&product%5B%5D%5Binsurance_period%5D=%E7%BB%88%E8%BA%AB" +
//                "&theme=fenhong&customer%5Bcustomer_name%5D=";
//
////        templateUrl = "https://p.bxr.im/agent/cases/calculate_rate?channel_id=1&outer_channel=1001001001&sso_id=3336162" +
////                "&customer%5Bins_gender%5D=$INS_GENDER&customer%5Bins_age%5D=$INS_AGE&cal_type=0" +
////                "&product%5B%5D%5Bproduct_id%5D=541295c203af78b746000003&product%5B%5D%5Binsurance_amount%5D=10000" +
////                "&product%5B%5D%5Bpay_period%5D=$PAY_PERIOD" +
////                "&product%5B%5D%5Binsurance_period%5D=1%E5%B9%B4" +
////                "&product%5B%5D%5Bcustom%5B%E8%81%8C%E4%B8%9A%E6%88%96%E5%B7%A5%E7%A7%8D%E7%B1%BB%E5%88%AB%5D%5D=$CONDITION1" +
////                "&theme=fenhong&customer%5Bcustomer_name%5D=";
//
//        genderList = Arrays.asList("男", "女");
////        ageList = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
////                "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
////                "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
////                "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
////                "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
////                "50", "51", "52", "53", "54", "55", "56", "57", "58", "59",
////                "60", "61", "62", "63", "64", "65");
//        ageList = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
//                "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
//                "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
//                "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
//                "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
//                "50", "51", "52", "53", "54", "55", "56", "57", "58", "59",
//                "60");
//        payPeriodList = Arrays.asList("趸交", "3年交", "5年交", "10年交", "15年交", "20年交");
//        //payPeriodList = Arrays.asList("趸交");
//        condition1List = Arrays.asList("1类", "2类", "3类", "4类", "5类", "6类");
//
//
//        // https://p.bxr.im/agent/cases/calculate_rate?channel_id=1&outer_channel=1001001001&sso_id=3336162&customer[ins_gender]=男&customer[ins_age]=0&cal_type=0
//        // &product[][product_id]=572b0caf9161ec58ab000007&product[][insurance_amount]=10000&product[][pay_period]=趸交&product[][insurance_period]=终身&product[][custom][是否吸烟]=否
//        // &product[][product_id]=572b17399161ec60f5000014&product[][insurance_amount]=10000&product[][pay_period]=趸交&product[][insurance_period]=终身&product[][custom[是否吸烟]]=否
//        // &product[][product_id]=5141432f08657502c200000a&product[][insurance_amount]=10000&product[][pay_period]=趸交&product[][insurance_period]=1年
//        // &theme=fenhong&customer[customer_name]=
//    }

    @Before
    public void setupForBXS() {
        client = new Client(ApacheHttpClient4.newInstance());
        domain = ".winbaoxian.com";
        nameValues.put("token", "f8e55a3c635f4ef6b02ad17c20469c77");
        templateUrl = "http://app.winbaoxian.com/planBook/calculate";
        jsonTemplate = "{\"baotype\":621,\"insuranceTypeId\":343,\"sex\":$SEX,\"age\":$AGE,\"idea\":-1,\"csex\":1,\"baoImgArr\":[\"logo_bnjkyb.jpg\",\"logo_addi_jkyb.png\"]," +
                "\"additionalShow\":{\"yiwaiSH\":true,\"yiwaiYl\":true,\"zhuyuanJT\":true,\"zhuyuanYL\":true},\"years\":$YEARS,\"duration\":$DURATION,\"baoe621\":100000,\"callMethod\":1}";

        genderList = Arrays.asList("1", "2");

        ageList = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
                "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
                "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
                "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
                "50", "51", "52", "53", "54", "55", "56", "57", "58", "59",
                "60");
        payPeriodList = Arrays.asList("5", "10", "15", "20");

        durationList = Arrays.asList("70", "80", "100");

    }

    /**
     * 保险人抓取方法，带额外条件
     * startPoint设置上次成功的次数，下次抓取时可以设置startPoint为上次成功的次数开始抓取，避免重复之前的数据抓取
     * @throws UnsupportedEncodingException
     * @throws InterruptedException
     */

    @Test
    public void testBXRExecuteGetWithCondition() throws UnsupportedEncodingException, InterruptedException {
        int startPoint = 0;
        int curPoint = 0;
        boolean flag = true;
        for (String condition1 : condition1List) {
            if (!flag) {
                logger.info("next startPoint={}", startPoint);
                break;
            }
            for (String gender : genderList) {
                if (!flag) {
                    break;
                }
                for (String age : ageList) {
                    if (!flag) {
                        break;
                    }
                    for (String payPeriod : payPeriodList) {
                        if (!flag) {
                            break;
                        }
                        curPoint++;
                        logger.info("curPoint={}", curPoint);
                        if (curPoint > startPoint) {
                            String requestUrl = templateUrl.replace("$INS_GENDER", URLEncoder.encode(gender, "utf-8")).replace("$INS_AGE", URLEncoder.encode(age, "utf-8"))
                                    .replace("$PAY_PERIOD", URLEncoder.encode(payPeriod, "utf-8")).replace("$CONDITION1", URLEncoder.encode(condition1, "utf-8"));
                            logger.info("url={}", requestUrl);
                            Map<String, Object> result = client.executeHttpGetRequest(requestUrl, nameValues, domain);
                            Integer statusCode = (Integer) result.get("statusCode");
                            JSONObject jsonObject = (JSONObject) result.get("jsonObject");
                            if (statusCode == HttpStatus.SC_OK && jsonObject != null) {
                                logger.info(jsonObject.toString());
                                if (jsonObject.toString().indexOf("rates") > 0) {
                                    JSONArray rates = jsonObject.getJSONArray("rates");
                                    if (rates != null && !rates.isEmpty()) {
                                        JSONObject rateObject = rates.getJSONObject(0);
                                        Double rate = rateObject.getDouble("rate");
                                        if (rate != null) {
                                            StringBuilder stringBuilder = new StringBuilder();
                                            stringBuilder.append("农银意外伤害保险（2014）").append(",");
                                            stringBuilder.append(age).append(",");
                                            stringBuilder.append(gender).append(",");
                                            stringBuilder.append(payPeriod).append(",");
                                            stringBuilder.append("1年").append(",");
                                            stringBuilder.append(rate.toString()).append(",");
                                            stringBuilder.append("10000").append(",");
                                            stringBuilder.append(condition1).append("\n");
                                            logger.info("line={}", stringBuilder.toString());
                                            appendToFile(fileName, stringBuilder.toString());
                                        }
                                    }
                                }
                                startPoint = curPoint;
                            } else {
                                flag = false;
                            }
                        }
                        Thread.sleep(500);
                    }
                }
            }
        }


    }

    /**
     * 保险人抓取方法，不带额外条件
     * startPoint设置上次成功的次数，下次抓取时可以设置startPoint为上次成功的次数开始抓取，避免重复之前的数据抓取
     * @throws UnsupportedEncodingException
     * @throws InterruptedException
     */

    @Test
    public void testBXRExecuteGetWithoutCondition() throws UnsupportedEncodingException, InterruptedException {
        int startPoint = 0;
        int curPoint = 0;
        boolean flag = true;
        for (String gender : genderList) {
            if (!flag) {
                break;
            }
            for (String age : ageList) {
                if (!flag) {
                    break;
                }
                for (String payPeriod : payPeriodList) {
                    if (!flag) {
                        break;
                    }
                    curPoint++;
                    logger.info("curPoint={}", curPoint);
                    if (curPoint > startPoint) {
                        String requestUrl = templateUrl.replace("$INS_GENDER", URLEncoder.encode(gender, "utf-8")).replace("$INS_AGE", URLEncoder.encode(age, "utf-8"))
                                .replace("$PAY_PERIOD", URLEncoder.encode(payPeriod, "utf-8"));
                        logger.info("url={}", requestUrl);
                        Map<String, Object> result = client.executeHttpGetRequest(requestUrl, nameValues, domain);
                        Integer statusCode = (Integer) result.get("statusCode");
                        JSONObject jsonObject = (JSONObject) result.get("jsonObject");
                        if (statusCode == HttpStatus.SC_OK && jsonObject != null) {
                            logger.info(jsonObject.toString());
                            if (jsonObject.toString().indexOf("rates") > 0) {
                                JSONArray rates = jsonObject.getJSONArray("rates");
                                if (rates != null && !rates.isEmpty()) {
                                    JSONObject rateObject = rates.getJSONObject(0);
                                    Double rate = rateObject.getDouble("rate");
                                    if (rate != null) {
                                        StringBuilder stringBuilder = new StringBuilder();
                                        stringBuilder.append("守护一生重疾").append(",");
                                        stringBuilder.append(age).append(",");
                                        stringBuilder.append(gender).append(",");
                                        stringBuilder.append(payPeriod).append(",");
                                        stringBuilder.append("终身").append(",");
                                        stringBuilder.append(rate.toString()).append(",");
                                        stringBuilder.append("10000").append("\n");
                                        logger.info("line={}", stringBuilder.toString());
                                        appendToFile(fileName, stringBuilder.toString());
                                    }
                                }
                            }
                            startPoint = curPoint;
                        } else {
                            flag = false;
                        }
                    }
                    Thread.sleep(500);
                }
            }
        }


    }

    /**
     * 保险师抓取方法
     * startPoint设置上次成功的次数，下次抓取时可以设置startPoint为上次成功的次数开始抓取，避免重复之前的数据抓取
     * @throws InterruptedException
     */
    @Test
    public void testBXSExecutePostWithoutCondition() throws InterruptedException {
        int startPoint = 766;
        int curPoint = 0;
        boolean flag = true;
        for (String gender : genderList) {
            if (!flag) {
                logger.info("next startPoint={}", startPoint);
                break;
            }
            for (String age : ageList) {
                if (!flag) {
                    break;
                }
                for (String payPeriod : payPeriodList) {
                    if (!flag) {
                        break;
                    }
                    for (String duration : durationList) {
                        if (!flag) {
                            break;
                        }
                        curPoint++;
                        logger.info("curPoint={}", curPoint);
                        if (curPoint > startPoint) {
                            String json = jsonTemplate.replace("$SEX", gender).replace("$AGE", age)
                                    .replace("$YEARS", payPeriod).replace("$DURATION", duration);
                            logger.info("json={}", json);
                            List<NameValuePair> params = new ArrayList<>();
                            params.add(new BasicNameValuePair("jsonParameters", json));
                            params.add(new BasicNameValuePair("baotype", "621"));
                            params.add(new BasicNameValuePair("insuranceTypeId", "343"));
                            params.add(new BasicNameValuePair("sex", gender));
                            params.add(new BasicNameValuePair("age", age));
                            params.add(new BasicNameValuePair("idea", "-1"));
                            params.add(new BasicNameValuePair("csex", "1"));
                            params.add(new BasicNameValuePair("baoImgArr[]", "logo_bnjkyb.jpg"));
                            params.add(new BasicNameValuePair("baoImgArr[]", "logo_addi_jkyb.png"));
                            params.add(new BasicNameValuePair("additionalShow[yiwaiSH]", "true"));
                            params.add(new BasicNameValuePair("additionalShow[yiwaiYl]", "true"));
                            params.add(new BasicNameValuePair("additionalShow[zhuyuanJT]", "true"));
                            params.add(new BasicNameValuePair("additionalShow[zhuyuanYL]", "true"));
                            params.add(new BasicNameValuePair("years", payPeriod));
                            params.add(new BasicNameValuePair("duration", duration));
                            params.add(new BasicNameValuePair("baoe621", "100000"));
                            params.add(new BasicNameValuePair("callMethod", "1"));
                            Map<String, Object> result = client.executeHttpPostRequest(templateUrl, params, nameValues, domain);
                            Integer statusCode = (Integer) result.get("statusCode");
                            JSONObject jsonObject = (JSONObject) result.get("jsonObject");
                            if (statusCode == HttpStatus.SC_OK && jsonObject != null) {
                                logger.info(jsonObject.toString());
                                if (jsonObject.toString().indexOf("outNum") > 0) {
                                    JSONObject outNumObject = jsonObject.getJSONObject("data").getJSONObject("outNum");
                                    if (outNumObject != null) {
                                        JSONObject rate1Object = outNumObject.getJSONObject("621");
                                        if (rate1Object != null) {
                                            Double baof = rate1Object.getDouble("baof");
                                            if (baof != null) {
                                                StringBuilder stringBuilder = new StringBuilder();
                                                stringBuilder.append("健康壹佰两全").append(",");
                                                stringBuilder.append(age).append(",");
                                                if ("1".equalsIgnoreCase(gender)) {
                                                    stringBuilder.append("男").append(",");
                                                } else {
                                                    stringBuilder.append("女").append(",");
                                                }
                                                stringBuilder.append(payPeriod).append(",");
                                                stringBuilder.append(duration).append(",");
                                                stringBuilder.append(baof.toString()).append(",");
                                                stringBuilder.append("100000").append("\n");
                                                logger.info("line={}", stringBuilder.toString());
                                                appendToFile(fileName, stringBuilder.toString());
                                            }
                                        }

                                        JSONObject rate2Object = outNumObject.getJSONObject("6211");
                                        if (rate2Object != null) {
                                            Double baof = rate2Object.getDouble("baof");
                                            if (baof != null) {
                                                StringBuilder stringBuilder = new StringBuilder();
                                                stringBuilder.append("附加健康壹佰重疾").append(",");
                                                stringBuilder.append(age).append(",");
                                                if ("1".equalsIgnoreCase(gender)) {
                                                    stringBuilder.append("男").append(",");
                                                } else {
                                                    stringBuilder.append("女").append(",");
                                                }
                                                stringBuilder.append(payPeriod).append(",");
                                                stringBuilder.append(duration).append(",");
                                                stringBuilder.append(baof.toString()).append(",");
                                                stringBuilder.append("100000").append("\n");
                                                logger.info("line={}", stringBuilder.toString());
                                                appendToFile(fileName2, stringBuilder.toString());
                                            }
                                        }
                                    }
                                }
                                startPoint = curPoint;
                            } else {
                                flag = false;
                            }
                        }
                        Thread.sleep(500);
                    }
                }
            }
        }


    }

    private void appendToFile(String fileName, String line) {
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(line);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
