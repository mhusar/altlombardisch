package altlombardisch.siglum;

import altlombardisch.auth.UserRoles;
import altlombardisch.auth.WebSession;
import altlombardisch.table.FilterPanelColumn;
import altlombardisch.ui.panel.ModalMessagePanel;
import altlombardisch.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * A custom column with actions for siglums and and a filter panel as filter.
 */
public class SiglumActionPanelColumn extends FilterPanelColumn<Siglum> {
    /**
     * Determines if a deserialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new column.
     * 
     * @param displayModel
     *            title of the column
     */
    public SiglumActionPanelColumn(IModel<String> displayModel) {
        super(displayModel, Siglum.class);
    }

    /**
     * Returns the CSS class of this type of column.
     * 
     * @return A string representing a CSS class.
     */
    @Override
    public String getCssClass() {
        return "actionColumn";
    }

    /**
     * Populates cell items with components.
     * 
     * @param cellItem
     *            cell item that is populated
     * @param componentId
     *            ID of the child component
     * @param rowModel
     *            model of the row
     */
    @Override
    public void populateItem(Item<ICellPopulator<Siglum>> cellItem,
            String componentId, IModel<Siglum> rowModel) {

        cellItem.add(new ActionPanel(componentId, rowModel));
    }

    /**
     * A panel with actions for siglum objects.
     */
    private class ActionPanel extends Panel {
        /**
         * Determines if a deserialized file is compatible with this class.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Creates a new action panel.
         * 
         * @param id
         *            ID of the panel
         * @param model
         *            siglum model of a cell item
         */
        public ActionPanel(String id, final IModel<Siglum> model) {
            super(id, model);

            User sessionUser = WebSession.get().getUser();

            if (sessionUser instanceof User) {
                if (sessionUser.getRole().equals(UserRoles.Role.USER)
                        || sessionUser.getRole().equals(UserRoles.Role.ADMIN)) {
                    add(new AjaxLink<Void>("editLink") {
                        /**
                         * Determines if a deserialized file is compatible with
                         * this class.
                         */
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            setResponsePage(new SiglumEditPage(model, getPage().getPageClass()));
                        }
                    });
                    add(new AjaxLink<Void>("deleteLink") {
                        /**
                         * Determines if a deserialized file is compatible with
                         * this class.
                         */
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            ModalMessagePanel siglumDeleteConfirmPanel = (ModalMessagePanel) getPage()
                                    .get("siglumDeleteConfirmPanel");

                            siglumDeleteConfirmPanel.show(target, model);
                        }
                    });
                } else {
                    add(new AjaxLink<Void>("viewLink") {
                        /**
                         * Determines if a deserialized file is compatible with
                         * this class.
                         */
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            setResponsePage(new SiglumViewPage(model));
                        }
                    });
                }
            }
        }

        /**
         * Returns markup variations based on user roles.
         * 
         * @return An identifier for a markup variation.
         */
        @Override
        public String getVariation() {
            User sessionUser = WebSession.get().getUser();

            if (!(sessionUser instanceof User)) {
                return "student";
            }

            if (sessionUser.getRole().equals(UserRoles.Role.USER)
                    || sessionUser.getRole().equals(UserRoles.Role.ADMIN)) {
                return super.getVariation();
            } else {
                return "student";
            }
        }
    }
}
