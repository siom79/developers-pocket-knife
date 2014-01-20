package developers.pocket.knife;

import developers.pocket.knife.ui.MainFrame;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class App {
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
}
