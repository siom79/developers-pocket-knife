package developers.pocket.knife.ui.tools.base64;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;

public class Base64UI extends JPanel {
    private JButton button;

    public Base64UI(Base64UIModel presentationModel) {
        buildComponents();
    }

    private void buildComponents() {
        button = new JButton();
    }

    public JPanel buildUi() {
        FormLayout formLayout = new FormLayout("p, 3dlu, fill:p:grow", "fill:p:grow");

        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(formLayout);
        builder.setDefaultDialogBorder();

        builder.add(button, cc.xy(1, 1));

        return builder.getPanel();
    }
}
