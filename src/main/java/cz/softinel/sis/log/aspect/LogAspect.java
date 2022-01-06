package cz.softinel.sis.log.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public abstract class LogAspect {

	private Logger logger = LoggerFactory.getLogger(LogAspect.class);

	private static final String ASTERISKS = "*****";
	private static final String DO_NOT_LOG_ARGS_ANNOTATION = "cz.softinel.sis.log.aspect.DoNotLogArgs";
	private static final String ARG_INDEXES_NAME = "argIndexes";

	/**
	 * Pointcut for calling controller methods
	 */
	@Pointcut
	public abstract void controllerMethods();

	/**
	 * Pointcut for calling logic methods - resolution.
	 */
	@Pointcut
	public abstract void logicMethods();

	/**
	 * Pointcut for calling dao methods - resolution.
	 */
	@Pointcut
	public abstract void daoMethods();

	/**
	 * Before controller method call.
	 * 
	 * @param jp
	 */
	@Before(value = "controllerMethods()")
	public void beforeController(final JoinPoint jp) {
		logBeforeMethod(jp);
	}

	/**
	 * Before logic method call.
	 * 
	 * @param jp
	 */
	@Before(value = "logicMethods()")
	public void beforeLogic(final JoinPoint jp) {
		logBeforeMethod(jp);
	}

	/**
	 * Before dao method call.
	 * 
	 * @param jp
	 */
	@Before(value = "daoMethods()")
	public void beforeDao(final JoinPoint jp) {
		logBeforeMethod(jp);
	}

	/**
	 * After controller method call.
	 * 
	 * @param jp
	 */
	@AfterReturning(value = "controllerMethods()", returning = "retVal")
	public void afterController(final JoinPoint jp, final Object retVal) {
		logAfterMethod(jp, retVal);
	}

	/**
	 * After logic method call.
	 * 
	 * @param jp
	 */
	@AfterReturning(value = "logicMethods()", returning = "retVal")
	public void afterLogic(final JoinPoint jp, final Object retVal) {
		logAfterMethod(jp, retVal);
	}

	/**
	 * After dao method call.
	 * 
	 * @param jp
	 */
	@AfterReturning(value = "daoMethods()", returning = "retVal")
	public void afterDao(final JoinPoint jp, final Object retVal) {
		logAfterMethod(jp, retVal);
	}
	
	/**
	 * Implementation of logging before.
	 * 
	 * @param jp
	 */
	private void logBeforeMethod(final JoinPoint jp) {
		StringBuilder sb = new StringBuilder();

		Object target = jp.getTarget();
		if (target == null) {
			logger.warn("No target class, no retra log :-).");
			return;
		}
		Class<?> targetClass = target.getClass();

		Signature signature = jp.getSignature();
		if (signature == null) {
			logger.warn("No signature, no retra log :-).");
			return;
		}
		String methodName = signature.getName();
		Set<Integer> asteriskArgsIndexes = getAsteriskArgsIndexes(signature);
		
		StringBuilder methodArgs = new StringBuilder();
		boolean first = true;
		Object[] jpArgs = jp.getArgs();
		for (int i = 0; i < jpArgs.length; i++) {
			if (!first) {
				methodArgs.append(", ");
			} else {
				first = false;
			}

			Object arg = jpArgs[i];
			methodArgs.append("\"");
			if (arg != null) {
				if (asteriskArgsIndexes != null
					&& !asteriskArgsIndexes.isEmpty()
					&& asteriskArgsIndexes.contains(Integer.valueOf(i))) {
					methodArgs.append(ASTERISKS);
				} else {
					methodArgs.append(arg.toString());
				}
			} else {
				methodArgs.append("null");
			}
			methodArgs.append("\"");
		}

		sb.append(targetClass.getName());
		sb.append(".");
		sb.append(methodName);
		sb.append("(), ARGS=[");
		sb.append(methodArgs.toString());
		sb.append("] +++ START +++");

		logger.debug(sb.toString());
	}

	/**
	 * Implementation of logging after.
	 * 
	 * @param jp
	 */
	private void logAfterMethod(final JoinPoint jp, final Object retVal) {
		StringBuilder sb = new StringBuilder();

		Object target = jp.getTarget();
		if (target == null) {
			logger.warn("No target class, no retra log :-).");
			return;
		}
		Class<?> targetClass = target.getClass();

		Signature signature = jp.getSignature();
		if (signature == null) {
			logger.warn("No signature, no retra log :-).");
			return;
		}
		String methodName = signature.getName();

		sb.append(targetClass.getName());
		sb.append(".");
		sb.append(methodName);
		sb.append("(), RETURN=[");
		sb.append(retVal);
		sb.append("] --- END ---");

		logger.debug(sb.toString());
	}

	/**
	 * Check method annotation and if there is annotation DoNotLogArgs returns array with args indexes, which will be replaced with * in log.
	 * 
	 * @param signature
	 * @return
	 */
	private Set<Integer> getAsteriskArgsIndexes(final Signature sig) {
		Set<Integer> result = new HashSet<>(); 
		if (sig instanceof MethodSignature) {
			MethodSignature signature = (MethodSignature) sig;
		    Method method = signature.getMethod();
		    
			//get annotations for given method
			Annotation[] annotations = method.getAnnotations();
			if (annotations != null && annotations.length > 0) {
				//must iterate over all annotations
				for (Annotation a : annotations) {
					String annotationName = a.annotationType().getName();

					//has do not log args annotation
					if (DO_NOT_LOG_ARGS_ANNOTATION.equals(annotationName)) {
						int[] argIndexes = readArgIndexes(a);
						if (argIndexes != null && argIndexes.length > 0) {
							for (int value : argIndexes) {
								result.add(value);
							}
						}
						break;
					}
				}
			}

		}
		
		return result;
	}

	/**
	 * Read propagation value of annotation.
	 * 
	 * @param annotation
	 * @return
	 */
	private int[] readArgIndexes(final Annotation annotation) {
		int[] result = null;
		try {
			Method argIndexesMethod = annotation.annotationType().getMethod(ARG_INDEXES_NAME, (Class[]) null);
			result = (int[]) argIndexesMethod.invoke(annotation, (Object[]) null);
		} catch (Exception e) {
			logger.warn("Unable to read indexes of arguments, which shouldn't be write into log. It is strange (ex. log password???). !!! CHECK IT !!!");
		}

		return result;
	}

}
