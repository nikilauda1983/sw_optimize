package com.swrunes.swrunes;

import static com.swrunes.gui.Application.scaleImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.swing.ImageIcon;

import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

import com.swrunes.swrunes.RuneType.RuneSet;

import static com.swrunes.swrunes.SwManager.petsBestiary;

public class PetType implements Comparable<PetType> {

    @Override
    public int compareTo(PetType o) {
        return Integer.compare(o.finalValue, finalValue);
    }

    public int finalValue = 0;

    public static class RuneSkill {

        public String skillName;
        int skillType;
        public String skillMulty, skillDesc;
        public int skillUp = 0;
        public int numHits = 1;
        public boolean noWikiMulty = false;
        public String hitStr = "";
        public boolean isAoe = false;
        public double aoeIndex = 1.0;
        public boolean isAoeRandom = false;
        public boolean ignoreChloe = false;
        public boolean ignoreChance = false;
        public long debuffScale = 0;
        public long debuffIncre = 0;
        public long ignoreChanceNum = 0;
        public long ignoreChanceHit = 0;
        public double extra_cd = 0.0;
        public double extra_atk = 0.0;
        public double extra_damage = 1.0;

        double skillValue = 0;
        public boolean ignoreDmg = false;
        public Function<RuneSet, Double> damageMultySkill = null;
        public Function<RuneSet, Double> afterMulty = null;
        public boolean isBomb = false;
        public int type = 0;

        public RuneSkill() {

        }

        @Override
        public String toString() {
            return "[" + skillName + " : " + skillMulty + " ; hits : " + numHits + "; skillsup = " + skillUp + "] " + damageMultySkill;
        }
    }

    ;

    public RuneSet savedBuild = null;
    public RuneSkill skillItem = new RuneSkill();
    public List<RuneSkill> skillList = new ArrayList();

    public int runesEquip = 0;
    int stats[];

    public List<RuneType> equipRuneList = new ArrayList();
    public String name, attribute, oname, a_name = "", u_name = "", full_name = "";
    public int id, stars, element;
    public String unit_id, master_id;

    public static String[] petLabels = {"b_hp", "b_def", "b_atk", "b_cdmg", "b_crate", "b_acc", "b_res", "b_spd"};
    public static String[] petLabelsNew = {"con", "def", "atk", "critical_damage", "critical_rate", "accuracy", "resist", "spd"};

    ImageIcon petIcon = null;

    public ImageIcon getPetIcon() {
        if (petIcon == null) {
            petIcon = new ImageIcon(scaleImage(Crawler.crawlPetPicture(this.a_name), 0.5));
            return petIcon;
        } else
            return petIcon;
    }

    public int baseStats[] = new int[petLabels.length];
    public Map<String, Integer> statfixMap = new HashMap();
    Map<String, Integer> baseMap = new HashMap();
    public Map<String, String> comboMap = new HashMap();
    public String[] comboList = new String[petLabels.length];

    public static Map<String, String> petNameMap = new HashMap();
    public static Map<String, String> petNameMapInv = new HashMap();
    public static Map<String, String> petNameElement = new HashMap();

    // Mapping can be found at https://github.com/lstern/SWProxy-plugins/blob/master/SWParser/monsters.py
    {
        petNameMap.put("101", "Fairy");
        petNameMap.put("10111", "Elucia");
        petNameMap.put("10112", "Iselia");
        petNameMap.put("10113", "Aeilene");
        petNameMap.put("10114", "Neal");
        petNameMap.put("10115", "Sorin");

        petNameMap.put("102", "Imp");
        petNameMap.put("10211", "Fynn");
        petNameMap.put("10212", "Cogma");
        petNameMap.put("10213", "Ralph");
        petNameMap.put("10214", "Taru");
        petNameMap.put("10215", "Garok");

        petNameMap.put("103", "Pixie");
        petNameMap.put("10311", "Kacey");
        petNameMap.put("10312", "Tatu");
        petNameMap.put("10313", "Shannon");
        petNameMap.put("10314", "Cheryl");
        petNameMap.put("10315", "Camaryn");

        petNameMap.put("104", "Yeti");
        petNameMap.put("10411", "Kunda");
        petNameMap.put("10412", "Tantra");
        petNameMap.put("10413", "Rakaja");
        petNameMap.put("10414", "Arkajan");
        petNameMap.put("10415", "Kumae");

        petNameMap.put("105", "Harpy");
        petNameMap.put("10511", "Ramira");
        petNameMap.put("10512", "Lucasha");
        petNameMap.put("10513", "Prilea");
        petNameMap.put("10514", "Kabilla");
        petNameMap.put("10515", "Hellea");

        petNameMap.put("106", "Hellhound");
        petNameMap.put("10611", "Tarq");
        petNameMap.put("10612", "Sieq");
        petNameMap.put("10613", "Gamir");
        petNameMap.put("10614", "Shamar");
        petNameMap.put("10615", "Shumar");

        petNameMap.put("107", "Warbear");
        petNameMap.put("10711", "Dagora");
        petNameMap.put("10712", "Ursha");
        petNameMap.put("10713", "Ramagos");
        petNameMap.put("10714", "Lusha");
        petNameMap.put("10715", "Gorgo");

        petNameMap.put("108", "Elemental");
        petNameMap.put("10811", "Daharenos");
        petNameMap.put("10812", "Bremis");
        petNameMap.put("10813", "Taharus");
        petNameMap.put("10814", "Priz");
        petNameMap.put("10815", "Camules");

        petNameMap.put("109", "Garuda");
        petNameMap.put("10911", "Konamiya");
        petNameMap.put("10912", "Cahule");
        petNameMap.put("10913", "Lindermen");
        petNameMap.put("10914", "Teon");
        petNameMap.put("10915", "Rizak");

        petNameMap.put("110", "Inugami");
        petNameMap.put("11011", "Icaru");
        petNameMap.put("11012", "Raoq");
        petNameMap.put("11013", "Ramahan");
        petNameMap.put("11014", "Belladeon");
        petNameMap.put("11015", "Kro");

        petNameMap.put("111", "Salamander");
        petNameMap.put("11111", "Kaimann");
        petNameMap.put("11112", "Krakdon");
        petNameMap.put("11113", "Lukan");
        petNameMap.put("11114", "Sharman");
        petNameMap.put("11115", "Decamaron");

        petNameMap.put("112", "Nine-tailed Fox");
        petNameMap.put("11211", "Soha");
        petNameMap.put("11212", "Shihwa");
        petNameMap.put("11213", "Arang");
        petNameMap.put("11214", "Chamie");
        petNameMap.put("11215", "Kamiya");

        petNameMap.put("113", "Serpent");
        petNameMap.put("11311", "Shailoq");
        petNameMap.put("11312", "Fao");
        petNameMap.put("11313", "Ermeda");
        petNameMap.put("11314", "Elpuria");
        petNameMap.put("11315", "Mantura");

        petNameMap.put("114", "Golem");
        petNameMap.put("11411", "Kuhn");
        petNameMap.put("11412", "Kugo");
        petNameMap.put("11413", "Ragion");
        petNameMap.put("11414", "Groggo");
        petNameMap.put("11415", "Maggi");

        petNameMap.put("115", "Griffon");
        petNameMap.put("11511", "Kahn");
        petNameMap.put("11512", "Spectra");
        petNameMap.put("11513", "Bernard");
        petNameMap.put("11514", "Shamann");
        petNameMap.put("11515", "Varus");

        petNameMap.put("116", "Undine");
        petNameMap.put("11611", "Mikene");
        petNameMap.put("11612", "Atenai");
        petNameMap.put("11613", "Delphoi");
        petNameMap.put("11614", "Icasha");
        petNameMap.put("11615", "Tilasha");

        petNameMap.put("117", "Inferno");
        petNameMap.put("11711", "Purian");
        petNameMap.put("11712", "Tagaros");
        petNameMap.put("11713", "Anduril");
        petNameMap.put("11714", "Eludain");
        petNameMap.put("11715", "Drogan");

        petNameMap.put("118", "Sylph");
        petNameMap.put("11811", "Tyron");
        petNameMap.put("11812", "Baretta");
        petNameMap.put("11813", "Shimitae");
        petNameMap.put("11814", "Eredas");
        petNameMap.put("11815", "Aschubel");

        petNameMap.put("119", "Sylphid");
        petNameMap.put("11911", "Lumirecia");
        petNameMap.put("11912", "Fria");
        petNameMap.put("11913", "Acasis");
        petNameMap.put("11914", "Mihael");
        petNameMap.put("11915", "Icares");

        petNameMap.put("120", "High Elemental");
        petNameMap.put("12011", "Ellena");
        petNameMap.put("12012", "Kahli");
        petNameMap.put("12013", "Moria");
        petNameMap.put("12014", "Shren");
        petNameMap.put("12015", "Jumaline");

        petNameMap.put("121", "Harpu");
        petNameMap.put("12111", "Sisroo");
        petNameMap.put("12112", "Colleen");
        petNameMap.put("12113", "Seal");
        petNameMap.put("12114", "Sia");
        petNameMap.put("12115", "Seren");

        petNameMap.put("122", "Slime");
        petNameMap.put("12211", "");
        petNameMap.put("12212", "");
        petNameMap.put("12213", "");
        petNameMap.put("12214", "");
        petNameMap.put("12215", "");

        petNameMap.put("123", "Forest Keeper");
        petNameMap.put("12311", "");
        petNameMap.put("12312", "");
        petNameMap.put("12313", "");
        petNameMap.put("12314", "");
        petNameMap.put("12315", "");

        petNameMap.put("124", "Mushroom");
        petNameMap.put("12411", "");
        petNameMap.put("12412", "");
        petNameMap.put("12413", "");
        petNameMap.put("12414", "");
        petNameMap.put("12415", "");

        petNameMap.put("125", "Maned Boar");
        petNameMap.put("12511", "");
        petNameMap.put("12512", "");
        petNameMap.put("12513", "");
        petNameMap.put("12514", "");
        petNameMap.put("12515", "");

        petNameMap.put("126", "Monster Flower");
        petNameMap.put("12611", "");
        petNameMap.put("12612", "");
        petNameMap.put("12613", "");
        petNameMap.put("12614", "");
        petNameMap.put("12615", "");

        petNameMap.put("127", "Ghost");
        petNameMap.put("12711", "");
        petNameMap.put("12712", "");
        petNameMap.put("12713", "");
        petNameMap.put("12714", "");
        petNameMap.put("12715", "");

        petNameMap.put("128", "Low Elemental");
        petNameMap.put("12811", "Tigresse");
        petNameMap.put("12812", "Lamor");
        petNameMap.put("12813", "Samour");
        petNameMap.put("12814", "Varis");
        petNameMap.put("12815", "Havana");

        petNameMap.put("129", "Mimick");
        petNameMap.put("12911", "");
        petNameMap.put("12912", "");
        petNameMap.put("12913", "");
        petNameMap.put("12914", "");
        petNameMap.put("12915", "");

        petNameMap.put("130", "Horned Frog");
        petNameMap.put("13011", "");
        petNameMap.put("13012", "");
        petNameMap.put("13013", "");
        petNameMap.put("13014", "");
        petNameMap.put("13015", "");

        petNameMap.put("131", "Sandman");
        petNameMap.put("13111", "");
        petNameMap.put("13112", "");
        petNameMap.put("13113", "");
        petNameMap.put("13114", "");
        petNameMap.put("13115", "");

        petNameMap.put("132", "Howl");
        petNameMap.put("13211", "Lulu");
        petNameMap.put("13212", "Lala");
        petNameMap.put("13213", "Chichi");
        petNameMap.put("13214", "Shushu");
        petNameMap.put("13215", "Chacha");

        petNameMap.put("133", "Succubus");
        petNameMap.put("13311", "Izaria");
        petNameMap.put("13312", "Akia");
        petNameMap.put("13313", "Selena");
        petNameMap.put("13314", "Aria");
        petNameMap.put("13315", "Isael");

        petNameMap.put("134", "Joker");
        petNameMap.put("13411", "Sian");
        petNameMap.put("13412", "Jojo");
        petNameMap.put("13413", "Lushen");
        petNameMap.put("13414", "Figaro");
        petNameMap.put("13415", "Liebli");

        petNameMap.put("135", "Ninja");
        petNameMap.put("13511", "Susano");
        petNameMap.put("13512", "Garo");
        petNameMap.put("13513", "Orochi");
        petNameMap.put("13514", "Gin");
        petNameMap.put("13515", "Han");

        petNameMap.put("136", "Surprise Box");
        petNameMap.put("13611", "");
        petNameMap.put("13612", "");
        petNameMap.put("13613", "");
        petNameMap.put("13614", "");
        petNameMap.put("13615", "");

        petNameMap.put("137", "Bearman");
        petNameMap.put("13711", "Gruda");
        petNameMap.put("13712", "Kungen");
        petNameMap.put("13713", "Dagorr");
        petNameMap.put("13714", "Ahman");
        petNameMap.put("13715", "Haken");

        petNameMap.put("138", "Valkyrja");
        petNameMap.put("13811", "Camilla");
        petNameMap.put("13812", "Vanessa");
        petNameMap.put("13813", "Katarina");
        petNameMap.put("13814", "Akroma");
        petNameMap.put("13815", "Trinity");

        petNameMap.put("139", "Pierret");
        petNameMap.put("13911", "Julie");
        petNameMap.put("13912", "Clara");
        petNameMap.put("13913", "Sophia");
        petNameMap.put("13914", "Eva");
        petNameMap.put("13915", "Luna");

        petNameMap.put("140", "Werewolf");
        petNameMap.put("14011", "Vigor");
        petNameMap.put("14012", "Garoche");
        petNameMap.put("14013", "Shakan");
        petNameMap.put("14014", "Eshir");
        petNameMap.put("14015", "Jultan");

        petNameMap.put("141", "Phantom Thief");
        petNameMap.put("14111", "Luer");
        petNameMap.put("14112", "Jean");
        petNameMap.put("14113", "Julien");
        petNameMap.put("14114", "Louis");
        petNameMap.put("14115", "Guillaume");

        petNameMap.put("142", "Angelmon");
        petNameMap.put("14211", "Blue Angelmon");
        petNameMap.put("14212", "Red Angelmon");
        petNameMap.put("14213", "Gold Angelmon");
        petNameMap.put("14214", "White Angelmon");
        petNameMap.put("14215", "Dark Angelmon");

        petNameMap.put("144", "Dragon");
        petNameMap.put("14411", "Verad");
        petNameMap.put("14412", "Zaiross");
        petNameMap.put("14413", "Jamire");
        petNameMap.put("14414", "Zerath");
        petNameMap.put("14415", "Grogen");

        petNameMap.put("145", "Phoenix");
        petNameMap.put("14511", "Sigmarus");
        petNameMap.put("14512", "Perna");
        petNameMap.put("14513", "Teshar");
        petNameMap.put("14514", "Eludia");
        petNameMap.put("14515", "Jaara");

        petNameMap.put("146", "Chimera");
        petNameMap.put("14611", "Taor");
        petNameMap.put("14612", "Rakan");
        petNameMap.put("14613", "Lagmaron");
        petNameMap.put("14614", "Shan");
        petNameMap.put("14615", "Zeratu");

        petNameMap.put("147", "Vampire");
        petNameMap.put("14711", "Liesel");
        petNameMap.put("14712", "Verdehile");
        petNameMap.put("14713", "Argen");
        petNameMap.put("14714", "Julianne");
        petNameMap.put("14715", "Cadiz");

        petNameMap.put("148", "Viking");
        petNameMap.put("14811", "Huga");
        petNameMap.put("14812", "Geoffrey");
        petNameMap.put("14813", "Walter");
        petNameMap.put("14814", "Jansson");
        petNameMap.put("14815", "Janssen");

        petNameMap.put("149", "Amazon");
        petNameMap.put("14911", "Ellin");
        petNameMap.put("14912", "Ceres");
        petNameMap.put("14913", "Hina");
        petNameMap.put("14914", "Lyn");
        petNameMap.put("14915", "Mara");

        petNameMap.put("150", "Martial Cat");
        petNameMap.put("15011", "Mina");
        petNameMap.put("15012", "Mei");
        petNameMap.put("15013", "Naomi");
        petNameMap.put("15014", "Xiao Ling");
        petNameMap.put("15015", "Miho");

        petNameMap.put("152", "Vagabond");
        petNameMap.put("15211", "Allen");
        petNameMap.put("15212", "Kai'en");
        petNameMap.put("15213", "Roid");
        petNameMap.put("15214", "Darion");
        petNameMap.put("15215", "Jubelle");

        petNameMap.put("153", "Epikion Priest");
        petNameMap.put("15311", "Rina");
        petNameMap.put("15312", "Chloe");
        petNameMap.put("15313", "Michelle");
        petNameMap.put("15314", "Iona");
        petNameMap.put("15315", "Rasheed");

        petNameMap.put("154", "Magical Archer");
        petNameMap.put("15411", "Sharron");
        petNameMap.put("15412", "Cassandra");
        petNameMap.put("15413", "Ardella");
        petNameMap.put("15414", "Chris");
        petNameMap.put("15415", "Bethony");

        petNameMap.put("155", "Rakshasa");
        petNameMap.put("15511", "Su");
        petNameMap.put("15512", "Hwa");
        petNameMap.put("15513", "Yen");
        petNameMap.put("15514", "Pang");
        petNameMap.put("15515", "Ran");

        petNameMap.put("156", "Bounty Hunter");
        petNameMap.put("15611", "Wayne");
        petNameMap.put("15612", "Randy");
        petNameMap.put("15613", "Roger");
        petNameMap.put("15614", "Walkers");
        petNameMap.put("15615", "Jamie");

        petNameMap.put("157", "Oracle");
        petNameMap.put("15711", "Praha");
        petNameMap.put("15712", "Juno");
        petNameMap.put("15713", "Seara");
        petNameMap.put("15714", "Laima");
        petNameMap.put("15715", "Giana");

        petNameMap.put("158", "Imp Champion");
        petNameMap.put("15811", "Yaku");
        petNameMap.put("15812", "Fairo");
        petNameMap.put("15813", "Pigma");
        petNameMap.put("15814", "Shaffron");
        petNameMap.put("15815", "Loque");

        petNameMap.put("159", "Mystic Witch");
        petNameMap.put("15911", "Megan");
        petNameMap.put("15912", "Rebecca");
        petNameMap.put("15913", "Silia");
        petNameMap.put("15914", "Linda");
        petNameMap.put("15915", "Gina");

        petNameMap.put("160", "Grim Reaper");
        petNameMap.put("16011", "Hemos");
        petNameMap.put("16012", "Sath");
        petNameMap.put("16013", "Hiva");
        petNameMap.put("16014", "Prom");
        petNameMap.put("16015", "Thrain");

        petNameMap.put("161", "Occult Girl");
        petNameMap.put("16111", "Anavel");
        petNameMap.put("16112", "Rica");
        petNameMap.put("16113", "Charlotte");
        petNameMap.put("16114", "Lora");
        petNameMap.put("16115", "Nicki");

        petNameMap.put("162", "Death Knight");
        petNameMap.put("16211", "Fedora");
        petNameMap.put("16212", "Arnold");
        petNameMap.put("16213", "Briand");
        petNameMap.put("16214", "Conrad");
        petNameMap.put("16215", "Dias");

        petNameMap.put("163", "Lich");
        petNameMap.put("16311", "Rigel");
        petNameMap.put("16312", "Antares");
        petNameMap.put("16313", "Fuco");
        petNameMap.put("16314", "Halphas");
        petNameMap.put("16315", "Grego");

        petNameMap.put("164", "Skull Soldier");
        petNameMap.put("16411", "");
        petNameMap.put("16412", "");
        petNameMap.put("16413", "");
        petNameMap.put("16414", "");
        petNameMap.put("16415", "");

        petNameMap.put("165", "Living Armor");
        petNameMap.put("16511", "Nickel");
        petNameMap.put("16512", "Iron");
        petNameMap.put("16513", "Copper");
        petNameMap.put("16514", "Silver");
        petNameMap.put("16515", "Zinc");

        petNameMap.put("166", "Dragon Knight");
        petNameMap.put("16611", "Chow");
        petNameMap.put("16612", "Laika");
        petNameMap.put("16613", "Leo");
        petNameMap.put("16614", "Jager");
        petNameMap.put("16615", "Ragdoll");

        petNameMap.put("167", "Magical Archer Promo");
        petNameMap.put("16711", "");
        petNameMap.put("16712", "");
        petNameMap.put("16713", "");
        petNameMap.put("16714", "Fami");
        petNameMap.put("16715", "");

        petNameMap.put("168", "Monkey King");
        petNameMap.put("16811", "Shi Hou");
        petNameMap.put("16812", "Mei Hou Wang");
        petNameMap.put("16813", "Xing Zhe");
        petNameMap.put("16814", "Qitian Dasheng");
        petNameMap.put("16815", "Son Zhang Lao");

        petNameMap.put("169", "Samurai");
        petNameMap.put("16911", "Kaz");
        petNameMap.put("16912", "Jun");
        petNameMap.put("16913", "Kaito");
        petNameMap.put("16914", "Tosi");
        petNameMap.put("16915", "Sige");

        petNameMap.put("170", "Archangel");
        petNameMap.put("17011", "Ariel");
        petNameMap.put("17012", "Velajuel");
        petNameMap.put("17013", "Eladriel");
        petNameMap.put("17014", "Artamiel");
        petNameMap.put("17015", "Fermion");

        petNameMap.put("172", "Drunken Master");
        petNameMap.put("17211", "Mao");
        petNameMap.put("17212", "Xiao Chun");
        petNameMap.put("17213", "Huan");
        petNameMap.put("17214", "Tien Qin");
        petNameMap.put("17215", "Wei Shin");

        petNameMap.put("173", "Kung Fu Girl");
        petNameMap.put("17311", "Xiao Lin");
        petNameMap.put("17312", "Hong Hua");
        petNameMap.put("17313", "Ling Ling");
        petNameMap.put("17314", "Liu Mei");
        petNameMap.put("17315", "Fei");

        petNameMap.put("174", "Beast Monk");
        petNameMap.put("17411", "Chandra");
        petNameMap.put("17412", "Kumar");
        petNameMap.put("17413", "Ritesh");
        petNameMap.put("17414", "Shazam");
        petNameMap.put("17415", "Rahul");

        petNameMap.put("175", "Mischievous Bat");
        petNameMap.put("17511", "");
        petNameMap.put("17512", "");
        petNameMap.put("17513", "");
        petNameMap.put("17514", "");
        petNameMap.put("17515", "");

        petNameMap.put("176", "Battle Scorpion");
        petNameMap.put("17611", "");
        petNameMap.put("17612", "");
        petNameMap.put("17613", "");
        petNameMap.put("17614", "");
        petNameMap.put("17615", "");

        petNameMap.put("177", "Minotauros");
        petNameMap.put("17711", "Urtau");
        petNameMap.put("17712", "Burentau");
        petNameMap.put("17713", "Eintau");
        petNameMap.put("17714", "Grotau");
        petNameMap.put("17715", "Kamatau");

        petNameMap.put("178", "Lizardman");
        petNameMap.put("17811", "Kernodon");
        petNameMap.put("17812", "Igmanodon");
        petNameMap.put("17813", "Velfinodon");
        petNameMap.put("17814", "Glinodon");
        petNameMap.put("17815", "Devinodon");

        petNameMap.put("179", "Hell Lady");
        petNameMap.put("17911", "Beth");
        petNameMap.put("17912", "Raki");
        petNameMap.put("17913", "Ethna");
        petNameMap.put("17914", "Asima");
        petNameMap.put("17915", "Craka");

        petNameMap.put("180", "Brownie Magician");
        petNameMap.put("18011", "Orion");
        petNameMap.put("18012", "Draco");
        petNameMap.put("18013", "Aquila");
        petNameMap.put("18014", "Gemini");
        petNameMap.put("18015", "Korona");

        petNameMap.put("181", "Kobold Bomber");
        petNameMap.put("18111", "Malaka");
        petNameMap.put("18112", "Zibrolta");
        petNameMap.put("18113", "Taurus");
        petNameMap.put("18114", "Dover");
        petNameMap.put("18115", "Bering");

        petNameMap.put("182", "King Angelmon");
        petNameMap.put("18211", "Blue King Angelmon");
        petNameMap.put("18212", "Red King Angelmon");
        petNameMap.put("18213", "Gold King Angelmon");
        petNameMap.put("18214", "White King Angelmon");
        petNameMap.put("18215", "Dark King Angelmon");

        petNameMap.put("183", "Sky Dancer");
        petNameMap.put("18311", "Mihyang");
        petNameMap.put("18312", "Hwahee");
        petNameMap.put("18313", "Chasun");
        petNameMap.put("18314", "Yeonhong");
        petNameMap.put("18315", "Wolyung");

        petNameMap.put("184", "Taoist");
        petNameMap.put("18411", "Gildong");
        petNameMap.put("18412", "Gunpyeong");
        petNameMap.put("18413", "Woochi");
        petNameMap.put("18414", "Hwadam");
        petNameMap.put("18415", "Woonhak");

        petNameMap.put("185", "Beast Hunter");
        petNameMap.put("18511", "Gangchun");
        petNameMap.put("18512", "Nangrim");
        petNameMap.put("18513", "Suri");
        petNameMap.put("18514", "Baekdu");
        petNameMap.put("18515", "Hannam");

        petNameMap.put("186", "Pioneer");
        petNameMap.put("18611", "Woosa");
        petNameMap.put("18612", "Chiwu");
        petNameMap.put("18613", "Pungbaek");
        petNameMap.put("18614", "Nigong");
        petNameMap.put("18615", "Woonsa");

        petNameMap.put("187", "Penguin Knight");
        petNameMap.put("18711", "Toma");
        petNameMap.put("18712", "Naki");
        petNameMap.put("18713", "Mav");
        petNameMap.put("18714", "Dona");
        petNameMap.put("18715", "Kuna");

        petNameMap.put("188", "Barbaric King");
        petNameMap.put("18811", "Aegir");
        petNameMap.put("18812", "Surtr");
        petNameMap.put("18813", "Hraesvelg");
        petNameMap.put("18814", "Mimirr");
        petNameMap.put("18815", "Hrungnir");

        petNameMap.put("189", "Polar Queen");
        petNameMap.put("18911", "Alicia");
        petNameMap.put("18912", "Brandia");
        petNameMap.put("18913", "Tiana");
        petNameMap.put("18914", "Elenoa");
        petNameMap.put("18915", "Lydia");

        petNameMap.put("190", "Battle Mammoth");
        petNameMap.put("19011", "Talc");
        petNameMap.put("19012", "Granite");
        petNameMap.put("19013", "Olivine");
        petNameMap.put("19014", "Marble");
        petNameMap.put("19015", "Basalt");

        petNameMap.put("191", "Fairy Queen");
        petNameMap.put("19111", "");
        petNameMap.put("19112", "");
        petNameMap.put("19113", "");
        petNameMap.put("19114", "Fran");
        petNameMap.put("19115", "");

        petNameMap.put("192", "Ifrit");
        petNameMap.put("19211", "Theomars");
        petNameMap.put("19212", "Tesarion");
        petNameMap.put("19213", "Akhamamir");
        petNameMap.put("19214", "Elsharion");
        petNameMap.put("19215", "Veromos");

        petNameMap.put("193", "Cow Girl");
        petNameMap.put("19311", "Sera");
        petNameMap.put("19312", "Anne");
        petNameMap.put("19313", "Hannah");
        petNameMap.put("19314", "");
        petNameMap.put("19315", "Cassie");

        petNameMap.put("194", "Pirate Captain");
        petNameMap.put("19411", "Galleon");
        petNameMap.put("19412", "Carrack");
        petNameMap.put("19413", "Barque");
        petNameMap.put("19414", "Brig");
        petNameMap.put("19415", "Frigate");

        petNameMap.put("195", "Charger Shark");
        petNameMap.put("19511", "Aqcus");
        petNameMap.put("19512", "Ignicus");
        petNameMap.put("19513", "Zephicus");
        petNameMap.put("19514", "Rumicus");
        petNameMap.put("19515", "Calicus");

        petNameMap.put("196", "Mermaid");
        petNameMap.put("19611", "Tetra");
        petNameMap.put("19612", "Platy");
        petNameMap.put("19613", "Cichlid");
        petNameMap.put("19614", "Molly");
        petNameMap.put("19615", "Betta");

        petNameMap.put("197", "Sea Emperor");
        petNameMap.put("19711", "Poseidon");
        petNameMap.put("19712", "Okeanos");
        petNameMap.put("19713", "Triton");
        petNameMap.put("19714", "Pontos");
        petNameMap.put("19715", "Manannan");

        petNameMap.put("198", "Magic Knight");
        petNameMap.put("19811", "Lapis");
        petNameMap.put("19812", "Astar");
        petNameMap.put("19813", "Lupinus");
        petNameMap.put("19814", "Iris");
        petNameMap.put("19815", "Lanett");

        petNameMap.put("199", "Assassin");
        petNameMap.put("19911", "Stella");
        petNameMap.put("19912", "Lexy");
        petNameMap.put("19913", "Tanya");
        petNameMap.put("19914", "Natalie");
        petNameMap.put("19915", "Isabelle");

        petNameMap.put("200", "Neostone Fighter");
        petNameMap.put("20011", "Ryan");
        petNameMap.put("20012", "Trevor");
        petNameMap.put("20013", "Logan");
        petNameMap.put("20014", "Lucas");
        petNameMap.put("20015", "Karl");

        petNameMap.put("201", "Neostone Agent");
        petNameMap.put("20111", "Emma");
        petNameMap.put("20112", "Lisa");
        petNameMap.put("20113", "Olivia");
        petNameMap.put("20114", "Illianna");
        petNameMap.put("20115", "Sylvia");

        petNameMap.put("202", "Martial Artist");
        petNameMap.put("20211", "Luan");
        petNameMap.put("20212", "Sin");
        petNameMap.put("20213", "Lo");
        petNameMap.put("20214", "Hiro");
        petNameMap.put("20215", "Jackie");

        petNameMap.put("203", "Mummy");
        petNameMap.put("20311", "Nubia");
        petNameMap.put("20312", "Sonora");
        petNameMap.put("20313", "Namib");
        petNameMap.put("20314", "Sahara");
        petNameMap.put("20315", "Karakum");

        petNameMap.put("204", "Anubis");
        petNameMap.put("20411", "Avaris");
        petNameMap.put("20412", "Khmun");
        petNameMap.put("20413", "Iunu");
        petNameMap.put("20414", "Amarna");
        petNameMap.put("20415", "Thebae");

        petNameMap.put("205", "Desert Queen");
        petNameMap.put("20511", "Bastet");
        petNameMap.put("20512", "Sekhmet");
        petNameMap.put("20513", "Hathor");
        petNameMap.put("20514", "Isis");
        petNameMap.put("20515", "Nephthys");

        petNameMap.put("206", "Horus");
        petNameMap.put("20611", "Qebehsenuef");
        petNameMap.put("20612", "Duamutef");
        petNameMap.put("20613", "Imesety");
        petNameMap.put("20614", "Wedjat");
        petNameMap.put("20615", "Amduat");

        petNameMap.put("207", "Jack-o'-lantern");
        petNameMap.put("20711", "Chilling");
        petNameMap.put("20712", "Smokey");
        petNameMap.put("20713", "Windy");
        petNameMap.put("20714", "Misty");
        petNameMap.put("20715", "Dusky");

        petNameMap.put("208", "Frankenstein");
        petNameMap.put("20811", "Tractor");
        petNameMap.put("20812", "Bulldozer");
        petNameMap.put("20813", "Crane");
        petNameMap.put("20814", "Driller");
        petNameMap.put("20815", "Crawler");

        petNameMap.put("209", "Elven Ranger");
        petNameMap.put("20911", "Eluin");
        petNameMap.put("20912", "Adrian");
        petNameMap.put("20913", "Erwin");
        petNameMap.put("20914", "Lucien");
        petNameMap.put("20915", "Isillen");

        petNameMap.put("210", "Harg");
        petNameMap.put("21011", "Remy");
        petNameMap.put("21012", "Racuni");
        petNameMap.put("21013", "Raviti");
        petNameMap.put("21014", "Dova");
        petNameMap.put("21015", "Kroa");

        petNameMap.put("211", "Fairy King");
        petNameMap.put("21111", "Psamathe");
        petNameMap.put("21112", "Daphnis");
        petNameMap.put("21113", "Ganymede");
        petNameMap.put("21114", "Oberon");
        petNameMap.put("21115", "Nyx");

        petNameMap.put("212", "Panda Warrior");
        petNameMap.put("21211", "Mo Long");
        petNameMap.put("21212", "Xiong Fei");
        petNameMap.put("21213", "Feng Yan");
        petNameMap.put("21214", "Tian Lang");
        petNameMap.put("21215", "Mi Ying");

        petNameMap.put("213", "Dice Magician");
        petNameMap.put("21311", "Reno");
        petNameMap.put("21312", "Ludo");
        petNameMap.put("21313", "Morris");
        petNameMap.put("21314", "Tablo");
        petNameMap.put("21315", "Monte");

        petNameMap.put("214", "Harp Magician");
        petNameMap.put("21411", "Sonnet");
        petNameMap.put("21412", "Harmonia");
        petNameMap.put("21413", "Triana");
        petNameMap.put("21414", "Celia");
        petNameMap.put("21415", "Vivachel");

        petNameMap.put("215", "Unicorn");
        petNameMap.put("21511", "Amelia");
        petNameMap.put("21512", "Helena");
        petNameMap.put("21513", "Diana");
        petNameMap.put("21514", "Eleanor");
        petNameMap.put("21515", "Alexandra");

        petNameMap.put("15105", "Devilmon");
        petNameMap.put("14314", "Rainbowmon");

        petNameMap.put("1000111", "HomunWater");
        petNameMap.put("1000112", "HomunFire");
        petNameMap.put("1000113", "HomunWind");

        petNameMap.put("1000204", "HomunLD (Light)");
        petNameMap.put("1000205", "HomunLD (Dark)");
        petNameMap.put("1000214", "HomunLight");
        petNameMap.put("1000215", "HomunDark");

        petNameMap.put("218", "Paladin");
        petNameMap.put("21811", "Josephine");
        petNameMap.put("21812", "Ophilia");
        petNameMap.put("21813", "Louise");
        petNameMap.put("21814", "Jeanne");
        petNameMap.put("21815", "Leona");

        petNameMap.put("219", "Chakram Dancer");

        petNameMap.put("220", "Boomerang Warrior");

        petNameMap.put("221", "Dryad");
        petNameMap.put("222", "Druid");
        petNameMap.put("224", "Giant Warrior");
        petNameMap.put("226", "Lightning Emperor");

        petNameElement.put("Water", "11");
        petNameElement.put("Fire", "12");
        petNameElement.put("Wind", "13");
        petNameElement.put("Light", "14");
        petNameElement.put("Dark", "15");

        String[] listE = {"Water", "Fire", "Wind", "Light", "Dark"};
        ArrayList<String> s1 = new ArrayList();
        s1.addAll(petNameMap.keySet());

        for (String p : s1) {
            if (p.length() == 3) {
                String oname = petNameMap.get(p);
                for (int i = 0; i < 5; i++) {
                    petNameMap.put(p + "0" + (i + 1), oname + " (" + listE[i] + ")");
                    //System.out.println(p+"0"+(i+1)+" : "+ oname+" ("+listE[i]+")");
                }
            }
        }
        for (String s2 : petNameMap.keySet()) {
            petNameMapInv.put(petNameMap.get(s2), s2);
        }
    }

    Map<String, String> info = new HashMap();

    Function<RuneSet, Double> damageMulty = x -> (x.f_atk * 3);
    public RuneSet currentEquip = new RuneSet();

    public boolean isBomb = false;
    public boolean defDame = false;
    public boolean isGuildWars = false;
    public float def_leader = 0;
    public float atk_leader = 30;
    public int leader_skill = 30;

    public long hp, spd, acc, res, cr, cd, def, atk;
    public int b_atk, b_def, b_hp, b_spd, b_cd;
    public int r_atk, r_def, r_hp, r_spd, r_cd;

    public int level = 0;
    public double SkillMulty = 3;
    public double skillsUp = 25;
    public double def_buff = 1.7;
    public double atk_buff = 1.5;

    public int statfixRune[] = new int[petLabels.length];

    long getDamage() {
        return atk * cr * cd;
    }

    public void setBaseStats(int hp, int atk, int def, int spd, int crit) {
        b_atk = atk;
        b_def = def;
        b_hp = hp;

        baseStats[4] = crit;
        baseStats[2] = atk;
        baseStats[1] = def;
        baseStats[0] = hp;
        baseStats[7] = spd;
    }

    public void setBaseAtk(int atk) {
        b_atk = atk;
        baseStats[2] = atk;
    }


    RuneSet oldRune;
    RuneSet curRune;

    int[] REMOVE_COST = {1000, 2500, 5000, 10000, 25000, 50000};

    public int calRemoveCost() {
        int totalCost = 0;
        for (int i = 0; i < 6; i++) {
            RuneType r1 = curRune.set[i];
            RuneType r2 = currentEquip.set[i];

            if (r1 == null || r2 == null) {
                continue;
            }

            if (r1.id == r2.id) {
                continue;
            }

            if (r2.grade > 0) {
                totalCost += REMOVE_COST[r2.grade - 1];
            }
            if (r1.monsterId > 0 && r1.monsterId != this.id) {
                totalCost += REMOVE_COST[r1.grade - 1];
            }

        }
        return totalCost;
    }

    public Map<String, String> applyRuneSet(RuneSet set) {
        curRune = set;
        set.runePet = this;
        for (int i = 0; i < petLabels.length; i++) {
            statfixRune[i] = 0;
            int incre = 0;
            try {
                if (i < 3) {
                    incre = baseStats[i] * set.totalStats[i] / 100 + set.totalStats[i + petLabels.length];
                } else {
                    incre = set.totalStats[i];
                }

                if (i == 7) {//swift bonus
                    incre += Math.ceil((double) baseStats[i] * set.totalStats[i + 8] / 100);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //System.out.println("Error : " + set);
                System.exit(0);
            }

            statfixRune[i] = baseStats[i] + incre;

            statfixMap.put(RuneType.slabels[i], statfixRune[i]);

            if (incre == 0) {
                comboMap.put(RuneType.slabels[i], "" + baseStats[i]);
            } else if (baseStats[i] == 0) {
                comboMap.put(RuneType.slabels[i], "" + incre);
            } else {
                comboMap.put(RuneType.slabels[i], "" + baseStats[i] + "+" + incre);
            }
            comboList[i] = "+" + incre;
        }

        spd = statfixMap.getOrDefault("spd", 0);
        acc = statfixMap.getOrDefault("acc", 0);
        res = statfixMap.getOrDefault("res", 0);
        cr = statfixMap.getOrDefault("cr", 0);
        cd = statfixMap.getOrDefault("cd", 0);
        atk = statfixMap.getOrDefault("atk", 0);
        def = statfixMap.getOrDefault("def", 0);
        hp = statfixMap.getOrDefault("hp", 0);

        return comboMap;

    }

    @Override
    public String toString() {
        String s1 = "";
        s1 = "[" + name + " , id_" + id + " ,lv_" + level + " ; " + stars + "* : " + attribute + element + "] " + skillItem;//+statfixMap;
        return s1;
    }

    public PetType(String name, int def, double hp, int spd) {
        this.spd = spd;
        this.name = name;
        this.hp = Math.round(hp * 15);
        this.def = def;
    }

    public PetType(String name, int def, double hp, int spd, int n2) {
        PetType p2 = SwManager.petsBestiary.get(name);
        if (p2 != null) {
            this.name = "G3 " + name;
            this.hp = p2.b_hp + (int) hp;
            this.def = p2.b_def + def;
            this.spd = p2.b_spd + spd;
        }
    }

    PetType(Bestiary.PetInfo p1) {
        //"b_hp","b_def","b_atk","b_cdmg","b_crate","b_acc","b_res","b_spd"};
        b_hp = baseStats[0] = Integer.parseInt(p1.hp);
        b_def = baseStats[1] = Integer.parseInt(p1.def);
        b_atk = baseStats[2] = Integer.parseInt(p1.atk);
        b_cd = baseStats[3] = Integer.parseInt(p1.cd.replace("%", ""));
        baseStats[4] = Integer.parseInt(p1.cr.replace("%", ""));
        baseStats[5] = Integer.parseInt(p1.acc.replace("%", ""));
        baseStats[6] = Integer.parseInt(p1.res.replace("%", ""));
        b_spd = baseStats[7] = Integer.parseInt(p1.spd);
        name = p1.aName;
        level = 40;
        stars = Integer.parseInt(p1.star);
        attribute = p1.element;
        element = ConfigInfo.getElementIndex(attribute);
        oname = name;
        a_name = p1.aName;
        u_name = p1.uName;
        full_name = p1.aName + " - " + p1.uName + " (" + p1.element + ")";
    }

    public void updatePetBaseStat() {
        for (int i = 0; i < petLabels.length; i++) {
            statfixMap.put(RuneType.slabels[i], baseStats[i]);
            baseMap.put(RuneType.slabels[i], baseStats[i]);
        }
        b_atk = baseMap.getOrDefault("atk", 0);
        b_def = baseMap.getOrDefault("def", 0);
        b_hp = baseMap.getOrDefault("hp", 0);
        b_spd = baseMap.getOrDefault("spd", 0);
    }

    public JSONObject jsonData = null;

    PetType(JSONObject p1, boolean optimizerFile, int index, int runeIndex) {
        try {
            jsonData = p1;
            //System.out.println("pet : "+p1);
            //System.out.println(p1.get("b_spd"));
            String[] petJsonLabels = (optimizerFile) ? petLabels : petLabelsNew;

            for (int i = 0; i < petJsonLabels.length; i++) {
                baseStats[i] = 0;
                if (!p1.has(petJsonLabels[i])) {
                    continue;
                }

                String s1 = p1.getString(petJsonLabels[i]);
                if (!s1.contains("-")) {
                    baseStats[i] = p1.getInt(petJsonLabels[i]);
                }
                if (!optimizerFile && i == 0) {
                    // in the full json, hp is divided by 15
                    baseStats[i] *= 15;
                }
            }
            updatePetBaseStat();

            String levelLabel = (optimizerFile) ? "level" : "unit_level";
            level = p1.getInt(levelLabel);
            if (level >= 30) {
                stars = 4;
            }
            if (level >= 35) {
                stars = 5;
            }
            if (level >= 40) {
                stars = 6;
            }

            String starsLabel = (optimizerFile) ? "stars" : "class";
            if (p1.has(starsLabel)) {
                stars = p1.getInt(starsLabel);
            }

            id = (optimizerFile) ? p1.getInt("id") : index;
            if (p1.has("unit_id")) {
                unit_id = p1.getString("unit_id");
            }

            String masterId = (optimizerFile) ? p1.getString("master_id") : p1.getString("unit_master_id");
            /*if (masterId.length() > 3 && masterId.charAt(3) == '0') {
                masterId = masterId.substring(0, 3);
            }*/
            //System.out.println("MasterId : "+masterId+" : "+petNameMap.containsKey(masterId));
            //System.out.println("json : "+p1.toString());
            master_id = masterId;
            name = (optimizerFile) ? p1.getString("name") : petNameMap.get(masterId);
            if (petNameMap.containsKey(masterId)) {
                name = petNameMap.get(masterId);
            }

            attribute = "Water";
            if (b_hp == 10380 && b_def == 571 && b_atk == 878 && level == 40 && b_spd == 101) {
                name = "Homunculus";
                attribute = "Water";
            }
            if (b_hp == 10215 && b_def == 637 && b_atk == 823 && level == 40 && b_spd == 101) {
                name = "Homunculus";
                attribute = "Fire";
            }
            if (b_hp == 9555 && b_def == 659 && b_atk == 845 && level == 40 && b_spd == 101) {
                name = "Homunculus";
                attribute = "Wind";
            }

            if (p1.has("attribute") && optimizerFile) {
                attribute = p1.getString("attribute");
            } else if (!optimizerFile) {
                attribute = ConfigInfo.petElement[p1.getInt("attribute") - 1];
            }
            element = (optimizerFile) ? ConfigInfo.getElementIndex(attribute) : p1.getInt("attribute") - 1;

            if (!optimizerFile && masterId.length() == 3) {
                name = name + " (" + attribute + ")";
            }

            if (level == 40) {
                if (name.contains("Unknown")) {
                    for (Bestiary.PetInfo p : Bestiary.getInstance().allPets.values()) {
                        if (p.element.equalsIgnoreCase(attribute)) {
                            if (b_hp == Integer.parseInt(p.hp) && b_def == Integer.parseInt(p.def) && b_atk == Integer.parseInt(p.atk)
                                    && b_spd == Integer.parseInt(p.spd)) {
                                //System.out.println("Found unknown : " + p.aName + " ; " + p.uName + " ; " + p.element);
                                name = p.aName;
                            }
                        }
                    }
                }
            }
            if (name == null) {
                //System.out.println("Failed to load this pet : " + master_id);
                return;
            }
            name = name.replace("*", "").trim();
            name = name.replace("(In Storage)", "").trim();
            oname = name;

            if (name.contains("(")) {
                String[] splits = name.split(" ");
                if (splits.length > 1 && name.length() > 10) {
                    String sum = splits[0];
                    for (int i = 1; i < splits.length; i++) {
                        if (!splits[i].contains("(") && splits[i].length() > 1) {
                            sum = sum + "." + splits[i].substring(0, 1);
                        } else {
                            sum = sum + " " + splits[i];
                        }
                    }
                    name = sum;
                }

                name = name.replace(" (Wind)", ".Wi");
                name = name.replace(" (Fire)", ".F");
                name = name.replace(" (Water)", ".Wa");
                name = name.replace(" (Light)", ".L");
                name = name.replace(" (Dark)", ".D");

            }
            if (name.contains("Unknown")) {
                name = "Unknown";
                oname = name;
            }

            info.put("name", name);

            if (p1.has("runes")) {
                JSONArray runes = p1.getJSONArray("runes");
                for (int i = 0; i < runes.length(); i++) {
                    runesEquip++;
                    RuneType r1 = new RuneType(runes.getJSONObject(i), optimizerFile, runeIndex++);
                    r1.monster = name;
                    r1.monsterId = id;
                    equipRuneList.add(r1);
                }
            }

            //System.out.println("statfixMap : "+statfixMap+" ; "+info);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //System.out.println("Pet error : " + p1);
            e.printStackTrace();
        }
    }

    public String showPetRune() {
        ConfigInfo cf = ConfigInfo.getInstance();
        /*this.atk_buff = 1.0;
        this.atk_leader=0;
        this.def_leader=0; */

        String s = "";
        s += "\n" + ("Combo : " + this.comboMap);
        s += "\n" + ("Final : " + this.statfixMap + "  ; Total Remove Cost : " + calRemoveCost());
        s += "\n" + ("Final damage guildwar: " + curRune.finalDamage() + "  (leader " + Math.max(atk_leader, def_leader) + "% ) ; normal = "
                + curRune.finalDamage(0, 0, 0) + " \n; skill : " + this.skillItem);
        /*s += "\n" + ("Damage vs 1000 def: " + curRune.finalDamage() / 4
                + " vs defbreak (-70%def) : " + curRune.finalDamage() * 1000 / (1300)
                + " \nvs hellhound Faimon w1 (400 def) : " + curRune.dmgVsDefense(400)
                + " \nvs raoq Faimon w1 (491 def) : " + curRune.dmgVsDefense(491)
                + " \nvs chikura hell w1 (579 def): " + curRune.dmgVsDefense(579)
                + " \nvs chikura hell w1 (837 def): " + curRune.dmgVsDefense(837)
                + " \nvs chikura hell w3 (1386 def): " + curRune.dmgVsDefense(1386)
                + " \nvs ferun hard w1 (544 def): " + curRune.dmgVsDefense(544)
                + " \nvs ferun hard w1 (484 def): " + curRune.dmgVsDefense(484)
                + " \nvs Dragon B10 (2502 def,11333*15 hp): " + curRune.dmgVsBoss(2502,11333*15)
                + " \nvs Giant B10 (1796 def,11338*15 hp): " + curRune.dmgVsBoss(1796,11338*15)
                + " \nvs Necro B10 (1098 def,9995*15 hp): " + curRune.dmgVsBoss(1098,9995*15)
                );*/
        s += "\n" + ("*****************Done****************************");

        //wind vs fire ,95% dmg. disadvantage.
        //System.out.println("Atk glory : " + cf.glory_atk + " ; cd=" + cf.glory_cd + " ; atk_element=" + cf.gloryAtkElement[this.element]);
        //System.out.println("Buff : def_buff : " + this.def_buff + " ; atk_buff : " + atk_buff + " skill_up=" + (int) this.skillsUp);
        //System.out.println("Dmg vs faimon Hell : ");
        //System.out.println(s);
        return s;
    }

    public static void checkPet(String petName) {
        //System.out.println("Found pet in petNameMap : " + petName + " : " + petNameMap.values().contains(petName));
        //System.out.println("petsBestiary : " + petName + " : " + petsBestiary.containsKey(petName));

        //System.out.println("Found id : " + petNameMapInv.get(petName));

        if (petsBestiary.containsKey(petName)) {
            //System.out.println("Family : [" + petsBestiary.get(petName).u_name + "]");
            //System.out.println("Found id : " + petNameMapInv.get(petsBestiary.get(petName).u_name));
        }
    }

    public static void main(String[] args) {
        SwManager.getInstance().loadPets("optimizer.json");
        //SwManager.searchPets("Zaiross");
        //SwManager.searchPets("Akhamamir");
        //SwManager.searchPets("Ran");
        //SwManager.searchPets("Spectra");
        //SwManager.searchPets("Spectra");
        //SwManager.searchPets("Sigmarus");
        //SwManager.searchPets("Hwa");
        //SwManager.searchPets("Ran");
        checkPet("Zaiross");
        checkPet("Martina");
    }
};
