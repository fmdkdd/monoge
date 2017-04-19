package com.packtpub.e4.application.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.osgi.service.log.LogService;

import com.packtpub.e4.application.IStringService;

public class Hello {
  @Inject
  private LogService log;

  @Inject
  private MWindow window;

  @Inject
  private UISynchronize ui;

  @Inject
  private IStringService stringService;

  private Label label;
  private Button button;

  @PostConstruct
  public void create(Composite parent, EMenuService menu) {
    label = new Label(parent, SWT.NONE);
    label.setText(window.getLabel());
    log.log(LogService.LOG_ERROR, "Hello@postConstruct");

    button = new Button(parent, SWT.PUSH);
    button.setText("Do not push");
    button.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent e) {
        button.setEnabled(false);
        new Job("Button Pusher") {
          protected IStatus run(IProgressMonitor monitor) {
            ui.asyncExec(() -> {
              button.setEnabled(true);
            });
            return Status.OK_STATUS;
          }
        }.schedule(1000);
      }

      public void widgetDefaultSelected(SelectionEvent e) {}
    });

    menu.registerContextMenu(button,
        "com.packtpub.e4.application.popupmenu.hello");
  }

  @Focus
  public void focus() {
    label.setFocus();
  }

  @Inject
  @Optional
  public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object selection) {
    if (selection != null) {
      label.setText(selection.toString());
    }
  }

  @Inject
  @Optional
  public void receiveEvent(@UIEventTopic("rainbow/color") String color) {
    label.setText(stringService.process(color));
  }

}
