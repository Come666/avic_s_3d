package com.eteks.sweethome3d.swing;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBarPanel extends JPanel {
  JProgressBar     pbar;
  static final int MY_MINIMUM = 0;
  static final int MY_MAXIMUM = 100;

  public ProgressBarPanel() {
    pbar = new JProgressBar();
    pbar.setMinimum(MY_MINIMUM);
    pbar.setMaximum(MY_MAXIMUM);
    add(pbar);
  }

  public void updateBar(int newValue) {
    pbar.setValue(newValue);
  }

}
