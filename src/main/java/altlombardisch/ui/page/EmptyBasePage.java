package altlombardisch.ui.page;

import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.head.*;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * An empty base page without any panel.
 */
public class EmptyBasePage extends WebPage {
    /**
     * Creates a base page.
     */
    protected EmptyBasePage() {
        super();
    }

    /**
     * Creates a base page.
     *
     * @param model the page model
     */
    protected EmptyBasePage(IModel<?> model) {
        super(model);
    }

    /**
     * Configures the response of a page.
     *
     * @param response response object
     */
    @Override
    protected void configureResponse(WebResponse response) {
        String contentType = AuthenticatedWebApplication.get().getInitParameter("wicket.contentType");

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
        PackageResourceReference iconicStyle = new CssResourceReference(EmptyBasePage.class,
                "styles/open-iconic-bootstrap.css");
        PackageResourceReference cmsStyle = new CssResourceReference(EmptyBasePage.class,
                "styles/cms.css");
        PackageResourceReference cmsScript = new JavaScriptResourceReference(EmptyBasePage.class,
                "scripts/cms.js");

        HeaderItem jQueryUiStyleItem = CssUrlReferenceHeaderItem
                .forUrl("/webjars/jquery-ui/1.12.1/jquery-ui.min.css");
        HeaderItem bootstrapStyleItem = CssUrlReferenceHeaderItem
                .forUrl("/webjars/bootstrap/4.0.0-2/css/bootstrap.min.css");
        CssHeaderItem iconicStyleItem = CssHeaderItem.forReference(iconicStyle);
        CssHeaderItem cmsStyleItem = CssHeaderItem.forReference(cmsStyle);
        HeaderItem jqueryScriptItem = JavaScriptHeaderItem.forReference(getApplication()
                .getJavaScriptLibrarySettings().getJQueryReference());
        HeaderItem jQueryUiScriptItem = JavaScriptUrlReferenceHeaderItem
                .forUrl("/webjars/jquery-ui/1.12.1/jquery-ui.min.js");
        HeaderItem popperScriptItem = JavaScriptUrlReferenceHeaderItem
                .forUrl("/webjars/popper.js/1.12.9-1/umd/popper.min.js");
        HeaderItem bootstrapScriptItem = JavaScriptUrlReferenceHeaderItem
                .forUrl("/webjars/bootstrap/4.0.0-2/js/bootstrap.min.js");
        JavaScriptHeaderItem cmsScriptItem = JavaScriptHeaderItem.forReference(cmsScript);

        StringHeaderItem viewportItem = new StringHeaderItem("<meta name=\"viewport\" content=\"width=device-width, " +
                "initial-scale=1, shrink-to-fit=no\">\n");
        StringHeaderItem faviconItem = new StringHeaderItem("<link rel=\"icon\" type=\"image/png\" " +
                "href=\"/favicon.png\" sizes=\"32x32\"/>\n");

        response.render(jQueryUiStyleItem);
        response.render(bootstrapStyleItem);
        response.render(iconicStyleItem);
        response.render(cmsStyleItem);
        response.render(jqueryScriptItem);
        response.render(jQueryUiScriptItem);
        response.render(popperScriptItem);
        response.render(bootstrapScriptItem);
        response.render(cmsScriptItem);
        response.render(viewportItem);
        response.render(faviconItem);
    }
}
