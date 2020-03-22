package com.reptile.juzi;

import java.util.ArrayList;

// 名言通 ———— 句子收集，爬取失败了。 因为返回 403
// www.mingyantong.com
public class Main {
    public static void main(String[] args) {
        MingYanTong ju = new MingYanTong();
        ArrayList<String> tags = ju.fetchTags();

        ArrayList<String> tagMenu = ju.fetchTagMenu(tags.get(0));

        ArrayList<String> contentList = ju.fetchTagMenuDetal(tagMenu.get(0));

        for (String s : contentList) {
            System.out.println(s);
        }
    }
}
