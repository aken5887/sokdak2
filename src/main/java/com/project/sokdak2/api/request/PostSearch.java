package com.project.sokdak2.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostSearch {

  private static final int DEFAULT_SIZE = 10;
  private static final int DEFAULT_MAX_SIZE = 50;

  @Builder.Default
  private Integer page = 1;
  @Builder.Default
  private Integer size = DEFAULT_SIZE;
  private String kw_opt;
  private String kw;
  @Builder.Default
  private String dir = "desc";
  @Builder.Default
  private String dir_props = "id";
  private Integer pwd;

  public long getOffSet() {
    return (Math.max(1, page) - 1) * Math.min(this.size, DEFAULT_MAX_SIZE);
  }

  public Pageable makePageable(){
    Sort.Direction dir = "desc".equals(this.dir) ? Direction.DESC : Direction.ASC;
    return PageRequest.of(this.page-1, this.size, dir, dir_props);
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page<1 ? 1:page;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = (size < DEFAULT_SIZE) || (size > DEFAULT_MAX_SIZE) ? DEFAULT_SIZE:size;
  }

  public String getKw_opt() {
    return kw_opt;
  }

  public void setKw_opt(String kw_opt) {
    this.kw_opt = kw_opt;
  }

  public String getKw() {
    return kw;
  }

  public void setKw(String kw) {
    this.kw = kw;
  }

  public String getDir() {
    return dir;
  }

  public void setDir(String dir) {
    this.dir = dir;
  }

  public String getDir_props() {
    return dir_props;
  }

  public void setDir_props(String dir_props) {
    this.dir_props = dir_props;
  }

  public Integer getPwd() {
    return pwd;
  }

  public void setPwd(Integer pwd) {
    this.pwd = pwd;
  }
}
