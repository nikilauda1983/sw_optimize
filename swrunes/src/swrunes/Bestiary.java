package swrunes;

import com.google.gson.Gson;
import gui.mainWindow;
import java.io.BufferedReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ducnd2 on 10/5/2016.
 */
public class Bestiary {

    public static class SkillWikiInfo {
        public String skill_name,skill_desc,multy, tooltip;
        public List<String> skill_up = new ArrayList<>();        
    }

    public static class PetInfo {

        String acc, cr, cd, res, star, spd, hp, atk, def;
        String uName, aName, element;
        String crawlLink;
        public List<SkillWikiInfo> skills = new ArrayList<>();
    }
    public Map<String, PetInfo> allPets = new HashMap<>();

    public static Bestiary loadFile() {

        
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(Bestiary.class.getResourceAsStream("/res/Pet_Bestiary.json")));
            String content="";
            String line;
            Gson gson = new Gson();
            while ((line = br.readLine()) != null) {
                content+=line+"\n";
            }
            br.close();
            //System.out.println("Content : "+content);
            if (content.length()>0){
                return gson.fromJson(content, Bestiary.class);
            }                        
            Reader reader = new FileReader("Pet_Bestiary.json");
            return gson.fromJson(reader, Bestiary.class);
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("File not found !");
        }
        return null;
    }

    public void saveFile() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("Pet_Bestiary.json")) {
            gson.toJson(this, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Save file Done");
        //System.out.println("Save config file : " + gson.toJson(this));
    }

    static Bestiary instance;

    public static void addPet(String url, PetInfo p1) {
        getInstance().allPets.put(url, p1);
    }

    public static Bestiary getInstance() {
        if (instance == null) {
            Gson gson = new Gson();
            instance = loadFile();
            if (instance == null) {
                instance = new Bestiary();
                instance.saveFile();
            } else {
                //System.out.println("Load config ok : " + gson.toJson(instance));
            }
            (new Thread() {
            public void run() {
                //jLabelIcon.setText("");
                System.out.println("Check missing avatars in Threading");
                Crawler.checkMissingAvatar();
                System.out.println("Done Check missing avatars in Threading");
            }
        }).start();
        }
        return instance;
    }
}
