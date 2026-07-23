package encoding.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** 
 Show stats (stdout) for the number of occurrences of each value 0..255 in a given file, 
 or tree of files beneath a root directory. 
*/
public final class Histogram {
  
  public static void main(String[] args) throws FileNotFoundException, IOException {
    Histogram histo = new Histogram();
    Map<Integer, Integer> stats = histo.histogramOneFile("C:\\johanley\\ProjectsPhoton\\quotations-ps\\quotes-data-1252\\quotes.txt");
    showStats(stats);
    
    //String startingDir = "C:\\johanley\\ProjectsPhoton\\book-les-mots-ont-une-ame\\input";
    //List<String> extensions = Arrays.asList("1252");

    /*
    String startingDir = "C:\\johanley\\ProjectsPhoton\\quotations-ps\\quotes-data-8859-1";
    List<String> extensions = Arrays.asList("txt");
    Map<Integer, Integer> recursiveStats = histo.histogramRecursive(startingDir, extensions);
    showStats(recursiveStats);
    */
  }
  
  Map<Integer /*0..255*/, Integer /*count*/> histogramOneFile(String inputFile) throws FileNotFoundException, IOException {
    Map<Integer, Integer> result = allZeroes();
    histogram(inputFile, result);
    return result;
  }
  
  Map<Integer /*0..255*/, Integer /*count*/> histogramRecursive(String startingDir, List<String> fileEndsWith) throws FileNotFoundException, IOException {
    Map<Integer, Integer> stats = allZeroes();
    Path start = Paths.get(startingDir);
    FileVisitor<Path> walker = new FileWalker(fileEndsWith, stats);
    Files.walkFileTree(start, walker);
    return stats;
  }
  
  private static void log(String msg) {
    System.out.println(msg);
  }
  
  /** Without a newline. */
  private static void logx(String msg) {
    System.out.print(msg);
  }

  private static String NL = System.getProperty("line.separator");
  
  private static void showStats(Map<Integer, Integer> stats) {
    for(Integer value : stats.keySet()) {
      log(HexValues.toStringLong(value) + ":" + stats.get(value));
    }
    showStatsTable(stats);
    log(NL + "Num distinct values: " + numDistinctValues(stats));
    int numAboveTilde = numDistinctValuesAbove(126, stats);
    log("Num distinct values above 126 (7E tilde): " + numAboveTilde);
    log("Num tab chars: " + numTabs(stats));
    if (hasLowControlChars(stats)) {
      log("*** WARNING: HAS ODD, LOW ORDER CONTROL CHARACTERS BELOW 32.");
    }

    incompatibleWith("ASCII", HexValues.forASCII(), stats);
    incompatibleWith("8859-1", HexValues.for8859_1(), stats);
    incompatibleWith("ISOLatin1Encoding (PostScript only)", HexValues.forPostScriptISOLatin1Encoding(), stats);
    incompatibleWith("Windows-1252", HexValues.forWindows1252(), stats);
  }

  private static void showStatsTable(Map<Integer, Integer> stats) {
    int count = 0;
    String line1 = "   0 1 2 3 4 5 6 7 8 9 A B C D E F";
    logx(line1);
    for(Integer value : stats.keySet()) {
      if (count % 16 == 0) logx(NL + HexValues.toString(count));
      logx((stats.get(value) > 0 ? "|x" : "| "));
      ++count;
    }
  }
  
  private static Integer numDistinctValues(Map<Integer, Integer> stats) {
    return numDistinctValuesAbove(-1, stats);
  }
  
  private static Integer numDistinctValuesAbove(int baseValue, Map<Integer, Integer> stats) {
    Integer result = 0;
    for(Integer value : stats.keySet()) {
      if ((stats.get(value) > 0) && (value > baseValue) ) {
        ++result; 
      }
    }
    return result;
  }

  /** Populate stats for a single file. */
  private void histogram(String input, Map<Integer, Integer> stats) throws FileNotFoundException, IOException {
    try (FileInputStream fis = new FileInputStream(input)) {
      int value;
      while((value = fis.read()) != -1) {
        Integer incremented = stats.get(value) + 1;
        stats.put(value, incremented);
      }
    }
  }

  /** Starting point for gathering stats. */
  private Map<Integer, Integer> allZeroes(){
    Map<Integer, Integer> result = new LinkedHashMap<>();
    for(Integer value : HexValues.allBytes()) {
      result.put(value, 0);
    }
    return result;
  }
  
  private static boolean hasLowControlChars(Map<Integer, Integer> stats) {
    boolean result = false;
    for(Integer key : stats.keySet()) {
      if (stats.get(key) > 0) {
        //beneath the SPACE, and not CR or LF or TAB
        if (key < 32 && key != 10 && key != 13 && key != 9) {
          result = true;
          break;
        }
      }
    }
    return result;
  }
  
  private static int numTabs(Map<Integer, Integer> stats) {
    return stats.get(9);
  }
  
  private static void incompatibleWith(String name, List<Integer> bytesInEncoding, Map<Integer, Integer> stats) {
    List<Integer> result = new ArrayList<>();
    for(Integer key : stats.keySet()) {
      if (stats.get(key) > 0) {
        if (!bytesInEncoding.contains(key)) {
          result.add(key);
        }
      }
    }
    logx("Incompatible with " + name + ": [ ");
    for(Integer bad : result) {
      logx(HexValues.toStringLong(bad) + " ");
    }
    log("]");
  }

  /** Recursive walking of a file tree. */
  private final class FileWalker extends SimpleFileVisitor<Path> {
    FileWalker(List<String> extensions, Map<Integer, Integer> stats){
      this.extensions = extensions;
      this.stats = stats;
    }
    private List<String> extensions;
    private Map<Integer, Integer> stats;
    @Override public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
      String ext = extension(path.toFile().getName());
      if (extensions.contains(ext)) {
        log("Processing file:" + path);
        histogram(path.toFile().getCanonicalPath(), stats);
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
