package net.strong.lang;

public class SStrings extends NStrings
{

	public static void main(String[] args){
		String a = FixSqlFieldsStarChar("res.rn,","res.", " m.id, m.name, m.target, m.marks_total_1, m.standard, m.action, m.rule, m.game_image, m.game_name, (m.marks_pass_1 + m.marks_pass_2)*3 marks_total, m.external_url");
		System.out.println(a);
	}
	public static String FixSqlFieldsStarChar(String perfix, String name, String sqlFields)
	  {
	    StringBuffer a = new StringBuffer(sqlFields.length());
	    if (perfix != null)
	      a.append(perfix);
	    int indexSpec = sqlFields.indexOf("(");
	    if (indexSpec > -1) {
	      StringBuffer sqlField = new StringBuffer(sqlFields.length());
	      while (indexSpec > -1) {
	        String tmp_s = sqlFields.substring(0, indexSpec);
	        sqlField.append(tmp_s);

	        int indexSpec_s = sqlFields.indexOf("(", indexSpec + 1);
	        int indexSpec_e = sqlFields.indexOf(")");
	        int indexSpec_ee = indexSpec_e;
	        while ((indexSpec_s > -1) && (indexSpec_ee > -1) && (indexSpec_s < indexSpec_ee)) {
	          indexSpec_ee = sqlFields.indexOf(")", indexSpec_ee + 1);
	          indexSpec_s = sqlFields.indexOf("(", indexSpec_s + 1);
	        }
	        if (indexSpec_ee > -1)
	          indexSpec_e = indexSpec_ee;
	        sqlFields = sqlFields.substring(indexSpec_e + 1, sqlFields.length());
	        indexSpec = sqlFields.indexOf("(");
	        if (indexSpec <= 0) {
	          sqlField.append(sqlFields);
	        }
	      }
	      sqlFields = sqlField.toString();
	    }
	    int indexI = sqlFields.indexOf(",");
	    int indexII = 0;
	    int indexAS = 0;
	    int indexSpace = 0;
	    String t = null;

	    if (indexI > -1) {
	      while (indexI > -1) {
	        t = sqlFields.substring(0, indexI);
	        t = t.trim();
	        indexAS = t.indexOf(" as ");
	        if (indexAS == 0) indexAS = t.indexOf(" AS ");
	        if (indexAS > -1) {
	          a.append(name).append(t.substring(indexAS + 4, t.length()));
	        } else {
	          indexSpace = t.lastIndexOf(" ");
	          if (indexSpace > -1) {
	            a.append(name).append(t.substring(indexSpace + 1, t.length()));
	          } else {
	            indexII = t.indexOf(".");
	            if (indexII > -1)
	              a.append(name).append(t.substring(indexII + 1, t.length()));
	            else {
	              a.append(name).append(t);
	            }
	          }
	        }
	        a.append(",");
	        sqlFields = sqlFields.substring(indexI + 1, sqlFields.length());
	        indexI = sqlFields.indexOf(",");
	        if (indexI <= 0) {
	        	sqlFields=sqlFields.trim();
	          indexAS = sqlFields.indexOf(" as ");
	          if (indexAS == -1) indexAS = sqlFields.indexOf(" AS ");
	          if (indexAS > -1) {
	            a.append(name).append(sqlFields.substring(indexAS + 4, sqlFields.length()));
	          } else {
	            indexSpace = sqlFields.lastIndexOf(" ");
	            if (indexSpace > -1) {
	              a.append(name).append(sqlFields.substring(indexSpace + 1, sqlFields.length()));
	            } else {
	              indexII = sqlFields.indexOf(".");
	              if (indexII > -1)
	                a.append(name).append(sqlFields.substring(indexII + 1, sqlFields.length()));
	              else
	                a.append(name).append(sqlFields);
	            }
	          }
	        }
	      }
	    }
	    else {
	    	sqlFields=sqlFields.trim();
	      indexAS = sqlFields.indexOf(" as ");
	      if (indexAS == 0) indexAS = sqlFields.indexOf(" AS ");
	      if (indexAS > -1) {
	        a.append(name).append(sqlFields.substring(indexAS + 4, sqlFields.length()));
	      } else {
	        indexSpace = sqlFields.lastIndexOf(" ");
	        if (indexSpace > -1) {
	          a.append(name).append(sqlFields.substring(indexSpace + 1, sqlFields.length()));
	        } else {
	          indexII = sqlFields.indexOf(".");
	          if (indexII > -1)
	            a.append(name).append(sqlFields.substring(indexII + 1, sqlFields.length()));
	          else {
	            a.append(name).append(sqlFields);
	          }
	        }
	      }
	    }
	    return a.toString();
	  }
  }