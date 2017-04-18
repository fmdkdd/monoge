package com.packtpub.e4.minimark.ui;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

public class MinimarkBuilder extends IncrementalProjectBuilder {
  // Should coincide with the builder name in the plugin.xml builder extension
  public static final String ID = "com.packtpub.e4.minimark.ui.MinimarkBuilder";

  public MinimarkBuilder() {
  }

  @Override
  protected IProject[] build(int kind, Map<String, String> args,
                             IProgressMonitor monitor)
      throws CoreException {
    if (kind == FULL_BUILD) {
      fullBuild(getProject(), monitor);
    } else {
      incrementalBuild(getProject(), monitor, getDelta(getProject()));
    }
    return null;
  }

  protected void incrementalBuild(IProject project, IProgressMonitor monitor,
                                  IResourceDelta delta)
      throws CoreException {
    if (delta == null) {
      fullBuild(project, monitor);
    } else {
      delta.accept(new MinimarkVisitor(monitor));
    }
  }

  protected void fullBuild(IProject project, IProgressMonitor monitor)
      throws CoreException {
    project.accept(new MinimarkVisitor(monitor), IResource.NONE);
  }

  @Override
  protected void clean(IProgressMonitor monitor) throws CoreException {
    // Visit the project and delete any HTML file that has the same name as a
    // minimark, and is derived (to avoid deleting manually created HTML files
    // which whould happen to have the same name)
    getProject().accept((proxy) -> {
      String name = proxy.getName();
      if (name.endsWith(".html") && proxy.isDerived()) {
        String minimarkName = name.replace(".html", ".minimark");
        IFile minimarkFile = proxy.getParent().getFile(new Path(minimarkName));
        if (minimarkFile.exists()) {
          proxy.delete(true, monitor);
        }
      }

      // Visit every resource in the project
      return true;
    });
  }

}
