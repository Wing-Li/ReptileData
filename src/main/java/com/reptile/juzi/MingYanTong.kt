package com.reptile.juzi

import org.jsoup.Jsoup
import java.io.IOException

class MingYanTong {


    private val baseUrl = "https://www.mingyantong.com"

    fun fetchTags(): ArrayList<String> {
        val menuUrl = "$baseUrl/alltags"
        val list = ArrayList<String>()
        try {
            val con = Jsoup.connect(menuUrl)
            val document = con.get()
            val elements = document.select("div.contentin dd a")
            elements?.forEach {
                val href = it.attr("href")
                list.add(href)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return list
    }

    fun fetchTagMenu(tag: String): ArrayList<String> {
        val detailUrl = baseUrl + tag
        val list = ArrayList<String>()
        try {
            val document = Jsoup.connect(detailUrl).get()
            val elements = document.select("div.view-content div.views-field-tid a")
            elements?.forEach {
                val href = it.attr("href")
                list.add(href)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return list
    }

    fun fetchTagMenuDetal(menu:String) : ArrayList<String> {
        val detailUrl = baseUrl + menu
        val list = ArrayList<String>()
        try {
            val document = Jsoup.connect(detailUrl).get()
            val viewContent = document.select("div.view-content")
            val viewsRow = viewContent.select("div.views-row")
            viewsRow?.forEach {
                val content = it.select("div.views-field-phpcode-1").text()
                val author = it.select("div.xqjulistwafo").text()
                list.add(content)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return list
    }
}