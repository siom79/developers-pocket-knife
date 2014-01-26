package developers.pocket.knife.ui.tools.validatexml;

import com.google.common.base.Optional;
import developers.pocket.knife.exceptions.TechnicalException;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ValidateXmlUIModel {
    private Optional<File> xmlFile = Optional.absent();
    private Optional<File> schemaFile = Optional.absent();
    private List<ParserError> parserErrors = new LinkedList<>();

    public static class ParserError {
        public enum Type {
            Warning, Error, FatalError
        }
        private final Type type;
        private final int line;
        private final int column;
        private final String message;

        public ParserError(Type type, int line, int column, String message) {
            this.type = type;
            this.line = line;
            this.column = column;
            this.message = message;
        }

        public Type getType() {
            return type;
        }

        public int getLine() {
            return line;
        }

        public int getColumn() {
            return column;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "ParserError{" +
                    "type=" + type +
                    ", line=" + line +
                    ", column=" + column +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

    public void validate() {
        parserErrors.clear();
        if (schemaFile.isPresent() && xmlFile.isPresent()) {
            try {
                ErrorHandler errorHandler = new ErrorHandler() {
                    @Override
                    public void warning(SAXParseException exception) throws SAXException {
                        ParserError parserError = exceptionToParserError(exception, ParserError.Type.Warning);
                        parserErrors.add(parserError);
                    }

                    private ParserError exceptionToParserError(SAXParseException exception, ParserError.Type type) {
                        return new ParserError(type, exception.getLineNumber(), exception.getColumnNumber(), exception.getLocalizedMessage());
                    }

                    @Override
                    public void error(SAXParseException exception) throws SAXException {
                        ParserError parserError = exceptionToParserError(exception, ParserError.Type.Error);
                        parserErrors.add(parserError);
                    }

                    @Override
                    public void fatalError(SAXParseException exception) throws SAXException {
                        ParserError parserError = exceptionToParserError(exception, ParserError.Type.FatalError);
                        parserErrors.add(parserError);
                    }
                };
                Source sourceFile = new StreamSource(xmlFile.get());
                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema = schemaFactory.newSchema(schemaFile.get());
                Validator validator = schema.newValidator();
                validator.setErrorHandler(errorHandler);
                validator.validate(sourceFile);
            } catch(SAXException se) {
                if(parserErrors.size() == 0) {
                    parserErrors.add(new ParserError(ParserError.Type.FatalError, -1, -1, se.getLocalizedMessage()));
                }
            } catch (Exception e) {
               throw new TechnicalException(TechnicalException.Reason.InternalError);
            }
        } else {
            throw new TechnicalException(TechnicalException.Reason.IllegalArgument);
        }
    }

    public void setXmlFile(File xmlFile) {
        this.xmlFile = Optional.of(xmlFile);
    }

    public void setSchemaFile(File selectedFile) {
        this.schemaFile = Optional.of(selectedFile);
    }

    public List<ParserError> getParserErrors() {
        return parserErrors;
    }
}
