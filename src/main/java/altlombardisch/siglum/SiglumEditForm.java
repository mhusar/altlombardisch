package altlombardisch.siglum;

import altlombardisch.siglum.text.SiglumTextPreviewPanel;
import altlombardisch.ui.SubmitLink;
import altlombardisch.ui.panel.FeedbackPanel;
import altlombardisch.ui.panel.ModalMessagePanel;
import altlombardisch.ui.xml.XmlEditor;
import altlombardisch.ui.xml.XmlTextField;
import altlombardisch.xml.XmlHelper;
import altlombardisch.xml.document.XmlDocumentDefinition;
import altlombardisch.xml.document.XmlDocumentDefinitionDao;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A form for editing siglums.
 */
public class SiglumEditForm extends Form<Siglum> {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Class of the next page.
     */
    private Class<? extends Page> nextPageClass;

    /**
     * Creates a new siglum edit form.
     * 
     * @param id
     *            ID of the edit form
     * @param model
     *            siglum model that is edited
     * @param nextPageClass
     *            class of the next page
     */
    public SiglumEditForm(String id, IModel<Siglum> model,
            Class<? extends Page> nextPageClass) {
        super(id, model);

        this.nextPageClass = nextPageClass;
        initialize();
    }

    /**
     * Called when a siglum edit form is initialized.
     * Don’t use onInitialize here, because the validator of field
     * Siglum.text won’t work (?!).
     */
    private void initialize() {
        XmlDocumentDefinitionDao documentDefinitionDao = new XmlDocumentDefinitionDao();
        XmlDocumentDefinition fontMarkupDocumentDefinition = documentDefinitionDao
                .findByIdentifier("fontMarkup");
        XmlDocumentDefinition siglumTextMarkupDocumentDefinition = documentDefinitionDao
                .findByIdentifier("siglumTextMarkup");
        XmlTextField taggedNameTextField = new XmlTextField("taggedName",
                fontMarkupDocumentDefinition);
        XmlEditor textXmlEditor = new XmlEditor("text", new PropertyModel<String>(getModel(), "text"),
                new Model<String>(new StringResourceModel("Siglum.text").getString()),
                siglumTextMarkupDocumentDefinition);
        ListChoice<SiglumType.Type> typeListChoice = new ListChoice<SiglumType.Type>(
                "type", new PropertyModel<SiglumType.Type>(getModelObject(),
                        "type"), new ArrayList<SiglumType.Type>(
                        Arrays.asList(SiglumType.Type.values())),
                new EnumChoiceRenderer<SiglumType.Type>(), 1);
        Button saveButton = new SaveButton("saveButton");

        add(new FeedbackPanel().setEscapeModelStrings(false)
                .setOutputMarkupId(true));
        add(taggedNameTextField);
        add(textXmlEditor);
        add(typeListChoice);
        add(new IntermediateStorageButton("intermediateStorageButton", this));
        add(new CancelButton("cancelButton"));
        add(saveButton);
        add(new DeleteButton("deleteButton", getModel())
                .setVisible(!(isSiglumTransient(getModel()))));
        add(new SiglumTextPreviewPanel(getModel()));

        taggedNameTextField.setRequired(true);
        taggedNameTextField.add(new UniqueSiglumNameValidator(getModel()));
        textXmlEditor.setMaximumLength(5000);
        textXmlEditor.setRows(5);
        typeListChoice.add(new RequiredTypeValidator());
    }

    /**
     * Checks if a siglum model is transient.
     * 
     * @param model
     *            siglum model that is checked
     * @return True if a siglum model is transient; false otherwise.
     */
    private Boolean isSiglumTransient(IModel<Siglum> model) {
        return new SiglumDao().isTransient(model.getObject());
    }

    /**
     * Called on form submit.
     */
    @Override
    protected void onSubmit() {
        SiglumDao siglumDao = new SiglumDao();
        Siglum siglum = getModelObject();
        IFormSubmitter submittingComponent = findSubmittingButton();
        String siglumName = XmlHelper.getTextContent(siglum.getTaggedName());

        siglum.setName(siglumName);

        if (siglumDao.isTransient(siglum)) {
            siglumDao.persist(siglum);
        } else {
            siglumDao.merge(siglum);
        }

        if (submittingComponent instanceof SubmitLink
                || submittingComponent instanceof IntermediateStorageButton) {
            siglumDao.refresh(getModelObject());
            return;
        }

        if (nextPageClass instanceof Class) {
            setResponsePage(nextPageClass);
        } else {
            setResponsePage(SiglumEditPage.class);
        }
    }

    /**
     * An button for intermediate storage.
     */
    private final class IntermediateStorageButton extends AjaxButton {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Creates an intermediate storage button.
         * 
         * @param id
         *            ID of the button
         * @param form
         *            form that is submitted
         */
        public IntermediateStorageButton(String id, Form<?> form) {
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
        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            Panel feedbackPanel = (Panel) form.get("feedbackPanel");
            target.add(feedbackPanel);
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
            target.appendJavaScript("setupFeedbackPanel(\"#" + feedbackPanel.getMarkupId() + "\")");
        }
    }

    /**
     * A button which cancels the editing of a siglum.
     */
    private final class CancelButton extends AjaxLink<Siglum> {
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
            if (nextPageClass instanceof Class) {
                setResponsePage(nextPageClass);
            } else {
                setResponsePage(SiglumIndexPage.class);
            }
        }
    }

    /**
     * A button which submits this form.
     */
    private final class SaveButton extends Button {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Creates a new save button.
         * 
         * @param id
         *            ID of the button
         */
        public SaveButton(String id) {
            super(id);
        }
    }

    /**
     * A button which deletes a siglum.
     */
    private final class DeleteButton extends AjaxLink<Siglum> {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Creates a new delete button.
         * 
         * @param id
         *            ID of the button
         * @param model
         *            model which is deleted by the button
         */
        private DeleteButton(String id, IModel<Siglum> model) {
            super(id, model);
        }

        /**
         * Called on button click.
         * 
         * @param target
         *            target that produces an Ajax response
         */
        @Override
        public void onClick(AjaxRequestTarget target) {
            ModalMessagePanel siglumDeleteConfirmPanel = (ModalMessagePanel) getPage().get("siglumDeleteConfirmPanel");

            siglumDeleteConfirmPanel.show(target, getModel());
        }
    }

    /**
     * Validates a siglum’s name against other existent siglums.
     */
    private class UniqueSiglumNameValidator implements IValidator<String> {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Siglum model that is edited.
         */
        private IModel<Siglum> siglumModel;

        /**
         * Creates a new siglum name validator.
         * 
         * @param model
         *            siglum model that is edited
         */
        public UniqueSiglumNameValidator(IModel<Siglum> model) {
            siglumModel = model;
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
            SiglumDao siglumDao = new SiglumDao();
            String taggedSiglumName = validatable.getValue();
            String siglumName = XmlHelper.getTextContent(taggedSiglumName);
            Siglum siglum = siglumDao.findByName(siglumName);

            if (siglumDao.isTransient(siglumModel.getObject())) {
                if (siglum instanceof Siglum) {
                    error.addKey("SiglumEditForm.siglum-is-non-unique");
                }
            } else if (siglum instanceof Siglum) {
                if (!(siglum.equals(siglumModel.getObject()))) {
                    error.addKey("SiglumEditForm.siglum-is-non-unique");
                }
            }

            if (!(error.getKeys().isEmpty())) {
                validatable.error(error);
            }
        }
    }

    /**
     * Checks if a user has a role.
     */
    private class RequiredTypeValidator implements
            INullAcceptingValidator<SiglumType.Type> {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Validates value of a form component.
         * 
         * @param validatable
         *            IValidatable instance that is validated
         */
        @Override
        public void validate(IValidatable<SiglumType.Type> validatable) {
            ValidationError error = new ValidationError();

            if (!(validatable.getValue() instanceof SiglumType.Type)) {
                error.addKey("SiglumEditForm.type-is-required");
            }

            if (!(error.getKeys().isEmpty())) {
                validatable.error(error);
            }
        }
    }
}
