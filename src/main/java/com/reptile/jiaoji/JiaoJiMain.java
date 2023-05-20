package com.reptile.jiaoji;

import com.google.gson.Gson;

import java.io.File;
import java.util.*;

public class JiaoJiMain {

    private static LCDB mLcdb;
    private static FileUpload mFileUpload;
    private static OCR mOcr;

    public static void main(String[] args) {
        mLcdb = new LCDB();
        mLcdb.init();

        mFileUpload = new FileUpload();

        mOcr = new OCR();



        ArrayList<HashMap<String, Object>> mapArrayList = ExcelUtils.get("女");

        mLcdb.saveUserInfo(mapArrayList);

        System.out.println("保存完成");

//        File rootFile = new File("OCR/男1");
//
//        File[] listFiles = rootFile.listFiles();
//        if (listFiles == null) {
//            System.err.println("文件不存在");
//            return;
//        }
//
//        List<File> fileList = Arrays.asList(listFiles);
//        fileSort(fileList);
//
//        ArrayList<HashMap<String, String>> mapArrayList = new ArrayList<>();
//        for (int i = 0; i < fileList.size(); i++) {  // 循环，人
//            File peopleFile = fileList.get(i);
//
//            if (peopleFile.getAbsolutePath().contains(".DS_Store")) {
//                continue;
//            }
//
//            File[] photos = peopleFile.listFiles();
//            List<File> peopleList = Arrays.asList(photos);
//            fileSort(peopleList);
//            if (peopleList.size() > 0) {
//
//                String path = peopleList.get(0).getAbsolutePath();
//                System.out.println(i + " start: " + path);
//
//                OCRModel ocrModel = uploadAndOcr(path);
////                // TODO：保存数据进数据库
//                ArrayList<String> photoList = uploadList(peopleList);
//                photoList.add(ocrModel.image_url);
////                // TODO: 将照片保存进数据库
//                HashMap<String, String> userInfo = getUserInfo(path, ocrModel, photoList);
//
//                mapArrayList.add(userInfo);
//                System.out.println(i + " end: " + peopleList.get(0).getAbsolutePath());
//                System.out.println("");
//            }
//
////            peopleFile.deleteOnExit();
//        }
//
//        System.out.println("=======  开始写入 Excel  ======= ");
//
//        ExcelUtils.write(mapArrayList);
//
//        System.out.println("=======  Excel 写入完毕  ======= ");

    }

    private static void fileSort(List<File> fileList) {
        Collections.sort(fileList, (o1, o2) -> {
            if (o1.isDirectory() && o2.isFile())
                return -1;
            if (o1.isFile() && o2.isDirectory())
                return 1;
            return o1.getName().compareTo(o2.getName());
        });
    }

    private static OCRModel uploadAndOcr(String filePath) {
        String user01Url = mFileUpload.upload("user", filePath);
        System.out.println("待解析的图片：" + user01Url);
        OCRModel ocrModel = null;
        try {
            Thread.sleep(500);

            String user01Json = mOcr.getWordByImage(user01Url);
            System.out.println("解析后的数据：" + user01Json);
            ocrModel = new Gson().fromJson(user01Json, OCRModel.class);
            if (ocrModel.error_code == 282112) {
                for (int i = 0; i < 30; i++) {
                    Thread.sleep(500);
                    user01Json = mOcr.getWordByImage(user01Url);
                    ocrModel = new Gson().fromJson(user01Json, OCRModel.class);
                    if (ocrModel.error_code != 282112) break;
                }
            }
            ocrModel.setImage_url(user01Url);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ocrModel;
    }

    private static ArrayList<String> uploadList(List<File> files) {
        ArrayList<String> result = new ArrayList<>();
        // 从第二个开始
        for (int i = 1; i < files.size(); i++) {
            String user = mFileUpload.upload("user", files.get(i).getAbsolutePath());
            result.add(user);
        }
        System.out.println("上传的图片地址：" + result.toString());
        return result;
    }

    private static HashMap<String, String> getUserInfo(String id, OCRModel model, ArrayList<String> photoList) {
        List<OCRModel.WordsResultModel> wordsResult = model.getWords_result();

        HashMap<String, String> map = new HashMap<>();

        map.put("id", id);
        map.put("photo", photoList.toString());

        // 是不是连续的
        for (int i = 0; i < wordsResult.size(); i++) {
            OCRModel.WordsResultModel resultModel = wordsResult.get(i);
            String words = resultModel.words;
            if (words.contains("呢称") || words.contains("昵称")) {
                map.put("name", getWord(words));
            } else if (i < 15 && words.contains("出生地")) {
                map.put("where", getWord(words));
            } else if (words.contains("现住地")) {
                map.put("nowWhere", getWord(words));
            } else if (words.contains("性别")) {
                map.put("gender", getWord(words));
            } else if (words.contains("生日")) {
                map.put("birthday", getWord(words));
            } else if (i < 15 && words.contains("身高")) {
                map.put("height", getWord(words));
            } else if (i < 15 && words.contains("体重")) {
                map.put("weight", getWord(words));
            } else if (i < 15 && words.contains("学历")) {
                map.put("edu", getWord(words));
            } else if (words.contains("工作")) {
                map.put("work", getWord(words));
            } else if (i < 16 && words.contains("房")) {
                map.put("house", getWord(words));
            } else if (words.contains("兄弟")) {
                map.put("family", getWord(words));
            } else if (words.contains("兴趣")) {
                map.put("hobby", getWord(words));
            } else if (words.contains("优点")) {
                String continuouStr = getWord(words);
                for (int j = i + 1; j < wordsResult.size(); j++) {
                    String trim = wordsResult.get(j).words.trim();
                    if (trim.contains("择偶要求")) {
                        break;
                    }
                    continuouStr += trim;
                    i = j;
                }
                map.put("advantage", continuouStr);
            } else if (words.contains("年龄")) {
                map.put("toAge", getWord(words));
            } else if (words.contains("身高")) {
                map.put("toHeight", getWord(words));
            } else if (words.contains("体重")) {
                map.put("toWeight", getWord(words));
            } else if (words.contains("房")) {
                map.put("toHouse", getWord(words));
            } else if (words.contains("学历")) {
                map.put("toEdu", getWord(words));
            } else if (words.contains("出生地")) {
                map.put("toFromWhere", getWord(words));
            } else if (words.contains("独生")) {
                map.put("toFamily", getWord(words));
            } else if (words.contains("无法接受")) {
                String continuouStr = getWord(words);
                for (int j = i + 1; j < wordsResult.size(); j++) {
                    String trim = wordsResult.get(j).words.trim();
                    if (trim.contains("加分")) {
                        break;
                    }
                    continuouStr += trim;
                    i = j;
                }
                map.put("toShortcoming", continuouStr);
            } else if (words.contains("加分项")) {
                String continuouStr = getWord(words);
                for (int j = i + 1; j < wordsResult.size(); j++) {
                    String trim = wordsResult.get(j).words.trim();
                    if (trim.contains("性格")) {
                        break;
                    }
                    continuouStr += trim;
                    i = j;
                }
                map.put("toAdvantage", continuouStr);
            } else if (words.contains("性格")) {
                String continuouStr = getWord(words);
                for (int j = i + 1; j < wordsResult.size(); j++) {
                    String trim = wordsResult.get(j).words.trim();
                    continuouStr += trim;
                    i = j;
                }
                map.put("toMettle", continuouStr);
            }
        }

        return map;
    }

    private static String getWord(String key) {
        if (key.contains("【") || key.contains("】")) {
            return key.substring(key.indexOf("】") + 1);
        } else if (key.contains("]")) {
            return key.substring(key.indexOf("]") + 1);
        } else if (key.contains(")")) {
            return key.substring(key.indexOf(")") + 1);
        } else if (key.contains(":")) {
            return key.substring(key.indexOf(":") + 1);
        } else if (key.contains("：")) {
            return key.substring(key.indexOf("：") + 1);
        }
        return "";
    }

}
