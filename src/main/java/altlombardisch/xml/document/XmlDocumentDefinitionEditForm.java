package altlombardisch.xml.document;

import altlombardisch.ui.AjaxView;
import altlombardisch.ui.panel.FeedbackPanel;
import altlombardisch.ui.xml.XmlEditor;
import altlombardisch.ui.xml.XmlValidator;
import altlombardisch.xml.tag.XmlTagDefinitionEditPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.*;

/**
 * An edit form for XML document definitions.
 */
public class XmlDocumentDefinitionEditForm extends Form<XmlDocumentDefinition> {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * An Ajax view displaying document definitions.
     */
    private AjaxView<XmlDocumentDefinition> definitionView;

    /**
     * Creates a XML document definition edit form.
     * 
     * @param id
     *            ID of the form
     * @param model
     *            model of the document definition
     * @param definitionView
     *            view displaying document definitions
     */
    public XmlDocumentDefinitionEditForm(String id, IModel<XmlDocumentDefinition> model,
                                         AjaxView<XmlDocumentDefinition> definitionView) {
        super(id, model);

        this.definitionView = definitionView;
        TextField<String> rootElementTextfield = new RequiredTextField<String>("rootElement",
                new PropertyModel<String>(model, "rootElement"));
        XmlEditor schemaXmlEditor = new XmlEditor("schema", new PropertyModel<String>(model, "schema"),
                new Model<String>(new StringResourceModel("XmlDocumentDefinition.schema").getString()));
        XmlEditor xslXmlEditor = new XmlEditor("xsl", new PropertyModel<String>(model, "xsl"),
                new Model<String>(new StringResourceModel("XmlDocumentDefinition.xsl").getString()),
                XmlValidator.ValidatorType.Type.XSL);

        add(new XmlTagDefinitionEditPageLink("editTagsLink", model));
        add(new FeedbackPanel().setEscapeModelStrings(false).setOutputMarkupId(true));
        add(rootElementTextfield);
        add(schemaXmlEditor);
        add(xslXmlEditor);
        add(new CancelButton("cancelButton"));
        add(new SaveButton("saveButton", this));

        rootElementTextfield.setOutputMarkupId(true);
        schemaXmlEditor.setRows(20);
        schemaXmlEditor.setMaximumLength(5000);
        schemaXmlEditor.setRequired(true);
        xslXmlEditor.setRows(20);
        xslXmlEditor.setMaximumLength(5000);
        xslXmlEditor.setRequired(false);
    }

    /**
     * Links to the attribute definition edit page of the child attribute
     * definitions.
     */
    private class XmlTagDefinitionEditPageLink extends
            Link<XmlDocumentDefinition> {
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
        public XmlTagDefinitionEditPageLink(String id, IModel<XmlDocumentDefinition> model) {
            super(id, model);
        }

        /**
         * Called when link is clicked.
         */
        @Override
        public void onClick() {
            XmlDocumentDefinition reloadedDefinition = new XmlDocumentDefinitionDao().findById(getModelObject().getId());

            setResponsePage(new XmlTagDefinitionEditPage(new Model<XmlDocumentDefinition>(reloadedDefinition)));
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
        public SaveButton(String id, final Form<XmlDocumentDefinition> form) {
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
            XmlDocumentDefinitionDao definitionDao = new XmlDocumentDefinitionDao();
            XmlDocumentDefinition definition = (XmlDocumentDefinition) form.getModelObject();

            if (definitionDao.isTransient(definition)) {
                definitionDao.persist(definition);
            } else {
                definitionDao.merge(definition);
            }

            Form<XmlDocumentDefinition> newEditForm = new XmlDocumentDefinitionEditForm(
                    "definitionEditForm",
                    new CompoundPropertyModel<XmlDocumentDefinition>(definition),
                    definitionView);

            this.remove();
            definitionView.setSelectedModel((IModel<XmlDocumentDefinition>) form.getModel());
            definitionView.refresh(target);
            target.add(feedbackPanel);
            form.replaceWith(newEditForm);
            target.add(newEditForm);
            target.focusComponent(newEditForm.get("rootElement"));
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
     * A button which cancels the editing of a document definition.
     */
    private final class CancelButton extends AjaxLink<XmlDocumentDefinition> {
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
            Form<XmlDocumentDefinition> editForm = findParent(XmlDocumentDefinitionEditForm.class);
            XmlDocumentDefinition definition = new XmlDocumentDefinitionDao()
                    .findFirst();

            if (definition instanceof XmlDocumentDefinition) {
                IModel<XmlDocumentDefinition> model = new Model<XmlDocumentDefinition>(definition);
                Form<XmlDocumentDefinition> newEditForm = new XmlDocumentDefinitionEditForm(
                        "definitionEditForm", model, definitionView);

                definitionView.setSelectedModel(model);
                definitionView.refresh(target);
                editForm.replaceWith(newEditForm);
                target.add(newEditForm);
                target.focusComponent(newEditForm.get("rootElement"));
            }
        }
    }
}
