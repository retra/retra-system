package cz.softinel.uaf.vc.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * This tag is parent for all Visual Components Tag
 * 
 * This support easy way to create custom tag.
 * Trying hide some tag implementation hell (reset method, 
 * It is designed for visual components - tags which 
 * generate some HTML output
 * 
 * @author Radek Pinc
 *
 */
public abstract class VisualComponentTag extends BodyTagSupport {

	// TODO: Dont use string buffer ... HTML writer should be better
	protected StringBuffer output;
	
	// TODO: Use more performance safe solution ...
	private String content;
	
	// TODO: It is right place for this attribute?
	private String tabindex;
	
	public VisualComponentTag() {
		internalReset();
	}

	private final void internalReset() {
		content = null;
		reset();
	}
	
	protected abstract void reset();
	
	protected abstract void generateTag(String content);

	protected abstract void beforeTag();
	
	protected abstract void afterTag();

	@Override
	public final int doStartTag() throws JspException {
		output = new StringBuffer();
		beforeTag();
		return super.doStartTag();
	}
	
	@Override
	public final void doInitBody() throws JspException {
		super.doInitBody();
	}
	
	@Override
	public final int doAfterBody() throws JspException {
		content = getBodyContent().getString();
		return super.doAfterBody();
	}

	@Override
	public final int doEndTag() throws JspException {
		generateTag(content);
		afterTag();
		try {
			pageContext.getOut().write(output.toString());
		} catch (IOException e) {
			throw new JspException("Unexpected IO Exception during generateing visual component output.", e);
		} finally {
			output = null;
			internalReset();
		}
		return super.doEndTag();
	}

	public String getTabindex() {
		return tabindex;
	}

	public void setTabindex(String tabindex) {
		this.tabindex = tabindex;
	}
	
}
