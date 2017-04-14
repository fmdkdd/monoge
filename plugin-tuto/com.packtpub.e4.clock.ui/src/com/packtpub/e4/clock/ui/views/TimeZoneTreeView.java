package com.packtpub.e4.clock.ui.views;

import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

public class TimeZoneTreeView extends ViewPart {

  private TreeViewer treeViewer;

  public TimeZoneTreeView() {
  }

  @Override
  public void createPartControl(Composite parent) {
    ResourceManager rm = JFaceResources.getResources();
    LocalResourceManager lrm = new LocalResourceManager(rm, parent);
    ImageRegistry ir = new ImageRegistry(lrm);
    URL sample = getClass().getResource("/icons/sample.gif");
    ir.put("sample", ImageDescriptor.createFromURL(sample));

    treeViewer = new TreeViewer(parent,
        SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    treeViewer.setLabelProvider(
        new DelegatingStyledCellLabelProvider(new TimeZoneLabelProvider(ir)));
    treeViewer.setContentProvider(new TimeZoneContentProvider());
    treeViewer.setInput(new Object[] { TimeZoneTreeView.getTimeZones() });

    getSite().setSelectionProvider(treeViewer);

    MenuManager manager = new MenuManager("#PopupMenu");
    Menu menu = manager.createContextMenu(treeViewer.getControl());
    treeViewer.getControl().setMenu(menu);
    Action deprecated = new Action() {
      public void run() {
        MessageDialog.openInformation(null, "Hello", "World");
      }
    };
    deprecated.setText("Hello");
    manager.add(deprecated);
  }

  @Override
  public void setFocus() {
    treeViewer.getControl().setFocus();
  }

  public static Map<String, Set<TimeZone>> getTimeZones() {
    String[] ids = TimeZone.getAvailableIDs();
    Map<String, Set<TimeZone>> timeZones = new TreeMap<>();
    for (int i = 0; i < ids.length; ++i) {
      String[] parts = ids[i].split("/");
      if (parts.length == 2) {
        String region = parts[0];
        Set<TimeZone> zones = timeZones.get(region);
        if (zones == null) {
          zones = new TreeSet<>((o1, o2) -> {
            if (o1 instanceof TimeZone && o2 instanceof TimeZone) {
              return ((TimeZone) o1).getID().compareTo(((TimeZone) o2).getID());
            } else {
              throw new IllegalArgumentException();
            }
          });
          timeZones.put(region, zones);
        }
        TimeZone timeZone = TimeZone.getTimeZone(ids[i]);
        zones.add(timeZone);
      }
    }
    return timeZones;
  }
}
