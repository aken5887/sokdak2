package com.project.sokdak2.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sokdak2.api.request.NewsArticle;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
                    sbf.append("<h2>섹션[").append(article.getSection()).append("]</h2>");
                    sbf.append("<ul>");
                }
                sbf.append("<li>").append(article.getTitle());
                sbf.append("<h3><a href='").append(article.getLink()).append("' target='_blank' style='color:#1111df;'> [링크]").append("</a></h3>");
                sbf.append("</li>");
                sbf.append("<p>").append(article.getSummary()).append("</p>");
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
                String summary = "";

                String newsNo = link.substring(link.indexOf("/015/")+5, link.indexOf("?"));
                String urlLink = "https://tts.news.naver.com/article/015/"+newsNo+"/summary?callback=callback";
                String summaryHtml = getSummaryResponse(urlLink);
                if(StringUtils.hasText(summaryHtml)){
                    summary = getSummaryHtml(summaryHtml);
                }

                if(!StringUtils.hasText(title)){
                    title = article.select("a.article_lst--title_only").text();
                }
                NewsArticle newsArticle = NewsArticle.builder()
                        .section(section)
                        .title(title)
                        .summary(summary)
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

    private static String getSummaryResponse(String urlLink) {
        StringBuilder sb = new StringBuilder();
        try{
            URL obj = new URL(urlLink);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while((inputLine = in.readLine()) != null){
                sb.append(inputLine);
            }
            in.close();
        }catch(IOException io){
            log.debug(io.getMessage());
        }
        return sb.toString();
    }

    private static String getSummaryHtml(String summaryHtml) {
        String result = "";
        int startIndex = summaryHtml.indexOf("{");
        int endIndex = summaryHtml.lastIndexOf("}");
        String jsonString = summaryHtml.substring(startIndex, endIndex+1);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            result = jsonNode.get("summary").asText();
            result = result.replaceAll("<br>","").replaceAll("<br/>","");
        } catch (JsonProcessingException e) {
            log.debug(e.getMessage());
        }
        return result;
    }
}
