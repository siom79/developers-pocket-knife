package developers.pocket.knife.ui.tools.base64;

import developers.pocket.knife.i18n.Messages;
import org.junit.Test;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class Base64UITest {

    @Test
    public void testEncode() {
        Base64UI base64UI = new Base64UI();
        base64UI.buttonEncode = new JButton();
        base64UI.buttonDecode = new JButton();
        base64UI.inputField = new JTextArea();
        base64UI.outputField = new JTextArea();
        base64UI.messages = mock(Messages.class);
        base64UI.presentationModel = new Base64UIModel();
        base64UI.buildComponents();
        base64UI.inputField.setText("abcd");
        base64UI.buttonEncode.getActionListeners()[0].actionPerformed(mock(ActionEvent.class));
        assertThat(base64UI.outputField.getText(), is("YWJjZA=="));
    }
}
