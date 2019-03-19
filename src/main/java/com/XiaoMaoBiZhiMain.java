package com;


import com.utils.DownloadUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XiaoMaoBiZhiMain {

    private static String BASE_URL = "http://www.netbian.com";

    public static void main(String[] args) {

        List<String> summaryList = getSummaryList();
        summaryList = summaryList.subList(69, summaryList.size() - 1);
        System.out.println("获取到 " + summaryList.size() + " 张图片");
        DownloadUtils.downloadImageSyn("xiaomaobizhi", summaryList);
    }

    private static List<String> getSummaryList() {
        List<String> imageList = new ArrayList<>();

        try {
            for (int i = 1; i <= 14; i++) {
                String url = BASE_URL + "/s/xiaomao/type96";

                if (i == 1) {
                    url = url + ".htm";
                } else {
                    url = url + "_" + i + ".htm";
                }

                Document document = Jsoup.connect(url).get();
                Elements elements = document.select("div.list ul li");
                for (Element element : elements) {
                    Elements a_img = element.select("a img");
                    String src = a_img.attr("src");
                    imageList.add(src);
                    System.out.println("get Image: " + src);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageList;
    }


}
