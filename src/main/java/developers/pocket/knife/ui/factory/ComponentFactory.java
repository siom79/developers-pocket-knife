package developers.pocket.knife.ui.factory;

import javax.enterprise.inject.Produces;
import javax.inject.Qualifier;
import javax.swing.*;
import java.awt.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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

    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier
    @Target({ElementType.FIELD, ElementType.METHOD})
    public @interface NotEditable {

    }

    @Produces
    public JTextArea produceTextArea() {
        return new JTextArea();
    }

    @Produces @NotEditable
    public JTextArea produceNotEditableTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        textArea.setBackground(new Color(245, 245, 245));
        return textArea;
    }

    @Produces
    public JTable produceTable() {
        return new JTable();
    }

    @Produces
    public JCheckBox produceCheckBox() {
        return new JCheckBox();
    }

    @Produces
    public JLabel produceLabel() {
        return new JLabel();
    }
}
