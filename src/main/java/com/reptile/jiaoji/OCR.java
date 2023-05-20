package com.reptile.jiaoji;

import com.config.MyConfig;
import com.google.gson.Gson;
import com.okhttp.OkhttpUtil;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;

public class OCR {

    static String token;

    public String getWordByImage(String imgUrl) {
        if (token == null || token.length() == 0) {
            token = getToken();
        }
        if (token == null) {
            System.out.println("百度云图片识别 Token 获取失败");
            return null;
        }

        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic" + "?access_token=" + token;

        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        HashMap<String, String> map = new HashMap<>();
        map.put("url", imgUrl);

        String result = OkhttpUtil.postSync(url, header, map);
        return result;
    }

    private String getToken() {
        HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("grant_type", "client_credentials");
        tokenMap.put("client_id", MyConfig.BAIDU_OCR_API_KEY);
        tokenMap.put("client_secret", MyConfig.BAIDU_OCR_SECRET_KEY);

        String result = OkhttpUtil.getSync("https://aip.baidubce.com/oauth/2.0/token", tokenMap);
        if (result != null) {
//            System.out.println("======= start 获取Token start ========");
//            System.out.println(result);
//            System.out.println("======= end 获取Token end ========");
            HashMap map = new Gson().fromJson(result, HashMap.class);
            String access_token = (String) map.get("access_token");

            return access_token;
        }
        return null;
    }

    private static String getImageBase64(File file) {
        InputStream in = null;
        byte[] data = null;

        try {
            in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Base64.getEncoder().encodeToString(data);
    }


}
