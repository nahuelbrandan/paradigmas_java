import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class DiccionarioHash implements IDiccionario {

  private final Map<String, String> diccionario = new HashMap<>();
    
  public void diccionarioHash() {
  }
    
  @Override
  public boolean containsKey(String key) {
    boolean bool = diccionario.containsKey(key);
    return bool;
  }

  @Override
  public String get(String key) {
    String value = diccionario.get(key);
    return value;
  }

  @Override
  public boolean isEmpty() {
    boolean bool = diccionario.isEmpty();
    return bool;
  }

  @Override
  public String put(String key, String value) {
    String val = diccionario.put(key, value);
    return val;
  }
  
  @Override
  public void putFromFile(String path, boolean revers) {  

    File yourFile = new File(path);
    if (!yourFile.exists()) {
      try {
        yourFile.createNewFile();
      } catch (IOException ex) {
        System.err.println("no se pudo crear el diccionario\n");
      }
    }

    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(new File(path)));
    } catch (FileNotFoundException ex) {
      System.err.println("Error: couldn't open dicctionary");
    }

    String line = null;
    try {
      while ((line = reader.readLine()) != null) {
        if (line.contains(",")) {
          String[] strings = line.split(",");
          if (!revers) {
            diccionario.put(strings[0], strings[1]);
          } else {
            diccionario.put(strings[1], strings[0]);
          }
        }
      }
    } catch (IOException ex) {
      System.err.println("Error: couldn't read dictionary");
    }
  }
    

  @Override
  public void toFile(String path, boolean revers) {
      
    try {
      File fileTwo = new File(path);
      try (FileOutputStream fos = new FileOutputStream(fileTwo);
      PrintWriter pw = new PrintWriter(fos)) {
        diccionario.entrySet().stream().forEach((aux) -> {
          if (!revers) {
            pw.println(aux.getKey() + "," + aux.getValue());
          } else {
            pw.println(aux.getValue() + "," + aux.getKey());
          }
        });
        pw.flush();
      }
    } catch (IOException ex) {
      System.err.println("Error: couldn't write the dictionary to file");
    }
  }

  @Override
  public int size() {
    return diccionario.size();
  }
}
