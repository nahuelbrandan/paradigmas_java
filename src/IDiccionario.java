public interface IDiccionario {
    
  public boolean containsKey(String key);
    
  public String get(String key);

  public boolean isEmpty();

  public String put(String key, String value);

  public void putFromFile(String path, boolean revers);

  public void toFile(String path, boolean revers);
  
  public int size();
}
