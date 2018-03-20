package altlombardisch.xml.document;

import altlombardisch.ui.AjaxView;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * A panel with a view which displays XML document definitions.
 */
public class XmlDocumentDefinitionViewPanel extends Panel {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * A view displaying XML document definitions.
     */
    private XmlDocumentDefinitionAjaxView definitionView;

    /**
     * Creates a panel for a document definition view.
     * 
     * @param id
     *            ID of the panel
     * @param model
     *            model of the edited document definition
     */
    public XmlDocumentDefinitionViewPanel(String id,
            IModel<XmlDocumentDefinition> model) {
        super(id);
        setOutputMarkupId(true);

        this.definitionView = new XmlDocumentDefinitionAjaxView(
                "definitionView", model);
        WebMarkupContainer dummyItem = new WebMarkupContainer("dummyItem");

        dummyItem.setOutputMarkupId(true);
        definitionView.setNoItemContainer(dummyItem);
        add(dummyItem);
        add(definitionView);
    }

    /**
     * Returns a view displaying XML document definitions.
     * 
     * @return A definition view.
     */
    public AjaxView<XmlDocumentDefinition> getDefinitionView() {
        return definitionView;
    }

    /**
     * Called when a document definition view item is clicked.
     * 
     * @param target
     *            target that produces an Ajax response
     * @param model
     *            model of the clicked document definition
     */
    @SuppressWarnings("unchecked")
    public void onItemClick(AjaxRequestTarget target,
            IModel<XmlDocumentDefinition> model) {
        Page definitionEditPage = getPage();
        Form<XmlDocumentDefinition> definitionEditForm = (Form<XmlDocumentDefinition>) definitionEditPage
                .get("definitionEditForm");
        Form<XmlDocumentDefinition> newDefinitionEditForm = new XmlDocumentDefinitionEditForm(
                "definitionEditForm", model, getDefinitionView());

        definitionEditForm.replaceWith(newDefinitionEditForm);
        target.add(newDefinitionEditForm);
        target.focusComponent(newDefinitionEditForm.get("rootElement"));
    }
}
