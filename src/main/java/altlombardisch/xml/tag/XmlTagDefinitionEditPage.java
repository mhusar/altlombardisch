package altlombardisch.xml.tag;

import altlombardisch.auth.WebSession;
import altlombardisch.ui.AjaxView;
import altlombardisch.ui.TitleLabel;
import altlombardisch.ui.page.BasePage;
import altlombardisch.xml.document.XmlDocumentDefinition;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * A page containing an edit form for XML tag definitions.
 */
@AuthorizeInstantiation({ "USER", "ADMIN" })
public class XmlTagDefinitionEditPage extends BasePage {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The parent document definition model.
     */
    private IModel<XmlDocumentDefinition> parentModel;

    /**
     * Model of the edited XML tag definition object.
     */
    private CompoundPropertyModel<XmlTagDefinition> definitionModel;

    /**
     * Creates a XML tag definition edit page.
     * 
     * @param parentModel
     *            the parent document definition model
     */
    public XmlTagDefinitionEditPage(IModel<XmlDocumentDefinition> parentModel) {
        this.parentModel = parentModel;

        // check if the session is expired
        WebSession.get().checkSessionExpired();

        if (parentModel instanceof IModel) {
            XmlTagDefinition definition = new XmlTagDefinitionDao()
                    .findFirst(parentModel.getObject());

            if (definition instanceof XmlTagDefinition) {
                definitionModel = new CompoundPropertyModel<XmlTagDefinition>(
                        definition);
            } else {
                definitionModel = new CompoundPropertyModel<XmlTagDefinition>(
                        new XmlTagDefinition());
            }
        }
    }

    /**
     * Called when a XML tag definition edit page is initialized.
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();

        XmlTagDefinitionViewPanel definitionViewPanel = new XmlTagDefinitionViewPanel(
                "definitionViewPanel", parentModel, definitionModel);

        add(new TitleLabel(getString("XmlTagDefinitionEditPage.header")));
        add(definitionViewPanel);
        add(new AddXmlTagDefinitionButton("addDefinitionButton", parentModel));
        add(new XmlTagDefinitionEditForm("definitionEditForm", parentModel,
                definitionModel, definitionViewPanel.getDefinitionView()));
    }

    /**
     * A button which starts the creation of a new tag definition.
     */
    private final class AddXmlTagDefinitionButton extends
            AjaxLink<XmlDocumentDefinition> {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Creates a button.
         * 
         * @param id
         *            ID of a button
         * @param parentModel
         *            model of the parent document definition
         */
        private AddXmlTagDefinitionButton(String id,
                IModel<XmlDocumentDefinition> parentModel) {
            super(id, parentModel);
        }

        /**
         * Called on button click.
         * 
         * @param target
         *            target that produces an Ajax response
         */
        @Override
        @SuppressWarnings("unchecked")
        public void onClick(AjaxRequestTarget target) {
            Page definitionEditPage = (XmlTagDefinitionEditPage) findParent(XmlTagDefinitionEditPage.class);
            Panel definitionViewPanel = (XmlTagDefinitionViewPanel) definitionEditPage
                    .get("definitionViewPanel");
            AjaxView<XmlTagDefinition> definitionView = (AjaxView<XmlTagDefinition>) definitionViewPanel
                    .get("definitionView");
            Component definitionEditForm = definitionEditPage
                    .get("definitionEditForm");
            Component newDefinitionEditForm = new XmlTagDefinitionEditForm(
                    "definitionEditForm", getModel(),
                    new CompoundPropertyModel<XmlTagDefinition>(
                            new XmlTagDefinition()), definitionView);

            definitionView.setSelectedModel(new Model<XmlTagDefinition>(
                    new XmlTagDefinition()));
            definitionView.refresh(target);
            definitionEditForm.replaceWith(newDefinitionEditForm);
            target.add(newDefinitionEditForm);
            target.focusComponent(newDefinitionEditForm.get("name"));
        }
    }
}
