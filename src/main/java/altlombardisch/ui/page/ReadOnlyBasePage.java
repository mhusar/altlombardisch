package altlombardisch.ui.page;

import altlombardisch.character.CharacterHelper;
import altlombardisch.ui.panel.HeaderPanel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import javax.json.JsonArray;

/**
 * A base page with a header panel.
 */
public class ReadOnlyBasePage extends EmptyBasePage {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a base page.
     */
    public ReadOnlyBasePage() {
        super();
    }

    /**
     * Creates a new base page.
     * 
     * @param model
     *            the web page’s model
     */
    public ReadOnlyBasePage(IModel<?> model) {
        super(model);
    }

    /**
     * Called when a base page is initialized.
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();

        JsonArray characterData = CharacterHelper.getCharacterData();
        String characterDataString = characterData.toString();
        WebMarkupContainer bodyContainer = new TransparentWebMarkupContainer(
                "body");

        bodyContainer.add(AttributeModifier.append("data-characters",
                characterDataString));
        add(bodyContainer);
        add(new HeaderPanel(getPage().getClass()));
        super.onBeforeRender();
    }
}
