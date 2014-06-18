package net.strong;

/**
 * <p>Title: 用户类</p>
 * <p>Description:用于记录用户信息的类，通常用户登录后，将数据库中
 * 的用户信息存入一User类的实例，并将此实例存入pageContext对象中，
 * 要用它时，从pageContext对象中取值即可 </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author unascribed
 * @version 1.0
 */

public class User {

  protected String username = null;
  protected String truename = null;
  protected String sex = null;
//  protected String password = null;
  protected String email = null;
//  private String pwQuestion = null;
//  private String pwAnswer = null;
  protected String localePrefer = null;
  protected String str_user_id = null;
  protected String str_dept_id = null;

  protected long level = 0;
  protected long user_id = 0;
  protected long dept_id = 0;
  protected long role_id = 0;

  private long member_id;
  private String alias_name;
  private String bas_no;
  private String member_type_name;
  private String password;
  private String com_parent_name;
  private String com_type_name;
  private String user_template=null;
  private int member_type;
  private int vip_flag;
  private int user_level;
  private int user_locked;
  private float ser_money;
  private Integer com_parent_id;
  private Integer com_type_id;
  private boolean isVip;
  
  public boolean isVip() {
	return isVip;
}

public void setVip(boolean isVip) {
	this.isVip = isVip;
}

public void setLevel(long level) {
	this.level = level;
}

public User() {
  }

  public void setStr_user_id(String str_user_id)
  {
    this.str_user_id = str_user_id;
    try
    {
      user_id = Long.valueOf(str_user_id).longValue();
      member_id=user_id;
    }
    catch(Exception e)
    {
      user_id = 0;
    }
  }
  public String getStr_user_id()
  {
    return this.str_user_id;
  }
  public void setStr_dept_id(String str_dept_id)
  {
    this.str_dept_id = str_dept_id;
    try
    {
      dept_id = Long.valueOf(str_dept_id).longValue();
    }
    catch(Exception e)
    {
      dept_id = 0;
    }
  }
  public String getStr_dept_id(){
    return this.str_dept_id;
  }
  public void setUsername(String username)
  {
    this.username = username;
  }
  public String getUsername()
  {
    return username;
  }
  public void setTruename(String truename)
  {
    this.truename = truename;
  }
  public String getTruename()
  {
    return truename;
  }
  public void setSex(String sex)
  {
    this.sex = sex;
  }
  public String getSex()
  {
    return sex;
  }
  public void setEmail(String email)
  {
    this.email = email;
  }
  public String getEmail()
  {
    return this.email;
  }
/*
  public void setPwQuestion(String pwQuestion)
  {
    this.pwQuestion = pwQuestion;
  }
  public String getPwQuestion()
  {
    return this.pwQuestion;
  }
  public void setPwAnswer(String pwAnswer)
  {
    this.pwAnswer = pwAnswer;
  }
  public String getPwAnswer()
  {
    return pwAnswer;
  }
*/
  public void setLocalePrefer(String localePrefer)
  {
    this.localePrefer = localePrefer;
  }
  public String getLocalePrefer()
  {
    return this.localePrefer;
  }
  public void setLevel(int level)
  {
    this.level = level;
  }
  public long getLevel()
  {
    return level;
  }
  public void setUser_id(long user_id)
  {
    this.user_id = user_id;
    this.str_user_id = String.valueOf(user_id);
    member_id=user_id;
  }
  public long getUser_id()
  {
    return this.user_id;
  }
  public void setUserId(long user_id)
  {
    this.user_id = user_id;
    this.str_user_id = String.valueOf(user_id);
    member_id=user_id;
  }
  public long getUserId()
  {
    return this.user_id;
  }
  public void setDept_id(long dept_id)
  {
    this.dept_id = dept_id;
    str_dept_id = String.valueOf(dept_id);
  }
  public long getDept_id()
  {
    return this.dept_id;
  }
  public void setDeptId(long dept_id)
  {
    this.dept_id = dept_id;
  }
  public long getDeptId()
  {
    return this.dept_id;
  }
  public void setRole_id(long role_id)
  {
    this.role_id = role_id;
  }
  public long getRole_id(){
    return this.role_id;
  }
  public void setRoleId(long role_id)
  {
    this.role_id = role_id;
  }
  public long getRoleId()
  {
    return this.role_id;
  }
  public long getMember_id() {
    return member_id;
  }
  public void setMember_id(long member_id) {
    this.member_id = member_id;
    this.user_id = member_id;
    this.str_user_id = String.valueOf(member_id);
  }
  public String getAlias_name() {
    return alias_name;
  }
  public void setAlias_name(String alias_name) {
    this.alias_name = alias_name;
  }
  public String getBas_no() {
    return bas_no;
  }
  public void setBas_no(String bas_no) {
    this.bas_no = bas_no;
  }
  public String getMember_type_name() {
    return member_type_name;
  }
  public void setMember_type_name(String member_type_name) {
    this.member_type_name = member_type_name;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getCom_parent_name() {
    return com_parent_name;
  }
  public void setCom_parent_name(String com_parent_name) {
    this.com_parent_name = com_parent_name;
  }
  public String getCom_type_name() {
    return com_type_name;
  }
  public void setCom_type_name(String com_type_name) {
    this.com_type_name = com_type_name;
  }
  public String getUser_template() {
    return user_template;
  }
  public void setUser_template(String user_template) {
    this.user_template = user_template;
  }
  public int getMember_type() {
    return member_type;
  }
  public void setMember_type(int member_type) {
    this.member_type = member_type;
  }
  public int getVip_flag() {
    return vip_flag;
  }
  public void setVip_flag(int vip_flag) {
    this.vip_flag = vip_flag;
  }
  public int getUser_level() {
    return user_level;
  }
  public void setUser_level(int user_level) {
    this.user_level = user_level;
  }
  public int getUser_locked() {
    return user_locked;
  }
  public void setUser_locked(int user_locked) {
    this.user_locked = user_locked;
  }
  public float getSer_money() {
    return ser_money;
  }
  public void setSer_money(float ser_money) {
    this.ser_money = ser_money;
  }
  public Integer getCom_parent_id() {
    return com_parent_id;
  }
  public void setCom_parent_id(Integer com_parent_id) {
    this.com_parent_id = com_parent_id;
  }
  public Integer getCom_type_id() {
    return com_type_id;
  }
  public void setCom_type_id(Integer com_type_id) {
    this.com_type_id = com_type_id;
  }
}