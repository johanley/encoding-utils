package encoding.utils;

import java.nio.charset.Charset;
import java.util.Map;

/**
 List the string identifiers for the encodings available to the current Java Runtime Environment.
   
 <P>The result includes: 'US-ASCII', 'ISO-8859-1', 'windows-1252', 'UTF-8', 'UTF-16', 'IBM437'.
 
 <P>(My system returns 172 supported encodings.)
 
 It doesn't seem to include 'ISOLatin1'. 
*/
public final class ListEncodingNames {
  
  public static void main(String[] args) {
    Map<String, Charset> allCharSets = Charset.availableCharsets();
    System.out.println("Num supported encodings: " + allCharSets.keySet().size());
    for(String key : allCharSets.keySet()) {
      System.out.println(key);
    }
  }

}
