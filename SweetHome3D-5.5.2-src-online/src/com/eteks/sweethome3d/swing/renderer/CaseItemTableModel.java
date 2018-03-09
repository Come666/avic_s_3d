package com.eteks.sweethome3d.swing.renderer;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.eteks.sweethome3d.io.CaseManager;
import com.eteks.sweethome3d.io.model.CaseItemTO;
import com.eteks.sweethome3d.io.model.CaseTO;

public class CaseItemTableModel extends AbstractTableModel {

	String headers[] = {"\u73b0\u573a\u540d\u79f0", "\u521b\u5efa\u4eba", "\u521b\u5efa\u65f6\u95f4", "\u64cd\u4f5c"};
	private String caseId;
	private List<CaseItemTO> data;

	public CaseItemTableModel(String caseId) {
		this.caseId = caseId;
		data = CaseManager.getInstance().getCaseItemByCaseId(this.caseId);
	}

	public int getColumnCount() {
		return headers.length;
	}

	public int getRowCount() {
		return data.size();
	}

	public String getColumnName(int col) {
		return headers[col];
	}

	public Object getValueAt(int row, int col) {
//		System.out.println(String.format("date:%s, row:%s, col:%s",
//				new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()),
//				row, col));
		CaseItemTO caseTO = this.data.get(row);
		if (col == 0) {
			return caseTO.getName();
		} else if (col == 1) {
			return caseTO.getAuthor();
		} else if (col == 2) {
			return new SimpleDateFormat("yyyy-MM-dd").format(caseTO
					.getCreatedDate());
		} else if (col == 3) {
			return "\u7f16\u8f91";
		} else {
			return "";
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 3) {
			return true;
		} else {
			return false;
		}
	}

	public void addCaseItem(String obj, String author) {
		String inputValue = obj;
		final CaseManager caseManager = CaseManager.getInstance();
		CaseTO caseTO=caseManager.getCaseById(this.caseId);
		caseManager.addCaseItem(this.caseId, inputValue, caseTO.getName(), author);
		data = caseManager.getCaseItemByCaseId(this.caseId);
		this.fireTableDataChanged();
	}
	
	public void editCaseItem(String obj, String author, int row) {
		String inputValue=obj;
		final CaseManager caseManager=CaseManager.getInstance();
		caseManager.editCaseItem(caseId, inputValue, author, row);
		data = caseManager.getCaseItemByCaseId(this.caseId);
		fireTableDataChanged();
	}
}
