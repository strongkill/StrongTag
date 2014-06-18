package net.strong.exutil;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;

/**
 *
 * <p>Title:自定义产生ID类 </p>
 * <p>Description:自定义的产生Hibernate的ID类，此类能产生long,string（１６位）两种类型的ID，
 * 所产生的ID一般来说都能达到全数据库唯一。使用时只须指定类型即可，如下：
 * 例１：
 * 		<id column="userid" length="16" name="userid" type="string">
 *                       <generator class="net.strong.exutil.idGenerator"/>
 *               </id>
*例２：
 * <id column="userid"  name="userid" type="long">
 *  <generator class="net.strong.exutil.idGenerator"/>
 * </id>
 *
 *  </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 */
public class idGenerator implements IdentifierGenerator, Configurable  {
  private Class returnClass;
  /**
   * 此函数用于设置此类的配置信息，在本类中，主要用到参数type
   * @param type 此参数是指所须产生的ID的类型，本类只提供string,long两种。
   * @param params
   * @param dialect
   */
  public void configure(Type type, Properties params, Dialect dialect) {
    returnClass = type.getReturnedClass();
  }

  /**
   * 本函数是产生ID的主要函数，产生ID的算法都在此函数中。本函数的两个参数都没有实际用到。
   * @param parm1
   * @param parm2
   * @return
   * @throws net.sf.hibernate.HibernateException
   */
  public Serializable generate(SessionImplementor parm1, Object parm2) throws
      org.hibernate.HibernateException {
    /**@todo Implement this net.sf.hibernate.id.IdentifierGenerator method*/
    long time = System.currentTimeMillis();

    int i=(int)(Math.random()*1000+1);
    long result = time*1000 + i;
    if ( returnClass.equals( Long.class ) ) {
            return new Long(result);
    }
    else if ( "java.lang.String".equals( returnClass.getName() )) {
            return  String.valueOf(result);
    }
    else {
            throw new HibernateException("this id generator generates long, string only.");
    }

  }
}