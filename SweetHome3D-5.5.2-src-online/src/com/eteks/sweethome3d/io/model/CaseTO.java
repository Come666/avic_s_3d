package com.eteks.sweethome3d.io.model;

import java.util.Date;

public class CaseTO {
  private String  name;
  private String  id;
  private Integer caseItemCnt=0;
  private Date    createdDate;
  private String  author;
  public String getName() {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getId() {
    return this.id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public Integer getCaseItemCnt() {
    return this.caseItemCnt;
  }
  public void setCaseItemCnt(Integer caseItemCnt) {
    this.caseItemCnt = caseItemCnt;
  }
  public Date getCreatedDate() {
    return this.createdDate;
  }
  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }
  public String getAuthor() {
    return this.author;
  }
  public void setAuthor(String author) {
    this.author = author;
  }

}
