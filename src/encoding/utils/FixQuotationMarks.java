package encoding.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/** 
 Change from 'straight' double-quotes (") to curly-quotes, assuming windows-1252.
 
 <P>WARNING: this is intended only for windows-1252 encodings.
 <P>WARNING: this overwrites file content.
*/
public final class FixQuotationMarks {
  
  public static void main(String[] args) throws IOException {
    String fileName = "C:\\johanley\\ProjectsPhoton\\quotations-ps\\quotes-data-1252\\quotes.txt";
    //String fileName = "C:\\temp\\quotes.txt";
    log(fileName);
    log("Replace sequential pairs of straight-quotes with curly-quotes.");
    FixQuotationMarks fixQuotationMarks = new FixQuotationMarks();
    int numFound = fixQuotationMarks.apply(fileName);
    log("Num found: " + numFound);
    log("Done");
  }
  
  /** 
   Returns the number of times the straight-quote value was found.
   <P>Implemented with a find-and-alternate-replace algorithm.
   WARNING: assumes that the straight-quotes appear in pairs in the usual way. 
  */
  int apply(String fileName) throws IOException {
    //no streams, no buffering is used here: small files only
    //read into a byte array; change as being read;
    int STRAIGHT_QUOTE = 34;
    int DBL_QUOTE_LEFT = 147;
    int DBL_QUOTE_RIGHT = 148;
    
    long fileSize = new File(fileName).length();
    int[] values = new int[(int)fileSize];
    int idx = 0;
    int numFound = 0;
    try (FileInputStream fis = new FileInputStream(fileName)) {
      int value;
      while((value = fis.read()) != -1) {
        if (STRAIGHT_QUOTE == value) {
          value = (numFound % 2 == 0) ? DBL_QUOTE_LEFT : DBL_QUOTE_RIGHT;
          ++numFound;
        }
        values[idx] = value;
        ++idx;
      }
    }
    if (numFound > 0) {
      //then output back to the same file
      try (FileOutputStream fos = new FileOutputStream(fileName)) {
        idx = 0;
        for(int value : values) {
          fos.write(value);  
        }
      }
    }
    return numFound;
  }
  
  private static void log(String msg) {
    System.out.println(msg);
  }
}