package com.packtpub.e4.application.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.osgi.service.log.LogService;

public class Hello {
  @Inject
  private LogService log;

  @Inject
  private MWindow window;

  private Label label;

  @PostConstruct
  public void create(Composite parent) {
    label = new Label(parent, SWT.NONE);
    label.setText(window.getLabel());
    log.log(LogService.LOG_ERROR, "Hello@postConstruct");
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

}
