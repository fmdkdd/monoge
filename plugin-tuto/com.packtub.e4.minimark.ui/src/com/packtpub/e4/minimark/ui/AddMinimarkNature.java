package com.packtpub.e4.minimark.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class AddMinimarkNature extends AbstractHandler implements IHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    ISelection sel = HandlerUtil.getCurrentSelection(event);
    if (sel instanceof IStructuredSelection) {
      Iterator<?> it = ((IStructuredSelection) sel).iterator();
      while (it.hasNext()) {
        Object object = (Object) it.next();
        if (object instanceof IProject) {
          try {
            addProjectNature((IProject) object, MinimarkNature.ID);
          } catch (CoreException ex) {
            throw new ExecutionException("Failed to set nature on " + object,
                ex);
          }
        }
      }
    }
    return null;
  }

  protected void addProjectNature(IProject project, String nature)
      throws CoreException {
    IProjectDescription desc = project.getDescription();
    List<String> natures = new ArrayList<>(Arrays.asList(desc.getNatureIds()));
    if (!desc.hasNature(nature)) {
      natures.add(nature);
      desc.setNatureIds(natures.toArray(new String[0]));
      project.setDescription(desc, IProject.FORCE, null);
    }
  }

}
