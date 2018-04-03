package altlombardisch;

import altlombardisch.frontend.HomePage;
import altlombardisch.ui.page.PageExpiredPage;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.settings.ExceptionSettings;

/**
 * A web application without any authentication.
 */
public class FrontendApplication extends WebApplication {
    /**
     * Provides a custom initialization for this app.
     */
    @Override
    protected void init() {
        super.init();
        SecurePackageResourceGuard packageResourceGuard = (SecurePackageResourceGuard) getResourceSettings()
                .getPackageResourceGuard();

        packageResourceGuard.addPattern("+*.css.map");
        packageResourceGuard.addPattern("+*.dtd");
        packageResourceGuard.addPattern("+*.xsd");

        getApplicationSettings().setPageExpiredErrorPage(PageExpiredPage.class);

        if (WebApplication.get().getConfigurationType().equals(RuntimeConfigurationType.DEPLOYMENT)) {
            // don’t show an exception page when an unexpected exception is thrown
            getExceptionSettings().setUnexpectedExceptionDisplay(ExceptionSettings.SHOW_NO_EXCEPTION_PAGE);
        }

        if (getInitParameter("wicket.stripWicketTags").equals("true")) {
            getMarkupSettings().setStripWicketTags(true);
        }
    }

    /**
     * Creates a home page class for this application.
     *
     * @return A home page class.
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }
}
