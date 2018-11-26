import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ignoradas {
  
  private String path = null;
  private final List<String> listWords = new ArrayList<>();
  
  public void ignoradas() {
  }
  
  public List<String> get() {
    return listWords;
  }
  
  public void add(String word) {
    listWords.add(word);
  }
  
  /**
  * Carga las palabras ignoradas desde un archivo.
  * @param path direccion
  * @throws FileNotFoundException .
  * @throws IOException .
  */
  public void addFromFile(String path) throws FileNotFoundException, IOException {
    this.path = path;
    
    // crea el archivo sino existe. 
    File file = new File(path);
    file.createNewFile();
    
    //captura el texto del archivo en un string.
    FileInputStream inputStream = new FileInputStream(path);
    String everything = IOUtils.toString(inputStream);
    
    // al texto lo divido en palabras.
    String words2 = everything.replaceAll("\n", " ");
    String[] words = words2.split(" ");
    listWords.addAll(Arrays.asList(words));
  }

  public boolean contains(String word) {
    return listWords.contains(word);
  }

  /**
  * Sube las palabras ignoradas a un archivo.
  * @throws java.io.IOException problemas al tratar de escribir en el documento.
  */
  public void toFile() throws IOException {
    try (FileWriter writer = new FileWriter(path)) {
      for (String str: listWords) {
        writer.write(str);
        writer.write(" ");
      }
    }
  }  
}
