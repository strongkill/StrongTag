package net.strong;

import java.io.*;
import java.util.Stack;

/**
 * <p>Title: Test_Porject</p>
 * <p>Description: Test_Project</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Strong Software CO,Ltd.</p>
 * @author Strong Yuan
 * @version 1.0
 */

public class FileConvert {

  public FileConvert() {
  }
  /**
   *
   * @param args
   */
  public static void main(String[] args) {
//    JDirectory jd = new JDirectory("c:\\test",null);
//    if (args == null || args.length<3)
//      return;
//    FileConvert(args[0],args[1],args[2],args[3]);
  //JDirectory jd =new JDirectory("D:\\workspace\\java_class\\src\\",null);
	  FileEncodeConvert("D:\\workspace\\java_class\\src\\com\\\\database\\PoolBean.java","D:\\workspace\\java_class\\src\\com\\\\database\\PoolBean.java.1","gb2312","utf-8");
  }

  public static boolean FileEncodeConvert(String filename, String newfile,
                                    String src_char, String dec_char) {
    boolean isConvert = false;
    try {
      File f = new File(filename);
      if (f.exists()) {
        return FileEncodeConvert(f, filename, src_char, dec_char);
      }
    }
    catch (Exception e) {
    }
    return isConvert;
  }

  public static boolean FileEncodeConvert(File f, String newfile,
                                    String src_char, String dec_char) {
    boolean isConvert = false;
    try {
        InputStream fi = new FileInputStream(f);
        InputStreamReader in = new InputStreamReader(fi, src_char);
        OutputStream fo = new FileOutputStream(newfile+".1");
        OutputStreamWriter fw = new OutputStreamWriter(fo, dec_char);
        int len = -1;
        char[] buf = new char[512];
        while ((len = in.read(buf)) != -1)
          fw.write(buf,0,len);

        fw.flush();
        fw.close();
        fw = null;
        fo.close();
        fo = null;
        in.close();
        in = null;
        fi.close();
        fi = null;
        f = null;
        isConvert = true;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      isConvert = false;
    }
    return isConvert;
  }

}


class JDirectory {

  private File dir;
  private Stack<File> fileStack;
  private Stack<File> dirStack;
  private int fileCount;
  private int dirCount;
  private String[] filter;

  /**
   * push all files and dirs into the stacks when construct
   * and initialize all the fields<br>
   * you can also input a filter (not necessary)
   * @param dir String
   */
  public JDirectory(String dir, String[] filter) {
    this(new File(dir), filter);
  }

  public JDirectory(File dir, String[] filter) {
    if (dir == null || !dir.exists() || dir.isFile()) {
      System.out.println("a null reference or not exist or not a directory!");
      return;
    }
    this.dir = dir;
    reset(filter);
  }

  /**
   * you can use reset() to reinitialize *all* the fields
   * including the two stacks.<p>
   * you can also input a filter (not necessary)<br>
   * use it when you have pushed some files and want to traverse again.<br>
   * or you want to change your filter
   */
  public void reset(String[] filter) {
    this.filter = filter;
    fileStack = new Stack<File>();
    dirStack = new Stack<File>();
    int n = dir.listFiles().length;
    for (int i = 0; i < n; i++) {
      spider(dir.listFiles()[i]);
    }
    fileCount = fileStack.size();
    dirCount = dirStack.size();
    makeDir(fileStack);
  }

  public void makeDir(Stack dir) {
    //DataInput a;
    while (dir.size() > 0 && dir.get(0) != null) {
      File dirs = (File) dir.remove(0);
      FileConvert.FileEncodeConvert(dirs,dirs.getPath(),"gb2312","utf-8");
    }
  }

  /**
   * use a spider to traverse all the files<p>
   * feed the spider a File reference,if it's a file,push it,
   * if it's a directory, give all files/directorys under this directory
   * to new spiders.<br>
   * based on the Depth-First-Search
   * @param f File
   */
  private void spider(File f) {
    if (f.isFile()) {
      if (filter == null || filter.length == 0) {
        fileStack.push(f);
      } else {
        int i = filter.length - 1;
        while (i >= 0 /*&& !filter[i].equals(JFile.getSuffix(f))*/) {
          i--;
        }
        if (i > -1) {
          fileStack.push(f);
        }
      }
    } else {
      dirStack.push(f);
      int n = f.listFiles().length;
      for (int i = 0; i < n; i++) {
        spider(f.listFiles()[i]);
      }
    }
  }

  /**
   * return the File which is given when constructing
   * @return File
   */
  public File getDirSelf() {
    return dir;
  }

  public void printInfo() {
    //JFile.fileInfo(dir);
    System.out.println("Files count:\t" + fileCount);
    System.out.println("Dirs count:\t" + dirCount);
  }

  /**
   * get the next file in the file stack,
   * just like a read() method in a IOStream.
   * @return File
   */
  public File nextFile() {
    return (File) fileStack.pop();
  }

  /**
   * the number of files still in stack
   * @return int
   */
  public int fileRemain() {
    return fileStack.size();
  }

  public File nextDir() {
    return (File) dirStack.pop();
  }

  public int dirRemain() {
    return dirStack.size();
  }

  /**
   * the number of all files in the directory
   * @return int
   */
  public int fileCount() {
    return fileCount;
  }

  public int dirCount() {
    return dirCount;
  }
}