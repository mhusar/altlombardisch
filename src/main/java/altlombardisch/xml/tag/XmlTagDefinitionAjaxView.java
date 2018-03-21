package altlombardisch.xml.tag;

import altlombardisch.ui.AjaxView;
import altlombardisch.xml.document.XmlDocumentDefinition;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An asynchronously refreshing repeating view for tag definitions.
 */
public class XmlTagDefinitionAjaxView extends AjaxView<XmlTagDefinition> {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The parent document definition model.
     */
    private IModel<XmlDocumentDefinition> parentModel;

    /**
     * Creates an Ajax view for tag definitions.
     * 
     * @param id
     *            ID of the view
     * @param parentModel
     *            parent document definition model
     * @param model
     *            model of the edited tag definition
     */
    public XmlTagDefinitionAjaxView(String id,
            IModel<XmlDocumentDefinition> parentModel,
            IModel<XmlTagDefinition> model) {
        super(id);
        setSelectedModel(model);

        this.parentModel = parentModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Iterator<IModel<XmlTagDefinition>> getItemModels() {
        Iterator<XmlTagDefinition> definitionIterator = new XmlTagDefinitionDao()
                .findAll(parentModel.getObject()).iterator();
        List<IModel<XmlTagDefinition>> definitionModels = new ArrayList<IModel<XmlTagDefinition>>();

        while (definitionIterator.hasNext()) {
            definitionModels.add(new Model<XmlTagDefinition>(definitionIterator
                    .next()));
        }

        return definitionModels.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Item<XmlTagDefinition> getNewItem(String id, int index,
            IModel<XmlTagDefinition> model, Boolean isSelected) {
        Item<XmlTagDefinition> item = new Item<XmlTagDefinition>(id, index,
                model);
        Label label = new Label("label", model.getObject().getName());

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
            IModel<XmlTagDefinition> model, Boolean isSelected) {
        String javaScript = String.format("var item = jQuery(\"#%s\"); "
                + "jQuery(\"%s\", item).text(\"%s\"); ", id, "span", model
                .getObject().getName());

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
            int index, IModel<XmlTagDefinition> model, Boolean isSelected) {
        String cssClass = (isSelected) ? "list-group-item active"
                : "list-group-item";
        String javaScript = String.format(
                "var item = jQuery(\"<div></div>\"); "
                        + "item.attr(\"id\", \"%s\").addClass(\"%s\"); "
                        + "item.append("
                        + "jQuery(\"<span></span>\").text(\"%s\")); "
                        + "jQuery(\"#%s .list-group\").append(item);", id,
                cssClass, model.getObject().getName(), parentId);

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
            IModel<XmlTagDefinition> model) {
        XmlTagDefinitionViewPanel panel = findParent(XmlTagDefinitionViewPanel.class);

        panel.onItemClick(target, model);
    }
}
