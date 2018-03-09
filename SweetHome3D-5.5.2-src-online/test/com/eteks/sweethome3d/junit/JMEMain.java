package com.eteks.sweethome3d.junit;

import java.util.List;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class JMEMain extends SimpleApplication {

  // UserInput app = new UserInput();
  private final static Trigger TRIGGER_COLOR  = new KeyTrigger(KeyInput.KEY_SPACE);
  private final static Trigger TRIGGER_ROTATE = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
  private final static String  MAPPING_COLOR  = "Toggle Color";
  private final static String  MAPPING_ROTATE = "Rotate";
  private final static Trigger TRIGGER_COLOR2 = new KeyTrigger(KeyInput.KEY_C);

  @Override
  /** initialize the scene here */
  public void simpleInitApp() {
    // inputManager.addMapping(MAPPING_COLOR, TRIGGER_COLOR);
    inputManager.addMapping(MAPPING_ROTATE, TRIGGER_ROTATE);
    inputManager.addMapping(MAPPING_COLOR, TRIGGER_COLOR, TRIGGER_COLOR2);

    inputManager.addListener(actionListener, new String [] {MAPPING_COLOR});
    inputManager.addListener(analogListener, new String [] {MAPPING_ROTATE});

    // create a cube-shaped mesh
    Box b = new Box(Vector3f.ZERO, 1, 1, 1);
    // create an object from the mesh
    Geometry geom = new Geometry("Box1", b);
    // create a simple blue material

    // geom.rotate(0, intensity, 0);
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Blue);
    // give the object the blue material

    geom.setMaterial(mat);
    geom.getMaterial().setColor("Color", ColorRGBA.randomColor());
    // make the object appear in the scene
    rootNode.attachChild(geom);
  }

  @Override
  public void simpleUpdate(float tpf) {
    // System.out.println(tpf);
  }

  @Override
  /** (optional) Advanced renderer/frameBuffer modifications */
  public void simpleRender(RenderManager rm) {
    // System.out.println(rm);
  }

  /** Start the jMonkeyEngine application */
  public static void main(String [] args) {
    JMEMain app = new JMEMain();
    app.start();
  }

  private ActionListener actionListener = new ActionListener() {
                                          public void onAction(String name, boolean isPressed, float tpf) {
                                            System.out.println("actionListener + You triggered" + name + " intensity"
                                                               + isPressed + " tpf" + tpf);
                                            if (name.equalsIgnoreCase(MAPPING_COLOR)) {
                                              List<Spatial> ls = rootNode.getChildren();
                                              for (Spatial s : ls) {
                                                System.out.println(s.getName());
                                                if (s.getName().equalsIgnoreCase("Box1")) {
                                                  Geometry g = (Geometry)s;
                                                  g.getMaterial().setColor("Color", ColorRGBA.randomColor());
                                                }
                                              }
                                            }
                                          }
                                        };
  private AnalogListener analogListener = new AnalogListener() {
                                          public void onAnalog(String name, float intensity, float tpf) {
                                            System.out.println("analogListener   You triggered: " + name + " intensity"
                                                               + intensity + " tpf" + tpf);
                                            Vector2f click2d = inputManager.getCursorPosition();
                                            System.out.println("Vector2f:" + click2d);
                                            if (name.equalsIgnoreCase(MAPPING_ROTATE)) {
                                              List<Spatial> ls = rootNode.getChildren();
                                              for (Spatial s : ls) {
                                                System.out.println(s.getName());
                                                if (s.getName().equalsIgnoreCase("Box1")) {
                                                  Geometry g = (Geometry)s;
                                                  g.getMaterial().setColor("Color", ColorRGBA.randomColor());
                                                }
                                              }
                                            }
                                          }
                                        };
}
