/*
 * FurnitureCatalogListPanel.java 10 janv 2010
 * 
 * Sweet Home 3D, Copyright (c) 2010 Emmanuel PUYBARET / eTeks <info@eteks.com>
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
package com.eteks.sweethome3d.swing;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.eteks.sweethome3d.io.CaseManager;
import com.eteks.sweethome3d.io.model.CaseTO;
import com.eteks.sweethome3d.model.HomeApplication;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.renderer.CaseTableModel;
import com.eteks.sweethome3d.swing.renderer.MyButtonEditor;
import com.eteks.sweethome3d.swing.renderer.MyButtonRenderer;
import com.eteks.sweethome3d.viewcontroller.DialogView;
import com.eteks.sweethome3d.viewcontroller.HomeController;
import com.eteks.sweethome3d.viewcontroller.View;

/**
 * A furniture catalog view that displays furniture in a list, with a combo and
 * search text field.
 * @author Emmanuel Puybaret
 */
public class CasePanel extends JRootPane implements DialogView {
  private HomeApplication application;
  private JFrame          frame;
  private JScrollPane     jsp;
  private JPanel          panel;
  private HomeController  homeController;
  private UserPreferences preferences;

  public CasePanel(UserPreferences preferences, HomeApplication application, HomeController homeController) {
    this.application = application;
    this.homeController = homeController;
    this.preferences = preferences;
    createComponents(preferences);
  }

  /**
   * Creates and initializes components and spinners model.
   */
  private void createComponents(final UserPreferences preferences) {
    final CaseTableModel tm = new CaseTableModel();
    TableColumnModel cm = new DefaultTableColumnModel() {
      boolean first = true;

      public void addColumn(TableColumn tc) {
        // Drop the first column, which will be the row header.
        if (first) {
          first = false;
          return;
        }
        tc.setMinWidth(150); // Just for looks, really...
        super.addColumn(tc);
      }
    };

    TableColumnModel rowHeaderModel = new DefaultTableColumnModel() {
      boolean first = true;

      public void addColumn(TableColumn tc) {
        if (first) {
          tc.setMaxWidth(tc.getPreferredWidth());
          super.addColumn(tc);
          first = false;
        }
      }
    };

    final JTable jt = new JTable(tm, cm);
    JTable headerColumn = new JTable(tm, rowHeaderModel);
    jt.createDefaultColumnsFromModel();
    headerColumn.createDefaultColumnsFromModel();
    jt.setSelectionModel(headerColumn.getSelectionModel());
    JViewport jv = new JViewport();
    jv.setView(headerColumn);
    jv.setPreferredSize(headerColumn.getMaximumSize());
    jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jt.getColumnModel().getColumn(3).setCellEditor(new MyButtonEditor());
    jt.getColumnModel().getColumn(3).setCellRenderer(new MyButtonRenderer());

    jsp = new JScrollPane(jt);
    jsp.setRowHeader(jv);
    jsp.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, headerColumn.getTableHeader());

    JButton addCaseButton = new JButton("\u521b\u5efa\u6848\u4ef6");
    addCaseButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String inputValue = JOptionPane.showInputDialog("\u8bf7\u8f93\u5165\u6848\u4ef6\u540d\u79f0");
        tm.addCase(inputValue, "root");
      }
    });
    JButton showCaseItemButton = new JButton("\u67e5\u770b\u6240\u5c5e\u73b0\u573a");
    showCaseItemButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        EventQueue.invokeLater(new Runnable() {
          public void run() {
            int row = jt.getSelectedRow();
            System.out.println(row);
            CaseManager caseManager = CaseManager.getInstance();
            List<CaseTO> ls = caseManager.getAllCase();
            CaseTO caseTO = ls.get(row);
            // System.out.println(caseTO.getName());
            JFrame f = new JFrame();
            CaseItemPane caseItemPane = new CaseItemPane(caseTO.getId(), preferences, application);
            f.add(caseItemPane.createComponent(frame, f, homeController));
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            f.pack();
            f.setLocation((int)(dimension.getWidth() / 3), (int)(dimension.getHeight() / 3));
            f.setVisible(true);
          }
        });
      }
    });

    jt.addMouseListener(new MouseAdapter() {

      public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {// 单击鼠标左键
          if (e.getClickCount() == 2) {
            int row = jt.getSelectedRow();
            System.out.println(row);
            CaseManager caseManager = CaseManager.getInstance();
            List<CaseTO> ls = caseManager.getAllCase();
            CaseTO caseTO = ls.get(row);
            // System.out.println(caseTO.getName());
            JFrame f = new JFrame();
            CaseItemPane caseItemPane = new CaseItemPane(caseTO.getId(), preferences, application);
            f.add(caseItemPane.createComponent(frame, f, homeController));
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            f.pack();
            f.setLocation((int)(dimension.getWidth() / 3), (int)(dimension.getHeight() / 3));
            f.setVisible(true);
          }
        }
      }
    });
    panel = new JPanel();
    panel.add(addCaseButton, BorderLayout.WEST);
    panel.add(showCaseItemButton, BorderLayout.WEST);

  }

  /**
   * Displays this panel in a dialog box.
   */
  public void displayView(View parentView) {
    // SwingTools.showConfirmDialogTest((JComponent)parentView, this,
    // this.dialogTitle, this.topViewRadioButton);
    if (this.frame == null) {
      this.frame = new JFrame() {
        {
          // Replace frame rootPane by case view
          setRootPane(CasePanel.this);
        }
      };
    }
    // Update frame image and title
    this.frame.setTitle("testtesttest");
    this.frame.applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
    // Compute frame size and location
    getContentPane().add(jsp, BorderLayout.CENTER);
    getContentPane().add(panel, BorderLayout.SOUTH);
    // Just hide help frame when user close it
    this.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    // Show frame
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    this.frame.setLocation((int)(dimension.getWidth() / 3), (int)(dimension.getHeight() / 3));
    this.frame.pack();
    this.frame.setVisible(true);
    this.frame.setState(JFrame.NORMAL);
    this.frame.toFront();
  }

}
