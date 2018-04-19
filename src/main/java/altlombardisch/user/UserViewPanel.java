package altlombardisch.user;

import altlombardisch.auth.WebSession;
import altlombardisch.ui.AjaxView;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

public class UserViewPanel extends Panel {
    /**
     * A view displaying special users.
     */
    private final UserAjaxView userView;

    /**
     * Creates a user view panel.
     */
    public UserViewPanel() {
        super("userViewPanel");
        setOutputMarkupId(true);

        this.userView = new UserAjaxView();
        WebMarkupContainer dummyItem = new WebMarkupContainer("dummyItem");

        dummyItem.setOutputMarkupId(true);
        userView.setNoItemContainer(dummyItem);
        add(dummyItem);
        add(userView);
    }

    /**
     * Returns the user view.
     *
     * @return A user view.
     */
    public AjaxView<User> getUserView() {
        return userView;
    }

    /**
     * Called when a user view item is clicked.
     *
     * @param target target that produces an Ajax response
     * @param model  user model of the clicked user
     */
    @SuppressWarnings("unchecked")
    public void onItemClick(AjaxRequestTarget target, IModel<User> model) {
        Page userEditPage = getPage();
        Panel userEditPanel = (Panel) userEditPage.get("userEditPanel");
        Form<User> userEditForm = (Form<User>) userEditPanel.get("userEditForm");
        Form<User> newUserEditForm = new UserEditForm(
                new CompoundPropertyModel<>(model), getUserView());

        userEditForm.replaceWith(newUserEditForm);
        target.add(newUserEditForm);
        target.focusComponent(newUserEditForm.get("user"));

        // clear feedback panel
        WebSession.get().clearFeedbackMessages();
        target.add(userEditPage.get("feedbackPanel"));
    }
}
