package cz.softinel.retra.employee;

import java.text.Collator;
import java.util.Comparator;

public class EmployeeNameComparator implements Comparator<String> {

	public int compare(String name1, String name2) {
		//This was the implementation when the first name and last name where displayed in first-last order.
		/*/
		String [] name1parts = name1.split(" ");
		String [] name2parts = name2.split(" ");
		String compare1;
		String compare2;
		if(name1parts.length < 2 ) {
			compare1 = name1parts[0];
		} else {
			compare1 = name1parts[name1parts.length-1];
		}
		if(name2parts.length < 2 ) {
			compare2 = name2parts[0];
		} else {
			compare2 = name2parts[name2parts.length-1];
		}
		Collator collator = Collator.getInstance();
		return collator.compare(compare1, compare2);
		//*/
		Collator collator = Collator.getInstance();
		return collator.compare(name1, name2);
		
		
//		return compare1.compareToIgnoreCase(compare2);
	}
	

}
