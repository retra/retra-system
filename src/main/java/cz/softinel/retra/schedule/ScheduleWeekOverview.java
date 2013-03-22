package cz.softinel.retra.schedule;

/**
 * This class represents week schedule items
 * 
 * @version $Revision: 1.1 $ $Date: 2007-01-30 08:49:13 $
 * @author Petr Sígl
 */
public class ScheduleWeekOverview {

	private Schedule monday;
	private Schedule tuesday;
	private Schedule wednesday;
	private Schedule thursday;
	private Schedule friday;
	private Schedule saturday;
	private Schedule sunday;

	/**
	 * @return the friday
	 */
	public Schedule getFriday() {
		return friday;
	}

	/**
	 * @param friday the friday to set
	 */
	public void setFriday(Schedule friday) {
		this.friday = friday;
	}

	/**
	 * @return the monday
	 */
	public Schedule getMonday() {
		return monday;
	}

	/**
	 * @param monday the monday to set
	 */
	public void setMonday(Schedule monday) {
		this.monday = monday;
	}

	/**
	 * @return the saturday
	 */
	public Schedule getSaturday() {
		return saturday;
	}

	/**
	 * @param saturday the saturday to set
	 */
	public void setSaturday(Schedule saturday) {
		this.saturday = saturday;
	}

	/**
	 * @return the sunday
	 */
	public Schedule getSunday() {
		return sunday;
	}

	/**
	 * @param sunday the sunday to set
	 */
	public void setSunday(Schedule sunday) {
		this.sunday = sunday;
	}

	/**
	 * @return the thursday
	 */
	public Schedule getThursday() {
		return thursday;
	}

	/**
	 * @param thursday the thursday to set
	 */
	public void setThursday(Schedule thursday) {
		this.thursday = thursday;
	}

	/**
	 * @return the tuesday
	 */
	public Schedule getTuesday() {
		return tuesday;
	}

	/**
	 * @param tuesday the tuesday to set
	 */
	public void setTuesday(Schedule tuesday) {
		this.tuesday = tuesday;
	}

	/**
	 * @return the wednesday
	 */
	public Schedule getWednesday() {
		return wednesday;
	}

	/**
	 * @param wednesday the wednesday to set
	 */
	public void setWednesday(Schedule wednesday) {
		this.wednesday = wednesday;
	}
}
