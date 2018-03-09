package com.eteks.sweethome3d.j3d;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.LabelImage;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.image.TextureLoader;

public class LabelImage3D extends Label3D {

  public LabelImage3D(LabelImage label, Home home, boolean waitForLoading) {
    super(label, home, waitForLoading);
    // this.setPickable(true);
    System.out.println("LabelImage3D(LabelImage) pickable " + this.getPickable());
  }

  @Override
  public void update() {
    LabelImage label = (LabelImage)getUserData();

    String text = label.getImageName();
    if (label.getPitch() != null && text != null) {
      BranchGroup objRoot = new BranchGroup();
      TransformGroup trans = new TransformGroup();
      trans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
      trans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
      BufferedImage image = createImage(label.getText());
      
      // System.out.println("label.getColor():"+label.getColor());

      // objRoot.addChild(trans);
      // 加载正方体的六个面
      // .1
       TextureLoader myloader = new TextureLoader(image, null, 0);
      // // 创建纹理
       Texture2D mytext = (Texture2D)myloader.getTexture();
       int picWidth = mytext.getWidth();
       int picHeight = mytext.getHeight();
      // // 将纹理和外观绑定
       Appearance apperarance1 = new Appearance();
       apperarance1.setTexture(mytext);
       TextureAttributes myTexAtt = new TextureAttributes();
       myTexAtt.setTextureMode(TextureAttributes.MODULATE);
       apperarance1.setTextureAttributes(myTexAtt);
       Material mat = new Material();
       apperarance1.setMaterial(mat);
      // // .2
      // TextureLoader myloader2 = new TextureLoader(text, null);
      // // 创建纹理
      // Texture2D mytext2 = (Texture2D)myloader2.getTexture();
      // // 将纹理和外观绑定
      // Appearance apperarance2 = new Appearance();
      // apperarance2.setTexture(mytext2);
      // TextureAttributes myTexAtt2 = new TextureAttributes();
      // myTexAtt2.setTextureMode(TextureAttributes.MODULATE);
      // apperarance2.setTextureAttributes(myTexAtt2);
      // Material mat2 = new Material();
      // apperarance2.setMaterial(mat2);
      // // .3
      // TextureLoader myloader3 = new TextureLoader(text, null);
      // // 创建纹理
      // Texture2D mytext3 = (Texture2D)myloader3.getTexture();
      // // 将纹理和外观绑定
      // Appearance apperarance3 = new Appearance();
      // apperarance3.setTexture(mytext3);
      // TextureAttributes myTexAtt3 = new TextureAttributes();
      // myTexAtt3.setTextureMode(TextureAttributes.MODULATE);
      // apperarance3.setTextureAttributes(myTexAtt3);
      // Material mat3 = new Material();
      // apperarance3.setMaterial(mat3);
      // .4
      
      
      
//      TextureLoader myloader4 = new TextureLoader(text, null);
      
      TextureLoader myloader4=new TextureLoader(image, null, 0);

      // 创建纹理
      Texture2D mytext4 = (Texture2D)myloader4.getTexture();
//      int picWidth = mytext4.getWidth();
//      int picHeight = mytext4.getHeight();
      // 将纹理和外观绑定
      Appearance apperarance4 = new Appearance();
      apperarance4.setTexture(mytext4);
      TextureAttributes myTexAtt4 = new TextureAttributes();
      myTexAtt4.setTextureMode(TextureAttributes.MODULATE);
      apperarance4.setTextureAttributes(myTexAtt4);
      Material mat4 = new Material();
      apperarance4.setMaterial(mat4);
      // // .5
      // TextureLoader myloader5 = new TextureLoader(text, null);
      // // 创建纹理
      // Texture2D mytext5 = (Texture2D)myloader5.getTexture();
      // // 将纹理和外观绑定
      // Appearance apperarance5 = new Appearance();
      // apperarance5.setTexture(mytext5);
      // TextureAttributes myTexAtt5 = new TextureAttributes();
      // myTexAtt5.setTextureMode(TextureAttributes.MODULATE);
      // apperarance5.setTextureAttributes(myTexAtt5);
      // Material mat5 = new Material();
      // apperarance5.setMaterial(mat5);
      // // .6
      // TextureLoader myloader6 = new TextureLoader(text, null);
      // // 创建纹理
      // Texture2D mytext6 = (Texture2D)myloader6.getTexture();
      // // 将纹理和外观绑定
      // Appearance apperarance6 = new Appearance();
      // apperarance6.setTexture(mytext6);
      // TextureAttributes myTexAtt6 = new TextureAttributes();
      // myTexAtt6.setTextureMode(TextureAttributes.MODULATE);
      // apperarance6.setTextureAttributes(myTexAtt6);
      // Material mat6 = new Material();
      // apperarance6.setMaterial(mat6);

      // 一个立方体
      float a = ((float)picWidth) / 30f;
//      float a = 1;
      float x = picWidth / a;// 20f;// 20f;
      float y = 0f;
      float z = picHeight / a;// 20f;
      Box box = new Box(x, z, y, Box.GENERATE_TEXTURE_COORDS, new Appearance());
       box.getShape(Box.FRONT).setAppearance(apperarance1);
      // box.getShape(Box.LEFT).setAppearance(apperarance2);
      // box.getShape(Box.RIGHT).setAppearance(apperarance3);
      box.getShape(Box.BACK).setAppearance(apperarance4);
      // box.getShape(Box.TOP).setAppearance(apperarance5);
      // box.getShape(Box.BOTTOM).setAppearance(apperarance6);
      trans.addChild(box);
      objRoot.addChild(trans);

      Transform3D transform = new Transform3D();
      transform.setTranslation(new Vector3d(label.getX(), label.getGroundElevation() + z, label.getY()));
      trans.setTransform(transform);

      objRoot.setName(label.getText());
      objRoot.compile();
      addChild(objRoot);

    }
  }

  // 根据str,font的样式以及输出文件目录
  private BufferedImage createImage(String str)  {
    Integer width = str.length() * 35;
    Integer height = 128;
    // 创建图片
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
    Graphics g = image.getGraphics();
    g.setClip(0, 0, width, height);
    g.setColor(Color.CYAN);
    g.fillRect(0, 0, width, height);// 先用黑色填充整张图片,也就是背景
    g.setColor(Color.BLACK);// 在换成黑色
    Font font = new Font("\u5b8b\u4f53", Font.BOLD, 40);
    g.setFont(font);// 设置画笔字体
    Rectangle clip = g.getClipBounds();
    FontMetrics fm = g.getFontMetrics(font);
    int ascent = fm.getAscent();
    int descent = fm.getDescent();
    int y = (clip.height - (ascent + descent)) / 2 + ascent;
    g.drawString(str, 0 * 680, y);// 画出字符串
    g.dispose();
    // ImageIO.write(image, "png", outFile);// 输出png图片
    return image;
  }

}
