package cz.softinel.retra.schedule;

import java.util.Calendar;
import java.util.Date;

import cz.softinel.retra.core.utils.convertor.DateConvertor;
import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.core.utils.helper.HolidaysHelper;
import cz.softinel.retra.schedule.web.ScheduleForm;
import cz.softinel.retra.type.Type;
import cz.softinel.retra.type.TypeHelper;

/**
 * Helpr methods for schedule
 *
 * @version $Revision: 1.5 $ $Date: 2007-04-03 07:55:39 $
 * @author Petr Sígl, Radek Pinc
 */
public class ScheduleHelper {

	// TODO siglp: change this according to StateEntity interface
	public final static int STATE_NEW = 0;
	public final static int STATE_APPROVED = 1;
	public final static int STATE_WAITING_FOR_DELETE = 2;
	public final static int STATE_DELETED = 3;
	public final static int STATE_CHANGED = 4;

	public final static int LEVEL_DETAIL_PLAN = 0;
	public final static int LEVEL_LONG_TIME_PLAN = 1;

	public final static int COPY_TYPE_DAY = 0;
	public final static int COPY_TYPE_WEEK = 1;

	public static void formToEntity(ScheduleForm form, Schedule entity) {
		if (form.getPk() != null) {
			Long pk = LongConvertor.getLongFromString(form.getPk());
			entity.setPk(pk);
		}

		Date from = DateConvertor.getDateFromDateStringHourString(form.getDate(), form.getWorkFrom());
		entity.setWorkFrom(from);

		Date to = DateConvertor.getDateFromDateStringHourString(form.getDate(), form.getWorkTo());
		entity.setWorkTo(to);

		entity.setComment(form.getComment());

		Type type = entity.getType();
		if (type == null) {
			type = new Type();
		}
		entity.setType(type);
		Long typePk = LongConvertor.getLongFromString(form.getType());
		type.setPk(typePk);

		// TODO: createdOn, changedOn, level, state
	}

	public static void entityToForm(Schedule entity, ScheduleForm form) {
		String pk = LongConvertor.convertToStringFromLong(entity.getPk());
		form.setPk(pk);

		String date = DateConvertor.convertToDateStringFromDate(entity.getWorkFrom());
		form.setDate(date);

		String from = DateConvertor.convertToHourStringFromDate(entity.getWorkFrom());
		form.setWorkFrom(from);

		String to = DateConvertor.convertToHourStringFromDate(entity.getWorkTo());
		form.setWorkTo(to);

		form.setComment(entity.getComment());

		Type type = entity.getType();
		if (type != null) {
			String typePk = LongConvertor.convertToStringFromLong(entity.getType().getPk());
			form.setType(typePk);
		}

		// TODO: createdOn, changedOn, level, state
	}

	public static boolean isApproved(Schedule schedule) {
		int state = schedule.getState();
		return state == STATE_APPROVED || state == STATE_DELETED;
	}

	public static String getCssClass(Schedule schedule) {
		String result = "scheduleDefault";
		// work from must be not null, else cssClass is default
		if (schedule.getWorkFrom() != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(schedule.getWorkFrom());
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			// if day is weekend always has class as weekend
			if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
				result = "scheduleWeekend";
			}
			// if it is public holidays
			else if (HolidaysHelper.isPublicHoliday(schedule.getWorkFrom())) {
				result = "scheduleWeekend";
			}
			// it is not weekend so we try get css class according to type (if it is set
			else if (schedule.getType() != null) {
				Type type = schedule.getType();
				result = TypeHelper.getCssClass(type);
			}
		}
		return result;
	}
}
