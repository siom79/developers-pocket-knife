package developers.pocket.knife.ui.factory;

import javax.enterprise.inject.Produces;
import javax.swing.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ComponentFactory {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface UiComponent {

    }
}
