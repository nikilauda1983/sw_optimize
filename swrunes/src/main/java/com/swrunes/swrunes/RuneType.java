package com.swrunes.swrunes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.json.JSONArray;

import org.json.JSONObject;

public class RuneType implements Serializable {

    private static final long serialVersionUID = 1;

    public static int[] setBonnusValue = {15, 15, 35, 40, 12, 20, 20, 25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static int[] setBonnusNum = {2, 2, 4, 4, 2, 2, 2, 4, 4, 4,
        2, 2, 2, 2, 4, 2, 2, 2, 2, 2, 2, 0};
    public static String[] setBonnusLabel = {"Energy", "Guard", "Fatal", "Rage", "Blade", "Focus", "Endure", "Swift", "Violent", "Despair",
        "Will", "Nemesis", "Revenge", "Destroy", "Vampire", "Shield", "Fight", "Determination", "Enhance", "Accuracy", "Tolerance", "All Broken"};
    public static int[] setBonnusLabelMapping = { -1, 0, 1, 7, 4, 3, 5, 6, 2, -1, 9, 14, -1, 8, 11, 10, 15, 12, 13, 16, //20
        17, 18, 19, 20, 20};

    public static int[][] setRuneMaxValue = {{43, 51, 63}, {43, 51, 63}, {43, 51, 63}, {58, 65, 80}, {41, 47, 58}, {44, 51, 64},
    {44, 51, 64}, {30, 39, 42}};
    public static int[][] setRuneMaxValue12 = {{31, 37, 47}, {31, 37, 47}, {31, 37, 47}, {42, 48, 59}, {30, 34, 43}, {32, 38, 48},
    {32, 38, 48}, {22, 29, 31}};
    public static int[][] setRuneMaxValue15 = {{112, 135, 160}, {112, 135, 160}, {1704, 2088, 2448}};
    public static int[][] setRuneMaxValue12_135 = {{82, 99, 118}, {82, 99, 118}, {1240, 1530, 1800}};

    public static int ENERGY_SET = 0, GUARD_SET = 1, FATAL_SET = 2, RAGE_SET = 3, BLADE_SET = 4, FOCUS_SET = 5, ENDURE_SET = 6, SWIFT_SET = 7;
    public static int HP = 0, DEF = 1, ATK = 2, CD = 3, CR = 4, ACC = 5, RES = 6, SPD = 7;
    public static String[] slabels = {"hp", "def", "atk", "cd", "cr", "acc", "res", "spd"};
    

    public static String[] slabelsMainDisplay = {
        "HP%", "DEF%", "ATK%", "CDmg", "CRate", "ACC", "RES", "SPD"};
    public static String[] slabelsMain = {
        "HP%", "DEF%", "ATK%", "CDmg", "CRate", "ACC", "RES", "SPD",
        "HP flat", "DEF flat", "ATK flat", "cd", "cr", "acc", "res", "spd"};

    public static String[] slabelsMain2 = {
        "HP%", "DEF%", "ATK%", "CDmg", "CRate", "ACC", "RES", "SPD",
        "HP flat", "DEF flat", "ATK flat"};
    public static int[] MinStatValue={5,5,5,4,4,4,4,4,135,10,10};
    public static int[] MaxStatValue={8,8,8,7,6,8,8,6,375,20,20};

    public static String[] slabelsFix = {
        "sub_hpp", "sub_defp", "sub_atkp", "sub_cdmg", "sub_crate", "sub_acc", "sub_res", "sub_spd",
        "sub_hpf", "sub_deff", "sub_atkf", "cd", "cr", "acc", "res", "spd"};
    
    public static int[] slabelsMapping = {
        -1, 8, 0, 10, 2, 9, 1, -1, 7, 4, 3, 6, 5
    };

    public static boolean sameStat(int id1, int id2) {
        if (id1 == id2) {
            return true;
        }
        if (id1 < 3 && id2 < 3) {
            return true;
        }
        if ((id1 == 5 || id1 == 6) && (id2 == 5 || id2 == 6)) {
            return true;
        }
        if ((id1 == 9 || id1 == 10) && (id2 == 9 || id2 == 10)) {
            return true;
        }

        return false;
    }
    public int statfix[] = new int[slabelsFix.length];

    public boolean optimizerFile = false;
    boolean recured = false;
    public String jsonString = "";
    public String mainStat = "";
    public String mainStatGui = "";
    public List<String> subStatGuis = new ArrayList();

    public List<Integer> subStat1 = new ArrayList();
    public List<Integer> subStat2 = new ArrayList();
    public List<Integer> grinds = new ArrayList();
    public List<Boolean> enchanted = new ArrayList();
    int mt = -1;

    List<Integer> getParaList(String list, int value) {
        List<Integer> l1 = new ArrayList();
        String[] s1 = list.split(",");

        boolean runeok = false;

        for (String s2 : s1) {
            if (s2.length() > 0) {
                for (int i = 0; i < slabels.length; i++) {
                    if (s2.equalsIgnoreCase(slabels[i])) {
                        if (statfix[i] >= value) {
                            runeok = true;
                        }
                        l1.add(statfix[i]);
                    }
                }
            }
        }
        if (runeok) {
            return l1;
        }

        return null;
    }

    public static Set<String> divideSet(String mainSet) {
        String second = "";
        if (mainSet.contains(",")) {
            int pos1 = mainSet.indexOf(",");
            second = mainSet.substring(pos1);
            mainSet = mainSet.substring(0, pos1);
        }
        String[] list = mainSet.split("/");
        Set<String> set2 = new HashSet();
        for (String s3 : list) {
            set2.add(s3 + second);
        }
        return set2;

        //return null;
    }

    public static int getSetId(String mainSet) {
        if (mainSet.contains(",")) {
            mainSet = mainSet.split(",")[0];
        }
        mainSet = mainSet.replace("x2", "");
        mainSet = mainSet.replace("x3", "");

        for (int i = 0; i < setBonnusLabel.length; i++) {
            if (setBonnusLabel[i].equalsIgnoreCase(mainSet)) {
                return i;
            }
        }
        return -1;
    }

    public static int getSecondSetId(String mainSet) {
        if (mainSet.contains(",")) {
            String[] ls = mainSet.split(",");
            if (ls.length >= 2) {
                mainSet = mainSet.split(",")[1];
                for (int i = 0; i < setBonnusLabel.length; i++) {
                    if (setBonnusLabel[i].equalsIgnoreCase(mainSet)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public Map<String, Integer> statfixMap = new HashMap();
    public Map<String, String> statDisplayMap = new HashMap();

    Map<String, String> info = new HashMap();

    public int legend;//normal,rare,magic,hero,legend...
    public int grade; //stars 5* or 6*
    public int slot = 1;
    public String runeType, monster;
    public int monsterId;
    public int runeTypeIndex = -1;

    public String uniqueId;
    public int level = 0;
    public int spd, cr, cd, res, acc, atk, def, hp, id;
    public int mainStatVal = -1;
    public int originMainStatVal = -1;
    public String optionStatGui = "";
    public int mainStatIndex = -1;
    public int iv = -1;
    public int it = -1;

    public int getSingleRarity(int i) {
        int v1 = subStat1.get(i);
        int t1 = subStat2.get(i) - grinds.get(i);
        if (enchanted.get(i)) {
            return 0;
        }
        int g = MaxStatValue[v1];
        int d2 = t1 / g;
        if (t1%g==0)d2--;
        return d2;
    }
    public int getOriginRarity(){
        if (level<12 && subStat1.size()==4)
            return 5;
        if (level>=12 && grade==6){
            int v1=subStat1.get(3);
            int t1=subStat2.get(3)-grinds.get(3);
            if (enchanted.get(3))
                t1=0;
            System.out.println("************************");
            System.out.println("Last sub v1 : "+v1+slabelsMain2[v1]);
            System.out.println("Last sub value : "+t1 +" ; Max value : "+MaxStatValue[v1]);
            if (t1>MaxStatValue[v1]){
                return 5;
            }
            
            int sum=0;
            for (int i=0;i<3;i++){
                v1=subStat1.get(i);
                t1=subStat2.get(i)-grinds.get(i);
                if (enchanted.get(i))
                    t1=0;
                int g=MaxStatValue[v1];
                int d2=t1/g;
                if (t1%g==0)d2--;
                if (d2>0){
                    sum+=d2;
                }
                
            }
            System.out.println("Sum 100%: "+sum);
            if (sum>3) return 5;
            
            sum=0;
            for (int i=0;i<3;i++){
                v1=subStat1.get(i);
                t1=subStat2.get(i)-grinds.get(i);
                if (enchanted.get(i))
                    t1=0;
                if (t1<=MaxStatValue[v1]) continue;
                int g=Math.round(((float)MaxStatValue[v1]+MinStatValue[v1])/2);    
                
                
                int d2=t1/g;
                if (t1%g==0)d2--;
                int minTotal=(d2+1)*(MinStatValue[v1]);
                if (t1<=(minTotal+d2)) d2--;
                int tg=(d2+1)*(MaxStatValue[v1]+MinStatValue[v1])/2;
                if (t1<(tg-1))d2--;
                System.out.println(slabelsMain2[v1]+" ; avg = "+g+" ; d2="+d2+" ; Mintotal ="+minTotal+" val = "+t1+" avg Total = "+(tg));
                if (d2>0){
                    sum+=d2;
                }                
            }
            System.out.println("Sum avg: "+sum);
            if (sum>3) return 4;
        }
        return 0;
    }
    public int[] getSetBonnus(String setType) {
        int setBonnus[] = new int[slabelsFix.length];

        return setBonnus;
    }

    public int futureMainStat;

    public void saveEquipInfo(int monsterId, String name) {
        this.monsterId = monsterId;
        this.monster = name;
        try {
            jsonData.put("monster", monsterId);
            jsonData.put("monster_n", name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject jsonData = null;

    public static int detectSubStat(String s2) {
        int ti = -1;
        for (int k2 = 0; k2 < slabelsMain.length; k2++) {
            if (slabelsMain[k2].equals(s2)) {
                ti = k2;
                break;
            }
        }
        return ti;
    }

    public void upateRuneEdit() {
        JSONObject p1 = jsonData;
        try {
            for (int i = 1; i <= 4; i++) {
                String s = "s" + i + "_v";
                String s1 = "s" + i + "_t";
                if (p1.getString(s).length() > 0) {
                    int d1 = Integer.parseInt(p1.getString(s));
                    if (d1 > 0) {
                        String s2 = p1.getString(s1);
                        int ti = detectSubStat(s2);
                        String s_fix = slabelsFix[ti];
                        jsonData.put(s_fix, "-");
                    }
                }
            }

            for (int i = 0; i < 4; i++) {
                String s_t1 = "";
                String s_v1 = "";
                if (i < subStat1.size()) {
                    s_t1 = slabelsMain[subStat1.get(i)];
                    s_v1 = "" + subStat2.get(i);
                    this.statfix[subStat1.get(i)] = subStat2.get(i);
                    String s_fix = slabelsFix[subStat1.get(i)];
                    jsonData.put(s_fix, s_v1);
                }

                jsonData.put("s" + (i+1) + "_v", s_v1);
                jsonData.put("s" + (i+1) + "_t", s_t1);

                jsonData.put("edited", true);
                jsonData.put("level", level);
                jsonData.put("m_v", mainStatVal);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Json : " + jsonData);
        //loadJson(jsonData);
    }

    public boolean isEdited = false;

    public void updateSubStats() {
        subStatGuis.clear();
        for (int i = 0; i < subStat1.size(); i++) {
            boolean flat = false;
            String s2 = slabelsMain[subStat1.get(i)];
            int d1 = subStat2.get(i);
            if (s2.contains("flat")) {
                flat = true;
            }
            if (s2.contains("SPD")) {
                flat = true;
            }

            s2 = s2.replace(" flat", "");
            if (s2.contains("%")) {
                s2 = s2.replace("%", "");
                //s2 = s2 + "%";
            }
            s2 += " +" + d1;
            if (!flat) {
                s2 = s2 + "%";
            }
            //System.out.println(s2);
            s2 = s2.replace("CRate", "CRt");
            s2 = s2.replace("CDmg", "CDm");
            subStatGuis.add(s2);
        }
    }

    public RuneType(JSONObject p1, boolean optimizerFile, int index) {
        loadJson(p1, optimizerFile, index);
    }

    public void loadJson(JSONObject p1, boolean optimizerFile, int index) {
        try {
            //System.out.println("rune : "+p1);
            //System.out.println(p1.get("b_spd"));
            jsonData = p1;
            jsonString = p1.toString();
            this.optimizerFile = optimizerFile;
            
            subStat1.clear();
            subStat2.clear();
            grinds.clear();
            enchanted.clear();

            for (int i = 0; i < slabelsFix.length; i++) {
                    statfix[i] = 0;
            }
            if (optimizerFile) {
                for (int i = 0; i < slabelsFix.length; i++) {
                    if (!p1.has(slabelsFix[i])) {
                        continue;
                    }

                    String s1 = p1.getString(slabelsFix[i]);
                    if (!s1.contains("-")) {
                        statfix[i] = p1.getInt(slabelsFix[i]);
                        statfixMap.put(slabelsMain[i], statfix[i]);
                    }
                }
            } else if (p1.has("sec_eff")) {
                JSONArray subs = p1.getJSONArray("sec_eff");
                for (int i = 0; i < subs.length(); i++) {
                    JSONArray subValues = subs.getJSONArray(i);
                    int statType = slabelsMapping[subValues.getInt(0)];
                    int origValue = subValues.getInt(1);
                    boolean gemmed = subValues.getInt(2) != 0;
                    int grindValue =  subValues.getInt(3);
                    statfix[statType] = origValue + grindValue;
                    statfixMap.put(slabelsMain[statType], statfix[statType]);
                    subStat1.add(statType);
                    subStat2.add(origValue + grindValue);
                    grinds.add(grindValue);
                    enchanted.add(gemmed);
                }
            }

            
            String mainType = (optimizerFile)? p1.getString("m_t") : slabelsMain2[slabelsMapping[p1.getJSONArray("pri_eff").getInt(0)]];
            String newVal = "";

            slot = (optimizerFile) ? p1.getInt("slot") : p1.getInt("slot_no");
            grade = (optimizerFile)? p1.getInt("grade") : p1.getInt("class");
            level = (optimizerFile) ? p1.getInt("level") : p1.getInt("upgrade_curr");

            for (int i = 0; i < slabelsMain.length; i++) {
                if (!mainType.equals(slabelsMain[i])) {
                    continue;
                }

                String s1 = (optimizerFile)? p1.getString("m_v") : p1.getJSONArray("pri_eff").getString(1);
                if (!s1.contains("-")) {
                    mainStatIndex = i;
                    statfix[i] = (optimizerFile)? p1.getInt("m_v") : p1.getJSONArray("pri_eff").getInt(1);
                    mainStatVal = statfix[i];
                    futureMainStat = mainStatVal;
                    originMainStatVal = mainStatVal;

                    int updateRune = ConfigInfo.getInstance().UpgradeAllRune;
                    //+12
                    if (updateRune == 2) {
                        if (slot % 2 == 0 && level < 12 && grade >= 4 && mainStatIndex < setRuneMaxValue.length) {
                            newVal = mainStatVal + "(" + setRuneMaxValue12[mainStatIndex][grade - 4] + ")";
                            //System.out.println(mainType+" :  lv_"+level+" : "+grade+"*  ; "+mainStatIndex+" : max val = "+newval);
                            statfix[i] = setRuneMaxValue12[mainStatIndex][grade - 4];
                            futureMainStat = setRuneMaxValue12[mainStatIndex][grade - 4];
                        }
                    }//+15

                    if (updateRune == 0 || updateRune == 3|| updateRune == 4) {
                        if (slot % 2 == 0 && level < 15 && grade >= 4 && mainStatIndex < setRuneMaxValue.length) {
                            newVal = mainStatVal + "(" + setRuneMaxValue[mainStatIndex][grade - 4] + ")";
                            //System.out.println(mainType+" :  lv_"+level+" : "+grade+"*  ; "+mainStatIndex+" : max val = "+newval);
                            statfix[i] = setRuneMaxValue[mainStatIndex][grade - 4];
                            futureMainStat = setRuneMaxValue[mainStatIndex][grade - 4];
                        }
                    }

                    //rune 1,3,5 to +15
                    if (updateRune == 3) {
                        if (slot % 2 == 1 && level < 15 && grade >= 4) {
                            statfix[i] = setRuneMaxValue15[slot / 2][grade - 4];
                            futureMainStat = setRuneMaxValue15[slot / 2][grade - 4];
                        }
                    }
                    if (updateRune == 4) {
                        if (slot % 2 == 1 && level < 12 && grade >= 4) {
                            statfix[i] = setRuneMaxValue12_135[slot / 2][grade - 4];
                            futureMainStat = setRuneMaxValue12_135[slot / 2][grade - 4];
                        }
                    }

                    statfixMap.put(slabelsMain[i], statfix[i]);

                }
            }

            if (optimizerFile) {
                for (int i = 1; i <= 4; i++) {
                    String s = "s" + i + "_v";
                    String s1 = "s" + i + "_t";
                    String sdata="s"+i+"_data";

                    if (p1.getString(s).length() > 0) {
                        int d1 = Integer.parseInt(p1.getString(s));
                        if (d1 > 0) {
                            String s2 = p1.getString(s1);

                            int ti = detectSubStat(s2);
                            if (ti < 0 || s2 == null) {
                                System.out.println("Found error sub type: " + s2);
                                System.exit(0);
                            }
                            int g=0;
                            boolean e=false;
                            if (p1.has(sdata)){
                                JSONObject d2 = p1.getJSONObject(sdata);
                                g = d2.getInt("gvalue");
                                e = d2.getBoolean("enchanted");
                            }

                            subStat1.add(ti);
                            subStat2.add(d1);
                            grinds.add(g);
                            enchanted.add(e);
                        }
                    }
                }
            }
            updateSubStats();

            String subType = (optimizerFile) ? p1.getString("i_t") : (p1.getJSONArray("prefix_eff").getInt(0) != 0) ? slabelsMain2[slabelsMapping[p1.getJSONArray("prefix_eff").getInt(0)]] : "";
            for (int i = 0; i < slabelsMain.length; i++) {
                if (!subType.equals(slabelsMain[i])) {
                    continue;
                }

                String s1 = (optimizerFile) ? p1.getString("i_v") : p1.getJSONArray("prefix_eff").getString(1);
                if (!s1.contains("-")) {
                    statfix[i] = (optimizerFile) ? p1.getInt("i_v") : p1.getJSONArray("prefix_eff").getInt(1);
                    statfixMap.put(slabelsMain[i], statfix[i]);
                    optionStatGui = subType + "+" + statfix[i];
                    optionStatGui = optionStatGui.replace(" flat", "");
                    it = i;
                    iv = Integer.parseInt(s1);
                }
            }

            if (p1.has("edited")) {
                isEdited = true;
            }
            monsterId = (optimizerFile) ? p1.getInt("monster") : 0;
            monster = (optimizerFile) ? p1.getString("monster_n") : "";
            monster = monster.replace("(In Storage)", "").trim();
            monster = monster.replace("Unknown name", "??").trim();

            if (monster.contains("(")) {
                monster = monster.replace("Wind", "Wi");
                monster = monster.replace("Fire", "F");
                monster = monster.replace("Water", "Wa");

                int pos1 = monster.indexOf("(") - 1;
                String n2 = monster.substring(0, pos1);
                //System.out.println(monster + " ; [" + n2 + "]");
                if (n2.length() > 7) {
                    n2 = n2.substring(0, 7);
                }
                monster = n2 + monster.substring(pos1);
            }

            if (monsterId == 0) {
                monster = "In Storage";
            }

            id = (optimizerFile) ? p1.getInt("id") : index;
            uniqueId = "" + id;
            String uniqueIdField = (optimizerFile) ? "unique_id" : "rune_id";
            if (p1.has(uniqueIdField)) {
                uniqueId = p1.getString(uniqueIdField);
            }

            String[] mainStatLabels = {"ATK", "?", "DEF", "?", "HP"};
            if (slot % 2 == 0) {
                mainStat = mainType;
                mainStatGui = mainType;
            } else {
                mainStat = "___";
                mainStatGui = mainStatLabels[slot - 1];
            }
            mainStatGui = mainStatGui.replace("flat", "");
            //statfixMap.put("slot", slot);
            //statfixMap.put("grade", grade);
            runeType = (optimizerFile) ? p1.getString("set") : setBonnusLabel[setBonnusLabelMapping[p1.getInt("set_id")]];
            runeTypeIndex = getSetId(runeType);

            //statfixMap.put("rune", runeTypeIndex);
            spd = statfixMap.getOrDefault("SPD", 0);
            acc = statfixMap.getOrDefault("ACC", 0);
            res = statfixMap.getOrDefault("RES", 0);
            cr = statfixMap.getOrDefault("CRate", 0);
            cd = statfixMap.getOrDefault("CDmg", 0);

            atk = statfixMap.getOrDefault("ATK%", 0);
            def = statfixMap.getOrDefault("DEF%", 0);
            hp = statfixMap.getOrDefault("HP%", 0);

            for (String s1 : statfixMap.keySet()) {
                String s2 = "";
                if (s1.contains("%")) {
                    s2 = "%";
                }
                statDisplayMap.put(s1, "" + statfixMap.get(s1) + s2);
            }

            if (newVal.length() > 0) {
                statDisplayMap.put(slabelsMain[mainStatIndex], newVal);
                //System.out.println("statDisplayMap : "+statfixMap+" ; "+info+" ; "+Arrays.toString(statfix));
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Error rune : " + p1);
            e.printStackTrace();
        }
    }

    public RuneType() {
        // TODO Auto-generated constructor stub
    }

    public String displayGui() {
        return "" + runeType + " : " + mainStat + " : " + " : lv_" + level + " ; " + statDisplayMap;
    }

    @Override
    public String toString() {
        info.clear();
        info.put("runeType", runeType);
        info.put("monster", monster);
        info.put("level", "lv_" + level);
        info.put("grade", grade + "*");
        info.put("id", "id_" + id);
        info.put("pos", "p_" + slot);
        info.put("pet", "pet_" + monsterId);

        // TODO Auto-generated method stub
        return mainStat + " : " + statDisplayMap + " ; " +runeType+" : "+ info.values();
        //return mainStat+" ["+runeType+", spd="+spd+",cr="+cr+",cd="+cd+",atk="+atk+"] "+monster;
    }

    public static Set<Integer> detectRuneStatIds(Function<RuneType, List<Integer>> paraRune, List<RuneType> allrunes) {
        Set<Integer> ls1 = new HashSet();

        System.out.println("First rune : " + allrunes.get(0));

        int num = 0;

        for (RuneType r1 : allrunes) {
            List<Integer> ls2 = paraRune.apply(r1);
            if (ls2 != null) {
                num = ls2.size();
                break;
            }
        }

        System.out.println("num : " + num);
        System.out.println("slabelsFix : " + Arrays.toString(slabelsFix));

        for (int k = 0; k < num; k++) {
            int k2 = 0;
            int[] counts = new int[slabelsFix.length];
            for (RuneType r1 : allrunes) {
                List<Integer> g1 = paraRune.apply(r1);
                if (g1 != null) {
                    for (int i = 0; i < slabelsFix.length; i++) {
                        if (r1.statfix[i] == g1.get(k)) {
                            counts[i]++;
                        }
                    }
                    k2++;
                }
            }
            //System.out.println("Counts : "+k2+" : "+Arrays.toString(counts));
            for (int i = 0; i < slabelsFix.length; i++) {
                if (counts[i] == k2) {
                    ls1.add(i);
                }
            }
        }

        return ls1;
    }

    //return true if have subtype but ungrinded.
    public boolean checkSubGrinded(int subType) {
        for (int i=0;i<subStat1.size();i++){
            if (subStat1.get(i)==subType){
                if (grinds.get(i)==0){
                    return true;
                }
            }
        }
        return false;
    }

    static public class RuneSet implements Comparable<RuneSet> {

        public static PetType runePet;
        public static String exceptPetRunes = "";

        public RuneType set[] = new RuneType[6];

        public int numRune=0;
        public int totalStats[] = new int[slabelsFix.length];
        int futureStats[] = new int[slabelsFix.length];

        Map<String, Integer> statfixMapPet = new HashMap();
        Map<String, Integer> statfixMap = new HashMap();
        public int spd, cr, cd, res, acc, def, hp, atk;

        public String mainStat = "";
        public Set<String> runeSets = new HashSet();
        
        public PetType equippedPet;

        long getId() {
            long total = 0;
            for (int i = 0; i < 6; i++) {
                total = total * 1024 + set[i].id;
            }
            return total;
        }

        public int statfixRune[] = new int[PetType.petLabels.length];
        public int pet_cd, pet_cr, pet_atk, pet_def, pet_hp, pet_spd, pet_acc, pet_res;
        float gw_cd = 0;
        float gw_def = 0;
        float gw_atk = 0;
        
        public int total_spd, total_hp, total_def, total_atk, total_cd;
        public int total_gw_hp, total_gw_def, total_gw_atk, total_gw_cd;

        public int getPetValue(String stat) {
            if ("eHp".equals(stat)) {
                return effectiveHP();
            }
            return statfixMapPet.getOrDefault(stat, 0);
        }

        int[] REMOVE_COST = {1000, 2500, 5000, 10000, 25000, 50000};

        public String getShortInfo() {
            String s = "[";
            for (RuneType r1 : set) {
                if (r1 != null) {
                    s = s + "," + r1.monster;
                }
            }
            s += "]";
            return s;
        }
        public String getSetRunesType() {
            String s = "[";
            for (RuneType r1 : set) {
                if (r1 != null) {
                    s = s + "," + r1.runeType;
                }
            }
            s += "]";
            return s;
        }

        public int calRemoveCost(PetType pet) {
            int totalCost = 0;
            for (int i = 0; i < 6; i++) {
                RuneType r1 = this.set[i];
                RuneType r2 = pet.currentEquip.set[i];

                if (r1 == null || r2 == null) {
                    continue;
                }

                if (r1.id == r2.id) {
                    continue;
                }

                if (r2.grade > 0) {
                    totalCost += REMOVE_COST[r2.grade - 1];
                }
                if (r1.monsterId > 0 && r1.monsterId != pet.id) {
                    totalCost += REMOVE_COST[r1.grade - 1];
                }

            }
            return totalCost;
        }

        public void equipOnPet(PetType pet) {
            RuneSet set = this;
            runePet = pet;
            removeCost = calRemoveCost(pet);
            equippedPet = pet;

            for (int i = 0; i < pet.petLabels.length; i++) {
                statfixRune[i] = 0;
                int incre = 0;
                if (i < 3) {
                    incre = pet.baseStats[i] * set.totalStats[i] / 100 + set.totalStats[i + pet.petLabels.length];
                } else {
                    incre = set.totalStats[i];
                }

                if (i == 7) {//swift bonus
                    incre += Math.ceil((double) pet.baseStats[i] * set.totalStats[i + 8] / 100);
                }

                statfixRune[i] = pet.baseStats[i] + incre;
                statfixMapPet.put(RuneType.slabels[i], statfixRune[i]);
            }
            ConfigInfo cf = ConfigInfo.getInstance();

            pet_cr = statfixMapPet.getOrDefault("cr", 0);
            pet_cd = statfixMapPet.getOrDefault("cd", 0);
            pet_atk = statfixMapPet.getOrDefault("atk", 0);
            pet_def = statfixMapPet.getOrDefault("def", 0);
            pet_hp = statfixMapPet.getOrDefault("hp", 0);
            pet_spd = statfixMapPet.getOrDefault("spd", 0);
            pet_acc = statfixMapPet.getOrDefault("acc", 0);
            pet_res = statfixMapPet.getOrDefault("res", 0);

            pet.r_atk = pet_atk - pet.b_atk;
            pet.r_cd = pet_cd - pet.b_cd;
            pet.r_spd = pet_spd - pet.b_spd;
            pet.r_def = pet_def - pet.b_def;
            pet.r_hp = pet_hp - pet.b_hp;

            if (gw_cd > 0) {
                gw_cd = cf.guildwar_cd;
            }

            //1.5 with def buff or atk buff !
            double gw_def = cf.guildwar_def;
            gw_atk = cf.guildwar_atk;

            if (gw_cd == 0) {
                gw_atk = 0;
                gw_def = 0;
            }

            //runePet.def_leader = 40;
            if (runePet!=null){
               //System.out.println(runePet.name +" ; def_leader : "+runePet.def_leader+" ; skillup = "+runePet.skillsUp+" ; gw_atk : "+gw_atk+" ; gw_cd : "+gw_cd+" ; def_buff : "+runePet.def_buff+" ; gw_def : "+gw_def+" ; glory_ele = "+cf.gloryAtkElement[pet.element]+" ; pet_atk = "+pet_atk+" ; pet.atk_buff = "+pet.atk_buff);
               //System.out.println("atk_buff : "+pet.atk_buff+" ; def_buff : "+pet.def_buff);
            }

            f_cd = (1 + (pet_cd + cf.glory_cd + gw_cd) / 100);
            f_atk = (pet.b_atk * (cf.glory_atk + cf.gloryAtkElement[pet.element] + gw_atk + pet.atk_leader) / 100 + pet_atk) * pet.atk_buff;
            f_def = (pet.b_def * (cf.glory_def + gw_def + pet.def_leader) / 100 + pet_def) * pet.def_buff;
            f_hp = (pet.b_hp * (cf.glory_hp) / 100 + pet_hp);
            f_spd = (pet.b_spd * (cf.glory_spd) / 100 + pet_spd);
            
            //System.out.println("f_cd = "+f_cd+" ; f_def = "+f_def+" ; f_atk = "+f_atk);
            
            total_cd = pet_cd + cf.glory_cd;
            total_atk = (int)Math.ceil(pet.b_atk * (cf.glory_atk + cf.gloryAtkElement[pet.element]) / 100.0) + pet_atk;
            total_def = (int)Math.ceil(pet.b_def * cf.glory_def / 100.0) + pet_def;
            total_hp = (int)Math.ceil(pet.b_hp * cf.glory_hp / 100.0) + pet_hp;
            total_spd = (int)Math.ceil(pet.b_spd * cf.glory_spd / 100.0) + pet_spd;
            
            total_gw_cd = pet_cd + cf.glory_cd + cf.guildwar_cd;
            total_gw_atk = (int)Math.ceil(pet.b_atk * (cf.glory_atk + cf.gloryAtkElement[pet.element] + cf.guildwar_atk) / 100.0) + pet_atk;
            total_gw_def = (int)Math.ceil(pet.b_def * (cf.glory_def + cf.guildwar_def) / 100.0) + pet_def;
            total_gw_hp = (int)Math.ceil(pet.b_hp * (cf.glory_hp + cf.guildwar_hp) / 100.0) + pet_hp;
        }
        
        public int spd() {
            return total_spd;
        }
        
        public int hp() {
            return (int)f_hp;
        }
        
        public int def() {
            return equippedPet.isGuildWars ? total_gw_def : total_def;
        }
        
        public int atk() {
            return (int)f_atk;
        }
        
        public int cr() {
            return pet_cr;
        }
        
        public int cd() {
            return equippedPet.isGuildWars ? total_gw_cd : total_cd;
        }
        
        public int acc() {
            return pet_acc;
        }
        
        public int res() {
            return pet_res;
        }

        public boolean runesetOK(List<Function<RuneSet, Boolean>> list) {
            for (Function<RuneSet, Boolean> f1 : list) {
                if (!f1.apply(this)) {
                    return false;
                }
            }
            return true;
        }

        public int avgDamage(){
            if (equippedPet.skillItem.isBomb) {
                return atk() + atk() * equippedPet.skillItem.skillUp;
            } else {
                // divide by 5,000 for scaling
                return (int)(((atk() * cd() * Math.min(cr(), 100)) + (atk() * equippedPet.skillItem.skillUp)) / 5000.0);
            }
        }

        public int finalDamage() {
            return finalDamage(15, 6, 0);
        }

        double f_atk_bomb = 0;
        public double f_atk = 0;
        public double f_cd = 0;
        double f_def = 0;
        double f_hp = 0;
        double f_spd = 0;

        // http://summonerswar.wikia.com/wiki/Equations
        public int effectiveHP() {
            return (int)(pet_hp * (1140 + (pet_def * 3.5)) / 1000);
        }
        
        public int eHp() {
            return (int)(hp() * (1140 + (def() * 3.5)) / 1000);
        }
         
        public int dmgVsBoss(double boss_def,double boss_hp) {
            if (runePet.skillItem.ignoreDmg)
                return finalDamage(0,0,0);

            this.enemy_hp = boss_hp;
            boss_def = boss_def;//defbreak reduce 70%;
            return (int)(pureDamage()*1000/(1140+boss_def*3.5));
        }

        public int dmgVsDefense(double def){
            return (int)(pureDamage()*1000/(1140+def*3.5));
        }
        public double pureDamage(){
            return finalDamage(0,0,0)*1140/1000;
        }

        public double enemy_spd=120;
        public double enemy_hp=2000;
        public double pet_level=40;
        
        public int finalDamage(float gw_cd, float gw_atk, float leader_atk) {
            this.gw_cd = gw_cd;
            this.gw_atk = gw_atk;
            this.gw_def = gw_def;

            Function<RuneSet, Double> dm = runePet.damageMulty;
            //System.out.println("Skill : "+runePet.skillItem);
            if (runePet.skillItem.damageMultySkill != null) {
                dm = runePet.skillItem.damageMultySkill;
                runePet.skillsUp = runePet.skillItem.skillUp;
                //System.out.println("Use skill : "+runePet.skillItem.skillMulty);
            }

            //runePet.applyRuneSet(this);
            equipOnPet(runePet);
            /*System.out.println("runePet.atk : "+runePet.atk);
             System.out.println("cd : "+cd);
             System.out.println("runePet.cd : "+runePet.cd);
             System.out.println("atk : "+atk+" ; runePet.atk : "+runePet.atk+" ; b_atk = "+runePet.b_atk);
             System.out.println("runePet.isBomb : "+runePet.isBomb);
             System.out.println("dm.apply(this) : "+dm.apply(this));*/

            //even ignore damage have reduction 1000/1140
            //return (int)(f_cd*f_atk*runePet.SkillMulty*1000/1140);
            double moreDamage = 0;
            if (runePet.skillItem.afterMulty !=null){
                moreDamage = runePet.skillItem.afterMulty.apply(this);
            }
            if (runePet.skillItem.isBomb) {
                double skillup = (100 + (double) runePet.skillItem.skillUp) / 100;
                return (int) (f_atk * runePet.skillItem.skillValue * skillup * 1000 / 1140 + moreDamage);
            }
            int fdmg = (int) ((f_cd+runePet.skillItem.extra_cd+(runePet.skillItem.skillUp/100.0)) * dm.apply(this)*runePet.skillItem.extra_damage * 1000 / 1140 + moreDamage);
            //System.out.println("Damage before crit dmg : "+Math.round(dm.apply(this))+
            //        " f_cd = "+f_cd+" ehp = "+enemy_hp+" f_atk="+f_atk+" ; skill_up = "+runePet.skillItem.skillUp+ " moredamage = "+moreDamage+" ; fdmg = "+fdmg);

            return fdmg;
        }

        int finalDamageWithBuff() {
            runePet.applyRuneSet(this);
            //with towers glory.
            float fp = (1 + (float) (runePet.cd + 25) / 100);
            return (int) (fp * (runePet.atk + 14 + 17 + 30) * runePet.SkillMulty * 3 / 2);
        }

        boolean isSamePet() {
            if (set[0].monster.startsWith("Inven")) {
                return false;
            }
            if (set[0].monster.startsWith("?")) {
                return false;
            }

            for (int i = 1; i < 6; i++) {
                if (set[i].monsterId != set[0].monsterId) {
                    return false;
                }
            }
            return true;
        }

        boolean haveSet(String set) {
            if (runeSetMap.contains(set)) {
                return true;
            } else {
                return false;
            }
        }

        public boolean will() {
            if (runeSetMap.contains("Will")) {
                return true;
            } else {
                return false;
            }
        }

        public boolean shield() {
            if (runeSetMap.contains("Shield")) {
                return true;
            } else {
                return false;
            }
        }

        public boolean rev() {
            if (runeSetMap.contains("Revenge")) {
                return true;
            } else {
                return false;
            }
        }

        public boolean nemesis() {
            if (runeSetMap.contains("Nemesis")) {
                return true;
            } else {
                return false;
            }
        }

        boolean rev(int num) {
            int count = 0;
            for (RuneType r : set) {
                if (r.runeType.equalsIgnoreCase("Revenge")) {
                    count++;
                }
            }
            if ((count / 2) == num) {
                return true;
            }
            return false;
        }

        boolean shield(int num) {
            int count = 0;
            for (RuneType r : set) {
                if (r.runeType.equalsIgnoreCase("Shield")) {
                    count++;
                }
            }
            if ((count / 2) == num) {
                return true;
            }
            return false;
        }

        Set<String> runeSetMap = new HashSet();
        public boolean isBroken = false;

        void updateStats() {
            for (int i = 0; i < slabelsMain.length; i++) {
                totalStats[i] = 0;
                for (int j = 0; j < 6; j++) {
                    if (set[j] != null) {
                        totalStats[i] += set[j].statfix[i];
                    }
                }
                //statfixMap.put(slabelsMain[i], totalStats[i]);
            }

            int[] setNums = new int[setBonnusLabel.length];

            //add bonnus stats
            for (RuneType s1 : set) {
                if (s1 != null && s1.runeTypeIndex >= 0) {
                    try {
                        setNums[s1.runeTypeIndex]++;
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Error : " + s1);
                    }
                }
            }
            for (int i = 0; i < setBonnusLabel.length; i++) {
                //runeSetMap.put(setBonnusLabel[i], setNums[i]);
                int snum = setNums[i];
                if (setBonnusNum[i] > 0 && ( (snum % setBonnusNum[i]) != 0)) {
                    isBroken = true;
                }

                if (snum >= setBonnusNum[i] && setBonnusNum[i] != 0) {
                    runeSetMap.add(setBonnusLabel[i]);

                    if (i == 7) {//swift bonus is %spd
                        totalStats[i + 8] += (snum / setBonnusNum[i]) * setBonnusValue[i];
                    } else //we take care of enhance,fight runeset bonus later !
                    if (i < slabelsFix.length) {
                        totalStats[i] += (snum / setBonnusNum[i]) * setBonnusValue[i];
                    }

                    if (setBonnusNum[i] == 2 && snum >= 4) {
                        runeSets.add((snum / setBonnusNum[i]) + "x" + setBonnusLabel[i]);
                    } else {
                        runeSets.add(setBonnusLabel[i]);
                    }
                }
            }

            mainStat = "";
            for (int i = 0; i < 6; i++) {
                if (set[i] != null && i % 2 == 1) {
                    mainStat += "," + set[i].mainStat;
                }
            }
            if (mainStat.length() > 2) {
                mainStat = mainStat.substring(1);
                mainStat = mainStat.replace(" flat", "");
                mainStat = mainStat.replace("%", "");
                mainStat = mainStat.replace("CDmg", "CD");
            }

            postUpdate();
        }

        public void postUpdate() {
            for (int i = 0; i < slabelsMain.length; i++) {
                if (totalStats[i] > 0) {
                    statfixMap.put(slabelsMain[i], totalStats[i]);
                }
            }

            spd = statfixMap.getOrDefault("SPD", 0);
            acc = statfixMap.getOrDefault("ACC", 0);
            res = statfixMap.getOrDefault("RES", 0);
            cr = statfixMap.getOrDefault("CRate", 0);
            cd = statfixMap.getOrDefault("CDmg", 0);

            atk = statfixMap.getOrDefault("ATK%", 0);
            def = statfixMap.getOrDefault("DEF%", 0);
            hp = statfixMap.getOrDefault("HP%", 0);
        }

        public RuneSet(List<RuneType> r, int totalss[]) {
            //System.out.println("r : "+r.size());
            for (int i = 0; i < r.size(); i++) {
                set[i] = r.get(i);
            }

            for (int i = 0; i < totalss.length; i++) {
                totalStats[i] = totalss[i];
            }

            postUpdate();
        }

        public long idrunes = 0;
        public long id = -1;
        public static long totalId = 0;

        public RuneSet(List<RuneType> r) {
            //System.out.println("r : "+r.size());
            totalId++;
            id = totalId;
            numRune = 0;
            for (int i = 0; i < r.size(); i++) {
                //set[i] = r.get(i);
                set[r.get(i).slot - 1] = r.get(i);
                idrunes += r.get(i).id;
                if (r.get(i).runeTypeIndex>=0)
                    numRune++;
            }
            updateStats();
        }

        public RuneSet() {
            // TODO Auto-generated constructor stub
        }

        public String getRuneStats(Function<RuneType, List<Integer>> paraRune) {
            // TODO Auto-generated method stub
            String s1 = "";
            for (int i = 0; i < 6; i++) {
                s1 += set[i].runeType + " : " + paraRune.apply(set[i]) + " : " + set[i].monster + " , ";
            }
            return s1;
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            String s1 = "";
            for (int i = 0; i < 6; i++) {
                if (set[i] != null) {
                    s1 += set[i].runeType + " : " + set[i].spd + " : " + set[i].monster + " , ";
                }
            }
            return s1;
        }

        public String info() {
            return statfixMap + " : " + runeSets;
        }

        public String details() {
            // TODO Auto-generated method stub
            String s1 = "";
            for (int i = 0; i < 6; i++) {
                s1 += (i + 1) + " : " + set[i] + "\n";
            }

            s1 += "runesets : " + statfixMap + " : " + runeSets + "\n";
            return s1;
        }

        public int bestValue;
        public int removeCost;

        @Override
        public int compareTo(RuneSet o) {
            if (o.bestValue == this.bestValue) {
                if (this.removeCost == o.removeCost) {
                    return Long.compare(this.idrunes, o.idrunes);
                }
                return Integer.compare(this.removeCost, o.removeCost);
            }
            return Integer.compare(o.bestValue, this.bestValue);
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
};
