package developers.pocket.knife.ui.tools.validatexml;

import com.google.common.base.Optional;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import developers.pocket.knife.i18n.Messages;
import jb5n.api.Message;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.html.Option;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ValidateXmlUI extends JPanel {
    private ValidateXmlUIModel model;
    private Messages messages;
    private JTextField inputXmlFile = new JTextField();
    private JButton buttonXmlFile = new JButton();
    private JTextField inputSchemaFile = new JTextField();
    private JButton buttonSchemaFile = new JButton();
    private JButton buttonValidate = new JButton();

    @Inject
    public ValidateXmlUI(ValidateXmlUIModel model, Messages messages) {
        this.model = model;
        this.messages = messages;
        buildComponents();
    }

    private void buildComponents() {
        inputXmlFile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        buttonXmlFile.setText(messages.choose() + "...");
        buttonXmlFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int showOpenDialog = fc.showOpenDialog(getRootPane());
                if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fc.getSelectedFile();
                    model.setXmlFile(selectedFile);
                    inputXmlFile.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        inputSchemaFile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        buttonSchemaFile.setText(messages.choose()+"...");
        buttonSchemaFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int showOpenDialog = fc.showOpenDialog(getRootPane());
                if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fc.getSelectedFile();
                    model.setSchemaFile(selectedFile);
                    inputSchemaFile.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        buttonValidate.setText(messages.validate());
        buttonValidate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.validate();
            }
        });
    }

    public JPanel buildUi() {
        FormLayout formLayout = new FormLayout("right:p, 3dlu, fill:p:grow, 3dlu, p", "p, 3dlu, p, 3dlu, p");

        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(formLayout);
        builder.setDefaultDialogBorder();

        builder.add(new JLabel(messages.xmlFile() + ":"), cc.xy(1, 1));
        builder.add(inputXmlFile, cc.xy(3, 1));
        builder.add(buttonXmlFile, cc.xy(5, 1));

        builder.add(new JLabel(messages.schemaFile() + ":"), cc.xy(1, 3));
        builder.add(inputSchemaFile, cc.xy(3, 3));
        builder.add(buttonSchemaFile, cc.xy(5, 3));

        builder.add(buttonValidate, cc.xy(1, 5));

        return builder.getPanel();
    }
}
