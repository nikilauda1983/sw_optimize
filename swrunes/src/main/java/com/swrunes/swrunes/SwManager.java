package com.swrunes.swrunes;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.swrunes.swrunes.PetType.RuneSkill;
import com.swrunes.swrunes.RuneType.RuneSet;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.JOptionPane;

import org.jsoup.helper.StringUtil;

import static com.swrunes.swrunes.PetType.petNameElement;
import static com.swrunes.swrunes.PetType.petNameMap;
import static com.swrunes.swrunes.PetType.petNameMapInv;

public class SwManager {

    public static Map<String, Bestiary.PetInfo> skillPetInfo = new HashMap();
    public static Map<String, String> petFamily = new HashMap();

    public static Map<String, PetType> pets = new HashMap();
    public static Map<String, PetType> petsBestiary = new HashMap();

    public static Map<Integer, PetType> petsIds = new HashMap();
    public static List<RuneType> runes = new ArrayList();

    public static Map<String, RuneType> runesIds = new HashMap();
    public static Map<Integer, RuneType> runesSimpleIds = new HashMap();
    public static Map<String, RuneType> runeMaps = new HashMap();

    static SwManager instance;

    public static SwManager getInstance() {
        if (instance == null) {
            instance = new SwManager();
        }
        return instance;
    }

    public List<RuneType> searchRunes(String runeName, Function<RuneType, Boolean> para) {
        List<RuneType> res = new ArrayList();
        //System.out.println("************Rune Search************ : ");
        for (RuneType r : runes) {
            if (runeName == null || runeName.length() == 0 || runeName.toLowerCase().contains(r.runeType.toLowerCase())) {
                if (para.apply(r)) {
                    res.add(r);
                    //System.out.println(r);
                }
            }
        }
        //System.out.println("************Rune Search************");
        return res;
    }

    static public PetType getPet(String pet) {
        return pets.get(pet.trim().toLowerCase());
    }

    public static RuneSet getPetRune(int petId) {
        //System.out.println("Get Pet RUne : " + petId + " ; " + runes.size());
        List<RuneType> runeList = new ArrayList();
        for (int i = 0; i < 6; i++) {
            runeList.add(new RuneType());
        }

        for (RuneType r : runes) {
            if (r.monsterId == petId) {
                runeList.set(r.slot - 1, r);
            }
        }
        RuneSet s1 = new RuneSet(runeList);
        return s1;
    }

    static public PetType searchPets(String pet, Function<RuneSet, Double> damageMulty, double skillsUp) {
        PetType copper = getPet(pet);
        if (copper == null) {
            return null;
        }
        RuneSet s1 = getPetRune(copper.id);
        copper.currentEquip = s1;
        RuneSet.runePet = copper;
        copper.damageMulty = damageMulty;
        copper.skillsUp = skillsUp;

        //System.out.println("Petrune : " + s1.details());
        //System.out.println(copper);
        copper.applyRuneSet(s1);
        copper.showPetRune();
        // if (true) System.exit(0);

        return copper;
    }

    static public PetType searchPets(String pet) {
        return searchPets(pet, x -> (x.f_atk * 3), 30);
    }

    String getContent(String s) {
        if (s.contains("\"")) {
            int pos1 = s.indexOf("\"") + 1;
            return s.substring(pos1, s.length() - 1);
        }
        return s;
    }

    public static boolean isDouble(String s) {
        try {
            double d = Double.parseDouble(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Function<RuneSet, Double> detectMultiplaer(String text, String petName) {
        Function<RuneSet, Double> p1 = null;

        text = text.replace("ATTACK_DEF", "DEF");
        text = text.replace("ATTACK_SPEED", "SPD");
        text = text.replace("TARGET_TOT_HP", "EHP");
        text = text.replace("ATTACK_TOT_HP", "HP");
        text = text.replace("ATTACK_CUR_HP_RATE", "CHP");
        text = text.replace("TARGET_CUR_HP_RATE", "CHP");

        String s1 = text.replace("(", "");
        s1 = s1.replace(")", "");
        s1 = s1.replace("%", "");

        String[] ss = s1.split(",");

        if (s1.contains("SPD")) {
            //System.out.println("ss : "+Arrays.toString(ss)+" ; "+ss.length);        
        }
        //if (petName.equalsIgnoreCase("teshar"))
        //System.out.println(petName+" ; ss : "+Arrays.toString(ss)+" ; "+ss.length+"   ; "+text);
        if (petName.contains("Spectra")) {
            //System.out.println("ss : " + Arrays.toString(ss) + " ; " + ss.length + "   ; " + text);
        }

        if (ss.length == 3 && ss[0].equalsIgnoreCase("atk") && ss[1].equalsIgnoreCase("*") && isDouble(ss[2])) {
            p1 = x -> (x.f_atk * Double.parseDouble(ss[2]));
        }

        //atk*1.9+120
        if (ss.length == 5 && ss[0].equalsIgnoreCase("atk") && ss[1].equalsIgnoreCase("*") && isDouble(ss[2])
                && ss[3].equalsIgnoreCase("+") && isDouble(ss[4])) {
            p1 = x -> (x.f_atk * Double.parseDouble(ss[2]) + Double.parseDouble(ss[4]));
        }
        //def*1.9+120
        if (ss.length == 5 && ss[0].equalsIgnoreCase("def") && ss[1].equalsIgnoreCase("*") && isDouble(ss[2])
                && ss[3].equalsIgnoreCase("+") && isDouble(ss[4])) {
            p1 = x -> (x.f_def * Double.parseDouble(ss[2]) + Double.parseDouble(ss[4]));
        }

        if (ss.length == 1 && isDouble(ss[0])) {
            p1 = x -> (x.f_atk * Double.parseDouble(ss[0]) / 100);
        }
        //[380,*,ATK]
        if (ss.length == 3 && ss[2].equalsIgnoreCase("atk") && ss[1].equalsIgnoreCase("*") && isDouble(ss[0])) {
            p1 = x -> (x.f_atk * Double.parseDouble(ss[0]) / 100);
        }
        //[3,*,200,*,ATK]
        if (ss.length == 5 && ss[4].equalsIgnoreCase("atk") && ss[1].equals("*") && ss[3].equals("*") && isDouble(ss[0])) {
            p1 = x -> (x.f_atk * Double.parseDouble(ss[2]) / 100);
        }

        //550%,*,ATK,+,10%,*,EHP
        if (ss.length == 7 && ss[2].equalsIgnoreCase("atk") && ss[1].equalsIgnoreCase("*") && isDouble(ss[0]) && ss[6].equalsIgnoreCase("EHP")) {
            p1 = x -> (x.f_atk * Double.parseDouble(ss[0]) / 100 + x.enemy_hp * Double.parseDouble(ss[4]) / 100);
            //System.out.println("EHP : "+Arrays.toString(ss));
        }

        //[100% Attack,*,((Spd + 120) / 60)]
        if (ss.length == 9 && ss[2].equalsIgnoreCase("atk") && ss[1].equals("*") && isDouble(ss[0])
                && ss[4].equalsIgnoreCase("spd") && ss[5].equals("+") && isDouble(ss[6]) && ss[7].equals("/")) {
            p1 = x -> (x.f_atk * Double.parseDouble(ss[0]) / 100 * (x.f_spd + Double.parseDouble(ss[6])) / Double.parseDouble(ss[8]));
        }

        if (ss.length == 3 && ss[0].equalsIgnoreCase("def") && ss[1].equalsIgnoreCase("*") && isDouble(ss[2])) {
            p1 = x -> (x.f_def * Double.parseDouble(ss[2]));
        }
        if (ss.length == 3 && ss[0].equalsIgnoreCase("ATTACK_LV") && ss[1].equalsIgnoreCase("*") && isDouble(ss[2])) {
            p1 = x -> (x.pet_level * Double.parseDouble(ss[2]));
        }

        if (ss.length == 3 && ss[0].equalsIgnoreCase("HP") && ss[1].equalsIgnoreCase("*") && isDouble(ss[2])) {
            p1 = x -> (x.f_hp * Double.parseDouble(ss[2]));
            //System.out.println("EHP : "+Arrays.toString(ss));
        }

        //def*0,2+ehp*.0,3
        if (ss.length == 7 && ss[0].equalsIgnoreCase("def") && ss[1].equalsIgnoreCase("*") && isDouble(ss[2])
                && ss[4].equalsIgnoreCase("EHP") && ss[5].equalsIgnoreCase("*") && isDouble(ss[6])) {
            p1 = x -> (x.f_def * Double.parseDouble(ss[2]) + x.enemy_hp * Double.parseDouble(ss[6]));
            //System.out.println(petName+" : EHP : "+Arrays.toString(ss)+" hp*"+ Double.parseDouble(ss[6]));
            return p1;
        }

        //def*0,2+ehp*.0,3
        if (ss.length == 9 && ss[0].equalsIgnoreCase("atk") && ss[1].equalsIgnoreCase("*") && isDouble(ss[2])
                && ss[4].equalsIgnoreCase("CHP") && ss[5].equalsIgnoreCase("*") && isDouble(ss[6])
                && ss[7].equalsIgnoreCase("+") && isDouble(ss[8])) {
            p1 = x -> (x.f_atk * Double.parseDouble(ss[2]) * (Double.parseDouble(ss[6]) + Double.parseDouble(ss[8])));
            //System.out.println(petName+" : CHP : "+Arrays.toString(ss)+" atk*"+ (Double.parseDouble(ss[6])+Double.parseDouble(ss[8])));
            return p1;
        }

        //def*2 +hp*3
        if (ss.length == 7 && ss[0].equalsIgnoreCase("def") && ss[1].equalsIgnoreCase("*") && isDouble(ss[2])
                && ss[4].equalsIgnoreCase("HP") && ss[5].equalsIgnoreCase("*") && isDouble(ss[6])) {
            p1 = x -> (x.f_def * Double.parseDouble(ss[2]) + x.f_hp * Double.parseDouble(ss[6]));
            //System.out.println(petName+" : EHP : "+Arrays.toString(ss)+" hp*"+ Double.parseDouble(ss[6]));
            return p1;
        }

        //atk*0,2+ehp*.0,3
        if (ss.length == 7 && ss[0].equalsIgnoreCase("atk") && ss[1].equalsIgnoreCase("*") && isDouble(ss[2])
                && ss[4].equalsIgnoreCase("EHP") && ss[5].equalsIgnoreCase("*") && isDouble(ss[6])) {
            p1 = x -> (x.f_atk * Double.parseDouble(ss[2]) + x.enemy_hp * Double.parseDouble(ss[6]));
            //System.out.println(petName+" : EHP : "+Arrays.toString(ss)+" hp*"+ Double.parseDouble(ss[6]));
            return p1;
        }
        if (ss.length == 7 && ss[0].equalsIgnoreCase("atk") && ss[1].equalsIgnoreCase("*") && isDouble(ss[2])
                && ss[4].equalsIgnoreCase("HP") && ss[5].equalsIgnoreCase("*") && isDouble(ss[6])) {
            p1 = x -> (x.f_atk * Double.parseDouble(ss[2]) + x.f_hp * Double.parseDouble(ss[6]));
            //System.out.println(petName+" : EHP : "+Arrays.toString(ss)+" hp*"+ Double.parseDouble(ss[6]));
            return p1;
        }

        if (ss.length == 7 && ss[0].equalsIgnoreCase("atk") && ss[1].equalsIgnoreCase("*") && isDouble(ss[2])
                && ss[4].equalsIgnoreCase("def") && ss[5].equalsIgnoreCase("*") && isDouble(ss[6])) {
            p1 = x -> (x.f_atk * Double.parseDouble(ss[2]) + x.f_def * Double.parseDouble(ss[6]));
        }

        if (ss.length == 9 && ss[0].equalsIgnoreCase("atk") && ss[1].equals("*") && isDouble(ss[2])
                && ss[4].equalsIgnoreCase("spd") && ss[5].equals("+") && isDouble(ss[6]) && ss[7].equals("/")) {
            p1 = x -> (x.f_atk * Double.parseDouble(ss[2]) * (x.f_spd + Double.parseDouble(ss[6])) / Double.parseDouble(ss[8]));
        }
        if (ss.length == 9 && ss[0].equalsIgnoreCase("atk") && ss[1].equals("*") && isDouble(ss[2])
                && ss[4].equalsIgnoreCase("DICE") && ss[5].equals("*") && isDouble(ss[6]) && ss[7].equals("+") && isDouble(ss[8])) {
            p1 = x -> (x.f_atk * Double.parseDouble(ss[2]) * (3.5 * Double.parseDouble(ss[6]) + Double.parseDouble(ss[8])));
        }

        /*if (p1 == null) {
            if (ss.length >= 3 && ss[0].equalsIgnoreCase("atk") && ss[1].equalsIgnoreCase("*") && isDouble(ss[2])) {
                p1 = x -> (x.f_atk * Double.parseDouble(ss[2]));
            }
        }*/
        if (p1 == null) {
            //System.out.println("Faill : ss : "+Arrays.toString(ss)+" ; "+ss.length+"   ; "+text);
        }
        return p1;
    }

    String bombers[] = {"Seara", "Liebli", "Malaka", "Taurus", "Dover", "Jojo"};
    double bombersMulty[] = {5, 4, 4.2, 4.2, 4.2, 4};
    int bombersSkillUp[] = {30, 25, 25, 30, 30, 25};

    public static JSONObject mainJsonContent;

    public static void saveFile() {
        try {
            FileWriter file = new FileWriter("optimizer.json");
            file.write(mainJsonContent.toString());
            //System.out.println("Successfully Copied JSON Object to File...");
            file.close();
            //System.out.println("\nJSON Object: " + obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<RuneSkill> loadHomuSkills() {
        List<RuneSkill> res = new ArrayList();
        try {
            //System.out.println("Load homun skills !");
            //BufferedReader br = new BufferedReader(new FileReader("homuSkills.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(Bestiary.class.getResourceAsStream("/homuSkills.txt")));
            String line = "";
            while ((line = br.readLine()) != null) {
                int pos1 = line.indexOf(":");
                if (pos1 > 0) {
                    String name = line.substring(0, pos1);
                    name = name.replace("Upgraded ", "U");
                    name = name.replace("Basic ", "B");
                    name = name.replace(". Skill", "");
                    name = name.replace(" - ", "-");

                    String skill = line.substring(pos1 + 1).trim();
                    skill = skill.replace(" Atk", ",*,ATK");
                    skill = skill.replace(" Attack", ",*,ATK");
                    skill = skill.replace(" x ", ",*,");

                    skill = skill.replace(" * ", ",*,");
                    skill = skill.replace(" + ", ",+,");
                    skill = skill.replace(" / ", ",/,");
                    skill = skill.replace("% Enemy MAX HP", "%,*,EHP");

                    //System.out.println("skill : [" + skill + "]");
                    //System.out.println("name : " + name);
                    Function<RuneSet, Double> p2 = detectMultiplaer(skill, "Homunculus");
                    if (name.endsWith("Burn Soul")) {
                        p2 = x -> ((x.f_spd / 100 + (x.enemy_spd + 225) / 60) * x.f_atk);
                    }
                    if (p2 == null) {
                        //System.out.println("Cant detect skill : " + skill + " ; " + name);
                    } else {
                        RuneSkill r1 = new RuneSkill();
                        r1.skillName = name;
                        r1.skillMulty = skill;
                        r1.damageMultySkill = p2;
                        res.add(r1);
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String myTrim(String s) {
        return s.replace(String.valueOf((char) 160), " ").trim();
    }

    public static boolean isDigit(char p) {
        if (p <= '9' && p >= '0') {
            return true;
        }
        return false;
    }

    public void updateSkillInfo(String petName, RuneSkill r1, Bestiary.SkillWikiInfo sk) {
        String nameSkill = r1.skillName;
        if (nameSkill.equalsIgnoreCase("Unknown")) {
            return;
        }
        //for (Bestiary.SkillWikiInfo sk : pi.skills) {
        // if (sk.skill_name.equalsIgnoreCase(nameSkill) || sk.skill_name.startsWith(nameSkill)) {
        if (petName.equalsIgnoreCase("Jean")) {
            r1.extra_atk = 1.0;
        }
        //when boss have more hp, and no buff
        if (petName.equalsIgnoreCase("Astar")) {
            r1.extra_damage = 1.8;
        }
        if (petName.equalsIgnoreCase("Guillaume")) {
            r1.extra_cd = 0.5;
        }
        if (petName.equalsIgnoreCase("Glinodon")) {
            r1.extra_cd = 0.2;
        }
        if (petName.equalsIgnoreCase("Chow")) {
            r1.afterMulty = x -> (x.total_hp * 13.0 / 100);
        }
        String desc = sk.skill_desc.toLowerCase();
        //System.out.println("Found skill : "+nameSkill+":"+sk.multy);
        if (desc.contains("% for each harmful effect")
                || desc.contains("% increased damage if the enemy is under weakening effect")
                || desc.contains("% increased damage on enemies that are under harmful effect")) {
            //System.out.println("Increased dame : "+petName+" ; type="+r1.type+" :"+sk.skill_desc);                    
            int pos1 = desc.indexOf("% for each harmful effect");
            if (pos1 > 0) {
                String d2 = desc.substring(pos1 - 2, pos1);
                //System.out.println("Increase by : "+d2);
                r1.debuffScale = Integer.parseInt(d2);
            }
            int pos2 = desc.indexOf("% increased damage");
            if (pos2 > 0) {
                String d2 = desc.substring(pos2 - 2, pos2);
                //System.out.println("Increase by : "+d2);
                r1.debuffIncre = Integer.parseInt(d2);
            }

        }
        if (detectMultiplaer(r1.skillMulty, petName) == null) {
            //System.out.println("Update wiki : " + petName + " ; type=" + r1.type + " :" + sk.skill_desc);                    
            //System.out.println("Skill Multy : "+r1.skillMulty+" ; "+detectMultiplaer(r1.skillMulty, petName));
            //System.out.println("wiki multy : "+sk.tooltip);

            if (sk.tooltip.contains("% of ATK stat") || sk.tooltip.contains("% of the ATK stat")) {
                int pos1 = sk.tooltip.indexOf("% of");
                if (pos1 > 0) {
                    String s2 = sk.tooltip.substring(0, pos1);
                    String s3 = s2.substring(s2.lastIndexOf(" ") + 1);
                    if (StringUtil.isNumeric(s3)) {
                        int def = Integer.parseInt(s3);
                        //System.out.println(petName +" Atk : ["+def+"]");
                        r1.skillMulty = "((ATK,*," + (def / 100.0) + "))";
                        r1.damageMultySkill = detectMultiplaer(r1.skillMulty, petName);
                    }
                }
            }
            if (sk.tooltip.contains("% of the DEF stat")) {
                int pos1 = sk.tooltip.indexOf("% of the DEF stat");
                if (pos1 > 0) {
                    String s2 = sk.tooltip.substring(0, pos1);
                    String s3 = s2.substring(s2.lastIndexOf(" ") + 1);
                    int def = Integer.parseInt(s3);
                    //System.out.println(petName +" DeF : ["+def+"]");
                    r1.skillMulty = "((DEF,*," + (def / 100.0) + "))";
                    r1.damageMultySkill = detectMultiplaer(r1.skillMulty, petName);
                }
            }
            if (detectMultiplaer(r1.skillMulty, petName) != null) {
                //System.out.println(petName+" : "+r1.skillName+" ; Update skill from wikia : "+r1.skillMulty+" ; "+sk.tooltip);
            }
        }

        if (desc.contains("increase") && desc.contains("critical damage")) {
            //System.out.println("Increased crit dame : " + petName + " ; type=" + r1.type + " :" + sk.skill_desc);
            r1.extra_cd = 1.0;
        }
        if (desc.contains("increase") && desc.contains("critical hits by")) {
            //System.out.println("Increased crit dame : " + petName + " ; type=" + r1.type + " :" + sk.skill_desc);
            r1.extra_cd = 1.0;
        }

        if (desc.contains("attacks all enem") || desc.contains("attack all enem") || desc.contains("crushes all enem")
                || desc.contains("attacking all enemie") || desc.contains("attack all other enemi") || desc.contains("damage to all enem")) {
            if (r1.skillName.equalsIgnoreCase("trigger happy")) {
                r1.isAoeRandom = true;
            } else {
                r1.isAoe = true;
            }
            if (r1.skillName.equalsIgnoreCase("Tempest Sword")
                    || r1.skillName.equalsIgnoreCase("Blade Surge")) {
                r1.aoeIndex = 1.0 / 3;
            }
            //Taor
            if (r1.skillName.equalsIgnoreCase("Crush")) {
                r1.aoeIndex = 0.5;
            }
            if (r1.skillName.equalsIgnoreCase("Lightning of Cycle")) {
                r1.aoeIndex = 0.5;
            }
        }
        if (desc.contains("attacks randomly") || desc.contains("attack the enemies random")
                || desc.contains("attack all enemies random")) {
            r1.isAoeRandom = true;
            r1.isAoe = false;
        }
        if (desc.contains("attacks the enemies") && r1.isAoe == false) {
            r1.isAoeRandom = true;
        }
        //3rd skill of Tagaros Meteor is real Aoe
        if (r1.skillName.equalsIgnoreCase("Meteor")) {
            r1.isAoe = true;
        }
        r1.skillDesc = sk.skill_desc;
        if (desc.contains("ignore") || desc.contains("ignoring") || desc.contains("penetrate")) {
            if (!desc.contains("chance")
                    && !desc.contains("reduction") && !desc.contains("reduce")) //if (desc.contains("damage reduction "))
            {
                r1.ignoreDmg = true;
            } else if (r1.skillName.equalsIgnoreCase("rolling punch")) {
                r1.ignoreDmg = true;
            } else {
                if (desc.contains("chance")) {
                    r1.ignoreChance = true;
                } else {
                    r1.ignoreChloe = true;
                }
            }
            if (petName.equalsIgnoreCase("Reno")) {
                r1.ignoreDmg = false;
                r1.ignoreChance = true;
            }
            //System.out.println("Ignore skill : " + curpet.a_name + ":" + sk.skill_desc);
        }

        if (sk.multy.length() == 0) {
            r1.noWikiMulty = true;
            //System.out.println("No wiki skill multy : "+petName+" : "+r1);
        }
        if (sk.multy.contains("x")) {
            int pos = sk.multy.indexOf("x");
            if (isDigit(sk.multy.charAt(pos + 1))) {
                //System.out.println("Found skill : "+nameSkill+":"+sk.multy+" ; "+curpet.full_name);
                r1.numHits = Integer.parseInt("" + sk.multy.charAt(pos + 1));
                r1.hitStr = "x" + r1.numHits;
            }
            if (sk.tooltip.contains("~")) {
                int pos2 = sk.tooltip.indexOf("~");
                //System.out.println(sk.tooltip);
                if (isDigit(sk.tooltip.charAt(pos2 + 1)) && isDigit(sk.tooltip.charAt(pos2 - 1))) {
                    int t1 = Integer.parseInt("" + sk.tooltip.charAt(pos2 - 1));
                    int t2 = Integer.parseInt("" + sk.tooltip.charAt(pos2 + 1));
                    r1.numHits = (t1 + t2) / 2;
                    r1.hitStr = t1 + "~" + t2;
                    if (r1.skillName.equalsIgnoreCase("Thousand Shots")) {
                        r1.numHits = t2;
                    }
                }
            }
        }
        if ((desc.contains("random targets") || desc.contains("randomly attack"))
                && r1.numHits > 1) {
            r1.isAoeRandom = true;
        }

        if (r1.ignoreChance) {
            //System.out.println("Ignore chance : "+petName+" ; "+r1.skillDesc);                    
            if (petName.equalsIgnoreCase("Reno")) {
                r1.ignoreChanceNum = 72;
            }
            if (desc.contains("% chance")) {
                int pos1 = desc.indexOf("% chance");
                if (pos1 > 0) {
                    String d2 = desc.substring(pos1 - 2, pos1);
                    double t1 = 1.0;
                    for (int i = 0; i < r1.numHits; i++) {
                        t1 = t1 * (1 - Double.parseDouble(d2) / 100.0);
                    }
                    t1 = 1 - t1;
                    r1.ignoreChanceNum = Math.round(t1 * 100);
                    //System.out.println("Ignore chance : "+d2+" ; "+r1.ignoreChanceNum+"%");
                    //r1.debuffScale = Integer.parseInt(d2);
                }
            }
        }

        for (String s1 : sk.skill_up) {
            String s2 = s1.toLowerCase();
            if (s2.toLowerCase().contains("damage +")) {
                //System.out.println("Found dmg : "+s1);
                String s3 = s2.substring(s2.indexOf("+") + 1);
                if (s3.contains("%")) {
                    s3 = s3.substring(0, s3.indexOf("%"));
                }
                s3 = s3.replace("%", "").replace(".", "").replace("▒", "");
                s3 = myTrim(s3);
                //System.out.println("Dmg inc : ["+s3+"]");
                try {
                    r1.skillUp += Integer.parseInt(s3);
                } catch (Exception e) {
                    e.printStackTrace();
                    //System.out.println("Error : [" + s3 + "] ; [" + s2 + "] : " + petName);
                    System.exit(0);
                }
            }
        }

    }

    public static void addPet(String petName) {
        PetType p2 = petsBestiary.get(petName);
        JSONObject newone = new JSONObject();
        int newId = -1;
        for (int i = 1; i < 100000; i++) {
            if (!petsIds.containsKey(i)) {
                newId = i;
                break;
            }
        }
        if (p2 != null) {
            //System.out.println("Add pet : " + p2.name + " ; newId = " + newId);
            try {
                newone.put("name", p2.a_name);
                newone.put("attribute", p2.attribute);
                newone.put("level", 40);
                newone.put("stars", 6);
                newone.put("id", newId);
                for (int i = 0; i < PetType.petLabels.length; i++) {
                    newone.put(PetType.petLabels[i], p2.baseStats[i]);
                }
                //System.out.println("newone : " + newone);
                int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to to add this pet to your account : " + p2.name
                        + " ? \nBe carefull, this will change the json File, Better back it up!", "Confirm", JOptionPane.YES_NO_OPTION);
                if (dialogResult == 0) {
                    JSONArray monsJson = mainJsonContent.getJSONArray("mons");
                    monsJson.put(newone);
                    p2.id = newId;
                    pets.put(p2.name.toLowerCase(), p2);
                    petsIds.put(newId, p2);
                    saveFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean loadPets(String filename) {
        try {
            pets.clear();
            petsIds.clear();
            runesSimpleIds.clear();

            runes.clear();
            runesIds.clear();
            runeMaps.clear();

            Bestiary.getInstance();
            int pid = 0;
            for (Bestiary.PetInfo p1 : Bestiary.getInstance().allPets.values()) {
                String u1 = p1.uName + " (" + p1.element + ")";

                petFamily.put(u1, p1.aName);
                PetType p2 = new PetType(p1);
                p2.id = pid++;
                petsBestiary.put(p1.aName, p2);
                petsBestiary.put(u1, p2);

                skillPetInfo.put(p1.aName, p1);
                skillPetInfo.put(u1, p1);

                //String[] listE = {"Water", "Fire", "Wind","Light","Dark"};
                if (!petNameMapInv.containsKey(p1.aName)) {
                    //System.out.println("No code : "+p1.aName+" : "+p1.element+" : "+petNameElement.get(p1.element));
                    if (petNameMapInv.containsKey(p1.uName)) {
                        String code = petNameMapInv.get(p1.uName) + petNameElement.get(p1.element);
                        //System.out.println("family code : "+petNameMapInv.get(p1.uName));                        
                        //System.out.println("code : "+code);

                        petNameMap.put(code, p1.aName);
                        petNameMapInv.put(p1.aName, code);
                    }
                }
            }
            File f1 = new File(filename);
            if (!f1.exists()) {
                //System.out.println("No file exist !");
                return false;
            }

            String content = new String(Files.readAllBytes(Paths.get(filename)));
            //System.out.println("Content : " + content.length() + " ; " + filename);

            mainJsonContent = new JSONObject(content);

            if (!mainJsonContent.has("mons") && !mainJsonContent.has("unit_list")) {
                //System.out.println("Choose wrong file : " + filename);
                return false;
            }

            boolean optimizerFile;
            JSONArray monsJson;
            if (mainJsonContent.has("mons")) {
                monsJson = mainJsonContent.getJSONArray("mons");
                optimizerFile = true;
            } else {
                monsJson = mainJsonContent.getJSONArray("unit_list");
                optimizerFile = false;
            }
            JSONArray runesJson = mainJsonContent.getJSONArray("runes");

            //System.out.println("mons : " + monsJson.length());
            //System.out.println("runes : " + runesJson.length());

            boolean failRune = false;

            Set<String> failSet = new HashSet();

            List<RuneSkill> loadedHomunSkill = loadHomuSkills();
            int runeIndex = 1;
            for (int i = 0; i < monsJson.length(); i++) {
                PetType p1 = new PetType(monsJson.getJSONObject(i), optimizerFile, i + 1, runeIndex);

                /*if (p1.stars < (6 - ConfigInfo.getInstance().loadPetLevel)) {
                    continue;
                }*/
                String name = p1.name;
                //System.out.println("Name : "+p1.name);
                if (p1.name == null) {
                    continue;
                }
                if (pets.containsKey(p1.name.trim().toLowerCase())) {
                    PetType oldone = pets.get(p1.name.trim().toLowerCase());
                    p1.name = p1.name + "2";
                    //System.out.println("Found second : " + p1);
                    if (pets.containsKey(p1.name.trim().toLowerCase())) {
                        p1.name = p1.name.replace("2", "3");

                    }
                    for (RuneType r1 : p1.equipRuneList) {
                        r1.monster = p1.name;
                    }
                    p1.skillItem = oldone.skillItem;
                }

                //System.out.println(p1.oname);
                Bestiary.PetInfo pi = skillPetInfo.get(p1.oname);
                if (pi != null) {
                    p1.u_name = pi.uName;
                    p1.a_name = pi.aName;
                    p1.full_name = p1.a_name + " - " + p1.u_name + " (" + pi.element + ")";
                } else {
                    //System.out.println("No bestiary : "+p1.oname);
                }

                pets.put(p1.name.trim().toLowerCase(), p1);
                petsIds.put(p1.id, p1);

                //for unawake and Homu
                if (p1.name.contains("Homunculus")) {
                    p1.skillList = loadedHomunSkill;
                    p1.skillItem = p1.skillList.get(0);
                    //System.out.println("Found homoculus : " + p1.skillList.size());
                    p1.a_name = "Homunculus";
                    p1.u_name = "Homunculus";
                    p1.full_name = "Homunculus (" + p1.attribute + ")";
                }
                if (p1.name.contains("(") || p1.name.contains("Unknown")) {
                    p1.skillItem.skillMulty = "(ATK*3)";
                    p1.skillItem.skillName = "Unknown";
                }
                if (p1.level < 40 && ConfigInfo.getInstance().upPet40) {
                    PetType p2 = petsBestiary.get(p1.oname);

                    if (p2 != null) {
                        //System.out.println("Update pet stat : "+p1.name+" ; "+p1.level);
                        p1.baseStats = p2.baseStats;
                        p1.updatePetBaseStat();
                    }
                }

                for (RuneType r1 : p1.equipRuneList) {
                    runes.add(r1);
                    runesIds.put(r1.uniqueId, r1);
                    runesSimpleIds.put(r1.id, r1);
                    runeMaps.put("" + r1.id, r1);
                    runeIndex++;
                }
            }

            for (int i = 0; i < runesJson.length(); i++) {

                RuneType r1 = new RuneType(runesJson.getJSONObject(i), optimizerFile, runeIndex + i);
                //System.out.println(petsIds.size()+" ; "+r1.monsterId+" ; "+petsIds);
                //System.exit(0);
                if (r1.monsterId > 0 && petsIds.containsKey(r1.monsterId)) {
                    PetType p1 = petsIds.get(r1.monsterId);
                    r1.monster = p1.name;
                }

                //Do not load unknown set Rune , for new runes set !
                if (r1.runeType.equals("???")) {
                    r1.runeType = "Fight";
                    r1.runeTypeIndex = 16;
                    failRune = true;
                    failSet.add(r1.uniqueId);
                    //continue;
                }
                runes.add(r1);
                runesIds.put(r1.uniqueId, r1);
                runesSimpleIds.put(r1.id, r1);
                runeMaps.put("" + r1.id, r1);

                if (r1.monsterId > 0) {
                    PetType p1 = petsIds.get(r1.monsterId);
                    if (p1 != null) {
                        p1.runesEquip++;
                        p1.equipRuneList.add(r1);
                    }
                }
            }

            //System.out.println("failRune : " + failRune + " ; " + failSet + " ; filename : " + filename);
            if (failRune) {
                //158523-optimizer
                //158523-swarfarm
                if (filename.contains("-optimizer")) {
                    String name2 = filename.replace("-optimizer", "-swarfarm");
                    if (!(new File(name2).exists())) {
                        name2 = filename.replace("-optimizer", "");
                    }
                    if ((new File(name2).exists())) {
                        String contentNew = new String(Files.readAllBytes(Paths.get(name2)));
                        JSONObject js = new JSONObject(contentNew);
                        JSONArray rj = js.getJSONArray("runes");
                        //System.out.println("name2 : " + name2);
                        //System.out.println("contentNew : "+contentNew);
                        for (int i = 0; i < rj.length(); i++) {
                            JSONObject jo1 = rj.getJSONObject(i);
                            if (failSet.contains(jo1.getString("rune_id"))) {
                                //System.out.println("Found problem : " + jo1.getString("rune_id") + " : set_id = " + jo1.getInt("set_id"));
                                RuneType r1 = runesIds.get(jo1.getString("rune_id"));

                                if (jo1.getInt("set_id") == 19) {
                                    r1.runeType = "Fight";
                                }
                                if (jo1.getInt("set_id") == 20) {
                                    r1.runeType = "Determination";
                                }
                                if (jo1.getInt("set_id") == 21) {
                                    r1.runeType = "Enhance";
                                }
                                if (jo1.getInt("set_id") == 22) {
                                    r1.runeType = "Accuracy";
                                }
                                if (jo1.getInt("set_id") == 23) {
                                    r1.runeType = "Tolerance";
                                }
                                r1.runeTypeIndex = RuneType.getSetId(r1.runeType);
                                r1.jsonData.put("set", r1.runeType);
                            }
                        }
                        SwManager.saveFile();
                    }
                }
            }

            for (PetType p1 : pets.values()) {
                if (p1.runesEquip > 0) {
                    p1.currentEquip = new RuneSet(p1.equipRuneList);
                    p1.currentEquip.equipOnPet(p1);
                }
            }

            //BufferedReader br = new BufferedReader(new FileReader("damageMulty.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(Bestiary.class.getResourceAsStream("/damageMulty.txt")));

            String sCurrentLine;
            int lineIndex = 0;

            String nameSkill = "", namePet = "", type = "1", skill;
            PetType curpet = null;
            PetType curpet2 = null;
            PetType curpet3 = null;
            while ((sCurrentLine = br.readLine()) != null) {
                //System.out.println(sCurrentLine);
                if (sCurrentLine.contains("name") || sCurrentLine.contains("description_en") || sCurrentLine.contains("type")
                        || sCurrentLine.contains("multiplier")) {
                    if (sCurrentLine.contains("name")) {
                        namePet = getContent(sCurrentLine);
                        //System.out.println("pet : "+namePet+" : "+getPet(namePet));
                        curpet = getPet(namePet);
                        curpet2 = getPet(namePet + "2");
                        curpet3 = getPet(namePet + "3");
                        if (curpet == null && petsBestiary.containsKey(namePet)) {
                            curpet = petsBestiary.get(namePet);
                        } else if (curpet != null && petsBestiary.containsKey(namePet)) {
                            petsBestiary.get(namePet).skillList = curpet.skillList;
                        } else {
                            //System.out.println("Cant find this pet : "+namePet);
                        }
                    }
                    if (sCurrentLine.contains("type")) {
                        type = "" + sCurrentLine.charAt(sCurrentLine.length() - 1);
                        //System.out.println("Type : ["+type+"] : "+sCurrentLine);
                    }
                    if (sCurrentLine.contains("description_en")) {
                        nameSkill = getContent(sCurrentLine);
                    }
                    if (sCurrentLine.contains("multiplier")) {
                        skill = getContent(sCurrentLine);
                        if (skill.length() > 4) {
                            skill = skill.replace("[", "(");
                            skill = skill.replace("]", ")");
                            //skill=skill.replace(",","");
                            skill = skill.replace("\"", "");
                            skill = skill.replace("\\", "");
                            skill = skill.replace(" ", "");
                            skill = skill.replace("(+)", "+");
                            skill = skill.replace("(*)", "*");
                            //System.out.println(namePet + " : " + skill);

                            if (curpet != null) {
                                if (nameSkill.contains("unknown")) {
                                    continue;
                                }
                                if (curpet.name.contains("Copper")) {
                                    //System.out.println("skill : " + skill + " ; " + curpet.name);
                                }
                                if (skill.contains("DEF")) {
                                    curpet.defDame = true;
                                    curpet.def_buff = 1.7;
                                    curpet.atk_buff = 1.0;
                                    curpet.atk_leader = 0;
                                    curpet.def_leader = 40;
                                }
                                RuneSkill r1 = new RuneSkill();

                                r1.skillName = nameSkill;
                                r1.skillMulty = skill;
                                r1.damageMultySkill = detectMultiplaer(skill, curpet.name);
                                //System.out.println(curpet.name + " : type : [" + type + "]");
                                try {
                                    r1.type = Integer.parseInt(type);
                                } catch (Exception e) {
                                    //System.out.println("Current line : " + sCurrentLine);
                                    e.printStackTrace();
                                    System.exit(0);
                                }
                                if (r1.damageMultySkill == null) {
                                    //System.out.println("Fail to detect skill : "+nameSkill+":"+skill+" ; "+curpet.name);
                                } else {
                                    curpet.skillItem = r1;
                                    if (curpet2 != null) {
                                        curpet2.skillItem = r1;
                                        if (skill.contains("DEF")) {
                                            curpet2.defDame = true;
                                            curpet2.def_buff = 1.7;
                                            curpet2.atk_buff = 1.0;
                                            curpet2.atk_leader = 0;
                                            curpet2.def_leader = 40;
                                        }
                                    }
                                    if (curpet3 != null) {
                                        curpet3.skillItem = r1;
                                        if (skill.contains("DEF")) {
                                            curpet3.defDame = true;
                                            curpet3.def_buff = 1.7;
                                            curpet3.atk_buff = 1.0;
                                            curpet3.atk_leader = 0;
                                            curpet3.def_leader = 40;
                                        }
                                    }
                                }

                                Bestiary.PetInfo pi = skillPetInfo.get(curpet.oname);
                                if (pi != null) {
                                    for (Bestiary.SkillWikiInfo sk : pi.skills) {
                                        if (sk.skill_name.equalsIgnoreCase(nameSkill) || sk.skill_name.startsWith(nameSkill)) {
                                            updateSkillInfo(curpet.a_name, r1, sk);
                                        }
                                    }
                                }

                                if (curpet.name.contains("Hwa")) {
                                    //System.out.println("Skill : " + curpet.name + " ; " + r1);
                                }
                                curpet.skillList.add(r1);
                                if (curpet2 != null) {
                                    curpet2.skillList.add(r1);
                                }
                                if (curpet3 != null) {
                                    curpet3.skillList.add(r1);
                                }
                            }
                        }
                    }
                }
            }

            for (Bestiary.PetInfo p1 : Bestiary.getInstance().allPets.values()) {
                if (p1.aName.startsWith("Homun")) {
                    PetType p2 = petsBestiary.get(p1.aName);
                    //System.out.println(p2.u_name+" : "+p2.name);
                    petFamily.put(p2.u_name + " (" + p2.attribute + ")", p2.name);

                    p2.skillList.clear();

                    //System.out.println("Pet : " + p1.aName);
                    for (Bestiary.SkillWikiInfo pk : p1.skills) {
                        if (pk.skill_name.contains("Passive")) {
                            continue;
                        }
                        boolean found = false;
                        for (RuneSkill r1 : loadedHomunSkill) {
                            if (r1.skillName.replace(" ", "").endsWith(pk.skill_name.replace(" ", ""))) {
                                //System.out.println("Compare : [" + pk.skill_name + "] vs " + r1.skillName+" ; "+pk.skill_desc);
                                found = true;
                                updateSkillInfo(p1.aName, r1, pk);
                                p2.skillList.add(r1);
                            }
                        }
                        if (!found) {
                            //System.out.println("Unfound : " + pk.skill_name);
                        }
                    }
                }
            }

            int i1 = 0;
            for (String s1 : bombers) {
                PetType p1 = pets.get(s1.toLowerCase());
                //System.out.println(s1 + " : " + p1);
                if (p1 != null) {
                    //bomb skill often skill 2;
                    RuneSkill p2 = p1.skillList.get(1);
                    p2.isBomb = true;
                    p2.ignoreDmg = true;
                    //p1.skillItem.skillMulty = "ATK*"+bombersMulty[i1];
                    p2.skillValue = bombersMulty[i1];
                }
                i1++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    static public BufferedImage crawlPetPicture(String petName) {
        //http://summonerswar.wikia.com/wiki/Perna
        try {
            if (petName.contains("Unknown")) {
                return crawlPetPicture("unknown");
            }
            if (petName.contains("2")) {
                petName = petName.replace("2", "");
            }
            if (petName.contains("3")) {
                petName = petName.replace("3", "");
            }
            long l1 = System.currentTimeMillis();
            String existingPath = "/imgs/" + petName + ".png";

            try {
                if (new File(existingPath).isFile()) {
                    BufferedImage image = ImageIO.read(new File(existingPath));
                    return image;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //unawaken
            if (petName.contains("(")) {
                petName = petName.replace(" ", "_");
            }
            String data = Resources.toString(new URL("http://summonerswar.wikia.com/wiki/" + petName), Charsets.UTF_8);
            //System.out.println("Load from wiki : " + data);

            Document doc = Jsoup.parse(data);
            Elements e = doc.select("div.monstertable a");
            String url = e.get(1).attr("href");
            //System.out.println(url);
            BufferedImage image = ImageIO.read(new URL(url));
            //System.out.println("Image size : " + image.getHeight() + " x " + image.getWidth());
            ImageIO.write(image, "png", new File(existingPath));

            //System.out.println("Done in " + (System.currentTimeMillis() - l1));

            return image;
        } catch (Exception e) {
            //e.printStackTrace();
            return crawlPetPicture("unknown");
        }
        //return null;
    }

    public static void generateValues(int begin, int last) {
        double incre = (last - begin) / (double) 14;
        for (int i = 0; i < 15; i++) {
            double p1 = begin + i * incre;
            int v = (int) Math.floor(begin + i * incre);
            if ((p1 - Math.floor(p1)) > 0.43) {
                v++;
            }
            //System.out.println(i + " : " + v + " : " + (begin + i * incre));
        }
    }

    //grade4*-6*
    public static int get135MainStat(int level, int grade, int slot) {
        int[] lv15 = {92, 135, 160};
        int[] lv_hp_15 = {1704, 2088, 2448};
        if (level == 15) {
            if (slot < 4) {
                return lv15[grade - 4];
            }
            return lv_hp_15[grade - 4];
        }
        int[] begin = {10, 15, 22};
        int[] begin_hp = {160, 270, 360};
        int[] incre = {6, 7, 8};
        int[] incre_hp = {90, 105, 120};

        if (slot < 4) {
            return begin[grade - 4] + level * incre[grade - 4];
        }

        return begin_hp[grade - 4] + level * incre_hp[grade - 4];
    }

    public static void main(String[] args) {
        SwManager.getInstance().loadPets("optimizer.json");

        /*generateValues(8,54);
        generateValues(4,36);
        generateValues(5,39);
        generateValues(8,43);*/
        for (RuneType r1 : runes) {
            if (r1.slot % 2 == 1 && r1.grade >= 4) {
                if (r1.mainStatVal != get135MainStat(r1.level, r1.grade, r1.slot)) {
                    //System.out.println("Found problem : " + r1);
                }
            }
        }

        //System.out.println("Finish done !");
        /*for (int i = 0;i<RuneType.slabelsMain.length;i++){
            Map <String,Integer> runestat= new HashMap();
            for (RuneType r1:runes){
                if (r1.mainStatIndex==i && r1.level==15){
                    String s1=RuneType.slabelsMain[i]+"_lv"+r1.level+"_"+r1.grade+"*";
                    runestat.put(s1, r1.mainStatVal);
                }
            }
            System.out.println("runestat : "+i+" : "+RuneType.slabelsMain[i]+" : "+runestat);
        }*/

        //SwManager.getInstance().loadPets("D:\\SWProxy-windows\\158523-optimizer.json");
        //loadHomuSkills();
        //SwManager.getInstance().testPests();
        //SwManager.getInstance().crawlPetPicture("Perna");
        //ConfigInfo.getInstance().saveFile();
        // new SwManager().loadPets("runes.json");
    }
}

// Lushen spd build spd 172, cr 82, cd 198
/*
 * Best build : 1 : ___ : {RES=8, ATK%=13%, CRate=12, SPD=5, ATK flat=118, HP%=15%} ; [lv_12, 6*, id_541, Rage, Kahli] 2 : SPD : {ACC=4,
 * RES=4, ATK%=7%, CDmg=5, CRate=15, SPD=42} ; [lv_15, 6*, id_565, Focus, Spectra] 3 : ___ : {DEF flat=78, ACC=3, CDmg=16, CRate=4} ; [lv_9,
 * 5*, id_608, Rage, Raki] 4 : CDmg : {ATK%=22%, CDmg=80, CRate=4, SPD=11, DEF%=8%} ; [lv_15, 6*, id_698, Rage, Lushen] 5 : ___ : {DEF
 * flat=14, ACC=7, CRate=15, SPD=5, HP flat=1530, ATK flat=15} ; [lv_12, 5*, id_729, Rage, Eshir] 6 : ATK% : {ACC=6, ATK%=63%, CDmg=7,
 * CRate=17, SPD=6} ; [lv_15, 6*, id_760, Endure, Lanett] runesets : {DEF flat=92, ACC=20, RES=12, ATK%=105, CDmg=148, CRate=67, SPD=69, HP
 * flat=1530, ATK flat=133, HP%=15, DEF%=8} : [Rage ] : Total : 69 Combo : {acc=20, cd=50+148, res=15+12, def=461+128, spd=103+69,
 * hp=9225+2913, atk=900+1078, cr=15+67} Final : {acc=20, cd=198, res=27, def=589, spd=172, hp=12138, atk=1978, cr=82}
 */
