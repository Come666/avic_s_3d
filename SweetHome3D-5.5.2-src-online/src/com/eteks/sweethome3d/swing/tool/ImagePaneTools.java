package com.eteks.sweethome3d.swing.tool;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztc on 15-11-13.
 */
public class ImagePaneTools {
  public static List<File> fileBuffer = new ArrayList<File>();

  public static void addFile(File f) {
    if (!fileBuffer.contains(f))
      fileBuffer.add(f);
  }

  public static File getNext(File f) {
    if (fileBuffer.indexOf(f) + 1 < fileBuffer.size())
      return fileBuffer.get(fileBuffer.indexOf(f) + 1);
    else
      return fileBuffer.get(0);
  }

  public static File getLast(File f) {
    if (fileBuffer.indexOf(f) - 1 >= 0)
      return fileBuffer.get(fileBuffer.indexOf(f) - 1);
    else
      return fileBuffer.get(fileBuffer.size() - 1);
  }

  public static FileFilter myFilter() {
    FileFilter ff = new FileFilter() {
      @Override
      public boolean accept(File f) {
        String name = f.getName();
        if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif") || name.endsWith(".png")
            || name.endsWith(".bmp") || !name.contains("."))
          return true;
        return false;
      }

      @Override
      public String getDescription() {
        return "*.jpg|*.jpeg|*.gif|*.bmp";
      }
    };
    return ff;
  }

  public static FilenameFilter myFilenameFilter() {
    FilenameFilter ff = new FilenameFilter() {
      public boolean accept(File dir, String name) {
        if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif") || name.endsWith(".png")
            || name.endsWith(".bmp"))
          return true;
        return false;
      }
    };
    return ff;
  }
}
