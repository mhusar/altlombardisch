package altlombardisch.xml.attribute;

import altlombardisch.auth.WebSession;
import altlombardisch.ui.AjaxView;
import altlombardisch.ui.TitleLabel;
import altlombardisch.ui.page.BasePage;
import altlombardisch.xml.tag.XmlTagDefinition;
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
 * A page containing an edit form for XML attribute definitions.
 */
@AuthorizeInstantiation({ "USER", "ADMIN" })
public class XmlAttributeDefinitionEditPage extends BasePage {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The parent tag definition model.
     */
    private IModel<XmlTagDefinition> parentModel;

    /**
     * Model of the edited XML attribute definition object.
     */
    private CompoundPropertyModel<XmlAttributeDefinition> definitionModel;

    /**
     * Creates a XML attribute definition edit page.
     * 
     * @param parentModel
     *            the parent tag definition model
     */
    public XmlAttributeDefinitionEditPage(IModel<XmlTagDefinition> parentModel) {
        this.parentModel = parentModel;

        // check if the session is expired
        WebSession.get().checkSessionExpired();

        if (parentModel instanceof IModel) {
            XmlAttributeDefinition definition = new XmlAttributeDefinitionDao()
                    .findFirst(parentModel.getObject());

            if (definition instanceof XmlAttributeDefinition) {
                definitionModel = new CompoundPropertyModel<XmlAttributeDefinition>(
                        definition);
            } else {
                definitionModel = new CompoundPropertyModel<XmlAttributeDefinition>(
                        new XmlAttributeDefinition());
            }
        }
    }

    /**
     * Called when a XML attribute definition edit page is initialized.
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();

        XmlAttributeDefinitionViewPanel definitionViewPanel = new XmlAttributeDefinitionViewPanel(
                "definitionViewPanel", parentModel, definitionModel);

        add(new TitleLabel(getString("XmlAttributeDefinitionEditPage.header")));
        add(definitionViewPanel);
        add(new AddXmlAttributeDefinitionButton("addDefinitionButton",
                parentModel));
        add(new XmlAttributeDefinitionEditForm("definitionEditForm",
                parentModel, definitionModel,
                definitionViewPanel.getDefinitionView()));
    }

    /**
     * A button which starts the creation of a new attribute definition.
     */
    private final class AddXmlAttributeDefinitionButton extends
            AjaxLink<XmlTagDefinition> {
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
         *            model of the parent tag definition
         */
        private AddXmlAttributeDefinitionButton(String id,
                IModel<XmlTagDefinition> parentModel) {
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
            Page definitionEditPage = (XmlAttributeDefinitionEditPage) findParent(XmlAttributeDefinitionEditPage.class);
            Panel definitionViewPanel = (XmlAttributeDefinitionViewPanel) definitionEditPage
                    .get("definitionViewPanel");
            AjaxView<XmlAttributeDefinition> definitionView = (AjaxView<XmlAttributeDefinition>) definitionViewPanel
                    .get("definitionView");
            Component definitionEditForm = definitionEditPage
                    .get("definitionEditForm");
            Component newDefinitionEditForm = new XmlAttributeDefinitionEditForm(
                    "definitionEditForm", getModel(),
                    new CompoundPropertyModel<XmlAttributeDefinition>(
                            new XmlAttributeDefinition()), definitionView);

            definitionView.setSelectedModel(new Model<XmlAttributeDefinition>(
                    new XmlAttributeDefinition()));
            definitionView.refresh(target);
            definitionEditForm.replaceWith(newDefinitionEditForm);
            target.add(newDefinitionEditForm);
            target.focusComponent(newDefinitionEditForm.get("name"));
        }
    }
}
