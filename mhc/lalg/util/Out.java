package mhc.lalg.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This is a convenience class that makes simple output in test programs and such, easier by
 * providing static methods for most of the methods defined by {@link PrintWriter}. These
 * methods write to a shared, auto-flushing {@link PrintWriter} held in the public static
 * variable {@link #out} which is initialized on first use. This {@link PrintWriter} writes to
 * {@link System#out}. For example: calling {@code Out.printf("%d",5); } is equivalent to
 * calling {@code Out.out.printf("%d",5);} assuming out has been initialized. You can force
 * {@link #out} to be initialized by calling {@link #checkOut()} but if you only call the
 * static methods you don't need to do this as it will be done automatically.
 * <p>
 * You can redirect the output to a file by calling {@link #setPrintWriter(String)}.
 *
 * @author Mike Conner
 * @version Jul 12, 2008
 */
public class Out {
  
  public static int outLevel = 0;
  
  /**
   * This is used in {@link #formatI(String, Object...)} and
   * {@link #formatDI(boolean, String, Object...)} for breaking long lines.
   */
  private static int indentionLimit = 90;
  
  /**
   * This is the shared, auto-flushing {@link PrintWriter} used by all the output methods in
   * this class. It is only initialized on first use or by an explicit call to
   * {@link #checkOut()}.
   */
  private static PrintWriter out;
  
  /**
   * 
   */
  private static boolean developmentMode;
  
  /**
   * If not null then {@link #out} is has been set to output to this FileWriter, if null then
   * out will use {@link System#out}.
   */
  private static FileWriter outFile = null;
  
  /**
   * Prevent instances from being created.
   */
  protected Out() {
  }
  
  /**
   * Just like {@link #format(String, Object...)} except that output only occurs if the
   * specified condition is true.
   * 
   * @param condition controls the output
   * @param format the format to use
   * @param args the arguments to format
   * @return the shared PrintWriter, {@link #out} which may not have been initialized unless
   *         the condition was true.
   * @see java.io.PrintWriter#format(java.lang.String, java.lang.Object[])
   */
  public static synchronized PrintWriter cformat(boolean condition, String format, Object... args) {
    if (condition) {
      format(format, args);
    }
    return getOut();
  }
  
  /**
   * Insure that {@link #out} is set. If not it is set to {@link System#out}.
   */
  public static synchronized void checkOut() {
    if (out == null) {
      out = new PrintWriter(System.out, true);
    }
  }
  
  /**
   * Closes the inclosed PrintWriter, it will be reopened if another method is called that
   * needs it.
   *
   * @see java.io.PrintWriter#close()
   */
  public static synchronized void close() {
    getOut().close();
    out = null;
  }
  
  /**
   * @see java.io.PrintWriter#flush()
   */
  public static synchronized void flush() {
    getOut().flush();
  }
  
  /**
   * @see java.io.PrintWriter#format(java.lang.String, java.lang.Object[])
   */
  public static synchronized PrintWriter format(String format, Object... args) {
    getOut().format(format, args);
    getOut().flush();
    return getOut();
  }
  
  /**
   * Just like {@link #format(String, Object...)} except that output only occurs if
   * {@link #isDevelopmentMode()} is true.
   * 
   * @param enable if false then no output will be produced even if
   *          {@link #isDevelopmentMode()} is true. This allows manual adjust of which
   *          statements will produce output.
   * @param format the format to use
   * @param args the arguments to format
   * @return the shared PrintWriter, {@link #out} which may not have been initialized unless
   *         the condition was true.
   * @see java.io.PrintWriter#format(java.lang.String, java.lang.Object[])
   */
  public static synchronized PrintWriter formatD(boolean enable, String format, Object... args) {
    if (enable && isDevelopmentMode()) {
      format(format, args);
    }
    return getOut();
  }
  
  /**
   * Just like {@link #format(String, Object...)} except that output only occurs if
   * {@link #isDevelopmentMode()} is true and lines are broken with indention.
   * 
   * @param enable if false then no output will be produced even if
   *          {@link #isDevelopmentMode()} is true. This allows manual adjust of which
   *          statements will produce output.
   * @param format the format to use
   * @param args the arguments to format
   * @return the shared PrintWriter, {@link #out} which may not have been initialized unless
   *         the condition was true.
   * @see java.io.PrintWriter#format(java.lang.String, java.lang.Object[])
   */
  public static synchronized PrintWriter formatDI(boolean enable, String format, Object... args) {
    if (enable && isDevelopmentMode()) {
      formatI(format, args);
    }
    return getOut();
  }
  
  /**
   * Just like {@link #format(String, Object...)} except that lines are broken with indention.
   * 
   * @param format the format to use
   * @param args the arguments to format
   * @return the shared PrintWriter, {@link #out} which may not have been initialized unless
   *         the condition was true.
   * @see java.io.PrintWriter#format(java.lang.String, java.lang.Object[])
   */
  public static synchronized PrintWriter formatI(String format, Object... args) {
    int indent = 6;
    String prefix = "                       ".substring(0, indent);
    String str = format.formatted(args);
    String lines[] = str.split("[\n\r]+");
    for (int i = 0; i < lines.length; i++ ) {
      String line = lines[i];
      if (i < 1) {
        line = "%s%n".formatted(line);
      } else {
        line = "%s%s%n".formatted(prefix, line);
      }
      String bLine = QFmt.wrapIndent(line, getIndentionLimit(), indent);
      print(bLine);
    }
    flush();
    // getOut().format(format, args);
    return getOut();
  }
  
  /**
   * Returns a string with the method's thread name, class name, name, and line number of the
   * indicated call point by going back in the stack trace to find the stack frame of the
   * caller. Where
   * <ul>
   * <li>2 - the caller of this method
   * <li>3 - the caller of the caller of this method -- used by
   * {@link #trace(boolean, String, Object...)}.
   * <li>4 - the caller of the caller of the caller this method -- used by
   * {@link #showCallPoint()} and {@link #showCallPoint(String)}.
   * <li>5 - the caller of the caller of the caller of the of the caller this method
   * </ul>
   * 
   * @param level how far to go back in the stack frames.
   * @return Returns a string with the method's thread name, class name, name, and line number
   *         of the indicated call point
   */
  public static String getCallPointInfo(int level) {
    Thread thisThread = Thread.currentThread();
    StackTraceElement[] stkTrace = thisThread.getStackTrace();
    String tName = thisThread.getName();
    // showStkTrace(stkTrace);
    StackTraceElement frame = stkTrace[level];
    String clsName = frame.getClassName();
    int prefixLen = clsName.lastIndexOf('.') + 1;
    clsName = clsName.substring(prefixLen);
    int lineNum = frame.getLineNumber();
    String methodName = frame.getMethodName();
    return "<%s:%s.%s[%03d]>".formatted(tName, clsName, methodName, lineNum);
  }
  
  /**
   * Returns the {@link #indentionLimit} for {@link Out}, this is used in
   * {@link #formatI(String, Object...)} and {@link #formatDI(boolean, String, Object...)} for
   * breaking long lines.
   * 
   * @return {@link #indentionLimit}.
   */
  public static int getIndentionLimit() {
    return indentionLimit;
  }
  
  /**
   * Returns the out of this Out.
   *
   * @return Returns {@link #out}.
   */
  public static synchronized PrintWriter getOut() {
    checkOut();
    return out;
  }
  
  /**
   * Returns true if development mode is set and false otherwise.
   * {@link #formatDI(boolean, String, Object...)} will only produce output when
   * {@link #isDevelopmentMode()} is true.
   * 
   * @return {@link #developmentMode}.
   */
  public static synchronized boolean isDevelopmentMode() {
    return developmentMode;
  }
  
  /**
   * @see java.io.PrintWriter#print(boolean)
   */
  public static synchronized void print(boolean b) {
    getOut().print(b);
  }
  
  /**
   * @see java.io.PrintWriter#print(char)
   */
  public static synchronized void print(char c) {
    getOut().print(c);
  }
  
  /**
   * @see java.io.PrintWriter#print(char[])
   */
  public static synchronized void print(char[] s) {
    getOut().print(s);
  }
  
  /**
   * @see java.io.PrintWriter#print(double)
   */
  public static synchronized void print(double d) {
    getOut().print(d);
  }
  
  /**
   * @see java.io.PrintWriter#print(float)
   */
  public static synchronized void print(float f) {
    getOut().print(f);
  }
  
  /**
   * @see java.io.PrintWriter#print(int)
   */
  public static synchronized void print(int i) {
    getOut().print(i);
  }
  
  /**
   * @see java.io.PrintWriter#print(long)
   */
  public static synchronized void print(long l) {
    getOut().print(l);
  }
  
  /**
   * @see java.io.PrintWriter#print(java.lang.Object)
   */
  public static synchronized void print(Object obj) {
    getOut().print(obj);
  }
  
  /**
   * @see java.io.PrintWriter#print(java.lang.String)
   */
  public static synchronized void print(String s) {
    getOut().print(s);
  }
  
  /**
   * @see java.io.PrintWriter#printf(java.lang.String, java.lang.Object[])
   */
  public static synchronized PrintWriter printf(String format, Object... args) {
    return getOut().printf(format, args);
  }
  
  /**
   * @see java.io.PrintWriter#println()
   */
  public static synchronized void println() {
    getOut().println();
  }
  
  // /**
  // * @see java.io.PrintWriter#printf(java.util.Locale, java.lang.String, java.lang.Object[])
  // */
  // public static PrintWriter printf(Locale l, String format, Object... args) {
  //
  // return getOut().printf(l, format, args);
  // }
  /**
   * @see java.io.PrintWriter#println(boolean)
   */
  public static synchronized void println(boolean x) {
    getOut().println(x);
  }
  
  /**
   * @see java.io.PrintWriter#println(char)
   */
  public static synchronized void println(char x) {
    getOut().println(x);
  }
  
  /**
   * @see java.io.PrintWriter#println(char[])
   */
  public static synchronized void println(char[] x) {
    getOut().println(x);
  }
  
  /**
   * @see java.io.PrintWriter#println(double)
   */
  public static synchronized void println(double x) {
    getOut().println(x);
  }
  
  /**
   * @see java.io.PrintWriter#println(float)
   */
  public static synchronized void println(float x) {
    getOut().println(x);
  }
  
  /**
   * @see java.io.PrintWriter#println(int)
   */
  public static synchronized void println(int x) {
    getOut().println(x);
  }
  
  /**
   * @see java.io.PrintWriter#println(long)
   */
  public static synchronized void println(long x) {
    getOut().println(x);
  }
  
  /**
   * @see java.io.PrintWriter#println(java.lang.Object)
   */
  public static synchronized void println(Object x) {
    getOut().println(x);
  }
  
  /**
   * @see java.io.PrintWriter#println(java.lang.String)
   */
  public static synchronized void println(String x) {
    getOut().println(x);
  }
  
  /**
   * Reset (or sets) the output PrintWriter ({@link #out} to be {@link System#out}. If
   * {@link #out} was directed to a file the file will be closed.
   *
   * @return the previous PrintWriter or null if there was not one.
   */
  public static synchronized PrintWriter reset() {
    PrintWriter result = getOut();
    if (outFile != null) {
      getOut().close();
    }
    out = null;
    return result;
  }
  
  /**
   * Controls the output of {@link #formatDI(boolean, String, Object...)}. Output will only
   * occur in {@link #isDevelopmentMode()} is true.
   * 
   * @param developmentMode The value to set development mode to.
   */
  public static synchronized void setDevelopmentMode(boolean developmentMode) {
    Out.developmentMode = developmentMode;
  }
  
  /**
   * Sets the {@link #indentionLimit} for {@link Out}, this is used in
   * {@link #formatI(String, Object...)} and {@link #formatDI(boolean, String, Object...)} for
   * breaking long lines.
   * 
   * @param indentionLimit The value to set indentionLimit to.
   */
  public static void setIndentionLimit(int indentionLimit) {
    Out.indentionLimit = indentionLimit;
  }
  
  /**
   * Allows output to be redirected to a file. This can be called at any time and all
   * subsequent output will be the specified file.
   *
   * @param filename the file to redirect output to
   * @return the previous value of {@link #out}.
   */
  public static synchronized PrintWriter setPrintWriter(String filename) {
    try {
      outFile = new FileWriter(filename);
      out = new PrintWriter(outFile, true);
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      throw new Error();
    }
  }
  
  /**
   * Produces formated output if the current setting of {@link #outLevel} is greater than or
   * equal to the specified output level. There are 4 output levels, as follows:
   * <ol>
   * <li>Always produce output unless trace output is turned off (outLevel set to 0). This
   * level is for summary output of major steps such as a completed test.
   * <li>This level is for summary output of major steps, that does not need to be seen once
   * confidence in the implementation is high.
   * <li>This level is for summary output of minor steps that help in diagnosing problems.
   * <li>This level is for summary output of minor steps inside of loops that might help in
   * diagnosing problems.
   * </ol>
   *
   * @param outputLevel the output level (see method description) for this output.
   * @param format a format control string, see {@link java.util.Formatter} for details.
   * @param args the arguments to be output under control of the format string.
   */
  public static synchronized void show(int outputLevel, String format, Object... args) {
    if (outputLevel <= outLevel) {
      Out.format(format, args);
    }
  }
  
  /**
   * If {@link #isDevelopmentMode()} is true then output will be produced to show where this
   * method was called from.
   */
  public static void showCallPoint() {
    if (isDevelopmentMode()) {
      Out.formatD(true, "%s", getCallPointInfo(4));
    }
  }
  
  /**
   * If {@link #isDevelopmentMode()} is true then output will be produced to show where this
   * method was called from followed by the specified message.
   * 
   * @param msg
   */
  public static void showCallPoint(String msg) {
    if (isDevelopmentMode()) {
      Out.formatD(true, "----- %s ------ %s%n", getCallPointInfo(4), msg);
    }
  }
  
  /**
   * Just like {@link #formatDI(boolean, String, Object...)} except that call point information
   * will be prefixed to any output.
   * 
   * @param enable if false then no output will be produced even if
   *          {@link #isDevelopmentMode()} is true. This allows manual adjust of which
   *          statements will produce output.
   * @param format the format to use
   * @param args the arguments to format
   * @return the shared PrintWriter, {@link #getOut()}.
   * @see java.io.PrintWriter#format(java.lang.String, java.lang.Object[])
   */
  public static synchronized PrintWriter trace(boolean enable, String format, Object... args) {
    if (enable) {
      String callerInfo = "----- %s ---- trace output:%n".formatted(getCallPointInfo(3));
      return formatDI(enable, callerInfo + format, args);
    } else
      return getOut();
  }
}
