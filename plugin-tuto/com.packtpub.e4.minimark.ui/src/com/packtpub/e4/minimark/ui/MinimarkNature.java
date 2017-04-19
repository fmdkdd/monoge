package com.packtpub.e4.minimark.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class MinimarkNature implements IProjectNature {

  public static final String ID = "com.packtpub.e4.minimark.ui.MinimarkNature";
  private IProject project;

  @Override
  public void configure() throws CoreException {
    IProjectDescription desc = project.getDescription();
    ICommand[] commands = desc.getBuildSpec();
    // Check if the MinimarkBuilder is already present
    for (ICommand command : commands) {
      if (command.getBuilderName().equals(MinimarkBuilder.ID)) {
        return;
      }
    }
    // If not, add it to the existing commands
    ICommand newCommand = desc.newCommand();
    newCommand.setBuilderName(MinimarkBuilder.ID);
    // Since it's an array, copy it to a list, add the new command there, and
    // replace the list of commands
    List<ICommand> newCommands = new ArrayList<>(Arrays.asList(commands));
    newCommands.add(newCommand);
    desc.setBuildSpec(newCommands.toArray(new ICommand[0]));
    project.setDescription(desc, null);
  }

  @Override
  public void deconfigure() throws CoreException {
    IProjectDescription desc = project.getDescription();
    // Remove the builder if present in the array
    List<ICommand> commands = new ArrayList<>(
        Arrays.asList(desc.getBuildSpec()));
    Iterator<ICommand> iterator = commands.iterator();
    while (iterator.hasNext()) {
      ICommand command = iterator.next();
      if (command.getBuilderName().equals(MinimarkBuilder.ID)) {
        iterator.remove();
      }
    }
    desc.setBuildSpec(commands.toArray(new ICommand[0]));
    project.setDescription(desc, null);
  }

  @Override
  public IProject getProject() {
    return project;
  }

  @Override
  public void setProject(IProject project) {
    this.project = project;
  }

}
