package com.project.dailylog.api.util;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ToString
@Slf4j
@Getter
public class PageMaker<T> {

  private Page<T> result;

  private Pageable prevPage;
  private Pageable nextPage;

  private int currentPageNo;
  private int totalPageNum;

  private Pageable currentPage;
  private List<Pageable> pageList;

  public PageMaker(Page<T> result){
    this.result = result;
    this.currentPage = result.getPageable();
    this.currentPageNo = currentPage.getPageNumber() + 1;
    this.totalPageNum = result.getTotalPages();
    this.pageList = new ArrayList<>();
    this.calculatePage();
  }

  private void calculatePage() {
    int tempEndPageNo = (int) (Math.ceil(this.currentPageNo / 5.0) * 5);
    int startPageNo = tempEndPageNo - 4;

    Pageable startPage = this.currentPage;

    // 필요한지 의문인 로직
    for(int i=startPageNo; i<this.currentPageNo; i++) {
      startPage = startPage.previousOrFirst();
    }

    this.prevPage = startPage.getPageNumber() <= 0 ? null : startPage.previousOrFirst();

    /** 실제 총 페이지 수가 리스트의 마지막 보다 더 작은 경우*/
    if(this.totalPageNum < tempEndPageNo){
      tempEndPageNo = this.totalPageNum;
      this.nextPage = null;
    }

    /** 페이징 리스트 생성*/
    for(int i=startPageNo; i<=tempEndPageNo; i++) {
      pageList.add(startPage);
      startPage = startPage.next();
    }

    this.nextPage = startPage.getPageNumber() < totalPageNum ? startPage :null;
  }
}
