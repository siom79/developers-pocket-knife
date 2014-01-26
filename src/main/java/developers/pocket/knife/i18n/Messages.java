package developers.pocket.knife.i18n;

import jb5n.api.Message;
import jb5n.api.MessageResource;

import javax.swing.*;

@MessageResource(resourceBundleName = "messages")
public interface Messages {

    @Message(defaultMessage = "Developer's Knife")
    String title();

    @Message(defaultMessage = "File")
    String file();

    @Message(defaultMessage = "Exit")
    String exit();

    @Message(defaultMessage = "Tools")
    String tools();

    @Message(defaultMessage = "Base64")
    String base64();

    @Message(defaultMessage = "Encode")
    String encode();

    @Message(defaultMessage = "The encoding {0} is not supported")
    String unsupportedEncoding(String encoding);

    @Message(defaultMessage = "Decode")
    String decode();

    @Message(defaultMessage = "Input")
    String input();

    @Message(defaultMessage = "Output")
    String output();

    @Message(defaultMessage = "Validate XML file")
    String validateXml();

    @Message(defaultMessage = "XML File")
    String xmlFile();

    @Message(defaultMessage = "Choose")
    String choose();

    @Message(defaultMessage = "Schema File")
    String schemaFile();

    @Message(defaultMessage = "Validate")
    String validate();
}
