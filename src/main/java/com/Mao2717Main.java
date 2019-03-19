package com;


import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;
import com.utils.ThreadPoolUtil;
import okhttp3.Call;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class Mao2717Main {

    private static String BASE_URL = "https://www.2717.com";

    public static void main(String[] args) {
        String url = BASE_URL + "/zt/maomi/1/";

        List<String> summaryList = getSummaryList(url);
        summaryList = summaryList.subList(69, summaryList.size() - 1);
        System.out.println("获取到 " + summaryList.size() + " 张图片");
        downloadImage("mao2717", summaryList);
    }

    private static List<String> getSummaryList(String url) {
        List<String> imageList = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select("div.w800 div.contentBox1 div.picBox ul li");
            for (Element element : elements) {
                String href = element.select("a").attr("href");

                String detailUrl = BASE_URL + href;

                List<String> detailImageList = getDetailImageList(detailUrl);
                imageList.addAll(detailImageList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageList;
    }

    private static List<String> getDetailImageList(String detailUrl) {
        List<String> imgList = new ArrayList<>();

        for (int i = 1; i < 50; i++) {
            try {
                Document document = null;
                if (i == 1) {
                    document = Jsoup.connect(detailUrl).get();
                } else {
                    String url = detailUrl.replace(".html", "_" + i + ".html");
                    document = Jsoup.connect(url).get();
                }

                Elements imgEl = document.select("div.articleV4Body img");
                if (imgEl != null) {
                    String imgUrl = imgEl.attr("src");
                    imgList.add(imgUrl);
                    System.out.println("get Image: " + imgUrl);
                } else {
                    break;
                }

            } catch (Exception e) {
                break;
            }
        }

        return imgList;
    }

    private static void downloadImage(String filePath, List<String> imageList) {
        File file = new File(filePath);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
        }

        if (imageList.size() <= 0) return;

        String imgUrl = imageList.get(0);
        String iconName = System.currentTimeMillis() + new Random().nextInt(100) + ".jpg";

        OkhttpUtil.okHttpDownloadFile(imgUrl, new CallBackUtil.CallBackFile(file.getAbsolutePath(), iconName) {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(File response) {
                System.out.println(response.getAbsolutePath() + " 下载成功," + imageList.size());
                imageList.remove(0);
                downloadImage(filePath, imageList);
            }
        });

    }

}
