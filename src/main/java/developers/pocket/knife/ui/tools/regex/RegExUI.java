package developers.pocket.knife.ui.tools.regex;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import developers.pocket.knife.i18n.Messages;
import developers.pocket.knife.ui.factory.ComponentFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegExUI extends JPanel {
    @Inject
    Messages messages;
    @Inject
    JTextField inputRegEx;
    @Inject
    JTextArea inputTestData;
    @Inject @ComponentFactory.NotEditable
    JTextArea outputResult;
    @Inject
    JButton buttonApply;
    @Inject
    RegExModel regExModel;

    @PostConstruct
    public void postConstruct() {
        buildComponents();
    }

    void buildComponents() {
        final JPanel panelRef = this;
        buttonApply.setText(messages.apply());
        buttonApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputRegExText = inputRegEx.getText();
                if(!validateInputText(inputRegExText, panelRef, RegExUI.this.messages.noRegEx())) {
                    return;
                }
                String inputTestDataText = inputTestData.getText();
                if(!validateInputText(inputTestDataText, panelRef, RegExUI.this.messages.noTestData())) {
                    return;
                }
                String output = regExModel.applyRegEx(inputRegExText, inputTestDataText);
                outputResult.setText(output);
            }
        });
    }

    private boolean validateInputText(String input, JPanel panelRef, String message) {
        if(input.trim().length() == 0) {
            JOptionPane.showMessageDialog(panelRef,
                    message,
                    messages.error(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public JPanel buildUI() {
        FormLayout formLayout = new FormLayout("fill:p:grow", "p, 3dlu, fill:p:grow, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, fill:p:grow");

        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(formLayout);
        builder.setDefaultDialogBorder();

        builder.add(new JLabel(messages.input() + ":"), cc.xy(1, 1));
        builder.add(new JScrollPane(inputTestData), cc.xy(1, 3));
        builder.add(new JLabel(messages.regex() + ":"), cc.xy(1, 5));
        builder.add(inputRegEx, cc.xy(1, 7));
        builder.add(buildButtonPanel(), cc.xy(1, 9));
        builder.add(new JLabel(messages.output() + ":"), cc.xy(1, 11));
        builder.add(new JScrollPane(outputResult), cc.xy(1, 13));

        return builder.getPanel();
    }

    public JPanel buildButtonPanel() {
        FormLayout formLayout = new FormLayout("p", "p");

        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(formLayout);

        builder.add(buttonApply, cc.xy(1, 1));

        return builder.getPanel();
    }
}
