package cz.softinel.retra.core.system.web;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.security.keycloak.KeycloakConfig;
import cz.softinel.retra.security.keycloak.KeycloakLoginResult;
import cz.softinel.retra.security.keycloak.KeycloakLoginStorage;
import cz.softinel.retra.security.keycloak.KeycloakLoginTx;
import cz.softinel.retra.security.keycloak.KeycloakOidcService;
import cz.softinel.retra.spring.web.DispatchController;
import cz.softinel.sis.login.Login;
import cz.softinel.sis.login.blo.LoginLogic;
import cz.softinel.sis.security.SecurityContext;

/**
 * Login via Keycloak (OIDC, Authorization Code flow + PKCE).
 * 
 * The <code>preferred_username</code> from Keycloak is mapped to
 * <code>sis11login.sis11ldaplogin</code> (Login.ldapLogin); roles and
 * permissions stay managed in Retra. See doc/spec/HBR-5.md.
 * 
 * Methods use the (HttpServletRequest, HttpServletResponse) signature -
 * CommonDispatchController first looks for (Model, RequestContext) and falls
 * back to MultiActionController which supports this signature.
 */
public class KeycloakController extends DispatchController {

	private static final Logger log = LoggerFactory.getLogger(KeycloakController.class);

	/** Session attribute with the URL to redirect to after a successful login. */
	private static final String KEYCLOAK_ORIGINAL_URL_KEY = "keycloakOriginalUrl";

	/** Session attribute key used by WebSecurityLogicImpl for the security context. */
	private static final String SECURITY_CONTEXT_KEY = "securityContext";

	private KeycloakConfig keycloakConfig;
	private KeycloakOidcService keycloakOidcService;
	private KeycloakLoginStorage keycloakLoginStorage;
	private LoginLogic loginLogic;

	// Configuration methods ...

	public void setKeycloakConfig(KeycloakConfig keycloakConfig) {
		this.keycloakConfig = keycloakConfig;
	}

	public void setKeycloakOidcService(KeycloakOidcService keycloakOidcService) {
		this.keycloakOidcService = keycloakOidcService;
	}

	public void setKeycloakLoginStorage(KeycloakLoginStorage keycloakLoginStorage) {
		this.keycloakLoginStorage = keycloakLoginStorage;
	}

	public void setLoginLogic(LoginLogic loginLogic) {
		this.loginLogic = loginLogic;
	}

	// Action methods ...

	/**
	 * Starts the Keycloak login: creates a login transaction and redirects the
	 * user agent to the Keycloak authorization endpoint.
	 */
	public ModelAndView keycloakLogin(HttpServletRequest request, HttpServletResponse response) {
		if (!keycloakConfig.isEnabled()) {
			return new ModelAndView("redirect:Login.do?fkprm=true");
		}
		// callback URL with placeholder - the service fills in the {loginTxId}
		StringBuffer url = request.getRequestURL();
		int servletPathIndex = url.indexOf(request.getServletPath());
		String base = servletPathIndex >= 0 ? url.substring(0, servletPathIndex) : url.toString();
		String callbackUrl = base + "/KeycloakCallback.do?loginTxId=" + KeycloakOidcService.LOGIN_TX_ID_PLACEHOLDER;
		KeycloakLoginTx tx = keycloakOidcService.createLoginTx(callbackUrl);
		keycloakLoginStorage.put(tx);
		// remember originalUrl for the redirect after login
		String originalUrl = request.getParameter("originalUrl");
		if (originalUrl != null && originalUrl.length() > 0) {
			request.getSession().setAttribute(KEYCLOAK_ORIGINAL_URL_KEY, originalUrl);
		}
		URI authRequest = keycloakOidcService.buildAuthorizationRequest(tx);
		return new ModelAndView("redirect:" + authRequest.toString());
	}

	/**
	 * Callback from Keycloak: validates the response, exchanges the code for
	 * tokens, maps the Keycloak username to a Retra login and logs the user in.
	 */
	public ModelAndView keycloakCallback(HttpServletRequest request, HttpServletResponse response) {
		String loginTxId = request.getParameter("loginTxId");
		KeycloakLoginTx tx = keycloakLoginStorage.get(loginTxId);
		if (tx == null) {
			return new ModelAndView("redirect:Login.do?fkprm=true&keycloakError=tx");
		}
		String queryString = request.getQueryString();
		String responseUri = request.getRequestURL() + (queryString != null ? "?" + queryString : "");
		KeycloakLoginResult result = keycloakOidcService.handleAuthorizationResponse(tx, responseUri);
		// the transaction is single-use
		keycloakLoginStorage.remove(loginTxId);

		if (result.isOk()) {
			String username = result.getUsername(); // preferred_username from Keycloak
			Login login = loginLogic.checkLoginByLdapLogin(username); // mapped to sis11ldaplogin
			if (login != null) {
				// session fixation - take the context, invalidate the old session and
				// create a new one with the security context in it
				SecurityContext securityContext = getSecurityLogic().getSecurityContext();
				getSecurityLogic().login(login.getUser());
				HttpSession oldSession = request.getSession(false);
				String originalUrl = oldSession == null ? null
						: (String) oldSession.getAttribute(KEYCLOAK_ORIGINAL_URL_KEY);
				if (oldSession != null) {
					oldSession.invalidate();
				}
				HttpSession newSession = request.getSession(true);
				newSession.setAttribute(SECURITY_CONTEXT_KEY, securityContext);
				// redirect
				if (originalUrl != null && originalUrl.length() > 0) {
					return new ModelAndView("redirect:" + originalUrl);
				}
				return new ModelAndView("redirect:Newsboard.do?fkprm=true");
			}
			return new ModelAndView("redirect:Login.do?fkprm=true&keycloakError=unknownUser");
		}
		log.warn("Keycloak callback error: {} / {} - {}", result.getErrorStep(), result.getErrorCode(),
				result.getErrorMessage());
		return new ModelAndView("redirect:Login.do?fkprm=true&keycloakError=" + result.getErrorStep());
	}

}
