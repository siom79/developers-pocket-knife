package developers.pocket.knife.ui.tools.classfinder;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import developers.pocket.knife.i18n.Messages;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.*;

public class ClassFinderUI extends ClassFinderUIModel {
    @Inject
    Messages messages;
    @Inject
    JTextField inputDirectory;
    @Inject
    JButton buttonDirectory;

    @PostConstruct
    public void postConstruct() {
        buildComponents();
    }

    private void buildComponents() {
        buttonDirectory.setText(messages.choose()+"...");
    }

    public JPanel buildUI() {
        FormLayout formLayout = new FormLayout("right:p, 3dlu, fill:p:grow, 3dlu, p", "p, 3dlu, p, 3dlu, p, 3dlu, fill:p:grow");

        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(formLayout);
        builder.setDefaultDialogBorder();

        builder.add(new JLabel(messages.directory() + ":"), cc.xy(1, 1));
        builder.add(inputDirectory, cc.xy(3, 1));
        builder.add(buttonDirectory, cc.xy(5, 1));

        return builder.getPanel();
    }
}
