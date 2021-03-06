package altlombardisch.character;

import altlombardisch.ui.AjaxView;
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
 * An asynchronously refreshing repeating view for special characters.
 */
public class CharacterAjaxView extends AjaxView<Character> {
    /**
     * Creates an Ajax view for special characters.
     */
    public CharacterAjaxView() {
        super("characterView");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Iterator<IModel<Character>> getItemModels() {
        List<IModel<Character>> characterModels = new ArrayList<>();

        for (Character character : new CharacterDao().getAll()) {
            characterModels.add(new Model<>(character));
        }

        return characterModels.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Item<Character> getNewItem(String id, int index, IModel<Character> model, Boolean isSelected) {
        Item<Character> item = new Item<>(id, index, model);
        Label label = new Label("label", model.getObject().getCharacter());

        if (isSelected) {
            item.add(AttributeModifier.append("class", "list-group-item active"));
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
    protected String getRefreshNoItemContainerJavaScript(String id, String parentId, Boolean isVisible) {
        if (isVisible()) {
            String cssClass = "list-group-item";
            return String.format("jQuery(\"#%s\").remove(); "
                            + "var item = jQuery(\"<div></div>\"); "
                            + "item.attr(\"id\", \"%s\").attr(\"class\", \"%s\"); "
                            + "jQuery(\"#%s .list-group\").prepend(item);", id, id,
                    cssClass, parentId);
        } else {
            return String.format("jQuery(\"#%s\").remove();", id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getRefreshItemJavaScript(String id, int index, IModel<Character> model, Boolean isSelected) {
        String javaScript = String.format("var item = jQuery(\"#%s\"); " + "jQuery(\"%s\", item).text(\"%s\"); ",
                id, "span", model.getObject().getCharacter());

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
    protected String getAppendItemJavaScript(String id, String parentId, int index, IModel<Character> model,
                                             Boolean isSelected) {
        String cssClass = (isSelected) ? "list-group-item active" : "list-group-item";

        return String.format(
                "var item = jQuery(\"<div></div>\"); "
                        + "item.attr(\"id\", \"%s\").addClass(\"%s\"); "
                        + "item.append("
                        + "jQuery(\"<span></span>\").text(\"%s\")); "
                        + "jQuery(\"#%s .list-group\").append(item);", id,
                cssClass, model.getObject().getCharacter(), parentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getRemoveItemJavaScript(String id) {
        return String.format("var item = jQuery(\"#%s\"); item.remove();", id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemClick(AjaxRequestTarget target, IModel<Character> model) {
        CharacterViewPanel panel = findParent(CharacterViewPanel.class);
        panel.onItemClick(target, model);
    }
}
