package net.strong.exutil;

/**
 * <p>Title:将16进制数转化为10进制的数 </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:  Strong Software International CO,.LTD </p>
 * @author Strong Yuan
 * @version 1.0
 */


public class HexConverter {
  public static void main(String[] args){

          //enter your code here
          HexConverter hexconverter = new HexConverter();
          String s = "1f";
          System.out.println(s + " = " + String.valueOf(scanhex(s)));

  }


  public static int scanhex(String s) {
          HexConverter converter = new HexConverter();
          return converter.getIntValue(s);
  }

  /**     Converts a hex string to an integer.
  *       Returns -1 if int limit exceeded.
  */
  public static int getIntValue(String hex){
          int value = 0;
          int power = 1;
          for (int i = hex.length()-1;i >= 0;i--){
            if (hexToInteger(hex.charAt(i)) != -1)
              value = value + power * hexToInteger(hex.charAt(i));
                  power = power *16;
          }
          return value;
  }

  /**     Converts a hex string to an integer.
  *       Returns -1 if int limit exceeded.
  */
  public static long getLongValue(String hex){
          long value = 0;
          int power = 1;
          for (int i = hex.length()-1;i >= 0;i--){
            if (hexToInteger(hex.charAt(i)) != -1)
              value = value + power * hexToInteger(hex.charAt(i));
                  power = power *16;
          }
          return value;
  }

  protected static boolean isAHexCharacter(char c){
          boolean status = false;
          String s = String.valueOf(c);
          s = s.toUpperCase();
          if ( ((c == '0') || (c == '1')) || ((c == '2') || (c  =='3')) ||
               ((c == '4') || (c == '5')) || ((c == '6') || (c  =='7')) ||
               ((c  =='8') || (c  =='9')) || ((c  =='a') || (c == 'A')) ||
               ((c == 'b') || (c == 'B')) || ((c == 'c') || (c == 'C')) ||
               ((c == 'd') || (c == 'D')) || ((c == 'e') || (c == 'E')) ||
               ((c == 'f') || (c  =='F')) ) status = true;

          return status;
  }

  /** Converts a hexadecimal character to its integer value.
  * Returns -1 if not a hex caharacter as in:
  * 0,1,2,3,4,5,6,7,8,9,A,a,B,b,C,c,D,d,E,e,F,f
  */
  protected static int hexToInteger(char c){
          int i = -1;
          if (c == '0') i = 0;
          if (c == '1') i = 1;
          if (c == '2') i = 2;
          if (c==  '3') i = 3;
          if (c == '4') i = 4;
          if (c == '5') i = 5;
          if (c == '6') i = 6;
          if (c == '7') i = 7;
          if (c == '8') i = 8;
          if (c == '9') i = 9;
          if (c == 'A') i = 10;
          if (c == 'a') i = 10;
          if (c == 'B') i = 11;
          if (c == 'b') i = 11;
          if (c == 'C') i = 12;
          if (c == 'c') i = 12;
          if (c == 'D') i = 13;
          if (c == 'd') i = 13;
          if (c == 'E') i = 14;
          if (c == 'e') i = 14;
          if (c == 'F') i = 15;
          if (c == 'f') i = 15;
          return i;
  }


}
