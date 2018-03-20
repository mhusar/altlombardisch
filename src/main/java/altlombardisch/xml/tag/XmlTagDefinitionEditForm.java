package altlombardisch.xml.tag;

import java.util.Iterator;

import altlombardisch.ui.AjaxView;
import altlombardisch.ui.panel.FeedbackPanel;
import altlombardisch.xml.document.XmlDocumentDefinition;
import altlombardisch.xml.document.XmlDocumentDefinitionDao;
import altlombardisch.xml.document.XmlDocumentDefinitionEditPage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
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

import altlombardisch.xml.attribute.XmlAttributeDefinitionEditPage;

/**
 * An edit form for XML tag definitions.
 */
public class XmlTagDefinitionEditForm extends Form<XmlTagDefinition> {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The parent document definition model.
     */
    private IModel<XmlDocumentDefinition> parentModel;

    /**
     * An Ajax view displaying tag definitions.
     */
    private AjaxView<XmlTagDefinition> definitionView;

    /**
     * Creates a XML tag definition edit form.
     * 
     * @param id
     *            ID of the form
     * @param parentModel
     *            parent document definition model
     * @param model
     *            model of the tag definition
     * @param definitionView
     *            view displaying tag definitions
     */
    public XmlTagDefinitionEditForm(String id,
            IModel<XmlDocumentDefinition> parentModel,
            IModel<XmlTagDefinition> model,
            AjaxView<XmlTagDefinition> definitionView) {
        super(id, model);

        this.parentModel = parentModel;
        this.definitionView = definitionView;
        TextField<String> nameTextField = new RequiredTextField<String>("name",
                new PropertyModel<String>(model, "name"));
        CheckBox selfClosingCheckBox = new CheckBox("selfClosing",
                new PropertyModel<Boolean>(model, "selfClosing"));
        WebMarkupContainer editAttributesListItem = new WebMarkupContainer(
                "editAttributesListItem");
        XmlAttributeDefinitionEditPageLink editAttributesLink = new XmlAttributeDefinitionEditPageLink(
                "editAttributesLink", model);

        add(new XmlDocumentDefinitionEditPageLink("editDocumentLink",
                parentModel));
        add(editAttributesListItem);
        editAttributesListItem.add(editAttributesLink);
        add(new FeedbackPanel().setEscapeModelStrings(false)
                .setOutputMarkupId(true));
        add(nameTextField);
        add(selfClosingCheckBox);
        add(new CancelButton("cancelButton"));
        add(new SaveButton("saveButton", this));
        add(new DeleteButton("deleteButton", model)
                .setVisible(!(isDefinitionTransient(model.getObject()))));

        nameTextField.setOutputMarkupId(true);
        nameTextField.add(new UniqueNameValidator(parentModel, getModel()));

        if (new XmlTagDefinitionDao().isTransient(getModelObject())) {
            editAttributesListItem.add(AttributeModifier.append("class",
                    "disabled"));
        }
    }

    /**
     * Checks if a tag definition is transient.
     * 
     * @param definition
     *            tag definition that is checked
     * @return True if a tag definition is transient; false otherwise.
     */
    private Boolean isDefinitionTransient(XmlTagDefinition definition) {
        return new XmlTagDefinitionDao().isTransient(definition);
    }

    /**
     * Links to the document definition edit page of the parent document
     * definition.
     */
    private class XmlDocumentDefinitionEditPageLink extends
            Link<XmlDocumentDefinition> {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Creates a link to a document definition edit page.
         * 
         * @param id
         *            ID of the link
         * @param model
         *            parent document definition model
         */
        public XmlDocumentDefinitionEditPageLink(String id,
                IModel<XmlDocumentDefinition> model) {
            super(id, model);
        }

        /**
         * Called when link is clicked.
         */
        @Override
        public void onClick() {
            XmlDocumentDefinition reloadedDefinition = new XmlDocumentDefinitionDao()
                    .findById(getModelObject().getId());

            setResponsePage(new XmlDocumentDefinitionEditPage(
                    new Model<XmlDocumentDefinition>(reloadedDefinition)));
        }
    }

    /**
     * Links to the attribute definition edit page of the child attribute
     * definitions.
     */
    private class XmlAttributeDefinitionEditPageLink extends
            Link<XmlTagDefinition> {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Creates a link to a attribute definition edit page.
         * 
         * @param id
         *            ID of the link
         * @param model
         *            parent document definition model
         */
        public XmlAttributeDefinitionEditPageLink(String id,
                IModel<XmlTagDefinition> model) {
            super(id, model);
        }

        /**
         * Called when link is clicked.
         */
        @Override
        public void onClick() {
            XmlTagDefinition reloadedDefinition = new XmlTagDefinitionDao().findById(getModelObject().getId());

            setResponsePage(new XmlAttributeDefinitionEditPage(
                    new Model<XmlTagDefinition>(reloadedDefinition)));
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
        public SaveButton(String id, final Form<XmlTagDefinition> form) {
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
            XmlTagDefinitionDao definitionDao = new XmlTagDefinitionDao();
            XmlTagDefinition definition = (XmlTagDefinition) form
                    .getModelObject();

            definition.setDocumentDefinition(parentModel.getObject());

            if (definitionDao.isTransient(definition)) {
                definitionDao.persist(definition);
            } else {
                definitionDao.merge(definition);
            }

            Form<XmlTagDefinition> newEditForm = new XmlTagDefinitionEditForm(
                    "definitionEditForm", parentModel,
                    new CompoundPropertyModel<XmlTagDefinition>(definition),
                    definitionView);

            this.remove();
            definitionView.setSelectedModel((IModel<XmlTagDefinition>) form
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
     * A button which cancels the editing of a tag definition.
     */
    private final class CancelButton extends AjaxLink<XmlTagDefinition> {
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
            Form<XmlTagDefinition> editForm = findParent(XmlTagDefinitionEditForm.class);
            XmlTagDefinition definition = new XmlTagDefinitionDao()
                    .findFirst(parentModel.getObject());

            if (definition instanceof XmlTagDefinition) {
                IModel<XmlTagDefinition> model = new Model<XmlTagDefinition>(
                        definition);
                Form<XmlTagDefinition> newEditForm = new XmlTagDefinitionEditForm(
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
     * A button which deletes a tag definition.
     */
    private final class DeleteButton extends AjaxLink<XmlTagDefinition> {
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
        private DeleteButton(String id, IModel<XmlTagDefinition> model) {
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
            IModel<XmlTagDefinition> selectedDefinitionModel = null;

            while (iterator.hasNext()) {
                Item<XmlTagDefinition> item = (Item<XmlTagDefinition>) iterator
                        .next();

                if (getModelObject().equals(item.getModelObject())) {
                    index = item.getIndex();
                }
            }

            if (index > 0) {
                selectedDefinitionModel = ((Item<XmlTagDefinition>) definitionView
                        .get(index - 1)).getModel();
            } else if (definitionView.size() > 1) {
                selectedDefinitionModel = ((Item<XmlTagDefinition>) definitionView
                        .get(1)).getModel();
            } else {
                selectedDefinitionModel = new Model<XmlTagDefinition>(
                        new XmlTagDefinition());
            }

            Form<XmlTagDefinition> editForm = findParent(XmlTagDefinitionEditForm.class);
            Form<XmlTagDefinition> newEditForm = new XmlTagDefinitionEditForm(
                    "definitionEditForm", parentModel,
                    new CompoundPropertyModel<XmlTagDefinition>(
                            selectedDefinitionModel), definitionView);

            this.remove();
            new XmlTagDefinitionDao().remove(getModelObject());
            definitionView.setSelectedModel(selectedDefinitionModel);
            definitionView.refresh(target);
            editForm.replaceWith(newEditForm);
            target.add(newEditForm);
            target.focusComponent(newEditForm.get("name"));
        }
    }

    /**
     * Validates an tag definition name against other existent definitions.
     */
    private class UniqueNameValidator implements IValidator<String> {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Parent document definition model.
         */
        private IModel<XmlDocumentDefinition> parentModel;

        /**
         * Tag definition model that is edited.
         */
        private IModel<XmlTagDefinition> model;

        /**
         * Creates a new name validator.
         * 
         * @param model
         *            tag definition model that is edited
         */
        public UniqueNameValidator(IModel<XmlDocumentDefinition> parentModel,
                IModel<XmlTagDefinition> model) {
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
            XmlTagDefinitionDao definitionDao = new XmlTagDefinitionDao();
            XmlTagDefinition definition = definitionDao.findByName(
                    parentModel.getObject(), validatable.getValue());

            if (definitionDao.isTransient(model.getObject())) {
                if (definition instanceof XmlTagDefinition) {
                    error.addKey("XmlTagDefinitionEditForm.name-is-non-unique");
                }
            } else if (definition instanceof XmlTagDefinition) {
                if (!(definition.equals(model.getObject()))) {
                    error.addKey("XmlTagDefinitionEditForm.name-is-non-unique");
                }
            }

            if (!(error.getKeys().isEmpty())) {
                validatable.error(error);
            }
        }
    }
}
