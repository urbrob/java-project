import java.io.*;
import java.util.*;
import java.nio.file.Files;

public class Coder{
  public String code(String text){
    return text.replace("\n", ":::n:::").replace("\r", ":::r:::");
  }
  public String decode(String text){
    return text.replace(":::n:::", "\n").replace(":::r:::", "\r");
  }
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
