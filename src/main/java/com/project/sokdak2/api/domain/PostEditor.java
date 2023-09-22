package com.project.sokdak2.api.domain;

import com.querydsl.core.util.StringUtils;
import lombok.Getter;

@Getter
public class PostEditor {

  private String title;
  private String content;
  private int topFixed;

  private PostEditor(String title, String content, int topFixed){
    this.title = title;
    this.content = content;
    this.topFixed = topFixed;
  }

  public static PostEditorBuilder builder(){
    return new PostEditorBuilder();
  }

  public static class PostEditorBuilder {
    private String title;
    private String content;
    private int topFixed;

    public PostEditorBuilder title(String title){
      if(!StringUtils.isNullOrEmpty(title)){
        this.title = title;
      }
      return this;
    }

    public PostEditorBuilder content(String content){
      if(!StringUtils.isNullOrEmpty(content)) {
        this.content = content;
      }
      return this;
    }

    public PostEditorBuilder topFixed(int topFixed){
      this.topFixed = topFixed;
      return this;
    }

    public PostEditor build(){
      return new PostEditor(this.title, this.content, this.topFixed);
    }
  }

}
