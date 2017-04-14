package com.packtub.e4.clock.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

public class HelloHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Job job = new Job("About to say hello") {
      public IStatus run(IProgressMonitor monitor) {
        try {
          monitor.beginTask("Preparing", 5000);
          for (int i = 0; i < 50; ++i) {
            Thread.sleep(100);
            monitor.worked(100);
          }
        } catch (InterruptedException e) {
        } finally {
          monitor.done();
        }
        PlatformUI.getWorkbench().getDisplay().asyncExec(() -> {
          MessageDialog.openInformation(null, "Hello", "World");
        });
        return Status.OK_STATUS;
      }
    };
    job.schedule();
    return null;
  }

}
