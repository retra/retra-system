package cz.softinel.uaf.vc.tag;

import javax.servlet.jsp.tagext.Tag;

@SuppressWarnings("serial")
public class SelectOptionTag extends VisualComponentTag {

	String value;
	String static_;
	String label;

	@Override
	protected void reset() {
		value = null;
		static_ = null;
		label = null;
	}

	@Override
	protected void generateTag(String content) {
		if (content != null) {
			label = content;
		}
		Tag parent = getParent();
		if (parent instanceof SelectTag) {
			SelectTag selectTag = (SelectTag) parent;
			selectTag.getStaticValues().put(value, label);
		} else {
			throw new IllegalStateException("SelectOptionTag is not used under SelectTag");
		}
	}

	// Getters and setters ...

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getStatic() {
		return static_;
	}

	public void setStatic(String static_) {
		this.static_ = static_;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	protected void afterTag() {
	}

	@Override
	protected void beforeTag() {
	}

}
