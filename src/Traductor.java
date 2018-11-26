import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Traductor {

  private static String  input = null;
  private static String  output = null;
  private static String  dictionary = null;
  private static String  dictionaryUser = null;
  private static String  ignored = null;
  private static boolean reverse = false;
  private static String  implement = null;
  
  private static IDiccionario diccionarioPrincipal = null;
  private static IDiccionario diccionarioPersonal = null;
  private static Ignoradas ignoradasPrincipal  = null;
  private static Ignoradas ignoradasSecundario = null;

  /**
  * El main.
  * @param args argumentos pasados por parametro.
  */
  public static void main(String[] args) {
            
    try {
      parseArgs(args);
    } catch (ParseException ex) {
      System.out.println("problemas en el parseo de los parametros\n");
      return;
    }
    if (input == null) {
      System.err.println("El documento a traducir es requerido, "
              + "debe hacer al menos: -i doc.txt o --input=doc.txt");
      return;
    }
 
    if (!"0".equals(implement) && !"1".equals(implement)) {
      System.out.println("El usuario decidio no continuar");
      return;
    }
    
    ignoradasPrincipal  = new Ignoradas();
    ignoradasSecundario = new Ignoradas();

    try {
      ignoradasPrincipal.addFromFile(ignored);
    } catch (IOException ex) {
      System.err.println("Error: problemas en cargar la lista de palabras ignoradas");
      return;
    }
    
    if ("1".equals(implement)) {
      diccionarioPrincipal  = new DiccionarioLinked();
      diccionarioPersonal   = new DiccionarioLinked();
    } else {
      diccionarioPrincipal  = new DiccionarioHash();
      diccionarioPersonal   = new DiccionarioHash();
    }
    //cargamos los diccionario.
    diccionarioPrincipal.putFromFile(dictionary, reverse);
    diccionarioPersonal.putFromFile(dictionaryUser, reverse);
    
    Archivo documento = new Archivo();
    try {
      documento.putTexto(input);
    } catch (FileNotFoundException ex) {
      System.out.println("No existe el archivo o el directorio a traducir, o no tiene permisos");
      return;
    } catch (IOException ex) {
      Logger.getLogger(Traductor.class.getName()).log(Level.SEVERE, null, ex);
      return;
    } catch (EmptyFileException ex) {
      System.out.println("El archivo a traducir esta vacio");
      // creo un archivo de salida vacio.
      try (PrintWriter out = new PrintWriter(output)) {
        out.println("");
      } catch (FileNotFoundException ext) {
        Logger.getLogger(Traductor.class.getName()).log(Level.SEVERE, null, ex);
      }
      System.out.println("\nEl documento " + input + " ha sido procesado.");
      System.out.println("Resultados en " + output);
      
      return;
    }
    
    List<String> traducciones = new ArrayList<>();        
    for (int i = 0; i < documento.size(); i++) {
      String word = documento.getPalabraAt(i);
      
      
      if (word.isEmpty()) {
        traducciones.add(word);
      } else if (ignoradasSecundario.contains(word)) {
        traducciones.add(word);
      } else if (ignoradasPrincipal.contains(word)) {
        traducciones.add(word);
      } else if (diccionarioPersonal.containsKey(word)) {
        traducciones.add(diccionarioPersonal.get(word));
      } else if (diccionarioPrincipal.containsKey(word)) {
        traducciones.add(diccionarioPrincipal.get(word));
      } else {
        traducciones.add(consultUser(word));
      }
    }
    //creamos la traduccion.
    String textoFinal = documento.getTexto();
    for (int i = 0;i < documento.size(); i++) {
      String oldWord = documento.getPalabraAt(i);
      String newWord = traducciones.get(i);
      textoFinal = textoFinal.replaceAll("(?i)(?<![a-z])" + oldWord + "(?![a-z])", newWord);
    }

    // escribir en archivo de salida.
    try (PrintWriter out = new PrintWriter(output)) {
      out.println(textoFinal);
    } catch (FileNotFoundException ex) {
      Logger.getLogger(Traductor.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    try {
      ignoradasPrincipal.toFile();
    } catch (IOException ex) {
      Logger.getLogger(Traductor.class.getName()).log(Level.SEVERE, null, ex);
      return;
    }

    diccionarioPersonal.toFile(dictionaryUser, reverse);
    
    System.out.println("\nEl documento " + input + " ha sido procesado.");
    System.out.println("Resultados en " + output);
  }

  private static void parseArgs(String[] args) throws ParseException {    
    // Configuramos las opciones de validación de entrada.
    Options options;
    options = new Options();
    options.addOption("i", "input=",        true,  "archivo a traducir");
    options.addOption("d", "dictionary=",   true,  "diccionario a utilizar");
    options.addOption("p", "dictpersonal=", true,  "diccionario personal");    
    options.addOption("g", "ignored=",      true,  "palabras ignoradas");
    options.addOption("o", "output=",       true,  "archivo de salida");
    options.addOption("r", "reverse",       false, "trad ingles español");
    options.addOption("m", "implementation=",true,  "implementacion");
    
    CommandLineParser parser = new DefaultParser();
    CommandLine cmdLine = parser.parse(options, args);

    input      = cmdLine.hasOption("i") ? cmdLine.getOptionValue("i") : null;
    output     = cmdLine.hasOption("o") ? cmdLine.getOptionValue("o") : "Output.txt";
    ignored    = cmdLine.hasOption('g') ? cmdLine.getOptionValue('g') : "Ignored.txt";
    dictionary = cmdLine.hasOption('d') ? cmdLine.getOptionValue('d') : "Dictionary.txt";
    dictionaryUser = cmdLine.hasOption('p') ? cmdLine.getOptionValue('p') : "DictUser.txt";
    implement = cmdLine.hasOption('m') ? cmdLine.getOptionValue('m') : "0";

    if (cmdLine.hasOption('r')) {
      reverse = true;
    }
    
    if (input == null) {
      return;
    }

    // si implement tiene un valor distinto de 0 o 1 consulta al usuario.
    if (!"0".equals(implement) && !"1".equals(implement)) {
      String ans = null;
      Scanner leer = new Scanner(System.in);
      do {
        System.out.println("implement solo recibe los valores 0 ó 1\n");
        System.out.println("¿Desea continua con el valor por defecto? S/n");
        ans = leer.next();
      }
      while (!(ans.equals("S")) && !(ans.equals("n")));
        
      if (ans.equals("S")) {
        implement = "0";
      }    
    }
  }

  private static String consultUser(String word) {
    String ans = null;
    String def = null;
    String traduction = null;
    Scanner leer = new Scanner(System.in);

    do {
      clearConsole();
      System.out.println("No hay traducción para la palabra: " + word);
      System.out.println("Ignorar (i) - Ignorar todas (h) ");
      System.out.println("- Traducir como (t) - Traducir siempre como (s) ");
      ans  = leer.next();
    }
    while (!(ans.equals("i")) && !(ans.equals("h")) && !(ans.equals("t")) && !(ans.equals("s")));

    switch (ans) {
      case "i":
        ignoradasSecundario.add(word);
        traduction = word;
        break;
      case "h":
        // agrego la palabra a la lista de palabras ignoradas.
        ignoradasPrincipal.add(word);
        traduction = word;
        break;
      case "t":
        //  agrego la palabra con su definición al diccionario principal.
        System.out.println("Traducir " + word + " como: ");
        def  = leer.next();
        diccionarioPrincipal.put(word, def);
        System.out.println("\nThe word and definition were added:\n");
        traduction = def;
        break;
      default:
        //  agrego la palabra con su definición al diccionario PERSONAL.
        System.out.println("Traducir " + word + " como: ");
        def  = leer.next();
        diccionarioPersonal.put(word, def);            
        System.out.println("\nThe word and definition were added:\n");
        traduction = def;
        break;
    }
    return traduction;
  }

  private static void clearConsole() {
    String ansiCls = "\u001b[2J";
    String ansiHome = "\u001b[H";
    System.out.print(ansiCls + ansiHome);
    System.out.flush();
  }
}