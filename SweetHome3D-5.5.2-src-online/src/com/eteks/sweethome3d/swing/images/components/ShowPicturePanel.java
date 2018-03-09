package com.eteks.sweethome3d.swing.images.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.eteks.sweethome3d.swing.images.filetree.FileNode;
import com.eteks.sweethome3d.swing.images.filetree.FileTree;
import com.eteks.sweethome3d.swing.images.utils.FileUtils;
import com.eteks.sweethome3d.swing.images.utils.OperateUtils;

public class ShowPicturePanel extends JPanel {
	ArrayList<SingleImagePanel> singleImagePanels = new ArrayList<SingleImagePanel>();// ������Panel
	MyToolBar toolBar;// ������
	MyMenuBar menuBar;// �˵���
	boolean isShowCheckBox = false;// �Ƿ���ʾ��ѡ��
	ArrayList<File> pictures;
	boolean isSelectAll = false;
	FileTree fileTree;
	DefaultMutableTreeNode currentTreeNode;

	public ShowPicturePanel(MyToolBar toolBar, MyMenuBar menuBar) {
		this.toolBar = toolBar;
		this.menuBar = menuBar;
		this.fileTree = fileTree;
		this.addListenerToComponents();
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		this.setBackground(Color.white);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
	}

	public void doShowPictures(final ArrayList<File> pictures) {
		this.pictures = pictures;
		this.removeAll();// ɾ�����ԭ��ͼƬ
		if (pictures.size() == 0) {
			this.updateUI();
			return;
		}
		int height = 125 * (pictures.size() / 5);
		if (pictures.size() % 5 != 0){
			height = height + 200;
		}
		this.setPreferredSize(new Dimension(650, height));
		singleImagePanels.clear();
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
		for (final File picture : pictures) {
			// ���ö��߳�(�̳߳�)��ÿ��ͼƬ����һ���̣߳��ӿ���ʾ�ٶ�(���ܰ�˳����ʾ)
			fixedThreadPool.execute((new Runnable() {
				public void run() {
					// TODO Auto-generated method stub
					try {
						SingleImagePanel singleImagePanel = new SingleImagePanel(
								picture, pictures, isShowCheckBox);
						if(isShowCheckBox){
							if (isSelectAll) {
								singleImagePanel.getCheckBox().setSelected(true);
								singleImagePanel.isSelected = true;
							}
						}
						add(singleImagePanel);
						singleImagePanels.add(singleImagePanel);
						updateUI();
					} catch (Exception e) {
					}
				}
			}));
			
		}
		this.updateUI();
	}
	
	private void addListenerToComponents() {
		// TODO Auto-generated method stub
		this.menuBar.getSetting_checkBox().addActionListener(
				new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						try {
							if (!isShowCheckBox) {
								isShowCheckBox = true;
								doShowPictures(pictures);
								updateUI();
							} else {
								isShowCheckBox = false;
								doShowPictures(pictures);
								updateUI();
							}
						} catch (Exception e1) {
							// TODO Auto-generated catch block
						}
					}
				});

		this.toolBar.getCopy().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {// �и�ѡ������
					// �ж��Ƿ����ļ���ѡ��
					boolean isFileSelected = false;
					for (SingleImagePanel panel : singleImagePanels) {
						if (panel.isSelected) {
							isFileSelected = true;
							break;
						}
					}
					if(!isFileSelected){
						OperateUtils.showTips("δѡ���ļ���", 2);
						return;
					}
					// �����Ի���ѡ�񱣴���ļ���
					JFileChooser fileChooser = OperateUtils
							.getDefaultFileChooser("���Ƶ�");
					String destination;
					try{
						destination = fileChooser.getSelectedFile().getAbsolutePath();
					}catch(Exception e1){
						return;
					}
					try {
						ArrayList<File> movedList = new ArrayList<File>();
						for (int i = 0; i < singleImagePanels.size(); i++) {// ����������壬���Ƿ�ѡ�У�ѡ������
							if (singleImagePanels.get(i).isSelected) {
								FileUtils.copyFile(destination +  "\\" + singleImagePanels.get(i).picture.getName(),
										singleImagePanels.get(i).picture.getAbsolutePath());
								movedList.add(new File(destination + "\\" + singleImagePanels.get(i).picture.getName()));
							} 
						}
						try {
							//����Ŀ¼���ڵ�
							synchronizeFileTree_Copy(movedList,destination);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							OperateUtils.showTips("���Ƴ���", 2);
							return;
						}
						OperateUtils.showTips("���Ƴɹ���", 2);
					} catch (Exception e2) {
						e2.printStackTrace();
						OperateUtils.showTips("����ʧ�ܣ�", 2);
					}
				}
			}
		});

		this.toolBar.getMove().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {
					// �ж��Ƿ����ļ���ѡ��
					boolean isFileSelected = false;
					for (SingleImagePanel panel : singleImagePanels) {
						if (panel.isSelected) {
							isFileSelected = true;
							break;
						}
					}
					if(!isFileSelected){
						OperateUtils.showTips("δѡ���ļ���", 2);
						return;
					}
					// �����Ի���ѡ�񱣴���ļ���
					JFileChooser fileChooser = OperateUtils
							.getDefaultFileChooser("�ƶ���");
					String destination;
					try{
						destination = fileChooser.getSelectedFile().getAbsolutePath();
					}catch(Exception e1){
						return;
					}
					try {
						ArrayList<File> movedList = new ArrayList<File> ();
						for (SingleImagePanel panel : singleImagePanels) {// ����������壬���Ƿ�ѡ�У�ѡ�����ƶ�
							if (panel != null) {
								if (panel.isSelected) {
									//ɾ��Ŀ¼���Ľڵ�
									synchronizeFileTree_Move(panel);
									//ɾ���ļ�
									FileUtils.moveFile(destination + "\\"+ panel.picture.getName(),
											panel.picture.getAbsolutePath());
									pictures.remove(panel.picture);
									movedList.add(new File(destination + "\\"+ panel.picture.getName()));
								}
							}
						}
						try{
							//����Ŀ¼���ڵ�
							synchronizeFileTree_Copy(movedList, destination);
						}catch(Exception e1){
							OperateUtils.showTips("�ƶ�ʧ�ܣ�", 2);
							return;
						}
						doShowPictures(pictures);
						// ������ʾ
						OperateUtils.showTips("�ƶ��ɹ���", 2);
					} catch (ConcurrentModificationException e3) {
						OperateUtils.showTips("�ƶ��ɹ���", 2);
					} catch (Exception e2) {
						// ������ʾ
						OperateUtils.showTips("�ƶ�ʧ�ܣ�", 2);
					}
				}
			}

		});

		this.toolBar.getDelete().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {
					try {
						boolean isSelectFile = false;
						for (SingleImagePanel panel : singleImagePanels) {// ����������壬���Ƿ�ѡ�У�ѡ����ɾ��
							if (panel.isSelected) {
								//ɾ��Ŀ¼���ж�Ӧ�ڵ�
								synchronizeFileTree_Delete(panel);
								//ɾ���ļ�
								pictures.remove(panel.picture);
								FileUtils.deleteFile(panel.picture
										.getAbsolutePath());
								isSelectFile = true;
							}
						}
						//û��ѡ���ļ����˳�
						if (!isSelectFile) {
							OperateUtils.showTips("δѡ���ļ���", 2);
							return;
						}
						//������ʾͼƬ
						doShowPictures(pictures);
						OperateUtils.showTips("ɾ���ɹ���", 2);
					} catch (ConcurrentModificationException e3) {
						OperateUtils.showTips("ɾ���ɹ���", 2);
					} catch (Exception e2) {
						OperateUtils.showTips("ɾ��ʧ�ܣ�", 2);
					}
				}
			}
		});

		this.toolBar.getRename().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {
					for (SingleImagePanel panel : singleImagePanels) {
						if (panel.isSelected) {
							break;
						}
						OperateUtils.showTips("δѡ���ļ���", 2);
						return;
					}
					MyDialog myDialog = new MyDialog();
				}
			}
		});

		this.toolBar.getSelectAll().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {
					if (!isSelectAll) {
						isSelectAll = true;
						toolBar.getSelectAll().setText("��ѡ");
						if(singleImagePanels.size() == 0){
							return;
						}
						for (SingleImagePanel panel : singleImagePanels) {
							panel.isSelected = false;
						}
					} else {
						isSelectAll = false;
						toolBar.getSelectAll().setText("ȫѡ");
						if(singleImagePanels.size() == 0){
							return;
						}
						for (SingleImagePanel panel : singleImagePanels) {
							panel.isSelected = true;
						}
					}
					doShowPictures(pictures);
				} else {
				}
			}
		});

		this.menuBar.getEdit_copy().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {// �и�ѡ������
					// �ж��Ƿ����ļ���ѡ��
					boolean isFileSelected = false;
					for (SingleImagePanel panel : singleImagePanels) {
						if (panel.isSelected) {
							isFileSelected = true;
							break;
						}
					}
					if(!isFileSelected){
						OperateUtils.showTips("δѡ���ļ���", 2);
						return;
					}
					// �����Ի���ѡ�񱣴���ļ���
					JFileChooser fileChooser = OperateUtils
							.getDefaultFileChooser("���Ƶ�");
					String destination;
					try{
						destination = fileChooser.getSelectedFile().getAbsolutePath();
					}catch(Exception e1){
						return;
					}
					try {
						ArrayList<File> movedList = new ArrayList<File> ();//���渴�ƺ��Ŀ���ļ����µı������ļ�
						for (int i = 0; i < singleImagePanels.size(); i++) {// ����������壬���Ƿ�ѡ�У�ѡ������
							if (singleImagePanels.get(i).isSelected) {
								FileUtils.copyFile(destination + "\\" + singleImagePanels.get(i).picture.getName(),
										singleImagePanels.get(i).picture.getAbsolutePath());
								movedList.add(new File(destination + singleImagePanels.get(i).getName()));
							}
						}
						try {
							//����Ŀ¼���ڵ�
							synchronizeFileTree_Copy(movedList,destination);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							OperateUtils.showTips("���Ƴ���", 2);
							return;
						}
						OperateUtils.showTips("���Ƴɹ���", 2);
					} catch (Exception e2) {
						OperateUtils.showTips("����ʧ�ܣ�", 2);
					}
				}
			}

		});

		this.menuBar.getEdit_move().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {
					// �ж��Ƿ����ļ���ѡ��
					boolean isFileSelected = false;
					for (SingleImagePanel panel : singleImagePanels) {
						if (panel.isSelected) {
							isFileSelected = true;
							break;
						}
					}
					if(!isFileSelected){
						OperateUtils.showTips("δѡ���ļ���", 2);
						return;
					}
					// �����Ի���ѡ�񱣴���ļ���
					JFileChooser fileChooser = OperateUtils
							.getDefaultFileChooser("�ƶ���");
					String destination;
					try{
						destination = fileChooser.getSelectedFile().getAbsolutePath();
					}catch(Exception e1){
						return;
					}
					try {
						ArrayList<File> movedList = new ArrayList<File> ();
						for (SingleImagePanel panel : singleImagePanels) {// ����������壬���Ƿ�ѡ�У�ѡ�����ƶ�
							if (panel != null) {
								if (panel.isSelected) {
									//ɾ��Ŀ¼���Ľڵ�
									synchronizeFileTree_Move(panel);
									//ɾ���ļ�
									FileUtils.moveFile(destination + "\\"+ panel.picture.getName(),
											panel.picture.getAbsolutePath());
									pictures.remove(panel.picture);
									movedList.add(new File(destination + "\\" + panel.picture.getName()));
								}
							}
							
						}
						try{
							synchronizeFileTree_Copy(movedList, destination);
						}catch(Exception e1){
							OperateUtils.showTips("�ƶ�ʧ��!", 2);
							return;
						}
						doShowPictures(pictures);
						// ������ʾ
						OperateUtils.showTips("�ƶ��ɹ���", 2);
					} catch (ConcurrentModificationException e3) {
						OperateUtils.showTips("�ƶ��ɹ���", 2);
					} catch (Exception e2) {
						// ������ʾ
						OperateUtils.showTips("�ƶ�ʧ�ܣ�", 2);
					}
				}
			}

		});

		this.menuBar.getEdit_rename().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {
					for (SingleImagePanel panel : singleImagePanels) {
						if (panel.isSelected) {
							break;
						}
						OperateUtils.showTips("δѡ���ļ���", 2);
						return;
					}
					MyDialog myDialog = new MyDialog();
				}
			}
		});

		this.menuBar.getEdit_delete().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {
					try {
						boolean isSelectFile = false;
						for (SingleImagePanel panel : singleImagePanels) {// ����������壬���Ƿ�ѡ�У�ѡ����ɾ��
							if (panel.isSelected) {
								//ɾ��Ŀ¼���ж�Ӧ�ڵ�
								synchronizeFileTree_Delete(panel);
								//ɾ���ļ�
								FileUtils.deleteFile(panel.picture
										.getAbsolutePath());
								pictures.remove(panel.picture);
								isSelectFile = true;
							}
						}
						doShowPictures(pictures);

						if (!isSelectFile) {
							OperateUtils.showTips("δѡ���ļ���", 2);
							return;
						}
						OperateUtils.showTips("ɾ���ɹ���", 2);
					} catch (ConcurrentModificationException e3) {
						OperateUtils.showTips("ɾ���ɹ���", 2);
					} catch (Exception e2) {
						OperateUtils.showTips("ɾ��ʧ�ܣ�", 2);
					}
				}
			}
		});
	
		this.menuBar.getEdit_selectAll().addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {
					if (!isSelectAll) {
						isSelectAll = true;
						toolBar.getSelectAll().setText("��ѡ");
						if(singleImagePanels.size() == 0){
							return;
						}
						for (SingleImagePanel panel : singleImagePanels) {
							panel.isSelected = false;
						}
					} else {
						isSelectAll = false;
						toolBar.getSelectAll().setText("ȫѡ");
						if(singleImagePanels.size() == 0){
							return;
						}
						for (SingleImagePanel panel : singleImagePanels) {
							panel.isSelected = true;
						}
					}
					doShowPictures(pictures);
				} else {
				}
			}
			
		});
		
	}

	class MyDialog extends JDialog {// �����������Ի���
		JTextField nameField, startField,bitField;
		JLabel nameLabel, startLabel,bitLabel;
		JButton submit;

		public MyDialog() {
			JPanel jp = new JPanel();
			jp.setLayout(new FlowLayout(FlowLayout.LEFT));

			nameLabel = new JLabel( "name label", JLabel.CENTER);
			startLabel = new JLabel("start label", JLabel.CENTER);
			bitLabel = new JLabel(  "bit label",JLabel.CENTER);
			nameField = new JTextField(25);
			startField = new JTextField(25);
			bitField = new JTextField(25);

			jp.add(nameLabel);
			jp.add(nameField);
			jp.add(startLabel);
			jp.add(startField);
			jp.add(bitLabel);
			jp.add(bitField);

			this.add(jp);

			submit = new JButton("submit");
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.CENTER));
			panel.add(submit);
			this.add(panel, BorderLayout.SOUTH);

			this.setSize(300, 160);
			// this.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
			//this.setModal(true);
			this.setLocationRelativeTo(null);
			this.setVisible(true);

			submit.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// TODO �Զ����ɵķ������

					String newName = nameField.getText();
					if (newName.equals("")) {
						OperateUtils.showTips("�ļ�������Ϊ��!", 2);
						return;
					}
					int start;//��ʼ���
					int bitNum;//λ��
					try {
						start = Integer.parseInt(startField.getText());
						bitNum = Integer.parseInt(bitField.getText());
						//���+ͼƬ�� ���ȴ��� λ�������
						if((start + pictures.size() + "").length() > bitNum){
							OperateUtils.showTips("���,λ������Ҫ��", 2);
							return;
						}
					} catch (Exception e1) {
						OperateUtils.showTips("����������!", 2);
						e1.printStackTrace();
						return;
					}
					
					try {
						for(SingleImagePanel panel : singleImagePanels){
							if(panel.isSelected){
								int index = panel.picture.getName().lastIndexOf(".");
								String type = panel.picture.getName().substring(index);
								
								Enumeration childrens = currentTreeNode.children();
								while(childrens.hasMoreElements()){
									DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) childrens.nextElement();
									FileNode fileNode = (FileNode) childNode.getUserObject();
									if(fileNode.getFile().getName().equals(panel.getDescribeTextField().getText())){
										String number = (((int)Math.pow(10, bitNum) + start)+"").substring(1);//���
										//�������ļ�
										FileUtils.renameFile(panel.picture.getAbsolutePath(), newName + number + type);
										//������ʾֵ
										panel.describeTextField.setText(newName + number + type);
										//��������ӦĿ¼���ڵ�
										String newPath = panel.picture.getAbsolutePath().substring(0,panel.picture.getAbsolutePath().lastIndexOf("\\")+1)+newName+number+type;
										File newPicture = new File(newPath);
										((FileNode)currentTreeNode.getUserObject()).getPictures().remove(panel.picture);
										panel.setPicture(newPicture);
										fileNode.setFile(panel.picture);
										((FileNode)currentTreeNode.getUserObject()).getPictures().add(panel.picture);
										fileNode.setName(FileSystemView.getFileSystemView().getSystemDisplayName(panel.picture));
									
										start++;
										break;
									}
								}
							}
							
						}
						DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
						model.nodeStructureChanged(currentTreeNode);
						// ������ʾ
						OperateUtils.showTips("�������ɹ���", 2);
						setVisible(false);
						dispose();
						setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					} catch (Exception e2) {
						// ������ʾ
						OperateUtils.showTips("������ʧ�ܣ�", 2);
						e2.printStackTrace();
					}
				}

			});

		}
	}

	
	public DefaultMutableTreeNode getCurrentTreeNode() {
		return currentTreeNode;
	}

	public void setCurrentTreeNode(DefaultMutableTreeNode currentTreeNode) {
		this.currentTreeNode = currentTreeNode;
	}

	public FileTree getFileTree() {
		return fileTree;
	}

	public void setFileTree(FileTree fileTree) {
		this.fileTree = fileTree;
	}
	//ɾ��ʱ����Ŀ¼���ж�Ӧ�ڵ�
	public void synchronizeFileTree_Delete(SingleImagePanel panel) {
		Enumeration childrens =  currentTreeNode.children();
		while(childrens.hasMoreElements()){
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)childrens.nextElement();
			FileNode fileNode = (FileNode)childNode.getUserObject();
			//�ж��Ƿ���Ҫ��ɾ���Ľڵ�
			if(fileNode.getFile().getAbsolutePath().equals(panel.picture.getAbsolutePath())){
				currentTreeNode.remove(childNode);
			}
		}
		DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
		model.nodeStructureChanged(currentTreeNode);
	}
	//�ƶ�ʱ���¶�ӦĿ¼���ڵ�
	public void synchronizeFileTree_Move(SingleImagePanel panel) {
		synchronizeFileTree_Delete(panel);
	}
	//����ʱ���¶�ӦĿ¼���ڵ�
	public void synchronizeFileTree_Copy(ArrayList<File> fileList,String destination) {
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) fileTree.getModel().getRoot();
		visitAllNodes(rootNode,fileList,destination);
	}
	
	public void visitAllNodes(DefaultMutableTreeNode startNode,ArrayList<File> fileList,String destination){
		
		if(!startNode.isRoot()){
			if(process(startNode,fileList,destination)){//�ҵ��ڵ�,����ɹ�
				DefaultTreeModel model = (DefaultTreeModel)fileTree.getModel();
				model.nodeStructureChanged(startNode);
				return;
			}
		}
		if(startNode.getChildCount() > 0){
			for(Enumeration e = startNode.children();e.hasMoreElements();){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
				visitAllNodes(node,fileList,destination);
			}
		}
	}

	private boolean process(DefaultMutableTreeNode node,ArrayList<File> fileList,String destination) {
		FileNode fileNode = (FileNode) node.getUserObject();
		//�жϽڵ��userobject��file·���Ƿ�Ϊdestination��������ýڵ�����ӽڵ�
		if(destination.equals(fileNode.getFile().getAbsolutePath())){
			FileSystemView fileSystemView = FileSystemView.getFileSystemView();
			for(File file : fileList){
				FileNode childFileNode = new FileNode(fileSystemView.getSystemDisplayName(file),
						file, fileSystemView.getSystemIcon(file),
						false, new ArrayList<File>());
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(childFileNode);
				node.add(childNode);
				fileNode.getPictures().add(file);
			}
			return true;
		}
		return false;
	}
}
