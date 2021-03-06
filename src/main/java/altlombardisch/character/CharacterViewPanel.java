package altlombardisch.character;

import altlombardisch.auth.WebSession;
import altlombardisch.ui.AjaxView;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * A panel with a view which displays special characters.
 */
class CharacterViewPanel extends Panel {
    /**
     * A view displaying special characters.
     */
    private final CharacterAjaxView characterView;

    /**
     * Creates a character view panel.
     */
    public CharacterViewPanel() {
        super("characterViewPanel");
        setOutputMarkupId(true);

        this.characterView = new CharacterAjaxView();
        WebMarkupContainer dummyItem = new WebMarkupContainer("dummyItem");

        dummyItem.setOutputMarkupId(true);
        characterView.setNoItemContainer(dummyItem);
        add(dummyItem);
        add(characterView);
    }

    /**
     * Returns the character view.
     *
     * @return A character view.
     */
    public AjaxView<Character> getCharacterView() {
        return characterView;
    }

    /**
     * Called when a character view item is clicked.
     *
     * @param target target that produces an Ajax response
     * @param model  character model of the clicked character
     */
    @SuppressWarnings("unchecked")
    public void onItemClick(AjaxRequestTarget target, IModel<Character> model) {
        Page characterEditPage = getPage();
        Form<Character> characterEditForm = (Form<Character>) characterEditPage.get("characterEditForm");
        Form<Character> newCharacterEditForm = new CharacterEditForm(
                new CompoundPropertyModel<>(model), getCharacterView());

        characterEditForm.replaceWith(newCharacterEditForm);
        target.add(newCharacterEditForm);
        target.focusComponent(newCharacterEditForm.get("character"));

        // clear feedback panel
        WebSession.get().clearFeedbackMessages();
        target.add(characterEditPage.get("feedbackPanel"));
    }
}
