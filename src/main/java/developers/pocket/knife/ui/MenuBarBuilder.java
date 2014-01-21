package developers.pocket.knife.ui;

import developers.pocket.knife.i18n.Messages;
import developers.pocket.knife.lifecycle.LifeCycle;
import developers.pocket.knife.ui.tools.base64.Base64UI;
import developers.pocket.knife.ui.tools.base64.Base64UIModel;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MenuBarBuilder {
    @Inject
    Messages messages;
    @Inject
    LifeCycle lifeCycle;
    @Inject
    Instance<Base64UI> base64UIInstance;

    public JMenuBar createMenuBar(Container contentPane) {
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add(createFileMenu());
        jMenuBar.add(createToolsMenu(contentPane));
        return jMenuBar;
    }

    private JMenu createToolsMenu(Container contentPane) {
        JMenu fileMenu = new JMenu(messages.tools());
        fileMenu.setMnemonic(KeyEvent.VK_T);
        fileMenu.add(createToolsBase64MenuItem(contentPane));
        return fileMenu;
    }

    private JMenuItem createToolsBase64MenuItem(final Container contentPane) {
        JMenuItem exitFileMenuItem = new JMenuItem(messages.base64(), KeyEvent.VK_E);
        exitFileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPane.removeAll();
                Base64UI base64UI = base64UIInstance.get();
                JPanel jPanel = base64UI.buildUi();
                contentPane.add(jPanel);
                jPanel.repaint();
                contentPane.validate();
                contentPane.repaint();
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
