package encoding.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 THIS IS A FAILED EXPERIMENT.
 APPARENTLY OS'S DON'T MAKE THE CLIPBOARD CONTENTS AVAILABLE AS A SIMPLE BYTE STREAM. 
*/
public final class InspectClipboard {
  
  public static void main(String[] args) throws IOException, UnsupportedFlavorException, ClassNotFoundException {
    InspectClipboard inspect = new InspectClipboard();
    inspect.showByteValues();
  }
  
  void showByteValues() throws IOException, UnsupportedFlavorException, ClassNotFoundException {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable contents = clipboard.getContents(null);
    if (contents != null) {
      log("Clipboard bytes (one per line):");
      for(DataFlavor flav : contents.getTransferDataFlavors()){
        log(flav.toString());
        log(flav.getMimeType());
      }
      //IMPORTANT: use a flavor that uses an InputStream in its implementation:
      //DataFlavor flavor = new DataFlavor("text/plain; class=java.io.InputStream");
      //String MIME_TYPE = "application/octet-stream";
      String MIME_TYPE = "application/x-java-text-encoding; class=\"[B\"";
      DataFlavor flavor = new DataFlavor(MIME_TYPE); // will use InputStream
      if (contents.isDataFlavorSupported(flavor)) {
        byte[] bytes = (byte[])contents.getTransferData(flavor);
        for (byte b : bytes) {
          log(HexValues.toStringLong(HexValues.byteToUnsignedInt(b)));
        }
        
        /*        
        try (InputStream is = (InputStream) contents.getTransferData(flavor)) {
          byte[] bytes = is.readAllBytes();
          for (byte b : bytes) {
            log(HexValues.toStringLong(HexValues.byteToUnsignedInt(b)));
          }
        }
        */
        //log("Emptying the clipboard.");
        //clipboard.setContents(new StringSelection(""), null);
      }
      else {
        log("Flavor is not supported.");
      }
    }
    else {
      log("Clipboard is empty.");
    }
  }
  
  private void log(String msg) {
    System.out.println(msg);
  }

}
