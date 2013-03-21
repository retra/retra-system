package cz.softinel.uaf.util;

public class TimerHelper {

	public static void notSafeSleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// This is not safe sleep helper method ... never mind
		}
	}
}
