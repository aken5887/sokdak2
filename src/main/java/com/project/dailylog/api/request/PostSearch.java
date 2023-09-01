package com.project.dailylog.api.request;

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
  private Integer direction = 0;
  @Builder.Default
  private String props = "id";

  public long getOffSet() {
    return (Math.max(1, page) - 1) * Math.min(this.size, DEFAULT_MAX_SIZE);
  }

  public Pageable makePageable(){
    Sort.Direction dir = this.direction==0 ? Direction.DESC : Direction.ASC;
    return PageRequest.of(this.page-1, this.size, dir, props);
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

  public String getKwOpt() {
    return kw_opt;
  }

  public void setKwOpt(String kw_opt) {
    this.kw_opt = kw_opt;
  }

  public String getKw() {
    return kw;
  }

  public void setKw(String kw) {
    this.kw = kw;
  }

  public Integer getDirection() {
    return direction;
  }

  public void setDirection(Integer direction) {
    this.direction = direction;
  }

  public String getProps() {
    return props;
  }

  public void setProps(String props) {
    this.props = props;
  }
}
