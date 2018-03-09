package com.eteks.sweethome3d.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.ToolTipManager;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.eteks.sweethome3d.SweetHome3D;
import com.eteks.sweethome3d.io.CaseManager;
import com.eteks.sweethome3d.io.model.CaseItemTO;
import com.eteks.sweethome3d.model.DamagedHomeRecorderException;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomeApplication;
import com.eteks.sweethome3d.model.InterruptedRecorderException;
import com.eteks.sweethome3d.model.RecorderException;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.renderer.CaseItemTableModel;
import com.eteks.sweethome3d.swing.renderer.MyButtonEditor;
import com.eteks.sweethome3d.swing.renderer.MyButtonRenderer;
import com.eteks.sweethome3d.viewcontroller.HomeController;
import com.eteks.sweethome3d.viewcontroller.ThreadedTaskController;

public class CaseItemPane extends JRootPane {
  public static double getScreenWidth() {
    return Toolkit.getDefaultToolkit().getScreenSize().getWidth();
  }

  public static double getScreenHeight() {
    return Toolkit.getDefaultToolkit().getScreenSize().getHeight();
  }

  private final String          caseId;
  private final HomeApplication application;
  private final UserPreferences userPreferences;

  public CaseItemPane(final String caseId, UserPreferences preferences, final HomeApplication application) {
    this.caseId = caseId;
    this.application = application;
    this.userPreferences = preferences;
    // createComponent(caseId, application);
  }

  public Container createComponent(final JFrame casePaneFrame, final JFrame caseItemPaneFrame,
                                   final HomeController homeController) {
    final CaseItemTableModel tm = new CaseItemTableModel(caseId);

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
    jt.getColumnModel().getColumn(2).setCellEditor(new MyButtonEditor());
    jt.getColumnModel().getColumn(2).setCellRenderer(new MyButtonRenderer());

    JScrollPane jsp = new JScrollPane(jt);
    jsp.setRowHeader(jv);
    jsp.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, headerColumn.getTableHeader());

    JButton addCaseButton = new JButton("\u521b\u5efa\u73b0\u573a");
    addCaseButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String inputValue = JOptionPane.showInputDialog("\u8bf7\u8f93\u5165\u73b0\u573a\u540d\u79f0");
        tm.addCaseItem(inputValue, "root");
      }
    });
    JButton showCaseItemButton = new JButton("\u6253\u5f00\u73b0\u573a");
    showCaseItemButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        on_click_action(homeController, jt);
        if (application != null) {
          casePaneFrame.setVisible(false);
          caseItemPaneFrame.setVisible(false);
        }

      }
    });
    jt.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
          if (e.getClickCount() == 2) {
            on_click_action(homeController, jt);
            if (application != null) {
              casePaneFrame.setVisible(false);
              caseItemPaneFrame.setVisible(false);
            }
          }
        }
      }
    });
    JPanel panel = new JPanel();
    panel.add(addCaseButton, BorderLayout.WEST);
    panel.add(showCaseItemButton, BorderLayout.WEST);
    getContentPane().add(jsp, BorderLayout.CENTER);
    getContentPane().add(panel, BorderLayout.SOUTH);
    ToolTipManager.sharedInstance().registerComponent(this);
    return getContentPane();
  }

  private void on_click_action(final HomeController homeController, final JTable jt) {
    int row = jt.getSelectedRow();
    // get filename(file_path+caseId), and new home then set homename
    CaseManager caseManager = CaseManager.getInstance();
    List<CaseItemTO> caseItems = caseManager.getCaseItemByCaseId(caseId);
    CaseItemTO caseItemTO = caseItems.get(row);
    String filePath = caseManager.getFilePath();
    File fileDir = new File(filePath + "/case");
    if (!fileDir.exists()) {
      fileDir.mkdirs();
    }
    final File file = new File(filePath + "/case/" + caseItemTO.getId() + ".sh3d");
    if (application != null) {
      Home home = null;
      if (homeController == null) {
        if (file.exists()) {
          try {
            home = application.getHomeRecorder().readHome(file.getAbsolutePath());
          } catch (RecorderException ex) {
            ex.printStackTrace();
          }
        } else {
          home = application.createHome();
        }
        home.setName(file.getAbsolutePath());
        application.addHome(home);

        EventQueue.invokeLater(new Runnable() {
          public void run() {
            ((SweetHome3D)application).start(new String [] {"-open", file.getAbsolutePath()});
          }
        });
      } else {
        innerControllerOpen(homeController, file);
      }

    }
  }

  private void innerControllerOpen(final HomeController homeController, final File file) {
    Home home = null;
    if (file.exists()) {
      try {
        home = application.getHomeRecorder().readHome(file.getAbsolutePath());
      } catch (RecorderException ex) {
        ex.printStackTrace();
      }
    } else {
      home = application.createHome();
    }
    home.setName(file.getAbsolutePath());
    final String homeName = file.getAbsolutePath();
    application.addHome(home);
    final Home openHome = home;
    // homeController.open(file.getAbsolutePath());

    Callable<Void> openTask = new Callable<Void>() {
      public Void call() throws RecorderException {
        if (openHome.isRepaired()) {
          homeController.getView().invokeLater(new Runnable() {
            public void run() {
              String message = userPreferences.getLocalizedString(HomeController.class, "openRepairedHomeMessage",
                  homeName);
              homeController.getView().showMessage(message);
            }
          });
        }
        return null;
      }
    };
    ThreadedTaskController.ExceptionHandler exceptionHandler = new ThreadedTaskController.ExceptionHandler() {
      public void handleException(Exception ex) {
        if (!(ex instanceof InterruptedRecorderException)) {
          if (ex instanceof DamagedHomeRecorderException) {
            DamagedHomeRecorderException ex2 = (DamagedHomeRecorderException)ex;
            System.out.println("ex2.getInvalidContent():" + ex2.getInvalidContent());
            homeController.openDamagedHome(homeName, ex2.getDamagedHome(), ex2.getInvalidContent());
          } else {
            ex.printStackTrace();
            if (ex instanceof RecorderException) {
              String message = userPreferences.getLocalizedString(HomeController.class, "openError", homeName);
              homeController.getView().showError(message);
            }
          }
        }
      }
    };
    new ThreadedTaskController(openTask, userPreferences.getLocalizedString(HomeController.class, "openMessage"),
        exceptionHandler, userPreferences, homeController.getViewFactory()).executeTask(homeController.getView());
  }

  public static void main(String [] args) {

  }
}
