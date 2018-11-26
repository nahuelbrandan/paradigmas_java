# Traductor en Java.

contacto: nahuelbrandan123@gmail.com
Desarrollador: Nahuel Brandán.

##Decisión de diseño:
### Clases:

* Archivo: Para manejar el documento a traducir.

* Ignoradas: Para el caso de las palabras ignoradas.

* IDiccionario: Interfaz para los diccionarios requerida en las consignas adicionales.

* DiccionarioHash y DiccionarioLinked: Diccionarios que implementan la interfaz antes mencionada.

* EmptyFileException: Provee la excepcion de archivo vacio.

* Traductor: Clase principal la cual contiene el main.

* El diccionario español-inglés e ingles-español tiene el mismo formato:

    palabra,español,palabra,inglés
    palabra,español,palabra,inglés
    ...
    palabra,español,palabra,inglés

* El documento de palabras ignoradas tiene el siguiente formato:

    palabra palabra ..... palabra
    ...
    palabra palabra ..... palabra

* Caso en que el usuario al parámetro -m (--implementation=) le pasa un valor distinto de 1 o 0 se le 
informa del error, y se le consulta si desea seguir con el valor por defecto o terminar la ejecución.

* Al escribir en la salida se borra todo el texto que podria llegar a tener anteriormente.

### Librerias:

* Las librerias externan se ubican en lib/

* Para el parsing de los argumentos utilizamos las funciones de la libreria: org.apache.commons.cli.

* Además se utiliza la libreria commons-io-2.5 en la clase Ignoradas para utilizar su método toString, el cual transforma un FileInputStream a String.

## Forma de ejecutarlo:

* Situados en la carpeta principal, hacemos make para compilar y luego para ejecutar el formato es:

 `$java -jar bin/Traductor.jar [parámetros]`

donde/

    -i FILE | --input=FILE          : Documento de entrada. (Requerido)
    -d FILE | --dictionary=FILE     : Diccionario de traducción.
    -g FILE | --ignored=FILE        : Diccionario de palabras ignoradas.
    -o FILE | --output=FILE         : Archivo de salida.
    -r      | --reverse             : Dirección de la traducción.
    -m FILE | --implementation=FILE : Solo acepta 0 ó 1.
    -p FILE | --dictpersonal=FILE   : Diccionario de traducciones personales.

* make clean para borrar el ejecutable y todos los .class creados.

### Ejemplos de ejecución.

    `$java -jar bin/Traductor.jar -i doc`
    `$java -jar bin/Traductor.jar -i doc.txt --dictionario=dic.txt`

### Ejemplos completo.    
    
    `$java -jar bin/Traductor.jar -i doc.txt --dictionario=dic.txt -i ign.txt -o salida.txt -m 0 -p dicPer.txt `


## Consignas Adicionales.

1. Definir una interfaz para los diccionarios.
    Hecho. Clase IDiccionario

2. Dar dos implementaciones de Diccionarios distintas que implementen la interfaz que introdujeron.
    Hecho. Clases DiccionarioHash y DiccionarioLinked.

3. Agregar un parámetro  `-m (--implementation)` que reciba `0` o `1`.
    Hecho.

##Comentarios:

* utilicé el IDE: NetBeans IDE 8.0.2.
* Existen mucha variedad de librerias útiles.

para correr checkstyle, parados en el directorio traductor/src/ hacer:
   	
	java -jar checkstyle-7.1-all.jar -c /google_checks.xml [FILE]

 