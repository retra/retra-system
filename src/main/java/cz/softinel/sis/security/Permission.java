package cz.softinel.sis.security;

import java.io.Serializable;

public class Permission implements Serializable {

	private String code;

	public Permission(final String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "(Permission: code=" + code + ")";
	}

	
}
