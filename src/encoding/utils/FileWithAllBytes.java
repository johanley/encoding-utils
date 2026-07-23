package encoding.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/** 
 Make files containing all byte values 0..255.
 
 <P>You can test how tools treat such files.
*/ 
public final class FileWithAllBytes {
  
  public static void main(String[] args) throws FileNotFoundException, IOException {
    FileWithAllBytes allBytes = new FileWithAllBytes();
    allBytes.makeFileRawBytes("C:\\temp\\all-bytes-1-line.raw");
    allBytes.makeFileHavingSeveralLines("C:\\temp\\all-bytes-N-lines.raw");
    allBytes.makeFileOneLinePerValue("C:\\temp\\all-bytes-256-lines.raw");
    log("Done");
  }

  /** With no new-lines inserted for convenience. */
  void makeFileRawBytes(String output) throws FileNotFoundException, IOException {
    log(output + ": Raw byte values.");
    List<Integer> allBytes = HexValues.allBytes();
    try (FileOutputStream fos = new FileOutputStream(output)) {
      for(Integer i : allBytes) {
        // OutputStream.java: "The byte to be written is the eight low-order bits of the argument b."
        fos.write(i);
      }
    } 
  }
  
  /** With new-lines repeated every 16 items. */ 
  void makeFileHavingSeveralLines(String output) throws FileNotFoundException, IOException {
    log(output + ": Raw byte values. New-lines added every 16 items.");
    List<Integer> allBytes = HexValues.allBytes();
    try (FileOutputStream fos = new FileOutputStream(output)) {
      for(Integer i : allBytes) {
        fos.write(i);
        if ((i + 1) % 16 == 0 ) {
          newLine(fos);
        }
      }
    } 
  }
  
  /** With new-lines after every single item. */ 
  void makeFileOneLinePerValue(String output) throws FileNotFoundException, IOException {
    log(output + ": Raw byte values. New-lines added after every item.");
    List<Integer> allBytes = HexValues.allBytes();
    try (FileOutputStream fos = new FileOutputStream(output)) {
      for(Integer i : allBytes) {
        fos.write(i);
        newLine(fos);
      }
    } 
  }
  
  private void newLine(FileOutputStream fos) throws IOException {
    String os = System.getProperty("os.name").toLowerCase();
    if ( os.startsWith("windows")  ) {
      fos.write(13); //CR
    }
    fos.write(10); //LF
  }
  
  private static void log(String msg) {
    System.out.println(msg);
  }
}
