package com.utils;

import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;
import okhttp3.Call;

import java.io.File;
import java.util.List;
import java.util.Random;

public class DownloadUtils {

    /**
     * 下载图片列表，同步
     *
     * @param filePath  下载的目标文件目录
     * @param imageList 下载地址
     */
    public static void downloadImageSyn(String filePath, List<String> imageList) {
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
                downloadImageSyn(filePath, imageList);
            }
        });

    }
}
