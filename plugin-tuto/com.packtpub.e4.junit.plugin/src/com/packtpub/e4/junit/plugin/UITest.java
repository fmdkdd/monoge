package com.packtpub.e4.junit.plugin;

import static org.junit.Assert.assertEquals;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class UITest {

  @Test
  public void testUI() {
    SWTWorkbenchBot bot = new SWTWorkbenchBot();
    SWTBotShell[] shells = bot.shells();
    for (SWTBotShell s : shells) {
      if (s.isVisible()) {
        assertEquals("junit-workspace - Resource - Eclipse Platform",
            s.getText());
      }
    }
  }

  @Test
  public void createProject() {
    SWTWorkbenchBot bot = new SWTWorkbenchBot();
    bot.menu("File").menu("New").menu("Project...").click();
    SWTBotShell shell = bot.shell("New Project");
    shell.activate();
    bot.tree().expandNode("General").select("Project");
    bot.button("Next >").click();
    bot.textWithLabel("Project name:").setText("SWTBot Test Project");
    bot.button("Finish").click();
  }
}
