package developers.pocket.knife.main;

import developers.pocket.knife.i18n.Messages;
import developers.pocket.knife.lifecycle.LifeCycle;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    @Inject
    MenuBarBuilder menuBarBuilder;
    @Inject
    Messages messages;
    @Inject
    LifeCycle lifeCycle;

    @PostConstruct
    public void postConstruct() {
        setSize(800, 600);
        setLocation(100, 100);
        setTitle(messages.title());
        setJMenuBar(menuBarBuilder.createMenuBar());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                lifeCycle.exit();
            }
        });
    }
}
