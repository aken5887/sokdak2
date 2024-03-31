package com.project.sokdak2.api.response;

import com.project.sokdak2.api.domain.common.File;
import com.project.sokdak2.api.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor
public class PostResponse {

  private Long id;
  private String title;
  private String userId;
  private String content;
  private int count;
  private String createdTime;
  private String lastUpdatedTime;
  private List<File> files;
  private Integer locked;
  private Integer password;
  private String thumbnailImage;
  private String contentPreview;

  public PostResponse(Post post) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.userId = post.getUserId();
    this.content = post.getContent();
    this.count = post.getCount();
    this.createdTime
        = post.getCreatedTime() != null ?
          post.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) :
          "";
    this.lastUpdatedTime
        = post.getLastUpdatedTime() != null ?
          post.getLastUpdatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) :
          "";
    this.files = post.getFiles();
    this.locked = post.getLocked();
    this.password = post.getPassword();

    if(this.content != null && !this.content.isEmpty()){
      /** 1. 섬네일 이미지 추출 **/
      // 정규 표현식 패턴 생성
      Pattern pattern = Pattern.compile("!\\[image alt attribute\\]\\((.*?)\\.(jpg|png|jpeg)\\)");
      // 패턴과 일치하는 문자열 찾기
      Matcher matcher = pattern.matcher(this.content);
      // 이미지 주소 출력
      while (matcher.find()) {
        String imageUrl = matcher.group(1) + "." + matcher.group(2);
        this.thumbnailImage = imageUrl;
        break;
      }

      /** 2. 내용 미리보기 추출 */
      this.contentPreview = this.content;
      // 이미지 주소를 추출할 정규식 패턴
      String patternString = "(!\\[.*?\\]\\(.*?\\.(jpg|jpeg|png)\\))";
      pattern = Pattern.compile(patternString);
      matcher = pattern.matcher(this.contentPreview);
      // 매칭된 패턴을 제거하여 결과 문자열 생성
      this.contentPreview = matcher.replaceAll("");
      // html 태그 제거
      this.contentPreview = Jsoup.clean(this.contentPreview, Safelist.none());
      // markdown 문법 제거
      this.contentPreview = this.contentPreview.replaceAll("[*#-]","");
      if(contentPreview.length() > 60){
        this.contentPreview = this.contentPreview.substring(0, 60) + "...";
      }
    }
  }

  @Builder
  public PostResponse(Long id, String title, String userId, String content, Integer locked) {
    this.id = id;
    this.title = title==null?"":title.substring(0, Math.min(20, title.length())); // 제목은 20자 제한
    this.userId = userId;
    this.content = content;
    this.locked = locked;
  }

  public void updateThumbnail(String thumbnailImage){
    this.thumbnailImage = thumbnailImage;
  }

}
