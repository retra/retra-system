/*
 * Created on 8.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.sis.login;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

import org.springframework.util.StringUtils;

import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.sis.login.web.LoginForm;
import cz.softinel.uaf.util.EncryptionHelper;

/**
 * @author Radek Pinc
 *
 */
public class LoginHelper {

	public static final int STATE_NEW = 0;
	public static final int STATE_ACTIVE = 1;
	public static final int STATE_BLOCKED = 2;
	public static final int STATE_DLETED = 3;

	public static final int MIN_PASSWORD_LENGTH = 5;
	public static final String STRING_WITH_DIGIT_REGEX = "^.+\\d.+$";

	public static boolean isActive(Login login) {
		return login.getState() == STATE_ACTIVE;
	}

	/**
	 * Hash password (with MD5). TODO: Refactor, use common utils
	 * 
	 * @param password
	 * @return hashed password
	 * @throws NoSuchAlgorithmException
	 */
	public static String hashPassword(String password) {
		return EncryptionHelper.md5(password);
	}

	public static void formToEntity(LoginForm form, Login entity) {
		if (form.getPk() != null) {
			Long pk = LongConvertor.getLongFromString(form.getPk());
			entity.setPk(pk);
		}
		if (form.getName() != null) {
			entity.setName(form.getName());
		}
		if (form.getLdapLogin() != null) {
			entity.setLdapLogin(form.getLdapLogin());
		}
		if (form.getPassword() != null) {
			entity.setPassword(form.getPassword());
		}
	}

	public static void entityToForm(Login entity, LoginForm form) {
		form.setPk(LongConvertor.convertToStringFromLong(entity.getPk()));
		form.setName(entity.getName());
		form.setLdapLogin(entity.getLdapLogin());
		// Password is never sent to form !!!
	}

	public static String generatePermanentPassword(Login login) {
		StringBuffer permanentPassword = new StringBuffer();
		String loginStamp = "" + login.getName() + " / " + login.getPk() + " / " + login.getPassword();
		String uniqueStamp = "" + Thread.currentThread() + " / " + new Date().getTime() + " / "
				+ new Random().nextDouble();
		permanentPassword.append(EncryptionHelper.md5(loginStamp));
		permanentPassword.append("-");
		permanentPassword.append(EncryptionHelper.md5(uniqueStamp));
		return permanentPassword.toString();
	}

	/**
	 * Password must be long enough, must contain at least one digit
	 * 
	 * @param password
	 * @return
	 */
	public static boolean isPasswordSecure(String password) {
		if (StringUtils.hasText(password)) {
			if (password.length() >= MIN_PASSWORD_LENGTH) {
				if (password.matches(STRING_WITH_DIGIT_REGEX)) {
					return true;
				}
			}
		}
		return false;
	}

}
