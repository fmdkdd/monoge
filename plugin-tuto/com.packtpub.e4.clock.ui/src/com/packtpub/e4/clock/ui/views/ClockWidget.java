package com.packtpub.e4.clock.ui.views;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class ClockWidget extends Canvas {
  private final Color color;

  public ClockWidget(Composite parent, int style, RGB rgb) {
    super(parent, style);

    this.color = new Color(parent.getDisplay(), rgb);

    // Create a dispose listener to dispose of the color when ClockWidget is
    // disposed
    this.addDisposeListener(e -> {
      if (this.color != null && !this.color.isDisposed()) {
        this.color.dispose();
      }
    });

    this.addPaintListener(e -> ClockWidget.this.paintControl(e));

    new Thread("TickTock") {
      public void run() {
        while (!ClockWidget.this.isDisposed()) {
          ClockWidget.this.getDisplay().asyncExec(new Runnable() {
            public void run() {
              if (!ClockWidget.this.isDisposed()) {
                ClockWidget.this.redraw();
              }
            }
          });
          try {
            Thread.sleep(1000);
          } catch (InterruptedException ex) {
            return;
          }
        }
      }
    }.start();
  }

  public void paintControl(PaintEvent e) {
    Date now = new Date();
    int hours = now.getHours();
    int mins = now.getMinutes();
    int seconds = now.getSeconds();

    // Draw contour
    e.gc.drawArc(e.x, e.y, e.width - 1, e.height - 1, 0, 360);

    // Draw hour hand
    int arc = hours * -30 % 360 + 90;
    e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_BLACK));
    e.gc.fillArc(e.x + ((e.width - 1) / 4), e.y + ((e.height - 1) / 4), (e.width - 1) / 2, (e.height - 1) / 2, arc - 2,
        4);

    // Draw minute hand
    arc = mins * -6 % 360 + 90;
    e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_BLACK));
    e.gc.fillArc(e.x, e.y, e.width - 1, e.height - 1, arc - 1, 2);

    // Draw second hand
    arc = seconds * -6 % 360 + 90;
    e.gc.setBackground(this.color);
    e.gc.fillArc(e.x, e.y, e.width - 1, e.height - 1, arc - 1, 2);

    // Draw ticks
    double cx = e.x + (e.width - 1d) / 2d;
    double cy = e.y + (e.height - 1d) / 2d;
    double r = 0.1;
    for (int t = 0; t < 20; ++t) {
      double angle = t / 20d * Math.PI * 2d;
      int dx0 = (int) (cx + (e.width - 1d) / 2d * Math.cos(angle) * (1d - r));
      int dx1 = (int) (cx + (e.width - 1d) / 2d * Math.cos(angle));
      int dy0 = (int) (cy + (e.height - 1d) / 2d * Math.sin(angle) * (1d - r));
      int dy1 = (int) (cy + (e.height - 1d) / 2d * Math.sin(angle));
      e.gc.drawLine(dx0, dy0, dx1, dy1);
    }
  }

  public Point computeSize(int w, int h, boolean changed) {
    int size;
    if (w == SWT.DEFAULT) {
      size = h;
    } else if (h == SWT.DEFAULT) {
      size = w;
    } else {
      size = Math.min(w, h);
    }

    if (size == SWT.DEFAULT) {
      size = 50;
    }

    return new Point(size, size);
  }
}
