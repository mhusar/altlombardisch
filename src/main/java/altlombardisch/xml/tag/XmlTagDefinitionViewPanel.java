package altlombardisch.xml.tag;

import altlombardisch.ui.AjaxView;
import altlombardisch.xml.document.XmlDocumentDefinition;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * A panel with a view which displays XML tag definitions.
 */
public class XmlTagDefinitionViewPanel extends Panel {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The parent document definition model.
     */
    private IModel<XmlDocumentDefinition> parentModel;

    /**
     * A view displaying XML tag definitions.
     */
    private XmlTagDefinitionAjaxView definitionView;

    /**
     * Creates a panel for a tag definition view.
     * 
     * @param id
     *            ID of the panel
     * @param parentModel
     *            parent document definition model
     * @param model
     *            model of the edited tag definition
     */
    public XmlTagDefinitionViewPanel(String id,
            IModel<XmlDocumentDefinition> parentModel,
            IModel<XmlTagDefinition> model) {
        super(id);
        setOutputMarkupId(true);

        this.parentModel = parentModel;
        this.definitionView = new XmlTagDefinitionAjaxView("definitionView",
                parentModel, model);
        WebMarkupContainer dummyItem = new WebMarkupContainer("dummyItem");

        dummyItem.setOutputMarkupId(true);
        definitionView.setNoItemContainer(dummyItem);
        add(dummyItem);
        add(definitionView);
    }

    /**
     * Returns a view displaying XML tag definitions.
     * 
     * @return A definition view.
     */
    public AjaxView<XmlTagDefinition> getDefinitionView() {
        return definitionView;
    }

    /**
     * Called when a tag definition view item is clicked.
     * 
     * @param target
     *            target that produces an Ajax response
     * @param model
     *            model of the clicked tag definition
     */
    @SuppressWarnings("unchecked")
    public void onItemClick(AjaxRequestTarget target,
            IModel<XmlTagDefinition> model) {
        Page definitionEditPage = getPage();
        Form<XmlTagDefinition> definitionEditForm = (Form<XmlTagDefinition>) definitionEditPage
                .get("definitionEditForm");
        Form<XmlTagDefinition> newDefinitionEditForm = new XmlTagDefinitionEditForm(
                "definitionEditForm", parentModel, model, getDefinitionView());

        definitionEditForm.replaceWith(newDefinitionEditForm);
        target.add(newDefinitionEditForm);
        target.focusComponent(newDefinitionEditForm.get("name"));
    }
}
