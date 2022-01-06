package cz.softinel.retra.core.system.web;

import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.spring.web.DispatchController;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

/**
 * For generating helps
 *
 * @version $Revision: 1.3 $ $Date: 2007-12-03 18:42:29 $
 * @author Petr Sígl
 */
public class HelpController extends DispatchController {

	public ModelAndView helpPrint(Model model, RequestContext requestContext) {
		return createModelAndView(model, getSuccessView());
	}

	public ModelAndView helpCopySchedule(Model model, RequestContext requestContext) {
		return createModelAndView(model, getSuccessView());
	}

	public ModelAndView helpImportWorklog(Model model, RequestContext requestContext) {
		return createModelAndView(model, getSuccessView());
	}
}
