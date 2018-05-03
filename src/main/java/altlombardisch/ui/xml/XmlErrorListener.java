package altlombardisch.ui.xml;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;

/**
 * An error listener for XML validation errors.
 */
public class XmlErrorListener implements ErrorListener {
    /**
     * The component that stores data entered by the XML editor.
     */
    private Component component;

    /**
     * The validatable variant of a component.
     */
    private IValidatable<String> validatable;

    /**
     * Disables the default constructor.
     */
    @SuppressWarnings("unused")
    private XmlErrorListener() {
    }

    /**
     * Creates a XML error listener.
     *
     * @param component
     *            component that stores entered data
     * @param validatable
     *            validatable variant of a component
     */
    public XmlErrorListener(Component component, IValidatable<String> validatable) {
        this.component = component;
        this.validatable = validatable;
    }

    /**
     * Reports a warning against a validatable’s value.
     *
     * @param exception
     *            the parse exception raised during validation
     */
    @Override
    public void warning(TransformerException exception) throws TransformerException {
        error(exception);
    }

    /**
     * Reports a fatal error against a validatable’s value.
     *
     * @param exception
     *            the parse exception raised during validation
     */
    @Override
    public void fatalError(TransformerException exception) throws TransformerException {
        error(exception);
    }

    /**
     * Reports an error against a validatable’s value.
     *
     * @param exception
     *            the parse exception raised during validation
     */
    @Override
    public void error(TransformerException exception) throws TransformerException {
        SourceLocator locator = exception.getLocator();
        StringResourceModel messageModel = null;

        if (locator != null) {
            if (component instanceof TextArea<?>) {
                messageModel = new StringResourceModel(
                        "XmlEditor.validatorMessage", component).setParameters(
                        String.format("<b>%s</b>", component.findParent(XmlEditor.class).getId()),
                        String.format("<b>%d</b>", locator.getLineNumber()),
                        String.format("<b>%d</b>", locator.getColumnNumber()),
                        exception.getMessage());
            } else if (component instanceof TextField<?>) {
                messageModel = new StringResourceModel(
                        "XmlTextField.validatorMessage", component).setParameters(
                        String.format("<b>%s</b>", component.getId()),
                        String.format("<b>%d</b>", locator.getColumnNumber()),
                        exception.getMessage());
            }
        } else {
            if (component instanceof TextArea<?>) {
                messageModel = new StringResourceModel(
                        "XmlEditor.validatorMessage2", component).setParameters(
                        String.format("<b>%s</b>", component.findParent(XmlEditor.class).getId()),
                        exception.getMessage());
            } else if (component instanceof TextField<?>) {
                messageModel = new StringResourceModel(
                        "XmlTextField.validatorMessage2", component).setParameters(
                        String.format("<b>%s</b>", component.getId()),
                        exception.getMessage());
            }
        }

        if (messageModel instanceof StringResourceModel) {
            IValidationError error = new ValidationError(messageModel.getString());
            validatable.error(error);
        }
    }
}
