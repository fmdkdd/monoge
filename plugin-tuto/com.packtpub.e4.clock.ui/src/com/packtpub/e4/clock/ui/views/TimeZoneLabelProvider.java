package com.packtpub.e4.clock.ui.views;

import java.util.Map;
import java.util.TimeZone;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

public class TimeZoneLabelProvider extends LabelProvider implements IStyledLabelProvider {
  private final ImageRegistry ir;

  public TimeZoneLabelProvider(ImageRegistry ir) {
    this.ir = ir;
  }

  public String getText(Object element) {
    if (element instanceof Map) {
      return "Time Zones";
    } else if (element instanceof Map.Entry) {
      return ((Map.Entry) element).getKey().toString();
    } else if (element instanceof TimeZone) {
      return ((TimeZone) element).getID().split("/")[1];
    } else {
      return "Unknown type: " + element.getClass();
    }
  }

  public Image getImage(Object element) {
    if (element instanceof Map.Entry) {
      return ir.get("sample");
    } else if (element instanceof TimeZone) {
      return ir.get("sample");
    } else {
      return super.getImage(element);
    }
  }

  @Override
  public StyledString getStyledText(Object element) {
    String text = getText(element);
    StyledString ss = new StyledString(text);
    if (element instanceof TimeZone) {
      int offset = -((TimeZone) element).getOffset(0);
      ss.append(" (" + offset / 3600000 + "h)", StyledString.DECORATIONS_STYLER);
    }
    return ss;
  }
}
