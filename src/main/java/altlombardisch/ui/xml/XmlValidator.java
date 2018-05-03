package altlombardisch.ui.xml;

import altlombardisch.xml.document.XmlDocumentDefinition;
import org.apache.wicket.Component;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.UrlRenderer;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

/**
 * Validates a form component’s value against a XML schema.
 */
public class XmlValidator implements IValidator<String> {
    /**
     * Types of XML validators.
     */
    public static abstract class ValidatorType {
        /**
         * Document validator type.
         */
        public static final String DOCUMENT = "DOCUMENT";

        /**
         * Schema validator type.
         */
        public static final String SCHEMA = "SCHEMA";

        /**
         * XSL validator type.
         */
        public static final String XSL = "XSL";

        /**
         * Allowed XML validator types.
         */
        public static enum Type {
            DOCUMENT, SCHEMA, XSL
        };
    };

    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The validated component.
     */
    private Component component;

    /**
     * Type of XML validator.
     */
    private ValidatorType.Type validatorType;

    /**
     * Document definition of a XML document.
     */
    private XmlDocumentDefinition documentDefinition;

    /**
     * Unused.
     */
    @SuppressWarnings("unused")
    private XmlValidator() {
    }

    /**
     * Creates a XML validator.
     * 
     * @param component
     *            component that is validated
     * @param validatorType
     *            validator type
     */
    public XmlValidator(Component component, ValidatorType.Type validatorType) {
        this.component = component;
        this.validatorType = validatorType;

        if (validatorType == null) {
            throw new IllegalArgumentException("Argument validatorType cannot be null.");
        } else if (validatorType.equals(ValidatorType.Type.DOCUMENT)) {
            throw new IllegalArgumentException("DOCUMENT is not allowed as validator type.");
        }
    }

    /**
     * Creates a XML validator.
     * 
     * @param component
     *            component that is validated
     * @param documentDefinition
     *            document definition of a XML document
     */
    public XmlValidator(Component component, XmlDocumentDefinition documentDefinition) {
        this.component = component;
        this.validatorType = ValidatorType.Type.DOCUMENT;
        this.documentDefinition = documentDefinition;

        if (documentDefinition == null) {
            throw new IllegalArgumentException("Argument documentDefinition cannot be null.");
        }
    }

    /**
     * Validates the value of a form component.
     * 
     * @param validatable
     *            IValidatable instance that is validated
     */
    @Override
    public void validate(IValidatable<String> validatable) {
        if (validatorType.equals(ValidatorType.Type.DOCUMENT) || validatorType.equals(ValidatorType.Type.SCHEMA)) {
            try {
                String xmlValue = validatable.getValue();
                Schema schema;
                Integer lineOffset = 0;

                if (validatorType.equals(ValidatorType.Type.DOCUMENT)) {
                    String schemaString = documentDefinition.getSchema();
                    Source schemaSource = new StreamSource(new StringReader(
                            schemaString));
                    schema = SchemaFactory.newInstance(
                            XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(
                            schemaSource);
                    xmlValue = "<" + documentDefinition.getRootElement() + ">\n"
                            + xmlValue + "\n</"
                            + documentDefinition.getRootElement() + ">";
                    lineOffset = -1;
                } else {
                    PackageResourceReference schemaReference = new PackageResourceReference(
                            XmlEditor.class, "schema/XMLSchema.xsd");
                    CharSequence schemaCharSequence = RequestCycle.get().urlFor(
                            schemaReference, new PageParameters());
                    UrlRenderer urlRenderer = RequestCycle.get().getUrlRenderer();
                    String urlString = urlRenderer.renderFullUrl(Url.parse(schemaCharSequence));
                    URL schemaUrl = new URL(urlString);
                    schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaUrl);
                }

                Validator validator = schema.newValidator();

                validator.setFeature("http://apache.org/xml/features/validation/schema", true);
                validator.setErrorHandler(new XmlErrorHandler(component, validatable, lineOffset));
                validator.validate(new StreamSource(new StringReader(xmlValue)));
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (validatorType.equals(ValidatorType.Type.XSL)) {
            String xmlValue = validatable.getValue();
            TransformerFactory factory = TransformerFactory.newInstance();
            Source xslSource = new StreamSource(new StringReader((xmlValue)));
            Source source = new StreamSource(new StringReader("<?xml version='1.0'?><root/>"));
            Result result = new StreamResult(new StringWriter());

            factory.setErrorListener(new XmlErrorListener(component, validatable));

            try {
                Transformer transformer = factory.newTransformer(xslSource);
                transformer.transform(source, result);
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }
    }
}
