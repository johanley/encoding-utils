package encoding.utils;

import java.io.IOException;
import java.nio.charset.Charset;
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
 Attempt to convert files from one encoding to another using Java's usual read-write file mechanics, and explicit encodings.
 
 <P>WARNING: THESE METHODS CONVERT A FILE IN PLACE, using a single file name.
 
 <P>WARNING: Encoding conversions can easily lead to pain and sorrow if the two encodings don't encode the same set of graphemes.
 For example changing from windows-1252 to 8859-1 can be a problem, since windows-1252 encodes more graphemes than 8859-1.
 And this can be data-dependent too: you can be 'okay' in one case, but 'not okay' in another, because a single char is not common between the encodings.
 By default, Java replaces unmappable chars, but Java can be configured to behave differently.
*/
public final class ConvertEncoding {
  
  public static void main(String[] args) throws IOException {
    String startingDir = "";
    ConvertEncoding convert = new ConvertEncoding();
    convert.convertRecursive(startingDir, "", "", Arrays.asList("txt"));
  }
  
  /** 
   Convert a file from one encoding to another.
   
   <P>NO CHECKING is made to confirm that the from-encoding is correct.
    
   <P>There are cases in which no actual change to the byte values takes place; it depends 
   on both the encodings and the source text.
  */
  void convert(String fileInput, String fromEncoding, String toEncoding) throws IOException {
    List<String> lines = readSmallTextFile(fileInput, fromEncoding);
    writeSmallTextFile(lines, fileInput, toEncoding);
  }

  void convertRecursive(String startingDir, String fromEncoding, String toEncoding, List<String> extensions) throws IOException {
    Path start = Paths.get(startingDir);
    FileVisitor<Path> walker = new FileWalker(fromEncoding, toEncoding, extensions);
    Files.walkFileTree(start, walker);
  }
  
  private List<String> readSmallTextFile(String fileName, String encoding) throws IOException {
    Path path = Paths.get(fileName);
    return Files.readAllLines(path, Charset.forName(encoding));
  }
  
  private void writeSmallTextFile(List<String> lines, String fileName, String encoding) throws IOException {
    Path path = Paths.get(fileName);
    Files.write(path, lines, Charset.forName(encoding));
  }

  /** Recursive walking of a file tree. */
  private final class FileWalker extends SimpleFileVisitor<Path> {
    FileWalker(String fromEncoding, String toEncoding, List<String> extensions){
      this.fromEncoding = fromEncoding;
      this.toEncoding = toEncoding;
      this.extensions = extensions;
    }
    private String fromEncoding;
    private String toEncoding;
    private List<String> extensions;
    @Override public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
      String ext = extension(path.toFile().getName());
      if (extensions.contains(ext)) {
        log("Processing file:" + path);
        convert(path.toFile().getCanonicalPath(), fromEncoding, toEncoding);
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
  
  private void log(String msg) {
    System.out.println(msg);
  }
  
  
}
