package developers.pocket.knife.ui;

import developers.pocket.knife.i18n.Messages;
import developers.pocket.knife.lifecycle.LifeCycle;
import developers.pocket.knife.ui.tools.base64.Base64UI;
import developers.pocket.knife.ui.tools.base64.Base64UIModel;
import developers.pocket.knife.ui.tools.classfinder.ClassFinderUI;
import developers.pocket.knife.ui.tools.validatexml.ValidateXmlUI;

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
    @Inject
    Instance<ClassFinderUI> findClassUIInstance;
    @Inject
    Instance<ValidateXmlUI> validateXmlUIInstance;

    public JMenuBar createMenuBar(Container contentPane) {
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add(createFileMenu());
        jMenuBar.add(createToolsMenu(contentPane));
        return jMenuBar;
    }

    private JMenu createToolsMenu(Container contentPane) {
        JMenu menu = new JMenu(messages.tools());
        menu.setMnemonic(KeyEvent.VK_T);
        menu.add(createToolsBase64MenuItem(contentPane));
        menu.add(createToolsValidateXmlMenuItem(contentPane));
        menu.add(createToolsFindClassItem(contentPane));
        return menu;
    }

    private JMenuItem createToolsFindClassItem(final Container contentPane) {
        JMenuItem menuItem = new JMenuItem(messages.findClass(), KeyEvent.VK_C);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                replacePanel(contentPane, new PanelFactory() {
                    @Override
                    public JPanel createPanel() {
                        return findClassUIInstance.get().buildUI();
                    }
                });
            }
        });
        return menuItem;
    }

    private JMenuItem createToolsValidateXmlMenuItem(final Container contentPane) {
        JMenuItem menuItem = new JMenuItem(messages.validateXml(), KeyEvent.VK_X);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                replacePanel(contentPane, new PanelFactory() {
                    @Override
                    public JPanel createPanel() {
                        return validateXmlUIInstance.get().buildUi();
                    }
                });
            }
        });
        return menuItem;
    }

    private JMenuItem createToolsBase64MenuItem(final Container contentPane) {
        JMenuItem menuItem = new JMenuItem(messages.base64(), KeyEvent.VK_B);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                replacePanel(contentPane, new PanelFactory() {
                    @Override
                    public JPanel createPanel() {
                        return base64UIInstance.get().buildUi();
                    }
                });
            }
        });
        return menuItem;
    }

    private void replacePanel(Container contentPane, PanelFactory panelFactory) {
        contentPane.removeAll();
        JPanel jPanel = panelFactory.createPanel();
        contentPane.add(jPanel);
        jPanel.repaint();
        contentPane.validate();
        contentPane.repaint();
    }

    private interface PanelFactory {
        JPanel createPanel();
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
