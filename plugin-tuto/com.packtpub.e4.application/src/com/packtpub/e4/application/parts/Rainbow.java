package com.packtpub.e4.application.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class Rainbow {
  private static String[] colors = { "Red", "Blue", "Green", "Yellow" };

  @Inject
  private ESelectionService selectionService;

  @PostConstruct
  public void create(Composite parent) {
    ListViewer lv = new ListViewer(parent, SWT.NONE);
    lv.setContentProvider(new ArrayContentProvider());
    lv.addSelectionChangedListener((event) -> {
      selectionService.setSelection(event.getSelection());
    });
    lv.setInput(colors);
  }

}
