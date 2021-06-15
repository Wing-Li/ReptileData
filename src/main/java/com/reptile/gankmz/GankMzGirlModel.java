package com.reptile.gankmz;

import com.google.gson.Gson;

import java.util.List;

public class GankMzGirlModel {

    public List<DataModel> data;
    public int page;
    public int page_count;
    public int status;
    public int total_counts;

    public static GankMzGirlModel objectFromData(String str) {

        return new Gson().fromJson(str, GankMzGirlModel.class);
    }

    public static class DataModel {
        public String _id;
        public String author;
        public String category;
        public String createdAt;
        public String desc;
        public List<String> images;
        public int likeCounts;
        public String publishedAt;
        public int stars;
        public String title;
        public String type;
        public String url;
        public int views;

        public static DataModel objectFromData(String str) {

            return new Gson().fromJson(str, DataModel.class);
        }
    }
}
