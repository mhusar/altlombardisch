package altlombardisch.xml.attribute;

import altlombardisch.ui.AjaxView;
import altlombardisch.xml.tag.XmlTagDefinition;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * A panel with a view which displays XML attribute definitions.
 */
public class XmlAttributeDefinitionViewPanel extends Panel {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The parent tag definition model.
     */
    private IModel<XmlTagDefinition> parentModel;

    /**
     * A view displaying XML attribute definitions.
     */
    private XmlAttributeDefinitionAjaxView definitionView;

    /**
     * Creates a panel for an attribute definition view.
     * 
     * @param id
     *            ID of the panel
     * @param parentModel
     *            parent tag definition model
     * @param model
     *            model of the edited attribute definition
     */
    public XmlAttributeDefinitionViewPanel(String id,
            IModel<XmlTagDefinition> parentModel,
            IModel<XmlAttributeDefinition> model) {
        super(id);
        setOutputMarkupId(true);

        this.parentModel = parentModel;
        this.definitionView = new XmlAttributeDefinitionAjaxView(
                "definitionView", parentModel, model);
        WebMarkupContainer dummyItem = new WebMarkupContainer("dummyItem");

        dummyItem.setOutputMarkupId(true);
        definitionView.setNoItemContainer(dummyItem);
        add(dummyItem);
        add(definitionView);
    }

    /**
     * Returns a view displaying XML attribute definitions.
     * 
     * @return A definition view.
     */
    public AjaxView<XmlAttributeDefinition> getDefinitionView() {
        return definitionView;
    }

    /**
     * Called when an attribute definition view item is clicked.
     * 
     * @param target
     *            target that produces an Ajax response
     * @param model
     *            model of the clicked attribute definition
     */
    @SuppressWarnings("unchecked")
    public void onItemClick(AjaxRequestTarget target,
            IModel<XmlAttributeDefinition> model) {
        Page definitionEditPage = getPage();
        Form<XmlAttributeDefinition> definitionEditForm = (Form<XmlAttributeDefinition>) definitionEditPage
                .get("definitionEditForm");
        Form<XmlAttributeDefinition> newDefinitionEditForm = new XmlAttributeDefinitionEditForm(
                "definitionEditForm", parentModel, model, getDefinitionView());

        definitionEditForm.replaceWith(newDefinitionEditForm);
        target.add(newDefinitionEditForm);
        target.focusComponent(newDefinitionEditForm.get("name"));
    }
}
