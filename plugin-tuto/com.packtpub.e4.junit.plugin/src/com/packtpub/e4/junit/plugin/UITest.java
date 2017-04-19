package com.packtpub.e4.junit.plugin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class UITest {

  private static SWTBot bot;

  @BeforeClass
  public static void beforeClass() {
    bot = new SWTBot();
  }

  @Test
  public void testUI() {
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
    String projectName = "SWTBot Test Project";

    assertFalse(ResourcesPlugin.getWorkspace().getRoot().getProject(projectName)
        .exists());

    bot.menu("File").menu("New").menu("Project...").click();
    SWTBotShell shell = bot.shell("New Project");
    shell.activate();
    bot.tree().expandNode("General").select("Project");
    bot.button("Next >").click();
    bot.textWithLabel("Project name:").setText("SWTBot Test Project");
    bot.button("Finish").click();

    assertTrue(ResourcesPlugin.getWorkspace().getRoot().getProject(projectName)
        .exists());
  }

  @AfterClass
  public static void sleep() {
    bot.sleep(2000);
  }
}
