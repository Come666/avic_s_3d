package com.eteks.sweethome3d.model;

import com.eteks.sweethome3d.model.Label.Property;

public class LabelImage extends Label {
  private String imageName;

  public LabelImage(String text, float x, float y, String imageFilePath) {
    super(text, x, y);
    this.imageName = imageFilePath;
  }

  public String getImageName() {
    return imageName;
  }

  public void setImageName(String imageName) {
    if (imageName != this.imageName) {
      String oldImageName = imageName;
      this.imageName = imageName;
      this.propertyChangeSupport.firePropertyChange(Property.IMAGE_NAME.name(), oldImageName, imageName);
    }
  }

  private static final long serialVersionUID = 1L;

  /**
   * Returns a clone of this label.
   */
  @Override
  public LabelImage clone() {

    LabelImage clone = (LabelImage)super.clone();
    clone.setProperty("text", this.text);
    clone.setProperty("x", String.valueOf(this.x));
    clone.setProperty("y", String.valueOf(this.y));
    clone.setProperty("color", String.valueOf(this.color));
    clone.setProperty("outlineColor", String.valueOf(this.outlineColor));
    clone.setProperty("angle", String.valueOf(this.angle));
    clone.setProperty("pitch", String.valueOf(this.pitch));
    clone.setProperty("elevation", String.valueOf(this.elevation));
    clone.setProperty("level", String.valueOf(this.level));
    clone.setProperty("imageName", String.valueOf(this.imageName));
    clone.setProperty("level", String.valueOf(this.level));

    return clone;
  }
}
