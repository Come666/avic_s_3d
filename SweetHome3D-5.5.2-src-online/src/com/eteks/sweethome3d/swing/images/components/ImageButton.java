package com.eteks.sweethome3d.swing.images.components;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class ImageButton extends JButton implements MouseListener{
	public ImageButton(){
		this.setBackground(Color.black);
	}
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	//	this.setBackground(new Color(184,224,143));
		this.setBackground(Color.red);
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		this.setBackground(new Color(255,255,255));
	}
}
