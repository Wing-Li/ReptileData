package com.reptile.jiaoji;

import cn.leancloud.LCException;
import cn.leancloud.LCFile;
import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.core.LeanCloud;
import cn.leancloud.types.LCNull;
import com.config.MyConfig;
import io.reactivex.functions.Consumer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LCDB {

    public void init() {
        LeanCloud.initialize(MyConfig.LC_JIAOJI_appid, MyConfig.LC_JIAOJI_appkey, "https://api.jj.lylyl.cn");
    }

    public void saveUserInfo(Map<String, Object> map) {
        if (map == null) return;

        LCObject lcObject = new LCObject("UserInfo");

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            lcObject.put(entry.getKey(), entry.getValue());
        }

        lcObject.save();
    }

    public void saveUserInfo(ArrayList<HashMap<String, Object>> mapArrayList) {
        ArrayList<LCObject> lcObjectArrayList = new ArrayList<>();

        for (HashMap<String, Object> map : mapArrayList) {
            LCObject lcObject = new LCObject("UserInfo");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                lcObject.put(entry.getKey(), entry.getValue());
            }
            lcObjectArrayList.add(lcObject);
        }

        try {
            LCObject.saveAll(lcObjectArrayList);
        } catch (LCException e) {
            e.printStackTrace();
        }
    }

    public List<LCObject> getUserInfoAll() {
        LCQuery<LCObject> query = new LCQuery<>("UserInfo");
//        query.whereEqualTo("priority", 2);
//        query.limit(10);
//        query.skip(20);
        query.orderByAscending("createdAt");

        return query.find();
    }

    public void saveFile(String filePath) {
        try {
            String name = "user_" + System.currentTimeMillis() + "." + getPathSuffixName(filePath);
            LCFile file = LCFile.withAbsoluteLocalPath(name, filePath);
            file.saveInBackground().subscribe(
                    lcFile -> {
                        String lcFileUrl = lcFile.getUrl();
                        System.out.println("文件保存完成。URL：" + lcFileUrl);
                    }, throwable -> {
                        // 保存失败，可能是文件无法被读取，或者上传过程中出现问题
                    }
            );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(String objId) {
        LCObject file = LCObject.createWithoutData("_File", objId);
        file.deleteInBackground().subscribe(new Consumer<LCNull>() {
            @Override
            public void accept(LCNull lcNull) throws Exception {

            }
        });
    }

    /// 获取文件的后缀名
    public String getPathSuffixName(String path) {
        return path.substring(path.lastIndexOf(".") + 1);
    }

    /// 获取文件名
    public String getPathFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

}
