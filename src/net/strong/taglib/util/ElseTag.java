package net.strong.taglib.util;

import javax.servlet.jsp.JspException;

import net.strong.taglib.TagIf;
/**
 * 条件可以为{OBJECT}::exp1 逻辑运算符 {OBJECT}::exp2 <br>
 * OBJECT为对象,如request、session、row等<br>
 * exp 为对像的方法（size,empty），对像的属性等
 *	逻辑运算符:　>= <= > < == !=  &&  ||
 *<stutil:if test="exp">
 *</stutil:if>
 *</stutil:elseif test="">
 *</stutil:elseif>
 *<stutil:else>
 *</stutil:else>
 * @ Strong Yuan
 * @since 2010-03-27
 */
public class ElseTag extends TagIf {

	private static final long serialVersionUID = 7380062505007215864L;

	@Override
	public int doEndTag() throws JspException {
		decressIfStatementNumber();
		return super.doEndTag();
	}

	@Override
	public int doStartTag() throws JspException {
		incressIfStatementNumber();
		boolean answer = findAnswer();
		//System.out.println("else:Number:" + number+";Attrubute:" + ProConstants.IF_ANSWER+number+";answer:" + answer);
		removeAnswer();
		if(answer){
			return SKIP_BODY;
		}
		return EVAL_BODY_INCLUDE;
	}

}
