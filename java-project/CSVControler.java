import java.util.*;
import java.io.*;


public class CSVControler{
  public String user;
  public String file_name;
  public HashMap<String, String> add;
  public HashMap<String, String> remove;

  CSVControler(String fname){
    file_name = fname;
    this.add = new HashMap<String, String>();
    remove = new HashMap<String, String>();
  }

  public void remove_user_file(String user, String file_name){
    this.remove.put(user, file_name);
  }
  public void add_user_file(String user, String file_name){
    this.add.put(user, file_name);
  }

  public HashMap<String, ArrayList<String>> load_csv_to_hash_map(){
    HashMap<String, ArrayList<String>> csv_hash = new HashMap<String, ArrayList<String>>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/1/" + this.file_name));
      reader.readLine(); // Skip header
			String line = reader.readLine();
			while (line != null) {
        String lines[] = line.split(",");
        ArrayList<String> file_names = new ArrayList<String>();
        for(String file_name : lines[1].split(";")){
          file_names.add(file_name);
        }
        csv_hash.put(lines[0], file_names);
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    return csv_hash;
  }

  public String csv_hash_to_string(HashMap<String, ArrayList<String>> csv_hash){
    String csv = "user,file_names\n";
    for(Map.Entry<String, ArrayList<String>> entry : csv_hash.entrySet()) {
      csv += entry.getKey() + ",";
      for(String file_name : entry.getValue()){
        csv += file_name + ";";
      }
      csv += "\n";
    }
    return csv;
  }

  public HashMap<String, ArrayList<String>> remove_files(HashMap<String, ArrayList<String>> csv_hash){
    for(Map.Entry<String, String> entry : this.remove.entrySet()) {
      if(csv_hash.containsKey(entry.getKey())){
        csv_hash.get(entry.getKey()).remove(entry.getValue());
      }
    }
    return csv_hash;
  }

  public HashMap<String, ArrayList<String>> add_files(HashMap<String, ArrayList<String>> csv_hash){
    for(Map.Entry<String, String> entry : this.add.entrySet()) {
      if(csv_hash.containsKey(entry.getKey())){
        if(csv_hash.get(entry.getKey()).contains(entry.getValue())){
          continue;
        }
      }else{
        csv_hash.put(entry.getKey(), new ArrayList<String>());
      }
      csv_hash.get(entry.getKey()).add(entry.getValue());
    }
    return csv_hash;
  }

  public void save_hash_map_to_csv(HashMap<String, ArrayList<String>> csv_hash){
    ServerWorker sw = new ServerWorker();
    System.out.println(this.csv_hash_to_string(csv_hash));
    sw.modify_file(this.file_name, this.csv_hash_to_string(csv_hash));
  }


  public void run(){
    new Thread(() -> {
      while(true){
        try{
          Thread.sleep(1000);
        }catch(Exception e){}
        if(!this.add.isEmpty() || !this.remove.isEmpty()){
          HashMap<String, ArrayList<String>> csv_hash = this.load_csv_to_hash_map();
          System.out.println(csv_hash);
          csv_hash = this.remove_files(csv_hash);
          System.out.println(csv_hash);
          csv_hash = this.add_files(csv_hash);
          System.out.println(csv_hash);
          this.save_hash_map_to_csv(csv_hash);
          this.add = new HashMap<String, String>();
          this.remove = new HashMap<String, String>();
        }
      }
    }).start();
  }

  public Boolean check_if_user_have_permission(String user, String file_name){
    HashMap<String, ArrayList<String>> hash_csv = load_csv_to_hash_map();
    System.out.println(hash_csv);
    return (hash_csv.containsKey(user) && hash_csv.get(user).contains(file_name));
  }

}
