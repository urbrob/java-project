import java.io.*;
import java.util.*;
import java.nio.file.Files;


/** Klasa Coder ma na celu wsparcie i standaryzacje kodowania podczas wysyłania zawartości plików między serwerem a klientem */
public class Coder{

  /** Metoda kodująca string */
  public String code(String text){
    return text.replace("\n", ":::n:::").replace("\r", ":::r:::");
  }

  /** Metoda dekodująca string */
  public String decode(String text){
    return text.replace(":::n:::", "\n").replace(":::r:::", "\r");
  }

  /** Metoda zamieniająca całą zawrtość pliku na string */
  public String file_to_string(File file){
    String text = "DEADBEEF";
    try{
      text = new Coder().code(new String(Files.readAllBytes(file.toPath()), "UTF-8"));
    }catch(Exception e){
      e.printStackTrace();
      System.out.println(e);
    }
    return text;
  }
}
