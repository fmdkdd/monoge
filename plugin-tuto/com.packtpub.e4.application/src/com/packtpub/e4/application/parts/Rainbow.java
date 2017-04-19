package com.packtpub.e4.application.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class Rainbow {
  private static String[] colors = { "Red", "Blue", "Green", "Yellow" };

  @Inject
  private ESelectionService selectionService;

  @Inject
  private IEventBroker eventBroker;

  @PostConstruct
  public void create(Composite parent) {
    ListViewer lv = new ListViewer(parent, SWT.NONE);
    lv.setContentProvider(new ArrayContentProvider());
    lv.addSelectionChangedListener((event) -> {
      Object color = ((IStructuredSelection) event.getSelection())
          .getFirstElement();
      eventBroker.post("rainbow/color", color);
    });
    lv.setInput(colors);
  }

}
