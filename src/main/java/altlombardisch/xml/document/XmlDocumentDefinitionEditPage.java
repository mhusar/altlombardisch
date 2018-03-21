package altlombardisch.xml.document;

import altlombardisch.auth.WebSession;
import altlombardisch.ui.TitleLabel;
import altlombardisch.ui.page.BasePage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * A page containing an edit form for XML document definitions.
 */
@AuthorizeInstantiation({ "USER", "ADMIN" })
public class XmlDocumentDefinitionEditPage extends BasePage {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Model of the edited XML document definition object.
     */
    private CompoundPropertyModel<XmlDocumentDefinition> definitionModel;

    /**
     * Creates a XML document definition edit page.
     */
    public XmlDocumentDefinitionEditPage() {
        XmlDocumentDefinition definition = new XmlDocumentDefinitionDao()
                .findFirst();

        if (definition instanceof XmlDocumentDefinition) {
            definitionModel = new CompoundPropertyModel<XmlDocumentDefinition>(
                    definition);
        } else {
            definitionModel = new CompoundPropertyModel<XmlDocumentDefinition>(
                    new XmlDocumentDefinition());
        }
    }

    /**
     * Creates a XML document definition edit page.
     * 
     * @param model
     *            model of the edited document definition
     */
    public XmlDocumentDefinitionEditPage(IModel<XmlDocumentDefinition> model) {
        if (model instanceof IModel) {
            definitionModel = new CompoundPropertyModel<XmlDocumentDefinition>(
                    model);
        }
    }

    /**
     * Called when a XML document definition edit page is initialized.
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();
        // check if the session is expired
        WebSession.get().checkSessionExpired();

        XmlDocumentDefinitionViewPanel definitionViewPanel = new XmlDocumentDefinitionViewPanel(
                "definitionViewPanel", definitionModel);

        add(new TitleLabel(getString("XmlDocumentDefinitionEditPage.header")));
        add(definitionViewPanel);
        add(new XmlDocumentDefinitionEditForm("definitionEditForm",
                definitionModel, definitionViewPanel.getDefinitionView()));
    }
}
