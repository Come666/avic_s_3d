/*
 * HomePieceOfFurniture3D.java 23 jan. 09
 * 
 * Sweet Home 3D, Copyright (c) 2007-2009 Emmanuel PUYBARET / eTeks
 * <info@eteks.com>
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.eteks.sweethome3d.j3d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingLeaf;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.CapabilityNotSetException;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Group;
import javax.media.j3d.Link;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PointLight;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.UIManager;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.eteks.sweethome3d.j3d.TextureManager.TextureObserver;
import com.eteks.sweethome3d.model.Content;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomeEnvironment;
import com.eteks.sweethome3d.model.HomeFurnitureGroup;
import com.eteks.sweethome3d.model.HomeLight;
import com.eteks.sweethome3d.model.HomeMaterial;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.HomeTexture;
import com.eteks.sweethome3d.model.Label;
import com.eteks.sweethome3d.model.Level;
import com.eteks.sweethome3d.model.Light;
import com.eteks.sweethome3d.model.LightSource;
import com.eteks.sweethome3d.model.Room;
import com.eteks.sweethome3d.model.Selectable;
import com.eteks.sweethome3d.model.SelectionEvent;
import com.eteks.sweethome3d.model.SelectionListener;
import com.eteks.sweethome3d.model.TextStyle;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.image.TextureLoader;

/**
 * Root of piece of furniture branch.
 */
public class HomePieceOfFurniture3D extends Object3DBranch {
  private static final TransparencyAttributes DEFAULT_TEXTURED_SHAPE_TRANSPARENCY_ATTRIBUTES   = new TransparencyAttributes(
                                                                                                   TransparencyAttributes.NICEST,
                                                                                                   0);
  private static final PolygonAttributes      DEFAULT_TEXTURED_SHAPE_POLYGON_ATTRIBUTES        = new PolygonAttributes(
                                                                                                   PolygonAttributes.POLYGON_FILL,
                                                                                                   PolygonAttributes.CULL_NONE,
                                                                                                   0, false);
  private static final PolygonAttributes      NORMAL_FLIPPED_TEXTURED_SHAPE_POLYGON_ATTRIBUTES = new PolygonAttributes(
                                                                                                   PolygonAttributes.POLYGON_FILL,
                                                                                                   PolygonAttributes.CULL_NONE,
                                                                                                   0, true);
  private static final Bounds                 DEFAULT_INFLUENCING_BOUNDS                       = new BoundingSphere(
                                                                                                   new Point3d(), 1E7);
  private static final Object                 DEFAULT_BOX                                      = new Object();

  private final Home                          home;

  static {
    DEFAULT_TEXTURED_SHAPE_POLYGON_ATTRIBUTES.setCapability(PolygonAttributes.ALLOW_CULL_FACE_READ);
    NORMAL_FLIPPED_TEXTURED_SHAPE_POLYGON_ATTRIBUTES.setCapability(PolygonAttributes.ALLOW_CULL_FACE_READ);
  }

  /**
   * Creates the 3D piece matching the given home <code>piece</code>.
   */
  public HomePieceOfFurniture3D(HomePieceOfFurniture piece, Home home) {
    this(piece, home, false, false);
  }

  /**
   * Creates the 3D piece matching the given home <code>piece</code>.
   */
  public HomePieceOfFurniture3D(HomePieceOfFurniture piece, Home home, boolean ignoreDrawingMode,
                                boolean waitModelAndTextureLoadingEnd) {
    setUserData(piece);
    this.home = home;

    // Allow piece branch to be removed from its parent
    setCapability(BranchGroup.ALLOW_DETACH);
    // Allow to read branch transform child
    setCapability(BranchGroup.ALLOW_CHILDREN_READ);
    setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

    HomePieceOfFurniture tmpPiece = (HomePieceOfFurniture)getUserData();
//    System.out.println("HomePieceOfFurniture3D.update piece.isScan:[" + tmpPiece.isScan() + "] ");
    if (tmpPiece.isScan() == true) {
      scanHotpointUpdate();
    } else {
      createPieceOfFurnitureNode(piece, ignoreDrawingMode, waitModelAndTextureLoadingEnd);
    }
  }

  /**
   * Creates the piece node with its transform group and add it to the piece
   * branch.
   */
  private void createPieceOfFurnitureNode(final HomePieceOfFurniture piece, final boolean ignoreDrawingMode,
                                          final boolean waitModelAndTextureLoadingEnd) {
    final TransformGroup pieceTransformGroup = new TransformGroup();
    // Allow the change of the transformation that sets piece size and position
    pieceTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    pieceTransformGroup.setCapability(Group.ALLOW_CHILDREN_READ);
    pieceTransformGroup.setCapability(Group.ALLOW_CHILDREN_WRITE);
    pieceTransformGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);
    addChild(pieceTransformGroup);

    if (piece instanceof HomeLight) {
      BoundingLeaf bounds = new BoundingLeaf();
      bounds.setCapability(BoundingLeaf.ALLOW_REGION_WRITE);
      addChild(bounds);
    }

    // While loading model use a temporary node that displays a white box
    final BranchGroup waitBranch = new BranchGroup();
    waitBranch.setCapability(BranchGroup.ALLOW_DETACH);
    TransformGroup normalization = new TransformGroup();
    normalization.addChild(getModelBox(Color.WHITE));
    setModelCapabilities(normalization);
    waitBranch.addChild(normalization);
    // Allow appearance change on all children
    setModelCapabilities(waitBranch);

    pieceTransformGroup.addChild(waitBranch);

    // Set piece model initial location, orientation and size
    updatePieceOfFurnitureTransform();

    // Load piece real 3D model
    Content model = piece.getModel();
    ModelManager.getInstance().loadModel(model, waitModelAndTextureLoadingEnd, new ModelManager.ModelObserver() {
      public void modelUpdated(BranchGroup modelRoot) {
        float [][] modelRotation = piece.getModelRotation();
        // Add piece model scene to a normalized transform group
        TransformGroup modelTransformGroup = ModelManager.getInstance().getNormalizedTransformGroup(modelRoot,
            modelRotation, 1, piece.isModelCenteredAtOrigin());

        cloneHomeTextures(modelRoot);
        updatePieceOfFurnitureModelNode(modelRoot, modelTransformGroup, ignoreDrawingMode,
            waitModelAndTextureLoadingEnd);
      }

      public void modelError(Exception ex) {
        // In case of problem use a default red box
        updatePieceOfFurnitureModelNode(getModelBox(Color.RED), new TransformGroup(), ignoreDrawingMode,
            waitModelAndTextureLoadingEnd);
      }

      /**
       * Replace the textures set on <code>node</code> shapes by clones.
       */
      private void cloneHomeTextures(Node node) {
        if (node instanceof Group) {
          // Enumerate children
          Enumeration<?> enumeration = ((Group)node).getAllChildren();
          while (enumeration.hasMoreElements()) {
            cloneHomeTextures((Node)enumeration.nextElement());
          }
        } else if (node instanceof Link) {
          cloneHomeTextures(((Link)node).getSharedGroup());
        } else if (node instanceof Shape3D) {
          Appearance appearance = ((Shape3D)node).getAppearance();
          if (appearance != null) {
            Texture texture = appearance.getTexture();
            if (texture != null) {
              appearance.setTexture(getHomeTextureClone(texture, home));
            }
          }
        }
      }
    });
  }

  /**
   * Updates this branch from the home piece it manages.
   */
  @Override
  public void update() {

    updatePieceOfFurnitureTransform();
    updatePieceOfFurnitureModelMirrored();
    updatePieceOfFurnitureColorAndTexture(false);
    updateLight();
    updatePieceOfFurnitureVisibility();
  }

  /**
   * Sets the transformation applied to piece model to match its location, its
   * angle and its size.
   */
  private void updatePieceOfFurnitureTransform() {
    TransformGroup transformGroup = (TransformGroup)getChild(0);
    Transform3D pieceTransform = ModelManager.getInstance().getPieceOfFurnitureNormalizedModelTransformation(
        (HomePieceOfFurniture)getUserData(), transformGroup.getChild(0));
    // Change model transformation
    transformGroup.setTransform(pieceTransform);
  }

  /**
   * Sets the color and the texture applied to piece model.
   */
  private void updatePieceOfFurnitureColorAndTexture(boolean waitTextureLoadingEnd) {
    HomePieceOfFurniture piece = (HomePieceOfFurniture)getUserData();
    Node filledModelNode = getFilledModelNode();
    Node filledModelChild = ((Group)filledModelNode).getChild(0);

    if (filledModelChild.getUserData() != DEFAULT_BOX) {
      if (piece.getColor() != null) {
        setColorAndTexture(filledModelNode, piece.getColor(), null, piece.getShininess(), null, false, null, null,
            new HashSet<Appearance>());
      } else if (piece.getTexture() != null) {
        setColorAndTexture(filledModelNode, null, piece.getTexture(), piece.getShininess(), null,
            waitTextureLoadingEnd, new Vector3f(piece.getWidth(), piece.getHeight(), piece.getDepth()), ModelManager
                .getInstance().getBounds(filledModelChild), new HashSet<Appearance>());
      } else if (piece.getModelMaterials() != null) {
        setColorAndTexture(filledModelNode, null, null, null, piece.getModelMaterials(), waitTextureLoadingEnd,
            new Vector3f(piece.getWidth(), piece.getHeight(), piece.getDepth()),
            ModelManager.getInstance().getBounds(filledModelChild), new HashSet<Appearance>());
      } else {
        // Set default material and texture of model
        setColorAndTexture(filledModelNode, null, null, piece.getShininess(), null, false, null, null,
            new HashSet<Appearance>());
      }
    }
  }

  /**
   * Sets the light color if the piece is a light.
   */
  private void updateLight() {
    HomePieceOfFurniture piece = (HomePieceOfFurniture)getUserData();
    if (piece instanceof HomeLight && this.home != null) {
      boolean enabled = this.home.getEnvironment().getSubpartSizeUnderLight() > 0 && piece.isVisible()
                        && (piece.getLevel() == null || piece.getLevel().isViewableAndVisible());
      HomeLight light = (HomeLight)piece;
      LightSource [] lightSources = light.getLightSources();
      if (numChildren() > 2) {
        Color homeLightColor = new Color(this.home.getEnvironment().getLightColor());
        float homeLightColorRed = homeLightColor.getRed() / 3072f;
        float homeLightColorGreen = homeLightColor.getGreen() / 3072f;
        float homeLightColorBlue = homeLightColor.getBlue() / 3072f;
        float angle = light.getAngle();
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);
        Group lightsBranch = (Group)getChild(2);
        for (int i = 0; i < lightSources.length; i++) {
          LightSource lightSource = lightSources [i];
          Color lightColor = new Color(lightSource.getColor());
          float power = light.getPower();
          PointLight pointLight = (PointLight)lightsBranch.getChild(i);
          pointLight.setColor(new Color3f(lightColor.getRed() / 255f * power + (power > 0
              ? homeLightColorRed
              : 0), lightColor.getGreen() / 255f * power + (power > 0
              ? homeLightColorGreen
              : 0), lightColor.getBlue() / 255f * power + (power > 0
              ? homeLightColorBlue
              : 0)));
          // Compute the position of the light instead of resizing/placing it
          // with a transformation
          // that has some influence on attenuation
          float xLightSourceInLight = -light.getWidth() / 2 + (lightSource.getX() * light.getWidth());
          float yLightSourceInLight = light.getDepth() / 2 - (lightSource.getY() * light.getDepth());
          float lightElevation = light.getGroundElevation();
          pointLight.setPosition(light.getX() + xLightSourceInLight * cos - yLightSourceInLight * sin,
              lightElevation + (lightSource.getZ() * light.getHeight()), light.getY() + xLightSourceInLight * sin
                                                                         + yLightSourceInLight * cos);
          pointLight.setEnable(enabled);
        }

        if (enabled) {
          Bounds bounds = DEFAULT_INFLUENCING_BOUNDS;
          for (Room room : this.home.getRooms()) {
            Level roomLevel = room.getLevel();
            if (light.isAtLevel(roomLevel)) {
              Shape roomShape = getShape(room.getPoints());
              if (roomShape.contains(light.getX(), light.getY())) {
                Rectangle roomBounds = roomShape.getBounds();
                float minElevation = roomLevel != null
                    ? roomLevel.getElevation()
                    : 0;
                float maxElevation = roomLevel != null
                    ? minElevation + roomLevel.getHeight()
                    : 1E7f;
                float epsilon = 0.1f;
                bounds = new BoundingBox(new Point3d(roomBounds.getMinX() - epsilon, minElevation - epsilon,
                    roomBounds.getMinY() - epsilon), new Point3d(roomBounds.getMaxX() + epsilon,
                    maxElevation + epsilon, roomBounds.getMaxY() + epsilon));
                break;
              }
            }
          }
          ((BoundingLeaf)getChild(1)).setRegion(bounds);
        }
      }
    }
  }

  /**
   * Returns the node of the filled model.
   */
  private Node getFilledModelNode() {
    TransformGroup transformGroup = (TransformGroup)getChild(0);
    BranchGroup branchGroup = (BranchGroup)transformGroup.getChild(0);
    return branchGroup.getChild(0);
  }

  /**
   * Returns the node of the outline model.
   */
  private Node getOutlineModelNode() {
    TransformGroup transformGroup = (TransformGroup)getChild(0);
    BranchGroup branchGroup = (BranchGroup)transformGroup.getChild(0);
    if (branchGroup.numChildren() > 1) {
      return branchGroup.getChild(1);
    } else {
      return null;
    }
  }

  /**
   * Sets whether this piece model is visible or not.
   */
  private void updatePieceOfFurnitureVisibility() {
    HomePieceOfFurniture piece = (HomePieceOfFurniture)getUserData();
    Node outlineModelNode = getOutlineModelNode();
    HomeEnvironment.DrawingMode drawingMode;
    if (this.home != null && outlineModelNode != null) {
      drawingMode = this.home.getEnvironment().getDrawingMode();
    } else {
      drawingMode = null;
    }
    // Update visibility of filled model shapes
    boolean visible = piece.isVisible() && (piece.getLevel() == null || piece.getLevel().isViewableAndVisible());
    HomeMaterial [] materials = piece.getColor() == null && piece.getTexture() == null
        ? piece.getModelMaterials()
        : null;
    setVisible(
        getFilledModelNode(),
        visible
            && (drawingMode == null || drawingMode == HomeEnvironment.DrawingMode.FILL || drawingMode == HomeEnvironment.DrawingMode.FILL_AND_OUTLINE),
        materials);
    if (outlineModelNode != null) {
      // Update visibility of outline model shapes
      setVisible(
          outlineModelNode,
          visible
              && (drawingMode == HomeEnvironment.DrawingMode.OUTLINE || drawingMode == HomeEnvironment.DrawingMode.FILL_AND_OUTLINE),
          materials);
    }
  }

  /**
   * Sets whether this piece model is mirrored or not.
   */
  private void updatePieceOfFurnitureModelMirrored() {
    HomePieceOfFurniture piece = (HomePieceOfFurniture)getUserData();
    // Cull front or back model faces whether its model is mirrored or not
    setCullFace(getFilledModelNode(), piece.isModelMirrored(), piece.isBackFaceShown());
  }

  /**
   * Updates transform group children with <code>modelMode</code>.
   */
  private void updatePieceOfFurnitureModelNode(Node modelNode, TransformGroup normalization, boolean ignoreDrawingMode,
                                               boolean waitTextureLoadingEnd) {
    normalization.addChild(modelNode);
    setModelCapabilities(normalization);
    // Add model node to branch group
    BranchGroup modelBranch = new BranchGroup();
    modelBranch.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
    modelBranch.addChild(normalization);
    if (!ignoreDrawingMode) {
      // Add outline model node
      modelBranch.addChild(createOutlineModelNode(normalization));
    }

    TransformGroup transformGroup = (TransformGroup)getChild(0);
    // Remove previous nodes
    transformGroup.removeAllChildren();
    // Add model branch to live scene
    transformGroup.addChild(modelBranch);
    HomePieceOfFurniture piece = (HomePieceOfFurniture)getUserData();
    if (piece.isHorizontallyRotated()) {
      // Update piece transformation to ensure its center is correctly placed
      updatePieceOfFurnitureTransform();
    }

    if (piece instanceof HomeLight) {
      BranchGroup lightBranch = new BranchGroup();
      lightBranch.setCapability(ALLOW_CHILDREN_READ);
      HomeLight light = (HomeLight)piece;
      for (int i = light.getLightSources().length; i > 0; i--) {
        PointLight pointLight = new PointLight(new Color3f(), new Point3f(), new Point3f(0.25f, 0, 0.0000025f));
        pointLight.setCapability(PointLight.ALLOW_POSITION_WRITE);
        pointLight.setCapability(PointLight.ALLOW_COLOR_WRITE);
        pointLight.setCapability(PointLight.ALLOW_STATE_WRITE);
        BoundingLeaf bounds = (BoundingLeaf)getChild(1);
        pointLight.setInfluencingBoundingLeaf(bounds);
        lightBranch.addChild(pointLight);
      }
      addChild(lightBranch);
    }

    // Flip normals if back faces of model are shown
    if (piece.isBackFaceShown()) {
      setBackFaceNormalFlip(getFilledModelNode(), true);
    }
    // Update piece color, visibility and model mirror in dispatch thread as
    // these attributes may be changed in that thread
    updatePieceOfFurnitureModelMirrored();
    updatePieceOfFurnitureColorAndTexture(waitTextureLoadingEnd);
    updateLight();
    updatePieceOfFurnitureVisibility();

    // Manage light sources visibility
    if (this.home != null && getUserData() instanceof Light) {
      this.home.addSelectionListener(new LightSelectionListener(this));
    }
  }

  /**
   * Selection listener bound to this object with a weak reference to avoid
   * strong link between home and this tree.
   */
  private static class LightSelectionListener implements SelectionListener {
    private WeakReference<HomePieceOfFurniture3D> piece;

    public LightSelectionListener(HomePieceOfFurniture3D piece) {
      this.piece = new WeakReference<HomePieceOfFurniture3D>(piece);
    }

    public void selectionChanged(SelectionEvent ev) {
      // If piece 3D was garbage collected, remove this listener from home
      HomePieceOfFurniture3D piece3D = this.piece.get();
      Home home = (Home)ev.getSource();
      if (piece3D == null) {
        home.removeSelectionListener(this);
      } else {
        piece3D.updatePieceOfFurnitureVisibility();
      }
    }
  }

  /**
   * Returns a box that may replace model.
   */
  private Node getModelBox(Color color) {
    Material material = new Material();
    material.setDiffuseColor(new Color3f(color));
    material.setAmbientColor(new Color3f(color.darker()));

    Appearance boxAppearance = new Appearance();
    boxAppearance.setMaterial(material);
    Box box = new Box(0.5f, 0.5f, 0.5f, boxAppearance);
    box.setUserData(DEFAULT_BOX);
    return box;
  }

  /**
   * Returns a clone of the given node with an outline appearance on its shapes.
   */
  private Node createOutlineModelNode(Node modelNode) {
    Node node = ModelManager.getInstance().cloneNode(modelNode);
    setOutlineAppearance(node);
    return node;
  }

  /**
   * Sets the outline appearance on all the children of <code>node</code>.
   */
  private void setOutlineAppearance(Node node) {
    if (node instanceof Group) {
      Enumeration<?> enumeration = ((Group)node).getAllChildren();
      while (enumeration.hasMoreElements()) {
        setOutlineAppearance((Node)enumeration.nextElement());
      }
    } else if (node instanceof Link) {
      setOutlineAppearance(((Link)node).getSharedGroup());
    } else if (node instanceof Shape3D) {
      Appearance outlineAppearance = new Appearance();
      ((Shape3D)node).setAppearance(outlineAppearance);
      outlineAppearance.setCapability(Appearance.ALLOW_RENDERING_ATTRIBUTES_READ);
      RenderingAttributes renderingAttributes = new RenderingAttributes();
      renderingAttributes.setCapability(RenderingAttributes.ALLOW_VISIBLE_WRITE);
      outlineAppearance.setRenderingAttributes(renderingAttributes);
      outlineAppearance.setColoringAttributes(Object3DBranch.OUTLINE_COLORING_ATTRIBUTES);
      outlineAppearance.setPolygonAttributes(Object3DBranch.OUTLINE_POLYGON_ATTRIBUTES);
      outlineAppearance.setLineAttributes(Object3DBranch.OUTLINE_LINE_ATTRIBUTES);
    }
  }

  /**
   * Sets the capabilities to change material and rendering attributes, and to
   * read geometries for all children of <code>node</code>.
   */
  private void setModelCapabilities(Node node) {
    if (node instanceof Group) {
      node.setCapability(Group.ALLOW_CHILDREN_READ);
      if (node instanceof TransformGroup) {
        node.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
      }
      Enumeration<?> enumeration = ((Group)node).getAllChildren();
      while (enumeration.hasMoreElements()) {
        setModelCapabilities((Node)enumeration.nextElement());
      }
    } else if (node instanceof Link) {
      node.setCapability(Link.ALLOW_SHARED_GROUP_READ);
      setModelCapabilities(((Link)node).getSharedGroup());
    } else if (node instanceof Shape3D) {
      Shape3D shape = (Shape3D)node;
      Appearance appearance = shape.getAppearance();
      if (appearance != null) {
        setAppearanceCapabilities(appearance);
      }
      Enumeration<?> enumeration = shape.getAllGeometries();
      while (enumeration.hasMoreElements()) {
        setGeometryCapabilities((Geometry)enumeration.nextElement());
      }
      node.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
      node.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
      node.setCapability(Shape3D.ALLOW_BOUNDS_READ);
    }
  }

  /**
   * Sets the material and texture attribute of all <code>Shape3D</code>
   * children nodes of <code>node</code> from the given <code>color</code> and
   * <code>texture</code>.
   */
  private void setColorAndTexture(Node node, Integer color, HomeTexture texture, Float shininess,
                                  HomeMaterial [] materials, boolean waitTextureLoadingEnd, Vector3f pieceSize,
                                  BoundingBox modelBounds, Set<Appearance> modifiedAppearances) {
    if (node instanceof Group) {
      // Set material and texture of all children
      Enumeration<?> enumeration = ((Group)node).getAllChildren();
      while (enumeration.hasMoreElements()) {
        setColorAndTexture((Node)enumeration.nextElement(), color, texture, shininess, materials,
            waitTextureLoadingEnd, pieceSize, modelBounds, modifiedAppearances);
      }
    } else if (node instanceof Link) {
      setColorAndTexture(((Link)node).getSharedGroup(), color, texture, shininess, materials, waitTextureLoadingEnd,
          pieceSize, modelBounds, modifiedAppearances);
    } else if (node instanceof Shape3D) {
      final Shape3D shape = (Shape3D)node;
      String shapeName = (String)shape.getUserData();
      Appearance appearance = shape.getAppearance();
      if (appearance == null) {
        appearance = createAppearanceWithChangeCapabilities();
        ((Shape3D)node).setAppearance(appearance);
      }

      // Check appearance wasn't already changed
      if (!modifiedAppearances.contains(appearance)) {
        DefaultMaterialAndTexture defaultMaterialAndTexture = null;
        boolean colorModified = color != null;
        boolean textureModified = !colorModified && texture != null;
        boolean materialModified = !colorModified && !textureModified && materials != null && materials.length > 0;
        boolean appearanceModified = colorModified || textureModified || materialModified || shininess != null;
        boolean windowPane = shapeName != null && shapeName.startsWith(ModelManager.WINDOW_PANE_SHAPE_PREFIX);
        float materialShininess = 0;
        if (appearanceModified) {
          // Use appearance user data to store shape default material
          defaultMaterialAndTexture = (DefaultMaterialAndTexture)appearance.getUserData();
          if (defaultMaterialAndTexture == null) {
            defaultMaterialAndTexture = new DefaultMaterialAndTexture(appearance);
            appearance.setUserData(defaultMaterialAndTexture);
          }

          materialShininess = shininess != null
              ? shininess.floatValue()
              : (defaultMaterialAndTexture.getMaterial() != null
                  ? defaultMaterialAndTexture.getMaterial().getShininess() / 128f
                  : 0);
        }
        if (colorModified) {
          // Change color only of shapes that are not window panes
          if (windowPane) {
            restoreDefaultMaterialAndTexture(appearance, materialShininess);
          } else {
            // Change material if no default texture is displayed on the shape
            // (textures always keep the colors of their image file)
            appearance.setMaterial(getMaterial(color, color, materialShininess));
            appearance.setTransparencyAttributes(defaultMaterialAndTexture.getTransparencyAttributes());
            appearance.setPolygonAttributes(defaultMaterialAndTexture.getPolygonAttributes());
            appearance.setTexCoordGeneration(defaultMaterialAndTexture.getTexCoordGeneration());
            appearance.setTextureAttributes(defaultMaterialAndTexture.getTextureAttributes());
            appearance.setTexture(null);
          }
        } else if (textureModified) {
          // Change texture only of shapes that are not window panes
          if (windowPane) {
            restoreDefaultMaterialAndTexture(appearance, materialShininess);
          } else {
            // Change material to white then texture
            appearance.setTexCoordGeneration(getTextureCoordinates(texture, pieceSize, modelBounds));
            appearance.setTextureAttributes(getTextureAttributes(texture, true));
            appearance.setMaterial(getMaterial(DEFAULT_COLOR, DEFAULT_AMBIENT_COLOR, materialShininess));
            TextureManager.getInstance().loadTexture(texture.getImage(), waitTextureLoadingEnd,
                getTextureObserver(appearance));
          }
        } else if (materialModified) {
          String appearanceName = null;
          try {
            appearanceName = appearance.getName();
          } catch (NoSuchMethodError ex) {
            // Don't support HomeMaterial with Java 3D < 1.4 where appearance
            // name was added
          }
          boolean materialFound = false;
          if (appearanceName != null) {
            // Apply color, texture and shininess of the material named as
            // appearance name
            for (HomeMaterial material : materials) {
              if (material != null
                  && (material.getKey() != null && material.getKey().equals(appearanceName) || material.getKey() == null
                                                                                               && material.getName()
                                                                                                   .equals(
                                                                                                       appearanceName))) {
                if (material.getShininess() != null) {
                  materialShininess = material.getShininess();
                }
                color = material.getColor();
                if (color != null && (color.intValue() & 0xFF000000) != 0) {
                  appearance.setMaterial(getMaterial(color, color, materialShininess));
                  appearance.setTexture(null);
                  appearance.setTransparencyAttributes(defaultMaterialAndTexture.getTransparencyAttributes());
                  appearance.setPolygonAttributes(defaultMaterialAndTexture.getPolygonAttributes());
                } else if (color == null && material.getTexture() != null) {
                  HomeTexture materialTexture = material.getTexture();
                  if (isTexturesCoordinatesDefined(shape)) {
                    restoreDefaultTextureCoordinatesGeneration(appearance);
                    appearance.setTextureAttributes(getTextureAttributes(materialTexture));
                  } else {
                    appearance.setTexCoordGeneration(getTextureCoordinates(material.getTexture(), pieceSize,
                        modelBounds));
                    appearance.setTextureAttributes(getTextureAttributes(materialTexture, true));
                  }
                  appearance.setMaterial(getMaterial(DEFAULT_COLOR, DEFAULT_AMBIENT_COLOR, materialShininess));
                  TextureManager.getInstance().loadTexture(materialTexture.getImage(), waitTextureLoadingEnd,
                      getTextureObserver(appearance));
                } else {
                  restoreDefaultMaterialAndTexture(appearance, material.getShininess());
                }
                materialFound = true;
                break;
              }
            }
          }
          if (!materialFound) {
            restoreDefaultMaterialAndTexture(appearance, null);
          }
        } else {
          restoreDefaultMaterialAndTexture(appearance, shininess);
        }
        // Store modified appearances to avoid changing their values more than
        // once
        modifiedAppearances.add(appearance);
      }
    }
  }

  /**
   * Returns a texture observer that will update the given
   * <code>appearance</code>.
   */
  private TextureObserver getTextureObserver(final Appearance appearance) {
    return new TextureManager.TextureObserver() {
      public void textureUpdated(Texture texture) {
        if (TextureManager.getInstance().isTextureTransparent(texture)) {
          appearance.setTransparencyAttributes(DEFAULT_TEXTURED_SHAPE_TRANSPARENCY_ATTRIBUTES);
          DefaultMaterialAndTexture defaultMaterialAndTexture = (DefaultMaterialAndTexture)appearance.getUserData();
          if (defaultMaterialAndTexture != null && defaultMaterialAndTexture.getPolygonAttributes() != null
              && defaultMaterialAndTexture.getPolygonAttributes().getBackFaceNormalFlip()) {
            appearance.setPolygonAttributes(NORMAL_FLIPPED_TEXTURED_SHAPE_POLYGON_ATTRIBUTES);
          } else {
            appearance.setPolygonAttributes(DEFAULT_TEXTURED_SHAPE_POLYGON_ATTRIBUTES);
          }
        } else {
          DefaultMaterialAndTexture defaultMaterialAndTexture = (DefaultMaterialAndTexture)appearance.getUserData();
          if (defaultMaterialAndTexture != null) {
            appearance.setTransparencyAttributes(defaultMaterialAndTexture.getTransparencyAttributes());
            appearance.setPolygonAttributes(defaultMaterialAndTexture.getPolygonAttributes());
          }
        }
        Texture homeTexture = getHomeTextureClone(texture, home);
        if (appearance.getTexture() != homeTexture) {
          appearance.setTexture(homeTexture);
        }
      }
    };
  }

  /**
   * Returns a texture coordinates generator that wraps the given texture on
   * front face.
   */
  private TexCoordGeneration getTextureCoordinates(HomeTexture texture, Vector3f pieceSize, BoundingBox modelBounds) {
    Point3d lower = new Point3d();
    modelBounds.getLower(lower);
    Point3d upper = new Point3d();
    modelBounds.getUpper(upper);
    float minimumSize = ModelManager.getInstance().getMinimumSize();
    float sx = pieceSize.x / (float)Math.max(upper.x - lower.x, minimumSize);
    float sw = texture.isLeftToRightOriented()
        ? (float)-lower.x * sx
        : 0;
    float ty = pieceSize.y / (float)Math.max(upper.y - lower.y, minimumSize);
    float tz = pieceSize.z / (float)Math.max(upper.z - lower.z, minimumSize);
    float tw = texture.isLeftToRightOriented()
        ? (float)(-lower.y * ty + upper.z * tz)
        : 0;
    return new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR, TexCoordGeneration.TEXTURE_COORDINATE_2,
        new Vector4f(sx, 0, 0, sw), new Vector4f(0, ty, -tz, tw));
  }

  /**
   * Returns <code>true</code> if all the geometries of the given
   * <code>shape</code> define some texture coordinates.
   */
  private boolean isTexturesCoordinatesDefined(Shape3D shape) {
    for (int i = 0, n = shape.numGeometries(); i < n; i++) {
      Geometry geometry = shape.getGeometry(i);
      if (geometry instanceof GeometryArray
          && (((GeometryArray)geometry).getVertexFormat() & GeometryArray.TEXTURE_COORDINATE_2) == 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Restores default material and texture of the given <code>appearance</code>.
   */
  private void restoreDefaultMaterialAndTexture(Appearance appearance, Float shininess) {
    DefaultMaterialAndTexture defaultMaterialAndTexture = (DefaultMaterialAndTexture)appearance.getUserData();
    if (defaultMaterialAndTexture != null) {
      Material defaultMaterial = defaultMaterialAndTexture.getMaterial();
      if (defaultMaterial != null && shininess != null) {
        defaultMaterial = (Material)defaultMaterial.cloneNodeComponent(true);
        defaultMaterial.setSpecularColor(new Color3f(shininess, shininess, shininess));
        defaultMaterial.setShininess(shininess * 128);
      }
      appearance.setMaterial(defaultMaterial);
      appearance.setTransparencyAttributes(defaultMaterialAndTexture.getTransparencyAttributes());
      appearance.setPolygonAttributes(defaultMaterialAndTexture.getPolygonAttributes());
      appearance.setTexCoordGeneration(defaultMaterialAndTexture.getTexCoordGeneration());
      appearance.setTexture(getHomeTextureClone(defaultMaterialAndTexture.getTexture(), home));
      appearance.setTextureAttributes(defaultMaterialAndTexture.getTextureAttributes());
    }
  }

  /**
   * Restores default texture coordinates generation of the given
   * <code>appearance</code>.
   */
  private void restoreDefaultTextureCoordinatesGeneration(Appearance appearance) {
    DefaultMaterialAndTexture defaultMaterialAndTexture = (DefaultMaterialAndTexture)appearance.getUserData();
    if (defaultMaterialAndTexture != null) {
      appearance.setTexCoordGeneration(defaultMaterialAndTexture.getTexCoordGeneration());
    }
  }

  /**
   * Sets the visible attribute of the <code>Shape3D</code> children nodes of
   * <code>node</code>.
   */
  private void setVisible(Node node, boolean visible, HomeMaterial [] materials) {
    if (node instanceof Group) {
      // Set visibility of all children
      Enumeration<?> enumeration = ((Group)node).getAllChildren();
      while (enumeration.hasMoreElements()) {
        setVisible((Node)enumeration.nextElement(), visible, materials);
      }
    } else if (node instanceof Link) {
      setVisible(((Link)node).getSharedGroup(), visible, materials);
    } else if (node instanceof Shape3D) {
      final Shape3D shape = (Shape3D)node;
      Appearance appearance = shape.getAppearance();
      if (appearance == null) {
        appearance = createAppearanceWithChangeCapabilities();
        ((Shape3D)node).setAppearance(appearance);
      }
      RenderingAttributes renderingAttributes = appearance.getRenderingAttributes();
      if (renderingAttributes == null) {
        renderingAttributes = new RenderingAttributes();
        renderingAttributes.setCapability(RenderingAttributes.ALLOW_VISIBLE_WRITE);
        appearance.setRenderingAttributes(renderingAttributes);
      }

      String shapeName = (String)shape.getUserData();
      if (visible && shapeName != null && (getUserData() instanceof Light)
          && shapeName.startsWith(ModelManager.LIGHT_SHAPE_PREFIX) && this.home != null
          && !isSelected(this.home.getSelectedItems())) {
        // Don't display light sources shapes of unselected lights
        visible = false;
      }

      if (visible && materials != null) {
        String appearanceName = null;
        try {
          appearanceName = appearance.getName();
        } catch (NoSuchMethodError ex) {
          // Don't support HomeMaterial with Java 3D < 1.4 where appearance name
          // was added
        }
        if (appearanceName != null) {
          // Check whether the material color used by this shape isn't invisible
          for (HomeMaterial material : materials) {
            if (material != null && material.getName().equals(appearanceName)) {
              Integer color = material.getColor();
              visible = color == null || (color.intValue() & 0xFF000000) != 0;
              break;
            }
          }
        }
      }
      // Change visibility
      renderingAttributes.setVisible(visible);
    }
  }

  /**
   * Returns <code>true</code> if this piece of furniture belongs to
   * <code>selectedItems</code>.
   */
  private boolean isSelected(List<? extends Selectable> selectedItems) {
    Object piece = getUserData();
    for (Selectable item : selectedItems) {
      if (item == piece
          || (item instanceof HomeFurnitureGroup && isSelected(((HomeFurnitureGroup)item).getFurniture()))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Sets the cull face of all <code>Shape3D</code> children nodes of
   * <code>node</code>.
   * @param cullFace <code>PolygonAttributes.CULL_FRONT</code> or
   *          <code>PolygonAttributes.CULL_BACK</code>
   */
  private void setCullFace(Node node, boolean mirrored, boolean backFaceShown) {
    if (node instanceof Group) {
      // Set cull face of all children
      Enumeration<?> enumeration = ((Group)node).getAllChildren();
      while (enumeration.hasMoreElements()) {
        setCullFace((Node)enumeration.nextElement(), mirrored, backFaceShown);
      }
    } else if (node instanceof Link) {
      setCullFace(((Link)node).getSharedGroup(), mirrored, backFaceShown);
    } else if (node instanceof Shape3D) {
      Appearance appearance = ((Shape3D)node).getAppearance();
      if (appearance == null) {
        appearance = createAppearanceWithChangeCapabilities();
        ((Shape3D)node).setAppearance(appearance);
      }
      PolygonAttributes polygonAttributes = appearance.getPolygonAttributes();
      if (polygonAttributes == null) {
        polygonAttributes = createPolygonAttributesWithChangeCapabilities();
        appearance.setPolygonAttributes(polygonAttributes);
      }

      // Change cull face
      try {
        int cullFace = polygonAttributes.getCullFace();
        if (cullFace != PolygonAttributes.CULL_NONE) {
          Integer defaultCullFace = (Integer)polygonAttributes.getUserData();
          if (defaultCullFace == null) {
            polygonAttributes.setUserData(defaultCullFace = cullFace);
          }
          polygonAttributes.setCullFace((mirrored ^ backFaceShown ^ defaultCullFace == PolygonAttributes.CULL_FRONT)
              ? PolygonAttributes.CULL_FRONT
              : PolygonAttributes.CULL_BACK);
        }
      } catch (CapabilityNotSetException ex) {
        // Shouldn't happen since capability is set but happens though with Java
        // 3D 1.3
        ex.printStackTrace();
      }
    }
  }

  /**
   * Sets whether all <code>Shape3D</code> children nodes of <code>node</code>
   * should have their normal flipped or not. Caution !!! Should be executed
   * only once per instance
   * @param backFaceNormalFlip <code>true</code> if normals should be flipped.
   */
  private void setBackFaceNormalFlip(Node node, boolean backFaceNormalFlip) {
    if (node instanceof Group) {
      // Set back face normal flip of all children
      Enumeration<?> enumeration = ((Group)node).getAllChildren();
      while (enumeration.hasMoreElements()) {
        setBackFaceNormalFlip((Node)enumeration.nextElement(), backFaceNormalFlip);
      }
    } else if (node instanceof Link) {
      setBackFaceNormalFlip(((Link)node).getSharedGroup(), backFaceNormalFlip);
    } else if (node instanceof Shape3D) {
      Appearance appearance = ((Shape3D)node).getAppearance();
      if (appearance == null) {
        appearance = createAppearanceWithChangeCapabilities();
        ((Shape3D)node).setAppearance(appearance);
      }
      PolygonAttributes polygonAttributes = appearance.getPolygonAttributes();
      if (polygonAttributes == null) {
        polygonAttributes = createPolygonAttributesWithChangeCapabilities();
        appearance.setPolygonAttributes(polygonAttributes);
      }

      // Change back face normal flip
      polygonAttributes.setBackFaceNormalFlip(backFaceNormalFlip
                                              ^ polygonAttributes.getCullFace() == PolygonAttributes.CULL_FRONT);
    }
  }

  private PolygonAttributes createPolygonAttributesWithChangeCapabilities() {
    PolygonAttributes polygonAttributes = new PolygonAttributes();
    polygonAttributes.setCapability(PolygonAttributes.ALLOW_CULL_FACE_READ);
    polygonAttributes.setCapability(PolygonAttributes.ALLOW_CULL_FACE_WRITE);
    polygonAttributes.setCapability(PolygonAttributes.ALLOW_NORMAL_FLIP_READ);
    polygonAttributes.setCapability(PolygonAttributes.ALLOW_NORMAL_FLIP_WRITE);
    return polygonAttributes;
  }

  private Appearance createAppearanceWithChangeCapabilities() {
    Appearance appearance = new Appearance();
    setAppearanceCapabilities(appearance);
    return appearance;
  }

  private void setAppearanceCapabilities(Appearance appearance) {
    // Allow future material and rendering attributes changes
    appearance.setCapability(Appearance.ALLOW_MATERIAL_READ);
    appearance.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
    Material material = appearance.getMaterial();
    if (material != null) {
      material.setCapability(Material.ALLOW_COMPONENT_READ);
    }
    appearance.setCapability(Appearance.ALLOW_RENDERING_ATTRIBUTES_READ);
    appearance.setCapability(Appearance.ALLOW_RENDERING_ATTRIBUTES_WRITE);
    appearance.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
    appearance.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);
    appearance.setCapability(Appearance.ALLOW_TEXGEN_READ);
    appearance.setCapability(Appearance.ALLOW_TEXGEN_WRITE);
    appearance.setCapability(Appearance.ALLOW_TEXTURE_READ);
    appearance.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
    appearance.setCapability(Appearance.ALLOW_TEXTURE_ATTRIBUTES_READ);
    appearance.setCapability(Appearance.ALLOW_TEXTURE_ATTRIBUTES_WRITE);
    appearance.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);
    appearance.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
    PolygonAttributes polygonAttributes = appearance.getPolygonAttributes();
    if (polygonAttributes != null) {
      polygonAttributes.setCapability(PolygonAttributes.ALLOW_CULL_FACE_READ);
      polygonAttributes.setCapability(PolygonAttributes.ALLOW_CULL_FACE_WRITE);
      polygonAttributes.setCapability(PolygonAttributes.ALLOW_NORMAL_FLIP_READ);
      polygonAttributes.setCapability(PolygonAttributes.ALLOW_NORMAL_FLIP_WRITE);
    }
  }

  private void setGeometryCapabilities(Geometry geometry) {
    // Sets the geometry capabilities needed to read attributes saved by
    // OBJWriter
    if (!geometry.isLive() && geometry instanceof GeometryArray) {
      geometry.setCapability(GeometryArray.ALLOW_FORMAT_READ);
      geometry.setCapability(GeometryArray.ALLOW_COUNT_READ);
      geometry.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
      geometry.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
      geometry.setCapability(GeometryArray.ALLOW_NORMAL_READ);
      geometry.setCapability(GeometryArray.ALLOW_TEXCOORD_READ);
    }
  }

  /**
   * A class used to store the default material and texture of a shape.
   */
  private static class DefaultMaterialAndTexture {
    private final Material               material;
    private final TransparencyAttributes transparencyAttributes;
    private final PolygonAttributes      polygonAttributes;
    private final TexCoordGeneration     texCoordGeneration;
    private final Texture                texture;
    private final TextureAttributes      textureAttributes;

    public DefaultMaterialAndTexture(Appearance appearance) {
      this.material = appearance.getMaterial();
      this.transparencyAttributes = appearance.getTransparencyAttributes();
      this.polygonAttributes = appearance.getPolygonAttributes();
      this.texCoordGeneration = appearance.getTexCoordGeneration();
      this.texture = appearance.getTexture();
      this.textureAttributes = appearance.getTextureAttributes();
    }

    public Material getMaterial() {
      return this.material;
    }

    public TransparencyAttributes getTransparencyAttributes() {
      return this.transparencyAttributes;
    }

    public PolygonAttributes getPolygonAttributes() {
      return this.polygonAttributes;
    }

    public TexCoordGeneration getTexCoordGeneration() {
      return this.texCoordGeneration;
    }

    public Texture getTexture() {
      return this.texture;
    }

    public TextureAttributes getTextureAttributes() {
      return this.textureAttributes;
    }
  }

  public void scanHotpointUpdate() {
    HomePieceOfFurniture label = (HomePieceOfFurniture)getUserData();
    String text = label.getName();
    if (text != null) {
      BranchGroup objRoot = new BranchGroup();
      TransformGroup trans = new TransformGroup();
      trans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
      trans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
      BufferedImage image = createImage(text);

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
      TextureLoader myloader4 = new TextureLoader(image, null, 0);

      // 创建纹理
      Texture2D mytext4 = (Texture2D)myloader4.getTexture();
      // int picWidth = mytext4.getWidth();
      // int picHeight = mytext4.getHeight();
      // 将纹理和外观绑定
      Appearance apperarance4 = new Appearance();
      apperarance4.setTexture(mytext4);
      TextureAttributes myTexAtt4 = new TextureAttributes();
      myTexAtt4.setTextureMode(TextureAttributes.MODULATE);
      apperarance4.setTextureAttributes(myTexAtt4);
      Material mat4 = new Material();
      apperarance4.setMaterial(mat4);

      // 一个立方体
      float a = ((float)picWidth) / 30f;
      // float a = 1;
      float x = picWidth / a;// 20f;// 20f;
      float y = 0f;
      float z = picHeight / a;// 20f;
      Box box = new Box(x, z, y, Box.GENERATE_TEXTURE_COORDS, new Appearance());
      box.getShape(Box.FRONT).setAppearance(apperarance1);
      box.getShape(Box.BACK).setAppearance(apperarance4);
      trans.addChild(box);
      objRoot.addChild(trans);

      Transform3D transform = new Transform3D();
      transform.setTranslation(new Vector3d(label.getX(), label.getGroundElevation() + z, label.getY()));
      trans.setTransform(transform);

      objRoot.setName(text);
      objRoot.compile();
      addChild(objRoot);

    }
  }

  private BufferedImage createImage(String str) {
    Integer width = str.length() * 35;
    Integer height = 128;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
    Graphics g = image.getGraphics();
    g.setClip(0, 0, width, height);
    g.setColor(Color.ORANGE);
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