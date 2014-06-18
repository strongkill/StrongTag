package net.strong.taglib;

import javax.servlet.jsp.JspException;

import net.strong.bean.ProConstants;
import net.strong.taglib.db.dbTag;

/**
 * 条件可以为{OBJECT}::exp1 逻辑运算符 {OBJECT}::exp2 <br>
 * OBJECT为对象,如request、session、row等<br>
 * exp 为对像的方法（size,empty），对像的属性等 逻辑运算符:　>= <= > < == != && ||
 *
 * <stutil:if test="exp"> </stutil:if>
 * </stutil:elseif test=""> </stutil:elseif>
 * <stutil:else> </stutil:else>
 * @version 2.0
 *
 * @ Strong Yuan
 *
 * @since 2010-03-27
 */
public class TagIfv2 extends dbTag {
	private static final long serialVersionUID = -902785683009452838L;
	private static String opts[] = {">=","<=",">","<","==","!="};
	private static String logic_opts[] = {"&&","||"};

	protected void removeAnswer() {
		getRequest().removeAttribute(
				ProConstants.IF_ANSWER + getIf_statement_number());
	}

	protected boolean findAnswer() {
		return (Boolean) (getRequest().getAttribute(
				ProConstants.IF_ANSWER + getIf_statement_number()) == null ? false
				: getRequest().getAttribute(
						ProConstants.IF_ANSWER + getIf_statement_number()));
	}

	protected void setAnswer(boolean answer) {
		getRequest().setAttribute(
				ProConstants.IF_ANSWER + getIf_statement_number(), answer);
	}

	protected int getIf_statement_number() {
		Object num = getRequest()
				.getAttribute(ProConstants.IF_STATEMENT_NUMBER) == null ? 0
				: getRequest().getAttribute(ProConstants.IF_STATEMENT_NUMBER);
		int number = (Integer) num;
		if (number < 0)
			number = 0;
		return number;
	}

	protected int incressIfStatementNumber() {
		int number = getIf_statement_number();
		number = number + 1;
		getRequest().setAttribute(ProConstants.IF_STATEMENT_NUMBER, number);
		return number;
	}

	protected int decressIfStatementNumber() {
		int number = getIf_statement_number();
		if (number > 0)
			number = number - 1;
		getRequest().setAttribute(ProConstants.IF_STATEMENT_NUMBER, number);
		return number;
	}

	protected boolean hasLogic(String test){
		for(String logic_opt : logic_opts)
			if(test.indexOf(logic_opt)>-1)
				return true;
		return false;
	}
	/**
	 * 判断test表达式
	 *
	 * @return
	 */
	protected boolean isTest() {
		// System.out.println(getTest());
		// long current = System.currentTimeMillis();
		boolean retval = false;
		String tmp_test = test;
		int s_pos = tmp_test.indexOf("(");
		if (s_pos > -1 && tmp_test.indexOf("(", s_pos + 1)>-1) { // 存在多个运算
			int e_pos = tmp_test.indexOf(")");
			boolean tmp_retval = false;
			boolean isfirst = true;
			String opt = null;
			while (s_pos > -1) {
				int ss_pos = tmp_test.indexOf("(", s_pos + 1);
				int loop_times = 0;//防止死loop
				while (loop_times<10 && ss_pos > -1 && ss_pos < e_pos) {
					e_pos = tmp_test.indexOf(")", e_pos + 1);
					ss_pos = tmp_test.indexOf("(", ss_pos + 1);
					loop_times = loop_times+1;
				}
				String tmp = tmp_test.substring(s_pos + 1, e_pos);
				try {
					tmp_retval = deal(tmp);
				} catch (Exception e) {
					e.printStackTrace();
				}
				tmp_test = tmp_test.substring(e_pos + 1, tmp_test.length());
				s_pos = tmp_test.indexOf("(");
				if (isfirst) {
					retval = tmp_retval;
				} else {
					if ("&&".equalsIgnoreCase(opt)) {
						retval = retval && tmp_retval;
					} else if ("||".equalsIgnoreCase(opt)) {
						retval = retval || tmp_retval;
					}
				}
				if (s_pos > -1) {
					opt = tmp_test.substring(0, s_pos);
					opt = opt.trim();
				}
				// System.out.println(opt.trim());

				e_pos = tmp_test.indexOf(")");
				isfirst = false;
			}
		} else {
			try {
				retval = deal(tmp_test);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// System.out.println("Test : "+retval+"; need time:" +
		// (System.currentTimeMillis()-current));
		return retval;
	}

	public static void main(String[] args) {
		long current = System.currentTimeMillis();
		boolean retval = false;

		// for(int i = 0;i<1000;i++){
		TagIfv2 ift = new TagIfv2();
		ift
				.setTest(" MemberUser::isAdmin == true  || MemberUser::userlevel == 2 ");//(strlen(substr(strong,1,3))>0) && ( substr(strong,1,3) == tr ) ||
		retval = ift.isTest();
		// }
		System.out.println("Test : " + retval + "; need time:"
				+ (System.currentTimeMillis() - current));

	}

	protected boolean deal(String exp1, String operation, String exp2)
			throws JspException {
		System.out.println(exp1 + operation + exp2);
		exp1 = exp1.trim();
		operation = operation.trim();
		exp2 = exp2.trim();
		boolean retval = false;

		String spe_operation1 = null;
		int arg1[] = { -1, -1 };
		String spe_operation2 = null;
		int arg2[] = { -1, -1 };

		if (exp1.indexOf("(") > -1) {
			int s_pos = exp1.indexOf("(");
			int e_pos = exp1.indexOf(")");
			if(e_pos<=-1){
				exp1 = exp1.substring(s_pos+1, exp1.length());
			}else{
			spe_operation1 = exp1.substring(0, s_pos);
			exp1 = exp1.substring(s_pos + 1, e_pos);
			if (exp1.indexOf(",") > -1) {
				String[] tmp_exp1 = exp1.split(",");
				exp1 = tmp_exp1[0];
				arg1[0] = Integer.parseInt(tmp_exp1[1]);
				arg1[1] = Integer.parseInt(tmp_exp1[2]);
			}
			}
		}
		if (exp2.indexOf("(") > -1) {
			int s_pos = exp2.indexOf("(");
			int e_pos = exp2.indexOf(")");
			spe_operation2 = exp2.substring(0, s_pos);
			exp2 = exp2.substring(s_pos + 1, e_pos);
			if (exp2.indexOf(",") > -1) {
				String[] tmp_exp2 = exp2.split(",");
				exp2 = tmp_exp2[0];
				arg2[0] = Integer.parseInt(tmp_exp2[1]);
				arg2[1] = Integer.parseInt(tmp_exp2[2]);
			}
		}else if(exp2.indexOf(")")>-1){
			int e_pos = exp2.indexOf(")");
			exp2 = exp2.substring(0, e_pos-1);
		}

		// System.out.println(exp1 + operation + exp2);
		Object value1 = exp1;
		Object value2 = exp2;

		if (exp1.indexOf("::") > -1) {
			String[] tmp = exp1.split("::");
			value1 = getValue(tmp[0], tmp[1]);
			// System.out.println(tmp[0]+" => "+tmp[1]+"=>" + value1);
		} else {
			try {
				value1 = Integer.parseInt((String) value1);
			} catch (Exception e) {
				// value1 is not number;
			}
		}
		if (exp2.indexOf("::") > -1) {
			String[] tmp = exp2.split("::");
			value2 = getValue(tmp[0], tmp[1]);
			// System.out.println(tmp[0]+" => "+tmp[1]+"=>" + value2);
		} else {
			try {
				value2 = Integer.parseInt((String) value2);
			} catch (Exception e) {
				// value2 is not number;
			}
		}
		// System.out.println(value1+operation+value2);
		if (value1 == null || value2 == null) {
			/*
			 * if("!=".equalsIgnoreCase(operation)){ retval= !retval; }
			 */
			return retval;
		}
		if (spe_operation1 != null) {
			if ("strlen".equalsIgnoreCase(spe_operation1) || "length".equalsIgnoreCase(spe_operation1)) {
				value1 = ((String) value1).length();
			} else if ("substr".equalsIgnoreCase(spe_operation1)) {
				value1 = ((String) value1).substring(arg1[0], arg1[1]);
			}
		}
		if (spe_operation2 != null) {
			if ("strlen".equalsIgnoreCase(spe_operation2) || "length".equalsIgnoreCase(spe_operation2)) {
				value2 = ((String) value2).length();
			} else if ("substr".equalsIgnoreCase(spe_operation2)) {
				value2 = ((String) value2).substring(arg2[0], arg2[1]);
			}
		}
		if (value1 instanceof String && value2 instanceof String) {

			retval = ((String) value1).equalsIgnoreCase((String) value2);
			if ("!=".equalsIgnoreCase(operation)) {
				retval = !retval;
			}
		} else if (value1 instanceof Boolean || value2 instanceof Boolean) {
			if (value1 instanceof Boolean && value2 instanceof Boolean) {
				retval = (Boolean) value1 && (Boolean) value2;
				if ("!=".equalsIgnoreCase(operation)) {
					retval = !retval;
				}
			} else if (value1 instanceof Boolean) {
				retval = (Boolean) value1 == "true"
						.equalsIgnoreCase((String) value2) ? true : false;
				if ("!=".equalsIgnoreCase(operation)) {
					retval = !retval;
				}
			} else {
				retval = (Boolean) value2
						&& ("true".equalsIgnoreCase((String) value1) ? true
								: false);
				if ("!=".equalsIgnoreCase(operation)) {
					retval = !retval;
				}
			}
		} else if (value1 instanceof Number || value2 instanceof Number) {
			double tmp1 = 0;
			double tmp2 = 0;
			if (value1 instanceof Number && value2 instanceof Number) {
				tmp1 = ((Number) value1).doubleValue();
				tmp2 = ((Number) value2).doubleValue();
			} else if (value1 instanceof Number) {
				tmp1 = ((Number) value1).doubleValue();
				try {
					tmp2 = Double.parseDouble((String) value2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					tmp1 = Double.parseDouble((String) value1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				tmp2 = ((Number) value2).doubleValue();
			}
			if (">=".equalsIgnoreCase(operation)) {
				retval = tmp1 >= tmp2;
			} else if ("<=".equalsIgnoreCase(operation)) {
				retval = tmp1 <= tmp2;
			} else if (">".equalsIgnoreCase(operation)) {
				retval = tmp1 > tmp2;
			} else if ("<".equalsIgnoreCase(operation)) {
				retval = tmp1 < tmp2;
			} else if ("==".equalsIgnoreCase(operation)) {
				retval = (tmp1 == tmp2);
			} else if ("!=".equalsIgnoreCase(operation)) {
				retval = tmp1 != tmp2;
			} else {
				retval = false;
			}
		} else if (value1 instanceof oracle.sql.TIMESTAMP) {

		} else if (value1 instanceof oracle.sql.DATE) {

		} else if (value1 instanceof java.util.Date) {

		}
		/*
		 * if(">=".equalsIgnoreCase(operation)){ }else
		 * if("<=".equalsIgnoreCase(operation)){ }else
		 * if(">".equalsIgnoreCase(operation)){ }else
		 * if("<".equalsIgnoreCase(operation)){ }else
		 * if("==".equalsIgnoreCase(operation)){ }else
		 * if("!=".equalsIgnoreCase(operation)){ }
		 */
		return retval;
	}

	protected boolean deal(String val) throws JspException {
		String[] tmp = null;
		String operation = null;
		if (val == null)
			return false;
		for (String opt : opts) {
			if (val.indexOf(opt) > -1) {
				tmp = val.split(opt);
				operation = opt;
				break;
			}
		}
		/*
		 * for(int i=0;i<opts.size();i++){ if(val.indexOf(opts.get(i))>-1){ tmp
		 * = val.split(opts.get(i)); operation = opts.get(i); break; } }
		 */
		if (operation != null)
			return deal(tmp[0], operation, tmp[1]);

		return false;
	}

	private String serial;

	public String getSerial() {
		return serial == null ? "" : serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	private String test;

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}


}
