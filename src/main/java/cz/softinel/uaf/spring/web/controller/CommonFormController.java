package cz.softinel.uaf.spring.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import cz.softinel.retra.core.utils.TypeFormats;
import cz.softinel.uaf.messages.Message;

/**
 * Common form controller is main controller for forms and it is parent of all
 * controllers in application.
 * 
 * @version $Revision: 1.7 $ $Date: 2007-11-28 23:05:12 $
 * @author Petr Sígl
 */
public class CommonFormController extends SimpleFormController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	// button names
	public static final String BUTTON_SAVE = "save";
	public static final String BUTTON_CANCEL = "cancel";
	public static final String BUTTON_BACK = "back";
	public static final String BUTTON_SAVE_AND_ADD = "saveAndAdd";

	// action names
	public static final int ACTION_UNDEFINED = 0;
	public static final int ACTION_SAVE = 1;
	public static final int ACTION_CANCEL = 2;
	public static final int ACTION_BACK = 3;
	public static final int ACTION_SAVE_AND_ADD = 4;

	private String cancelView;
	private String saveAndAddView;
	private String backView;
	private String errorView;
	private AbstractCookieHelper cookieHelper;

	private int action;

	private Model model;
	private RequestContext requestContext;

	/**
	 * @return the cancelView
	 */
	public String getCancelView() {
		return cancelView;
	}

	/**
	 * @param cancelView the cancelView to set
	 */
	public void setCancelView(String cancelView) {
		this.cancelView = cancelView;
	}

	/**
	 * @return the saveAndAddView
	 */
	public String getSaveAndAddView() {
		return saveAndAddView;
	}

	/**
	 * @param saveAndAddView the saveAndAddView to set
	 */
	public void setSaveAndAddView(String saveAndAddView) {
		this.saveAndAddView = saveAndAddView;
	}

	/**
	 * @return the backView
	 */
	public String getBackView() {
		return backView;
	}

	/**
	 * @param backView the backView to set
	 */
	public void setBackView(String backView) {
		this.backView = backView;
	}

	/**
	 * @return the errorView
	 */
	public String getErrorView() {
		return errorView;
	}

	/**
	 * @param errorView the errorView to set
	 */
	public void setErrorView(String errorView) {
		this.errorView = errorView;
	}

	/**
	 * @return the abstractCookieHelper
	 */
	public AbstractCookieHelper getCookieHelper() {
		return cookieHelper;
	}

	/**
	 * @param abstractCookieHelper the abstractCookieHelper to set
	 */
	public void setCookieHelper(AbstractCookieHelper cookieHelper) {
		this.cookieHelper = cookieHelper;
	}

	/**
	 * Returns model and view.
	 * 
	 * @param model
	 * @param view
	 * 
	 * @return
	 * 
	 * @deprecated
	 */
	public ModelAndView createModelAndView(Map model, String view) {
		return new ModelAndView(view, model);
	}

	/**
	 * Returns model and view.
	 * 
	 * @param view
	 * @return
	 */
	public ModelAndView createModelAndView(String view) {
		return new ModelAndView(view);
	}

	/**
	 * Returns model and view.
	 * 
	 * @param model
	 * @param view
	 * 
	 * @return
	 */
	public ModelAndView createModelAndView(Model model, String view) {
		return new ModelAndView(view, model.getMap());
	}

	/**
	 * This method is override of srping method. It prepares request context and
	 * model. Than returns super method.
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 * 
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// prepare model and requestContext
		model = new Model();
		requestContext = createRequestContext(request, response);
		// type formats for using in jsp
		request.setAttribute(TypeFormats.ATTRIBUTE_NAME_TYPE_FORMATS, TypeFormats.getInstance());

		// call super method
		return super.handleRequestInternal(request, response);
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

	/**
	 * This is override of spring method. It checks which submit button was pressed
	 * and according to it set action. If button is cancel button, than call method
	 * do before cancel and returns cancel view (of course do not validation). If
	 * button was back button, than only returns back view. In other casses calls
	 * super.
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#processFormSubmission(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		String cancel = request.getParameter(BUTTON_CANCEL);
		String back = request.getParameter(BUTTON_BACK);
		String save = request.getParameter(BUTTON_SAVE);
		String saveAndAdd = request.getParameter(BUTTON_SAVE_AND_ADD);
		// if pressed button than do nothing and return cancel view
		if (cancel != null) {
			setAction(ACTION_CANCEL);
			// when needed to do something before cancel.
			doBeforeCancelAction(model, requestContext, command, errors);
			return new ModelAndView(getCancelView());
		} else if (back != null) {
			setAction(ACTION_BACK);
			return new ModelAndView(getBackView());
		} else if (save != null) {
			setAction(ACTION_SAVE);
		} else if (saveAndAdd != null) {
			setAction(ACTION_SAVE_AND_ADD);
		} else {
			setAction(ACTION_UNDEFINED);
		}

		// ready to call super method
		return super.processFormSubmission(request, response, command, errors);
	}

	/**
	 * This method is override of spring method. It just try to find method with
	 * parameters Model, RequestContext and Object and this method try to invoke. It
	 * is used to prepare data (entity - form) into edit form.
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#onBindOnNewForm(javax.servlet.http.HttpServletRequest,
	 *      java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onBindOnNewForm(HttpServletRequest request, Object command) throws Exception {
		// actual class
		Class thisClass = this.getClass();
		Method method = null;
		// try to find method with params Model, RequestContext and Object
		try {
			method = thisClass.getMethod("onBindOnNewForm",
					new Class[] { Model.class, RequestContext.class, Object.class });
		} catch (NoSuchMethodException e) {
			// method not found - never mind, call super
			logger.error("Couldn't find method for on bind new form.");
		}
		// method was found
		if (method != null) {
			// try to invoke found method
			try {
				method.invoke(this, new Object[] { model, requestContext, command });
				// after invoking found method, call super method
				super.onBindOnNewForm(request, command);
			} catch (InvocationTargetException e) {
				// something is bad
				if (e.getCause() instanceof Exception) {
					throw (Exception) e.getCause();
				} else {
					throw e;
				}
			}
		}
		// method was not foud -> call super
		else {
			super.onBindOnNewForm(request, command);
		}
	}

	/**
	 * This method is override of spring method. It try find method with parameters
	 * Model, RequestContext, BindException and Map. This method is invoked and than
	 * is run super method. It also binds spring errors into our messages. It is
	 * used for prepare data (as list into select boxes for example) which are used
	 * in form view.
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#showForm(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse,
	 *      org.springframework.validation.BindException, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors,
			Map controlModel) throws Exception {
		// actual class
		Class thisClass = this.getClass();
		Method method = null;
		// bind errors from spring to our messages
		putBindErrorsToMessages(errors, requestContext);
		// try to find method with params Model, RequestContext and BindException
		try {
			method = thisClass.getMethod("showForm",
					new Class[] { Model.class, RequestContext.class, BindException.class });
		} catch (NoSuchMethodException e) {
			// method not found - never mind, call super
			logger.error("Couldn't find method for show form.");
		}
		// method was found
		if (method != null) {
			// put everything from control model to our model
			if (controlModel != null) {
				model.putAll(controlModel);
			}
			// try to invoke found method
			try {
				method.invoke(this, new Object[] { model, requestContext, errors });
				// after found method call super method
				return super.showForm(request, response, errors, model.getMap());
			} catch (InvocationTargetException e) {
				// something is bad
				if (e.getCause() instanceof Exception) {
					throw (Exception) e.getCause();
				} else {
					throw e;
				}
			}
		}
		// method was not found -> call super
		else {
			return super.showForm(request, response, errors, controlModel);
		}
	}

	/**
	 * This method is override of spring method. It try to find method with
	 * parameters Model, RequestContext Object, BindException and invoke it. It also
	 * binds spring errors into our messages. This method is used to serve action
	 * after submiting form.
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors) throws Exception {
		// actual class
		Class thisClass = this.getClass();
		Method method = null;
		// bind errors from spring to our messages
		putBindErrorsToMessages(errors, requestContext);
		// try to find method with params Model, RequestContext, Object and
		// BindException
		try {
			method = thisClass.getMethod("onSubmit",
					new Class[] { Model.class, RequestContext.class, Object.class, BindException.class });
		} catch (NoSuchMethodException e) {
			// method not found - never mind, call super
			logger.error("Couldn't find method for on submit.");
		}
		// method was found
		if (method != null) {
			// try to invoke found method
			try {
				return (ModelAndView) method.invoke(this, new Object[] { model, requestContext, command, errors });
			} catch (InvocationTargetException e) {
				// something is bad
				if (e.getCause() instanceof Exception) {
					throw (Exception) e.getCause();
				} else {
					throw e;
				}
			}
		}
		// method was not found -> call super
		else {
			return super.onSubmit(request, response, command, errors);
		}
	}

	/**
	 * @return the action
	 */
	protected int getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	protected void setAction(int action) {
		this.action = action;
	}

	/**
	 * Overide this method in child when need to do some action before cancel.
	 * 
	 * @param model
	 * @param requestContext
	 * @param command
	 * @param errors
	 */
	protected void doBeforeCancelAction(Model model, RequestContext requestContext, Object command,
			BindException errors) {
		return;
	}

	/**
	 * Put errors from binding - spring into our messages on request.
	 * 
	 * @param errors
	 * @param context
	 */
	private void putBindErrorsToMessages(BindException errors, RequestContext context) {
		for (Iterator it = errors.getAllErrors().iterator(); it.hasNext();) {
			ObjectError error = (ObjectError) it.next();
			context.addError(new Message(error.getCode(), error.getArguments(), error.getDefaultMessage()));
		}
	}

	/**
	 * Overide this method in child when need to do some action before form view.
	 * 
	 * @param model
	 * @param requestContext
	 * @param errors
	 * @throws Exception
	 */
	public void showForm(Model model, RequestContext requestContext, BindException errors) throws Exception {
	}

}
