package developers.pocket.knife.ui;

import developers.pocket.knife.config.ConfigurationFactory;
import developers.pocket.knife.i18n.Messages;
import developers.pocket.knife.lifecycle.LifeCycle;
import developers.pocket.knife.ui.tools.base64.Base64UI;
import developers.pocket.knife.ui.tools.classfinder.ClassFinderUI;
import developers.pocket.knife.ui.tools.regex.RegExUI;
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
    @Inject
    Instance<RegExUI> regExUIInstance;
    @Inject
    @ConfigurationFactory.ConfigurationValue(key = ConfigurationFactory.ConfigurationKey.Version)
    String version;
    @Inject
    @ConfigurationFactory.ConfigurationValue(key = ConfigurationFactory.ConfigurationKey.BuildTimestamp)
    String buildTimestamp;

    public JMenuBar createMenuBar(Container contentPane) {
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add(createFileMenu());
        jMenuBar.add(createToolsMenu(contentPane));
        jMenuBar.add(createInfoMenu(contentPane));
        return jMenuBar;
    }

    private JMenu createInfoMenu(Container contentPane) {
        JMenu menu = new JMenu(messages.info());
        menu.setMnemonic(KeyEvent.VK_I);
        menu.add(createAboutMenuItem(contentPane));
        return menu;
    }

    private JMenuItem createAboutMenuItem(final Container contentPane) {
        JMenuItem menuItem = new JMenuItem(messages.about());
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(contentPane,
                        messages.version() + ": " + version + "\n" +
                                messages.timestamp() + ": " + buildTimestamp,
                        messages.about(),
                        JOptionPane.PLAIN_MESSAGE);
            }
        });
        return menuItem;
    }

    private JMenu createToolsMenu(Container contentPane) {
        JMenu menu = new JMenu(messages.tools());
        menu.setMnemonic(KeyEvent.VK_T);
        menu.add(createToolsBase64MenuItem(contentPane));
        menu.add(createToolsValidateXmlMenuItem(contentPane));
        menu.add(createToolsFindClassItem(contentPane));
        menu.add(createToolsRegExItem(contentPane));
        return menu;
    }

    private JMenuItem createToolsRegExItem(final Container contentPane) {
        JMenuItem menuItem = new JMenuItem(messages.testRegEx(), KeyEvent.VK_R);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                replacePanel(contentPane, new PanelFactory() {
                    @Override
                    public JPanel createPanel() {
                        return regExUIInstance.get().buildUI();
                    }
                });
            }
        });
        return menuItem;
    }

    private JMenuItem createToolsFindClassItem(final Container contentPane) {
        JMenuItem menuItem = new JMenuItem(messages.findFile(), KeyEvent.VK_C);
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
