package com.project.sokdak2.api.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class RankService {
    public static void main(String[] args) {
        try {
            // 멜론 차트 URL
            String melonChartUrl = "https://www.melon.com/chart/index.htm";

            // Jsoup을 사용하여 웹 페이지의 HTML을 가져옴
            Document doc = Jsoup.connect(melonChartUrl).get();

            // 멜론 차트에서 곡 정보를 가져옴
            Elements chartItems = doc.select(".lst50, .lst100"); // 곡 목록의 CSS 클래스에 따라 선택자를 설정

            for (Element item : chartItems) {
                String rank = item.selectFirst(".rank").text();
                String title = item.selectFirst(".ellipsis.rank01 a").text();
                String artist = item.selectFirst(".ellipsis.rank02 a").text();
                String album = item.selectFirst("a.image_typeAll img").attr("src");
                System.out.println(rank + ". " + title + " - " + artist+" - "+album);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
