package com.eteks.sweethome3d.swing.images.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileSystemView;

import com.eteks.sweethome3d.swing.images.components.TipDialog;

public class OperateUtils {
	public static void showTips(final String tipText,final  int tipExitsTime) {
		// ������ʾ
		Thread thread = new Thread(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				TipDialog dialog = new TipDialog(tipText);
				try {
					Thread.sleep(1000 * tipExitsTime);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dialog.setVisible(false);
				dialog.dispose();
				dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			}
		});
		thread.start();
	}

	public static JFileChooser getDefaultFileChooser(String dialogTitle) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(dialogTitle);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setCurrentDirectory(FileSystemView.getFileSystemView()
				.getHomeDirectory());// ���ô򿪵�Ĭ��Ŀ¼Ϊ����
		fileChooser.showSaveDialog(null);// ��ʾѡ�񴰿�
		fileChooser.setVisible(true);
		return fileChooser;
	}

	public static <T> Object deepClone(T object) throws Exception {//���¡
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(object);
		// �����������
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bis);
		return ois.readObject();
	}
}
