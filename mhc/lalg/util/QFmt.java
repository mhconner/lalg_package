package mhc.lalg.util;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * This class provides a number of static methods for simple formating tasks. Generally these
 * are more convenient to use that the Formating classes in java.text, but also less efficient.
 */
public class QFmt {
  
  private static DecimalFormat LongWithCommas = new DecimalFormat("###,###");
  
  /**
   * The set of characters that javaEncode will pass thru unmodified.
   */
  private final static String PLAIN_CHARS = " !#$%&'()*+,-./0123456789:;<=>?"
          + "@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_" + "`abcdefghijklmnopqrstuvwxyz{|}~";
  
  private QFmt() {
  }
  
  /**
   * Returns the arg string with escapeChar before each character in targetChars.
   */
  public static String addEscapes(String arg, String targetChars, char escapeChar) {
    if (arg == null)
      return "null";
    boolean escapes = false;
    int len = arg.length();
    StringBuffer buf = null;
    for (int i = 0; i < len; i++ ) {
      char c = arg.charAt(i);
      if (containsChar(targetChars, c)) {
        if ( !escapes) {
          escapes = true;
          buf = new StringBuffer(len + 5);
          buf.append(arg.substring(0, i));
        }
        buf.append(escapeChar);
      }
      if (escapes) {
        buf.append(c);
      }
    }
    if (escapes)
      return buf.toString();
    else
      return arg;
  }
  
  /**
   * Returns the unicode escape sequence for the specified character, for example, \\u0067 (but
   * with a single backslash)
   */
  public static String asUnicodeEscape(char c) {
    char[] buf = new char[4];
    for (int i = 3; i >= 0; i-- ) {
      int dig = c % 16;
      c /= 16;
      buf[i] = Character.forDigit(dig, 16);
    }
    return "\\u" + new String(buf);
  }
  
  /**
   * Equivalent to center(arg, fmt, width, false, ' ')
   */
  public static String center(double arg, String fmt, int width) {
    return center(arg, fmt, width, false, ' ');
  }
  
  /**
   * Equivalent to center(arg, fmt, width, truncate, ' ')
   */
  public static String center(double arg, String fmt, int width, boolean truncate) {
    return center(arg, fmt, width, truncate, ' ');
  }
  
  /**
   * Equivalent to center(QFmt.fmt(arg, fmt), width, truncate, fill)
   */
  public static String center(double arg, String fmt, int width, boolean truncate, char fill) {
    return center(fmt(arg, fmt), width, truncate, fill);
  }
  
  /**
   * Equivalent to center(arg, width, false, ' ', false)
   */
  public static String center(long arg, int width) {
    return center(arg, width, false, ' ', false);
  }
  
  /**
   * Equivalent to center(arg, width, false, ' ', false)
   */
  public static String center(long arg, int width, boolean commas) {
    return center(arg, width, false, ' ', commas);
  }
  
  /**
   * Equivalent to center(arg, width, truncate, fill, false)
   */
  public static String center(long arg, int width, boolean truncate, char fill) {
    return center(arg, width, false, ' ', false);
  }
  
  /**
   * Equivalent to center(String.valueOf(arg), width, truncate, fill), except that commas are
   * added every 3 digits if commas is true.
   */
  public static String center(long arg, int width, boolean truncate, char fill, boolean commas) {
    return center(fmt(arg, commas), width, false, ' ');
  }
  
  /**
   * Equivalent to center(arg, width, false, ' ')
   */
  public static String center(String arg, int width) {
    return center(arg, width, false, ' ');
  }
  
  /**
   * Returns a string of the indicated width (or longer) filled with the indicated fill
   * character with arg positioned in the center of the string. If truncate is true then the
   * argument string will be truncated if necessary so that the returned string is not longer
   * than width.
   */
  public static String center(String arg, int width, boolean truncate, char fill) {
    if (arg == null) {
      arg = "null";
    }
    int len = arg.length();
    if (width < 0) {
      width = len;
    }
    int fillLen = width - len;
    if (fillLen > 0) {
      int leftFillLen = (fillLen + 1) / 2;
      int rightFillLen = fillLen / 2;
      StringBuffer buf = new StringBuffer(width);
      for (int i = 0; i < leftFillLen; i++ ) {
        buf.append(fill);
      }
      buf.append(arg);
      for (int i = 0; i < rightFillLen; i++ ) {
        buf.append(fill);
      }
      return buf.toString();
    } else {
      if (truncate) {
        int leftTrunc = (len - width) / 2;
        return arg.substring(leftTrunc, width + leftTrunc);
      } else
        return arg;
    }
  }
  
  /**
   * Return true if the specified character is in the specified string.
   *
   * @param s the string
   * @param c the character
   * @return <i>true </i> if <i>c </i> is in <i>s </i> and <i>false </i> otherwise.
   */
  public static final boolean containsChar(String s, char c) {
    return -1 != s.indexOf(c);
  }
  
  /**
   * Formats a BigDecimal number into string.
   *
   * @param num the BigDecimal to be formatted
   * @param width the target width in characters of the resulting string, the number will not
   *          be truncated
   * @param after the number of digits to appear after the decimal point, if zero then no
   *          demimal point will be present, if -1 then the decimal point will allowed to
   *          float. If necessary, the number will be rounded using the
   *          MathContext.ROUND_HALF_UP algorithm.
   * @param commas if true then commas will be inserted between each three digits. The commas
   *          are part of the overall length of the resulting String (controled by
   *          <code>width</code>), but they are not considered part of the digits after the
   *          decimal point as controlled by <code>after</code>.
   */
  public static String fmt(BigDecimal num, int width, int after, boolean commas) {
    // String str = num.format(-1, after);
    // BigDecimal oldNum = num;
    if (width != -1) {
      num = num.setScale(after, RoundingMode.HALF_UP);
    }
    String str = num.toString();
    if (commas) {
      // Add commas
      StringBuffer buf = new StringBuffer(width);
      int len = str.length();
      int dPtPos = str.indexOf('.');
      if (dPtPos == -1) {
        dPtPos = len;
      }
      int commaOffset = dPtPos % 3;
      for (int i = 0; i < dPtPos; i++ ) {
        if (((i - commaOffset) % 3) == 0) {
          // check for digit to the left
          if ((i > 0) && Character.isDigit(str.charAt(i - 1))) {
            // add a comma
            buf.append(',');
          }
        }
        buf.append(str.charAt(i));
      }
      // copy the decimal point if one is there
      if (dPtPos < len) {
        buf.append(str.charAt(dPtPos));
        // add commas to the decimal part
        for (int i = dPtPos + 1; i < len; i++ ) {
          int offset = (i - dPtPos) - 1;
          if ((offset > 0) && ((offset % 3) == 0)) {
            buf.append(',');
          }
          buf.append(str.charAt(i));
        }
      }
      str = buf.toString();
    }
    return right(str, width);
  }
  
  private static String fmt(double d) {
    return fmt(d, "###,###.#####");
  }
  
  /**
   * Returns a string representation of the indicated argument using the DecimalFormat pattern.
   */
  public static String fmt(double arg, String pattern) {
    return (new DecimalFormat(pattern)).format(arg);
  }
  
  /**
   * Returns a string representation of the indicated argument.
   */
  public static String fmt(long arg) {
    return String.valueOf(arg);
  }
  
  /**
   * Formats the argument as a string with optional commas.
   *
   * @param arg
   * @param withCommas
   * @return a string which may have commas inserted
   */
  public static String fmt(long arg, boolean withCommas) {
    if (withCommas)
      return LongWithCommas.format(arg);
    else
      return String.valueOf(arg);
  }
  
  /**
   * Returns a string representation of the indicated argument using the DecimalFormat pattern.
   */
  public static String fmt(long arg, String pattern) {
    return (new DecimalFormat(pattern)).format(arg);
  }
  
  /**
   * Formats the number d milliseconds as a duration, picking a appropriate unit to make the
   * duration easy to understand. For example, the value .002 would be formated as 2
   * microseconds, and the value 10000.0 would be formatted at 10 seconds.
   *
   * @param d the duration in milliseconds to be formatted
   * @return a string representation of the duration with a convenient unit.
   */
  public static String fmtDuration(double d) {
    String result = "";
    if (d <= 0.001) {
      result = "" + fmt(d * 1000000.0) + " nanoseconds";
    } else if (inRange(d, 0.001, 1.0)) {
      result = "" + fmt(d * 1000.0) + " microseconds";
    } else if (inRange(d, 1.0, 1000.0)) {
      result = "" + fmt(d) + " milliseconds";
    } else {
      result = "" + fmt(d / 1000.0) + " seconds";
    }
    return result;
  }
  
  /**
   * Formats the number of milliseconds as a duration, picking a appropriate unit to make the
   * duration easy to understand. For example, the value .002 would be formated as 2
   * microseconds, and the value 10000.0 would be formatted at 10 seconds.
   * 
   * @param millis the duration in milliseconds
   * @param fmtSpec a format specification suitable to {@link String#format(String, Object...)}
   *          to format the more understandable number with.
   * @return a result like '5.6 seconds' or '3.45 minutes'
   */
  public static String fmtDuration(double millis, String fmtSpec) {
    String units;
    double duration;
    if (millis <= 0.001) {
      units = "nanoseconds";
      duration = millis * 1_000_000.0;
    } else if (inRange(millis, 0.001, 1.000)) {
      units = "microseconds";
      duration = millis * 1_000.0;
    } else if (inRange(millis, 1.0, 1000.0)) {
      units = "milliseconds";
      duration = millis;
    } else if (inRange(millis, 1000.0, 59_999.0)) {
      units = "seconds";
      duration = millis / 1_000.0;
    } else {
      units = "minutes";
      duration = millis / 60_000.0;
    }
    return String.format(fmtSpec, duration) + " " + units;
  }
  
  /**
   * Returns the duration specified in milliseconds as duration with unit value that is more
   * readable.
   * 
   * @param millis the duration to be formatted in milliseconds
   * @return the duration specified in milliseconds as duration with unit value that is more
   *         readable.
   */
  public static String fmtLongDuration(long millis) {
    int secondLimit = 1000;
    int minuteLimit = 60 * secondLimit;
    int hourLimit = 60 * minuteLimit;
    int dayLimit = 24 * hourLimit;
    int yearLimit = 365 * dayLimit;
    double duration = millis;
    String unit = "milliseconds";
    if (millis > yearLimit) {
      duration = duration / yearLimit;
      unit = "years";
    } else if (millis > dayLimit) {
      duration = duration / dayLimit;
      unit = "days";
    } else if (millis > hourLimit) {
      duration = duration / hourLimit;
      unit = "hours";
    } else if (millis > minuteLimit) {
      duration = duration / minuteLimit;
      unit = "minutes";
    } else if (millis > secondLimit) {
      duration = duration / secondLimit;
      unit = "seconds";
    }
    return "%.3f %s".formatted(duration, unit);
  }
  
  /**
   * Formats the specified positive number into a 4 character string showing its approximate
   * value using a suffix to indicate the magnitude of the number. As follows:
   * <ul>
   * <li>0-999 will appear as nnn
   * <li>1,000-999,999 will appear as nnnk where k means 1,000
   * <li>1,000,000-999,999,999 will appear as nnnm where m means 1,000,000
   * <li>1,000,000,000-999,999,999,999 will appear as nnnb where b means 1,000,000,000
   * <li>1,000,000,000,000-999,999,999,999,999 will appear as nnnt where t means 1,000,000,000
   * <li>1,000,000,000,000,000 and above will appear as ####
   * </ul>
   * In all cases the number will be rounded to the nearest magnitude as indicated by its
   * letter.
   *
   * @param val the number to be approximated.
   * @param pad if true then the result will be padded on the left with 0's, if false it be
   *          padded with blanks.
   * @return a four character string with 0 padding if requested
   */
  public static String fmtMagnitude(long val, boolean pad) {
    String result;
    if (val < 1000) {
      result = "" + val + " ";
    } else if (val < 1000000L) {
      result = "" + ((val + 500L) / 1000L) + "k";
    } else if (val < 1000000000L) {
      result = "" + ((val + 500000L) / 1000000L) + "m";
    } else if (val < 1000000000000L) {
      result = "" + ((val + 500000000L) / 1000000000L) + "b";
    } else if (val < 1000000000000000L) {
      result = "" + ((val + 500000000000L) / 1000000000000L) + "t";
    } else {
      result = "####";
    }
    if (pad)
      return right(result, 4, false, '0');
    else
      return right(result, 4);
  }
  
  private static boolean inRange(double value, double low, double high) {
    return (value > low) && (value <= high);
  }
  
  /**
   * Equivalent to {@link #javaEncode(String) javaEncode}(new String(arg)).
   */
  public static String javaEncode(char[] arg) {
    return javaEncode(new String(arg));
  }
  
  /**
   * Encodes the specified characterg into a form that could be used in ASCII Java source code
   * in a char literal. That is:
   * <ul>
   * <li>Line control characters are encoded as
   * <ul>
   * <li>newline - \n
   * <li>tab - \t
   * <li>return - \r
   * <li>formfeed - \f
   * <li>backspace - \b
   * </ul>
   * <li>Backslash is encoded as \\
   * <li>Double quote is encoded as \"
   * <li>Printable ascii characters other than backslash and double quote are not changed
   * <li>Space is not changed
   * <li>All other characters are converted to unicode escape sequences of the form \\uxxxx.
   * </ul>
   */
  public static String javaEncode(int val) {
    char c = (char) val;
    String result;
    if (containsChar(PLAIN_CHARS, c)) {
      result = "" + c;
    } else if (Character.isISOControl(c)) {
      if (c == '\n') {
        result = "\\n";
      } else if (c == '\t') {
        result = "\\t";
      } else if (c == '\r') {
        result = "\\r";
      } else if (c == '\f') {
        result = "\\f";
      } else if (c == '\b') {
        result = "\\b";
      } else {
        result = asUnicodeEscape(c);
      }
    } else if (c == '\\') {
      result = "\\\\";
    } else if (c == '"') {
      result = "\\\"";
    } else {
      result = asUnicodeEscape(c);
    }
    return result;
  }
  
  /**
   * Encodes the characters in the specified String into a form that could be used in ASCII
   * Java source code in a String literal. That is:
   * <ul>
   * <li>Line control characters are encoded as
   * <ul>
   * <li>newline - \n
   * <li>tab - \t
   * <li>return - \r
   * <li>formfeed - \f
   * <li>backspace - \b
   * </ul>
   * <li>Backslash is encoded as \\
   * <li>Double quote is encoded as \"
   * <li>Printable ascii characters other than backslash and double quote are not changed
   * <li>Space is not changed
   * <li>All other characters are converted to unicode escape sequences of the form \\uxxxx.
   * </ul>
   */
  public static String javaEncode(String arg) {
    if (arg == null)
      return "null";
    int len = arg.length();
    StringBuffer buf = new StringBuffer(len + 10);
    for (int i = 0; i < len; i++ ) {
      char c = arg.charAt(i);
      buf.append(javaEncode(c));
    }
    return buf.toString();
  }
  
  /**
   * Equivalent to left(arg, fmt, width, false, ' ')
   */
  public static String left(double arg, String fmt, int width) {
    return left(arg, fmt, width, false, ' ');
  }
  
  /**
   * Equivalent to left(arg, fmt, width, truncate, ' ')
   */
  public static String left(double arg, String fmt, int width, boolean truncate) {
    return left(arg, fmt, width, truncate, ' ');
  }
  
  /**
   * Equivalent to left(QFmt.fmt(arg, fmt), width, truncate, fill)
   */
  public static String left(double arg, String fmt, int width, boolean truncate, char fill) {
    return left(fmt(arg, fmt), width, truncate, fill);
  }
  
  /**
   * Equivalent to left(arg, width, false, ' ', false)
   */
  public static String left(long arg, int width) {
    return left(arg, width, false, ' ', false);
  }
  
  /**
   * Equivalent to left(arg, width, false, ' ', commas)
   */
  public static String left(long arg, int width, boolean commas) {
    return left(arg, width, false, ' ', commas);
  }
  
  /**
   * Equivalent to left(arg, width, truncate, fill, false)
   */
  public static String left(long arg, int width, boolean truncate, char fill) {
    return left(arg, width, truncate, fill, false);
  }
  
  /**
   * Equivalent to left(String.valueOf(arg), width, truncate, fill), except that true for
   * commas adds commas every three digits.
   */
  public static String left(long arg, int width, boolean truncate, char fill, boolean commas) {
    return left(fmt(arg, commas), width, truncate, fill);
  }
  
  /**
   * Equivalent to left(arg, width, false, ' ')
   */
  public static String left(String arg, int width) {
    return left(arg, width, false, ' ');
  }
  
  /**
   * Returns a string of the indicated width (or longer) filled with the indicated fill
   * character with arg positioned to the left of the string. If truncate is true then the
   * argument string will be truncated if necessary so that the returned string is not longer
   * than width.
   */
  public static String left(String arg, int width, boolean truncate, char fill) {
    if (arg == null) {
      arg = "null";
    }
    int len = arg.length();
    if (width < 0) {
      width = len;
    }
    int fillLen = width - len;
    if (fillLen > 0) {
      StringBuffer buf = new StringBuffer(width);
      buf.append(arg);
      for (int i = 0; i < fillLen; i++ ) {
        buf.append(fill);
      }
      return buf.toString();
    } else {
      if (truncate)
        return arg.substring(0, width);
      else
        return arg;
    }
  }
  
  public static PrintWriter newBuf() {
    CharArrayWriter outBuf = new CharArrayWriter();
    PrintWriter out = new PrintWriter(outBuf, true);
    return out;
  }
  
  /**
   * Quick quote: equivalent to quote(arg, '"', "\"", '\\')
   */
  public static String qquote(String arg) {
    return quote(arg, '"', "\"", '\\');
  }
  
  /**
   * Equivalent to calling {@link #quoteIfNeeded(String, char, String, char) quoteIfNeeded}
   * (arg, '"', ",", '\\').
   *
   * @param arg the String to be quoted if needed.
   * @return The original String with quotes and escapes only if needed.
   */
  public static String qquoteIfNeeded(String arg) {
    return quoteIfNeeded(arg, '"', ",", '\\');
  }
  
  /**
   * Wraps the argument string in quotes and escapes any characters in the argument string as
   * necessary, see {@link #addEscapes(String, String,char) addEscapes}.
   */
  public static String quote(String arg, char quoteChar, String targetChars, char escapeChar) {
    return quoteChar + addEscapes(arg, targetChars, escapeChar) + quoteChar;
  }
  
  /**
   * Quotes the specified String if it contains any of the characters in targetChars. If the
   * quote character occurs anywhere in the String it will be escaped with the specified escape
   * character whether or not the string needs to be quoted. The design of the algorithm is
   * optomized for seldom needing to quote or escape, in which case the original String is just
   * returned.
   *
   * @param arg The String to be quoted if needed.
   * @param quoteChar The character to use before and after the String if quoting is needed.
   * @param targetChars The characters to look for. If any occur in the String it must be
   *          quoted.
   * @param escapeChar The character to use escape the quote character if necessary.
   * @return The original String with quotes and escapes only if needed.
   */
  public static String quoteIfNeeded(String arg,
          char quoteChar,
          String targetChars,
          char escapeChar) {
    boolean escapes = false;
    int len = arg.length();
    StringBuffer buf = null;
    boolean quoteNeeded = false;
    for (int i = 0; i < len; i++ ) {
      char c = arg.charAt(i);
      if (containsChar(targetChars, c)) {
        quoteNeeded = true;
      }
      if (c == quoteChar) {
        if ( !escapes) {
          // This is the first char needing escapes so initialize buf
          escapes = true;
          buf = new StringBuffer(len + 5);
          buf.append(arg.substring(0, i));
        }
        buf.append(escapeChar);
      }
      if (escapes) {
        // only copy if an escape has occured and buf is initialized
        buf.append(c);
      }
    }
    if (escapes && quoteNeeded)
      return ((buf.insert(0, quoteChar)).append(quoteChar)).toString();
    else if (quoteNeeded)
      return quoteChar + arg + quoteChar;
    else if (escapes)
      return buf.toString();
    else
      return arg;
  }
  
  /**
   * Equivalent to right(arg, fmt, width, false, ' ')
   */
  public static String right(double arg, String fmt, int width) {
    return right(arg, fmt, width, false, ' ');
  }
  
  /**
   * Equivalent to right(arg, fmt, width, truncate, ' ')
   */
  public static String right(double arg, String fmt, int width, boolean truncate) {
    return right(arg, fmt, width, truncate, ' ');
  }
  
  /**
   * Equivalent to right(QFmt.fmt(arg, fmt), width, truncate, fill)
   */
  public static String right(double arg, String fmt, int width, boolean truncate, char fill) {
    return right(fmt(arg, fmt), width, truncate, fill);
  }
  
  /**
   * Equivalent to right(arg, width, false, ' ', false)
   */
  public static String right(long arg, int width) {
    return right(arg, width, false, ' ', false);
  }
  
  /**
   * Equivalent to right(arg, width, false, ' ', commas)
   */
  public static String right(long arg, int width, boolean commas) {
    return right(arg, width, false, ' ', commas);
  }
  
  /**
   * Equivalent to right(arg, width, truncate, fill, false)
   */
  public static String right(long arg, int width, boolean truncate, char fill) {
    return right(arg, width, false, fill, false);
  }
  
  /**
   * Equivalent to right(String.valueOf(arg), width, truncate, fill)
   */
  public static String right(long arg, int width, boolean truncate, char fill, boolean commas) {
    return right(fmt(arg, commas), width, false, fill);
  }
  
  /**
   * Equivalent to right(arg, width, false, ' ')
   */
  public static String right(String arg, int width) {
    return right(arg, width, false, ' ');
  }
  
  /**
   * Returns a string of the indicated width (or longer) filled with the indicated fill
   * character with arg positioned to the right of the string. If truncate is true then the
   * argument string will be truncated if necessary so that the returned string is not longer
   * than width.
   */
  public static String right(String arg, int width, boolean truncate, char fill) {
    if (arg == null) {
      arg = "null";
    }
    int len = arg.length();
    if (width < 0) {
      width = len;
    }
    int fillLen = width - len;
    if (fillLen > 0) {
      StringBuffer buf = new StringBuffer(width);
      for (int i = 0; i < fillLen; i++ ) {
        buf.append(fill);
      }
      buf.append(arg);
      return buf.toString();
    } else {
      if (truncate)
        return arg.substring(len - width);
      else
        return arg;
    }
  }
  
  /**
   * Returns a string representing the value of indicated number of low-order bits in the
   * argument formated for ease of analysis.
   *
   * @param val the int to be converted.
   * @param num the number of low order bits to show, this will be rounded up to an even
   *          multiple of 4.
   * @return a string of the indicated number of zeros and ones.
   */
  public static String showHex(long val, int num) {
    num = (num + 3) / 4; // number of hex digits to show
    StringBuilder buf = new StringBuilder(num + (num / 2) + 1);
    String hexDigits = toHex(val, num);
    int offset = 16 - num;
    for (int i = 0; i < num; i++ ) {
      buf.append(hexDigits.charAt(i));
      offset++ ;
      if (offset < 16) {
        if ((offset % 8) == 0) {
          buf.append('|');
        } else if ((offset % 4) == 0) {
          buf.append('_');
        } else if ((offset % 2) == 0) {
          buf.append('.');
        }
      }
    }
    return buf.toString();
  }
  
  /**
   * Formats an object in the form needed for a value in a SQL statement. In particular if the
   * object is a string it will be wrapped with single quotes.
   */
  public static String SQL(Object obj) {
    if (obj == null)
      return "null";
    else if (obj instanceof String)
      return "'" + addEscapes((String) obj, "'", '\'') + "'";
    else
      return obj.toString();
  }
  
  /**
   * Converts value into an upper case Hex representation. E.g., 30 becomes "1E", 127 becomes
   * "FF", 1024 becomes "0400"
   */
  public static String toHex(long value, int width) {
    char[] buf = new char[16];
    char[] rep = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };
    for (int i = 15; i >= 0; i-- ) {
      int nibble = (int) value & 0xF;
      buf[i] = rep[nibble];
      value = value >>> 4;
    }
    return new String(buf, 16 - width, width);
  }
  
  /**
   * Simple version of
   * {@link #wrap(String, int, String, String, String, String, boolean, boolean)} that does
   * what you want most of the time.
   *
   * @param arg the String to be wrapped
   * @param width the longest non-separated string to be generated
   * @return the argument string broken in the lines
   */
  public static String wrap(String arg, int width) {
    return wrap(arg, width, " :,.!\t", "\n", "", "", false, false);
  }
  
  /**
   * Returns the input argument string with the indicated separator string inserted as
   * necessary to insure that each non-separated string is less than or equal to the indicated
   * width. The separators will be inserted right after one of the breakChars. Note: if the
   * breakChars do not occur frequently enough then a non-separated string may exceed the
   * indicated width.
   *
   * @param str the String to be wrapped
   * @param width the longest non-separated string to be generated
   * @param breakChars a String of characters that can be followed by a separator
   * @param separator a String to be used to separate parts of the argument String.
   * @param escapes a String of characters that indicate that the next character is not to be
   *          treated as a breakCharacter
   * @param quotes a String of characters that can be used in pairs to indicated that
   *          breakCharacters are not to be recognized.
   * @param stripEscapes a boolean that indicates if escape characters should be removed, if
   *          they are removed then they are not counted in the length of a string section,
   *          otherwise they are.
   * @param stripQuotes a boolean that indicates if quote characters should be removed, if they
   *          are removed then they are not counted in the length of a string section,
   *          otherwise they are.
   */
  public static String wrap(String str,
          int width,
          String breakChars,
          String separator,
          String escapes,
          String quotes,
          boolean stripEscapes,
          boolean stripQuotes) {
    StringBuffer buf = new StringBuffer(str.length() + 100);
    int sectionLength = 0;
    Tokenizer toker = new Tokenizer(breakChars, false, "", quotes, escapes, stripEscapes,
            stripQuotes);
    toker.setSourceString(str);
    while (toker.hasNext()) {
      String fragment = toker.next();
      int fragmentLength = fragment.length();
      if ((fragmentLength + sectionLength) > width) {
        // insert a separator unless this is the first token in this section
        if (sectionLength > 0) {
          buf.append(separator);
          sectionLength = 0;
        }
      }
      buf.append(fragment);
      sectionLength += fragmentLength;
    }
    return buf.toString();
  }
  
  /**
   * Wraps the specified string at limit, indenting each line after the first with 'indent'
   * spaces.
   *
   * @param str the string to wrap and indent
   * @param limit the limit on the length of a line
   * @param indent the indention to use after the first line
   */
  public static String wrapIndent(String str, int limit, int indent) {
    String indentStr = ("\n                                                             "
            + "                                               ").substring(0, indent + 1);
    return wrap(str, limit, " :,.!\t", indentStr, "", "", false, false);
  }
} // end of class
