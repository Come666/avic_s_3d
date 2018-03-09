package com.eteks.sweethome3d.swing.renderer;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.eteks.sweethome3d.io.CaseManager;
import com.eteks.sweethome3d.io.model.CaseTO;

public class CaseTableModel extends AbstractTableModel {

	String headers[] = { "\u6848\u4ef6\u540d\u79f0", "\u73b0\u573a\u6570", "\u521b\u5efa\u4eba", "\u521b\u5efa\u65f6\u95f4", "\u64cd\u4f5c" };

	public int getColumnCount() {
		return headers.length;
	}

	public int getRowCount() {
		List<CaseTO> data = CaseManager.getInstance().getAllCase();
//		System.out.println("data.size" + data.size());
		return data.size();
	}

	public String getColumnName(int col) {
		return headers[col];
	}

	public Object getValueAt(int row, int col) {
		List<CaseTO> data = CaseManager.getInstance().getAllCase();
//		System.out.println(String.format("date:%s, row:%s, col:%s",
//				new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()),
//				row, col));
		CaseTO caseTO = data.get(row);
		if (col == 0) {
			return caseTO.getName();
		} else if (col == 1) {
			return caseTO.getCaseItemCnt();
		} else if (col == 2) {
			return caseTO.getAuthor();
		} else if (col == 3) {
			return new SimpleDateFormat("yyyy-MM-dd").format(caseTO
					.getCreatedDate());
		} else if (col == 4) {
			return "\u7f16\u8f91";
		} else {
			return "";
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 4) {
			return true;
		} else {
			return false;
		}
	}

	public void addCase(String obj, String author) {
		String inputValue=obj;
		final CaseManager caseManager=CaseManager.getInstance();
		caseManager.addCase(inputValue, author);
		fireTableDataChanged();
	}
	
	public void editCase(String obj, String author, int row) {
		String inputValue=obj;
		final CaseManager caseManager=CaseManager.getInstance();
		caseManager.editCase(inputValue, author, row);
		fireTableDataChanged();
	}
}
