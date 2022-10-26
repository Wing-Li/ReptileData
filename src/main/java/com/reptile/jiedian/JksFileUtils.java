package com.reptile.jiedian;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class JksFileUtils {

    private static String jksPath = "/Users/lyl/developer/v/";
    private static String jksPwd = "123456";

    private static boolean isLogPrint = false;

    public static void main(String[] args) {
        // 获取10个
        int num = 10;
        // 开始的数字
        int start = 41;

        for (int i = start; i < start + num; i++) {
            String name = "job" + i;
            String path = jksPath + name + ".jks";

            buildJksFile(path);

            System.out.println(name);
            System.out.println("https://res.lylyl.cn/jiedian/jks/" + name + ".jks");
            String s = getJksKey(path);
            System.out.println(s);
            System.out.println("");
        }
    }

    /**
     * 执行命令
     *
     * @param command 命令，中间不能有空格
     * @return 输出的字符串
     */
    private static String executeCommand(String[] command, boolean isLog) {
        if (isLog) {
            System.out.println("executeCommand start!");
            System.out.println(Arrays.toString(command));
        }
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            Process p = pb.start();
            int waitFor = p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            if (isLog) {
                System.out.println("executeCommand end!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }

    /**
     * 生成密钥并保存到jks文件
     * keytool -genkey -alias key0 -keyalg RSA -keysize 2048 -sigalg SHA256withRSA -dname
     * 'CN=(job), OU=(aa), O=(aa), L=(aa), ST=(aa), C=(中国)' -validity 36500 -keypass 123465 -keystore job40.jks -storepass 123465 -v
     *
     * @param filePath 文件路径，包含文件名和后缀
     */
    public static void buildJksFile(String filePath) {
        String[] command = new String[]{

                "keytool",
                "-genkey", //表示生成密钥
                "-alias", //要处理的条目的别名（jks文件别名）
                "key0",
                "-keyalg", //密钥算法名称(如 RSA DSA（默认是DSA）)
                "RSA",
                "-keysize",//密钥位大小(长度)
                "2048",
                "-sigalg", //签名算法名称
                "SHA256withRSA",
                "-dname", // 唯一判别名,CN=(名字与姓氏), OU=(组织单位名称), O=(组织名称), L=(城市或区域名称),
                // ST=(州或省份名称), C=(单位的两字母国家代码)"
                "CN=(job), OU=(rmb), O=(aa), L=(cn), ST=(bj), C=(cn)",
                "-validity", // 有效天数
                "36500",
                "-keypass", // 密钥口令(私钥的密码)
                jksPwd,
                "-keystore", //密钥库名称(jks文件路径)
                filePath,
                "-storepass", // 密钥库口令(jks文件的密码)
                jksPwd,
                "-v" // 详细输出（秘钥库中证书的详细信息）
        };
        String s = executeCommand(command, isLogPrint);
        // System.out.println(s);
    }

    /**
     * 生成 MD5, SHA1
     * keytool -list -v -keystore ./job40.jks -storepass 123465
     *
     * @param filePath 文件路径，包含文件名和后缀
     */
    public static String getJksKey(String filePath) {
        String md5 = "", sha1 = "";
        String[] command = new String[]{"keytool", "-list", "-v", "-keystore", filePath, "-storepass", jksPwd};
        String content = executeCommand(command, isLogPrint);

//        // jks 的信息
//        System.out.println(content);

        String[] split = content.split("\n");
        for (String s : split) {
            if (s.contains("MD5:")) {
                md5 = s.trim();
            } else if (s.contains("SHA1:")) {
                sha1 = s.trim();
            }
        }
        md5 = md5.toLowerCase().replace(":", "").replace("md5", "MD5:");
        return md5 + "\n" + sha1;
    }

}
