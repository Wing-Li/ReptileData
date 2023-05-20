package com.reptile.jiaoji;

import java.util.List;

public class OCRModel {

    public List<WordsResultModel> words_result;
    public int words_result_num;
    public int error_code;
    public long log_id;
    public String image_url;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<WordsResultModel> getWords_result() {
        return words_result;
    }

    public void setWords_result(List<WordsResultModel> words_result) {
        this.words_result = words_result;
    }

    public int getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(int words_result_num) {
        this.words_result_num = words_result_num;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public static class WordsResultModel {
        public String words;

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }
    }
}
