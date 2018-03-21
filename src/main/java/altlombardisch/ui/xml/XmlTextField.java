package altlombardisch.ui.xml;

import altlombardisch.xml.TagDataProvider;
import altlombardisch.xml.XmlHelper;
import altlombardisch.xml.document.XmlDocumentDefinition;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.TextField;

import javax.json.JsonObject;

/**
 * A text field able validate against XML document definitions.
 */
public class XmlTextField extends TextField<String> implements TagDataProvider {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Document definition of a XML document.
     */
    private XmlDocumentDefinition documentDefinition;

    /**
     * Creates a XML text field.
     * 
     * @param id
     *            ID of the XML text field
     * @param documentDefinition
     *            document definition of a XML document
     */
    public XmlTextField(String id, XmlDocumentDefinition documentDefinition) {
        super(id);

        this.documentDefinition = documentDefinition;
    }

    /**
     * Called when a XML text field is initialized.
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new XmlValidator(this, documentDefinition));

        // initialize data for clickable tags
        initializeTagData(this, documentDefinition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeTagData(Component component,
            XmlDocumentDefinition documentDefinition) {
        if (!(documentDefinition instanceof XmlDocumentDefinition)) {
            return;
        }

        JsonObject documentData = XmlHelper.getDocumentData(documentDefinition);

        component.add(AttributeModifier.append("data-document",
                documentData.toString()));
    }
}
