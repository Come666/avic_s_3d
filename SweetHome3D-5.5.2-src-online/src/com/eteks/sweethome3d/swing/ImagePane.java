package com.eteks.sweethome3d.swing;

import java.awt.Panel;
import java.io.File;
import java.util.ArrayList;

import com.eteks.sweethome3d.swing.images.components.ViewerDialog;
/**
 * Created by ztc on 15-10-31.
 */
/**
 * Created by ztc on 15-10-31.
 */
public class ImagePane extends Panel  {
  public ImagePane(String filepath) {
    File picture = new File(filepath);
    ArrayList<File> pictures = new ArrayList<File>();
    pictures.add(picture);
    new ViewerDialog(pictures, picture);
  }

  // JButton[] jmi=new JButton[7];
  // JLabel jl,jl1;
  // JPanel jp;
  // JScrollPane jsp;
  // ImageIcon imgIco,showii;
  // File f;
  // float width;
  // float height;
  // public ImagePane(String filepath){
  // //创建操作按钮
  // jmi[0]=new JButton("\u653e\u5927");
  // jmi[0].addActionListener(this);
  // jmi[1]=new JButton("\u7f29\u5c0f�");
  // jmi[1].addActionListener(this);
  // jmi[2]=new JButton("\u539f\u56fe");
  // jmi[2].addActionListener(this);
  //
  // //设置图片并显示图片
  // f = new File(filepath);
  // ImagePaneTools.addFile(f);
  // imgIco = new ImageIcon(f.getPath());
  //
  // //显示区域
  // jl=new
  // JLabel("\u8bf7\u9009\u62e9\u56fe\u7247\u3002\u3002\u3002",JLabel.CENTER);
  // jl.setForeground(Color.gray);
  // jl.addMouseWheelListener(this);
  // jl.addMouseListener(this);
  // jsp=new JScrollPane(jl);
  //
  // //底部信息
  // jl1=new JLabel("\u56fe\u7247\u4fe1\u606f");
  // //设置窗口
  //
  // JPanel toolbar = new JPanel();
  // toolbar.add(jmi[0]);
  // toolbar.add(jmi[1]);
  // toolbar.add(jmi[2]);
  // this.setLayout(new BorderLayout());
  // this.add(toolbar, BorderLayout.NORTH );
  // this.add(jsp, BorderLayout.CENTER);
  // this.add(jl1, BorderLayout.SOUTH);
  // ShowImg();
  // this.setSize(700,500);
  // this.setLocation(300,200);
  // this.setVisible(true);
  // }
  // public void ShowImg(){
  // if(imgIco.getIconWidth()>695||imgIco.getIconHeight()>465){
  // width=695;
  // height=675*(float)imgIco.getIconHeight()/imgIco.getIconWidth();
  // //System.out.println((float)ii.getIconHeight()/ii.getIconWidth());
  // }else {
  // width=imgIco.getIconWidth();
  // height=imgIco.getIconHeight();
  // }
  // showii= new ImageIcon(
  // imgIco.getImage().getScaledInstance((int)width,(int)height,0));
  // this.jl.setText("");
  // this.jl.setIcon(showii);
  // ShowMsg();
  // }
  // public void Zoom(int flag){
  // if((width<5000&&height<4000)&&(width>5&&height>4)) {
  // if (flag == 0) {
  // //bigger
  // width *= 1.3;height *= 1.3;
  // } else if (flag == 1) {
  // //smaller
  // width *= 0.7;height *= 0.7;
  // } else if (flag == 2) {
  // //normal
  // width = imgIco.getIconWidth();height = imgIco.getIconHeight();
  // }
  // showii = new ImageIcon(imgIco.getImage().getScaledInstance((int) width,
  // (int) height, 0));
  // this.jl.setIcon(showii);
  // ShowMsg();
  // }
  // else if(width>5000||height>4000){
  // width*=0.8;
  // height*=0.8;
  // // JOptionPane.showMessageDialog(this, "再放大会失真！");
  // }else if(width<5||height<3){
  // width*=1.2;
  // height*=1.2;
  // // JOptionPane.showMessageDialog(this, "再缩小就看不见了！");
  // }
  // }
  // public void ShowMsg(){
  // String[] n=f.getName().split("\\.");
  // float per=(float)showii.getIconWidth()/imgIco.getIconWidth();
  // String
  // msg="\u539f\u56fe\uff1a"+imgIco.getIconWidth()+"x"+imgIco.getIconHeight()+"\u50cf\u7d20\u3002"
  // +"\u663e\u793a\uff1a"+showii.getIconWidth()+"x"+showii.getIconHeight()+"\u50cf\u7d20\u3002"
  // +"\u7f29\u653e\u5ea6\uff1a"+(int)(per*100)+"%"
  // +"\u683c\u5f0f:"+(n.length==1?"\u672a\u77e5":n[1]);
  // this.jl1.setText(msg);
  // this.jl1.setForeground(Color.gray);
  // }
  // public void actionPerformed(ActionEvent e) {
  // if(f!=null&&e.getSource()==jmi[0]){
  // //放大
  // Zoom(0);
  // }else if(f!=null&&e.getSource()==jmi[1]){
  // //缩小
  // Zoom(1);
  // }else if(f!=null&&e.getSource()==jmi[2]){
  // //原图
  // Zoom(2);
  // }
  // }
  //
  // public void mouseWheelMoved(MouseWheelEvent e) {
  // if(f!=null) {
  // if (e.getWheelRotation() == 1) {
  // Zoom(0);
  // } else if (e.getWheelRotation() == -1) {
  // Zoom(1);
  // }
  // }
  // }
  //
  // public void mouseClicked(MouseEvent e) {
  // if(f!=null) {
  // if (e.getClickCount() == 1 && e.getX() < 200) {
  // f = ImagePaneTools.getLast(f);
  // imgIco = new ImageIcon(f.getPath());
  // ShowImg();
  // } else if (e.getClickCount() == 1 && e.getX() > 500) {
  // f = ImagePaneTools.getNext(f);
  // imgIco = new ImageIcon(f.getPath());
  // ShowImg();
  // }
  // }
  // }
  //
  // public void mousePressed(MouseEvent e) {
  //
  // }
  //
  // public void mouseReleased(MouseEvent e) {
  //
  // }
  //
  // public void mouseEntered(MouseEvent e) {
  //
  // }
  //
  // public void mouseExited(MouseEvent e) {
  //
  // }
}
