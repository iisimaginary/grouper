/*
  Copyright (C) 2004-2007 University Corporation for Advanced Internet Development, Inc.
  Copyright (C) 2004-2007 The University Of Pennsylvania

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package edu.internet2.middleware.grouper.ui.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.lang.StringUtils;

/**
 * <pre>
 * This will generate an infodot image which when clicked will hide/show some instructions
 * 
 * Generates something like this:
 * 
 * &lt;a href="#" onclick="return grouperHideShow(event, 'firstHideShow');"&gt;&lt;img
 *	src="grouper/images/infodot.gif" border="0" alt="More Information" class="infodotImage"
 *	height="16" width="16" /&gt;&lt;/a&gt;
 * </pre>
 * @author mchyzer
 *
 */
public class GrouperInfodotTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 0L;
	
	/**
	 * id of the html element to hide/show (actually it is the prefix, needs an 
	 * index after, which is generated by the hide/show tag
	 */
	private String hideShowHtmlId;

	/**
	 * reset field on construct or recycle
	 */
	private void init() {
		this.hideShowHtmlId = null;
	}
	
	/**
	 * init fields on construct
	 */
	public GrouperInfodotTag() {
		this.init();
	}

	/** 
     * Releases any resources we may have (or inherit)
     */
    public void release() {
        super.release();
        init();
    }

    /**
	 * id of the html element to hide/show (actually it is the prefix, needs an 
	 * index after, which is generated by the hide/show tag
	 * @param hideShowHtmlId the hideShowHtmlId to set
	 */
	public void setHideShowHtmlId(String hideShowHtmlId) {
		this.hideShowHtmlId = hideShowHtmlId;
	}

	/**
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		
		//if not enabled, then dont display anything
		if (!TagUtils.mediaResourceBoolean(this.pageContext.getRequest(), "infodot.enable", true)) {
			return Tag.EVAL_PAGE;
		}
		
		String altText = StringUtils.trim(TagUtils.navResourceString(this.pageContext.getRequest(), "groups.infodot.alt"));
		
		String result = "<a href=\"#\" class=\"infodotLink\" onclick=\"return grouperHideShow(event, '" + this.hideShowHtmlId + "');\"><img \n" +
				"src=\"grouper/images/infodot.gif\" border=\"0\" alt=\"" + altText + "\" \n" +
				"height=\"16\" class=\"infodotImage\" width=\"16\" /></a>";
		
		//just print out the image tag
		try {
			this.pageContext.getOut().print(result);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
		
		return Tag.EVAL_PAGE;
	}
	
}
