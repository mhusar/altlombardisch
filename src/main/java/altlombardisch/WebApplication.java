package altlombardisch;

import altlombardisch.auth.SignInPage;
import altlombardisch.auth.WebSession;
import altlombardisch.siglum.SiglumEditPage;
import altlombardisch.siglum.SiglumIndexPage;
import altlombardisch.siglum.SiglumViewPage;
import altlombardisch.ui.page.AccessDeniedPage;
import altlombardisch.ui.page.PageExpiredPage;
import altlombardisch.user.User;
import altlombardisch.user.UserEditPage;
import altlombardisch.xml.attribute.XmlAttributeDefinitionEditPage;
import altlombardisch.xml.document.XmlDocumentDefinitionDao;
import altlombardisch.xml.document.XmlDocumentDefinitionEditPage;
import altlombardisch.xml.tag.XmlTagDefinitionEditPage;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AnnotationsRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.settings.ExceptionSettings;

/**
 * A web application that does role-based authentication.
 */
public class WebApplication extends AuthenticatedWebApplication {
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
        getSecuritySettings().setAuthorizationStrategy(
                new AnnotationsRoleAuthorizationStrategy(this));
        getSecuritySettings().setUnauthorizedComponentInstantiationListener(
                new IUnauthorizedComponentInstantiationListener() {
                    @Override
                    public void onUnauthorizedInstantiation(Component component) {
                        if (component instanceof Page) {
                            if (WebSession.get().getUser() instanceof User) {
                                throw new UnauthorizedInstantiationException(AccessDeniedPage.class);
                            } else {
                                if (component instanceof SiglumEditPage
                                        || component instanceof SiglumViewPage
                                        || component instanceof XmlAttributeDefinitionEditPage
                                        || component instanceof XmlTagDefinitionEditPage) {
                                    component.setResponsePage(SignInPage.class);
                                } else {
                                    throw new RestartResponseAtInterceptPageException(SignInPage.class);
                                }
                            }
                        }
                    }
                });
        getApplicationSettings().setAccessDeniedPage(AccessDeniedPage.class);
        getApplicationSettings().setPageExpiredErrorPage(PageExpiredPage.class);

        if (AuthenticatedWebApplication.get().getConfigurationType().equals(RuntimeConfigurationType.DEPLOYMENT)) {
            // don’t show an exception page when an unexpected exception is thrown
            getExceptionSettings().setUnexpectedExceptionDisplay(ExceptionSettings.SHOW_NO_EXCEPTION_PAGE);
        }

        if (getInitParameter("wicket.stripWicketTags").equals("true")) {
            getMarkupSettings().setStripWicketTags(true);
        }

        mountPage("/AccessDeniedPage", AccessDeniedPage.class);
        mountPage("/PageExpiredPage", PageExpiredPage.class);
        mountPage("/SignInPage", SignInPage.class);
        mountPage("/siglum/SiglumIndexPage", SiglumIndexPage.class);
        mountPage("/siglum/SiglumEditPage", SiglumEditPage.class);
        mountPage("/user/UserEditPage", UserEditPage.class);
        mountPage("/xml/XmlDocumentDefinitionEditPage", XmlDocumentDefinitionEditPage.class);
        mountPage("/xml/XmlTagDefinitionEditPage", XmlTagDefinitionEditPage.class);
        mountPage("/xml/XmlAttributeDefinitionEditPage", XmlAttributeDefinitionEditPage.class);

        // create needed XML document definitions
        new XmlDocumentDefinitionDao().initialize();
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

    /**
     * Creates a sign-in page class for this application.
     *
     * @return A sign-in page class.
     */
    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return SignInPage.class;
    }

    /**
     * Creates a web session class for this application.
     *
     * @return A web session class.
     */
    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return WebSession.class;
    }
}
