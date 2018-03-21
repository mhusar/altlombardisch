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
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

/**
 * Validates a from component’s value against a XML schema.
 */
public class XmlValidator implements IValidator<String> {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The validated component.
     */
    private Component component;

    /**
     * Document definition of a XML document.
     */
    private XmlDocumentDefinition documentDefinition;

    /**
         * 
         */
    @SuppressWarnings("unused")
    private XmlValidator() {
    }

    /**
     * Creates a XML validator.
     * 
     * @param component
     *            component that is validated
     */
    public XmlValidator(Component component) {
        this.component = component;
    }

    /**
     * Creates a XML validator.
     * 
     * @param component
     *            component that is validated
     * @param documentDefinition
     *            document definition of a XML document
     */
    public XmlValidator(Component component,
            XmlDocumentDefinition documentDefinition) {
        this.component = component;
        this.documentDefinition = documentDefinition;
    }

    /**
     * Validates the value of a form component.
     * 
     * @param validatable
     *            IValidatable instance that is validated
     */
    @Override
    public void validate(IValidatable<String> validatable) {
        try {
            String xmlValue = validatable.getValue();
            Schema schema;
            Integer lineOffset = 0;

            if (documentDefinition instanceof XmlDocumentDefinition) {
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
                String urlString = urlRenderer.renderFullUrl(Url
                        .parse(schemaCharSequence));
                URL schemaUrl = new URL(urlString);
                schema = SchemaFactory.newInstance(
                        XMLConstants.W3C_XML_SCHEMA_NS_URI)
                        .newSchema(schemaUrl);
            }

            Validator validator = schema.newValidator();

            validator.setFeature(
                    "http://apache.org/xml/features/validation/schema", true);
            validator.setErrorHandler(new XmlErrorHandler(component,
                    validatable, lineOffset));
            validator.validate(new StreamSource(new StringReader(xmlValue)));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
