package altlombardisch.siglum;

import altlombardisch.table.TextFilterColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * A TextFilteredColumn adding to display values of context type enums properly.
 */
class SiglumTypeTextFilterColumn extends TextFilterColumn<Siglum, Siglum, String> {
    /**
     * Creates a TextFilterColumn for context type enums.
     *
     * @param displayModel       title of a column
     * @param propertyExpression property expression of a column
     */
    public SiglumTypeTextFilterColumn(IModel<String> displayModel, String propertyExpression) {
        super(displayModel, propertyExpression);
    }

    /**
     * Creates a TextFilterColumn for context type enums.
     *
     * @param displayModel       title of a column
     * @param sortProperty       sort property of a column
     * @param propertyExpression property expression of a column
     */
    public SiglumTypeTextFilterColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
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
    public void populateItem(Item<ICellPopulator<Siglum>> item, String componentId, IModel<Siglum> rowModel) {
        Siglum context = rowModel.getObject();
        SiglumType.Type type = context.getType();

        item.add(new SiglumTypePanel(componentId, new StringResourceModel("Type." + type.name())));
    }

    /**
     * A panel used for displaying text for context type enumns.
     */
    private class SiglumTypePanel extends Panel {
        /**
         * Creates a panel.
         *
         * @param id    ID of the panel
         * @param model string resource model to display
         */
        public SiglumTypePanel(String id, StringResourceModel model) {
            super(id);
            add(new Label("enumText", model));
        }
    }
}
