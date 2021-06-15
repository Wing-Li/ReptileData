package com.reptile.gankmz;

import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;
import com.utils.DownloadUtils;
import okhttp3.Call;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GankMzMain {

    private static String mzURL = "https://gank.io/api/v2/data/category/Girl/type/Girl/page/2/count/10000";

    public static void main(String[] args) {
        OkhttpUtil.okHttpGet(mzURL, new CallBackUtil<GankMzGirlModel>() {

            @Override
            public GankMzGirlModel onParseResponse(Call call, Response response) {
                try {
                    if (response.body() == null) return null;

                    return GankMzGirlModel.objectFromData(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(GankMzGirlModel model) {
                List<String> imageList = new ArrayList<String>();

                model.data.forEach(
                        dataModel -> imageList.add(dataModel.url)
                );

                DownloadUtils.downloadImageSyn("GankMzGirl", imageList);

            }
        });
    }
}
