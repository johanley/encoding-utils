package encoding.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;

/** 
 Find specific byte values in files, and report to stdout.
 
 WARNING: Notepad++ interface reports the first byte is having Position 1, not 0. 
 This class uses 0 for the first byte in the file.
*/
public final class FindWeirdChars {
  
  public static void main(String[] args) throws IOException {
    String fileName = "C:\\johanley\\ProjectsPhoton\\quotations-ps\\quotes-data-8859-1\\quotes.txt";
    log(fileName);
    FindWeirdChars findWeirdos = new FindWeirdChars();
    //173 SHY
    findWeirdos.find(Arrays.asList(173), fileName);
    
    //String dirName = "C:\\johanley\\ProjectsPhoton\\quotations-ps\\quotes-data-8859-1\\";
    //String dirName = "C:\\johanley\\ProjectsPhoton\\book-les-mots-ont-une-ame\\input";
    //FindWeirdChars findWeirdos = new FindWeirdChars();
    //findWeirdos.findRecursive(Arrays.asList(133, 156), dirName, Arrays.asList("1252"));
    
    log("Done.");
  }
  
  void find(List<Integer> targetValues, String fileName) throws IOException {
    try (FileInputStream fis = new FileInputStream(fileName)) {
      int byteCount = 0;
      int value;
      while((value = fis.read()) != -1) {
        if (targetValues.contains(value)) {
          log(fileName + " [byte position "  + byteCount + "]: has value " + HexValues.toStringLong(value));
        }
        ++byteCount;
      }
    }
  }
  
  void findRecursive(List<Integer> targetValues, String startingDir, List<String> extensions) throws IOException {
    Path start = Paths.get(startingDir);
    FileVisitor<Path> walker = new FileWalker(targetValues, extensions);
    Files.walkFileTree(start, walker);
  }
  
  private static void log(String msg) {
    System.out.println(msg);
  }
  
  /** Recursive walking of a file tree. */
  private final class FileWalker extends SimpleFileVisitor<Path> {
    FileWalker(List<Integer> targetValues, List<String> extensions){
      this.extensions = extensions;
      this.targetValues = targetValues;
    }
    private List<String> extensions;
    private List<Integer> targetValues;
    @Override public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
      String ext = extension(path.toFile().getName());
      if (extensions.contains(ext)) {
        log("Processing file:" + path);
        find(targetValues, path.toFile().getCanonicalPath());
      }
      else {
        //log("Skipping file:" + path);
      }
      return FileVisitResult.CONTINUE;
    }
    @Override  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
      return FileVisitResult.CONTINUE;
    }
    private String extension(String fileName) {
      int dot = fileName.lastIndexOf(".");
      return fileName.substring(dot + 1);
    }
  }  
  
}
