package altlombardisch.xml.attribute;

import java.util.Iterator;

import altlombardisch.ui.AjaxView;
import altlombardisch.ui.panel.FeedbackPanel;
import altlombardisch.xml.document.XmlDocumentDefinition;
import altlombardisch.xml.tag.XmlTagDefinition;
import altlombardisch.xml.tag.XmlTagDefinitionEditPage;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * An edit form for XML attribute definitions.
 */
public class XmlAttributeDefinitionEditForm extends Form<XmlAttributeDefinition> {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The parent tag definition model.
     */
    private IModel<XmlTagDefinition> parentModel;

    /**
     * An Ajax view displaying attribute definitions.
     */
    private AjaxView<XmlAttributeDefinition> definitionView;

    /**
     * Creates a XML attribute definition edit form.
     * 
     * @param id
     *            ID of the form
     * @param parentModel
     *            parent tag definition model
     * @param model
     *            model of the attribute definition
     * @param definitionView
     *            view displaying attribute definitions
     */
    public XmlAttributeDefinitionEditForm(String id,
            IModel<XmlTagDefinition> parentModel,
            IModel<XmlAttributeDefinition> model,
            AjaxView<XmlAttributeDefinition> definitionView) {
        super(id, model);

        this.parentModel = parentModel;
        this.definitionView = definitionView;
        TextField<String> nameTextField = new RequiredTextField<String>("name",
                new PropertyModel<String>(model, "name"));
        CheckBox requiredCheckBox = new CheckBox("required",
                new PropertyModel<Boolean>(model, "required"));

        add(new XmlTagDefinitionEditPageLink("editTagLink", parentModel));
        add(new FeedbackPanel().setEscapeModelStrings(false)
                .setOutputMarkupId(true));
        add(nameTextField);
        add(requiredCheckBox);
        add(new CancelButton("cancelButton"));
        add(new SaveButton("saveButton", this));
        add(new DeleteButton("deleteButton", model)
                .setVisible(!(isDefinitionTransient(model.getObject()))));

        nameTextField.setOutputMarkupId(true);
        nameTextField.add(new UniqueNameValidator(parentModel, getModel()));
    }

    /**
     * Checks if an attribute definition is transient.
     * 
     * @param definition
     *            attribute definition that is checked
     * @return True if an attribute definition is transient; false otherwise.
     */
    private Boolean isDefinitionTransient(XmlAttributeDefinition definition) {
        return new XmlAttributeDefinitionDao().isTransient(definition);
    }

    /**
     * Links to the tag definition edit page of the parent tag definition.
     */
    private class XmlTagDefinitionEditPageLink extends Link<XmlTagDefinition> {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Creates a link to a tag definition edit page.
         * 
         * @param id
         *            ID of the link
         * @param model
         *            parent tag definition model
         */
        public XmlTagDefinitionEditPageLink(String id,
                IModel<XmlTagDefinition> model) {
            super(id, model);
        }

        /**
         * Called when link is clicked.
         */
        @Override
        public void onClick() {
            setResponsePage(new XmlTagDefinitionEditPage(
                    new Model<XmlDocumentDefinition>(getModelObject()
                            .getDocumentDefinition())));
        }
    }

    /**
     * A button which saves form contents.
     */
    private final class SaveButton extends AjaxButton {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Creates a save button.
         * 
         * @param id
         *            ID of the button
         * @param form
         *            form that is submitted
         */
        public SaveButton(String id, final Form<XmlAttributeDefinition> form) {
            super(id, form);
        }

        /**
         * Called on form submit.
         * 
         * @param target
         *            target that produces an Ajax response
         * @param form
         *            the submitted form
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            Panel feedbackPanel = (Panel) form.get("feedbackPanel");
            XmlAttributeDefinitionDao definitionDao = new XmlAttributeDefinitionDao();
            XmlAttributeDefinition definition = (XmlAttributeDefinition) form
                    .getModelObject();

            definition.setTagDefinition(parentModel.getObject());

            if (definitionDao.isTransient(definition)) {
                definitionDao.persist(definition);
            } else {
                definitionDao.merge(definition);
            }

            Form<XmlAttributeDefinition> newEditForm = new XmlAttributeDefinitionEditForm(
                    "definitionEditForm", parentModel,
                    new CompoundPropertyModel<XmlAttributeDefinition>(
                            definition), definitionView);

            this.remove();
            definitionView
                    .setSelectedModel((IModel<XmlAttributeDefinition>) form
                            .getModel());
            definitionView.refresh(target);
            target.add(feedbackPanel);
            form.replaceWith(newEditForm);
            target.add(newEditForm);
            target.focusComponent(newEditForm.get("name"));
        }

        /**
         * Called when form submit fails.
         * 
         * @param target
         *            target that produces an Ajax response
         * @param form
         *            the submitted form
         */
        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form) {
            Panel feedbackPanel = (Panel) form.get("feedbackPanel");

            target.add(feedbackPanel);
            target.appendJavaScript("setupFeedbackPanel(\"#"
                    + feedbackPanel.getMarkupId() + "\")");
        }
    }

    /**
     * A button which cancels the editing of an attribute definition.
     */
    private final class CancelButton extends AjaxLink<XmlAttributeDefinition> {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Creates a new cancel button.
         * 
         * @param id
         *            ID of the button
         */
        public CancelButton(String id) {
            super(id);
        }

        /**
         * Called on button click.
         * 
         * @param target
         *            target that produces an Ajax response
         */
        @Override
        public void onClick(AjaxRequestTarget target) {
            Form<XmlAttributeDefinition> editForm = findParent(XmlAttributeDefinitionEditForm.class);
            XmlAttributeDefinition definition = new XmlAttributeDefinitionDao()
                    .findFirst(parentModel.getObject());

            if (definition instanceof XmlAttributeDefinition) {
                IModel<XmlAttributeDefinition> model = new Model<XmlAttributeDefinition>(
                        definition);
                Form<XmlAttributeDefinition> newEditForm = new XmlAttributeDefinitionEditForm(
                        "definitionEditForm", parentModel, model,
                        definitionView);

                definitionView.setSelectedModel(model);
                definitionView.refresh(target);
                editForm.replaceWith(newEditForm);
                target.add(newEditForm);
                target.focusComponent(newEditForm.get("name"));
            }
        }
    }

    /**
     * A button which deletes an attribute definition.
     */
    private final class DeleteButton extends AjaxLink<XmlAttributeDefinition> {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Creates a delete button.
         * 
         * @param id
         *            ID of the button
         * @param model
         *            model which is deleted by the button
         */
        private DeleteButton(String id, IModel<XmlAttributeDefinition> model) {
            super(id, model);
        }

        /**
         * Called on button click.
         * 
         * @param target
         *            target that produces an Ajax response
         */
        @Override
        @SuppressWarnings("unchecked")
        public void onClick(AjaxRequestTarget target) {
            Iterator<Component> iterator = definitionView.iterator();
            Integer index = 0;
            IModel<XmlAttributeDefinition> selectedDefinitionModel = null;

            while (iterator.hasNext()) {
                Item<XmlAttributeDefinition> item = (Item<XmlAttributeDefinition>) iterator
                        .next();

                if (getModelObject().equals(item.getModelObject())) {
                    index = item.getIndex();
                }
            }

            if (index > 0) {
                selectedDefinitionModel = ((Item<XmlAttributeDefinition>) definitionView
                        .get(index - 1)).getModel();
            } else if (definitionView.size() > 1) {
                selectedDefinitionModel = ((Item<XmlAttributeDefinition>) definitionView
                        .get(1)).getModel();
            } else {
                selectedDefinitionModel = new Model<XmlAttributeDefinition>(
                        new XmlAttributeDefinition());
            }

            Form<XmlAttributeDefinition> editForm = findParent(XmlAttributeDefinitionEditForm.class);
            Form<XmlAttributeDefinition> newEditForm = new XmlAttributeDefinitionEditForm(
                    "definitionEditForm", parentModel,
                    new CompoundPropertyModel<XmlAttributeDefinition>(
                            selectedDefinitionModel), definitionView);

            this.remove();
            new XmlAttributeDefinitionDao().remove(getModelObject());
            definitionView.setSelectedModel(selectedDefinitionModel);
            definitionView.refresh(target);
            editForm.replaceWith(newEditForm);
            target.add(newEditForm);
            target.focusComponent(newEditForm.get("name"));
        }
    }

    /**
     * Validates an attribute definition name against other existent
     * definitions.
     */
    private class UniqueNameValidator implements IValidator<String> {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Parent tag definition model.
         */
        private IModel<XmlTagDefinition> parentModel;

        /**
         * Attribute definition model that is edited.
         */
        private IModel<XmlAttributeDefinition> model;

        /**
         * Creates a new name validator.
         * 
         * @param model
         *            attribute definition model that is edited
         */
        public UniqueNameValidator(IModel<XmlTagDefinition> parentModel,
                IModel<XmlAttributeDefinition> model) {
            this.parentModel = parentModel;
            this.model = model;
        }

        /**
         * Validates the value of a form component.
         * 
         * @param validatable
         *            IValidatable instance that is validated
         */
        @Override
        public void validate(IValidatable<String> validatable) {
            ValidationError error = new ValidationError();
            XmlAttributeDefinitionDao definitionDao = new XmlAttributeDefinitionDao();
            XmlAttributeDefinition definition = definitionDao.findByName(
                    parentModel.getObject(), validatable.getValue());

            if (definitionDao.isTransient(model.getObject())) {
                if (definition instanceof XmlAttributeDefinition) {
                    error.addKey("XmlAttributeDefinitionEditForm.name-is-non-unique");
                }
            } else if (definition instanceof XmlAttributeDefinition) {
                if (!(definition.equals(model.getObject()))) {
                    error.addKey("XmlAttributeDefinitionEditForm.name-is-non-unique");
                }
            }

            if (!(error.getKeys().isEmpty())) {
                validatable.error(error);
            }
        }
    }
}
