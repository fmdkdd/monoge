package com.packtpub.e4.application.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.osgi.service.log.LogService;

public class Hello {
  @Inject
  private LogService log;

  private Label label;

  @PostConstruct
  public void create(Composite parent) {
    label = new Label(parent, SWT.NONE);
    label.setText("Hello");
    log.log(LogService.LOG_ERROR, "Hello@postConstruct");
  }

  @Focus
  public void focus() {
    label.setFocus();
  }

}
