package com.project.dailylog.api.request;

import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class PostSearch {

  private static final int DEFAULT_SIZE = 10;
  private static final int DEFAULT_MAX_SIZE = 50;

  @Builder.Default
  private Integer page = 1;

  @Builder.Default
  private Integer size = DEFAULT_SIZE;

  private String type;
  private String keyword;

  public long getOffSet() {
    return (Math.max(1, page) - 1) * Math.min(this.size, DEFAULT_MAX_SIZE);
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page<0 ? 1:page;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = (size < DEFAULT_SIZE) || (size > DEFAULT_MAX_SIZE) ? DEFAULT_SIZE:size;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }
}
