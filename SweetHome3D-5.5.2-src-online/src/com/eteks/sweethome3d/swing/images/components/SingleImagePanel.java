package com.eteks.sweethome3d.swing.images.components;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class SingleImagePanel extends JPanel implements MouseListener {
	File picture;
	MyPopupMenu popupMenu;// �Ҽ��˵�
	JLabel imageLabel;
	JTextField describeTextField;
	ArrayList<File> pictures;
	JCheckBox checkBox;//��ѡ��
	boolean isShowCheckBox ;//�Ƿ���ʾ��ѡ��
	boolean isSelected;//��־�Ƿ�ѡ��
	String reName_OldName;//������ʱ��������ԭ�����ļ���
	JPanel checkBoxPanel;
	public SingleImagePanel(File picture, ArrayList<File> pictures,boolean isShowCheckBox) {
//		super();
//		this.picture = picture;
//		this.pictures = pictures;
//		this.isShowCheckBox = isShowCheckBox;
//		if(isShowCheckBox){//����isShowCheckBox�Ƿ���ʾ��ѡ��
//			checkBox = new JCheckBox();
//			checkBox.setBackground(Color.white);
//		}
//		popupMenu = new MyPopupMenu();
//		this.setPreferredSize(new Dimension(100, 150));
//		this.setBackground(Color.white);
//		this.addMouseListener(this);
//		this.addListenerToComponents();
//		this.setComponentPopupMenu(popupMenu);
//		// *************************************
//
//		this.setLayout(new BorderLayout());
//		@SuppressWarnings("rawtypes")
//		Builder builder = Thumbnails.of(picture).size(90, 120);
//		BufferedImage image = null;
//		try {
//			image = builder.asBufferedImage();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		ImageIcon imageIcon = new ImageIcon(image);
//		imageLabel = new JLabel(imageIcon);
//		describeTextField = new JTextField(picture.getName());
//		this.describeTextField.setOpaque(true);
//		this.describeTextField.setBackground(new Color(255, 255, 255));
//		this.describeTextField.setBorder(BorderFactory.createEmptyBorder());
//		this.describeTextField.setEditable(false);
//		
//		if(checkBox != null){
//			checkBoxPanel = new JPanel();
//			checkBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
//			checkBoxPanel.setBackground(Color.white);
//			checkBoxPanel.add(checkBox);
//			this.add(checkBoxPanel,BorderLayout.NORTH);
//		}
//		this.add(imageLabel, BorderLayout.CENTER);
//		this.add(describeTextField, BorderLayout.SOUTH);
//
//		this.describeTextField.addMouseListener(new MouseListener() {
//
//			public void mouseClicked(MouseEvent e) { // TODO �Զ����ɵķ������
//				if (e.getClickCount() == 2) {// ˫��������
//					reName_OldName = describeTextField.getText();//���������
//					describeTextField.setEditable(true);
//					//describeTextField.setFocusable(true);
//					describeTextField.selectAll();
//					describeTextField.setBorder(BorderFactory
//							.createLineBorder(Color.lightGray));
//					}
//			}
//			public void mousePressed(MouseEvent e) {
//			}
//			public void mouseReleased(MouseEvent e) {
//			}
//			public void mouseEntered(MouseEvent e) {
//			}
//			public void mouseExited(MouseEvent e) {
//			}
//		});
	}
	
	// ���Ҽ��˵���Ӽ�����
	private void addListenerToComponents() {
//		if (checkBox != null) {
//			this.checkBox.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					// TODO �Զ����ɵķ������
//					if (checkBox.isSelected()) {
//						isSelected = true;
//					} else {
//						isSelected = false;
//					}
//				}
//			});
//		}
//		// ����
//		this.popupMenu.getPopupMenu_copy().addActionListener(
//				new ActionListener() {
//
//					public void actionPerformed(ActionEvent e) {
//						// TODO Auto-generated method stub
//						try {// ûѡ���ļ�ʱ�����쳣
//							JFileChooser fileChooser = OperateUtils
//									.getDefaultFileChooser("file chooser");
//							String destination;
//							try{
//								destination = fileChooser.getSelectedFile().getAbsolutePath();
//							}catch(Exception e1){
//								return;
//							}
//							try {
//								//�����ļ�
//								FileUtils.copyFile(destination+ "\\"+picture.getName(),
//										picture.getAbsolutePath());
//								//���¶�ӦĿ¼���ڵ�
//								try{
//									sychronizeFileTree_Copy(destination,new File(destination+ "\\"+picture.getName()));
//								}catch(Exception e1){
//									OperateUtils.showTips("����ʧ��!", 2);
//									return;
//								}
//								// ������ʾ
//								OperateUtils.showTips("���Ƴɹ���", 2);
//							} catch (Exception e1) {
//								// TODO Auto-generated catch block
//								// ������ʾ
//								OperateUtils.showTips("����ʧ�ܣ�", 2);
//							}
//						} catch (Exception e1) {
//							// TODO Auto-generated catch block
//						}
//					}
//				});
//		// �ƶ�
//		this.popupMenu.getPopupMenu_move().addActionListener(
//				new ActionListener() {
//
//					public void actionPerformed(ActionEvent e) {
//						// TODO Auto-generated method stub
//						try {// ûѡ���ļ�ʱ�����쳣
//							JFileChooser fileChooser = OperateUtils
//									.getDefaultFileChooser("�ƶ���");
//							String destination;
//							try{
//								destination = fileChooser.getSelectedFile().getAbsolutePath();
//							}catch(Exception e1){
//								return;
//							}
//							try {
//								//ɾ��Ŀ¼����Ӧ�ڵ�
//								sychronizeFileTree_Move();
//								//�ƶ��ļ�
//								FileUtils.moveFile(destination+"\\"+picture.getName(),
//										picture.getAbsolutePath());
//								// ɾ��pictures�еĸü�¼
//								pictures.remove(picture);
//								//���¶�ӦĿ¼���ڵ�
//								try{
//									sychronizeFileTree_Copy(destination+ "\\"+picture.getName(),new File(destination+ "\\"+picture.getName()));
//								}catch(Exception e1){
//									OperateUtils.showTips("�ƶ�ʧ��!", 2);
//									return;
//								}
//								((ShowPicturePanel) getParent()).doShowPictures(pictures);
//								// ������ʾ
//								OperateUtils.showTips("�ƶ��ɹ���", 2);
//							} catch (Exception e1) {
//								// TODO Auto-generated catch block
//								// ������ʾ
//								OperateUtils.showTips("�ƶ�ʧ�ܣ�", 2);
//							}
//						} catch (Exception e1) {
//							// TODO Auto-generated catch block
//						}
//					}
//
//				});
//		// ������
//		this.popupMenu.getPopupMenu_reName().addActionListener(
//				new ActionListener() {
//
//					public void actionPerformed(ActionEvent e) {
//						// TODO Auto-generated method stub
//						reName_OldName = describeTextField.getText();//���������
//						describeTextField.setEditable(true);
//						// describeTextField.setFocusable(true);
//						describeTextField.selectAll();
//						describeTextField.setBorder(BorderFactory
//								.createLineBorder(Color.lightGray));
//					}
//
//				});
//		// c�鿴����
//		this.popupMenu.getPopupMenu_property().addActionListener(
//				new ActionListener() {
//
//					public void actionPerformed(ActionEvent e) {
//						// TODO Auto-generated method stub
//						new PropertyDialog(picture);
//					}
//
//				});
//
//		// ɾ��
//		this.popupMenu.getPopupMenu_delete().addActionListener(
//				new ActionListener() {
//
//					public void actionPerformed(ActionEvent e) {
//						// TODO Auto-generated method stub
//
//						try {
//							//ɾ��Ŀ¼����Ӧ�ڵ�
//							sychronizeFileTree_Delete();
//							//�ƶ��ļ�
//							FileUtils.deleteFile(picture.getAbsolutePath());
//							pictures.remove(picture);
//							((ShowPicturePanel) getParent())
//									.doShowPictures(pictures);
//							// ������ʾ
//							OperateUtils.showTips("ɾ���ɹ���", 2);
//						} catch (Exception e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//							// ������ʾ
//							OperateUtils.showTips("ɾ��ʧ�ܣ�", 2);
//						}
//					}
//
//				});
//		// ȫ���鿴
//		this.popupMenu.getPopupMenu_view().addActionListener(
//				new ActionListener() {
//
//					public void actionPerformed(ActionEvent e) {
//						// TODO Auto-generated method stub
//						ViewerDialog dialog = new ViewerDialog(pictures, picture);
//					}
//
//				});
//		
	}

	public void mouseClicked(MouseEvent e) {
//		// TODO Auto-generated method stub
//		if (e.getClickCount() == 1) {// ����һ��ȷ��������
//			if(!describeTextField.isEditable()){
//				this.describeTextField.setBorder(null);
//				this.describeTextField.setEditable(false);
//				return;
//			}
//			this.describeTextField.setBorder(null);
//			this.describeTextField.setEditable(false);
//			try {
//				//��������ӦĿ¼���ڵ�
//				ShowPicturePanel showPicturePanel = (ShowPicturePanel)getParent();
//				FileTree fileTree = showPicturePanel.getFileTree();
//				DefaultMutableTreeNode currentTreeNode = showPicturePanel.getCurrentTreeNode();
//				Enumeration childrens = currentTreeNode.children();
//				while(childrens.hasMoreElements()){
//					DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) childrens.nextElement();
//					FileNode fileNode = (FileNode) childNode.getUserObject();
//					//�ж��Ƿ������������ļ�(�ڵ����ֺ�������ǰ������һ��)
//					if(fileNode.getFile().getName().equals(reName_OldName)){
//						//�������ļ�
//						FileUtils.renameFile(picture.getAbsolutePath(),describeTextField.getText());
//						//������Ŀ¼����Ӧ�ڵ�
//						int index = picture.getAbsolutePath().lastIndexOf("\\");
//						String newPath = picture.getAbsolutePath().substring(0,index+1)+describeTextField.getText();
//						File newPicture = new File(newPath);
//						showPicturePanel.pictures.remove(picture);
//						picture = newPicture;
//						fileNode.setFile(picture);
//						showPicturePanel.pictures.add(picture);
//						fileNode.setName(FileSystemView.getFileSystemView().getSystemDisplayName(picture));
//						break;
//					}
//				}
//				DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
//				model.nodeStructureChanged(currentTreeNode);
//				
//				OperateUtils.showTips("�������ɹ�", 2);
//			} catch (Exception e1) {
//			}
//		} else if (e.getClickCount() == 2) {// ˫��ȡ������鿴����ͼƬ��ģʽ
//			ViewerDialog dialog = new ViewerDialog(pictures, picture);
//		}
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		this.setBackground(new Color(184,224,243));
		this.describeTextField.setBackground(new Color(184,224,243));
		if(checkBoxPanel != null){
			this.checkBoxPanel.setBackground(new Color(184,224,243));
			this.checkBox.setBackground(new Color(184,224,243));
		}
		//this.setBorder(BorderFactory.createLineBorder(new Color(229, 243, 251)));
	}

	public void mouseExited(MouseEvent e) {
		this.setBackground(Color.white);
		this.describeTextField.setBackground(Color.white);
		if(checkBoxPanel != null){
			this.checkBoxPanel.setBackground(Color.white);
			this.checkBox.setBackground(Color.white);
		}
	}

	public File getPicture() {
		return picture;
	}

	public void setPicture(File picture) {
		this.picture = picture;
	}

	public MyPopupMenu getPopupMenu() {
		return popupMenu;
	}

	public void setPopupMenu(MyPopupMenu popupMenu) {
		this.popupMenu = popupMenu;
	}

	public JLabel getImageLabel() {
		return imageLabel;
	}

	public void setImageLabel(JLabel imageLabel) {
		this.imageLabel = imageLabel;
	}

	public JTextField getDescribeTextField() {
		return describeTextField;
	}

	public void setDescribeTextField(JTextField describeTextField) {
		this.describeTextField = describeTextField;
	}

	public JCheckBox getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(JCheckBox checkBox) {
		this.checkBox = checkBox;
	}

	public boolean isShowCheckBox() {
		return isShowCheckBox;
	}

	public void setShowCheckBox(boolean isShowCheckBox) {
		this.isShowCheckBox = isShowCheckBox;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public ArrayList<File> getPictures() {
		return pictures;
	}
	public void setPictures(ArrayList<File> pictures) {
		this.pictures = pictures;
	}


	public void sychronizeFileTree_Delete() {
//		ShowPicturePanel showPicrurePanel = (ShowPicturePanel)getParent();
//		FileTree fileTree = showPicrurePanel.getFileTree();
//		DefaultMutableTreeNode currentTreeNode = showPicrurePanel.getCurrentTreeNode();
//		Enumeration childrens =  currentTreeNode.children();
//		while(childrens.hasMoreElements()){
//			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)childrens.nextElement();
//			FileNode fileNode = (FileNode)childNode.getUserObject();
//			if(picture.getAbsolutePath().equals(fileNode.getFile().getAbsolutePath())){
//				currentTreeNode.remove(childNode);
//			}
//		}
//		DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
//		model.nodeStructureChanged(currentTreeNode);
	}
	public void sychronizeFileTree_Move() {
		sychronizeFileTree_Delete();
	}
	public void sychronizeFileTree_Copy(String destination,File picture) {
//		ShowPicturePanel showPicrurePanel = (ShowPicturePanel)getParent();
//		FileTree fileTree = showPicrurePanel.getFileTree();
//		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) fileTree.getModel().getRoot();//�õ����ڵ�
//		visitAllNodes(rootNode,picture,destination);
	}
	public void visitAllNodes(DefaultMutableTreeNode startNode,File picture,String destination){
		
		if(!startNode.isRoot()){
			if(process(startNode,picture,destination)){//�ҵ��ڵ�,����ɹ�
//				ShowPicturePanel showPicrurePanel = (ShowPicturePanel)getParent();
//				FileTree fileTree = showPicrurePanel.getFileTree();
//				DefaultTreeModel model = (DefaultTreeModel)fileTree.getModel();
//				//֪ͨģ�������Ѹı�
//				model.nodeStructureChanged(startNode);
				return;
			}
		}
		if(startNode.getChildCount() > 0){
			for(Enumeration e = startNode.children();e.hasMoreElements();){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
				visitAllNodes(node,picture,destination);
			}
		}
	}
	private boolean process(DefaultMutableTreeNode node,File picture,String destination) {
//		FileNode fileNode = (FileNode) node.getUserObject();
//		//�жϽڵ��userobject��file·���Ƿ�Ϊdestination��������ýڵ�����ӽڵ�
//		if(destination.equals(fileNode.getFile().getAbsolutePath())){
//			FileSystemView fileSystemView = FileSystemView.getFileSystemView();
//			
//				FileNode childFileNode = new FileNode(fileSystemView.getSystemDisplayName(picture),
//						picture, fileSystemView.getSystemIcon(picture),
//						false, new ArrayList<File>());
//				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(childFileNode);
//				node.add(childNode);
//				fileNode.getPictures().add(picture);
//			
//			return true;
//		}
		return false;
	}
	
}