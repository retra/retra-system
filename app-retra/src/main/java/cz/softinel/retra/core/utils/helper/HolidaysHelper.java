package cz.softinel.retra.core.utils.helper;

import java.util.Date;

import cz.softinel.uaf.holidays.HolidaysFactory;

public class HolidaysHelper {


	public static boolean isPublicHoliday(Date date) {
		HolidaysFactory holidaysFactory = HolidaysFactory.getInstance();
		return holidaysFactory.isPublicHoliday(date);
	}
}
