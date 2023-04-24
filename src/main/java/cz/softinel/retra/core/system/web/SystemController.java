/*
 * Created on 7.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.retra.core.system.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.activity.Activity;
import cz.softinel.retra.activity.blo.ActivityLogic;
import cz.softinel.retra.core.joke.blo.JokeGenerator;
import cz.softinel.retra.core.utils.helper.BooleanHelper;
import cz.softinel.retra.project.Project;
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

	private ActivityLogic activityLogic;

	// Configuration methods ...

	public void setCookieHelper(AbstractCookieHelper cookieHelper) {
		this.cookieHelper = cookieHelper;
	}

	public ActivityLogic getActivityLogic() {
		return activityLogic;
	}

	public void setActivityLogic(ActivityLogic activityLogic) {
		this.activityLogic = activityLogic;
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

	public ModelAndView showSettingsDashboard(Model model, RequestContext requestContext) {
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

	public ModelAndView showNoPermission(Model model, RequestContext requestContext) {
		return createModelAndView(model, getSuccessView());
	}

	public ModelAndView doLogin(Model model, RequestContext requestContext) {
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
				cookieHelper.createAndAddCookie(AbstractCookieHelper.RETRA_PERMANENT_COOKIE_NAME,
						login.getPermanentPassword(), requestContext);
			}
			if (originalUrl != null && originalUrl.trim().length() > 0) {
				return createModelAndView(model, "redirect:" + originalUrl);
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

	public ModelAndView showAbout(Model model, RequestContext requestContext) {
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

		// hack to show message -> succesfully logged out
		requestContext.addRedirectIgnoreInfo(new Message("succesfully.logged.out"));

		return createModelAndView(getSuccessView());
	}

	/**
	 * Logout user from application.
	 * 
	 * @param model
	 * @param requestContext
	 * @return
	 */
	public ModelAndView reloadLoggedEmployee(Model model, RequestContext requestContext) {
		//reload employee
		getSecurityLogic().reloadLoggedEmployee();
		requestContext.addInfo(new Message("succesfully.reloaded.employee"));

		return createModelAndView(getSuccessView());
	}
	
	public ModelAndView setShowHistoryData(Model model, RequestContext requestContext) {
		String showHistoryDataStr = requestContext.getParameter(MiraController.PARAM_NAME_SHOW_HISTORY_DATA);
		String redirectUrl = requestContext.getParameter(MiraController.PARAM_NAME_AFTER_SET_ACTION);
		if (StringUtils.hasText(showHistoryDataStr)) {
			Boolean showHistoryData = Boolean.valueOf(BooleanHelper.isChecked(showHistoryDataStr));
			setShowHistoryData(showHistoryData, requestContext);
		} else {
			setShowHistoryData(false, requestContext);
		}

		if (StringUtils.hasText(redirectUrl)) {
			String view = "redirect:" + redirectUrl;
			return createModelAndView(model, view);
		}
		return createModelAndView(model, getSuccessView());
	}

	public static String SKIN_KEY = "skinkey";

	public static String PROJECT_ACTIVITY_KEY = "retra.projectActivityRelation";

	public ModelAndView changeVisualConfiguration(Model model, RequestContext requestContext) {
		Object vcForm = model.get("filledVCForm");
		VisualConfigurationForm form = null;
		if (vcForm != null) {
			form = (VisualConfigurationForm) vcForm;
		} else {
			form = new VisualConfigurationForm();
			form.setSkinName(resolveSkin(requestContext));
			loadProjectActivityRelation(requestContext, form);
		}
		model.set("skins", getAllSkins());
		model.set("timeSelectors", getAllTimeSelectors());
		model.set("visualConfigurationForm", form);
		prepareActivities(model);
		prepareProjects(model);
		return createModelAndView(model, getSuccessView());
	}

	public ModelAndView changeVisualConfigurationStore(Model model, RequestContext requestContext) {
		VisualConfigurationForm form = new VisualConfigurationForm();
		form.setSkinName(requestContext.getParameter("skinName"));
		form.setTimeSelectorImplementation(requestContext.getParameter("timeSelectorImplementation"));
		setSkin(requestContext, form);
		storeProjectActivityRelation(requestContext, form);
		model.set("filledVCForm", form);
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

	// TODO: fast horrible implementation
	private void storeProjectActivityRelation(RequestContext requestContext, VisualConfigurationForm form) {
		form.setDa(requestContext.getParameter("da"));
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "DA", form.getDa(), requestContext);

		form.setP0(requestContext.getParameter("p0"));
		form.setA0(requestContext.getParameter("a0"));
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "P0", form.getP0(), requestContext);
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "A0", form.getA0(), requestContext);

		form.setP1(requestContext.getParameter("p1"));
		form.setA1(requestContext.getParameter("a1"));
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "P1", form.getP1(), requestContext);
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "A1", form.getA1(), requestContext);

		form.setP2(requestContext.getParameter("p2"));
		form.setA2(requestContext.getParameter("a2"));
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "P2", form.getP2(), requestContext);
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "A2", form.getA2(), requestContext);

		form.setP3(requestContext.getParameter("p3"));
		form.setA3(requestContext.getParameter("a3"));
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "P3", form.getP3(), requestContext);
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "A3", form.getA3(), requestContext);

		form.setP4(requestContext.getParameter("p4"));
		form.setA4(requestContext.getParameter("a4"));
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "P4", form.getP4(), requestContext);
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "A4", form.getA4(), requestContext);

		form.setP5(requestContext.getParameter("p5"));
		form.setA5(requestContext.getParameter("a5"));
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "P5", form.getP5(), requestContext);
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "A5", form.getA5(), requestContext);

		form.setP6(requestContext.getParameter("p6"));
		form.setA6(requestContext.getParameter("a6"));
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "P6", form.getP6(), requestContext);
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "A6", form.getA6(), requestContext);

		form.setP7(requestContext.getParameter("p7"));
		form.setA7(requestContext.getParameter("a7"));
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "P7", form.getP7(), requestContext);
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "A7", form.getA7(), requestContext);

		form.setP8(requestContext.getParameter("p8"));
		form.setA8(requestContext.getParameter("a8"));
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "P8", form.getP8(), requestContext);
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "A8", form.getA8(), requestContext);

		form.setP9(requestContext.getParameter("p9"));
		form.setA9(requestContext.getParameter("a9"));
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "P9", form.getP9(), requestContext);
		cookieHelper.createAndAddCookie(PROJECT_ACTIVITY_KEY + "A9", form.getA9(), requestContext);
	}

	// TODO: fast horrible implementation
	private void loadProjectActivityRelation(RequestContext requestContext, VisualConfigurationForm form) {
		Cookie[] cookies = requestContext.getCookies();
		for (Cookie cookie : cookies) {
			if ((PROJECT_ACTIVITY_KEY + "DA").equals(cookie.getName())) {
				form.setDa(cookie.getValue());
			}

			if ((PROJECT_ACTIVITY_KEY + "P0").equals(cookie.getName())) {
				form.setP0(cookie.getValue());
			}
			if ((PROJECT_ACTIVITY_KEY + "A0").equals(cookie.getName())) {
				form.setA0(cookie.getValue());
			}

			if ((PROJECT_ACTIVITY_KEY + "P1").equals(cookie.getName())) {
				form.setP1(cookie.getValue());
			}
			if ((PROJECT_ACTIVITY_KEY + "A1").equals(cookie.getName())) {
				form.setA1(cookie.getValue());
			}

			if ((PROJECT_ACTIVITY_KEY + "P2").equals(cookie.getName())) {
				form.setP2(cookie.getValue());
			}
			if ((PROJECT_ACTIVITY_KEY + "A2").equals(cookie.getName())) {
				form.setA2(cookie.getValue());
			}

			if ((PROJECT_ACTIVITY_KEY + "P3").equals(cookie.getName())) {
				form.setP3(cookie.getValue());
			}
			if ((PROJECT_ACTIVITY_KEY + "A3").equals(cookie.getName())) {
				form.setA3(cookie.getValue());
			}

			if ((PROJECT_ACTIVITY_KEY + "P4").equals(cookie.getName())) {
				form.setP4(cookie.getValue());
			}
			if ((PROJECT_ACTIVITY_KEY + "A4").equals(cookie.getName())) {
				form.setA4(cookie.getValue());
			}

			if ((PROJECT_ACTIVITY_KEY + "P5").equals(cookie.getName())) {
				form.setP5(cookie.getValue());
			}
			if ((PROJECT_ACTIVITY_KEY + "A5").equals(cookie.getName())) {
				form.setA5(cookie.getValue());
			}

			if ((PROJECT_ACTIVITY_KEY + "P6").equals(cookie.getName())) {
				form.setP6(cookie.getValue());
			}
			if ((PROJECT_ACTIVITY_KEY + "A6").equals(cookie.getName())) {
				form.setA6(cookie.getValue());
			}

			if ((PROJECT_ACTIVITY_KEY + "P7").equals(cookie.getName())) {
				form.setP7(cookie.getValue());
			}
			if ((PROJECT_ACTIVITY_KEY + "A7").equals(cookie.getName())) {
				form.setA7(cookie.getValue());
			}

			if ((PROJECT_ACTIVITY_KEY + "P8").equals(cookie.getName())) {
				form.setP8(cookie.getValue());
			}
			if ((PROJECT_ACTIVITY_KEY + "A8").equals(cookie.getName())) {
				form.setA8(cookie.getValue());
			}

			if ((PROJECT_ACTIVITY_KEY + "P9").equals(cookie.getName())) {
				form.setP9(cookie.getValue());
			}
			if ((PROJECT_ACTIVITY_KEY + "A9").equals(cookie.getName())) {
				form.setA9(cookie.getValue());
			}
		}

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

		list.add(new Skin("skin04", "Rusty Fox"));

		return list;
	}

	private List getAllTimeSelectors() {
		// TODO radek: This is FAKE implementation ...
		List list = new LinkedList();
		return list;
	}

	private void prepareActivities(Model model) {
		prepareActivities(model, false);
	}

	private void prepareActivities(Model model, boolean showAll) {
		List<Activity> activities = null;
		if (showAll) {
			activities = activityLogic.findAllActivities();
		} else {
			activities = activityLogic.findAllNotDeletedActivities();
		}

		model.put("activities", activities);
	}

	private void prepareProjects(Model model) {
		prepareProjects(model, false);
	}

	private void prepareProjects(Model model, boolean showAll) {
		List<Project> projects = null;
		if (showAll) {
			// projects = projectLogic.findAllProjects();
			projects = filterActiveProject(getSecurityLogic().getLoggedEmployee().getProjects());
		} else {
			// projects = projectLogic.findAllNotDeletedProjects();
			projects = filterActiveProject(getSecurityLogic().getLoggedEmployee().getProjects());
		}

		model.put("projects", projects);
	}

	private List<Project> filterActiveProject(Set<Project> projects) {
		List<Project> filtersList = new ArrayList<Project>();
		if (projects != null && !projects.isEmpty()) {
			for (Project p : projects) {
				if (p.getState() == Project.STATE_ACTIVE) {
					filtersList.add(p);
				}
			}
		}
		return filtersList;
	}

}
