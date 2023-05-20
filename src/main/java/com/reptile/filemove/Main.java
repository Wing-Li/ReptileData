package com.reptile.filemove;

import com.utils.MyUtils;

import java.io.File;

public class Main {


    public static void main(String[] args) {
        String filePath = "/Users/lyl/life/v/";

        File path = new File(filePath);
        File[] listFiles = path.listFiles();
        for (File file : listFiles) {
            if (file.isFile()) continue;

            File[] movieList = file.listFiles();
            for (File m : movieList) {
                if (m.getAbsolutePath().lastIndexOf(".mp4") > 0
                        || m.getAbsolutePath().lastIndexOf(".MOV") > 0
                        || m.getAbsolutePath().lastIndexOf(".TS") > 0) {
                    moveFile(m.getAbsolutePath(), filePath + m.getName());
                }
            }
        }
    }

    private static void moveFile(String startPath, String endPath) {
        try {
            File startFile = new File(startPath);
            File endFile = new File(endPath);
            if (!endFile.exists()) {
                endFile.createNewFile();
            }
            if (startFile.renameTo(new File(endPath))) {
                System.out.println("File is moved successful!");
                MyUtils.logD(String.format("文件移动成功！文件名：{%s} 目标路径：{%s}", startPath, endPath));
            } else {
                System.out.println("File is failed to move!");
                MyUtils.logD(String.format("文件移动失败！文件名：《{%s}》 起始路径：{%s}", startPath, startPath));
            }
        } catch (Exception e) {

        }
    }
}
