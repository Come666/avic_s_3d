package com.eteks.sweethome3d.swing.renderer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

public class MyButtonEditor extends AbstractCellEditor implements
		TableCellEditor {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6546334664166791132L;

	private JPanel panel;

	private JButton button;
	private String num;
	private JTable table;
	private int row;

	public MyButtonEditor() {

		initButton();

		initPanel();

		panel.add(this.button, BorderLayout.CENTER);
	}

	private void initButton() {
		button = new JButton();

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(row + "  actionEvent e:" + e);
				String inputValue = JOptionPane.showInputDialog("input modify case name:");
				TableModel tableModel = MyButtonEditor.this.table.getModel();
				if (tableModel instanceof CaseTableModel) {
					CaseTableModel tm = (CaseTableModel) tableModel;
					tm.editCase(inputValue, "root", row);
				}else if(tableModel instanceof CaseItemTableModel){
					CaseItemTableModel tm = (CaseItemTableModel) tableModel;
					tm.editCaseItem(inputValue, "root", row);
				}
			}
		});

	}

	private void initPanel() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.num = String.valueOf(value);
		System.out.println("value:" + value);
		System.out.println("isSelected:" + isSelected);
		System.out.println("row:" + row);
		System.out.println("column:" + column);
		this.row = row;
		this.table = table;
		button.setText(value == null ? "" : String.valueOf(value));
		return panel;
	}

	public Object getCellEditorValue() {
		return num;
	}

}