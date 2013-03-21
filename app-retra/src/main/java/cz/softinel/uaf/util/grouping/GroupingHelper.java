package cz.softinel.uaf.util.grouping;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.apache.commons.beanutils.PropertyUtils;



public class GroupingHelper {

//	private static Log logger = LogFactory.getLog(GroupingHelper.class);

	public static GroupingMap group(Collection collection, String valueProperty, String groupKeyProperty, boolean removeIt) {
		try {
			GroupingMap groupedMap = new GroupingMap(1);
			for (Object item : collection) {
				Object groupKey = PropertyUtils.getProperty(item, groupKeyProperty);
				Float value = Float.valueOf(String.valueOf(PropertyUtils.getProperty(item, valueProperty)));
				groupedMap.add(new GroupingKey(new Object[]{groupKey}), new GroupedItem(value));
			}
			return groupedMap;
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Unexpected eception " + e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Unexpected eception " + e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Unexpected eception " + e.getMessage(), e);
		}
	}
	
	public static GroupingMap group(Collection collection, String valueProperty, String[] groupKeyProperties) {
		try {
			GroupingMap groupedMap = new GroupingMap(groupKeyProperties.length);
			for (Object item : collection) {
				Object[] groupKeys = new Object[groupKeyProperties.length];
				for (int i=0; i<groupKeyProperties.length; i++) {
//					logger.info(" >>> " + item + " ///  " + groupKeyProperties[i]);
					Object val = PropertyUtils.getProperty(item, groupKeyProperties[i]);
//					logger.info("   - " + val + " / " + groupKeys[i]);
					groupKeys[i] = val;
				}
				Float value = Float.valueOf(String.valueOf(PropertyUtils.getProperty(item, valueProperty)));
				groupedMap.add(new GroupingKey(groupKeys), new GroupedItem(value));
			}
			return groupedMap;
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Unexpected eception " + e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Unexpected eception " + e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Unexpected eception " + e.getMessage(), e);
		}
	}

	// FIXME radek: Write regular UnitTest
	public static void main(String[] args) {
		GroupingMap groupedMap = new GroupingMap(3);
		System.out.println(groupedMap.toString());
		groupedMap.add(new GroupingKey(new Object[]{1,1,1}), new GroupedItem(3));
		System.out.println(groupedMap.toString());
		groupedMap.add(new GroupingKey(new Object[]{1,1,1}), new GroupedItem(4));
		System.out.println(groupedMap.toString());
		groupedMap.add(new GroupingKey(new Object[]{2,1,1}), new GroupedItem(5));
		System.out.println(groupedMap.toString());
		groupedMap.add(new GroupingKey(new Object[]{1,2,3}), new GroupedItem(6));
		System.out.println(groupedMap.toString());
		groupedMap.add(new GroupingKey(new Object[]{4,1,1}), new GroupedItem(7));
		System.out.println(groupedMap.toString());
		groupedMap.add(new GroupingKey(new Object[]{1,1,1}), new GroupedItem(1));
		groupedMap.add(new GroupingKey(new Object[]{1,2,1}), new GroupedItem(1));
		groupedMap.add(new GroupingKey(new Object[]{1,3,1}), new GroupedItem(1));
		groupedMap.add(new GroupingKey(new Object[]{1,4,1}), new GroupedItem(1));
		
//		System.out.println(groupedMap.getGroupedListForDimension(0));
//		System.out.println(groupedMap.getGroupedListForDimension(1));
//		System.out.println(groupedMap.getGroupedListForDimension(2));

		System.out.println("---");
		System.out.println(groupedMap.getProjection(0).getList());
		System.out.println(groupedMap.getProjection(0).getList().get(0).getProjection(0));
		System.out.println(groupedMap.getProjection(0).getList().get(0).getProjection(1));
		System.out.println(groupedMap.getProjection(0).getList().get(0).getProjection(2));
		System.out.println("---");
		System.out.println(groupedMap.getProjection(1).getList());
		System.out.println(groupedMap.getProjection(2).getList());
	}
	
}
