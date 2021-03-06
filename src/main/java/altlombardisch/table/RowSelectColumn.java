package altlombardisch.table;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * A TextFilteredColumn enabling row selection.
 *
 * @param <T> object type
 * @param <F> filter model type
 * @param <S> sort property type
 */
public class RowSelectColumn<T, F, S> extends TextFilterColumn<T, F, S> {
    /**
     * Creates a row selection column.
     *
     * @param displayModel       title of a column
     * @param propertyExpression property expression of a column
     */
    public RowSelectColumn(IModel<String> displayModel, String propertyExpression) {
        super(displayModel, propertyExpression);
    }

    /**
     * Creates a row selection column.
     *
     * @param displayModel       model of a column
     * @param sortProperty       sort property of a column
     * @param propertyExpression property expression of a column
     */
    public RowSelectColumn(IModel<String> displayModel, S sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    /**
     * Populates the current table cell item.
     *
     * @param item        item representing the current table cell being rendered
     * @param componentId id of the component used to render the cell
     * @param rowModel    model of the row item being rendered
     */
    @Override
    public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
        item.add(new CheckboxPanel(componentId, rowModel));
    }

    /**
     * A panel used to display a checkbox.
     */
    private class CheckboxPanel extends Panel {
        /**
         * Creates a panel.
         *
         * @param id    ID of the panel
         * @param model row model
         */
        public CheckboxPanel(String id, IModel<T> model) {
            super(id, model);
            CheckBox checkBox = new CheckBox("selected", new PropertyModel<>(model, getPropertyExpression()));
            checkBox.add(new AjaxFormComponentUpdatingBehavior("change") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                }
            });
            add(checkBox);
        }
    }
}
