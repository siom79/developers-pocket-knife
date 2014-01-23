package developers.pocket.knife.ui.tools.base64;

import developers.pocket.knife.exceptions.BusinessException;
import developers.pocket.knife.i18n.Messages;
import org.apache.commons.codec.binary.Base64;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;

public class Base64UIModel {
    @Inject
    Messages messages;
    private String inputText;
    private String outputText;

    public void encode() throws BusinessException {
        if (this.inputText != null) {
            String charsetName = "UTF-8";
            try {
                this.outputText = Base64.encodeBase64String(this.inputText.getBytes(charsetName));
            } catch (UnsupportedEncodingException e) {
                throw new BusinessException(BusinessException.Reason.UnsupportedEncoding, messages.unsupportedEncoding(charsetName));
            }
        } else {
            this.outputText = "";
        }
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getInputText() {
        return inputText;
    }

    public String getOutputText() {
        return outputText;
    }

    public void setOutputText(String outputText) {
        this.outputText = outputText;
    }

    public void decode() throws BusinessException {
        if (this.inputText != null) {
            byte[] bytes = Base64.decodeBase64(this.inputText);
            String charsetName = "UTF-8";
            try {
                this.outputText = new String(bytes, charsetName);
            } catch (UnsupportedEncodingException e) {
                throw new BusinessException(BusinessException.Reason.UnsupportedEncoding, messages.unsupportedEncoding(charsetName));
            }
        } else {
            this.outputText = "";
        }
    }
}
