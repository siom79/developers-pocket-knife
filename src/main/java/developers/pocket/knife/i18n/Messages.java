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

    @Message(defaultMessage = "The file {0} does not exist.")
    String fileDoesNotExist(String filename);

    @Message(defaultMessage = "Error")
    String error();

    @Message(defaultMessage = "You haven't provided any XML file.")
    String noXmlFile();

    @Message(defaultMessage = "You haven't provided any schema file.")
    String noSchemaFile();

    @Message(defaultMessage = "Type")
    String type();

    @Message(defaultMessage = "Line")
    String line();

    @Message(defaultMessage = "Column")
    String column();

    @Message(defaultMessage = "Message")
    String message();

    @Message(defaultMessage = "Information")
    String information();

    @Message(defaultMessage = "Validation was successful")
    String validationSuccessful();

    @Message(defaultMessage = "Internal error")
    String internalError();
}
