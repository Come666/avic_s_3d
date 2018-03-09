package com.eteks.sweethome3d.junit;

import java.util.List;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.eteks.sweethome3d.io.CaseManager;
import com.eteks.sweethome3d.io.model.CaseItemTO;
import com.eteks.sweethome3d.io.model.CaseTO;

public class CaseManagerTest extends TestCase{
  public void testn() {
    CaseManager caseManager=CaseManager.getInstance();
    String caseName = "test";
    String author = "root";
    String caseItemName = "test_caseItemName";
    String caseId=caseManager.addCase(caseName, author);
    caseManager.addCaseItem(caseId, caseItemName, caseName, author);
      System.out.println("xx");
  }
  
  
  public void test_aet() {
    CaseManager caseManager= CaseManager.getInstance();
    List<CaseTO>ls=caseManager.getAllCase();
    for(CaseTO c:ls){
      System.out.println(JSON.toJSONString(c));
    }
    List<CaseItemTO> lis = caseManager.getCaseItemByCaseId("a342f574-ead5-4575-a5c3-35949e16728e");
    for(CaseItemTO c:lis){
      System.out.println(JSON.toJSONString(c));
    }
  }
}
