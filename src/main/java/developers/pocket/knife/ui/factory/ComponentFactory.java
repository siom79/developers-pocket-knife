package developers.pocket.knife.ui.factory;

import javax.enterprise.inject.Produces;
import javax.swing.*;
import java.awt.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ComponentFactory {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface InputField {

    }

    @Produces
    public JTextField producesTextField() {
        JTextField textField = new JTextField();
        textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return textField;
    }

    @Produces
    public JButton producesButton() {
        return new JButton();
    }

    @Produces
    public JTextArea produceTextArea() {
        return new JTextArea();
    }
}
