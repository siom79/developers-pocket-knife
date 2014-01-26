package developers.pocket.knife.ui.tools.validatexml;

import com.google.common.base.Optional;

import java.io.File;

public class ValidateXmlUIModel {
    private Optional<File> xmlFile = Optional.absent();
    private Optional<File> schemaFile = Optional.absent();

    public void validate() {

    }

    public void setXmlFile(File xmlFile) {
        this.xmlFile = Optional.of(xmlFile);
    }

    public void setSchemaFile(File selectedFile) {
        this.schemaFile = Optional.of(selectedFile);
    }
}
