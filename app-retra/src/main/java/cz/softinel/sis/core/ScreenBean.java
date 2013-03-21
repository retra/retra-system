/*
 * Created on 2.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.sis.core;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import cz.softinel.retra.core.system.web.SystemController;

/**
 * @author Radek Pinc
 *
 */
public class ScreenBean implements Controller {

	private static final String DEFAULT_SKIN_NAME = "skin02";

	private static final String MAIN_MENU = "mainMenu";
	
	private static final String SUB_MENU = "subMenu";

	private String defaultSkin = DEFAULT_SKIN_NAME;
	private String primaryLinks = null;

	private String template;
	
	private final Map<String, String> parameters = new HashMap<String, String>();
		
	/**
	 * @return Returns the template.
	 */
	public String getTemplate() {
		return template;
	}
	
	/**
	 * @param template The template to set.
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * Sets the mainMenu.
	 * @param mainMenu The mainMenu to set.
	 */
	public void setMainMenu(String mainMenu) {
		Assert.hasText(mainMenu, "Main menu must be set.");
		parameters.put(MAIN_MENU, mainMenu);
	}
	
	/**
	 * Sets the subMenu.
	 * @param subMenu The subMenu to set.
	 */
	public void setSubMenu(String subMenu) {
		if (StringUtils.hasText(subMenu)) {
			parameters.put(SUB_MENU, subMenu);
		}
		else {
			parameters.remove(SUB_MENU);
		}
	}
	
	public String getDefaultSkin() {
		return defaultSkin;
	}

	public void setDefaultSkin(String defaultSkin) {
		this.defaultSkin = defaultSkin;
	}
	
	public String getPrimaryLinks() {
		return primaryLinks;
	}

	public void setPrimaryLinks(String primaryLinks) {
		this.primaryLinks = primaryLinks;
	}

	/**
	 * @return Returns the parameters.
	 */
	public Map getParameters() {
		return parameters;
	}
	
	/**
	 * @param parameters The parameters to set.
	 */
	public void setParameters(Map<String, String> params) {
		this.parameters.putAll(params);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response){
		request.setAttribute("skinName", resolveSkin(request));
		request.setAttribute("primaryLinks", getPrimaryLinks());
		return new ModelAndView(getTemplate(), getParameters());
	}
	
	private String resolveSkin(HttpServletRequest request) {
		// TODO radek: this is only fake implementation
		String skin = (String) request.getSession().getAttribute(SystemController.SKIN_KEY);
		if (skin == null) {
			// TODO radek: Create some common cookie helper
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (SystemController.SKIN_KEY.equals(cookie.getName())) {
						skin = cookie.getValue();
						request.getSession().setAttribute(SystemController.SKIN_KEY, skin);
						break;
					}
				}
			}
		}
		if (!StringUtils.hasLength(skin)) {
			skin = getDefaultSkin();
		}
		return skin;
	}

}
