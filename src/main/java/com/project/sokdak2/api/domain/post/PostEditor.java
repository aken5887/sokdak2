package com.project.sokdak2.api.domain.post;

import com.querydsl.core.util.StringUtils;
import lombok.Getter;

@Getter
public class PostEditor {

  private String title;
  private String content;
  private Integer locked;

  private PostEditor(String title, String content, Integer locked){
    this.title = title;
    this.content = content;
    this.locked = locked;
  }

  public static PostEditorBuilder builder(){
    return new PostEditorBuilder();
  }

  public static class PostEditorBuilder {
    private String title;
    private String content;
    private Integer locked;

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

    public PostEditorBuilder locked(Integer locked){
      if(locked != null) {
        this.locked = locked;
      }else{
        this.locked = 0;
      }
      return this;
    }

    public PostEditor build(){
      return new PostEditor(this.title, this.content, this.locked);
    }
  }

}
