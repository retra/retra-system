package cz.softinel.uaf.orm.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;

public abstract class AbstractHibernateDao extends HibernateDaoSupport {

	/** Returns exactly one entity from list */
	// TODO: Refactor ... use common util helper method
	public Object getExactlyOne(List list) {

		if (list == null || list.isEmpty()) {
			return null;
		}
		if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new IllegalArgumentException("List should contains exactly one entity.");
		}
	}

	// private HibernateLister hibernateLister;

	public String getLikeValue(String value) {

		String result = null;

		if (value != null && !value.equals("null") && StringUtils.hasLength(value)) {
			result = value.replaceAll("\\*", "%");
			result = result.replaceAll("\\?", "_");
		}

		return result;
	}
}
