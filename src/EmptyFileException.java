public class EmptyFileException extends Exception {
  String filename;

  EmptyFileException() {
  }

  EmptyFileException(String filename) {
    this.filename = filename;
  }

  public String toString() {
    return "The file " + filename + " is empty.";
  }
}