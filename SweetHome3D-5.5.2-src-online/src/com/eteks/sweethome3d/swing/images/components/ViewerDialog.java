package com.eteks.sweethome3d.swing.images.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.Timer;
//�鿴ͼƬ����
public class ViewerDialog extends JDialog {
    
    //������
     JToolBar toolBar;
    //��Ŧ
    JButton nextPicture, previousPicture, play, accelerate, decelerate;
    JButton enlarge, decrease, rotateR, rotateL;
    JButton type, fullScreen;
    //��ʾͼƬ�ı�ǩ
    PictureLabel pictureLabel;
    //��Ϣ���
    JTextField MessageLabel = new JTextField();
    //ͼ���б�
    List<Image> imageList = new ArrayList();
    //ͼƬ�ļ��б�
    List<File> pictures;
    //��ʱ��
    Timer timer;
    Timer timer2;
    //ͼƬ�����ٶ�
    int JumpSpeed = 3000;//����ģʽ�µ��ٶ�
    int MoveSpeed = 20;//��Ƭģʽ�µ��ٶ�
    //����ģʽ
    int model = 0;
    //��ת�Ƕ�
    int rotation = 0;
    //������ת��ͼƬ��ͼƬ�б�����±�
    int rotateIndex = -1;
    //�Ӵ���(ȫ���鿴)
    ViewerChildDialog viewerChildDialog;

    public ViewerDialog(List<File> pictures, File currentPicture) {
        this.pictures = pictures;
        System.out.println(this.imageList);
        //this.setSize(new Dimension(1366, 700));
        this.setSize(new Dimension(1024, 400));
        initComponent();
        initImageList(pictures);
        initImage(pictures.indexOf(currentPicture));
        System.out.println(this.imageList);
        timer = new Timer(JumpSpeed, new TimerListener());
        timer2 = new Timer(MoveSpeed, new TimerListener2());

        this.addListener();

        this.setResizable(false);
        this.setVisible(true);
        this.setModal(false);
        this.setTitle("view picture");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    //��ʼ��ͼ���б�
    public void initImageList(List<File> pictures) {
        for (int i = 0; i < pictures.size(); i++) {
            try {
                Image img = ImageIO.read(pictures.get(i));
                if (img != null) {
                    imageList.add(img);
                } else {
                    pictures.remove(i);
                }
            } catch (IOException ex) {
//            	 OperateUtils.showTips("ͼƬ����ʧ��!", 2);
            }
        }
    }

    //���õ�һ��ͼƬ����ͼƬ��ǩ�ӵ�������
    public void initImage(int index) {
        if (index < 0) {
            index = 0;
        }
        Image img = imageList.get(index);
        MessageLabel.setText(pictures.get(index).getAbsolutePath());
        pictureLabel = PictureLabel.getInstanceLabel(img);
        //����ͼƬĬ�ϴ�С
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        if (w > pictureLabel.getWidth() || h > pictureLabel.getHeight()) {
            double p = 1.0 * h / w;
            w = pictureLabel.getWidth() * 3 / 5;
            h = (int) (w * p);
        }
        pictureLabel.setImageWidth(w);
        pictureLabel.setImageHeight(h);
        //����ͼƬĬ��λ��
        pictureLabel.setIx(pictureLabel.getWidth() / 2 - pictureLabel.getImageWidth() / 2);
        pictureLabel.setIy(pictureLabel.getHeight() / 2 - pictureLabel.getImageHeight() / 2);
        pictureLabel.repaint();

        this.getRootPane().add(pictureLabel, BorderLayout.CENTER);
    }

    //��ʼ�����
    public void initComponent() {
        toolBar = new JToolBar();
        nextPicture = new JButton("next");
        
        play = new JButton("play");
        previousPicture = new JButton("previous picture");
        accelerate = new JButton("accelerate");
        decelerate = new JButton("decelerate");
        accelerate.setEnabled(false);
        decelerate.setEnabled(false);
        enlarge = new JButton("\u653e\u5927");
        decrease = new JButton("\u7f29\u5c0f");
        type = new JButton("type");
        type.setEnabled(false);
        fullScreen = new JButton("fullScreen");
        rotateR = new JButton("rotateR");
        rotateL = new JButton("rotateL");
        toolBar.setLayout(new FlowLayout(FlowLayout.CENTER));
//        toolBar.add(type);
//        toolBar.add(rotateR);
        toolBar.add(enlarge);
//        toolBar.add(decelerate);
//        toolBar.add(previousPicture);
//        toolBar.add(play);
//        toolBar.add(nextPicture);
//        toolBar.add(accelerate);
        toolBar.add(decrease);
//        toolBar.add(rotateL);
//        toolBar.add(fullScreen);
        
        toolBar.setBackground(Color.blue);
   

        //�����ײ����
        JRootPane rootPane = this.getRootPane();
        rootPane.setLayout(new BorderLayout());
        //�����������ڴ��������ı���
        rootPane.add(toolBar, BorderLayout.NORTH);
        //����Ϣ�����ڴ��ڵ�����
        rootPane.add(MessageLabel, BorderLayout.SOUTH);
        
        MessageLabel.setForeground(Color.orange);
        MessageLabel.setBackground(Color.gray);
        
        rootPane.setBackground(Color.LIGHT_GRAY);
        MessageLabel.setFont(new Font("����",Font.BOLD,16));
        for(int i=0;i<toolBar.getComponents().length;i++){
            toolBar.getComponent(i).setFont(new Font("����",Font.ITALIC,18));
            toolBar.getComponent(i).setBackground(Color.PINK);
        }     
        
    }

    //˳ʱ��ת90��
    public void rotateRight() {
        try {
            rotation += 90;
            if (rotation == 360) {
                rotation = 0;
            }
            //��һ����ת���ӱ�ǩ��õ�ͼƬ���ٵõ�ͼƬ��ͼƬ�б���±�
            if (rotateIndex == -1) {
                rotateIndex = imageList.indexOf(pictureLabel.getImage());
            }
////            //����ͼƬ��ͼƬ�б���±��·���б��еõ���ͼƬ��·��
////            Builder b = Thumbnails.of(pictures.get(rotateIndex).getAbsoluteFile())
////                    .scale(1.0)
////                    .rotate(rotation);
//            pictureLabel.setImage(new ImageIcon(b.asBufferedImage()).getImage());
            int wh = pictureLabel.getImageHeight();
            pictureLabel.setImageHeight(pictureLabel.getImageWidth());
            pictureLabel.setImageWidth(wh);
            pictureLabel.setIx(pictureLabel.getWidth() / 2 - pictureLabel.getImageWidth() / 2);
            pictureLabel.setIy(pictureLabel.getHeight() / 2 - pictureLabel.getImageHeight() / 2);
            pictureLabel.repaint();
        } catch (Exception ex) {
//        	 OperateUtils.showTips("ͼƬ����ʧ��!", 2);
        }
    }

    //��ʱ��ת90��
    public void rotateLeft() {
        try {
            rotation -= 90;
            if (rotation == -360) {
                rotation = 0;
            }
            //��һ����ת���ӱ�ǩ��õ�ͼƬ���ٵõ�ͼƬ��ͼƬ�б���±�
            if (rotateIndex == -1) {
                rotateIndex = imageList.indexOf(pictureLabel.getImage());
            }
//            //����ͼƬ��ͼƬ�б���±��·���б��еõ���ͼƬ��·��
//            Builder b = Thumbnails.of(pictures.get(rotateIndex).getAbsoluteFile())
//                    .scale(1.0)
//                    .rotate(rotation);
//            Image img = new ImageIcon(b.asBufferedImage()).getImage();
//            pictureLabel.setImage(img);
            int wh = pictureLabel.getImageHeight();
            pictureLabel.setImageHeight(pictureLabel.getImageWidth());
            pictureLabel.setImageWidth(wh);
            pictureLabel.setIx(pictureLabel.getWidth() / 2 - pictureLabel.getImageWidth() / 2);
            pictureLabel.setIy(pictureLabel.getHeight() / 2 - pictureLabel.getImageHeight() / 2);
            pictureLabel.repaint();
        } catch (Exception ex) {
//        	OperateUtils.showTips("ͼƬ����ʧ��!", 2);
        }
    }

    //ֹͣ����
    public void SetStop() {
        timer.stop();
        timer2.stop();
        play.setText("��ʼ����");
        previousPicture.setEnabled(true);
        nextPicture.setEnabled(true);
        accelerate.setEnabled(false);
        decelerate.setEnabled(false);
        rotateR.setEnabled(true);
        rotateL.setEnabled(true);
        //�ָ����Ź���
        enlarge.setEnabled(true);
        decrease.setEnabled(true);
        pictureLabel.setChangeSize(true);
        //�ָ��϶�����
        pictureLabel.setCanMove(true);
        type.setEnabled(false);
        updateImageLocation();
        //��ͼƬ2 ��Ϊ��
        pictureLabel.setImage2(null);
        pictureLabel.repaint();
        if (MessageLabel.getText().contains(" ")) {
            String text = MessageLabel.getText().substring(0, MessageLabel.getText().indexOf(" "));
            if (model == 1) {
                //�ı���Ϣ��������
                if (MessageLabel.getText().contains("~")) {
                    text = text.substring(0, text.indexOf("~"));
                    MessageLabel.setText(text);
                }
            } 
            else {
                MessageLabel.setText(text);
            }
        } else {            
            if (MessageLabel.getText().contains("~")) {
                String text =MessageLabel.getText().substring(0, MessageLabel.getText().indexOf("~"));
                MessageLabel.setText(text);
            }
        }
    }

    //��ʼ����
    public void SetPlay() {
        if (model == 0) {
            timer.start();
            //����ģʽ������ͼƬ��С��λ��
            updateImageLocation();
        } else {
            timer2.start();
            //��Ƭģʽ��Ҫ��������ͼƬ��С��λ��
            pictureLabel.setImageWidth(pictureLabel.getWidth());
            pictureLabel.setImageHeight(pictureLabel.getHeight());
            pictureLabel.setIx(0);
            pictureLabel.setIy(0);
        }
        play.setText("ֹͣ����");
        type.setEnabled(true);
        previousPicture.setEnabled(false);
        nextPicture.setEnabled(false);
        accelerate.setEnabled(true);
        decelerate.setEnabled(true);
        rotateR.setEnabled(false);
        rotateL.setEnabled(false);
        //�������Ź���
        enlarge.setEnabled(false);
        decrease.setEnabled(false);
        pictureLabel.setChangeSize(false);
        //�����϶�����
        pictureLabel.setCanMove(false);
    }

    //����ͼƬ��С��λ��
    public void updateImageLocation() {
        //����ͼƬĬ�ϴ�С
        int w = pictureLabel.getImage().getWidth(null);
        int h = pictureLabel.getImage().getHeight(null);
        if (w > pictureLabel.getWidth() || h > pictureLabel.getHeight()) {
            double p = 1.0 * h / w;
            w = pictureLabel.getWidth() * 3 / 5;
            h = (int) (w * p);
        }
        pictureLabel.setImageWidth(w);
        pictureLabel.setImageHeight(h);
        //����ͼƬĬ��λ��
        pictureLabel.setIx(pictureLabel.getWidth() / 2 - pictureLabel.getImageWidth() / 2);
        pictureLabel.setIy(pictureLabel.getHeight() / 2 - pictureLabel.getImageHeight() / 2);
        //�ָ�ͼƬĬ����ת
        rotation = 0;
        //�ػ�ͼƬ
        pictureLabel.repaint();
    }

    private void addListener() {
        // ��ʾǰһ��
        previousPicture.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int front;
                if (rotateIndex >= 0) {
                    front = rotateIndex - 1;
                    rotateIndex = -1;
                } else {
                    front = imageList.indexOf(pictureLabel.getImage()) - 1;
                }
                if (front < 0) {
                    front = imageList.size() - 1;
                }
                pictureLabel.setImage(imageList.get(front));
                MessageLabel.setText(pictures.get(front).getAbsolutePath());
                updateImageLocation();
            }

        });
        //��ʾ��һ��
        nextPicture.addActionListener(new ActionListener() {

            
            public void actionPerformed(ActionEvent e) {
                int next;
                //ͼƬ�ѷ�����ת��ֻ�ܴ�ͼƬ�б���õ�ͼƬ
                if (rotateIndex >= 0) {
                    next = rotateIndex + 1;
                    rotateIndex = -1;
                } else {
                    next = imageList.indexOf(pictureLabel.getImage()) + 1;
                }
                if (next >= imageList.size()) {
                    next = 0;
                }
                pictureLabel.setImage(imageList.get(next));
                MessageLabel.setText(pictures.get(next).getAbsolutePath());

                updateImageLocation();
            }
        });
        //�Զ�����
        play.addActionListener(new ActionListener() {

            
            public void actionPerformed(ActionEvent e) {
                if ("��ʼ����".equals(play.getText())) {
                    if (rotateIndex >= 0) {
                        pictureLabel.setImage(imageList.get(rotateIndex));
                        rotateIndex = -1;
                    }
                    SetPlay();
                } else {
                    SetStop();
                }
            }
        }
        );
        //���ٲ���
        accelerate.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                decelerate.setEnabled(true);
                if (model == 0) {
                    if (JumpSpeed > 500) {
                        JumpSpeed -= 500;
                    } else {
                        JOptionPane.showMessageDialog(null, "�����ټ����ˣ�");
                        accelerate.setEnabled(false);
                    }
                    timer.setDelay(JumpSpeed);
                } else if (model == 1) {
                    if (MoveSpeed > 2) {
                        MoveSpeed -= 2;
                    } else {
//                    	OperateUtils.showTips("�Ѵ�����ٶ�!", 2);
                        accelerate.setEnabled(false);
                    }
                    timer2.setDelay(MoveSpeed);
                }
            }
        }
        );
        //���ٲ���
        decelerate.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                accelerate.setEnabled(true);
                if (model == 0) {
                    if (JumpSpeed < 6000) {
                        JumpSpeed += 500;
                    } else {
                    	JOptionPane.showMessageDialog(null, "�����ټ����ˣ�");
                        decelerate.setEnabled(false);
                    }
                    timer.setDelay(JumpSpeed);
                } else if (model == 1) {
                    if (MoveSpeed < 30) {
                        MoveSpeed += 2;
                    } else {
//                    	OperateUtils.showTips("�Ѵ���С�ٶ�!", 2);
                        decelerate.setEnabled(false);
                    }
                    timer2.setDelay(MoveSpeed);
                }
            }
        }
        );
        //�Ŵ�
        enlarge.addActionListener(new ActionListener() {

            
            public void actionPerformed(ActionEvent e) {
                //�Ŵ�ͼ��
                pictureLabel.EnlargePicture();
                //�ػ�ͼ��
                pictureLabel.repaint();
            }
        }
        );
        //��С
        decrease.addActionListener(
                new ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                        pictureLabel.DecreasePicture();
                        pictureLabel.repaint();
                    }
                }
        );
        //����ģʽ�л�
        type.addActionListener( new ActionListener() {

                    
                    public void actionPerformed(ActionEvent e ) {
                        if (model == 0) {
                            //���óɽ�Ƭģʽ
                            model = 1;
                            type.setText("����ģʽ");
                            pictureLabel.setImageWidth(pictureLabel.getWidth());
                            pictureLabel.setImageHeight(pictureLabel.getHeight());
                            pictureLabel.setIx(0);
                            pictureLabel.setIy(0);
                            //ֹͣ��ʱ��1��������ʱ��λ��������ʱ��2
                            timer.stop();
                            timer2.start();
                            //�ѼӼ��ٰ�Ŧ�ָ�ʹ��
                            accelerate.setEnabled(true);
                            decelerate.setEnabled(true);
                            pictureLabel.repaint();
                        } else if (model == 1) {
                        	updateImageLocation();
                            //ֹͣ��ʱ��2��������ʱ��λ��������ʱ��1
                            timer2.stop();
                            timer.start();
                            //���ó�����ģʽ
                            model = 0;
                            type.setText("��Ƭģʽ");
                            //�ı���Ϣ��������
                            if(MessageLabel.getText().contains("~")){
                            String text = MessageLabel.getText().substring(0, MessageLabel.getText().indexOf("~"));
                            MessageLabel.setText(text+"     ��ǰ����ˢ��Ƶ��"+JumpSpeed+"����");
                            }
                            //�ѼӼ��ٰ�Ŧ�ָ�ʹ��
                            accelerate.setEnabled(true);
                            decelerate.setEnabled(true);
                            //��ͼƬ2 ��Ϊ��
                            pictureLabel.setImage2(null);
                        }
                    }

                }
        );
        //˳ʱ����ת
        rotateR.addActionListener(
                new ActionListener() {

                    
                    public void actionPerformed(ActionEvent e
                    ) {
                        rotateRight();
                    }

                }
        );
        //��ʱ����ת
        rotateL.addActionListener(  new ActionListener() {

                    
                    public void actionPerformed(ActionEvent e
                    ) {
                        rotateLeft();
                    }

                }
        );

        //ȫ����ʾ
        fullScreen.addActionListener(new ActionListener() {
                    
                    public void actionPerformed(ActionEvent e) {
                    	//�����ǰ�����ڲ���״̬�£�����ֹͣ����
                    	if(play.getText()=="ֹͣ����")
                    		SetStop();
                    	else{
                    		 //����ת��ͼƬ��λ
                            if (rotateIndex >= 0) {
                                pictureLabel.setImage(imageList.get(rotateIndex));
                                rotateIndex = -1;
                            }
                            //�ָ�ͼƬ��С��λ��
//                            updateImageLocation();
                    	}
                    		
                        //��һ�δ��Ӵ��ڣ����ȴ����Ӵ���
                        if (viewerChildDialog == null) {
                            viewerChildDialog = ViewerChildDialog.getInstanceDialog(imageList, pictureLabel);
                          System.out.println("create new dialog");
                        }                       
                       
                        //��ͼ���ǩ�ӵ��Ӵ����ϣ�����ʼ��
                        viewerChildDialog.initImage();
                        //��ʾ�Ӵ���
                        viewerChildDialog.setVisible(true);
                    }

                }
        );

        this.addWindowListener(new WindowAdapter() {

            
            public void windowActivated(WindowEvent e) {
            		initImage(imageList.indexOf(pictureLabel.getImage()));    
            		
            		 getRootPane().add(pictureLabel, BorderLayout.CENTER);
            		System.out.println("get focus");
            		pictureLabel.setImage(imageList.get(imageList.indexOf(pictureLabel.getImage()) ));
                    updateImageLocation();
            }
            
            public void windowClosed(WindowEvent e){
            	pictureLabel=null;
            }
        });
    }

    //ʱ�Ӽ�����1
    private class TimerListener implements ActionListener {

        
        public void actionPerformed(ActionEvent e) {
            int next = imageList.indexOf(pictureLabel.getImage()) + 1;
            System.out.println(next);
            if (next >= imageList.size()) {
                next = 0;
            }
            pictureLabel.setImage(imageList.get(next));
            MessageLabel.setText(pictures.get(next).getAbsolutePath()+"     ��ǰ����ˢ��Ƶ��"+JumpSpeed+"����");

            //����ͼƬĬ�ϴ�С
            updateImageLocation();
        }
    }

    //ʱ�Ӽ�����2
    private class TimerListener2 implements ActionListener {

        
        public void actionPerformed(ActionEvent e) {

            //���ͼƬ2Ϊ�գ�����һ��ͼƬ��ͼƬ2
            if (pictureLabel.getImage2() == null) {
                int next = imageList.indexOf(pictureLabel.getImage()) + 1;
                if (next >= imageList.size()) {
                    next = 0;
                }
                pictureLabel.setImage2(imageList.get(next));
                pictureLabel.setImageWidth(pictureLabel.getWidth());
                pictureLabel.setImageHeight(pictureLabel.getHeight());
                pictureLabel.setIx(0);
                pictureLabel.setIy(0);
                if (MessageLabel.getText().contains(" ")) {
                    String t = MessageLabel.getText().substring(0, MessageLabel.getText().indexOf(" "));
                    MessageLabel.setText(t + "~~~~~~~~~" + pictures.get(next).getAbsolutePath() + "     ��ǰ����ˢ��Ƶ��" + MoveSpeed + "����");
                }
                else{
                    MessageLabel.setText(MessageLabel.getText() + "~~~~~~~~~" + pictures.get(next).getAbsolutePath() + "     ��ǰ����ˢ��Ƶ��" + MoveSpeed + "����");
                }
            } //���ͼƬ1�Ѿ����꣬��ͼƬ2 ͼƬ1���ٽ���һ��ͼƬ��ͼƬ2
            else if (pictureLabel.getIx() + pictureLabel.getImageWidth() < 0) {
                int next = imageList.indexOf(pictureLabel.getImage2()) + 1;
                if (next >= imageList.size()) {
                    next = 0;
                }
                pictureLabel.setImage(pictureLabel.getImage2());
                pictureLabel.setImage2(imageList.get(next));
                //��ͼƬ1 �����긴λ
                pictureLabel.setIx(0);
                String text1 = MessageLabel.getText().substring(MessageLabel.getText().lastIndexOf("~") + 1,MessageLabel.getText().indexOf(" "));
                MessageLabel.setText(text1 + "~~~~~~~~~" + pictures.get(next).getAbsolutePath()+"     ��ǰ����ˢ��Ƶ��"+MoveSpeed+"����");
            }
            pictureLabel.setIx(pictureLabel.getIx() - 5);
            pictureLabel.repaint();
        }

    }
}
