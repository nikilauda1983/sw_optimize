package com.swrunes.swrunes;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.swrunes.swrunes.Bestiary.PetInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Crawls wiki pages to get documentation for certain monsters.
 */
public class Crawler {

    public static Document getDoc(String url) {
        try {
            String data = "";
            //url = "http://summonerswar.wikia.com/wiki/Perna";
            //System.out.println("crawlPage : " + url);
            try {
                data = Resources.toString(new URL(url), Charsets.UTF_8);
            } catch (Exception e) {
                //e.printStackTrace();
                data = readUrlData(url);
            }
            //System.out.println("Load from wiki : " + data);
            return Jsoup.parse(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
                InputStream streamReader = Crawler.class.getResourceAsStream(existingPath);
                if (streamReader != null) {
                    return ImageIO.read(streamReader);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //System.out.println("petName : "+petName);

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
            e.printStackTrace();
        }
        return null;
    }

    public static String readUrlData(String url) throws Exception {
        URL oracle = new URL(url);
        String content = "";
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine);
            content += inputLine + "\n";
        }
        in.close();
        return content;
    }

    public static String getMainStat(Elements eSkills, String stat, int homuIndex) {
        int hp1 = 0;
        for (Element t1 : eSkills) {
            if (t1.select("td").size() == 4) {
                //if (t1.text().contains("HP")) {
                if (t1.text().contains(stat)) {
                    hp1++;
                    if (hp1 % 2 == 0) {
                        //System.out.println(hp1 + " : " + t1.text());
                        if (hp1 / 2 == homuIndex + 1) {
                            return t1.select("td").get(3).text();
                        }
                        //petinfo.hp = t1.select("td").get(3).text();
                    }
                }
            }
        }
        return "";
    }

    public static Bestiary.PetInfo crawlHomuPage(String url, String element) {
        try {
            long l1 = System.currentTimeMillis();
            String data = "";
            //url = "http://summonerswar.wikia.com/wiki/Perna";
            //System.out.println("crawlPage : " + url);
            try {
                data = Resources.toString(new URL(url), Charsets.UTF_8);
            } catch (Exception e) {
                data = readUrlData(url);
            }

            //System.out.println("Load from wiki : " + data);
            Document doc = Jsoup.parse(data);
            Bestiary.PetInfo petinfo = new Bestiary.PetInfo();

            Elements eSkills = doc.select("table.article-table tr");
            int count = 0;

            String homuSkill = "Fire";
            int homuIndex = 0;
            if (element.equalsIgnoreCase("Water")) {
                homuIndex = 1;
            }
            if (element.equalsIgnoreCase("Wind")) {
                homuIndex = 2;
            }
            if (element.equalsIgnoreCase("Light")) {
                homuIndex = 3;
            }
            if (element.equalsIgnoreCase("Dark")) {
                homuIndex = 4;
            }
            petinfo.element = element;
            int hp1 = 0;
            Bestiary.SkillWikiInfo skillInfo = new Bestiary.SkillWikiInfo();

            for (Element t1 : eSkills) {
                if (!t1.classNames().contains("monstercell")) {
                    Elements el = t1.select("li");
                    String multy = t1.select("span.basic-tooltip").text();
                    String tooltip = t1.select("span.basic-tooltip").attr("title");
                    if (multy.length() > 0) {
                        //System.out.println("\ntooltip : " + tooltip + " ; multy = " + multy);
                    }
                    if (el.size() > 0) {
                        if (t1.select("td").size() == 2) {
                            count++;
                            //System.out.println(count+" : ["+t1.text()+"]");
                            Element skill = t1.select("td").get(1);
                            String skillName = skill.select("b").text();
                            String skildes = skill.toString();
                            skildes = skildes.substring(0, skildes.indexOf("<ul>")).replace("<td>", "").trim();

                            if (skillName.equalsIgnoreCase("Frost Ray")) {
                                homuSkill = "Water";
                            }
                            if (skillName.equalsIgnoreCase("Shock Ray")) {
                                homuSkill = "Wind";
                            }
                            if (skillName.equalsIgnoreCase("Light Shock")) {
                                homuSkill = "Light";
                            }
                            if (skillName.equalsIgnoreCase("Dark Shock")) {
                                homuSkill = "Dark";
                            }

                            //System.out.println(count + " : SkillName : [" + skillName + "]     ;  " + homuSkill);
                            //System.out.println("Skill : [" + skildes + "]");

                            skillInfo = new Bestiary.SkillWikiInfo();
                            skillInfo.tooltip = tooltip;
                            skillInfo.multy = multy;
                            skillInfo.skill_desc = skildes;
                            skillInfo.skill_name = skillName;
                            for (Element e1 : el) {
                                //System.out.println(e1.text());
                                skillInfo.skill_up.add(e1.text());
                            }
                            if (homuSkill.equalsIgnoreCase(element) && skillName.length() > 2) {
                                petinfo.skills.add(skillInfo);
                            }
                        }
                    }
                }
            }

            Bestiary.SkillWikiInfo passiveSkill = new Bestiary.SkillWikiInfo();
            passiveSkill.skill_name = "Magic Power Explosion (Passive)";
            passiveSkill.skill_desc = "Magic Power Explosion (Passive): Revives with 50% HP when you’re inflicted with fatal damage and falls under the Magic Power Explosion state for 2 turns. The Magic Power Explosion state will increase the damage inflicted to the enemies by 30%, "
                    + "but you’ll be uncontrollable and die when the effect ends. (Reusable in 10 turns)";
            petinfo.skills.add(passiveSkill);

            petinfo.hp = getMainStat(eSkills, "HP", homuIndex);
            petinfo.def = getMainStat(eSkills, "DEF", homuIndex);
            petinfo.atk = getMainStat(eSkills, "ATK", homuIndex);

            petinfo.spd = "100";
            petinfo.cr = "15";
            petinfo.cd = "50";
            petinfo.res = "15";
            petinfo.acc = "50";
            petinfo.aName = "Homun" + element;
            petinfo.uName = "Homunculus";
            petinfo.star = "5";

            return petinfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void crawlSkills(Document doc, PetInfo petinfo, String prefix) {
        int d1 = doc.select("div.row").size();
        //System.out.println("d1 : "+d1);
        Element main = doc.select("div.row").get(0);
        if (d1 == 4) {
            main = doc.select("div.row").get(2);
        }
        Elements sk2 = main.select("div.panel.panel-default");
        for (Element sk : sk2) {
            int li = sk.select("li").size();
            if (li > 0) {
                //System.out.println(li + " : " + sk.text());
                String name = sk.select("p.panel-title").text();
                //System.out.println("Skill : " + name);
                //System.out.println("Skill des: " + sk.select("p").get(1).text());
                Elements ff = sk.select("li:contains(Formula) p");

                Elements uls = sk.select("ul.list-unstyled li");
                Bestiary.SkillWikiInfo skillInfo = new Bestiary.SkillWikiInfo();

                skillInfo.skill_desc = name + ": " + sk.select("p").get(1).text();

                if (prefix.length() > 0) {
                    name += " (" + prefix + ")";
                }
                skillInfo.skill_name = name;
                skillInfo.tooltip = "";

                for (Element ul : uls) {
                    //System.out.println("Skill up : " + ul.text());
                    skillInfo.skill_up.add(ul.text());
                }
                if (ff.size() == 2) {
                    //System.out.println("Skill Multy: " + ff.get(1).text());
                    skillInfo.multy = ff.get(1).text();
                }
                petinfo.skills.add(skillInfo);
            }
        }

    }

    public static Bestiary.PetInfo crawlSwafarmPage(String url) {
        try {
            //url = "http://summonerswar.wikia.com/wiki/Perna";
            //System.out.println("crawlPage : " + url);
            //System.out.println("Load from wiki : " + data);
            Document doc = getDoc(url);
            Bestiary.PetInfo petinfo = new Bestiary.PetInfo();
            Elements n1 = doc.select("div.bestiary-name");
            String fullname = doc.select("div.bestiary-name h1").text();
            String petType = doc.select("div.bestiary-name h1 small").text();
            String uname = "";
            if (n1.size() > 1) {
                fullname = n1.get(1).select("h1").text();
                petType = n1.get(1).select("h1 small").text();
                uname = n1.get(0).select("h1").text();
            }
            fullname = fullname.replace(petType, "").trim();
            uname = uname.replace(petType, "").trim();
            petinfo.crawlLink = url;

            crawlSkills(doc, petinfo, "");
            Elements tables = doc.select("div.table-responsive");
            int i = 0;
            List<String> pstats = new ArrayList();
            for (Element tr : tables.get(tables.size() - 1).select("tr")) {
                if (i > 0) {
                    int d1 = tr.select("td").size();
                    String vl = tr.select("td").get(d1 - 1).text();
                    //System.out.println(i + " : " + vl + "  ;  " + tr.text());
                    pstats.add(vl);
                }
                i++;
            }

            String s1 = doc.select("li.active").text();
            String[] listE = {"Fire", "Water", "Dark", "Light", "Wind"};
            for (String l1 : listE) {
                if (s1.contains("(" + l1 + ")")) {
                    petinfo.element = l1;
                }
            }
            petinfo.hp = pstats.get(0);
            petinfo.atk = pstats.get(1);
            petinfo.def = pstats.get(2);
            petinfo.spd = pstats.get(3);
            petinfo.cr = pstats.get(4);
            petinfo.cd = pstats.get(5);
            petinfo.acc = pstats.get(6);
            petinfo.res = pstats.get(7);

            petinfo.star = "" + (doc.select("div.monster-box").get(0).select("img").size() - 1);

            petinfo.aName = fullname;
            petinfo.uName = uname;
            //System.out.println("Full name : [" + fullname + "]");
            //System.out.println("uname : [" + uname + "]");
            //System.out.println("petType : " + petType);

            if (uname.contains("Unicorn")) {
                String humanLink = url.replace("-unicorn", "");
                humanLink = humanLink.replace(fullname.toLowerCase(), fullname.toLowerCase() + "-human");
                //System.out.println("Human Link : "+humanLink);
                crawlSkills(getDoc(humanLink), petinfo, "Human");
            }

            Gson gson = new Gson();
            //System.out.println("json : " + gson.toJson(petinfo));

            Bestiary.addPet(fullname, petinfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bestiary.PetInfo crawlPage(String url) {
        try {
            long l1 = System.currentTimeMillis();
            if (url.length() == 0) {
                url = "http://summonerswar.wikia.com/wiki/Perna";
            }
            String data = "";

            //url = "http://summonerswar.wikia.com/wiki/Perna";
            //System.out.println("crawlPage : " + url);
            try {
                data = Resources.toString(new URL(url), Charsets.UTF_8);
            } catch (Exception e) {
                data = readUrlData(url);
            }

            //System.out.println("Load from wiki : " + data);
            Document doc = Jsoup.parse(data);
            Bestiary.PetInfo petinfo = new Bestiary.PetInfo();

            String fullname = doc.select("div.page-header__main h1").text().split(" - ")[1].trim();
            Elements eName = doc.select("th#header font");
            petinfo.element = doc.select("th#header img").attr("alt");

            petinfo.uName = eName.get(0).text();
            petinfo.aName = fullname;
            String star = eName.get(1).text();
            if (!star.contains("x")) {
                star = "1";
            }
            star = star.replace("(", "").replace(")", "").replace("x", "").trim();
            petinfo.star = star;
            //System.out.println("fullname : " + fullname);
            //System.out.println("unawake Name : " + petinfo.uName);
            //System.out.println("awake Name : " + petinfo.aName);
            //System.out.println("element : " + petinfo.element);
            //System.out.println("star : " + petinfo.star);

            Elements eSkills = doc.select("table.monstertable tr");
            int count = 0;
            Bestiary.SkillWikiInfo skillInfo = new Bestiary.SkillWikiInfo();
            for (Element t1 : eSkills) {
                if (!t1.classNames().contains("monstercell")) {
                    count++;
                    Elements el = t1.select("li");
                    String multy = t1.select("span.basic-tooltip").text();
                    String tooltip = t1.select("span.basic-tooltip").attr("title");
                    if (multy.length() > 0) {
                        //System.out.println("tooltip : " + tooltip + " ; multy = " + multy);
                    }
                    if (el.size() > 0) {
                        for (Element e1 : el) {
                            //System.out.println(e1.text());
                            skillInfo.skill_up.add(e1.text());
                        }
                    } else {
                        if (t1.select("td").size() == 2) {
                            Element skill = t1.select("td").get(1);
                            //System.out.println("Skill : " + skill);
                            //System.out.println(count + " : [" + t1.text() + "]");

                            skillInfo = new Bestiary.SkillWikiInfo();
                            skillInfo.tooltip = tooltip;
                            skillInfo.multy = multy;
                            skillInfo.skill_desc = skill.text();
                            skillInfo.skill_name = skill.select("b").text();
                            petinfo.skills.add(skillInfo);
                        }
                    }
                }
            }
            //System.out.println("Stats : "+e.text());
            List<String> pstats = new ArrayList();
            //Elements e = doc.select("tr td.monstercell");
            Elements e = doc.select("tr");
            for (Element e2 : e) {
                Elements e3 = e2.select("td.monstercell");
                if (e3.size() > 0) {
                    pstats.add(e3.get(e3.size() - 1).text());
                }
                if (e3.size() == 6) {
                    //System.out.println("e3 : "+e3.text());
                    for (Element e4 : e3) {
                        pstats.add(e4.text());
                    }
                }
            }
            //System.out.println("pstats : " + pstats);
            for (int i = 0; i < pstats.size(); i++) {
                //System.out.println(i+" : "+pstats.get(i));
            }

            Map<String, String> stats = new HashMap();
            petinfo.hp = pstats.get(3);
            petinfo.atk = pstats.get(4);
            petinfo.def = pstats.get(5);

            petinfo.spd = pstats.get(22);
            petinfo.cr = pstats.get(23);
            petinfo.cd = pstats.get(24);
            petinfo.res = pstats.get(25);
            petinfo.acc = pstats.get(26);

            //System.out.println("stats : " + stats);

            //System.out.println("Done in " + (System.currentTimeMillis() - l1));
            Gson gson = new Gson();
            //System.out.println("json : " + gson.toJson(petinfo));

            return petinfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void crawlAllImage() {
        Bestiary.getInstance();
        for (PetInfo p : Bestiary.getInstance().allPets.values()) {
            crawlPetPicture(p.aName);
        }
    }

    public static void crawlPetInfo(String petName) {
        Gson gson = new Gson();
        Bestiary.getInstance();
        String url = "http://summonerswar.wikia.com" + petName;
        //System.out.println("url : " + url);

        PetInfo p1 = crawlPage(url);
        //System.out.println("p1 : " + gson.toJson(p1));
        Bestiary.addPet(p1.aName, p1);
    }

    public static void crawlPetHomuInfo(String petName, String element, String uName) {
        Gson gson = new Gson();
        Bestiary.getInstance();
        String url = "http://summonerswar.wikia.com" + petName;
        //System.out.println("url : " + url);

        PetInfo p1 = crawlHomuPage(url, element);
        //System.out.println("p1 : " + p1.aName + " : " + gson.toJson(p1));
        int count = 0;
        p1.uName = uName;
        for (Bestiary.SkillWikiInfo k1 : p1.skills) {
            //System.out.println((count++)+" : "+k1.skill_name);
        }
        Bestiary.addPet(p1.aName, p1);
    }

    public static void crawlAll() {
        try {
            String data = Resources.toString(new URL("http://summonerswar.wikia.com/wiki/Monster_Collection"), Charsets.UTF_8);
            //System.out.println("Load from wiki : " + data);

            Document doc = Jsoup.parse(data);
            Elements e = doc.select("a.image.image-thumbnail.link-internal");
            Bestiary.getInstance();

            int count = 0;
            for (Element e1 : e) {
                String url = e1.attr("href");
                if (!url.contains("(")) {
                    if (url.contains("Angelmon") || url.contains("Devilmon") || url.contains("Rainbowmon") || url.contains("Varis")) {
                        continue;
                    }

                    count++;
                    //
                    //http://summonerswar.wikia.com/
                    if (Bestiary.getInstance().allPets.containsKey(url)) {
                        continue;
                    }
                    if (url.contains("Homunculus")) {
                        continue;
                    }

                    if (count < 500) {
                        //System.out.println(count + " : " + url);
                        String nurl = url;
                        if (url.contains("Feng_Yan")) {
                            nurl = "wiki/Panda_Warrior_(Wind)";
                        }
                        PetInfo p1 = crawlPage("http://summonerswar.wikia.com/" + nurl);
                        Bestiary.addPet(url, p1);

                        if (count % 10 == 0) {
                            //Bestiary.getInstance().saveFile();
                        }
                    }
                }
            }
            //Bestiary.getInstance().saveFile();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkMissingPet() {
        Bestiary.getInstance();
        for (String p1 : Bestiary.getInstance().allPets.keySet()) {
            PetInfo pet = Bestiary.getInstance().allPets.get(p1);
            if (pet.aName.contains("(") || pet.aName.length() < 3) {
                //System.out.println("Found : " + pet.aName + " ; " + p1);
                //crawlPetInfo(p1);
            }
        }
    }

    public static void checkMissingAvatar() {
        Bestiary.getInstance();
        int count = 0;
        try {
            for (String p1 : Bestiary.getInstance().allPets.keySet()) {
                PetInfo pet = Bestiary.getInstance().allPets.get(p1);
                String existingPath = "/imgs/" + pet.aName + ".png";
                InputStream streamReader = Crawler.class.getResourceAsStream(existingPath);
                if (streamReader == null) {
                    //System.out.println("No image : " + pet.aName);
                    String petName = pet.aName;
                    if (petName.contains(" ")) {
                        petName = petName.replace(" ", "_");
                    }
                    if (petName.contains("Feng_Yan")) {
                        petName = "Panda_Warrior_(Wind)";
                    }
                    String imageUrl = "";
                    if (pet.aName.equalsIgnoreCase("HomunLight")) {
                        imageUrl = "http://swarfarm.com/static/herders/images/monsters/unit_icon_0044_2_1.png";
                    }
                    if (pet.aName.equalsIgnoreCase("HomunDark")) {
                        imageUrl = "http://swarfarm.com/static/herders/images/monsters/unit_icon_0044_3_1.png";
                    }
                    if (imageUrl.isEmpty()) {
                        if (pet.crawlLink != null && pet.crawlLink.contains("swarfarm")) {
                            //System.out.println("crawllink : "+pet.crawlLink);
                            Document doc = getDoc(pet.crawlLink);
                            imageUrl = "https://swarfarm.com" + doc.select("div.monster-box").get(1).select("img").attr("src");
                        } else {
                            if (petName.compareToIgnoreCase("Bagir") == 0) {
                                petName = "Giant_Warrior_(Water)_-_Bagir";
                            }
                            //System.out.println("Load image petName : " + petName);
                            String data = Resources.toString(new URL("http://summonerswar.wikia.com/wiki/" + petName), Charsets.UTF_8);
                            //System.out.println("Load from wiki : " + data);

                            Document doc = Jsoup.parse(data);
                            Elements e = doc.select("div.monstertable a");
                            imageUrl = e.get(1).attr("href");
                        }
                    }
                    //System.out.println("imageUrl : " + imageUrl);

                    BufferedImage image = ImageIO.read(new URL(imageUrl));
                    if (image.getWidth() < 80) {
                        //System.out.println("Image too small, maybe wrong!");
                        continue;
                    }
                    //System.out.println(pet.aName + " ; Image size : " + image.getHeight() + " x " + image.getWidth());
                    ImageIO.write(image, "png", new File(existingPath));
                } else {
                    count++;
                }
            }
            //System.out.println("Found " + count + " images. on " + Bestiary.getInstance().allPets.size() + " pets in Bestiary");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void crawlPatch1() {
        Set<String> crawled = new HashSet();
        for (PetInfo p1 : Bestiary.getInstance().allPets.values()) {
            if (p1.uName.contains("Drunken") || p1.uName.contains("Dragon Knight")
                    || p1.uName.contains("Desert Queen")
                    || p1.uName.contains("Sky Dancer")
                    || p1.uName.contains("Polar Queen")
                    || p1.uName.contains("Barbaric")
                    || p1.uName.contains("Chimera")
                    || p1.uName.contains("Bounty")
                    || p1.uName.contains("Monkey")
                    || p1.uName.contains("Sylph")
                    || p1.uName.contains("Pioneer")) {
                String s1 = "/wiki/" + p1.aName;
                s1 = s1.replace(" ", "_");
                crawlPetInfo(s1);
                crawled.add(p1.aName);
            }
        }
        //System.out.println("Crawled : " + crawled);
        Bestiary.getInstance().saveFile();
    }

    public static void crawlHomunculus() {
        //crawlPetHomuInfo("/wiki/Homunculus", "Fire");
        //crawlPetHomuInfo("/wiki/Homunculus", "Water");
        //crawlPetHomuInfo("/wiki/Homunculus", "Wind");
        crawlPetHomuInfo("/wiki/Homunculus", "Light", "HomunLD");
        crawlPetHomuInfo("/wiki/Homunculus", "Dark", "HomunLD");
        Bestiary.getInstance().saveFile();
    }

    public static void crawlFamily(String familyName) {
        familyName = familyName.trim();
        familyName = familyName.replace(" ", "_");
        String[] list = {"Fire", "Water", "Dark", "Light", "Wind"};
        for (String s : list) {
            String u = familyName + "_(" + s + ")";
            //System.out.println(u);
            crawlPetInfo("/wiki/" + u);
        }
    }

    public static void crawlDicePets() {
        crawlFamily("Dice_Magician");
        crawlFamily("Harp_Magician");
        Bestiary.getInstance().saveFile();
    }

    public static void crawlPatch3_2_5() {
        crawlFamily("Hell_Lady");
        crawlFamily("Sea_Emperor");
        crawlFamily("Ninja");
        crawlFamily("Occult Girl");
        crawlFamily("Beast Monk");
        crawlFamily("Martial Artist");
        crawlFamily("Brownie Magician");
        crawlFamily("Grim Reaper");
        crawlFamily("Sky Dancer");
        crawlFamily("Drunken Master");
        crawlFamily("Horus");
        crawlFamily("Jack-o'-lantern");
        Bestiary.getInstance().saveFile();
    }

    public static void crawlPatch3_2_0() {
        crawlFamily("Dragon_Knight");
        crawlFamily("Fairy_King");
        crawlFamily("Archangel");
        crawlFamily("Sylphid");
        crawlFamily("Magic_Knight");
        crawlFamily("Mermaid");
        crawlFamily("Sea_Emperor");
        crawlFamily("Phantom_Thief");
        crawlFamily("Anubis");
        crawlFamily("Bounty_Hunter");
        Bestiary.getInstance().saveFile();
    }

    public static void crawlUnicorns() {
        crawlSwafarmPage("https://swarfarm.com/bestiary/dark-unicorn-alexandra/");
        crawlSwafarmPage("https://swarfarm.com/bestiary/fire-unicorn-helena/");
        crawlSwafarmPage("https://swarfarm.com/bestiary/water-unicorn-amelia/");
        crawlSwafarmPage("https://swarfarm.com/bestiary/light-unicorn-eleanor/");
        crawlSwafarmPage("https://swarfarm.com/bestiary/wind-unicorn-diana/");
        Bestiary.getInstance().saveFile();
    }

    public static void main(String[] args) {
        Bestiary.getInstance();
        //crawlSwafarmPage("https://swarfarm.com/bestiary/wind-unicorn-diana/");
        //crawlPatch3_2_0();
        //crawlPatch3_2_5();
        //crawlUnicorns();

        //crawlAll();
        //crawlPage("");
        //crawlAllImage();
        //crawlPetInfo("Shihwa");
        //crawlHomunculus();
        //crawlFamily("Boomerang Warrior");
        //crawlFamily("Chakram Dancer");        
        //Bestiary.getInstance().saveFile();
        //crawlPetHomuInfo("/wiki/Homunculus", "Light");

        //crawlDicePets();
        //checkMissingAvatar();
        //crawlFamily("Beast Monk");
        //crawlFamily("Monkey King");
        //crawlFamily("Pixie");
        //crawlFamily("Sylphid");
        crawlFamily("Dryad");
        crawlFamily("Druid");
        crawlFamily("Lightning Emperor");
        crawlFamily("Giant Warrior");
        Bestiary.getInstance().saveFile();
    }
}
