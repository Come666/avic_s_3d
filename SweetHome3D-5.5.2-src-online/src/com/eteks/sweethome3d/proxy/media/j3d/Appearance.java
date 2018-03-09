package com.eteks.sweethome3d.proxy.media.j3d;


import java.util.Hashtable;

import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.NodeComponent;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TextureUnitState;
import javax.media.j3d.TransparencyAttributes;

public class Appearance  {
    private javax.media.j3d.Appearance proxy;
    public Appearance() {
      proxy= new javax.media.j3d.Appearance();
    }


    /**
     * Sets the material object to the specified object.
     * Setting it to null disables lighting.
     * @param material object that specifies the desired material
     * properties
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setMaterial(Material material) {
      proxy.setMaterial(material);
    }

    /**
     * Retrieves the current material object.
     * @return the material object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public Material getMaterial() {
        return proxy.getMaterial();
    }

    /**
     * Sets the coloringAttributes object to the specified object.
     * Setting it to null will result in default attribute usage.
     * @param coloringAttributes object that specifies the desired
     * coloringAttributes parameters
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setColoringAttributes(ColoringAttributes coloringAttributes) {
       proxy.setColoringAttributes(coloringAttributes);
    }

    /**
     * Retrieves the current coloringAttributes object.
     * @return the coloringAttributes object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public ColoringAttributes getColoringAttributes() {
       return proxy.getColoringAttributes();
    }

    /**
     * Sets the transparencyAttributes object to the specified object.
     * Setting it to null will result in default attribute usage.
     * @param transparencyAttributes object that specifies the desired
     * transparencyAttributes parameters
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setTransparencyAttributes(TransparencyAttributes transparencyAttributes) {
        proxy.setTransparencyAttributes(transparencyAttributes);
    }

    /**
     * Retrieves the current transparencyAttributes object.
     * @return the transparencyAttributes object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public TransparencyAttributes getTransparencyAttributes() {
        return proxy.getTransparencyAttributes();
    }

    /**
     * Sets the renderingAttributes object to the specified object.
     * Setting it to null will result in default attribute usage.
     * @param renderingAttributes object that specifies the desired
     * renderingAttributes parameters
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setRenderingAttributes(RenderingAttributes renderingAttributes) {
       proxy.setRenderingAttributes(renderingAttributes);
    }

    /**
     * Retrieves the current renderingAttributes object.
     * @return the renderingAttributes object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public RenderingAttributes getRenderingAttributes() {
        return proxy.getRenderingAttributes();
    }

    /**
     * Sets the polygonAttributes object to the specified object.
     * Setting it to null will result in default attribute usage.
     * @param polygonAttributes object that specifies the desired
     * polygonAttributes parameters
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setPolygonAttributes(PolygonAttributes polygonAttributes) {
       proxy.setPolygonAttributes(polygonAttributes);
    }

    /**
     * Retrieves the current polygonAttributes object.
     * @return the polygonAttributes object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public PolygonAttributes getPolygonAttributes() {
       return proxy.getPolygonAttributes();
    }

    /**
     * Sets the lineAttributes object to the specified object.
     * Setting it to null will result in default attribute usage.
     * @param lineAttributes object that specifies the desired
     * lineAttributes parameters
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setLineAttributes(LineAttributes lineAttributes) {
       proxy.setLineAttributes(lineAttributes);
    }

    /**
     * Retrieves the current lineAttributes object.
     * @return the lineAttributes object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public LineAttributes getLineAttributes() {
         return proxy.getLineAttributes();
    }

    /**
     * Sets the pointAttributes object to the specified object.
     * Setting it to null will result in default attribute usage.
     * @param pointAttributes object that specifies the desired
     * pointAttributes parameters
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setPointAttributes(PointAttributes pointAttributes) {
       proxy.setPointAttributes(pointAttributes);
    }

    /**
     * Retrieves the current pointAttributes object.
     * @return the pointAttributes object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public PointAttributes getPointAttributes() {
      return proxy.getPointAttributes();
    }

    /**
     * Sets the texture object to the specified object.
     * Setting it to null disables texture mapping.
     *
     * <p>
     * Applications must not set individual texture component objects
     * (texture, textureAttributes, or texCoordGeneration) and
     * the texture unit state array in the same Appearance object.
     * Doing so will result in an exception being thrown.
     *
     * @param texture object that specifies the desired texture
     * map and texture parameters
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @exception IllegalStateException if the specified texture
     * object is non-null and the texture unit state array in this
     * appearance object is already non-null.
     *
     * @exception IllegalSharingException if this Appearance is live and
     * the specified texture refers to an ImageComponent2D that is being used
     * by a Canvas3D as an off-screen buffer.
     *
     * @exception IllegalSharingException if this Appearance is
     * being used by an immediate mode context and
     * the specified texture refers to an ImageComponent2D that is being used
     * by a Canvas3D as an off-screen buffer.
     */
    public void setTexture(Texture texture) {
       proxy.setTexture(texture);
    }

    /**
     * Retrieves the current texture object.
     * @return the texture object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public Texture getTexture() {
       return proxy.getTexture();
    }

    /**
     * Sets the textureAttributes object to the specified object.
     * Setting it to null will result in default attribute usage.
     *
     * <p>
     * Applications must not set individual texture component objects
     * (texture, textureAttributes, or texCoordGeneration) and
     * the texture unit state array in the same Appearance object.
     * Doing so will result in an exception being thrown.
     *
     * @param textureAttributes object that specifies the desired
     * textureAttributes map and textureAttributes parameters
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @exception IllegalStateException if the specified textureAttributes
     * object is non-null and the texture unit state array in this
     * appearance object is already non-null.
     */
    public void setTextureAttributes(TextureAttributes textureAttributes) {
         proxy.setTextureAttributes(textureAttributes);
    }

    /**
     * Retrieves the current textureAttributes object.
     * @return the textureAttributes object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public TextureAttributes getTextureAttributes() {
         return proxy.getTextureAttributes();
    }

    /**
     * Sets the texCoordGeneration object to the specified object.
     * Setting it to null disables texture coordinate generation.
     *
     * <p>
     * Applications must not set individual texture component objects
     * (texture, textureAttributes, or texCoordGeneration) and
     * the texture unit state array in the same Appearance object.
     * Doing so will result in an exception being thrown.
     *
     * @param texCoordGeneration object that specifies the texture coordinate
     * generation parameters
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @exception IllegalStateException if the specified texCoordGeneration
     * object is non-null and the texture unit state array in this
     * appearance object is already non-null.
     */
    public void setTexCoordGeneration(TexCoordGeneration texCoordGeneration) {
       proxy.setTexCoordGeneration(texCoordGeneration);
    }

    /**
     * Retrieves the current texCoordGeneration object.
     * @return the texCoordGeneration object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public TexCoordGeneration getTexCoordGeneration() {
       return proxy.getTexCoordGeneration();
    }

    /**
     * Sets the texture unit state array for this appearance object to the
     * specified array.  A shallow copy of the array of references to
     * the TextureUnitState objects is made.  If the specified array
     * is null or if the length of the array is 0, multi-texture is
     * disabled.  Within the array, a null TextureUnitState element
     * disables the corresponding texture unit.
     *
     * <p>
     * Applications must not set individual texture component objects
     * (texture, textureAttributes, or texCoordGeneration) and
     * the texture unit state array in the same Appearance object.
     * Doing so will result in an exception being thrown.
     *
     * @param stateArray array of TextureUnitState objects that
     * specify the desired texture state for each unit.  The length of
     * this array specifies the maximum number of texture units that
     * will be used by this appearance object.  The texture units are
     * numbered from <code>0</code> through
     * <code>stateArray.length-1</code>.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @exception IllegalStateException if the specified array is
     * non-null and any of the texture object, textureAttributes
     * object, or texCoordGeneration object in this appearance object
     * is already non-null.
     *
     * @exception IllegalSharingException if this Appearance is live and
     * any of the specified textures refers to an ImageComponent2D that is
     * being used by a Canvas3D as an off-screen buffer.
     *
     * @exception IllegalSharingException if this Appearance is
     * being used by an immediate mode context and
     * any of the specified textures refers to an ImageComponent2D that is
     * being used by a Canvas3D as an off-screen buffer.
     *
     * @since Java 3D 1.2
     */
    public void setTextureUnitState(TextureUnitState[] stateArray) {
        proxy.setTextureUnitState(stateArray);
    }

    /**
     * Sets the texture unit state object at the specified index
     * within the texture unit state array to the specified object.
     * If the specified object is null, the corresponding texture unit
     * is disabled.  The index must be within the range
     * <code>[0,&nbsp;stateArray.length-1]</code>.
     *
     * @param index the array index of the object to be set
     *
     * @param state new texture unit state object
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception NullPointerException if the texture unit state array is
     * null.
     * @exception ArrayIndexOutOfBoundsException if <code>index >=
     * stateArray.length</code>.
     *
     * @exception IllegalSharingException if this Appearance is live and
     * the specified texture refers to an ImageComponent2D that is being used
     * by a Canvas3D as an off-screen buffer.
     *
     * @exception IllegalSharingException if this Appearance is
     * being used by an immediate mode context and
     * the specified texture refers to an ImageComponent2D that is being used
     * by a Canvas3D as an off-screen buffer.
     *
     * @since Java 3D 1.2
     */
    public void setTextureUnitState(int index, TextureUnitState state) {
      proxy.setTextureUnitState(index, state);
    }

    /**
     * Retrieves the array of texture unit state objects from this
     * Appearance object.  A shallow copy of the array of references to
     * the TextureUnitState objects is returned.
     *
     * @return the array of texture unit state objects
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public TextureUnitState[] getTextureUnitState() {
        return proxy.getTextureUnitState();
    }

    /**
     * Retrieves the texture unit state object at the specified
     * index within the texture unit state array.  The index must be
     * within the range <code>[0,&nbsp;stateArray.length-1]</code>.
     *
     * @param index the array index of the object to be retrieved
     *
     * @return the texture unit state object at the specified index
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public TextureUnitState getTextureUnitState(int index) {
       return proxy.getTextureUnitState(index);
    }

    /**
     * Retrieves the length of the texture unit state array from
     * this appearance object.  The length of this array specifies the
     * maximum number of texture units that will be used by this
     * appearance object.  If the array is null, a count of 0 is
     * returned.
     *
     * @return the length of the texture unit state array
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public int getTextureUnitCount() {
       return proxy.getTextureUnitCount();
    }


   /**
     * @deprecated replaced with cloneNodeComponent(boolean forceDuplicate)
     */
    public NodeComponent cloneNodeComponent() {
        return proxy.cloneNodeComponent();
    }

    /**
     * NOTE: Applications should <i>not</i> call this method directly.
     * It should only be called by the cloneNode method.
     *
     * @deprecated replaced with duplicateNodeComponent(
     *  NodeComponent originalNodeComponent, boolean forceDuplicate)
     */
    public void duplicateNodeComponent(NodeComponent originalNodeComponent) {
        proxy.duplicateNodeComponent(originalNodeComponent);
    }

   /**
     * Copies all Appearance information from
     * <code>originalNodeComponent</code> into
     * the current node.  This method is called from the
     * <code>cloneNode</code> method which is, in turn, called by the
     * <code>cloneTree</code> method.<P>
     *
     * @param originalNodeComponent the original node to duplicate.
     * @param forceDuplicate when set to <code>true</code>, causes the
     *  <code>duplicateOnCloneTree</code> flag to be ignored.  When
     *  <code>false</code>, the value of each node's
     *  <code>duplicateOnCloneTree</code> variable determines whether
     *  NodeComponent data is duplicated or copied.
     *
     * @exception RestrictedAccessException if this object is part of a live
     *  or compiled scenegraph.
     *
     * @see Node#cloneTree
     * @see NodeComponent#setDuplicateOnCloneTree
     */
    void duplicateAttributes(NodeComponent originalNodeComponent,
                             boolean forceDuplicate) {
      proxy.duplicateNodeComponent(originalNodeComponent, forceDuplicate);
    }


}
