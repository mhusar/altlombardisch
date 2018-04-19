package altlombardisch.user;

import altlombardisch.auth.UserRoles;
import altlombardisch.auth.WebSession;
import altlombardisch.ui.AjaxView;
import altlombardisch.ui.TitleLabel;
import altlombardisch.ui.page.BasePage;
import altlombardisch.ui.panel.FeedbackPanel;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

/**
 * A page containing a user list and a user edit form.
 */
@AuthorizeInstantiation("SIGNED_IN")
public class UserEditPage extends BasePage {
    /**
     * Model of the edited user object.
     */
    private final CompoundPropertyModel<User> userModel;

    /**
     * Creates a user edit page.
     */
    public UserEditPage() {
        User sessionUser = WebSession.get().getUser();

        // check if the session is expired
        WebSession.get().checkSessionExpired();
        userModel = new CompoundPropertyModel<>(sessionUser);
    }

    /**
     * Called when a user edit page is initialized.
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();
        MarkupContainer feedbackPanel = new FeedbackPanel();
        UserViewPanel userViewPanel = new UserViewPanel();
        AjaxView<User> userView = userViewPanel.getUserView();

        add(new TitleLabel(getString("UserEditPage.header")));
        add(new UserDeleteDeniedPanel());
        add(feedbackPanel);
        add(new UserEditPanel(userModel, userView));
        add(userViewPanel);
        add(new AddUserButton());
        feedbackPanel.setOutputMarkupId(true);
    }

    /**
     * Returns markup variations based on user roles.
     *
     * @return An identifier for a markup variation.
     */
    @Override
    public String getVariation() {
        User sessionUser = WebSession.get().getUser();

        if (sessionUser == null) {
            return "user";
        }

        if (sessionUser.getRole().equals(UserRoles.Role.ADMIN)) {
            return super.getVariation();
        } else {
            return "user";
        }
    }

    /**
     * A button which starts the creation of a new user.
     */
    @AuthorizeAction(action = "RENDER", roles = {UserRoles.ADMIN})
    private final class AddUserButton extends AjaxLink<Void> {
        /**
         * Creates a add user button.
         */
        private AddUserButton() {
            super("addUserButton");
        }

        /**
         * Called on button click.
         *
         * @param target target that produces an Ajax response
         */
        @Override
        public void onClick(AjaxRequestTarget target) {
            Page userEditPage = getPage();
            Panel userViewPanel = (UserViewPanel) userEditPage.get("userViewPanel");
            Panel userEditPanel = (Panel) userEditPage.get("userEditPanel");
            AjaxView<User> userView = (AjaxView<User>) userViewPanel.get("userView");
            Component userEditForm = userEditPanel.get("userEditForm");
            Component newUserEditForm = new UserEditForm(new CompoundPropertyModel<>(new User()), userView);

            userView.setSelectedModel(new Model<>(new User()));
            userView.refresh(target);
            userEditForm.replaceWith(newUserEditForm);
            target.add(newUserEditForm);
            target.focusComponent(newUserEditForm.get("user"));

            // clear feedback panel
            WebSession.get().clearFeedbackMessages();
            target.add(userEditPage.get("feedbackPanel"));
        }
    }
}
