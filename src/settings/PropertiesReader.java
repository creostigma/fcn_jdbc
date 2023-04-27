package settings;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesReader implements ISettings{

    @Override
    public Map<String, String> read(){
        String rootFolder = System.getProperty("user.dir");

        Map<String, String> proops = new HashMap<>();

       // try(InputStream input = Files.newInputStream(Paths.get(String.format("%s/scr/resources/%s", rootFolder, "db.properties")))){
        try(InputStream input = Files.newInputStream(Paths.get(String.format("/Users/ruslan/IdeaProjects/fcn_jdbc/src/resources/db.properties")))){
            Properties properties = new Properties();
            properties.load(input);

            for (Map.Entry<Object, Object> entry: properties.entrySet()){
                proops.put(entry.getKey().toString(), entry.getValue().toString());
            }

        }catch (IOException e){
            throw new RuntimeException(e);
        }

        return proops;
    }
}
