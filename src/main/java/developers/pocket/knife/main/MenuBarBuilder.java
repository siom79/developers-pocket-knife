package developers.pocket.knife.main;

import developers.pocket.knife.i18n.Messages;
import developers.pocket.knife.lifecycle.LifeCycle;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MenuBarBuilder {
    @Inject
    Messages messages;
    @Inject
    LifeCycle lifeCycle;

    public JMenuBar createMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add(createFileMenu());
        jMenuBar.add(createToolsMenu());
        return jMenuBar;
    }

    private JMenu createToolsMenu() {
        JMenu fileMenu = new JMenu(messages.tools());
        fileMenu.setMnemonic(KeyEvent.VK_T);
        fileMenu.add(createToolsBase64MenuItem());
        return fileMenu;
    }

    private JMenuItem createToolsBase64MenuItem() {
        JMenuItem exitFileMenuItem = new JMenuItem(messages.base64(), KeyEvent.VK_E);
        exitFileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        return exitFileMenuItem;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu(messages.file());
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.add(createExitFileMenuItem());
        return fileMenu;
    }

    private JMenuItem createExitFileMenuItem() {
        JMenuItem exitFileMenuItem = new JMenuItem(messages.exit(), KeyEvent.VK_E);
        exitFileMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lifeCycle.exit();
            }
        });
        return exitFileMenuItem;
    }
}
