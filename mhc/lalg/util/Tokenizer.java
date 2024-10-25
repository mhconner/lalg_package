package mhc.lalg.util;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Tokenizer is a class that tokenizes simple character strings containing delimiters. Unlike
 * java.util.StringTokenizer this class supports escapes and quotations.
 */
public class Tokenizer implements Iterator<String> {
  
  /**
   * The set of characters that delimit tokens, default value is comma. A token is defined to
   * be all the characters between two delimitors minus in leading or trailing skip characters.
   * The begining of a string and the end of the string are always treated as delimitors.
   */
  public String delimiters = ",";
  
  /**
   * If true (the default) then delimiters will be removed from the returned token. If
   * delimiters are not removed then trailing skips cannot be removed.
   */
  public boolean removeDelimiters = true;
  
  /**
   * The set of characters than can be used to escape parts of a token, default value is
   * backslash. An escape character causes the next character (which may be any character) to
   * be treated as a regular character.
   */
  public String escapes = "\\";
  
  /**
   * The set of characters that can be used to quote tokens or parts of tokens, default value
   * is single and double quote characters. Skip and delimiter characters will be treated as
   * regular characters if they are quoted. Quote characters must occur in pairs, they quote
   * the characters that occure after the first member of the pair and before the second.
   */
  public String quotes = "\"'";
  
  /**
   * The set of characters to skip if they are at the start or end of a token, default value is
   * space and tab.
   */
  public String skips = " \t";
  
  /**
   * The set of characters to compress when they ocurr in groups, default value is space and
   * tab. They will always be compressed (when compress in turned on) to one occurance of the
   * first of the fills characters.
   */
  public String fills = " \t";
  
  /**
   * Controls the striping of quotations characters, default is true. If true then they are
   * striped and if false they are kept and returned as part of a token.
   */
  public boolean stripQuotes = true;
  
  /**
   * Controls the stripping of escape characters, default is true. If true then they are
   * striped and if false they are kept and returned as part of a token.
   */
  public boolean stripEscapes = true;
  
  /**
   * Controls the compression of fill characters, default is true. If true then any sequence of
   * more than one of any combination of the fill characters will be compressed to a single
   * occurance of the first fill character.
   */
  public boolean compressFill = true;
  
  /**
   * The string that is being tokenized.
   */
  protected String source;
  
  /**
   * The next character to be examined in <i>source </i>.
   */
  protected int position;
  
  /**
   * Create a new Tokenizer with default controls.
   * <ul>
   * <li>delimiters=",",
   * <li>skips=" \t",
   * <li>quotes="\"'",
   * <li>escapes= "\\",
   * <li>fill=" \t"
   * <li>stripEscapes = true,
   * <li>stripQuotes = true, and
   * <li>compressFill = true
   * </ul>
   */
  public Tokenizer() {
  }
  
  /**
   * Create a new Tokenizer with the specified controls, and default fill characterists.
   */
  public Tokenizer(String delimiters, boolean removeDelimiters, String skips, String quotes,
          String escapes, boolean stripEscapes, boolean stripQuotes) {
    this.delimiters = delimiters;
    this.removeDelimiters = removeDelimiters;
    this.skips = skips;
    this.quotes = quotes;
    this.escapes = escapes;
    this.stripEscapes = stripEscapes;
    this.stripQuotes = stripQuotes;
  }
  
  /**
   * Create a new Tokenizer with the specified controls, and default fill characterists.
   */
  public Tokenizer(String delimiters, String skips, String quotes, String escapes,
          boolean stripEscapes, boolean stripQuotes) {
    this.delimiters = delimiters;
    this.skips = skips;
    this.quotes = quotes;
    this.escapes = escapes;
    this.stripEscapes = stripEscapes;
    this.stripQuotes = stripQuotes;
  }
  
  /**
   * Create a new Tokenizer with the specified controls.
   */
  public Tokenizer(String delimiters, String skips, String fills, String quotes, String escapes,
          boolean stripEscapes, boolean stripQuotes, boolean compressFill) {
    this.delimiters = delimiters;
    this.skips = skips;
    this.fills = fills;
    this.quotes = quotes;
    this.escapes = escapes;
    this.stripEscapes = stripEscapes;
    this.stripQuotes = stripQuotes;
    this.compressFill = compressFill;
  }
  
  /**
   * Return true if the specified character is in the specified string.
   * 
   * @param s the string
   * @param c the character
   * @return <i>true </i> if <i>c </i> is in <i>s </i> and <i>false </i> otherwise.
   */
  protected final boolean containsChar(String s, char c) {
    return -1 != s.indexOf(c);
  }
  
  /**
   * Returns the current character in the source, does not advance the position.
   */
  protected final char curChar() {
    return source.charAt(position);
  }
  
  /**
   * Returns a list of all the tokens in the specified source String.
   * 
   * @param sourceStr a String containing tokens.
   * @return an ArrayList with all the tokens from the specified source in the order they
   *         occured in source. If source is the empty String then an empty ArrayList will be
   *         returned.
   */
  public ArrayList<String> getAllTokens(String sourceStr) {
    ArrayList<String> tokens = new ArrayList<>(15);
    setSourceString(sourceStr);
    while (hasNext()) {
      tokens.add(next());
    }
    return tokens;
  }
  
  /**
   * Returns true if all tokens in the source string have been returned. If there is nothing
   * left except posibly skip characters then return false. Leaves the string positioned at the
   * first character of the next token (past any skip characters, so change the skip characters
   * before calling if want the changes to affect this call).
   */
  @Override
  public boolean hasNext() {
    skipForward();
    if ((source == null) || (source.length() <= position))
      return false;
    else
      return true;
  }
  
  /**
   * Returns true if the specifed character is a delimiter.
   */
  protected final boolean isDelimiter(char c) {
    return containsChar(delimiters, c);
  }
  
  /**
   * Returns true if the specifed character is a escape.
   */
  protected final boolean isEscape(char c) {
    return containsChar(escapes, c);
  }
  
  /**
   * Returns true if the specifed character is a fill character.
   */
  protected final boolean isFill(char c) {
    return containsChar(fills, c);
  }
  
  /**
   * Returns true if the specifed character is a quote.
   */
  protected final boolean isQuote(char c) {
    return containsChar(quotes, c);
  }
  
  /**
   * Returns true if the specifed character is a skip.
   */
  protected final boolean isSkip(char c) {
    return containsChar(skips, c);
  }
  
  /**
   * Same as {@link #nextToken}.
   */
  @Override
  public String next() {
    return nextToken();
  }
  
  /**
   * Returns the next token in the source string. This method will not raise the
   * NoSuchElementException. It will just return null when there are no more tokens in the
   * source string.
   * <p>
   * Each call to next will advance the internal string postion to just past the delimitor that
   * delimits the returned token. If any of the control strings (skips, quotes, escapes) or the
   * control flags (stripQuotes, stripEscapes) is changed before the next call then the changes
   * will effect the processing of the next token.
   * 
   * @return null if there is no next token (i.e., after processing the last delimitor there is
   *         nothing left in the string except posibly some skip characters. Otherwise a string
   *         is returned containing the next token.
   */
  public String nextToken() {
    if ( !hasNext())
      return null;
    StringBuffer buf = new StringBuffer(20);
    boolean inQuote = false;
    char quoteCharacter = '\0'; // contains the initial quote
    // character when processing a quoted
    // string
    boolean inEscape = false;
    boolean inFill = false;
    int lastNonSkip = 0; // the position of the last character that is
    // not a skip character.
    for (; position < source.length(); position++ ) {
      char c = curChar();
      if (inEscape) {
        // Check for standard escape characters
        switch (c) {
          case 'n':
            buf.append('\n');
            break;
          case 't':
            buf.append('\t');
            break;
          case 'b':
            buf.append('\b');
            break;
          case 'f':
            buf.append('\f');
            break;
          case 'r':
            buf.append('\r');
            break;
          default:
            buf.append(c);
        }
        lastNonSkip = buf.length();
        inEscape = false;
        inFill = false;
      } else if (isEscape(c)) {
        if ( !stripEscapes) {
          buf.append(c);
        }
        inEscape = true;
        inFill = false;
      } else if (inQuote) {
        // Processing a quoted string, continue until a matching quote
        // character
        if (c == quoteCharacter) {
          // end the quoted string
          if ( !stripQuotes) {
            buf.append(c);
            lastNonSkip = buf.length();
          }
          inQuote = false;
        } else {
          // not an ending quote or an escape character
          buf.append(c);
          lastNonSkip = buf.length();
        }
      } else if (isQuote(c)) {
        if ( !stripQuotes) {
          buf.append(c);
        }
        inQuote = true;
        inFill = false;
        quoteCharacter = c;
      } else if (isDelimiter(c)) {
        position++ ;
        if ( !removeDelimiters) {
          buf.append(c);
          lastNonSkip = buf.length();
        }
        break;
      } else if (compressFill) {
        if (isFill(c)) {
          if ( !inFill) {
            inFill = true;
            c = fills.charAt(0);
            buf.append(c);
            if ( !isSkip(c)) {
              lastNonSkip = buf.length();
            }
          }
        } else {
          inFill = false;
          buf.append(c);
          if ( !isSkip(c)) {
            lastNonSkip = buf.length();
          }
        }
      } else {
        buf.append(c);
        if ( !isSkip(c)) {
          lastNonSkip = buf.length();
        }
      }
    }
    // Check for syntax error
    if (inQuote || inEscape)
      throw new Error(
              "Source string terminated while inside a" + " quoted string or while processing an"
                      + " escape character.\n" + "Source string is: \"" + source + "\"");
    // Strip off trailing skip characters
    buf.setLength(lastNonSkip);
    return buf.toString();
  }
  
  /**
   * Unsupported.
   */
  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }
  
  /**
   * Returns the remaining part of the source string after the last. When a token is processed
   * the rest of the string will begin with the first character after the delimiter that
   * delimited the token. After calling rest the internal string position will be positioned
   * past the last item in the string. <br>
   * No skips are removed from the string.
   */
  public String rest() {
    String result = source.substring(position);
    position = source.length();
    return result;
  }
  
  /**
   * Set the source string for tokenization. The next token will be constructed starting with
   * the first character of the string.
   * 
   * @param stringToTokenize the string to be tokeninzed.
   */
  public void setSourceString(String stringToTokenize) {
    source = stringToTokenize;
    position = 0;
  }
  
  /**
   * Skips any skip character starting with the current position.
   */
  protected void skipForward() {
    if (source == null)
      return;
    while ((position < source.length()) && isSkip(curChar())) {
      position++ ;
    }
    return;
  }
  
  @Override
  public String toString() {
    if (source == null)
      return "Not initialized";
    if (source.isEmpty())
      return "Empty string";
    String firstPart = source.substring(0, position);
    String lastPart = source.substring(position);
    String result = firstPart + "<%d>".formatted(position) + lastPart;
    return result;
  }
} // end of class
