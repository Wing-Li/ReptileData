package com.reptile.jiaoji;

import com.config.MyConfig;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

/**
 * 七牛云，文件上传删除
 */
public class FileUpload {


    String accessKey = MyConfig.QINIU_accessKey;
    String secretKey = MyConfig.QINIU_secretKey;
    String bucket = MyConfig.QINIU_bucket;

    public String upload(String directory, String localFilePath) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huadongZheJiang2());
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);

        try {
            //...生成上传凭证，然后准备上传
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            //默认不指定key的情况下，以文件内容的hash值作为文件名
            String key = directory + "/" + "user_" + System.currentTimeMillis() + "." + getPathSuffixName(localFilePath);

            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

            return MyConfig.QINIU_BaseUrl + putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
        return null;
    }

    public void deleteFile(String file) {
        String[] keyList = new String[]{file};
        deleteFile(keyList);
    }

    /**
     * 批量删除文件
     * //单次批量请求的文件数量不得超过1000
     * String[] keyList = new String[]{
     * "qiniu.jpg",
     * "qiniu.mp4",
     * "qiniu.png",
     * };
     *
     * @param keyList
     */
    public void deleteFile(String[] keyList) {
        try {
            //构造一个带指定 Region 对象的配置类
            Configuration cfg = new Configuration(Region.huadongZheJiang2());

            Auth auth = Auth.create(accessKey, secretKey);
            BucketManager bucketManager = new BucketManager(auth, cfg);

            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucket, keyList);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);

            for (int i = 0; i < keyList.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keyList[i];
                System.out.print(key + "\t");
                if (status.code == 200) {
                    System.out.println("delete success");
                } else {
                    System.out.println(status.data.error);
                }
            }
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }

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
