package com.eteks.sweethome3d.io.model;

import java.util.Date;

public class CaseItemTO {
  private String id;
  private String caseName;
  private String caseId;
  private String name;
  private String author;
  private Date   createdDate;
  public String getId() {
    return this.id;
  }
  public void setId(String caseItemId) {
    this.id = caseItemId;
  }
  public String getCaseName() {
    return this.caseName;
  }
  public void setCaseName(String caseName) {
    this.caseName = caseName;
  }
  public String getCaseId() {
    return this.caseId;
  }
  public void setCaseId(String caseId) {
    this.caseId = caseId;
  }
  public String getName() {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getAuthor() {
    return this.author;
  }
  public void setAuthor(String author) {
    this.author = author;
  }
  public Date getCreatedDate() {
    return this.createdDate;
  }
  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

}
