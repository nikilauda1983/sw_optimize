package swrunes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import swrunes.RuneType.RuneSet;

import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;
import javax.swing.JComboBox;
import static swrunes.RuneType.CD;
import static swrunes.RuneType.RAGE_SET;
import static swrunes.RuneType.setBonnusNum;

public class RunePermutation implements Serializable {

    public static AtomicLong totalCount = new AtomicLong(0);
    public static AtomicInteger firstRuneRoundCount = new AtomicInteger(0);
    public static int allFirstRune = 0;
    public static long runFirstRune = 0;

    static int bestValue = 0;

    static PetType mainPet;
    public static RuneSet bestRuneSet;

    static List<RuneSet> foundRuneGroup = new ArrayList();
    static List<RuneType> perRunes = new ArrayList();

    public static boolean useThreads = true;
    public static int numThreads = 5;
    public static Set<Integer> exceptPetIds = new HashSet();

    static Map<String, RuneType> bestSlot = new HashMap();

    // if (a>b) return true
    public static boolean runeCompare(List<Integer> a, List<Integer> b) {
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i) <= b.get(i)) {
                return false;
            }
        }
        return true;
    }

    public static boolean runeCompareStronger(List<Integer> a, List<Integer> b) {
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i) <= b.get(i)) {
                return false;
            }
        }
        return true;
    }

    public static int runeCompareSum(List<Integer> a, List<Integer> b) {
        int suma = 0;
        int sumb = 0;
        for (Integer i : a) {
            suma += i;
        }
        for (Integer i : b) {
            sumb += i;
        }
        return Integer.compare(suma, sumb);
    }

    public static String checkMemory() {
        Runtime runtime = Runtime.getRuntime();
        long mb = 1024 * 1024;
        long total = runtime.totalMemory() / mb;
        long free = runtime.freeMemory() / mb;
        long used = total - free;

        return "[ Total : " + total + " ; free : " + free + " ; used : " + used + " ] ";
    }

    //HashMap<Long,RuneSet> cacheRunes = new HashMap();
    public static Map<Long, int[]> cacheRunes = new HashMap();

    public void saveCache() {
        System.out.println("Save caches : " + cacheRunes.size());
        try {
            FileOutputStream fos = new FileOutputStream("cacheRunes.dat");

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeInt(cacheRunes.size());
            for (Long l1 : cacheRunes.keySet()) {
                oos.writeLong(l1);

                for (int i1 = 0; i1 < RuneType.slabelsFix.length; i1++) {
                    //s[i1] = ois.readInt();
                    oos.writeInt(cacheRunes.get(l1)[i1]);
                }
                //oos.writeObject(cacheRunes.get(l1));
            }
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void loadCache() {

        cacheRunes.clear();
        long t1 = System.currentTimeMillis();
        try {
            FileInputStream fis = new FileInputStream("cacheRunes.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            //cacheRunes = (Map<String,String>) ois.readObject();
            int len = ois.readInt();
            for (int i = 0; i < len; i++) {
                long l1 = ois.readLong();

                int[] s = new int[RuneType.slabelsFix.length];
                for (int i1 = 0; i1 < RuneType.slabelsFix.length; i1++) {
                    s[i1] = ois.readInt();
                }

                cacheRunes.put(l1, s);
            }

            ois.close();
            fis.close();
        } catch (Exception ioe) {
            ioe.printStackTrace();
            cacheRunes.clear();
        }

        System.out.println("Load caches : " + cacheRunes.size() + "  in " + (System.currentTimeMillis() - t1) + " ms");
    }

    public long getRuneSetId(List<RuneType> curRune) {
        long total = 0;
        for (int i = 0; i < 6; i++) {
            total = total * 1024 + curRune.get(i).id;
        }
        return total;
    }

    public static String estimateTime = "";
    public static long allBest = 0;
    public static long curBest = 0;
    public static long foundBest = 0;
    public static RuneSet curBestRuneSet = null;
    public static TreeSet<RuneSet> resultTreeRunes = new TreeSet();
    public int minTreeValue = 0;

    public static List<RuneSet> resultRunesList = new ArrayList();

    int count0 = 0;

    public static String du_str(long s) {
        s = s / 1000;
        if (s >= 3600 * 24) {
            return String.format("%dd:%dh:%dm", s / (3600 * 24), (s % (3600 * 24)) / 3600, (s % 3600) / 60);
        } else if (s >= 3600) {
            return String.format("%dh:%dm:%ds", (s % (3600 * 24)) / 3600, (s % 3600) / 60, s % 60);
        } else if (s >= 60) {
            return String.format("%dm:%ds", (s % 3600) / 60, s % 60);
        }
        return s + "s";
    }

    public static String dunow_str(long s) {
        return du_str(System.currentTimeMillis() - s);
    }

    public static String per_str(long current, long full) {
        if (full <= 0) {
            return "0%";
        }
        return " " + (current * 100 / full) + "% ";
    }

    public static String estim_str(long current, long full, long s) {
        if (current <= 0) {
            return "0";
        }
        long es = (long) ((double) (System.currentTimeMillis() - s) * full / current);
        //System.out.println("estim : "+current+" ; "+full+" ; "+es);
        return du_str(es);
    }

    public void processTreeSet(RuneSet r1) {
        //System.out.println("processTreeSet : "+r1.bestValue+" ; "+(count0++));
        resultTreeRunes.add(r1);
        //resultRunesList.add(r1);
        if (true) {
            return;
        }

        if (resultTreeRunes.size() < 1000) {
            resultTreeRunes.add(r1);

            //System.out.println("Min : "+resultTreeRunes.first().bestValue+" ; max : "+resultTreeRunes.last().bestValue);
            //System.out.println("Add tree : "+minTreeValue+" ; size = "+resultTreeRunes.size());
        } else if (r1.bestValue > minTreeValue) {
            resultTreeRunes.add(r1);
            //System.out.println("Min : "+resultTreeRunes.first().bestValue+" ; max : "+resultTreeRunes.last().bestValue);

            RuneSet p1 = resultTreeRunes.pollFirst();
            minTreeValue = p1.bestValue;
            // System.out.println("Remove tree : "+minTreeValue+" ; size = "+resultTreeRunes.size());
        }
    }

    public boolean runePosOK(RuneType r, int pos, int mainSet, Set<Integer> formation) {
        if (r.slot == (pos + 1)) {
            //if (paraRune!=null && !paraRune.apply(r)) continue;
            if (formation.contains(pos) && mainSet != r.runeTypeIndex) {
                return false;
            }
            if (completeSet && !formation.contains(pos) && mainSet == r.runeTypeIndex) {
                return false;
            }
            return true;
        }
        return false;
    }
   
    public void iteratePermute(int mainSet, Set<Integer> formation, Function<RuneSet, Boolean> para, Function<RuneType, List<Integer>> paraRune,
            Function<RuneSet, Integer> bestpara, List<RuneSet> bestRuneSet) {
        if (fullStop) {
            return;
        }

        LinkedList<List<RuneType>> fullStack = new LinkedList();
        int count = 0;
        Set<RuneType> usedRunes = new HashSet();
        int pos = 0;
        for (RuneType r : perRunes) {
            if (fullStop) {
                return;
            }
            if (runePosOK(r, pos, mainSet, formation)) {
                List<RuneType> res = new ArrayList();
                res.add(r);
                fullStack.add(res);
            }
        }
        //System.out.println(formation + " : " + fullStack);
        long time1 = System.currentTimeMillis();
        boolean doneOne=false;
        while (true) {
            List<RuneType> last = fullStack.pollLast();
            if (last != null) {
                

                Set<Integer> flags = new HashSet();
                for (RuneType r1 : last) {
                    flags.add(r1.id);
                }
                pos = last.size();
                if (last.size() >= 6) {
                    processRune(last, para, paraRune, bestpara, bestRuneSet);
                    doneOne = true;
                } else {
                    for (RuneType r1 : perRunes) {
                        if (fullStop) {
                            break;
                        }
                        if (!flags.contains(r1.id)) {
                            if (runePosOK(r1, pos, mainSet, formation)) {

                                List<RuneType> res = new ArrayList();
                                res.addAll(last);
                                res.add(r1);
                                fullStack.add(res);
                            }
                        }
                    }
                }                
                if (last.size() == 1) {
                    firstRuneRoundCount.incrementAndGet();
                    //System.out.println(firstRuneRoundCount + " / " + allFirstRune);
                    if (pBar != null && doneOne) {
                        pBar.setValue(firstRuneRoundCount.get());
                        estimateTime = estim_str(firstRuneRoundCount.get(), allFirstRune, runFirstRune);
                    }
                }
            }
            if (fullStack.isEmpty() || fullStop) {
                RuneSet r1 = new RuneSet();
                if (bestRuneSet.size() > 0) {
                    r1 = bestRuneSet.get(0);
                    if (bestpara.apply(r1) > allBest) {
                        allBest = bestpara.apply(r1);
                    }
                }
                System.out.println("Done this patch : " + formation + " : " + totalCount + " : in " + (System.currentTimeMillis() - time1) + " ms. [" + Thread.currentThread().getName() + "] Best rune set : "
                        + bestpara.apply(r1) + " : " + r1.runeSets + " curBest : " + allBest);
                foundRuneGroup.addAll(bestRuneSet);
                time1 = System.currentTimeMillis();
                return;
            }
        }
    }

    public void processRune(List<RuneType> curRune, Function<RuneSet, Boolean> para, Function<RuneType, List<Integer>> paraRune, Function<RuneSet, Integer> bestpara, List<RuneSet> bestRuneSet) {
        totalCount.incrementAndGet();

        //if (true) return;
        //long id = getRuneSetId(curRune);
        RuneSet s1 = new RuneSet(curRune);
        //System.out.println(totalCount + " : Process : " + s1.getSetRunesType());
        if (noBrokenSet && s1.isBroken) {
            //System.out.println("Found broken : "+s1);
            return;
        }
        //System.out.println(totalCount+" : Process : "+s1.runeSets);

        s1.equipOnPet(RuneSet.runePet);

        if (totalCount.get() % 10000000 == 0) {
            //System.out.println("Process : "+totalCount+" : "+s1.getRuneStats(paraRune));
        }

        if (RuneSet.runePet != null && s1.isSamePet()) {
            if (curRune.get(0).monsterId == RuneSet.runePet.id) {
                //System.out.println("Found current rune pet : " + curRune.get(0).monster + " : " + s1.getRuneStats(paraRune));
            }
        }

        if (para != null && !para.apply(s1)) {
            return;
        }

        //if (true) return;
        //synchronized(bestRuneSet){
        s1.bestValue = bestpara.apply(s1);
        //processTreeSet(s1);

        if (bestRuneSet.size() == 0 || s1.compareTo(bestRuneSet.get(0)) < 0) {
            if (bestRuneSet.size() > 0) {
                bestRuneSet.clear();
            }
            bestRuneSet.add(s1);

            if (bestpara.apply(s1) > allBest) {
                allBest = bestpara.apply(s1);
                curBestRuneSet = s1;
            }
            //System.out.println(totalCount+" : Best Rune : "+s1+" : "+bestpara.apply(s1));
        }
        if (s1.bestValue >= (allBest * 90) / 100) {
            processTreeSet(s1);
        }
        foundBest++;
    }

    public void recurPermute(int pos, List<RuneType> curRune, int mainSet, Set<Integer> formation, Set<RuneType> usedRunes, Function<RuneSet, Boolean> para, Function<RuneType, List<Integer>> paraRune,
            Function<RuneSet, Integer> bestpara, List<RuneSet> bestRuneSet) {

        if (fullStop) {
            return;
        }
        long time1 = System.currentTimeMillis();
        List<RuneType> res = new ArrayList();
        res.addAll(curRune);
        if (pos == 6) {
            processRune(curRune, para, paraRune, bestpara, bestRuneSet);
            return;
        }
        res.add(perRunes.get(0));

        for (RuneType r : perRunes) {
            if (fullStop) {
                break;
            }
            if (!usedRunes.contains(r) && r.slot == (pos + 1)) {
                //if (paraRune!=null && !paraRune.apply(r)) continue;                
                if (!runePosOK(r, pos, mainSet, formation)) {
                    continue;
                }

                String label = r.runeType + "_" + r.slot;
                //System.out.println("Come here : "+label);

                res.set(pos, r);
                usedRunes.add(r);
                recurPermute(pos + 1, res, mainSet, formation, usedRunes, para, paraRune, bestpara, bestRuneSet);
                usedRunes.remove(r);
            }
            if (pos == 0) {
                firstRuneRoundCount.incrementAndGet();
                //System.out.println(firstRuneRoundCount + " / " + allFirstRune);
                if (pBar != null) {
                    pBar.setValue(firstRuneRoundCount.get());
                }
            }
        }
        if (pos == 0) {
            RuneSet r1 = new RuneSet();
            if (bestRuneSet.size() > 0) {
                r1 = bestRuneSet.get(0);
            }

            if (bestpara.apply(r1) > allBest) {
                allBest = bestpara.apply(r1);

            }
            System.out.println("Done this patch : " + formation + " : " + totalCount + " : in " + (System.currentTimeMillis() - time1) + " ms. [" + Thread.currentThread().getName() + "] Best rune set : "
                    + bestpara.apply(r1) + " : " + r1.runeSets + " curBest : " + allBest);
            foundRuneGroup.addAll(bestRuneSet);
        }
    }

    public static Set<Integer> excludeList = new HashSet();
    public static int[] slot246 = {0, 0, 0};
    public static Set<String>[] slotData = new HashSet[3];

    public static boolean noBrokenSet = false;

    public static void genSlot246(String d1, String d2, String d3) {
        //"HP%", "DEF%", "ATK%", "CDmg", "CRate", "ACC", "RES", "SPD"};
        slotData[0] = new HashSet();
        slotData[1] = new HashSet();
        slotData[2] = new HashSet();

        slotData[0].add(d1);
        slotData[1].add(d2);
        slotData[2].add(d3);
    }

    public static Set<String> updateSlotData(JComboBox combo) {
        Set<String> s1 = new HashSet();
        if (combo.getSelectedIndex() == 0) {
            return null;
        }
        String cb = combo.getSelectedItem().toString();
        String[] ss = cb.split(",");
        for (String cb1 : ss) {
            for (String s2 : RuneType.slabelsMainDisplay) {
                if (cb1.replace("%", "").equalsIgnoreCase(s2.replace("%", ""))) {
                    s1.add(s2);
                }
            }
        }
        //jComboSlot4.addItem("CR,CD,ATK");
        //jComboSlot2.addItem("SPD,ATK");
        //jComboSlot2.addItem("HP,DEF");
        //"HP%", "DEF%", "ATK%", "CDmg", "CRate", "ACC", "RES", "SPD"};
        /*if (cb.equalsIgnoreCase("CR,CD,ATK")) {
            s1.add(RuneType.slabelsMainDisplay[3]);
            s1.add(RuneType.slabelsMainDisplay[4]);
            s1.add(RuneType.slabelsMainDisplay[2]);
        }
        if (cb.equalsIgnoreCase("SPD,ATK")) {
            s1.add(RuneType.slabelsMainDisplay[7]);
            s1.add(RuneType.slabelsMainDisplay[2]);
        }
        if (cb.equalsIgnoreCase("HP,DEF")) {
            s1.add(RuneType.slabelsMainDisplay[0]);
            s1.add(RuneType.slabelsMainDisplay[1]);
        }
        if (cb.equalsIgnoreCase("ATK,HP")) {
            s1.add(RuneType.slabelsMainDisplay[0]);
            s1.add(RuneType.slabelsMainDisplay[2]);
        }
        if (cb.equalsIgnoreCase("ATK,HP,DEF")) {
            s1.add(RuneType.slabelsMainDisplay[0]);
            s1.add(RuneType.slabelsMainDisplay[1]);
            s1.add(RuneType.slabelsMainDisplay[2]);
        }*/
        return s1;
    }

    public static int[] includeRunes = {-1, -1, -1, -1, -1, -1, -1};

    public static void excludeRunes(List<Integer> list) {
        //excludeList.clear();
        excludeList.addAll(list);
    }

    public static void excludePets(List<Integer> list) {
        exceptPetIds.addAll(list);
    }

    static int icount = 0;
    public static long startTime = 0;

    public static List<RuneSet> perMute(String mainSet, Function<RuneSet, Boolean> para, Function<RuneType, List<Integer>> paraRune,
            Function<RuneSet, Integer> bestpara) {
        return new RunePermutation().perMuteReal(mainSet, para, paraRune, bestpara);
    }

    //Rune for broken set
    //must have 2x sub stat or 3 1x stat
    public boolean isSuperRune(Function<RuneType, List<Integer>> paraRune, RuneType rune) {

        return false;
    }

    public static long preCalc = 1, preRunes = 0;
    public static List<String> lockPetLists = new ArrayList<>();
    public static boolean useStorage = false;

    public static void preOptimize(String mainSet, Function<RuneType, List<Integer>> paraRune, Collection<Set<Integer>> alls) {
        int mainSetId = RuneType.getSetId(mainSet);
        System.out.println("preOptimize MainId : " + mainSetId + " ; mainSet = " + mainSet + " ; runes : " + SwManager.runes.size());
        //loadCache();
        lockPetLists.clear();
        exceptPetIds.clear();

        System.out.println("perRunes : " + perRunes.size());
        allBest = 0;

        if (mainSetId < 0) {
            return;
        }

        int numSet = RuneType.setBonnusNum[mainSetId];

        Set<Integer> vals = Sets.newHashSet(1, 2, 3, 4, 5, 0);
        Collection<Set<Integer>> orderPerm
                = Sets.powerSet(vals);

        Set<Integer> runeStatOnly = new HashSet();
        if (paraRune != null) {
            runeStatOnly = RuneType.detectRuneStatIds(paraRune, SwManager.runes);
            System.out.println("runeStatOnly : " + runeStatOnly);
            Set<String> s2 = new HashSet();
            for (int i : runeStatOnly) {
                s2.add(RuneType.setBonnusLabel[i]);
            }
            System.out.println("runeStatOnly : " + s2);
        }

        String[] ps = RuneSet.exceptPetRunes.split(",");
        System.out.println("exceptPetRunes : " + Arrays.toString(ps));
        for (String s2 : ps) {
            s2 = s2.trim().toLowerCase();
            if (SwManager.pets.containsKey(s2)) {
                exceptPetIds.add(SwManager.pets.get(s2).id);
                lockPetLists.add(SwManager.pets.get(s2).name);
            }
        }

        System.out.println("exceptPetIds : " + exceptPetIds);
        System.out.println("exceptPetIds : " + lockPetLists);
        System.out.println("excludeList runes : " + excludeList);
        System.out.println("slot246 : " + Arrays.toString(slotData));

        Set<RuneType> optimizeRunes = new HashSet();
        int i2 = 0;
        for (RuneType r : SwManager.runes) {
            if (r.monsterId == RuneSet.runePet.id) {
                //System.out.println("Found current rune : "+r.slot+" : "+r);
            }
            if (mainSet.contains("x3") && r.runeTypeIndex != mainSetId) {
                continue;
            }

            if (r.slot == 2) {
                //System.out.println("Current Rune : "+(i2++)+" : "+r+" ; "+paraRune.apply(r)+" : "+r.monster+" ; lv_"+r.level);
            }

            if (paraRune != null && paraRune.apply(r) == null) {
                continue;
            }

            String label = r.runeType + "_" + r.slot;
            if (paraRune != null && paraRune.apply(r) == null) {
                continue;
            }

            if (useStorage) {
                //storage ??
                if (r.monsterId != 0 && r.monsterId != RuneSet.runePet.id) {
                    boolean ok = true;
                    if (ConfigInfo.getInstance().runePet4star) {

                    }

                    if (ok) {
                        continue;
                    }
                }
            }
            if (includeRunes[r.slot] >= 0) {
                if (r.id != includeRunes[r.slot]) {
                    continue;
                }
            }
            if (noBrokenSet && RuneType.setBonnusNum[mainSetId] == 4
                    && RuneType.setBonnusNum[r.runeTypeIndex] == 4 && r.runeTypeIndex != mainSetId) {
                continue;
            }
            if (r.slot % 2 == 0) {
                int index = (r.slot - 1) / 2;
                if (slotData[index] != null) {
                    if (!slotData[index].contains(r.mainStat)) {
                        continue;
                    }
                    //System.out.println("ok rune : " + r);
                }
            }

            //if (RuneSet.exceptPetRunes.contains(r.monster)) continue;
            //but still use current Runes for global
            if (r.monsterId != RuneSet.runePet.id) {
                if (exceptPetIds.contains(r.monsterId)) {
                    continue;
                }
            }

            //exclude low rune leve < 6?
            if (r.level < ConfigInfo.getInstance().lowRuneLevel) {
                continue;
            }

            //exclude runes
            if (excludeList.contains(r.id)) {
                continue;
            }

            if (mainSet.contains(",") && !mainSet.toLowerCase().contains(r.runeType.toLowerCase())) {
                continue;
            }

            if (r.slot == 2) {
                //     System.out.println("ok Rune : "+(i2++)+" : "+r+" ; "+paraRune.apply(r));
            }
            //System.out.println("bestSlot : "+bestSlot+" : "+r);
            boolean goodRune = true;
            Set<RuneType> needRemoves = new HashSet();

            if (r.monsterId == RuneSet.runePet.id) {
                //System.out.println("Found current rune : "+r.slot+" : "+r);
            }

            for (RuneType r2 : optimizeRunes) {
                //all kind of rune
                if (!mainSet.contains(",") && r2.runeTypeIndex != r.runeTypeIndex && r2.slot == r.slot && runeStatOnly.size() > 0) {
                    if (!runeStatOnly.contains(r2.runeTypeIndex) && !runeStatOnly.contains(r.runeTypeIndex)
                            && r2.runeTypeIndex != mainSetId && r.runeTypeIndex != mainSetId) {
                        //System.out.println("Detect : "+r+" ; "+r2);

                        //remove r2;
                        if (runeCompare(paraRune.apply(r), paraRune.apply(r2))) {
                            needRemoves.add(r2);
                        }
                        if (runeCompare(paraRune.apply(r2), paraRune.apply(r))) {
                            //this rune is no good!
                            goodRune = false;
                            break;
                        }
                    }
                }

                if (r2.runeTypeIndex == r.runeTypeIndex && r2.slot == r.slot) {
                    //remove r2;
                    if (runeCompare(paraRune.apply(r), paraRune.apply(r2))) {
                        needRemoves.add(r2);
                        if (r2.monsterId == RuneSet.runePet.id) {
                            System.out.println("Rune current on pet remove : " + r2);
                        }
                    }
                    if (runeCompare(paraRune.apply(r2), paraRune.apply(r))) {
                        //this rune is no good!

                        // System.out.println("Remove rune : "+r+" because of "+r2);
                        goodRune = false;
                        break;
                    }
                }
            }

            /*for (RuneType r1:needRemoves){
             if (r1.monsterId==RuneSet.runePet.id)
             System.out.println("Current rune is removed by better rune : "+r1);
             }*/
            optimizeRunes.removeAll(needRemoves);

            /*if (r.monsterId==RuneSet.runePet.id)
             System.out.println("Found current : "+r+" ; "+goodRune);*/
            if (goodRune) {
                optimizeRunes.add(r);
            }
        }

        perRunes.clear();
        perRunes.addAll(optimizeRunes);
        System.out.println("Curpet : " + RuneSet.runePet.name);
        System.out.println("runeset : " + perRunes.size());

        Collections.sort(perRunes, new Comparator<RuneType>() {
            @Override
            public int compare(RuneType r1, RuneType r2) {
                if (r1.slot != r2.slot) {
                    return Integer.compare(r1.slot, r2.slot);
                }
                return runeCompareSum(paraRune.apply(r1), paraRune.apply(r2));
            }

        });

        //limit big search -> quick search
        //only select a good number of top runes
        //sort by total value (not very great)
        if (perRunes.size() > 120 && !fullSearch) {
            List<RuneType> tempRunes1 = new ArrayList();
            int[] c2 = new int[8];
            int[] m3 = new int[8];
            int[] m4 = new int[300];
            int[] dtype = new int[100];
            int[] dtype2 = new int[400];
            int firstDepth = 10;
            for (int i = 0; i < perRunes.size(); i++) {
                RuneType r1 = perRunes.get(perRunes.size() - i - 1);
                if (c2[r1.slot] <= firstDepth || (r1.slot % 2 == 0 && dtype2[r1.slot * 20 + r1.mainStatIndex] < 10)) {
                    tempRunes1.add(r1);
                    c2[r1.slot]++;
                    dtype2[r1.slot * 20 + r1.mainStatIndex]++;
                    dtype[r1.runeTypeIndex]++;
                    if (r1.runeTypeIndex == mainSetId) {
                        m3[r1.slot]++;
                    }
                }
            }
            if (noBrokenSet) {
                for (int i = 0; i < perRunes.size(); i++) {
                    RuneType r1 = perRunes.get(perRunes.size() - i - 1);
                    if (dtype[r1.runeTypeIndex] == 1 && setBonnusNum[r1.runeTypeIndex] == 2 && !tempRunes1.contains(r1)) {
                        tempRunes1.add(r1);
                        dtype[r1.runeTypeIndex]++;
                        System.out.println("Add broken runes pair : " + r1);
                    }
                }
            }            
            int count2 = 0;
            int count4 = 0;
            for (int i = 0; i < perRunes.size(); i++) {
                RuneType r1 = perRunes.get(perRunes.size() - i - 1);
                if (m3[r1.slot] < 10 && r1.runeTypeIndex == mainSetId && !tempRunes1.contains(r1)) {
                    if (!tempRunes1.contains(r1)){
                        tempRunes1.add(r1);
                        count2++;
                    }
                    m3[r1.slot]++;                    
                }                
                if (mainSet.contains(",") && m4[r1.runeTypeIndex*10+r1.slot]<5 && 
                        mainSet.toLowerCase().contains(r1.runeType.toLowerCase())) {
                    m4[r1.runeTypeIndex*10+r1.slot]++;
                    if (!tempRunes1.contains(r1)){
                        tempRunes1.add(r1);   
                        count4++;
                    }
                }
            }
            /*for (RuneType r1 : perRunes) {            
            if (r1.runeTypeIndex==mainSetId && !tempRunes1.contains(r1)){
                tempRunes1.add(r1);
                count2++;
            }
        }*/
            System.out.println("Add all left main runes : " + count2+" ; "+count4);
            System.out.println("After remove : " + perRunes.size() + " -> " + tempRunes1.size());
            perRunes = tempRunes1;
        }

        for (int i = 0; i < 20; i++) {
            //RuneType r1=perRunes.get(i);
            //System.out.println(r1.slot+" : "+paraRune.apply(r1)+" : "+r1);
        }

        /*for (RuneType r:optimizeRunes) {
         if (r.monsterId == RuneSet.runePet.id)
         System.out.println("Current Optimize Rune : " + r);
         }*/
        System.out.println(mainSet + " ; Optimize : " + optimizeRunes.size());
        System.out.println("main RunesSet : " + SwManager.runes.size());

        preRunes = optimizeRunes.size();
        preCalc = 1;

        for (int i = 1; i <= 6; i++) {
            long count = 0;
            long count3 = 0;
            List<String> rs = new ArrayList();
            for (RuneType r1 : perRunes) {
                if (r1.slot == i && paraRune.apply(r1) != null) {
                    rs.add(r1.runeType + ":" + r1.mainStat + ":" + r1.monster + ":" + paraRune.apply(r1));
                    count++;
                    if (r1.runeTypeIndex == mainSetId) {
                        count3++;
                    }
                }
            }
            System.out.println("Runes : " + (i - 1) + " ; " + count3 + " ; " + count + " ; " + rs);
            preCalc *= count;
        }

        System.out.println("Estimate : " + preCalc);
        if (mainSet.contains("x2")) {
            numSet = 4;
        }

        for (Set<Integer> g1 : orderPerm) {
            if (g1.size() == numSet) {
                alls.add(g1);
            }
        }

        //TODO : Sometime broken is better than full set.
        //Swift,Rage,Fatal, if the broken runes with super sub stat, they may result better
        //than 4 set runes. So we select runes with super high stats Rage->CD, Swift -> Spd...
        if (mainSetId == RAGE_SET) {
            List<RuneType> temp1 = new ArrayList();
            for (RuneType p1 : perRunes) {
                if (p1.runeTypeIndex != RAGE_SET) {
                    temp1.add(p1);
                }
            }
            System.out.println("Temp1.size : " + temp1.size());
            for (int i = 1; i <= 6; i++) {
                int count = 0;
                int cd_stat = 0;
                RuneType bestRune = null;
                for (RuneType p1 : SwManager.runes) {
                    if (p1.slot == i && paraRune.apply(p1) != null) {
                        count++;
                        if (p1.cd > cd_stat) {
                            cd_stat = p1.cd;
                            bestRune = p1;
                        }
                        if (!temp1.contains(p1)) {
                            temp1.add(p1);
                        }
                    }
                }
                //System.out.println("Slot "+i+" : "+count+" ; "+cd_stat+"  ; "+bestRune);
            }
            //System.out.println("Temp1.size : "+temp1.size());
            //System.exit(0);
        }
    }

    public static void preOptimizeGui(String mainSet, String paraRune, int paraRuneValue) {
        System.out.println("paraRune : [" + paraRune + "] ; paraRuneValue : " + paraRuneValue + " ; mainSet : " + mainSet);

        Function<RuneType, List<Integer>> paraRune1 = x -> (x.getParaList(paraRune, paraRuneValue));
        Collection<Set<Integer>> alls = Sets.newHashSet();
        preOptimize(mainSet, paraRune1, alls);
    }

    public static List<Function<RuneSet, Boolean>> paraRuneList = new ArrayList();

    public static Function<RuneSet, Integer> detectBestPara(String mainBestPara) {
        Function<RuneSet, Integer> bestpara = null;

        if (mainBestPara.equalsIgnoreCase("spd")) {
            bestpara = x -> (x.spd());
        }
        if (mainBestPara.equalsIgnoreCase("hp")) {
            bestpara = x -> (x.hp());
        }
        if (mainBestPara.equalsIgnoreCase("def")) {
            bestpara = x -> ((int) x.f_def);
        }
        if (mainBestPara.equalsIgnoreCase("cd")) {
            bestpara = x -> ((int) x.f_cd);
        }
        if (mainBestPara.equalsIgnoreCase("cr")) {
            bestpara = x -> (x.cr());
        }
        if (mainBestPara.equalsIgnoreCase("atk")) {
            bestpara = x -> (x.atk());
        }
        if (mainBestPara.equalsIgnoreCase("acc")) {
            bestpara = x -> (x.acc());
        }
        if (mainBestPara.equalsIgnoreCase("res")) {
            bestpara = x -> (x.res());
        }

        if (mainBestPara.equalsIgnoreCase("finalDamage")) {
            bestpara = x -> (x.finalDamage());
        }
        if (mainBestPara.equalsIgnoreCase("effectiveHP")) {
            bestpara = x -> (x.eHp());
        }
        if (mainBestPara.equalsIgnoreCase("atk*cd")) {
            bestpara = x -> ((int) (x.f_atk * x.f_cd));
        }
        if (mainBestPara.equalsIgnoreCase("DMG*spd")) {
            bestpara = x -> (x.finalDamage() * x.spd());
        }
        if (mainBestPara.equalsIgnoreCase("AVGDMG")) {
            bestpara = x -> (x.avgDamage());
        }
        if (mainBestPara.equalsIgnoreCase("AVGDMG*spd")) {
            bestpara = x -> (x.avgDamage() * x.spd());
        }
        if (mainBestPara.equalsIgnoreCase("hp*def")) {
            bestpara = x -> ((int) (x.f_hp * x.f_def));
        }
        if (mainBestPara.equalsIgnoreCase("hp*spd")) {
            bestpara = x -> ((int) (x.f_hp * x.f_spd));
        }
        if (mainBestPara.equalsIgnoreCase("effectHp*spd")) {
            bestpara = x -> ((int) (x.eHp() * x.f_spd));
        }
        if (mainBestPara.equalsIgnoreCase("hp bruiser")) {
            bestpara = x -> (int) (Math.pow(optHp(x), 1.25) / 1000.0 * x.spd() * damage(x));
        }
        if (mainBestPara.equalsIgnoreCase("bruiser")) {
            bestpara = x -> (int) (Math.pow(x.eHp(), 1.25) / 1000.0 * x.spd() * damage(x));
        }
        if (mainBestPara.equalsIgnoreCase("spd bruiser")) {
            bestpara = x -> (int) (optHp(x) * Math.pow(x.spd(), 1.5) * damage(x));
        }
        if (mainBestPara.equalsIgnoreCase("pve dd")) {
            bestpara = x -> (int) (Math.pow(x.eHp(), .333) / 100.0 * x.spd() * damage(x));
        }
        if (mainBestPara.equalsIgnoreCase("pve def dd")) {
            bestpara = x -> (int) (Math.pow(x.eHp(), .333) / 100.0 * x.spd() * defDamage(x));
        }
        if (mainBestPara.equalsIgnoreCase("pvp tank")) {
            bestpara = x -> (int) (optHp(x) * Math.pow(x.spd(), .5));
        }
        if (mainBestPara.equalsIgnoreCase("tank")) {
            bestpara = x -> (int) (x.eHp() * Math.pow(x.spd(), .5));
        }
        if (mainBestPara.equalsIgnoreCase("speed")) {
            bestpara = x -> (int) (Math.pow(x.eHp(), .333) * x.spd() * 1000);
        }
        if (mainBestPara.equalsIgnoreCase("speed hp")) {
            bestpara = x -> (int) (Math.pow(optHp(x), .333) * x.spd() * 1000);
        }
        return bestpara;
    }

    private static int damage(RuneSet x) {
        return (int) (x.atk() + x.atk() * x.cd() * Math.min(x.cr(), 100) / 10000.0);
    }

    private static int defDamage(RuneSet x) {
        return (int) (x.def() + x.def() * x.cd() * Math.min(x.cr(), 100) / 10000.0);
    }

    private static int optHp(RuneSet x) {
        return (int) (Math.pow(x.hp(), .25) * Math.pow(x.eHp(), .75) / 1000.0);
    }

    public static List<RuneSet> perMuteGUI(String mainSet, String paraRune, int paraRuneValue, String mainBestPara) {
        Function<RuneType, List<Integer>> paraRune1 = x -> (x.getParaList(paraRune, paraRuneValue));
        return perMute(mainSet, x -> (x.runesetOK(paraRuneList)), paraRune1, detectBestPara(mainBestPara));
    }

    public static boolean fullSearch = false;
    public static boolean fullStop = false;
    public static javax.swing.JProgressBar pBar = null;
    public boolean completeSet = false;

    public List<RuneSet> perMuteReal(String mainSet, Function<RuneSet, Boolean> para, Function<RuneType, List<Integer>> paraRune,
            Function<RuneSet, Integer> bestpara) {
        List<RuneSet> finalResult = new ArrayList();
        Collection<Set<Integer>> alls = Sets.newHashSet();

        foundBest = 0;
        bestRuneSet = null;
        curBestRuneSet = null;
        //System.out.println(alls.size()+" : "+alls);
        foundRuneGroup.clear();

        Set<String> mainStringSet = new HashSet();
        if (mainSet.contains("/")) {
            mainStringSet = RuneType.divideSet(mainSet);
        } else {
            mainStringSet.add(mainSet);
        }

        System.out.println("mainStringSet : " + mainStringSet + "; mainSet = " + mainSet);
        long t1 = System.currentTimeMillis();
        fullStop = false;

        allFirstRune = 0;

        for (String m2 : mainStringSet) {
            int mainSetId = RuneType.getSetId(m2);
            //loadCache();

            if (mainSetId < 0) {
                return null;
            }
            completeSet = false;

            int numSet = RuneType.setBonnusNum[mainSetId];
            if (mainSet.contains("x2")) {
                numSet = 4;
            }

            int secondSetId = RuneType.getSecondSetId(m2);
            System.out.println("*************Start permu***************");
            System.out.println("Mainset : " + mainSetId + " ; secondSet : " + secondSetId + " ; numSet = " + numSet);
            if (numSet == 4 && secondSetId >= 0) {
                completeSet = true;
                System.out.println("Complete set : " + m2);
            }


            /*for (RuneType r1:RuneSet.runePet.currentEquip.set){
         System.out.println("Check current : "+r1+" -> "+paraRune.apply(r1));
         }*/
            preOptimize(m2, paraRune, alls);

            //useThreads = false;
            icount = 0;
            startTime = System.currentTimeMillis();
            firstRuneRoundCount.set(0);
            estimateTime=" ";

            totalCount.set(0);
            for (Set<Integer> formation : alls) {
                int sum = 0;
                for (RuneType r1 : perRunes) {
                    if (runePosOK(r1, 0, mainSetId, formation)) {
                        sum++;
                    }
                }
                allFirstRune += sum;
            }
            //allFirstRune = perRunes.size() * alls.size();

            if (pBar != null) {
                pBar.setValue(0);
                pBar.setMaximum(allFirstRune);
            }
            runFirstRune = System.currentTimeMillis();
            boolean useNonRecursive = true;
            if (!useThreads) {

                for (Set<Integer> g1 : alls) {
                    System.out.println((icount++) + "/" + alls.size() + " : " + g1 + " ;  total = " + totalCount);
                    RuneSet b1 = null;
                    if (useNonRecursive) {
                        iteratePermute(mainSetId, g1, para, paraRune, bestpara, new ArrayList<RuneSet>());
                    } else {
                        recurPermute(0, new ArrayList(), mainSetId, g1, new HashSet<RuneType>(), para, paraRune, bestpara, new ArrayList<RuneSet>());
                    }
                }
            } else {
                ExecutorService executorService2 = Executors.newFixedThreadPool(numThreads);

                for (Set<Integer> g1 : alls) {
                    executorService2.submit(new Runnable() {
                        @Override
                        public void run() {
                            final Set<Integer> f2 = new HashSet();
                            f2.addAll(g1);
                            System.out.println((icount++) + "/" + alls.size() + " : " + f2 + " ; " + totalCount);
                            if (useNonRecursive) {
                                iteratePermute(mainSetId, f2, para, paraRune, bestpara, new ArrayList<RuneSet>());
                            } else {
                                recurPermute(0, new ArrayList(), mainSetId, f2, new HashSet<RuneType>(), para, paraRune, bestpara, new ArrayList<RuneSet>());
                            }
                        }
                    });
                }

                try {
                    executorService2.shutdown();
                    while (!executorService2.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                        if (fullStop) {
                            break;
                        }
                    }
                } catch (InterruptedException ie) {
                    executorService2.shutdownNow();
                }
            }
            System.out.println("bestRuneSetAll : " + foundRuneGroup.size() + "   ; mainSet = " + m2);
        }

        System.out.println("Finish in : " + (System.currentTimeMillis() - t1) / 1000 + " s.");
        System.out.println("totalCount : " + totalCount);

        if (foundRuneGroup.size() > 0) {
            bestRuneSet = foundRuneGroup.get(0);
            for (RuneSet s1 : foundRuneGroup) {
                //System.out.println("Final runes : "+s1.info()+" : "+bestpara.apply(s1));
                if (s1 != null && bestRuneSet != null) {
                    if (s1.compareTo(bestRuneSet) < 0) {
                        bestRuneSet = s1;
                    }
                }
            }
        }

        System.out.println("resultRunesList : " + resultTreeRunes.size());
        //resultTreeRunes.clear();
        long t2 = System.currentTimeMillis();
        int i = 0;
        for (RuneSet r1 : resultTreeRunes) {
            if (i < 10) {
                System.out.println((i++) + " : " + r1 + "-> " + r1.bestValue + " ; cost=" + r1.removeCost);
            }
        }
        System.out.println("Sort tree in : " + (System.currentTimeMillis() - t2) + " ms!");

        //saveCache();
        if (bestRuneSet != null) {
            System.out.println(RuneSet.runePet.name + " : ");
            System.out.println("Best build : " + bestRuneSet.details() + " : Total : " + bestpara.apply(bestRuneSet));
        }

        return finalResult;
    }
}
