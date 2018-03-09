package com.eteks.sweethome3d.swing.images.components;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class MyPopupMenu extends JPopupMenu{
	JMenuItem popupMenu_view,popupMenu_copy,popupMenu_move,popupMenu_delete,
	popupMenu_reName,popupMenu_property;
	public MyPopupMenu(){
//		popupMenu_view = new JMenuItem("Ԥ��");
//		popupMenu_copy = new JMenuItem("���Ƶ�");
//		popupMenu_move = new JMenuItem("�ƶ���");
//		popupMenu_delete = new JMenuItem("ɾ��");
//		popupMenu_reName = new JMenuItem("������");
//		popupMenu_property = new JMenuItem("����");
		
		
		popupMenu_view = new JMenuItem("view");
		popupMenu_copy = new JMenuItem("copy");
		popupMenu_move = new JMenuItem("move");
		popupMenu_delete = new JMenuItem("delete");
		popupMenu_reName = new JMenuItem("rename");
		popupMenu_property = new JMenuItem("property");
		
		this.add(popupMenu_view);
		this.add(popupMenu_copy);
		this.add(popupMenu_move);
		this.add(popupMenu_delete);
		this.add(popupMenu_reName);
		this.add(popupMenu_property);
	}
	public JMenuItem getPopupMenu_view() {
		return popupMenu_view;
	}
	public JMenuItem getPopupMenu_copy() {
		return popupMenu_copy;
	}
	public JMenuItem getPopupMenu_move() {
		return popupMenu_move;
	}
	public JMenuItem getPopupMenu_delete() {
		return popupMenu_delete;
	}
	public JMenuItem getPopupMenu_reName() {
		return popupMenu_reName;
	}
	public JMenuItem getPopupMenu_property() {
		return popupMenu_property;
	}
	
}
