package altlombardisch.xml.document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import altlombardisch.ui.AjaxView;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * An asynchronously refreshing repeating view for document definitions.
 */
public class XmlDocumentDefinitionAjaxView extends
        AjaxView<XmlDocumentDefinition> {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates an Ajax view for document definitions.
     * 
     * @param id
     *            ID of the view
     * @param model
     *            model of the edited document definition
     */
    public XmlDocumentDefinitionAjaxView(String id,
            IModel<XmlDocumentDefinition> model) {
        super(id);
        setSelectedModel(model);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Iterator<IModel<XmlDocumentDefinition>> getItemModels() {
        Iterator<XmlDocumentDefinition> definitionIterator = new XmlDocumentDefinitionDao()
                .findAll().iterator();
        List<IModel<XmlDocumentDefinition>> definitionModels = new ArrayList<IModel<XmlDocumentDefinition>>();

        while (definitionIterator.hasNext()) {
            definitionModels.add(new Model<XmlDocumentDefinition>(
                    definitionIterator.next()));
        }

        return definitionModels.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Item<XmlDocumentDefinition> getNewItem(String id, int index,
            IModel<XmlDocumentDefinition> model, Boolean isSelected) {
        Item<XmlDocumentDefinition> item = new Item<XmlDocumentDefinition>(id,
                index, model);
        Label label = new Label("label",
                getString("XmlDocumentDefinitionAjaxView."
                        + model.getObject().getIdentifier()));

        if (isSelected) {
            item.add(AttributeModifier
                    .append("class", "list-group-item active"));
        } else {
            item.add(AttributeModifier.append("class", "list-group-item"));
        }

        item.add(label);
        return item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getRefreshNoItemContainerJavaScript(String id,
            String parentId, Boolean isVisible) {
        if (isVisible()) {
            String cssClass = "list-group-item";
            String javaScript = String.format("jQuery(\"#%s\").remove(); "
                    + "var item = jQuery(\"<div></div>\"); "
                    + "item.attr(\"id\", \"%s\").attr(\"class\", \"%s\"); "
                    + "jQuery(\"#%s .list-group\").prepend(item);", id, id,
                    cssClass, parentId);

            return javaScript;
        } else {
            String javaScript = String.format("jQuery(\"#%s\").remove();", id);

            return javaScript;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getRefreshItemJavaScript(String id, int index,
            IModel<XmlDocumentDefinition> model, Boolean isSelected) {
        String javaScript = String.format("var item = jQuery(\"#%s\"); "
                + "jQuery(\"%s\", item).text(\"%s\"); ", id, "span",
                getString("XmlDocumentDefinitionAjaxView."
                        + model.getObject().getIdentifier()));

        if (isSelected) {
            javaScript += String.format("item.addClass(\"%s\");", "active");
        } else {
            javaScript += String.format("item.removeClass(\"%s\");", "active");
        }

        return javaScript;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getAppendItemJavaScript(String id, String parentId,
            int index, IModel<XmlDocumentDefinition> model, Boolean isSelected) {
        String cssClass = (isSelected) ? "list-group-item active"
                : "list-group-item";
        String javaScript = String.format(
                "var item = jQuery(\"<div></div>\"); "
                        + "item.attr(\"id\", \"%s\").addClass(\"%s\"); "
                        + "item.append("
                        + "jQuery(\"<span></span>\").text(\"%s\")); "
                        + "jQuery(\"#%s .list-group\").append(item);", id,
                cssClass, getString("XmlDocumentDefinitionAjaxView."
                        + model.getObject().getIdentifier()), parentId);

        return javaScript;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getRemoveItemJavaScript(String id) {
        String javaScript = String.format(
                "var item = jQuery(\"#%s\"); item.remove();", id);

        return javaScript;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemClick(AjaxRequestTarget target,
            IModel<XmlDocumentDefinition> model) {
        XmlDocumentDefinitionViewPanel panel = findParent(XmlDocumentDefinitionViewPanel.class);

        panel.onItemClick(target, model);
    }
}
