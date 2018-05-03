package altlombardisch.ui.xml;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * An error handler for XML validation errors.
 */
public class XmlErrorHandler implements ErrorHandler {
    /**
     * The component that stores data entered by the XML editor.
     */
    private Component component;

    /**
     * The validatable variant of a component.
     */
    private IValidatable<String> validatable;

    /**
     * The line offset for error messages.
     */
    private Integer lineOffset;

    /**
     * Disables the default constructor.
     */
    @SuppressWarnings("unused")
    private XmlErrorHandler() {
    }

    /**
     * Creates a XML error handler.
     * 
     * @param component
     *            component that stores entered data
     * @param validatable
     *            validatable variant of a component
     * @param lineOffset
     *            line offset for error messages
     */
    public XmlErrorHandler(Component component, IValidatable<String> validatable, Integer lineOffset) {
        this.component = component;
        this.validatable = validatable;
        this.lineOffset = lineOffset;
    }

    /**
     * Reports a warning against a validatable’s value.
     * 
     * @param exception
     *            the parse exception raised during validation
     */
    @Override
    public void warning(SAXParseException exception) {
        error(exception);
    }

    /**
     * Reports a fatal error against a validatable’s value.
     * 
     * @param exception
     *            the parse exception raised during validation
     */
    @Override
    public void fatalError(SAXParseException exception) {
        error(exception);
    }

    /**
     * Reports an error against a validatable’s value.
     * 
     * @param exception
     *            the parse exception raised during validation
     */
    @Override
    public void error(SAXParseException exception) {
        StringResourceModel messageModel = null;

        if (component instanceof TextArea<?>) {
            messageModel = new StringResourceModel(
                    "XmlEditor.validatorMessage", component).setParameters(
                    String.format("<b>%s</b>", component.findParent(XmlEditor.class).getId()),
                    String.format("<b>%d</b>", exception.getLineNumber() + lineOffset),
                    String.format("<b>%d</b>", exception.getColumnNumber()),
                    exception.getMessage());
        } else if (component instanceof TextField<?>) {
            messageModel = new StringResourceModel(
                    "XmlTextField.validatorMessage", component).setParameters(
                    String.format("<b>%s</b>", component.getId()),
                    String.format("<b>%d</b>", exception.getColumnNumber()),
                    exception.getMessage());
        }

        if (messageModel instanceof StringResourceModel) {
            IValidationError error = new ValidationError(messageModel.getString());
            validatable.error(error);
        }
    }
}
