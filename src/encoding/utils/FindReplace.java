package encoding.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/** 
 Find-and-replace for single bytes.
 
 <P>WARNING: this overwrites file content. 
*/
public final class FindReplace {

  public static void main(String[] args) throws IOException {
    String fileName = "C:\\johanley\\ProjectsPhoton\\quotations-ps\\quotes-data-1252\\quotes.txt";
    //String fileName = "C:\\temp\\quotes.txt";
    
    //int find = 173; //SHY (soft hyphen) with /minus
    //int replace = 45;
    
    //int find = 39; //apostrophe
    //int replace = 146; // right single quote
    
    int find = 96; //back quote
    int replace = 145; // left single quote
    
    
    log(fileName);
    log("Replacing " + find + " with " + replace);
    FindReplace findReplace = new FindReplace();
    int numFound = findReplace.findReplace(find, replace, fileName);
    log("Num found: " + numFound);
    log("Done");
  }
  
  /** Returns the number of times the value was found. */
  int findReplace(int find, int replace, String fileName) throws IOException {
    //this is a simplistic impl that doesn't use streams or even buffering
    //read into a byte array; change as being read;
    long fileSize = new File(fileName).length();
    int[] values = new int[(int)fileSize];
    int idx = 0;
    int numFound = 0;
    try (FileInputStream fis = new FileInputStream(fileName)) {
      int value;
      while((value = fis.read()) != -1) {
        if (find == value) {
          value = replace;
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
