package com.packtpub.e4.clock.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class ClockView extends ViewPart {
  /**
   * The constructor.
   */
  public ClockView() {
  }

  /**
   * This is a callback that will allow us
   * to create the viewer and initialize it.
   */
  public void createPartControl(Composite parent) {      
      RowLayout layout = new RowLayout(SWT.HORIZONTAL);
      parent.setLayout(layout);
    (new ClockWidget(parent, SWT.NONE, new RGB(255, 0, 0))).setLayoutData(new RowData(20, 20));
    new ClockWidget(parent, SWT.NONE, new RGB(0, 255, 0));
    (new ClockWidget(parent, SWT.NONE, new RGB(0, 0, 255))).setLayoutData(new RowData(100, 100));
    
    Object[] oo = parent.getDisplay().getDeviceData().objects;
    int c = 0;
    for (Object o : oo) {
	  if (o instanceof Color)
	      ++c;
    }
    System.err.println(String.format("%d remaining Color instances", c));
  }

  /**
   * Passing the focus request to the viewer's control.
   */
  public void setFocus() {

  }
}
