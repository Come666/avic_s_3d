package com.eteks.sweethome3d.swing.images.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.Timer;

import com.eteks.sweethome3d.swing.images.utils.OperateUtils;

/**
 *
 * @author Administrator
 */
public class ViewerChildDialog extends JDialog {

    //��ʾͼƬ�ı�ǩ
    PictureLabel pictureLabel;
    //ͼƬ�б�
    List<Image> imageList;
    //��ʱ��
    Timer timer;
    //ͼƬ�����ٶ�
    int speed = 1000;
    //�˵�
    JPopupMenu menuBar = new JPopupMenu();
    JMenuItem pause = new JMenuItem("pause");
    JMenuItem accelerate = new JMenuItem("accelerate");
    JMenuItem decerate = new JMenuItem("decerate");
    JMenuItem exit = new JMenuItem("exit");

    public static ViewerChildDialog dialog;

    public static ViewerChildDialog getInstanceDialog(List<Image> imageList, PictureLabel pictureLabel) {
        if (dialog == null) {
            dialog = new ViewerChildDialog(imageList, pictureLabel);
        }
        return dialog;
    }

    private ViewerChildDialog(List<Image> imageList, PictureLabel pictureLabel) {
        this.pictureLabel = pictureLabel;
        this.imageList = imageList;        
        timer = new Timer(speed, new TimerListener());
        initImage();
        pause.setIcon(new ImageIcon("fullscreen.png"));        
        menuBar.add(pause);
        menuBar.add(accelerate);
        menuBar.add(decerate);
        menuBar.add(exit);

        addListener();

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setUndecorated(true);
        this.setSize(d.width, d.height);
        this.setVisible(true);
        this.setModal(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
    }
    public void initImage(){
        
        //��������壨���ݲ㣩        
        this.getContentPane().add(this.pictureLabel,BorderLayout.CENTER);
        this.getContentPane().setBackground(Color.black);
        pictureLabel.setImage(imageList.get(imageList.indexOf(pictureLabel.getImage()) ));
        updateImageLocation2();
      //�Ƴ�����ͼƬ���϶�ͼƬ�ļ�����
        pictureLabel.removeMouseWheelListener(pictureLabel.EnlargeDcrease);
        pictureLabel.removeMouseMotionListener(pictureLabel.move);
        timer.start();
    }

    private void addListener() {
        //��ʾ�˵�
        pictureLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    menuBar.show(e.getComponent(), e.getX(), e.getY());
                }
            }

        });

        //�˳�
        exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pictureLabel.addMouseMotionListener(pictureLabel.move);
                pictureLabel.addMouseWheelListener(pictureLabel.EnlargeDcrease);
                timer.stop();
                //�����Ӵ���
                dialog.setVisible(false);
            }

        });

        //��ͣ/����
        pause.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if ("��ͣ".equals(pause.getText())) {
                    timer.stop();
                    pause.setText("pause");
                } else {
                    timer.start();
                    pause.setText("pause");
                }
            }

        });

        //����
        accelerate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (speed > 200) {
                    speed -= 200;
                } else {
                    accelerate.setEnabled(false);
                    OperateUtils.showTips("accelerate", 2);
                }
                timer.setDelay(speed);
            }

        });

        //����
        decerate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                accelerate.setEnabled(true);
                speed += 200;
                timer.setDelay(speed);
            }

        });
    }
    //����ͼƬ��С��λ��
    public void updateImageLocation2() {
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
        //�ػ�ͼƬ
        pictureLabel.repaint();
    }
    //ʱ�Ӽ�����
    private class TimerListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int next = imageList.indexOf(pictureLabel.getImage()) + 1;
            if (next >= imageList.size()) {
                next = 0;
            }
            pictureLabel.setImage(imageList.get(next));
            updateImageLocation2();
        }
    }

}
