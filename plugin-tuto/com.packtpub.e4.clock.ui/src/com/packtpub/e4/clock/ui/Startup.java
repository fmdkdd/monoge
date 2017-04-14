package com.packtpub.e4.clock.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

public class Startup implements IStartup {

  private TrayItem trayItem;
  private Image image;

  @Override
  public void earlyStartup() {
    Display display = PlatformUI.getWorkbench().getDisplay();
    display.asyncExec(() -> {
      image = new Image(display, Activator.class.getResourceAsStream("/icons/sample.gif"));
      Tray tray = display.getSystemTray();
      if (tray != null && image != null) {
        trayItem = new TrayItem(tray, SWT.NONE);
        trayItem.setToolTipText("Hello");
        trayItem.setVisible(true);
        trayItem.setText("Hello text");
        trayItem.setImage(image);
      }
    });

  }

}
