package developers.pocket.knife;

import com.jgoodies.plaf.Options;
import developers.pocket.knife.i18n.Messages;
import developers.pocket.knife.ui.MainFrame;
import jb5n.api.JB5n;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.*;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    @Inject
    MainFrame mainFrame;

    public static void main(String args[]) {
        App app = new App();
        app.run();
    }

    private void run() {
        configure();
        bootstrapDiContainer();
    }

    private void configure() {
        configureLogging();
        configureLookAndFeel();
        configureGlobalExceptionHandling();
    }

    private void configureLogging() {
        PropertyConfigurator.configure(App.class.getClassLoader().getResourceAsStream("log4j.properties"));
    }

    @PostConstruct
    public void postConstruct() {
        mainFrame.setVisible(true);
    }

    private void bootstrapDiContainer() {
        Weld weld = new Weld();
        WeldContainer weldContainer = weld.initialize();
        weldContainer.instance().select(App.class).get();
    }

    private void configureLookAndFeel() {
        try {
            String lafName = Options.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lafName);
        } catch (Exception e) {
            LOGGER.error("Setting look and feel failed: " + e.getMessage(), e);
        }
    }

    public void configureGlobalExceptionHandling() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Messages messages = JB5n.createInstance(Messages.class);
                JOptionPane.showMessageDialog(null,
                        messages.internalError()+": "+e.getLocalizedMessage(),
                        messages.error(),
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
