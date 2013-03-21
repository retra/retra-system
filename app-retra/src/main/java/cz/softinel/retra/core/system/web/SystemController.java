/*
 * Created on 7.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.retra.core.system.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.Cookie;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.core.joke.blo.JokeGenerator;
import cz.softinel.retra.core.utils.helper.BooleanHelper;
import cz.softinel.retra.spring.web.DispatchController;
import cz.softinel.retra.spring.web.MiraController;
import cz.softinel.retra.vc.VisualConfigurationForm;
import cz.softinel.sis.login.Login;
import cz.softinel.sis.security.blo.SecurityLogic;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.news.SystemNewsFactory;
import cz.softinel.uaf.spring.web.controller.AbstractCookieHelper;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;
import cz.softinel.uaf.vc.skin.Skin;

/**
 * @author Radek Pinc
 *
 */
public class SystemController extends DispatchController {

	private AbstractCookieHelper cookieHelper;
	private JokeGenerator jokeGenerator;
	private SystemNewsFactory systemNewsFactory;

	// Configuration methods ...
	
	public void setCookieHelper(AbstractCookieHelper cookieHelper) {
		this.cookieHelper = cookieHelper;
	}

	// Action methods ...

	public ModelAndView showNewsboard(Model model, RequestContext requestContext) {
		List news = systemNewsFactory.getSystemNews();
		model.put("news", news);
		model.put("miraJokes", jokeGenerator.getJokesForDate(new Date()));
		return createModelAndView(model, getSuccessView());
	}	

	public ModelAndView showDashboard(Model model, RequestContext requestContext) {
		return createModelAndView(model, getSuccessView());
	}

	public ModelAndView showLogin(Model model, RequestContext requestContext) {
		return createModelAndView(model, getSuccessView());
	}

	public ModelAndView showError(Model model, RequestContext requestContext) {
		String errId = requestContext.getParameter("errId");
		model.set("errId", errId);
		return createModelAndView(model, getSuccessView());
	}
	
	public ModelAndView doLogin(Model model, RequestContext requestContext)	{
		// Prepare parameters ...
		String loginName = requestContext.getParameter("loginName");
		String password = requestContext.getParameter("password");
		String originalUrl = requestContext.getParameter("originalUrl");
		// Call business logic ...
		SecurityLogic securityLogic = getSecurityLogic();
		securityLogic.login(loginName, password);
		// Check result ...
		if (securityLogic.isUserLoggedIn()) {
			requestContext.addRedirectIgnoreInfo(new Message("succesfully.logged.in"));
			if (requestContext.getParameter("permanentPassword") != null) {
				Login login = securityLogic.getLoggedUser().getLogin();
				securityLogic.createPermanentPassword(login);
				cookieHelper.createAndAddCookie(AbstractCookieHelper.RETRA_PERMANENT_COOKIE_NAME, login.getPermanentPassword(), requestContext);
			}
			if (originalUrl != null && originalUrl.trim().length() > 0) {
				return createModelAndView(model, "redirect:"+originalUrl);
			} else {
				return createModelAndView(model, getSuccessView());
			}
		} else {
			model.set("loginName", loginName);
			model.set("password", password);
			try {
				model.set("originalUrl", URLEncoder.encode(originalUrl, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				model.set("originalUrl", "");
			}
			requestContext.addError(new Message("bad.username.or.password"));
			return createModelAndView(model, getErrorView());
		}
	}

	public ModelAndView showAbout(Model model, RequestContext requestContext)	{
		return createModelAndView(model, getSuccessView());
	}

	public void setJokeGenerator(JokeGenerator jokeGenerator) {
		this.jokeGenerator = jokeGenerator;
	}

	public void setSystemNewsFactory(SystemNewsFactory systemNewsFactory) {
		this.systemNewsFactory = systemNewsFactory;
	}

	/**
	 * Logout user from application.
	 * 
	 * @param model
	 * @param requestContext
	 * @return
	 */
	public ModelAndView doLogout(Model model, RequestContext requestContext) {
		
		// Process business logic ...
		getSecurityLogic().logout();
		
		//hack to show message -> succesfully logged out
		requestContext.addRedirectIgnoreInfo(new Message("succesfully.logged.out"));

		return createModelAndView(getSuccessView());
	}

	public ModelAndView setShowHistoryData(Model model, RequestContext requestContext) {
		String showHistoryDataStr = requestContext.getParameter(MiraController.PARAM_NAME_SHOW_HISTORY_DATA);
		String redirectUrl =  requestContext.getParameter(MiraController.PARAM_NAME_AFTER_SET_ACTION);
		if (StringUtils.hasText(showHistoryDataStr)) {
			Boolean showHistoryData = Boolean.valueOf(BooleanHelper.isChecked(showHistoryDataStr));
			setShowHistoryData(showHistoryData, requestContext);
		}
		else {
			setShowHistoryData(false, requestContext);
		}
		
		if (StringUtils.hasText(redirectUrl)) {
			String view = "redirect:" + redirectUrl;
			return createModelAndView(model, view);
		}
		return createModelAndView(model, getSuccessView());
	}

	public static String SKIN_KEY = "skinkey";
	
	public ModelAndView changeVisualConfiguration(Model model, RequestContext requestContext) {
		VisualConfigurationForm form = new VisualConfigurationForm();
		form.setSkinName(resolveSkin(requestContext));
		model.set("skins", getAllSkins());
		model.set("timeSelectors", getAllTimeSelectors());
		model.set("visualConfigurationForm", form);
		return createModelAndView(model, getSuccessView());
	}
	
	public ModelAndView changeVisualConfigurationStore(Model model, RequestContext requestContext) {
		VisualConfigurationForm form = new VisualConfigurationForm();
		form.setSkinName(requestContext.getParameter("skinName"));
		form.setTimeSelectorImplementation(requestContext.getParameter("timeSelectorImplementation"));
		setSkin(requestContext, form);
		return changeVisualConfiguration(model, requestContext);
	}
	
	private void setSkin(RequestContext requestContext, VisualConfigurationForm form) {
		// TODO radek: this is only fake implementation
		String skinName = form.getSkinName();
		requestContext.getSessionContext().setAttribute(SKIN_KEY, skinName);
		cookieHelper.createAndAddCookie(SKIN_KEY, skinName, requestContext);
	}
	
	private String resolveSkin(RequestContext requestContext) {
		// TODO radek: this is only fake implementation
		String skin = (String) requestContext.getSessionContext().getAttribute(SKIN_KEY);
		if (skin == null) {
			Cookie[] cookies = requestContext.getCookies();
			for (Cookie cookie : cookies) {
				if (SKIN_KEY.equals(cookie.getName())) {
					skin = cookie.getValue();
				}
			}
		}
		return skin;
	}
	
	private List<Skin> getAllSkins() {
		// TODO radek: This is FAKE implementation ...
		List<Skin> list = new LinkedList<Skin>();
		list.add(new Skin("skin01", "Yellow Hell"));
		list.add(new Skin("skin01lr", "Yellow Hell - low res."));
		list.add(new Skin("skin01hr", "Yellow Hell - hi res."));

		list.add(new Skin("skin02", "Light Blue"));
		list.add(new Skin("skin02lr", "Light Blue - low res."));
		list.add(new Skin("skin02hr", "Light Blue - hi res."));

		list.add(new Skin("skin03", "HB Green"));
		list.add(new Skin("skin03lr", "HB Green - low res."));
		list.add(new Skin("skin03hr", "HB Green - hi res."));

		return list;
	}
	
	private List getAllTimeSelectors() {
		// TODO radek: This is FAKE implementation ...
		List list = new LinkedList();
		return list;
	}
	
	
}
