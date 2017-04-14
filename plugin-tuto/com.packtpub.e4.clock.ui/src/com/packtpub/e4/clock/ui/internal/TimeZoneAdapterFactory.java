package com.packtpub.e4.clock.ui.internal;

import java.util.TimeZone;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

public class TimeZoneAdapterFactory implements IAdapterFactory {

  @Override
  public <T> T getAdapter(Object o, Class<T> type) {
    if (type == IPropertySource.class && o instanceof TimeZone) {
      return type.cast(new TimeZonePropertySource((TimeZone) o));
    } else {
      // Would rather throw, since this shouldn't happen
      return null;
    }
  }

  @Override
  public Class<?>[] getAdapterList() {
    return new Class[] { IPropertySource.class };
  }

}
