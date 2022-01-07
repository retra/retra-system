package cz.softinel.uaf.vc.tag;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.util.StringUtil;

import cz.softinel.uaf.util.CommonHelper;

@SuppressWarnings("serial")
public class SelectTag extends VisualComponentTag {

	private String id;
	private String name;
	private Map<String, String> staticValues;
	private List<Object> valueObjects;
	private String selected;
	private String valueProperty;
	private String labelProperty;
	private String parentProperty;
	private String orderBy;
	private boolean multiselect;
	private String onchange;

	@Override
	protected void reset() {
		id = null;
		name = null;
		staticValues = new HashMap<String, String>();
		valueObjects = null;
		selected = null;
		valueProperty = null;
		labelProperty = null;
		parentProperty = null;
		orderBy = null;
		multiselect = false;
		onchange = null;
	}

	@Override
	protected void generateTag(String content) {
		if (parentProperty == null) {
			generateList();
		} else {
			generateTree();
		}
	}

	protected void generateStaticValues() {
		for (Map.Entry<String, String> entry : staticValues.entrySet()) {
			generateOption(entry.getKey(), entry.getValue(), -1);
		}
	}

	protected void generateTree() {
		generateSelectBegin();
		generateStaticValues();
		generateTreeLevel(0, null);
		generateSelectEnd();
	}

	private static class ValueObjectComparator implements Comparator<Object> {
		private final String orderBy;

		public ValueObjectComparator(String orderBy) {
			this.orderBy = orderBy;
		}

		public int compare(Object o1, Object o2) {
			if (o1 == null) {
				if (o2 == null) {
					return 0;
				} else {
					return -1;
				}
			} else {
				if (o2 == null) {
					return 1;
				} else {
					try {
						Comparable<String> v1 = BeanUtils.getProperty(o1, orderBy);
						String v2 = BeanUtils.getProperty(o2, orderBy);
						if (v1 != null && v2 != null) {
							return v1.compareTo(v2);							
						} else {
							if (v2 == null) {
								return 0;
							} else {
								return -1;
							}
						}
					} catch (IllegalAccessException e) {
						throw new RuntimeException("Exception while getting property: " + e.getMessage(), e);
					} catch (InvocationTargetException e) {
						throw new RuntimeException("Exception while getting property: " + e.getMessage(), e);
					} catch (NoSuchMethodException e) {
						throw new RuntimeException("Exception while getting property: " + e.getMessage(), e);
					}
				}
			}
		}
	}

	private void orderList(List<Object> list) {
		if (orderBy != null) {
			Collections.sort(list, new ValueObjectComparator(orderBy));
		}
	}

	private void generateTreeLevel(int level, Object parentValue) {
		try {
			if (valueObjects == null) {
				return;
			}
			List<Object> orderedList = new ArrayList<Object>(valueObjects.size());
			for (Object value : valueObjects) {
				Object parent = null;
				try {
					parent = BeanUtils.getProperty(value, parentProperty);
				} catch (Exception e) {
					// It is ok ...
				}
				if (CommonHelper.isEquals(parentValue, parent)) {
					orderedList.add(value);
				}
			}
			orderList(orderedList);
			for (Object valueObject : orderedList) {
				generateOption(valueObject, level);
				generateTreeLevel(level + 1, BeanUtils.getProperty(valueObject, valueProperty));
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException("BeanUtils exception: " + e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("BeanUtils exception: " + e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("BeanUtils exception: " + e.getMessage(), e);
		}
	}

	protected void generateList() {
		try {
			generateSelectBegin();
			generateStaticValues();
			List<Object> orderedList;
			if (valueObjects != null) {
				orderedList = new ArrayList<Object>(valueObjects);
			} else {
				orderedList = new ArrayList<Object>();
			}
			orderList(orderedList);
			for (Object o : orderedList) {
				generateOption(o, -1);
			}
			generateSelectEnd();
		} catch (IllegalAccessException e) {
			throw new RuntimeException("BeanUtils exception: " + e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("BeanUtils exception: " + e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("BeanUtils exception: " + e.getMessage(), e);
		}
	}

	private void generateSelectBegin() {
		output.append("\n	<select name='" + name + "'");
		if (onchange != null) {
			output.append(" onchange='" + onchange + "'");
		}
		if (id != null) {
			output.append(" id='" + id + "'");
		}
		output.append(">");
	}

	private void generateSelectEnd() {
		output.append("\n	</select>");
	}

	private void generateOption(Object o, int level)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String label = BeanUtils.getProperty(o, this.labelProperty);
		String value = BeanUtils.getProperty(o, this.valueProperty);
		generateOption(value, label, level);
	}

	private void generateOption(String value, String label, int level) {
		String selectedAttribute = "";
		// TODO: Support multi value for selected attribute
		if (CommonHelper.isEquals(value, selected)) {
			selectedAttribute = " selected='selected'";
		}
		String prefix = "";
		if (level >= 0) {
			prefix = "&nbsp;+&nbsp;";
			for (int i = 0; i < level; i++) {
				prefix = "&nbsp;&nbsp;&nbsp;&nbsp;" + prefix;
			}
		}
		String escapedValue = StringEscapeUtils.escapeHtml(value);
		String escapedLabel = StringEscapeUtils.escapeHtml(label);
		
		output.append(
				"\n		<option value='" + escapedValue + "'" + selectedAttribute + ">" + prefix + escapedLabel + "</option>");
	}

	// Setters ...

	public void setHierarchyKey(String hierarchyKey) {
		this.parentProperty = hierarchyKey;
	}

	public void setLabelKay(String labelKay) {
		this.labelProperty = labelKay;
	}

	public void setMultiselect(boolean multiselect) {
		this.multiselect = multiselect;
	}

	public void setValueKey(String valueKey) {
		this.valueProperty = valueKey;
	}

	public void setLabelProperty(String labelProperty) {
		this.labelProperty = labelProperty;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public void setParentProperty(String parentProperty) {
		this.parentProperty = parentProperty;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public void setValueProperty(String valueProperty) {
		this.valueProperty = valueProperty;
	}

	public void setValueObjects(List<Object> valueObjects) {
		this.valueObjects = valueObjects;
	}

	public Map<String, String> getStaticValues() {
		return staticValues;
	}

	public String getOnchange() {
		return onchange;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	protected void afterTag() {
	}

	@Override
	protected void beforeTag() {
	}

}
