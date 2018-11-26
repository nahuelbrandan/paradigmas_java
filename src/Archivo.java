import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Archivo {
  
  private String texto = null;
  private String[] palabras = null;
  
  public void archivo() {
  }

  public String getTexto() {
    return texto;
  }

  public String[] getPalabras() {
    return palabras;
  }

  public String getPalabraAt(int pos) {
    return palabras[pos];
  }

  /**
  * carga el texto de un archivo
     * @param path direccion del archivo
     * @throws java.io.FileNotFoundException El documento no existe o no tiene permisos.
     * @throws EmptyFileException El documento esta vacio.
  */
  public void putTexto(String path) throws FileNotFoundException, IOException, EmptyFileException {
    
    //caso en que el archivo esta vacio.
    BufferedReader br = new BufferedReader(new FileReader(path));     
    if (br.readLine() == null) {
      throw new EmptyFileException();
    }
      
    texto = new Scanner(new File(path)).useDelimiter("\\Z").next();
    palabras = texto.split("\\s+");
    
    // remover los primeros y ultimos signos en cada palabra.
    for (int i = 0; i < palabras.length; i++) {
      palabras[i] = removeFirstSpecial(palabras[i]);
      palabras[i] = removeLastSpecial(palabras[i]);
    }
  }

  public int size() {
    return palabras.length;
  }

  private static String removeFirstSpecial(String in) {
    boolean corte = true;
    int iter = 0;
    int contador = 0;
    while (iter < in.length() && corte) {
      if (!Character.isAlphabetic(in.charAt(iter)) && corte) {
        contador++;
      } else {
        corte = false;
      }
      iter++;
    }
    in = in.substring(contador);
    return in;
  }

  private static String removeLastSpecial(String in) {

    boolean corte = true;
    int iter = 1;
    int contador = 0;
    while (iter < in.length() + 1 && corte) {
      if (!Character.isAlphabetic(in.charAt(in.length() - iter)) && corte) {
        contador++;
      } else {
        corte = false;
      }
      iter++;
    }
    in = in.substring(0, in.length() - contador);
    return in;
  }  
}