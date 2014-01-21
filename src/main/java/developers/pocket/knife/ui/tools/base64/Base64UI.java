package developers.pocket.knife.ui.tools.base64;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import developers.pocket.knife.i18n.Messages;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Base64UI extends JPanel {
    Messages messages;
    private JTextArea inputField;
    private JButton buttonEncode;
    private Base64UIModel presentationModel;

    @Inject
    public Base64UI(Base64UIModel presentationModel, Messages messages) {
        this.presentationModel = presentationModel;
        this.messages = messages;
        buildComponents();
    }

    private void buildComponents() {
        inputField = new JTextArea();
        buttonEncode = new JButton();
        buttonEncode.setText(messages.encode());
        buttonEncode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presentationModel.encode();
            }
        });
    }

    public JPanel buildUi() {
        FormLayout formLayout = new FormLayout("fill:p:grow", "fill:p:grow, 3dlu, p");

        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(formLayout);
        builder.setDefaultDialogBorder();

        builder.add(inputField, cc.xy(1, 1));
        builder.add(buttonEncode, cc.xy(1, 3));

        return builder.getPanel();
    }
}
