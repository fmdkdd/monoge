package com.packtpub.e4.minimark.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;

public class MinimarkVisitor
    implements IResourceProxyVisitor, IResourceDeltaVisitor {

  private IProgressMonitor monitor;

  public MinimarkVisitor(IProgressMonitor monitor) {
    this.monitor = monitor;
  }

  @Override
  public boolean visit(IResourceDelta delta) throws CoreException {
    boolean deleted = (IResourceDelta.REMOVED & delta.getKind()) != 0;
    IResource resource = delta.getResource();
    String name = resource.getName();
    if (name.endsWith(".minimark")) {
      // Deleting the minimark file should delete the HTML
      if (deleted) {
        String htmlName = name.replace(".minimark", ".html");
        IFile htmlFile = resource.getParent().getFile(new Path(htmlName));
        if (htmlFile.exists()) {
          htmlFile.delete(true, monitor);
        }
        // Modifying the minimark should update the HTML
      } else {
        processResource(resource);
      }
    } else if (name.endsWith(".html")) {
      // Any modification to the HTML is discarded, as we regenerate it from the
      // minimark file
      String minimarkName = name.replace(".html", ".minimark");
      IFile minimarkFile = resource.getParent().getFile(new Path(minimarkName));
      if (minimarkFile.exists()) {
        processResource(minimarkFile);
      }
    }
    return true;
  }

  @Override
  public boolean visit(IResourceProxy proxy) throws CoreException {
    String name = proxy.getName();
    if (name != null && name.endsWith(".minimark")) {
      processResource(proxy.requestResource());
    }
    return true;
  }

  protected void processResource(IResource resource) throws CoreException {
    resource.deleteMarkers("com.packtpub.e4.minimark.ui.MinimarkMarker", true,
        IResource.DEPTH_INFINITE);

    if (resource instanceof IFile && resource.exists()) {
      try {
        // Translate the file contents to HTML
        IFile file = (IFile) resource;
        InputStream in = file.getContents();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MinimarkTranslator.convert(new InputStreamReader(in),
            new OutputStreamWriter(baos));
        ByteArrayInputStream contents = new ByteArrayInputStream(
            baos.toByteArray());
        // Check if there is anything in the file
        URI fileURI = file.getLocationURI();
        if (fileURI != null) {
          if (EFS.getLocalFileSystem().getStore(fileURI).fetchInfo()
              .getLength() == 0) {
            IMarker marker = resource
                .createMarker("com.packtpub.e4.minimark.ui.MinimarkMarker");
            marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
            marker.setAttribute(IMarker.MESSAGE, "Minimark file is empty");
            marker.setAttribute(IMarker.LINE_NUMBER, 0);
            marker.setAttribute(IMarker.CHAR_START, 0);
            marker.setAttribute(IMarker.CHAR_END, 0);
          }
        }
        // Write contents to an HTML file
        String htmlName = file.getName().replace(".minimark", ".html");
        IFile htmlFile = file.getParent().getFile(new Path(htmlName));
        if (htmlFile.exists()) {
          htmlFile.setContents(contents, true, false, monitor);
        } else {
          htmlFile.create(contents, true, monitor);
        }
        // Inform Eclipse this is an automatically generated file
        htmlFile.setDerived(true, null);
      } catch (IOException ex) {
        throw new CoreException(new Status(Status.ERROR, Activator.PLUGIN_ID,
            "Failed to generate resource", ex));
      }
    }
  }

}
