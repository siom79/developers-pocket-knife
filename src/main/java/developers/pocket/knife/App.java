package developers.pocket.knife;

import com.jgoodies.plaf.Options;
import developers.pocket.knife.ui.MainFrame;
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
        bootstrapDiContainter();
    }

    private void configure() {
        configureLogging();
        configureLookAndFeel();
    }

    private void configureLogging() {
        PropertyConfigurator.configure(App.class.getClassLoader().getResourceAsStream("log4j.properties"));
    }

    @PostConstruct
    public void postConstruct() {
        mainFrame.setVisible(true);
    }

    private void bootstrapDiContainter() {
        Weld weld = new Weld();
        WeldContainer weldContainer = weld.initialize();
        weldContainer.instance().select(App.class).get();
    }

    private static void configureLookAndFeel() {
        try {
            String lafName = Options.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lafName);
        } catch (Exception e) {
            LOGGER.error("Setting look and feel failed: " + e.getMessage(), e);
        }
    }
}
