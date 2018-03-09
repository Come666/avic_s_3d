package com.eteks.sweethome3d.swing.images.components;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
//ͼƬ�����
public class PictureLabel extends JLabel implements Cloneable {

    private Image image;
    private Image image2;
    //ͼƬ��С
    private static int imageWidth;
    private static int imageHeight;
    //ͼƬ����
    private static int ix;//�˴�ǧ���ܶ����x��y 
    private static int iy;
    //ͼƬ�϶�ʱ�����������
    private static int vx;
    private static int vy;
    private static boolean startMove = false;//�Ƿ�ʼ�϶�
    private boolean changeSize = true;//�Ƿ��������
    private boolean canMove = true;//�Ƿ�����϶�
    public static PictureLabel pictureLabel;//����ģʽ
    //�����ּ�����
    public MouseWheelListenerImpl EnlargeDcrease = new MouseWheelListenerImpl();
    //����϶�������
    public MouseMotionListenerImpl move = new MouseMotionListenerImpl();

    //���ͼƬ��ǩʵ��
    public static PictureLabel getInstanceLabel(Image image) {
        if (getPictureLabel() == null) {
            setPictureLabel(new PictureLabel(image));
       }
        return getPictureLabel();
    }

    private PictureLabel(Image image) {
        this.setImage(image);
        //�϶�ͼƬ
        this.addMouseMotionListener(move);
        //����ͼƬ
        this.addMouseWheelListener(EnlargeDcrease);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getImage() != null) {
            //ģʽ1��ֻ��ʾһ��ͼƬ
            if (getImage2() == null) {
                g.drawImage(getImage(), getIx(), getIy(), getImageWidth(), getImageHeight(), this);
            } //ģʽ2����ʾ����ͼƬ
            else {
                g.drawImage(getImage(), getIx(), getIy(), getImageWidth(), getImageHeight(), this);
                g.drawImage(image2, getIx() + getImageWidth(), getIy(), getImageWidth(), getImageHeight(), this);
            }
        }
    }

    //�Ŵ�ͼ��
    public boolean EnlargePicture() {
        //�Ŵ�ͼ����������
        if (isChangeSize() == true) {
            if (pictureLabel.getImageHeight() < pictureLabel.getHeight() * 12) {
                pictureLabel.setImageHeight(pictureLabel.getImageHeight() * 5 / 4);
                pictureLabel.setImageWidth(pictureLabel.getImageWidth() * 5 / 4);
                pictureLabel.setIx(pictureLabel.getWidth() / 2 - pictureLabel.getImageWidth() / 2);
                pictureLabel.setIy(pictureLabel.getHeight() / 2 - pictureLabel.getImageHeight() / 2);
            } else {
               // JOptionPane.showMessageDialog(null, "�Ѿ�̫���ˣ�");
                return false;
            }
        }
        return true;
    }

    //��Сͼ��
    public boolean DecreasePicture() {
        if (isChangeSize() == true) {
            if (pictureLabel.getImageHeight() > pictureLabel.getHeight() / 12) {
                pictureLabel.setImageHeight(pictureLabel.getImageHeight() * 3 / 4);
                pictureLabel.setImageWidth(pictureLabel.getImageWidth() * 3 / 4);
                pictureLabel.setIx(pictureLabel.getWidth() / 2 - pictureLabel.getImageWidth() / 2);
                pictureLabel.setIy(pictureLabel.getHeight() / 2 - pictureLabel.getImageHeight() / 2);
            } else {
               // JOptionPane.showMessageDialog(null, "�Ѿ���С�ˣ�");
                return false;
            }
        }
        return true;
    }

    //�����ק������
    private class MouseMotionListenerImpl implements MouseMotionListener {

        public void mouseDragged(MouseEvent e) {
            if (canMove) {
                //�ж��Ƿ����϶�״̬���Ƿ�հ�����꣩
                {
                    if (isStartMove() == false) {
                        //����հ�����꣬������ͼƬ�������������֮��
                        setVx(getIx() - e.getX());
                        setVy(getIy() - e.getY());
                        setStartMove(true);
                    }
                }
                //ͼƬ��������Χ��
                if ((getVy() + e.getY() > (getPictureLabel().getHeight() - getImageHeight())) && (getVy() + e.getY() < 0)) {
                    //��ͼƬ��հ���ʱ�����������ϴ˿̵���������ʾͼƬ�˿̵�����
                    setIy(getVy() + e.getY());
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
                }
                if ((getVx() + e.getX() > getPictureLabel().getWidth() - getImageWidth()) && getVx() + e.getX() < 0 && isStartMove() == true) {
                    //��ͼƬ��հ���ʱ�����������ϴ˿̵���������ʾͼƬ�˿̵�����
                    setIx(getVx() + e.getX());
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
                }

                repaint();
            }
        }

        public void mouseMoved(MouseEvent e) {
            setStartMove(false);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    //�����ּ�����
    private class MouseWheelListenerImpl implements MouseWheelListener {

        public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getWheelRotation() < 0) {
                EnlargePicture();
            } else {
                DecreasePicture();
            }
            repaint();
        }
    }

    /**
     * @return the imageWidth
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * @param imageWidth the imageWidth to set
     */
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    /**
     * @return the imageHeight
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * @param imageHeight the imageHeight to set
     */
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    /**
     * @return the ix
     */
    public int getIx() {
        return ix;
    }

    /**
     * @param ix the ix to set
     */
    public void setIx(int ix) {
        this.ix = ix;
    }

    /**
     * @return the iy
     */
    public int getIy() {
        return iy;
    }

    /**
     * @param iy the iy to set
     */
    public void setIy(int iy) {
        this.iy = iy;
    }

    /**
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * @return the image2
     */
    public Image getImage2() {
        return image2;
    }

    /**
     * @param image2 the image2 to set
     */
    public void setImage2(Image image2) {
        this.image2 = image2;
    }

    /**
     * @return the vx
     */
    public static int getVx() {
        return vx;
    }

    /**
     * @param aVx the vx to set
     */
    public static void setVx(int aVx) {
        vx = aVx;
    }

    /**
     * @return the vy
     */
    public static int getVy() {
        return vy;
    }

    /**
     * @param aVy the vy to set
     */
    public static void setVy(int aVy) {
        vy = aVy;
    }

    /**
     * @return the startMove
     */
    public static boolean isStartMove() {
        return startMove;
    }

    /**
     * @param aStartMove the startMove to set
     */
    public static void setStartMove(boolean aStartMove) {
        startMove = aStartMove;
    }

    /**
     * @return the pictureLabel
     */
    public static PictureLabel getPictureLabel() {
        return pictureLabel;
    }

    /**
     * @param aPictureLabel the pictureLabel to set
     */
    public static void setPictureLabel(PictureLabel aPictureLabel) {
        pictureLabel = aPictureLabel;
    }

    /**
     * @return the changeSize
     */
    public boolean isChangeSize() {
        return changeSize;
    }

    /**
     * @param changeSize the changeSize to set
     */
    public void setChangeSize(boolean changeSize) {
        this.changeSize = changeSize;
    }

    /**
     * @return the canMove
     */
    public boolean isCanMove() {
        return canMove;
    }

    /**
     * @param canMove the canMove to set
     */
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

}
