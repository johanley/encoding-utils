package encoding.utils;

import java.util.ArrayList;
import java.util.List;

/** 
 Hex values for some specific encodings.
 
 TAB, CR, and LF are included, but other weird control characters 00..1F and 7F (DEL) are ignored here! 
*/
public final class HexValues {
  
  /** All possible byte values, 0..255. */
  static List<Integer> allBytes() {
    List<Integer> res = new ArrayList<>();
    for(int i = 0; i < 256; ++i) {
      res.add(i);
    }
    return res;
  }

  /** From SPACE to tilde. Ref: https://en.wikipedia.org/wiki/ASCII */
  static List<Integer> forASCII(){
    List<Integer> res = new ArrayList<>();
    res.add(9); res.add(10); res.add(13); //TAB, LF, CR
    for(int i = 32; i <= 126; ++i) {
      res.add(i);
    }
    return res;
  }

  /** Ref: https://en.wikipedia.org/wiki/ISO/IEC_8859-1 */
  static List<Integer> for8859_1(){
    List<Integer> res = new ArrayList<>();
    res.addAll(forASCII());
    for(int i = 160; i <= 255; ++i) {
      res.add(i);
    }
    return res;
  }

  /** Ref: https://en.wikipedia.org/wiki/Windows-1252 */
  static List<Integer> forWindows1252(){
    List<Integer> res = new ArrayList<>();
    res.addAll(forASCII());
    for(int i = 128; i <= 255; ++i) {
      if (i != 129 && i != 141 && i != 143 && i != 144 && i != 157) {
        res.add(i);
      }
    }
    return res;
  }

  /**
   Reference: the PostScript Language Reference Manual.
   
   ***WARNING***: As far as I can figure out, the PostScript manual is misleading on this point.
   Their 'ISOLatin1Encoding' seems to be specific to PostScript, and isn't really the standard !!
   According to other sources, 'ISO-Latin-1' is actually the exact same as '8859-1', with no deviation.
  */
  static List<Integer> forPostScriptISOLatin1Encoding(){
    List<Integer> res = new ArrayList<>();
    res.addAll(forASCII());
    for(int i = 144; i <= 255; ++i) {
      if (i != 153 && i != 156) {
        res.add(i);
      }
    }
    return res;
  }
  
  /** Render a value as hexadecimal values are usually written. */ 
  static String toString(Integer val) {
    String res = Integer.toHexString(val).toUpperCase();
    if (val < 10) {
      res = "0" + res;
    }
    return res;
  }
  
  /** Render a value as hexadecimal values are usually written, followed by the decimal value in parens. */ 
  static String toStringLong(Integer val) {
    String res = Integer.toHexString(val).toUpperCase();
    if (res.length() == 1) {
      res = "0" + res;
    }
    res = res + "(" + val + ")";
    return res;
  }

  /** Convert a Java byte (-128..127) to an unsigned int (0..255). */
  static int byteToUnsignedInt(byte b) {
    return Byte.toUnsignedInt(b);
  }
  
}
