package altlombardisch.siglum;

import java.util.ArrayList;
import java.util.List;

import altlombardisch.auth.WebSession;
import altlombardisch.data.GenericDataProvider;
import altlombardisch.table.GenericDataTable;
import altlombardisch.ui.TitleLabel;
import altlombardisch.ui.page.IndexBasePage;
import altlombardisch.ui.panel.FeedbackPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.Model;

import altlombardisch.table.TextFilterColumn;

/**
 * An index page that lists all available siglums in a data table.
 */
@AuthorizeInstantiation({ "SIGNED_IN" })
public class SiglumIndexPage extends IndexBasePage {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * True if the filter form shall be enabled.
     */
    private static final Boolean FILTER_FORM_ENABLED = false;

    /**
     * Creates a new siglum index page.
     */
    public SiglumIndexPage() {
        GenericDataProvider<Siglum> dataProvider = new GenericDataProvider<Siglum>(Siglum.class,
                new SortParam<String>("name", true));
        FilterForm<Siglum> filterForm = new FilterForm<Siglum>("filterForm", dataProvider);
        TextField<String> filterTextField = new TextField<String>("filterTextField", Model.of(""));
        WebMarkupContainer container = new WebMarkupContainer("container");
        Fragment fragment;
        GenericDataTable<Siglum> dataTable;

        // check if the session is expired
        WebSession.get().checkSessionExpired();

        if (FILTER_FORM_ENABLED) {
            fragment = new Fragment("fragment", "withFilterForm", this);
            dataTable = new GenericDataTable<Siglum>("siglumDataTable",
                    getColumns(), dataProvider, filterForm);

            filterTextField.add(new FilterUpdatingBehavior(filterTextField,
                    dataTable, dataProvider));
            filterForm.add(dataTable);
            fragment.add(filterForm);
        } else {
            fragment = new Fragment("fragment", "withoutFilterForm", this);
            dataTable = new GenericDataTable<Siglum>("siglumDataTable",
                    getColumns(), dataProvider);

            filterTextField.add(new FilterUpdatingBehavior(filterTextField,
                    dataTable, dataProvider));
            fragment.add(dataTable);
        }

        add(new SiglumDeleteConfirmPanel("siglumDeleteConfirmPanel", dataTable));
        add(new FeedbackPanel());
        add(filterTextField);
        add(new NewButton("new"));
        add(new BatchProcessingButton("batchProcessing"));
        add(new CorrectionButton("correction"));
        add(container);
        container.add(fragment);
    }

    /**
     * Called when a pos index page is initialized.
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new TitleLabel(getString("SiglumIndexPage.header")));
    }

    /**
     * Returns the list of columns of the data table.
     * 
     * @return A list of columns.
     */
    private List<IColumn<Siglum, String>> getColumns() {
        List<IColumn<Siglum, String>> columns = new ArrayList<IColumn<Siglum, String>>();

        columns.add(new TextFilterColumn<Siglum, Siglum, String>(Model.of(getString("Siglum.name")),
                "name", "name"));
        columns.add(new SiglumActionPanelColumn(Model.of("")));

        return columns;
    }

    /**
     * Implementation of a form component updating behavior for a filter text field.
     */
    private class FilterUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * The text field used as filter component.
         */
        TextField<String> textField;

        /**
         * Data table displaying filtered data.
         */
        GenericDataTable<Siglum> dataTable;

        /**
         * Data provider which delivers data for the table.
         */
        GenericDataProvider<Siglum> dataProvider;

        /**
         * Creates a new behavior.
         * 
         * @param textField
         *            text field used a filter component
         * @param dataTable
         *            data table displaying filtered data
         * @param dataProvider
         *            data provider which delivers data for the table.
         */
        public FilterUpdatingBehavior(TextField<String> textField,
                GenericDataTable<Siglum> dataTable,
                GenericDataProvider<Siglum> dataProvider) {
            super("input");
            this.textField = textField;
            this.dataTable = dataTable;
            this.dataProvider = dataProvider;
        }

        /**
         * Called when the text field content changes.
         * 
         * @param target
         *            target that produces an Ajax response
         */
        @Override
        protected void onUpdate(AjaxRequestTarget target) {
            target.add(dataTable);
        }
    }

    /**
     * A button which runs a siglum edit form to create a new siglum.
     */
    private class NewButton extends Link<Void> {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Creates a new button.
         * 
         * @param id
         *            ID of the button
         */
        public NewButton(String id) {
            super(id);
        }

        /**
         * Called on button click.
         */
        @Override
        public void onClick() {
            setResponsePage(new SiglumEditPage(getPage().getPageClass()));
        }
    }

    /**
     * A button which runs a siglum edit form to create new siglums.
     */
    private class BatchProcessingButton extends Link<Void> {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Creates a new batch processing button.
         * 
         * @param id
         *            ID of the button
         */
        public BatchProcessingButton(String id) {
            super(id);
        }

        /**
         * Called on button click.
         */
        @Override
        public void onClick() {
            setResponsePage(new SiglumEditPage());
        }
    }

    /**
     * A button which runs a siglum edit form to correct siglums.
     *
     */
    private class CorrectionButton extends Link<Void> {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Creates a new correction button.
         * 
         * @param id
         *            ID of the button
         */
        public CorrectionButton(String id) {
            super(id);

            if (new SiglumDao().findAll("").isEmpty()) {
                setEnabled(false);
            }
        }

        /**
         * Called on button click.
         */
        @Override
        public void onClick() {
            SiglumDao siglumDao = new SiglumDao();
            Siglum firstSiglum = siglumDao.getFirstSiglum();
            Integer nextSiglumId = siglumDao.getNextSiglumId(firstSiglum);

            setResponsePage(new SiglumEditPage(new Model<Siglum>(firstSiglum), nextSiglumId));
        }
    }
}
