package cz.softinel.uaf.spring.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import cz.softinel.retra.core.utils.TypeFormats;
import cz.softinel.uaf.filter.Filter;
import cz.softinel.uaf.filter.FilterHelper;

/**
 * Common dispatch controller is main controller and it is parent of all
 * controllers in application.
 *
 * @version $Revision: 1.13 $ $Date: 2007-02-23 12:16:27 $
 * @author Radek Pinc, Petr Sígl
 */
public abstract class CommonDispatchController extends MultiActionController {

	/** Method name - defined default action method */
	private String method = "defaultAction";

	// Commonly used view names ...

	/** Expected right result of controller action */
	private String successView;

	/** Return to input view - some validation error? */
	private String inputView;

	/** Expected error - for example: return to input view with error message */
	private String errorView;

	/** Unexpected fatal error - typically general error screen */
	private String failureView;

	// Helper classes ...

	private class PrivateMethodNameResolver implements MethodNameResolver {
		public String getHandlerMethodName(HttpServletRequest arg0) throws NoSuchRequestHandlingMethodException {
			return method;
		}
	}

	// Constructors ...

	public CommonDispatchController() {
		setMethodNameResolver(new PrivateMethodNameResolver());
	}

	// Configuration setter methods ...

	public String getErrorView() {
		return errorView;
	}

	public void setErrorView(String errorView) {
		this.errorView = errorView;
	}

	public String getFailureView() {
		return failureView;
	}

	public void setFailureView(String failureView) {
		this.failureView = failureView;
	}

	public String getInputView() {
		return inputView;
	}

	public void setInputView(String inputView) {
		this.inputView = inputView;
	}

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Filter getFilter(Model model) {
		return null;
	}

	// Implementation ...

	public ModelAndView defaultAction(HttpServletRequest request, HttpServletResponse response) {
		return createModelAndView(getSuccessView());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Class thisClass = this.getClass();
		String methodName = getMethod();
		Method method = null;
		// type formats for using in jsp
		request.setAttribute(TypeFormats.ATTRIBUTE_NAME_TYPE_FORMATS, TypeFormats.getInstance());
		try {
			method = thisClass.getMethod(methodName, new Class[] { Model.class, RequestContext.class });
		} catch (NoSuchMethodException e) {
			// It is ok - expected method with parameters (HttpRequest, HttpResponse)
		}

		Model model = new Model();
		Filter filter = getFilter(model);

		if (method != null) {
			RequestContext requestContext = createRequestContext(request, response);
			// fill filter from request parameters or session if saved, if want clean than
			// clear it
			FilterHelper.cleanOrFillFilter(filter, request);
			try {
				ModelAndView result = (ModelAndView) method.invoke(this, new Object[] { model, requestContext });
				// set attributes according to filter
				FilterHelper.saveFilter(filter, request);
				return result;
			} catch (InvocationTargetException e) {
				if (e.getCause() instanceof Exception) {
					throw (Exception) e.getCause();
				} else {
					throw e;
				}
			}
		} else {
			// fill filter from request parameters or session if saved, if want clean than
			// clear it
			FilterHelper.cleanOrFillFilter(filter, request);
			ModelAndView result = super.handleRequestInternal(request, response);
			// set attributes according to filter
			FilterHelper.saveFilter(filter, request);
			return result;
		}
	}

	/**
	 * Creates new request context. Override this method to create a custom request
	 * context. This method creates {@link HttpRequestContext}.
	 * 
	 * @param request  current HTTP request
	 * @param response current HTTP response
	 * @return new request context
	 */
	protected RequestContext createRequestContext(HttpServletRequest request, HttpServletResponse response) {
		return new HttpRequestContext(request, response);
	}

	public ModelAndView createModelAndView(String view) {
		return new ModelAndView(view);
	}

	public ModelAndView createModelAndView(Model model, String view) {
		return new ModelAndView(view, model.getMap());
	}

	protected void prepareNext(Model model, Filter filter) {
		String parameters = prepareParameters(filter);
		model.put("parametersNext", parameters);
	}

	protected void preparePrevious(Model model, Filter filter) {
		String parameters = prepareParameters(filter);
		model.put("parametersPrevious", parameters);
	}

	protected String prepareParameters(Filter filter) {
		StringBuffer sb = new StringBuffer();
		for (Iterator it = filter.getAllFields().iterator(); it.hasNext();) {
			String fieldName = (String) it.next();
			sb.append(fieldName);
			sb.append("=");
			sb.append(filter.getFieldValue(fieldName));
			if (it.hasNext()) {
				sb.append("&amp;");
			}
		}
		return sb.toString();
	}
}
