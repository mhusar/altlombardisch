package altlombardisch.user;

import altlombardisch.HomePage;
import altlombardisch.auth.SignInPage;
import altlombardisch.auth.UserRoles;
import altlombardisch.auth.WebSession;
import altlombardisch.ui.AjaxView;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * A form for editing special users.
 */
class UserEditForm extends Form<User> {
    /**
     * A user view displaying special users.
     */
    private final AjaxView<User> userView;

    /**
     * Creates a special user edit form.
     *
     * @param model         model of the user
     * @param userView user view displaying users
     */
    public UserEditForm(IModel<User> model, AjaxView<User> userView) {
        super("userEditForm", model);

        this.userView = userView;
        TextField<String> realNameTextField = new RequiredTextField<>("realName");
        TextField<String> usernameTextField = new RequiredTextField<>("username");
        ListChoice<UserRoles.Role> roleListChoice = new ListChoice<>("role",
                new PropertyModel<>(getModelObject(), "role"),
                new ArrayList<>(Arrays.asList(UserRoles.Role.values())),
                new EnumChoiceRenderer<>(), 3);

        add(realNameTextField.setOutputMarkupId(true));
        add(usernameTextField);
        add(new PasswordTextField("password").setResetPassword(false).setRequired(true));
        add(new CancelButton());
        add(new SaveButton(this));

        if (WebSession.get().getUser().getRole().equals(UserRoles.Role.ADMIN)) {
            CheckBox enabledCheckBox = new CheckBox("enabled");

            add(roleListChoice);
            add(enabledCheckBox);
            add(new DeleteButton(getModel()).setVisible(!(isUserTransient(getModelObject()))));

            if (isUserTransient(getModelObject())) {
                enabledCheckBox.add(AttributeModifier.append("checked", "checked"));
            }

            roleListChoice.add(new RequiredRoleValidator());
        }

        realNameTextField.add(new UniqueRealNameValidator(getModel()));
        usernameTextField.add(new UniqueUsernameValidator(getModel()));
    }

    /**
     * Checks if a user is transient.
     *
     * @param user user that is checked
     * @return True if a user is transient; false otherwise.
     */
    private Boolean isUserTransient(User user) {
        return new UserDao().isTransient(user);
    }

    /**
     * A button which saves form contents.
     */
    private final class SaveButton extends AjaxButton {
        /**
         * Creates a save button.
         *
         * @param form form that is submitted
         */
        public SaveButton(final Form<User> form) {
            super("saveButton", form);
        }

        /**
         * Called on form submit.
         *
         * @param target target that produces an Ajax response
         * @param form   the submitted form
         */
        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            Panel feedbackPanel = (Panel) form.getPage().get("feedbackPanel");
            UserDao userDao = new UserDao();
            User user = (User) form.getModelObject();
            Integer userId = user.getId();
            Boolean passwordChanged = true;

            if (userId != null) {
                User persistentUser = userDao.find(userId);

                if (persistentUser != null) {
                    passwordChanged = !user.getPassword().equals(persistentUser.getPassword());
                }
            }

            if (passwordChanged) {
                try {
                    byte[] saltBytes = userDao.createRandomSaltBytes();
                    String hashedPassword = userDao.hashPassword(user.getPassword(), saltBytes);

                    user.setPassword(hashedPassword);
                    user.setSalt(saltBytes);
                } catch (NoSuchAlgorithmException | InvalidKeySpecException ignored) {
                }
            }

            if (userDao.isTransient(user)) {
                userDao.persist(user);
                userId = user.getId();
            } else {
                userDao.merge(user);
            }

            if (WebSession.get().getUser().getRole().equals(UserRoles.Role.ADMIN)) {
                User refreshedUser = userDao.find(userId);
                IModel<User> refreshedUserModel = new Model<>(refreshedUser);
                Form<User> newEditForm = new UserEditForm(
                        new CompoundPropertyModel<>(refreshedUser), userView);

                this.remove();
                userView.setSelectedModel(refreshedUserModel);
                userView.refresh(target);
                target.add(feedbackPanel);
                form.replaceWith(newEditForm);
                target.add(newEditForm);
                target.focusComponent(newEditForm.get("user"));
            } else {
                setResponsePage(HomePage.class);
            }
        }

        /**
         * Called when form submit fails.
         *
         * @param target target that produces an Ajax response
         * @param form   the submitted form
         */
        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form) {
            Panel feedbackPanel = (Panel) form.getPage().get("feedbackPanel");

            target.add(feedbackPanel);
            target.appendJavaScript("setupFeedbackPanel(\"#" + feedbackPanel.getMarkupId() + "\")");
        }
    }

    /**
     * A button which cancels the editing of a user.
     */
    private final class CancelButton extends AjaxLink<Void> {
        /**
         * Creates a cancel button.
         */
        public CancelButton() {
            super("cancelButton");
        }

        /**
         * Called on button click.
         *
         * @param target target that produces an Ajax response
         */
        @Override
        @SuppressWarnings("unchecked")
        public void onClick(AjaxRequestTarget target) {
            Form<User> editForm = findParent(UserEditForm.class);
            Form<User> newEditForm = null;
            Panel feedbackPanel = (Panel) editForm.getPage().get("feedbackPanel");
            Iterator<Component> iterator = userView.iterator();

            if (WebSession.get().getUser().getRole().equals(UserRoles.Role.ADMIN)) {
                if (iterator.hasNext()) {
                    Item<User> item = (Item<User>) iterator.next();
                    IModel<User> firstUserModel = item.getModel();
                    newEditForm = new UserEditForm(
                            new CompoundPropertyModel<>(firstUserModel), userView);

                    userView.setSelectedModel(firstUserModel);
                } else {
                    newEditForm = new UserEditForm(
                            new CompoundPropertyModel<>(new User()), userView);

                    userView.setSelectedModel(new Model<>(new User()));
                }

                this.remove();
                userView.refresh(target);
                editForm.replaceWith(newEditForm);
                target.add(newEditForm);
                target.focusComponent(newEditForm.get("user"));

                // clear feedback panel
                WebSession.get().clearFeedbackMessages();
                target.add(feedbackPanel);
            } else {
                setResponsePage(HomePage.class);
            }
        }
    }

    /**
     * A button which deletes a siglum variant.
     */
    private final class DeleteButton extends AjaxLink<User> {
        /**
         * Creates a delete button.
         *
         * @param model model which is deleted by the button
         */
        private DeleteButton(IModel<User> model) {
            super("deleteButton", model);
        }

        /**
         * Called on button click.
         *
         * @param target target that produces an Ajax response
         */
        @Override
        @SuppressWarnings("unchecked")
        public void onClick(AjaxRequestTarget target) {
            Iterator<Component> iterator = userView.iterator();
            IModel<User> selectedUserModel = null;
            Item<User> lastItem = null;

            while (iterator.hasNext()) {
                Item<User> item = (Item<User>) iterator.next();

                if (getModelObject().equals(item.getModelObject())) {
                    if (lastItem != null) {
                        selectedUserModel = lastItem.getModel();
                    } else if (iterator.hasNext()) {
                        selectedUserModel = ((Item<User>) iterator.next()).getModel();
                        break;
                    }
                }

                lastItem = item;
            }

            if (selectedUserModel == null) {
                selectedUserModel = new Model<>(new User());
            }

            if (getModelObject().equals(WebSession.get().getUser())) {
                new UserDao().remove(getModelObject());
                WebSession.get().invalidate();
                setResponsePage(SignInPage.class);
            } else {
                new UserDao().remove(getModelObject());

                Form<User> editForm = findParent(UserEditForm.class);
                Form<User> newEditForm = new UserEditForm(
                        new CompoundPropertyModel<>(selectedUserModel), userView);
                Panel feedbackPanel = (Panel) editForm.getPage().get("feedbackPanel");

                this.remove();
                userView.setSelectedModel(selectedUserModel);
                userView.refresh(target);
                editForm.replaceWith(newEditForm);
                target.add(newEditForm);
                target.focusComponent(newEditForm.get("user"));

                // clear feedback panel
                WebSession.get().clearFeedbackMessages();
                target.add(feedbackPanel);
            }
        }
    }

    /**
     * Checks if a user has a role.
     */
    private class RequiredRoleValidator implements
            INullAcceptingValidator<UserRoles.Role> {
        /**
         * Validates value of a form component.
         *
         * @param validatable IValidatable instance that is validated
         */
        @Override
        public void validate(IValidatable<UserRoles.Role> validatable) {
            ValidationError error = new ValidationError();

            if (validatable.getValue() == null) {
                error.addKey("UserEditForm.role-is-required");
            }

            if (!(error.getKeys().isEmpty())) {
                validatable.error(error);
            }
        }
    }

    /**
     * Validates a users’s realName against other existent users.
     */
    private class UniqueRealNameValidator implements IValidator<String> {
        /**
         * User model that is edited.
         */
        private final IModel<User> userModel;

        /**
         * Creates a realName validator.
         *
         * @param model user model that is edited
         */
        public UniqueRealNameValidator(IModel<User> model) {
            userModel = model;
        }

        /**
         * Validates value of a form component.
         *
         * @param validatable IValidatable instance that is validated
         */
        @Override
        public void validate(IValidatable<String> validatable) {
            ValidationError error = new ValidationError();
            UserDao userDao = new UserDao();
            User user = userDao.findByRealName(validatable.getValue());

            if (userDao.isTransient(userModel.getObject())) {
                if (user != null) {
                    error.addKey("UserEditForm.realName-is-non-unique");
                }
            } else if (user != null) {
                if (!(user.equals(userModel.getObject()))) {
                    error.addKey("UserEditForm.realName-is-non-unique");
                }
            }

            if (!(error.getKeys().isEmpty())) {
                validatable.error(error);
            }
        }
    }

    /**
     * Validates a users’s username against other existent users.
     */
    private class UniqueUsernameValidator implements IValidator<String> {
        /**
         * User model that is edited.
         */
        private final IModel<User> userModel;

        /**
         * Creates a username validator.
         *
         * @param model user model that is edited
         */
        public UniqueUsernameValidator(IModel<User> model) {
            userModel = model;
        }

        /**
         * Validates the value of a form component.
         *
         * @param validatable IValidatable instance that is validated
         */
        @Override
        public void validate(IValidatable<String> validatable) {
            ValidationError error = new ValidationError();
            UserDao userDao = new UserDao();
            User user = userDao.findByUsername(validatable.getValue());

            if (userDao.isTransient(userModel.getObject())) {
                if (user != null) {
                    error.addKey("UserEditForm.username-is-non-unique");
                }
            } else if (user != null) {
                if (!(user.equals(userModel.getObject()))) {
                    error.addKey("UserEditForm.username-is-non-unique");
                }
            }

            if (!(error.getKeys().isEmpty())) {
                validatable.error(error);
            }
        }
    }
}
