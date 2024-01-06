package com.project.sokdak2.api.service;

import com.project.sokdak2.api.request.NewsArticle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {

    private static String generateHtmlFile(List<NewsArticle> newsList) throws IOException {
        StringBuilder sbf = new StringBuilder();
        if(newsList.isEmpty()){
            sbf.append("<h3>발행된 종이신문이 없습니다.</h3>");
        }else{
            int count = 0;
            for (NewsArticle article : newsList) {
                if(StringUtils.hasText(article.getSection())){
                    if(count>0) sbf.append("</ul>");
                    sbf.append("<h3>섹션[").append(article.getSection()).append("]</h3>");
                    sbf.append("<ul>");
                }
                sbf.append("<li>");
                sbf.append("<a href='").append(article.getLink()).append("' target='_blank'>").append(article.getTitle()).append("</a>");
                sbf.append("</li>");
                count++;
            }
            sbf.append("</ul>");
        }
        return sbf.toString();
    }

    private static List<NewsArticle> extractNewsArticles(Document document) {
        List<NewsArticle> newsList = new ArrayList<>();
        Elements newsElements = document.select("div.newspaper_wrp");

        for (Element element : newsElements) {
            String section = element.select("span.page_notation").text();
            Elements articleList = element.select("ul.newspaper_article_lst li");
            int count = 0;
            for(Element article:articleList){
                if(count != 0) section = "";
                String title = article.select("div.newspaper_txt_box").text();
                String link = article.select("a").attr("href");
                if(!StringUtils.hasText(title)){
                    title = article.select("a.article_lst--title_only").text();
                }
                NewsArticle newsArticle = NewsArticle.builder()
                        .section(section)
                        .title(title)
                        .link(link)
                        .build();
                newsList.add(newsArticle);
                count++;
            }
        }
        return newsList;
    }

    public String getNewsArticleHtml(){
        StringBuilder sbf = new StringBuilder();
        try {
            String url = "https://media.naver.com/press/015/newspaper";
            // 네이버 뉴스 검색 페이지에 접속
            Document document = Jsoup.connect(url).get();
            // 뉴스 기사 정보를 추출하여 리스트에 저장
            List<NewsArticle> newsList = extractNewsArticles(document);
            // HTML 파일 생성
           return generateHtmlFile(newsList);
        } catch (IOException e) {
            sbf.append("<h3>뉴스 스크랩 중 오류가 발생하였습니다.</h3>");
            sbf.append("<p>").append("사유:").append(e.getMessage()).append("</p>");
        }
        return sbf.toString();
    }
}
