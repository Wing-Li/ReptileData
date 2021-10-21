package com.reptile.jiedian;

import com.okhttp.CallBackUtil;
import com.okhttp.OkhttpUtil;
import okhttp3.Call;
import okhttp3.Response;

import java.io.File;
import java.util.*;

public class UploadAppUtils {

    private static String filePath = "/Users/lyl/developer/project/jiedian/talent-app-android/Nodetech_sys_app/app/build/outputs/apk";
    private static List<File> mFileList;
    private static List<String> mFileListUploaded = new ArrayList<>();

    public static void main(String[] args) {
        File file = new File(filePath);
        File[] listArray = file.listFiles();
        mFileList = Arrays.asList(listArray);
        uploadList();
        uploadList();
        uploadList();
        uploadList();
        uploadList();
    }

    private static File getFile() {
        for (File file : mFileList) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                File[] apkListFiles = files[0].listFiles();
                for (File apkListFile : apkListFiles) {
                    if (!mFileListUploaded.contains(apkListFile.getName()) && apkListFile.getAbsolutePath().contains(".apk")) {
                        return apkListFile;
                    }
                }

            }
        }
        return null;
    }

    private static void uploadList() {
        File file = getFile();
        if (file != null) {
            System.out.println("\n====开始上传：\n" + file.getAbsolutePath());
            long startTime = System.currentTimeMillis();

            mFileListUploaded.add(file.getName());
            upload(file, new CallBackUtil<File>() {
                @Override
                public File onParseResponse(Call call, Response response) {
                    return null;
                }

                @Override
                public void onFailure(Call call, Exception e) {
                    System.out.println("上传：" + file.getName() + " 失败:");
                    e.printStackTrace();
                }

                @Override
                public void onResponse(File response) {
                    long time = (System.currentTimeMillis() - startTime) / 1000;
                    System.out.println(mFileListUploaded.size() + ", 上传 " + file.getName() + " 成功, 用时 " + time + "s");

                    if (mFileListUploaded.size() != mFileList.size()) {
                        uploadList();
                    } else {
                        System.out.println("\n======== 上传完毕 ===========");
                    }
                }
            });
        }
    }

    private static void upload(File file, CallBackUtil callBack) {
        String url = "https://www.pgyer.com/apiv2/app/upload";
        Map<String, File> fileMap = new HashMap<>();
        fileMap.put("file", file);
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("_api_key", "");

        OkhttpUtil.okHttpUploadMapFile(url, fileMap, "file", paramsMap, callBack);
    }
}
