package com.eteks.sweethome3d.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.eteks.sweethome3d.io.model.CaseItemTO;
import com.eteks.sweethome3d.io.model.CaseTO;
import com.eteks.sweethome3d.tools.OperatingSystem;

public class CaseManager {
  // private String filePath =
  // "/Users/gouyunfei/Documents/workspace/sweet_home/SweetHome3D-5.5.2-src";
  private static String      filePath         = null;
  static {
    try {
      filePath = OperatingSystem.getDefaultApplicationFolder().getAbsolutePath();
      
      System.out.println("CaseManager file path:"+filePath);
    } catch (Exception e) {

    }
  }
  private String             filenameCase     = filePath + "/case.json";
  private String             filenameCaseItem = filePath + "/case-item.json";
  private List<CaseTO>       cases;
  private List<CaseItemTO>   caseItems;
  private static CaseManager instance         = new CaseManager();

  public static CaseManager getInstance() {
    return instance;
  }

  private CaseManager() {
    reloadCaseItem();
    reloadCase();
  }

  public String getFilePath() {
    return filePath;
  }

  public synchronized List<CaseTO> getAllCase() {
    if (cases == null) {
      reloadCase();
    }
    return this.cases;
  }

  public synchronized void reloadCase() {
    try {
      File file = new File(filenameCase);
      if (!file.exists()) {
        file.createNewFile();
      }
      BufferedReader fis = new BufferedReader(new FileReader(file));
      StringBuffer sb = new StringBuffer();
      for (String line = fis.readLine(); line != null; line = fis.readLine()) {
        sb.append(line);
      }
      fis.close();
      List<CaseTO> a = JSON.parseArray(sb.toString(), CaseTO.class);
      this.cases = a;
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    if (this.cases == null) {
      this.cases = new ArrayList<CaseTO>();
    }
  }

  public synchronized void reloadCaseItem() {
    try {
      File file = new File(filenameCaseItem);
      if (!file.exists()) {
        file.createNewFile();
      }
      BufferedReader fis = new BufferedReader(new FileReader(new File(filenameCaseItem)));
      StringBuffer sb = new StringBuffer();
      for (String line = fis.readLine(); line != null; line = fis.readLine()) {
        sb.append(line);
      }
      fis.close();
      List<CaseItemTO> a = JSON.parseArray(sb.toString(), CaseItemTO.class);
      this.caseItems = a;
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    if (this.caseItems == null) {
      this.caseItems = new ArrayList<CaseItemTO>();
    }
  }

  public synchronized List<CaseItemTO> getCaseItemByCaseId(String caseId) {
    List<CaseItemTO> ls = new ArrayList<CaseItemTO>();
    for (CaseItemTO to : this.caseItems) {
      if (to.getCaseId().equalsIgnoreCase(caseId)) {
        ls.add(to);
      }
    }
    return ls;
  }

  public synchronized String addCase(String name, String author) {
    CaseTO caseTO = new CaseTO();
    caseTO.setId(UUID.randomUUID().toString());
    caseTO.setAuthor(author);
    caseTO.setCreatedDate(new Date());
    caseTO.setCaseItemCnt(0);
    caseTO.setName(name);
    this.cases.add(caseTO);

    saveCase();
    reloadCase();
    return caseTO.getId();
  }

  public synchronized String editCase(String name, String author, int index) {
    CaseTO caseTO = cases.get(index);
    caseTO.setName(name);
    caseTO.setAuthor(author);

    saveCase();
    reloadCase();
    return caseTO.getId();
  }

  private synchronized void saveCase() {
    try {
      BufferedWriter fis = new BufferedWriter(new FileWriter(new File(filenameCase)));
      String sb = JSON.toJSONString(this.cases);
      fis.write(sb);
      fis.close();
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public synchronized void addCaseItem(String caseId, String caseItemName, String caseName, String author) {
    CaseItemTO caseItemTO = new CaseItemTO();
    caseItemTO.setName(caseItemName);
    caseItemTO.setAuthor(author);
    caseItemTO.setCaseName(caseName);
    caseItemTO.setCaseId(caseId);
    caseItemTO.setCreatedDate(new Date());
    caseItemTO.setId(UUID.randomUUID().toString());

    this.caseItems.add(caseItemTO);
    CaseTO caseTO = getCaseById(caseId);
    caseTO.setCaseItemCnt(caseTO.getCaseItemCnt() + 1);
    saveCaseItem();
    saveCase();

    reloadCase();
    reloadCaseItem();
  }

  public synchronized void editCaseItem(String caseId, String caseItemName, String author, int index) {
    CaseItemTO itemTO = this.getCaseItemByCaseId(caseId).get(index);
    itemTO.setAuthor(author);
    itemTO.setName(caseItemName);
    saveCaseItem();
    reloadCase();
    reloadCaseItem();
  }

  private void saveCaseItem() {
    try {
      BufferedWriter fis = new BufferedWriter(new FileWriter(new File(filenameCaseItem)));
      String sb = JSON.toJSONString(this.caseItems);
      fis.write(sb);
      fis.close();
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public synchronized void deleteCaseItemById(String caseItemId) {
    List<CaseItemTO> ls = new ArrayList<CaseItemTO>();
    for (CaseItemTO to : this.caseItems) {
      if (!to.getId().equalsIgnoreCase(caseItemId)) {
        ls.add(to);
      }
    }
    this.caseItems = ls;
    saveCaseItem();
  }

  public synchronized void deleteCaseById(String caseId) {
    List<CaseTO> ls = new ArrayList<CaseTO>();
    for (CaseTO to : this.cases) {
      if (!to.getId().equalsIgnoreCase(caseId)) {
        ls.add(to);
      }
    }
    this.cases = ls;
    saveCase();
  }

  public synchronized CaseTO getCaseById(String caseId) {
    for (CaseTO to : this.cases) {
      if (to.getId().equalsIgnoreCase(caseId)) {
        return to;
      }
    }
    return null;
  }
}
