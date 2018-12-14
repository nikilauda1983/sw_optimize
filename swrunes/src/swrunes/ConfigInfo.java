package swrunes;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

/**
 * @author ducnd2 on 7/22/2016.
 */
public class ConfigInfo {

	public static class PetSetting {
		public int mainSet,leaderBuff=30;
		public String secondSet,petsLock;
		public int finalOptimize,runeFilterValue=7;
		public boolean haveWill, haveShield, haveRevenge,haveNemesis;
		public boolean filterSpd, filterCr, filterCDmg, filterDef, filterHp, filterAtk,filterRes,filterAcc;
		public int firstFilterCombo,firstFilterValue;
		public int secondFilterCombo,secondFilterValue;
		public int thirdFilterCombo,thirdFilterValue;
		public int fourthFilterCombo,fourthFilterValue;
                public int fifthFilterCombo,fifthFilterValue;

		public boolean useOnlyStorage = false;
		public boolean excludedLocked = true;
		public String petName = "";
		public boolean isLocked = false;
		
                public Set<String> excludeRunes = new HashSet();
                public Set<String> includeRunes = new HashSet();
                
                public boolean isSaved = false;
		public int[] lastOptimize= new int[6];
                public String[] buildUniqueId= new String[6];
                
                public int mainDouble = 0;
		public int effectiveHp,finalDamage,optimizeSpd;
		public int[] slot246={0,0,0};
                public int mainSkill = 0;
                
                public boolean noBroken = false;
                public boolean guildWars = false;
	};

	public static class PetBuild {
		public String name;
		public String petName;
		public int[] runes=new int[6];
		public boolean locked;

		public PetBuild(String name,String petName){
			this.name = name;
			this.petName = petName;
		}
	}                
	public Map<String, PetSetting> petMaps = new HashMap();
	public Map<String, List<PetBuild>> petBuilds = new HashMap();

        public int loadPetLevel = 1;
        public String lastJsonFile="";
        
        public int UpgradeAllRune = 0;
	static ConfigInfo instance;
	public String lastPet;
	public boolean useThreads=false;
	public int numThreads=5;
	public int lowRuneLevel = 6;
        
        public String globalLocks = "";
        public boolean runePet4star = true;
        public boolean runePet5star = true;
        public boolean useBigUI = false;
        public boolean upPet40 = false;
        public int fontSize=0;

	public static ConfigInfo getInstance() {
		if (instance == null) {
			Gson gson = new Gson();
			instance = loadFile();
			if (instance == null) {
				instance = new ConfigInfo();
				instance.saveFile();
			} else {
				//System.out.println("Load config ok : " + gson.toJson(instance));
			}
		}
		return instance;
	}



	public Set<String> favourite = new HashSet();
	public static int GL_WATER = 0, GL_FIRE = 1, GL_WIND = 2, GL_DARK = 3, GL_LIGHT = 4;
	public static String[] petElement = {
		"Water", "Fire", "Wind", "Dark", "Light"};

	public static int getElementIndex(String s1) {
		for (int i = 0; i < petElement.length; i++) {
			if (s1.equalsIgnoreCase(petElement[i])) {
				return i;
			}
		}
		return 0;
	}

	public int[] gloryAtkElement = {
			17, 17, 21, 9, 13};
	public int glory_atk = 14;
	public int glory_def = 20;
	public int glory_hp = 10;
	public int glory_cd = 25;
	public int glory_spd = 14;

	public int guildwar_cd = 12;
	public int guildwar_def = 6;
	public int guildwar_atk = 6;
	public int guildwar_hp = 6;

	public void saveFile() {
		Gson gson = new Gson();
		try (FileWriter writer = new FileWriter("config.json")) {
			gson.toJson(this, writer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Save file Done");
		//System.out.println("Save config file : " + gson.toJson(this));
	}
	public void saveFileOther() {
		Gson gson = new Gson();
		try (FileWriter writer = new FileWriter("config_temp.json")) {
			gson.toJson(this, writer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Save file Done");
		//System.out.println("Save config file : " + gson.toJson(this));
	}

	public static ConfigInfo loadFile() {
		Gson gson = new Gson();
		try (Reader reader = new FileReader("config.json")) {
			return gson.fromJson(reader, ConfigInfo.class);
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("File not found !");
		}
		return null;
	}
}
