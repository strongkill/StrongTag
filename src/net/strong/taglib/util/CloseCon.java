package net.strong.taglib.util;

import java.sql.Connection;

import net.strong.util.Helper;

public class CloseCon {
  public static void Close(Connection con)
  {
	  Helper.cleanup(con);
  }
}