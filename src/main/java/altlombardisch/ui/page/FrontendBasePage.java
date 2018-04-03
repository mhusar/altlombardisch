package altlombardisch.ui.page;

import org.apache.wicket.markup.head.*;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.http.WebResponse;

/**
 * An empty base page without any panel.
 */
public class FrontendBasePage extends WebPage {
    /**
     * Creates a base page.
     */
    protected FrontendBasePage() {
        super();
    }

    /**
     * Creates a base page.
     *
     * @param model the page model
     */
    protected FrontendBasePage(IModel<?> model) {
        super(model);
    }

    /**
     * Configures the response of a page.
     *
     * @param response response object
     */
    @Override
    protected void configureResponse(WebResponse response) {
        String contentType = WebApplication.get().getInitParameter("wicket.contentType");

        if (contentType != null) {
            response.setContentType(contentType);
        }
    }

    /**
     * Renders header items to the web response.
     *
     * @param response the response object
     */
    public void renderHead(IHeaderResponse response) {
        HeaderItem jQueryUiStyleItem = CssUrlReferenceHeaderItem
                .forUrl("/webjars/jquery-ui/1.12.1/jquery-ui.min.css");
        HeaderItem bootstrapStyleItem = CssUrlReferenceHeaderItem
                .forUrl("/webjars/bootstrap/3.3.7-1/css/bootstrap.min.css");
        HeaderItem bootstrapThemeStyleItem = CssUrlReferenceHeaderItem
                .forUrl("/webjars/bootstrap/3.3.7-1/css/bootstrap-theme.min.css");
        HeaderItem jqueryScriptItem = JavaScriptHeaderItem.forReference(getApplication()
                .getJavaScriptLibrarySettings().getJQueryReference());
        HeaderItem jQueryUiScriptItem = JavaScriptUrlReferenceHeaderItem
                .forUrl("/webjars/jquery-ui/1.12.1/jquery-ui.min.js");
        HeaderItem bootstrapScriptItem = JavaScriptUrlReferenceHeaderItem
                .forUrl("/webjars/bootstrap/3.3.7-1/js/bootstrap.min.js");
        StringHeaderItem faviconItem = new StringHeaderItem("<link rel=\"icon\" type=\"image/png\" " +
                "href=\"/favicon.png\" sizes=\"32x32\"/>");

        response.render(jQueryUiStyleItem);
        response.render(bootstrapStyleItem);
        response.render(bootstrapThemeStyleItem);
        response.render(jqueryScriptItem);
        response.render(jQueryUiScriptItem);
        response.render(bootstrapScriptItem);
        response.render(faviconItem);
    }
}
