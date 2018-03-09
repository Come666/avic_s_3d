package com.eteks.sweethome3d.swing.images.main;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.eteks.sweethome3d.swing.images.components.ViewerDialog;

/* 
 * ������ 
 */
public class MainFrame extends JFrame {
//	MyMenuBar menuBar;
//	MyStatusBar statusBar;// ״̬��
//	MyToolBar toolBar;// ������
//	ShowPicturePanel showPicturePanel;// ��ʾͼƬ�����
//
//	public MainFrame() {
//
//		menuBar = new MyMenuBar();
//		statusBar = new MyStatusBar();
//		toolBar = new MyToolBar();
//		showPicturePanel = new ShowPicturePanel(toolBar, menuBar);
//
//		/* ��ʼ����Ŀ¼�� */
//		FileTree fileTree = establishFiletree();
//		/* ��������Ŀ¼�� */
//		showPicturePanel.setFileTree(fileTree);
//
//		JScrollPane jsp = new JScrollPane(fileTree);
//		jsp.setPreferredSize(new Dimension(200, 590));
//		jsp.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
//
//		Container contentPane = this.getContentPane();
//		JSplitPane splitPane = new JSplitPane();
//		splitPane.setDividerSize(8);
//		contentPane.add(splitPane);
//		splitPane.setLeftComponent(jsp);
//		JScrollPane jsp1 = new JScrollPane(showPicturePanel);
//		jsp1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		splitPane.setRightComponent(jsp1);
//		this.addListenerToMenuBar();// ��MenuBar���������Ӽ���
//		this.setJMenuBar(menuBar);
//		this.add(statusBar, BorderLayout.SOUTH);
//		this.add(toolBar, BorderLayout.NORTH);
//
//		this.setTitle("PictureViewer");
//		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//		this.setSize(900, 700);
//		this.setLocationRelativeTo(null);
//		this.setVisible(true);
//
//	}
//
//	private FileTree establishFiletree() {
//		FileTree fileTree = new FileTree(showPicturePanel, statusBar);
//		FileTreeModel model = new FileTreeModel(new DefaultMutableTreeNode(
//				new FileNode("root", null, null, true, new ArrayList<File>())));
//		FileTreeRenderer renderer = new FileTreeRenderer();
//		fileTree.setModel(model);
//		fileTree.setCellRenderer(renderer);
//		return fileTree;
//	}
//
//	public void addListenerToMenuBar() {
//		// �˳�
//		this.menuBar.getFile_exit().addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				System.exit(0);
//			}
//		});
//		// ����
//		this.menuBar.getEdit_copy().addActionListener(new ActionListener() {
//
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//			}
//		});
//
//		// �ƶ�
//		this.menuBar.getEdit_move().addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//			}
//		});
//
//		// ɾ��
//		this.menuBar.getEdit_delete().addActionListener(new ActionListener() {
//
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//			}
//		});
//
//		// ������
//		this.menuBar.getEdit_rename().addActionListener(new ActionListener() {
//
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//			}
//		});
//	}

	public static void main(String[] args) {
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// new MainFrame();
		ArrayList<File> pictures = new ArrayList<File>();
		File picture = new File(
				"/Users/gouyunfei/Downloads/t01090204baa0a3d282.jpg");
		pictures.add(picture);
		new ViewerDialog(pictures, picture);

	}
}
