package developers.pocket.knife.ui.tools.base64;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import developers.pocket.knife.i18n.Messages;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Base64UI extends JPanel {
    @Inject
    Messages messages;
    @Inject
    JTextArea inputField;
    @Inject
    JTextArea outputField;
    @Inject
    JButton buttonEncode;
    @Inject
    JButton buttonDecode;
    @Inject
    Base64UIModel presentationModel;

    @PostConstruct
    public void postConstruct() {
        buildComponents();
    }

    private void buildComponents() {
        inputField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        buttonEncode.setText(messages.encode());
        buttonEncode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presentationModel.setInputText(inputField.getText());
                presentationModel.encode();
                outputField.setText(presentationModel.getOutputText());
            }
        });
        buttonDecode.setText(messages.decode());
        buttonDecode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presentationModel.setInputText(inputField.getText());
                presentationModel.decode();
                outputField.setText(presentationModel.getOutputText());
            }
        });
        outputField.setEditable(false);
        outputField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        outputField.setBackground(new Color(245, 245, 245));
    }

    public JPanel buildUi() {
        FormLayout formLayout = new FormLayout("fill:p:grow", "p, 3dlu, fill:p:grow, 3dlu, p, 3dlu, p, 3dlu, fill:p:grow");

        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(formLayout);
        builder.setDefaultDialogBorder();

        builder.add(new JLabel(messages.input() + ":"), cc.xy(1, 1));
        builder.add(inputField, cc.xy(1, 3));
        builder.add(buildButtonPanel(), cc.xy(1, 5));
        builder.add(new JLabel(messages.output() + ":"), cc.xy(1, 7));
        builder.add(outputField, cc.xy(1, 9));

        return builder.getPanel();
    }

    public JPanel buildButtonPanel() {
        FormLayout formLayout = new FormLayout("fill:p:grow, 3dlu, fill:p:grow", "p");

        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(formLayout);

        builder.add(buttonEncode, cc.xy(1, 1));
        builder.add(buttonDecode, cc.xy(3, 1));

        return builder.getPanel();
    }
}
