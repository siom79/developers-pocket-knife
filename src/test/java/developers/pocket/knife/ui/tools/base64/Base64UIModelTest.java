package developers.pocket.knife.ui.tools.base64;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Base64UIModelTest {

    @Test
    public void testEncode() {
        Base64UIModel subject = new Base64UIModel();
        subject.setInputText("abcd");
        subject.encode();
        assertThat(subject.getOutputText(), is("YWJjZA=="));
    }

    @Test
    public void testDecode() {
        Base64UIModel subject = new Base64UIModel();
        subject.setInputText("YWJjZA==");
        subject.decode();
        assertThat(subject.getOutputText(), is("abcd"));
    }
}
