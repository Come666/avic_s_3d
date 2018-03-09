/*
 * ImportedFurnitureWizardTest.java 4 juil. 2007
 * 
 * Copyright (c) 2007 Emmanuel PUYBARET / eTeks <info@eteks.com>. All Rights
 * Reserved.
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
package com.eteks.sweethome3d.junit;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.media.j3d.BranchGroup;
import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.vecmath.Vector3f;

import junit.extensions.abbot.ComponentTestFixture;
import abbot.finder.AWTHierarchy;
import abbot.finder.BasicFinder;
import abbot.finder.ComponentSearchException;
import abbot.finder.matchers.ClassMatcher;
import abbot.tester.JComponentTester;

import com.eteks.sweethome3d.io.FileUserPreferences;
import com.eteks.sweethome3d.j3d.ModelManager;
import com.eteks.sweethome3d.j3d.OBJWriter;
import com.eteks.sweethome3d.model.CatalogPieceOfFurniture;
import com.eteks.sweethome3d.model.CollectionEvent;
import com.eteks.sweethome3d.model.CollectionListener;
import com.eteks.sweethome3d.model.Content;
import com.eteks.sweethome3d.model.FurnitureCatalog;
import com.eteks.sweethome3d.model.FurnitureCategory;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.RecorderException;
import com.eteks.sweethome3d.model.Selectable;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.ColorButton;
import com.eteks.sweethome3d.swing.HomePane;
import com.eteks.sweethome3d.swing.ImportedFurnitureWizardStepsPanel;
import com.eteks.sweethome3d.swing.SwingViewFactory;
import com.eteks.sweethome3d.swing.WizardPane;
import com.eteks.sweethome3d.tools.OperatingSystem;
import com.eteks.sweethome3d.tools.TemporaryURLContent;
import com.eteks.sweethome3d.tools.URLContent;
import com.eteks.sweethome3d.viewcontroller.ContentManager;
import com.eteks.sweethome3d.viewcontroller.HomeController;
import com.eteks.sweethome3d.viewcontroller.ImportedFurnitureWizardController;
import com.eteks.sweethome3d.viewcontroller.View;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

/**
 * Tests imported furniture wizard.
 * @author Emmanuel Puybaret
 */
public class ImportedFurnitureWizardTest extends ComponentTestFixture {
  public void testImportFurnitureWizard() throws ComponentSearchException, InterruptedException, NoSuchFieldException,
      IllegalAccessException, InvocationTargetException {
    String language = Locale.getDefault().getLanguage();
    final UserPreferences preferences = new FileUserPreferences();
    // Ensure we use default language and centimeter unit
    preferences.setLanguage(language);
    preferences.setUnit(LengthUnit.CENTIMETER);
    final ViewFactory viewFactory = new SwingViewFactory();
    // final URL testedModelName =
    // ImportedFurnitureWizardTest.class.getResource("resources/test.obj");
    final URL testedModelName = ImportedFurnitureWizardTest.class
        .getResource("/Users/gouyunfei/Documents/tools/apache-tomcat-7.0.65/webapps/examples/models/obj/tree.obj");

    // Create a dummy content manager
    final ContentManager contentManager = new ContentManager() {
      public Content getContent(String contentName) throws RecorderException {
        try {
          // Let's consider contentName is a URL
          return new URLContent(new URL(contentName));
        } catch (IOException ex) {
          fail();
          return null;
        }
      }

      public String getPresentationName(String contentName, ContentType contentType) {
        return "test";
      }

      public boolean isAcceptable(String contentName, ContentType contentType) {
        return true;
      }

      public String showOpenDialog(View parentView, String dialogTitle, ContentType contentType) {
        // Return tested model name URL
        return testedModelName.toString();
      }

      public String showSaveDialog(View parentView, String dialogTitle, ContentType contentType, String name) {
        return null;
      }
    };
    Home home = new Home();
    final HomeController controller = new HomeController(home, preferences, viewFactory, contentManager);
    final JComponent homeView = (JComponent)controller.getView();
    final List<CatalogPieceOfFurniture> addedCatalogFurniture = new ArrayList<CatalogPieceOfFurniture>();
    preferences.getFurnitureCatalog().addFurnitureListener(new CollectionListener<CatalogPieceOfFurniture>() {
      public void collectionChanged(CollectionEvent<CatalogPieceOfFurniture> ev) {
        addedCatalogFurniture.add(ev.getItem());
      }
    });

    // 1. Create a frame that displays a home view
    JFrame frame = new JFrame("Imported Furniture Wizard Test");
    frame.add(homeView);
    frame.pack();

    // Show home plan frame
    showWindow(frame);
    JComponentTester tester = new JComponentTester();
    tester.waitForIdle();

    // 2. Transfer focus to plan view
    ((JComponent)controller.getPlanController().getView()).requestFocusInWindow();
    tester.waitForIdle();

    // Check plan view has focus
    assertTrue("Plan component doesn't have the focus",
        ((JComponent)controller.getPlanController().getView()).isFocusOwner());
    // Open wizard to import a test object (1 width x 2 depth x 3 height) in
    // plan
    tester.invokeLater(new Runnable() {
      public void run() {
        // Display dialog box later in Event Dispatch Thread to avoid blocking
        // test thread
        homeView.getActionMap().get(HomePane.ActionType.IMPORT_FURNITURE).actionPerformed(null);
      }
    });
    // Wait for import furniture view to be shown
    tester.waitForFrameShowing(new AWTHierarchy(),
        preferences.getLocalizedString(ImportedFurnitureWizardController.class, "importFurnitureWizard.title"));
    // Check dialog box is displayed
    JDialog wizardDialog = (JDialog)TestUtilities.findComponent(frame, JDialog.class);
    assertTrue("Wizard view dialog not showing", wizardDialog.isShowing());

    // Retrieve ImportedFurnitureWizardStepsPanel components
    ImportedFurnitureWizardStepsPanel panel = (ImportedFurnitureWizardStepsPanel)TestUtilities.findComponent(frame,
        ImportedFurnitureWizardStepsPanel.class);
    final JButton modelChoiceOrChangeButton = (JButton)TestUtilities.getField(panel, "modelChoiceOrChangeButton");
    final JButton turnLeftButton = (JButton)TestUtilities.getField(panel, "turnLeftButton");
    final JButton turnDownButton = (JButton)TestUtilities.getField(panel, "turnDownButton");
    JCheckBox backFaceShownCheckBox = (JCheckBox)TestUtilities.getField(panel, "backFaceShownCheckBox");
    final JTextField nameTextField = (JTextField)TestUtilities.getField(panel, "nameTextField");
    final JCheckBox addToCatalogCheckBox = (JCheckBox)TestUtilities.getField(panel, "addToCatalogCheckBox");
    final JComboBox categoryComboBox = (JComboBox)TestUtilities.getField(panel, "categoryComboBox");
    final JSpinner widthSpinner = (JSpinner)TestUtilities.getField(panel, "widthSpinner");
    JSpinner heightSpinner = (JSpinner)TestUtilities.getField(panel, "heightSpinner");
    JSpinner depthSpinner = (JSpinner)TestUtilities.getField(panel, "depthSpinner");
    final JCheckBox keepProportionsCheckBox = (JCheckBox)TestUtilities.getField(panel, "keepProportionsCheckBox");
    JSpinner elevationSpinner = (JSpinner)TestUtilities.getField(panel, "elevationSpinner");
    JCheckBox movableCheckBox = (JCheckBox)TestUtilities.getField(panel, "movableCheckBox");
    JCheckBox doorOrWindowCheckBox = (JCheckBox)TestUtilities.getField(panel, "doorOrWindowCheckBox");
    ColorButton colorButton = (ColorButton)TestUtilities.getField(panel, "colorButton");
    final JButton clearColorButton = (JButton)TestUtilities.getField(panel, "clearColorButton");

    // Check current step is model
    tester.waitForIdle();
    assertStepShowing(panel, true, false, false, false);

    // 3. Choose tested model
    String modelChoiceOrChangeButtonText = modelChoiceOrChangeButton.getText();
    tester.invokeAndWait(new Runnable() {
      public void run() {
        modelChoiceOrChangeButton.doClick();
      }
    });
    // Wait 1 s to let time to Java 3D to load the model
    Thread.sleep(1000);
    // Check choice button text changed
    assertFalse("Choice button text didn't change",
        modelChoiceOrChangeButtonText.equals(modelChoiceOrChangeButton.getText()));
    // Click on next button
    WizardPane view = (WizardPane)TestUtilities.findComponent(frame, WizardPane.class);
    // Retrieve wizard view next button
    final JButton nextFinishOptionButton = (JButton)TestUtilities.getField(view, "nextFinishOptionButton");
    assertTrue("Next button isn't enabled", nextFinishOptionButton.isEnabled());
    tester.invokeAndWait(new Runnable() {
      public void run() {
        nextFinishOptionButton.doClick();
      }
    });
    // Check current step is rotation
    assertStepShowing(panel, false, true, false, false);
    // Check back face shown check box isn't selected by default
    assertFalse("Back face shown check box is selected", backFaceShownCheckBox.isSelected());

    // 4. Click on left button
    float width = (Float)widthSpinner.getValue();
    float depth = (Float)depthSpinner.getValue();
    float height = (Float)heightSpinner.getValue();
    tester.invokeAndWait(new Runnable() {
      public void run() {
        turnLeftButton.doClick();
      }
    });
    // Check depth and width values were swapped
    float newWidth = (Float)widthSpinner.getValue();
    float newDepth = (Float)depthSpinner.getValue();
    float newHeight = (Float)heightSpinner.getValue();
    TestUtilities.assertEqualsWithinEpsilon("Incorrect width", depth, newWidth, 1E-3f);
    TestUtilities.assertEqualsWithinEpsilon("Incorrect depth", width, newDepth, 1E-3f);
    TestUtilities.assertEqualsWithinEpsilon("Incorrect height", height, newHeight, 1E-3f);
    // Click on down button
    width = newWidth;
    depth = newDepth;
    height = newHeight;
    tester.invokeAndWait(new Runnable() {
      public void run() {
        turnDownButton.doClick();
      }
    });
    // Check height and depth values were swapped
    newWidth = (Float)widthSpinner.getValue();
    newDepth = (Float)depthSpinner.getValue();
    newHeight = (Float)heightSpinner.getValue();
    TestUtilities.assertEqualsWithinEpsilon("Incorrect width", width, newWidth, 1E-3f);
    TestUtilities.assertEqualsWithinEpsilon("Incorrect depth", height, newDepth, 1E-3f);
    TestUtilities.assertEqualsWithinEpsilon("Incorrect height", depth, newHeight, 1E-3f);

    // 5. Click on next button
    assertTrue("Next button isn't enabled", nextFinishOptionButton.isEnabled());
    tester.invokeAndWait(new Runnable() {
      public void run() {
        nextFinishOptionButton.doClick();
      }
    });
    // Check current step is attributes
    assertStepShowing(panel, false, false, true, false);

    // 6. Check default furniture name is the presentation name proposed by
    // content manager
    assertEquals("Wrong default name",
        contentManager.getPresentationName(testedModelName.toString(), ContentManager.ContentType.MODEL),
        nameTextField.getText());
    // Check Add to catalog check box isn't selected and category combo box
    // is disabled when furniture is imported in home
    assertFalse("Add to catalog check box is selected", addToCatalogCheckBox.isSelected());
    assertFalse("Category combo box isn't disabled", categoryComboBox.isEnabled());
    // Check default category is first category
    final FurnitureCategory firstCategory = preferences.getFurnitureCatalog().getCategories().get(0);
    assertEquals("Wrong default category", firstCategory, categoryComboBox.getSelectedItem());
    // Rename furniture with an empty name
    tester.invokeAndWait(new Runnable() {
      public void run() {
        nameTextField.setText("");
      }
    });
    // Check next button is disabled
    assertFalse("Next button isn't disabled", nextFinishOptionButton.isEnabled());
    // Select Add to catalog check box
    tester.invokeAndWait(new Runnable() {
      public void run() {
        addToCatalogCheckBox.setSelected(true);
      }
    });
    // Check next button is disabled because imported furniture has a wrong name
    assertFalse("Next button isn't disabled", nextFinishOptionButton.isEnabled());
    // Rename furniture and its category
    final String pieceTestName = "#@" + System.currentTimeMillis() + "@#";
    final String categoryTestName = "sdfghjkl";
    tester.invokeAndWait(new Runnable() {
      public void run() {
        nameTextField.setText(pieceTestName);
        categoryComboBox.getEditor().selectAll();
      }
    });
    tester.actionKeyString(categoryComboBox.getEditor().getEditorComponent(), categoryTestName);
    // Check next button is enabled again
    assertTrue("Next button isn't enabled", nextFinishOptionButton.isEnabled());

    // 7. Check keep proportions check box is selected by default
    assertTrue("Keep proportions check box isn't selected", keepProportionsCheckBox.isSelected());
    // Change width with a value 10 times greater
    width = newWidth;
    depth = newDepth;
    height = newHeight;
    final float enteredWidth = newWidth * 10;
    tester.invokeAndWait(new Runnable() {
      public void run() {
        widthSpinner.setValue(enteredWidth);
      }
    });
    // Check height and depth values are 10 times greater
    newWidth = (Float)widthSpinner.getValue();
    newDepth = (Float)depthSpinner.getValue();
    newHeight = (Float)heightSpinner.getValue();
    TestUtilities.assertEqualsWithinEpsilon("Incorrect width", 10 * width, newWidth, 1E-3f);
    TestUtilities.assertEqualsWithinEpsilon("Incorrect depth", 10 * depth, newDepth, 1E-3f);
    TestUtilities.assertEqualsWithinEpsilon("Incorrect height", 10 * height, newHeight, 1E-3f);
    // Deselect keep proportions check box
    tester.invokeAndWait(new Runnable() {
      public void run() {
        keepProportionsCheckBox.setSelected(false);
      }
    });
    // Change width with a value 2 times greater
    width = newWidth;
    depth = newDepth;
    height = newHeight;
    final float twiceValue = newWidth * 2;
    tester.invokeAndWait(new Runnable() {
      public void run() {
        widthSpinner.setValue(twiceValue);
      }
    });
    // Check height and depth values didn't change
    newWidth = (Float)widthSpinner.getValue();
    newDepth = (Float)depthSpinner.getValue();
    newHeight = (Float)heightSpinner.getValue();
    TestUtilities.assertEqualsWithinEpsilon("Incorrect width", 2 * width, newWidth, 1E-3f);
    TestUtilities.assertEqualsWithinEpsilon("Incorrect depth", depth, newDepth, 1E-3f);
    TestUtilities.assertEqualsWithinEpsilon("Incorrect height", height, newHeight, 1E-3f);

    // 8. Change elevation, movable, door or window, color default values
    assertEquals("Wrong default elevation", 0f, (Float)elevationSpinner.getValue());
    elevationSpinner.setValue(10);
    assertTrue("Movable check box isn't selected", movableCheckBox.isSelected());
    movableCheckBox.setSelected(false);
    assertFalse("Door or window check box is selected", doorOrWindowCheckBox.isSelected());
    doorOrWindowCheckBox.setSelected(true);
    assertEquals("Wrong default color", null, colorButton.getColor());
    assertFalse("Clear color button isn't disabled", clearColorButton.isEnabled());
    // Change color
    colorButton.setColor(0x000000);
    // Check clear color button is enabled
    assertTrue("Clear color button isn't enabled", clearColorButton.isEnabled());
    // Click on clear color button
    tester.invokeAndWait(new Runnable() {
      public void run() {
        clearColorButton.doClick();
      }
    });
    // Check color is null and clear color button is disabled
    assertEquals("Wrong color", null, colorButton.getColor());
    assertFalse("Clear color button isn't disabled", clearColorButton.isEnabled());

    // 9. Click on next button
    tester.invokeAndWait(new Runnable() {
      public void run() {
        nextFinishOptionButton.doClick();
      }
    });
    // Check current step is icon
    assertStepShowing(panel, false, false, false, true);
    tester.invokeAndWait(new Runnable() {
      public void run() {
        // Click on Finish to hide dialog box in Event Dispatch Thread
        nextFinishOptionButton.doClick();
      }
    });

    // 10. Check the matching new catalog piece of furniture was created
    List<CatalogPieceOfFurniture> selectedCatalogFurniture = controller.getFurnitureCatalogController()
        .getSelectedFurniture();
    assertEquals("Wrong selected furniture count in catalog", 0, selectedCatalogFurniture.size());
    assertEquals("Incorrect count of created catalog piece", 1, addedCatalogFurniture.size());
    CatalogPieceOfFurniture catalogPiece = addedCatalogFurniture.get(0);
    assertEquals("Wrong catalog piece name", pieceTestName, catalogPiece.getName());
    assertEquals("Wrong catalog piece category name", categoryTestName, catalogPiece.getCategory().getName());
    assertTrue("Catalog doesn't contain new piece",
        preferences.getFurnitureCatalog().getCategories().contains(catalogPiece.getCategory()));
    TestUtilities.assertEqualsWithinEpsilon("Incorrect width", newWidth, catalogPiece.getWidth(), 1E-3f);
    TestUtilities.assertEqualsWithinEpsilon("Incorrect depth", newDepth, catalogPiece.getDepth(), 1E-3f);
    TestUtilities.assertEqualsWithinEpsilon("Incorrect height", newHeight, catalogPiece.getHeight(), 1E-3f);
    TestUtilities.assertEqualsWithinEpsilon("Incorrect elevation", 10, catalogPiece.getElevation(), 1E-3f);
    assertFalse("Catalog piece is movable", catalogPiece.isMovable());
    assertTrue("Catalog piece isn't a door or window", catalogPiece.isDoorOrWindow());
    assertEquals("Wrong catalog piece color", null, catalogPiece.getColor());
    assertTrue("Catalog piece isn't modifiable", catalogPiece.isModifiable());

    // Check a new home piece of furniture was created and it's the selected
    // piece in home
    List<Selectable> homeSelectedItems = home.getSelectedItems();
    assertEquals("Wrong selected furniture count in home", 1, homeSelectedItems.size());
    HomePieceOfFurniture homePiece = (HomePieceOfFurniture)homeSelectedItems.get(0);
    assertEquals("Wrong home piece name", pieceTestName, homePiece.getName());

    // 11. Transfer focus to tree
    JComponent catalogView = (JComponent)controller.getFurnitureCatalogController().getView();
    tester.focus(catalogView);
    // Check plan view has focus
    assertTrue("Catalog tree doesn't have the focus", catalogView.isFocusOwner());
    // Select the piece added to catalog
    controller.getFurnitureCatalogController().setSelectedFurniture(addedCatalogFurniture);
    // Delete new catalog piece of furniture
    final Action deleteAction = homeView.getActionMap().get(HomePane.ActionType.DELETE);
    assertTrue("Delete action isn't enable", deleteAction.isEnabled());
    tester.invokeLater(new Runnable() {
      public void run() {
        deleteAction.actionPerformed(null);
      }
    });
    // Wait for confirm dialog to be shown
    tester.waitForFrameShowing(new AWTHierarchy(),
        preferences.getLocalizedString(HomePane.class, "confirmDeleteCatalogSelection.title"));
    // Find displayed dialog box
    JDialog confirmDeleteCatalogSelectionDialog = (JDialog)new BasicFinder().find(frame, new ClassMatcher(
        JDialog.class, true));
    // Click on Ok in dialog box
    final JOptionPane optionPane = (JOptionPane)TestUtilities.findComponent(confirmDeleteCatalogSelectionDialog,
        JOptionPane.class);
    tester.invokeAndWait(new Runnable() {
      public void run() {
        // Select delete option to hide dialog box in Event Dispatch Thread
        optionPane.setValue(preferences.getLocalizedString(HomePane.class, "confirmDeleteCatalogSelection.delete"));
      }
    });
    // Check selection is empty
    selectedCatalogFurniture = controller.getFurnitureCatalogController().getSelectedFurniture();
    assertTrue("Catalog selected furniture isn't empty", selectedCatalogFurniture.isEmpty());
    // Check catalog doesn't contain the new piece
    assertFalse("Piece is still in catalog",
        preferences.getFurnitureCatalog().getCategories().contains(catalogPiece.getCategory()));
    // Check home piece of furniture is still in home and selected
    assertTrue("Home piece isn't in home", home.getFurniture().contains(homePiece));
    assertTrue("Home piece isn't selected", home.getSelectedItems().contains(homePiece));

    // 12. Undo furniture creation in home
    tester.invokeAndWait(new Runnable() {
      public void run() {
        homeView.getActionMap().get(HomePane.ActionType.UNDO).actionPerformed(null);
      }
    });
    // Check home is empty
    assertTrue("Home isn't empty", home.getFurniture().isEmpty());
    // Redo
    tester.invokeAndWait(new Runnable() {
      public void run() {
        homeView.getActionMap().get(HomePane.ActionType.REDO).actionPerformed(null);
      }
    });
    // Check home piece of furniture is in home and selected
    assertTrue("Home piece isn't in home", home.getFurniture().contains(homePiece));
    assertTrue("Home piece isn't selecteed", home.getSelectedItems().contains(homePiece));
  }

  /**
   * Asserts if each <code>panel</code> step preview component is showing or
   * not.
   */
  private void assertStepShowing(ImportedFurnitureWizardStepsPanel panel, boolean modelStepShwing,
                                 boolean rotationStepShowing, boolean attributesStepShowing, boolean iconStepShowing)
      throws NoSuchFieldException, IllegalAccessException {

    assertEquals("Wrong model step visibility", modelStepShwing,
        ((JComponent)TestUtilities.getField(panel, "modelPreviewComponent")).isShowing());
    assertEquals("Wrong rotation step visibility", rotationStepShowing,
        ((JComponent)TestUtilities.getField(panel, "rotationPreviewComponent")).isShowing());
    assertEquals("Wrong attributes step visibility", attributesStepShowing,
        ((JComponent)TestUtilities.getField(panel, "attributesPreviewComponent")).isShowing());
    assertEquals("Wrong icon step visibility", iconStepShowing,
        ((JComponent)TestUtilities.getField(panel, "iconPreviewComponent")).isShowing());
  }

  public void test_importedFurnitureWizardTest() throws MalformedURLException, URISyntaxException {
    String language = Locale.getDefault().getLanguage();
    final UserPreferences preferences = new FileUserPreferences();
    // Ensure we use default language and centimeter unit
    preferences.setLanguage(language);
    preferences.setUnit(LengthUnit.CENTIMETER);
    final ViewFactory viewFactory = new SwingViewFactory();
    // final URL testedModelName =
    // ImportedFurnitureWizardTest.class.getResource("resources/test.obj");
//    final URL testedModelName = new URL(
//        "file:/Users/gouyunfei/Documents/tools/apache-tomcat-7.0.65/webapps/examples/models/obj/tree.obj");
    final URL testedModelName = new URL("file:/Users/gouyunfei/Documents/workspace/sweet_home/SweetHome3D-5.5.2-src-online/install/textured_mesh.obj");
    // Create a dummy content manager
    final ContentManager contentManager = new ContentManager() {
      public Content getContent(String contentName) throws RecorderException {
        try {
          // Let's consider contentName is a URL
          return new URLContent(new URL(contentName));
        } catch (IOException ex) {
          ex.printStackTrace();
          fail();
          return null;
        }
      }

      public String getPresentationName(String contentName, ContentType contentType) {
        return "test";
      }

      public boolean isAcceptable(String contentName, ContentType contentType) {
        return true;
      }

      public String showOpenDialog(View parentView, String dialogTitle, ContentType contentType) {
        // Return tested model name URL
        return testedModelName.toString();
      }

      public String showSaveDialog(View parentView, String dialogTitle, ContentType contentType, String name) {
        return null;
      }
    };
    Home home = new Home();
    final HomeController controller = new HomeController(home, preferences, viewFactory, contentManager);
    // final JComponent homeView = (JComponent)controller.getView();
    final List<CatalogPieceOfFurniture> addedCatalogFurniture = new ArrayList<CatalogPieceOfFurniture>();
    preferences.getFurnitureCatalog().addFurnitureListener(new CollectionListener<CatalogPieceOfFurniture>() {
      public void collectionChanged(CollectionEvent<CatalogPieceOfFurniture> ev) {
        addedCatalogFurniture.add(ev.getItem());
      }
    });
    // CatalogPieceOfFurniture piece,
    // String modelName,
    // boolean importHomePiece,
    // UserPreferences preferences,
    // final ImportedFurnitureWizardController controller
    // final ImportedFurnitureWizardController importedFurnitureWizardController
    // = new ImportedFurnitureWizardController(
    // preferences, viewFactory, contentManager);
    // ImportedFurnitureWizardStepsPanel s = new
    // ImportedFurnitureWizardStepsPanel(null, testedModelName.getFile()
    // .toUpperCase(), true, preferences, importedFurnitureWizardController);

    System.out.println(testedModelName.toURI());
    updateController(testedModelName.toURI().toString(), preferences, contentManager, null, true);
  }

  /**
   * Reads model from <code>modelName</code> and updates controller values.
   */
  private static void updateController(final String modelName, final UserPreferences preferences,
                                       final ContentManager contentManager, final FurnitureCategory defaultCategory,
                                       final boolean ignoreException) {
    // Cancel current model
    // this.controller.setModel(null);
    // updatePreviewComponentsModel(null);
    // setReadingState();
    // Read model in modelLoader executor
    Executor modelLoader = Executors.newSingleThreadExecutor();
    modelLoader.execute(new Runnable() {
      public void run() {
        Content modelContent = null;
        try {
          modelContent = contentManager.getContent(modelName);
        } catch (RecorderException ex) {
          // setDefaultStateAndShowModelChoiceError(modelName, preferences,
          // !ignoreException);
        }

        try {
          BranchGroup model = ModelManager.getInstance().loadModel(modelContent);
          final Vector3f modelSize = ModelManager.getInstance().getSize(model);
          // Copy model to a temporary OBJ content with materials and textures
          final Content copiedContent = copyToTemporaryOBJContent(model, modelName);
          EventQueue.invokeLater(new Runnable() {
            public void run() {
              // Load copied content using cache to make it accessible by
              // preview components
              ModelManager.getInstance().loadModel(copiedContent, new ModelManager.ModelObserver() {
                public void modelUpdated(BranchGroup modelRoot) {
                  setDefaultStateAndInitializeReadModel(copiedContent, modelName, defaultCategory, modelSize,
                      preferences, contentManager);
                }

                public void modelError(Exception ex) {
                  // setDefaultStateAndShowModelChoiceError(modelName,
                  // preferences, !ignoreException);
                }
              });
            }
          });
          return;
        } catch (IllegalArgumentException ex) {
          // Thrown by getSize if model is empty
        } catch (IOException ex) {
          // Try with zipped content
        }

        try {
          // Copy model content to a temporary content
          modelContent = TemporaryURLContent.copyToTemporaryURLContent(modelContent);
        } catch (IOException ex) {
          return;
        }

        // If content couldn't be loaded, try to load model as a zipped file
        ZipInputStream zipIn = null;
        try {
          URLContent urlContent = (URLContent)modelContent;
          // Open zipped stream
          zipIn = new ZipInputStream(urlContent.openStream());
          // Parse entries to see if one is readable
          while (true) {
            ZipEntry entry;
            try {
              if ((entry = zipIn.getNextEntry()) == null) {
                // No more entry
                break;
              }
            } catch (IllegalArgumentException ex) {
              // Exception thrown if entry name can't be read
              break;
            }

            String entryName = entry.getName();
            // Ignore directory entries and entries starting by a dot
            if (!entryName.endsWith("/")) {
              int slashIndex = entryName.lastIndexOf('/');
              String entryFileName = entryName.substring(++slashIndex);
              if (!entryFileName.startsWith(".")) {
                URL entryUrl = new URL("jar:" + urlContent.getURL() + "!/"
                                       + URLEncoder.encode(entryName, "UTF-8").replace("+", "%20").replace("%2F", "/"));
                final Content entryContent = new TemporaryURLContent(entryUrl);
                final AtomicReference<Vector3f> modelSize = new AtomicReference<Vector3f>();
                final CountDownLatch latch = new CountDownLatch(1);
                EventQueue.invokeAndWait(new Runnable() {
                  public void run() {
                    // Load content using cache to make it accessible by preview
                    // components
                    ModelManager.getInstance().loadModel(entryContent, new ModelManager.ModelObserver() {
                      public void modelUpdated(BranchGroup modelRoot) {
                        try {
                          modelSize.set(ModelManager.getInstance().getSize(modelRoot));
                        } catch (IllegalArgumentException ex) {
                          // Thrown by getSize if model is empty
                        }
                        latch.countDown();
                      }

                      public void modelError(Exception ex) {
                        latch.countDown();
                      }
                    });
                  }
                });

                latch.await();
                if (modelSize.get() != null) {
                  // Check if all remaining entries in the ZIP file can be read,
                  // to be able to save edited home with them
                  do {
                    try {
                      entry = zipIn.getNextEntry();
                    } catch (IllegalArgumentException ex) {
                      // Exception thrown if entry name can't be read
                      break;
                    }
                  } while (entry != null);

                  if (entry == null) {
                    EventQueue.invokeAndWait(new Runnable() {
                      public void run() {
                        setDefaultStateAndInitializeReadModel(entryContent, modelName, defaultCategory,
                            modelSize.get(), preferences, contentManager);
                      }
                    });
                    return;
                  }
                }
              }
            }
          }
        } catch (IOException ex) {
          return;
        } catch (InterruptedException ex) {
          return;
        } catch (InvocationTargetException ex) {
          // Show next message
        } finally {
          try {
            if (zipIn != null) {
              zipIn.close();
            }
          } catch (IOException ex) {
            // Ignore close exception
          }
        }

        // Found no readable model
      }
    });

    try {
      Thread.sleep(1000 * 60L);
    } catch (InterruptedException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
  }

  private static Content copyToTemporaryOBJContent(BranchGroup model, String modelName) throws IOException {
    try {
      String objFile = new File(modelName).getName();
      if (!objFile.toLowerCase().endsWith(".obj")) {
        objFile += ".obj";
      }
      // Ensure the file contains only letters, figures, underscores, dots,
      // hyphens or spaces
      if (objFile.matches(".*[^a-zA-Z0-9_\\.\\-\\ ].*")) {
        objFile = "model.obj";
      }
      File tempZipFile = OperatingSystem.createTemporaryFile("import", ".zip");
      OBJWriter.writeNodeInZIPFile(model, tempZipFile, 0, objFile, "3D model import " + modelName);
      return new TemporaryURLContent(new URL("jar:" + tempZipFile.toURI().toURL() + "!/"
                                             + URLEncoder.encode(objFile, "UTF-8").replace("+", "%20")));
    } finally {
    }
  }

  /**
   * Restores default state and initializes read model.
   */
  private static void setDefaultStateAndInitializeReadModel(final Content modelContent, final String modelName,
                                                            final FurnitureCategory defaultCategory,
                                                            final Vector3f modelSize,
                                                            final UserPreferences preferences,
                                                            final ContentManager contentManager) {
    System.out.println(((TemporaryURLContent)modelContent).getURL());
    // controller.setModel(modelContent);
    // controller.setModelSize(modelContent instanceof URLContent ?
    // ((URLContent)modelContent).getSize() : null);
    // controller.setModelRotation(new float [][] {{1, 0, 0}, {0, 1, 0}, {0, 0,
    // 1}});
    // controller.setBackFaceShown(false);
    // controller.setName(contentManager.getPresentationName(
    // modelName, ContentManager.ContentType.MODEL));
    // controller.setCreator(null);
    // controller.setCategory(defaultCategory);
    // // Initialize size with default values
    // controller.setWidth(modelSize.x);
    // controller.setDepth(modelSize.z);
    // controller.setHeight(modelSize.y);
    // controller.setMovable(true);
    // controller.setDoorOrWindow(false);
    // controller.setStaircaseCutOutShape(null);
    // controller.setColor(null);
    // controller.setIconYaw((float)Math.PI / 8);
    // controller.setProportional(true);
    String name = contentManager.getPresentationName(modelName, ContentManager.ContentType.MODEL);
    Content icon = getIcon(400);// ////need find value
    ButtonModel buttonModel = null;// ////need find value
    float width = modelSize.x;
    float depth = modelSize.z;
    float height = modelSize.y;
    float elevation = 0f;// ////need find value
    boolean isMovable = true;
    String staircaseCutOutShape = null;// ////need find value
    Integer color = null;
    float [][] modelRotation = new float [] [] { {1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
    boolean isBackFaceShown = false;
    long modelSizeLong = modelContent instanceof URLContent
        ? ((URLContent)modelContent).getSize()
        : null;
    String creator = "root";
    float iconYaw = (float)Math.PI / 8;
    boolean isProportional = true;

    CatalogPieceOfFurniture newPiece;
    // String name, Content icon, Content model,
    // float width, float depth, float height, float elevation,
    // boolean movable, String staircaseCutOutShape,
    // Integer color, float [][] modelRotation, boolean backFaceShown, Long
    // modelSize,
    // String creator, float iconYaw, boolean proportional
    newPiece = new CatalogPieceOfFurniture(name, icon, modelContent, width, depth, height, elevation, isMovable,
        staircaseCutOutShape, color, modelRotation, isBackFaceShown, modelSizeLong, creator, iconYaw, isProportional);
    HomePieceOfFurniture homePieceOfFurniture = new HomePieceOfFurniture(newPiece);

    // Remove the edited piece from catalog
    FurnitureCatalog catalog = preferences.getFurnitureCatalog();
    FurnitureCategory category = new FurnitureCategory("test");
    catalog.add(category, newPiece);
    try {
      preferences.write();
    } catch (RecorderException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
  }

  public static Content getIcon(int maxWaitingDelay) {
    try {
      File tempIconFile = OperatingSystem.createTemporaryFile("icon", ".png");
      ImageIO.write(getIconImage(maxWaitingDelay), "png", tempIconFile);
      return new TemporaryURLContent(tempIconFile.toURI().toURL());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // static Object iconImageLock;
  private static BufferedImage getIconImage(int maxWaitingDelay) {
    Color backgroundColor = Color.WHITE;

    BufferedImage imageWithWhiteBackgound = null;
    BufferedImage imageWithBlackBackgound = null;

    Object iconImageLock = new Object();
    try {
      // Instead of using off screen images that may cause some problems
      // use Robot to capture canvas 3D image
      Point component3DOrigin = new Point();
      // SwingUtilities.convertPointToScreen(component3DOrigin,
      // this.component3D);

      Robot robot = new Robot();
      // Render scene with a white background
      if (iconImageLock != null) {
        synchronized (iconImageLock) {
          // setBackground(Color.WHITE);
          try {
            iconImageLock.wait(maxWaitingDelay / 2);
            if (OperatingSystem.isMacOSX()) {
              // Under Mac OS X, sleep an additional time to ensure the screen
              // got refreshed
              Thread.sleep(30);
            }
          } catch (InterruptedException ex) {
            ex.printStackTrace();
          }
        }
      }
      Dimension dimetion = new Dimension();
      dimetion.setSize(10, 10);
      imageWithWhiteBackgound = robot.createScreenCapture(new Rectangle(component3DOrigin, dimetion));

      // Render scene with a black background
      if (iconImageLock != null) {
        synchronized (iconImageLock) {
          // setBackground(Color.BLACK);
          try {
            iconImageLock.wait(maxWaitingDelay / 2);
            if (OperatingSystem.isMacOSX()) {
              // Under Mac OS X, sleep an additional time to ensure the screen
              // got refreshed
              Thread.sleep(30);
            }
          } catch (InterruptedException ex) {
            ex.printStackTrace();
          }
        }
      }
      imageWithBlackBackgound = robot.createScreenCapture(new Rectangle(component3DOrigin, dimetion));
    } catch (AWTException ex) {
      throw new RuntimeException(ex);
    } finally {
      // this.iconImageLock = null;
      // setBackground(backgroundColor);
    }

    int [] imageWithWhiteBackgoundPixels = imageWithWhiteBackgound.getRGB(0, 0, imageWithWhiteBackgound.getWidth(),
        imageWithWhiteBackgound.getHeight(), null, 0, imageWithWhiteBackgound.getWidth());
    int [] imageWithBlackBackgoundPixels = imageWithBlackBackgound.getRGB(0, 0, imageWithBlackBackgound.getWidth(),
        imageWithBlackBackgound.getHeight(), null, 0, imageWithBlackBackgound.getWidth());

    // Create an image with transparent pixels where model isn't drawn
    for (int i = 0; i < imageWithBlackBackgoundPixels.length; i++) {
      if (imageWithBlackBackgoundPixels [i] != imageWithWhiteBackgoundPixels [i]
          && imageWithBlackBackgoundPixels [i] == 0xFF000000 && imageWithWhiteBackgoundPixels [i] == 0xFFFFFFFF) {
        imageWithWhiteBackgoundPixels [i] = 0;
      }
    }

    BufferedImage iconImage = new BufferedImage(imageWithWhiteBackgound.getWidth(),
        imageWithWhiteBackgound.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2D = (Graphics2D)iconImage.getGraphics();
    g2D.drawImage(
        Toolkit.getDefaultToolkit().createImage(
            new MemoryImageSource(imageWithWhiteBackgound.getWidth(), imageWithWhiteBackgound.getHeight(),
                imageWithWhiteBackgoundPixels, 0, imageWithWhiteBackgound.getWidth())), null, null);
    g2D.dispose();

    return iconImage;
  }
  
  
//  public void test
  public void testString() {
    String tmpStr = "c: / U s e r s / u s e r / D e s k t o p / s c a n n e r / s c a n n e r / s c a n - 2 0 1 7 1 2 2 3 - 1 7 5 9 2 1 / t e x t u r e d _ m e s h . o b j";
    
    
    tmpStr=tmpStr.replaceAll("\\s*", "");
    System.out.println(tmpStr);
  }
}
