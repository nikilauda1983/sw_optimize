package com.swrunes.swrunes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.swrunes.swrunes.SwManager.getPet;
import static com.swrunes.swrunes.SwManager.searchPets;

/**
 * @author ducnd2 on 10/3/2016.
 */
public class ManualWork {

    public void BulldozerWork() {
        getPet("Bulldozer").defDame = true;
        getPet("Bulldozer").def_leader = 30;
        PetType lushen = searchPets("Bulldozer", x -> (x.f_def * 1.6 * 2), 25);
        RuneType.RuneSet.exceptPetRunes = "Lushen,Theomars,Tyron,Copper,Eshir,Tanya,Kahli,Katarina";
        RuneType.RuneSet.runePet = lushen;

        RunePermutation.perMute("Rage,Will", x -> (x.cr >= 50),
                // RunePermutation.perMute("Guard,Blade,Will", x -> (x.cr>=69 && x.def>170 && x.will()),
                // RunePermutation.perMute("Rage,Blade", x -> (x.cr>=50 && x.def>140),
                // RunePermutation.perMute("Guard,Blade", x -> (x.cr>=(80) && x.def>180 && x.cd>=90),
                x -> {
                    if (x.cr > 6 || x.cd > 6 || x.def > 6) {
                        return Arrays.asList(x.cd, x.cr, x.def);
                    }
                    return null;
                }, x -> (x.finalDamage()));

        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();
    }

    public void copperWork() {
        getPet("Copper2").defDame = true;
        PetType lushen = searchPets("Copper2", x -> (x.f_atk * 3 + x.f_def * 3), 30);

        if (true) return;
        // for lushen with max cd
        // RunePermutation.perMute("Rage,Blade", x -> (x.cr>=80 && x.atk>100),
        //RuneSet.exceptPetRunes = "Copper,Basalt,Xiong Fei,Talc";
        // RuneSet.exceptPetIds.add(lushen.id);

        RuneType.RuneSet.runePet = lushen;

        //System.out.println("Current equip damage : " + lushen.currentEquip.finalDamage());

        // if (true) return;
        RunePermutation.genSlot246("DEF%", "CDmg", "DEF%");
        RunePermutation.perMute("Guard,Blade,Will", x -> (x.cr >= 55 && x.will()),
                //RunePermutation.perMute("Rage,Guard", x -> (x.cr >= 55),
                //RunePermutation.perMute("Guard,Blade,Will", x -> (x.cr>=65 && x.def>170 && x.will()),
                // RunePermutation.perMute("Rage,Blade", x -> (x.cr>=50 && x.def>140),
                // RunePermutation.perMute("Guard,Blade", x -> (x.cr>=(80) && x.def>180 && x.cd>=90),
                x -> {
                    if (x.cr > 6 || x.cd > 6 || x.def > 6) {
                        return Arrays.asList(x.cd, x.cr, x.def);
                    }
                    return null;
                }, x -> (x.finalDamage()));

        // RunePermutation.perMute("Rage", runes, x -> (x.cr>=(63-15) && x.atk>100),x -> (x.cr>10 || x.cd>10 || x.atk >10),x -> (x.cd));
        //RunePermutation.bestRuneSet.equipOnPet(lushen);
        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();
    }

    public void copperWork2() {
        getPet("Copper").defDame = true;
        PetType lushen = searchPets("Copper", x -> (x.f_atk * 3 + x.f_def * 3), 30);
        lushen.def_leader = 0;
        lushen.atk_leader = 0;
        // with delphoy leader, buf def, gw
        // for lushen with max cd
        // RunePermutation.perMute("Rage,Blade", x -> (x.cr>=80 && x.atk>100),
        RuneType.RuneSet.exceptPetRunes = "Copper,Basalt,Xiong Fei,Talc";
        // RuneSet.exceptPetIds.add(lushen.id);

        RuneType.RuneSet.runePet = lushen;

        //System.out.println("Current equip damage : " + lushen.currentEquip.finalDamage());

        // if (true) return;
        RunePermutation.genSlot246("DEF%", "DEF%", "DEF%");
        RunePermutation.perMute("Vampire,Will", x -> (x.cr >= 55),
                //RunePermutation.perMute("Guard,Blade,Will", x -> (x.cr>=65 && x.will()),
                //RunePermutation.perMute("Rage,Guard", x -> (x.cr >= 55),
                //RunePermutation.perMute("Guard,Blade,Will", x -> (x.cr>=65 && x.def>170 && x.will()),
                // RunePermutation.perMute("Rage,Blade", x -> (x.cr>=50 && x.def>140),
                // RunePermutation.perMute("Guard,Blade", x -> (x.cr>=(80) && x.def>180 && x.cd>=90),
                x -> {
                    if (x.cr > 6 || x.cd > 6 || x.def > 6) {
                        return Arrays.asList(x.cd, x.cr, x.def);
                    }
                    return null;
                }, x -> (x.def));

        // RunePermutation.perMute("Rage", runes, x -> (x.cr>=(63-15) && x.atk>100),x -> (x.cr>10 || x.cd>10 || x.atk >10),x -> (x.cd));
        //RunePermutation.bestRuneSet.equipOnPet(lushen);
        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();
    }

    int[] attackSet = {3, 4, 3};

    public void kahliWork() {
        getPet("Kahli").atk_leader = 0;
        getPet("Kahli").def_leader = 0;
        PetType lushen = searchPets("Kahli");

        lushen.SkillMulty = 3;
        lushen.skillsUp = 25;
        RuneType.RuneSet.runePet = lushen;
        RuneType.RuneSet.exceptPetRunes = "Lushen,Copper,Katarina,Theomars,Tanya,Raki,Akhamamir,Tyron,Lushen1";
        RunePermutation.slot246 = attackSet;

        RunePermutation.perMute("Rage", x -> (x.pet_cr >= (95 - 23) && x.atk > 100 && x.spd >= 19), //
                x -> {
                    if (x.cr >= 8 || x.cd >= 8 || x.atk >= 10) {
                        return Arrays.asList(x.cd, x.cr, x.atk, x.spd);
                    }
                    return null;
                }, x -> (x.finalDamage()));

        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();
    }

    public void shumarWork() {
        PetType lushen = searchPets("Shumar");
        lushen.SkillMulty = ((200 + 30 + 24 + 15 + 140) * 2) / 100;
        lushen.skillsUp = 30;

        RuneType.RuneSet.runePet = lushen;
        RuneType.RuneSet.exceptPetRunes = "Lushen,Eshir,Copper,Kahli,Katarina,Sigmarus,Theomars,Bulldozer,Verdehile,Chloe,"
                + "Ramagos,Tyron,Tanya,Akhamamir,Raki,Hwa,Chasun,Spectra,Galleon,Bernard";

        RunePermutation.perMute("Swift,Blade", x -> (x.cr >= (55) && x.spd >= 70 && x.spd < 75), //
                x -> {
                    if (x.cr >= 8 || x.cd >= 8 || x.atk >= 8 || x.spd >= 8) {
                        return Arrays.asList(x.cd, x.cr, x.atk, x.spd);
                    }
                    return null;
                }, x -> (x.finalDamage()));

        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();
    }

    public void taruWork() {
        getPet("Taru").atk_leader = 0;
        getPet("Taru").def_leader = 0;
        PetType lushen = searchPets("Taru");
        if (true) return;

        lushen.SkillMulty = 3.7;
        lushen.skillsUp = 30;
        RunePermutation.slot246 = attackSet;
        RuneType.RuneSet.runePet = lushen;
        RuneType.RuneSet.exceptPetRunes = "Lushen,Copper,Kahli,Katarina,Lushen1,Tyron,Eshir,Randy";

        RunePermutation.perMute("Rage", x -> (x.cr >= (30) && x.atk > 100 && x.spd <= 5), //
                x -> {
                    if (x.cr >= 8 || x.cd >= 8 || x.atk >= 10) {
                        return Arrays.asList(x.cd, x.cr, x.atk);
                    }
                    return null;
                }, x -> (x.finalDamage()));

        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();
    }

    public void shrenWork() {
        PetType lushen = searchPets("Taru");
        lushen.SkillMulty = 3.0;
        lushen.skillsUp = 25;
        lushen.setBaseStats(10380, 725, 395, 104, 15);

        RuneType.RuneSet.runePet = lushen;
        RuneType.RuneSet.exceptPetRunes = "Lushen,Eshir,Copper,Kahli,Katarina";

        RunePermutation.perMute("Rage", x -> (x.cr >= (55) && x.atk > 100), //
                x -> {
                    if (x.cr >= 8 || x.cd >= 8 || x.atk >= 10) {
                        return Arrays.asList(x.cd, x.cr, x.atk);
                    }
                    return null;
                }, x -> (x.finalDamage()));

        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();
    }

    public void droganWork() {
        getPet("Basalt").setBaseStats(8400, 878, 373, 95, 15);
        PetType lushen = searchPets("Basalt", x -> (x.f_def * 1.7 * 3), 20);
        RuneType.RuneSet.runePet = lushen;

        RuneType.RuneSet.exceptPetRunes = "Copper,Zaiross,Tyron,Triton,Chloe,Bulldozer,Tanya,Lushen,Eshir,Kahli,Katarina,Xiong Fei,Rina,Talc,Fedora,Delphoi,Galleon";

        RunePermutation.perMute("Despair,Will", x -> (x.acc >= 60 && x.will()), //
                // RunePermutation.perMute("Guard,Energy,Will", x -> (x.acc>=60 && x.will() ), //
                x -> {
                    if (x.hp >= 8 || x.def >= 10 || x.acc >= 8) {
                        return Arrays.asList(x.hp, x.acc, x.def);
                    }
                    return null;
                }, x -> (x.effectiveHP()));

        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();
    }

    public void zincWork() {
        getPet("zinc").def_leader = 30;
        PetType lushen = searchPets("Zinc", x -> (x.f_def * 1.7 * 3), 20);
        RuneType.RuneSet.runePet = lushen;

        RuneType.RuneSet.exceptPetRunes = "Copper,Zaiross,Tyron,Triton,Chloe,Bulldozer,Tanya,Lushen,Eshir,Kahli,Katarina,Xiong Fei";
        //RuneSet.exceptPetRunes = "Copper,Lushen,Kahli,Tyron";

        RunePermutation.perMute("Despair,will", x -> (x.cr >= (50) && x.acc >= 60), //
                //RunePermutation.perMute("Rage,Will", x -> (x.cr >= (50) && x.will() && x.def >= 150), //
                x -> {
                    if (x.cr >= 6 || x.cd >= 6 || x.def >= 10 || x.acc >= 60) {
                        return Arrays.asList(x.cd, x.cr, x.def);
                    }
                    return null;
                }, x -> (x.finalDamage()));

        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();
    }

    public void tanyaWork() {
        PetType lushen = searchPets("Tanya", x -> (x.f_atk * 7.7), 30);
        RuneType.RuneSet.runePet = lushen;
        RuneType.RuneSet.exceptPetRunes = "Lushen,Eshir,Copper,Kahli,Akhamamir,Bulldozer,Katarina,Theomars,Tyron";

        RunePermutation.perMute("Fatal,Will", x -> (x.cr >= (45) && x.acc >= 15), //
                x -> {
                    if (x.cr >= 6 || x.cd >= 6 || x.atk >= 10 || x.acc >= 8) {
                        return Arrays.asList(x.cd, x.cr, x.atk, x.acc);
                    }
                    return null;
                }, x -> (x.finalDamage()));

        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();
    }

    public void katarinaWork() {
        PetType lushen = searchPets("Katarina", x -> (x.f_atk * 2.8), 20);

        RuneType.RuneSet.runePet = lushen;
        RuneType.RuneSet.exceptPetRunes = "Lushen,Copper,Kahli,Tyron,Eshir,Julie,Akhamamir";

        RunePermutation.perMute("Rage,Will", x -> (x.cr >= (55) && x.atk > 100), //
                x -> {
                    if (x.cr >= 6 || x.cd >= 6 || x.atk >= 10) {
                        return Arrays.asList(x.cd, x.cr, x.atk);
                    }
                    return null;
                }, x -> (x.finalDamage()));

        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();
    }

    public void sieqWork() {
        PetType lushen = searchPets("Sieq");
        lushen.SkillMulty = (float) ((300.0) / 100);
        RuneType.RuneSet.runePet = lushen;
        RuneType.RuneSet.exceptPetRunes = "Lushen,Copper,Kahli,Julie,Tyron,Katarina,Eshir,Verdehile,Bulldozer";
        // RuneSet.exceptPetRunes = "Lushen";

        RunePermutation.perMute("Rage", x -> (x.cr >= (45) && x.atk > 100 && x.spd >= 25), //
                x -> {
                    if (x.cr >= 6 || x.cd >= 6 || x.atk >= 10) {
                        return Arrays.asList(x.cd, x.cr, x.atk);
                    }
                    return null;
                }, x -> (x.cd * x.atk));

        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();
    }

    public void amirWork() {
        PetType lushen = searchPets("Akhamamir");
        lushen.SkillMulty = (float) ((1.98) / 3);
        lushen.skillsUp = 30;
        // Chasun
        // for lushen with max cd
        // RunePermutation.perMute("Rage,Blade", x -> (x.cr>=80 && x.atk>100),

        RuneType.RuneSet.exceptPetRunes = "Copper,Tyron,Eshir,Lushen,Hwa,Bulldozer";
        RuneType.RuneSet.runePet = lushen;

        //System.out.println("Current equip damage : " + lushen.currentEquip.finalDamage());

        // if (true) return;
        RunePermutation.perMute("Fatal,Blade", x -> (x.cr >= (65) && x.spd > 90), x -> {
            if (x.cr > 10 || x.cd > 10 || x.atk > 10 || x.spd > 10) {
                return Arrays.asList(x.cd, x.cr, x.atk, x.spd);
            }
            return null;
        }, x -> (x.cd * x.atk));

        // RunePermutation.perMute("Rage", runes, x -> (x.cr>=(63-15) && x.atk>100),x -> (x.cr>10 || x.cd>10 || x.atk >10),x -> (x.cd));
        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();
    }

    public void evaWork() {
        PetType lushen = searchPets("Julie");
        lushen.SkillMulty = (float) ((1.98) / 3);
        lushen.skillsUp = 30;
        // Chasun
        // for lushen with max cd
        // RunePermutation.perMute("Rage,Blade", x -> (x.cr>=80 && x.atk>100),

        RuneType.RuneSet.exceptPetRunes = "Copper,Tyron,Eshir,Lushen,Hwa,Theomars,Raki,Kahli";
        RuneType.RuneSet.runePet = lushen;

        //System.out.println("Current equip damage : " + lushen.currentEquip.finalDamage());

        // if (true) return;
        RunePermutation.perMute("Rage,Will", x -> (x.cd >= (65)), x -> {
            if (x.cd > 5 || x.atk > 5) {
                return Arrays.asList(x.cd, x.atk);
            }
            return null;
        }, x -> (x.cd * x.atk));

        // RunePermutation.perMute("Rage", runes, x -> (x.cr>=(63-15) && x.atk>100),x -> (x.cr>10 || x.cd>10 || x.atk >10),x -> (x.cd));
        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();
    }

    public void julieWork() {
        PetType lushen = searchPets("Julie");
        lushen.SkillMulty = (float) ((1.98) / 3);
        lushen.skillsUp = 30;
        // Chasun
        // for lushen with max cd
        // RunePermutation.perMute("Rage,Blade", x -> (x.cr>=80 && x.atk>100),

        RuneType.RuneSet.exceptPetRunes = "Copper,Tyron,Eshir,Lushen,Hwa,Bulldozer";
        RuneType.RuneSet.runePet = lushen;

        //System.out.println("Current equip damage : " + lushen.currentEquip.finalDamage());

        // if (true) return;
        RunePermutation.perMute("Swift", x -> (x.cr >= (65) && x.spd > 90), x -> {
            if (x.cr > 10 || x.cd > 10 || x.atk > 10 || x.spd > 10) {
                return Arrays.asList(x.cd, x.cr, x.atk, x.spd);
            }
            return null;
        }, x -> (x.cd * x.atk));

        // RunePermutation.perMute("Rage", runes, x -> (x.cr>=(63-15) && x.atk>100),x -> (x.cr>10 || x.cd>10 || x.atk >10),x -> (x.cd));
        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();
    }

    public void lushenWorkFastRage() {
        PetType lushen = searchPets("Lushen", x -> (x.f_atk * 0.68), 30);
        // Chasun
        // for lushen with max cd
        // RunePermutation.perMute("Rage,Blade", x -> (x.cr>=80 && x.atk>100),

        RuneType.RuneSet.exceptPetRunes = "Copper,Zaiross,Hwa,Kahli,Akhamamir,Theomars,Eshir";
        RuneType.RuneSet.runePet = lushen;

        //System.out.println("Current equip damage : " + lushen.currentEquip.finalDamage());

        // if (true) return;
        RunePermutation.perMute("Rage", x -> (x.cr >= (70) && x.cd > 120 && x.spd >= 60), x -> {
            if (x.cr >= 10 || x.cd >= 10 || x.atk >= 10 || x.spd >= 10) {
                return Arrays.asList(x.cd, x.cr, x.atk, x.spd);
            }
            return null;
        }, x -> (x.finalDamage()));

        // RunePermutation.perMute("Rage", runes, x -> (x.cr>=(63-15) && x.atk>100),x -> (x.cr>10 || x.cd>10 || x.atk >10),x -> (x.cd));
        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();
    }

    public void lushenWorkFast() {
        PetType lushen = searchPets("Lushen1", x -> (x.f_atk * 0.68), 30);
        // Chasun
        // for lushen with max cd
        // RunePermutation.perMute("Rage,Blade", x -> (x.cr>=80 && x.atk>100),
        // RunePermutation.excludeRunes(Arrays.asList(670,669));
        RuneType.RuneSet.exceptPetRunes = "Tyron";
        RuneType.RuneSet.runePet = lushen;

        //System.out.println("Current equip damage : " + lushen.currentEquip.finalDamage());

        // if (true) return;
        // RunePermutation.perMute("Swift", x -> (x.cr>=(55) && x.cd>120 && x.spd>=100),
        RunePermutation.perMute("Swift,Blade", x -> (x.cr >= (70) && x.cd > 120 && x.spd >= 60), x -> {
            if (x.cr >= 10 || x.cd >= 10 || x.atk >= 10 || x.spd >= 5) {
                return Arrays.asList(x.cd, x.cr, x.atk, x.spd);
            }
            return null;
        }, x -> (x.finalDamage()));

        // RunePermutation.perMute("Rage", runes, x -> (x.cr>=(63-15) && x.atk>100),x -> (x.cr>10 || x.cd>10 || x.atk >10),x -> (x.cd));
        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();
    }

    public void lushenWorkWill() {
        PetType lushen = searchPets("Lushen1", x -> (x.f_atk * 0.68), 30);
        RuneType.RuneSet.exceptPetRunes = "Tyron,Lushen,Copper,Eshir";
        RuneType.RuneSet.runePet = lushen;

        //System.out.println("Current equip damage : " + lushen.currentEquip.finalDamage());

        // if (true) return;
        // RunePermutation.perMute("Swift", x -> (x.cr>=(55) && x.cd>120 && x.spd>=100),
        RunePermutation.perMute("Rage/Fatal,Will", x -> (x.cr >= (50)), x -> {
            if (x.cr >= 10 || x.cd >= 10 || x.atk >= 10) {
                return Arrays.asList(x.cd, x.cr, x.atk);
            }
            return null;
        }, x -> (x.finalDamage()));
        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();

    }

    public void lushenWork() {
        PetType lushen = searchPets("Lushen", x -> (x.f_atk * 0.68), 30);
        // lushen.SkillMulty = (float) ((1.98)/3);
        // Chasun
        // for lushen with max cd
        // RunePermutation.perMute("Rage,Blade", x -> (x.cr>=80 && x.atk>100),

        // RuneSet.exceptPetRunes = "Tyron,Akhamamir,Eshir,Copper";
        RuneType.RuneSet.exceptPetRunes = "Copper,Eshir";
        RuneType.RuneSet.runePet = lushen;

        // if (true) return;
        RunePermutation.perMute("Rage,Blade", x -> (x.cr >= 75 && x.finalDamage() >= 9200),
                // RunePermutation.perMute("Rage,Blade", x -> (x.cr>=(100-30-15) && x.atk>100),
                // RunePermutation.perMute("Fatal,Blade", x -> (x.cr>=50 && x.atk>100),
                // x -> {if (x.cr>10 || x.cd>10 || x.atk >10) return Arrays.asList(x.cd,x.cr,x.atk);return null;},x ->
                // ((x.cd+50+25)*(x.atk+15+18+18)));
                x -> {
                    if (x.cr > 5 || x.cd > 5 || x.atk > 5 || x.spd > 5) {
                        return Arrays.asList(x.cd, x.cr, x.atk, x.spd);
                    }
                    return null;
                }, x -> (x.cr));

        // RunePermutation.perMute("Rage", runes, x -> (x.cr>=(63-15) && x.atk>100),x -> (x.cr>10 || x.cd>10 || x.atk >10),x -> (x.cd));
        lushen.applyRuneSet(RunePermutation.bestRuneSet);
        lushen.showPetRune();
    }

    public void delphoyWork() {
        PetType bernard = searchPets("Delphoi");
        // Chasun
        RunePermutation.excludeRunes(Arrays.asList(298, 451, 806));
        RuneType.RuneSet.exceptPetRunes = "Copper,Galleon,Fedora,Megan,Chasun,Talc,Xiong Fei";

        RunePermutation.perMute("Will,Shield,Guard,Focus,Energy", x -> (x.spd > 10 && x.acc >= 75 && x.shield()), x -> {
            if (x.acc > 8 || x.hp > 8 || x.def > 8) {
                return Arrays.asList(x.acc, x.hp, x.def);
            }
            return null;
        }, x -> (x.hp * x.def));

        bernard.applyRuneSet(RunePermutation.bestRuneSet);

        //System.out.println("Combo : " + bernard.comboMap);
        //System.out.println("Final : " + bernard.statfixMap);
    }

    public void galleonWork() {
        PetType bernard = searchPets("Galleon");
        // Chasun
        // RunePermutation.excludeRunes(Arrays.asList(793));

        RuneType.RuneSet.exceptPetRunes = "Chloe,Bernard,Theomars,Delphoi,Spectra,Megan,Lisa,Chasun,Triton";

        RunePermutation.perMute("Violent,Will", x -> (x.acc >= 60 && x.spd >= 95 && x.will()), x -> {
            if (x.acc > 8 || x.spd > 8) {
                return Arrays.asList(x.spd, x.acc, x.hp);
            }
            return null;
        }, x -> (x.hp * x.def));
        bernard.applyRuneSet(RunePermutation.bestRuneSet);

        //System.out.println("Combo : " + bernard.comboMap);
        //System.out.println("Final : " + bernard.statfixMap);
    }

    public void meganWork() {
        PetType bernard = searchPets("Megan");
        // Chasun
        RunePermutation.excludeRunes(Arrays.asList(793));

        RuneType.RuneSet.exceptPetRunes = "Chloe,Bernard,Theomars,Delphoi,Galleon";

        RunePermutation.perMute("Swift,Will", x -> (x.will()), x -> {
            if (x.spd < 5) {
                return null;
            }
            return Arrays.asList(x.spd);
        }, x -> (x.spd));
        bernard.applyRuneSet(RunePermutation.bestRuneSet);

        //System.out.println("Combo : " + bernard.comboMap);
        //System.out.println("Final : " + bernard.statfixMap);
    }

    public void bernardWork() {
        PetType bernard = searchPets("Bernard");
        // Chasun
        RunePermutation.perMute("Swift", null, x -> {
            if (x.spd < 10) {
                return null;
            }
            return Arrays.asList(x.spd);
        }, x -> (x.spd));
        bernard.applyRuneSet(RunePermutation.bestRuneSet);

        System.out.println("Combo : " + bernard.comboMap);
        System.out.println("Final : " + bernard.statfixMap);
    }

    public void stellaWork() {
        PetType bernard = searchPets("Theomars");
        // Chasun
        RuneType.RuneSet.exceptPetRunes = "Bernard,Chloe";
        RunePermutation.perMute("Swift,Blade", x -> (x.spd > 120 && x.cr > 40 && x.atk > 40), x -> {
            if (x.cr > 8 || x.spd > 8) {
                return Arrays.asList(x.spd, x.cr, x.atk);
            }
            return null;
        }, x -> (x.spd * x.atk * x.cr));
        bernard.applyRuneSet(RunePermutation.bestRuneSet);

        bernard.showPetRune();
    }

    public void ramagodWork() {
        PetType chasun = searchPets("Ramagos");
        // Chasun
        // RunePermutation.excludeRunes(Arrays.asList(611));
        RuneType.RuneSet.exceptPetRunes = "Hwa,Lisa,Belladeon,Talc,Fedora";

        RunePermutation.perMute("Energy,Will", x -> (x.hp > 110 && x.will()),
                //RunePermutation.perMute("Vampire,Will", x -> (x.hp > 110 && x.will() && x.def > 20),
                // RunePermutation.perMute("Violent,Will", x -> (x.haveWill() && x.spd>20 && x.hp>180 && x.res>20),
                x -> {
                    if (x.def > 10 || x.hp > 10) {
                        return Arrays.asList(x.hp, x.res, x.def);
                    }
                    return null;
                }, x -> (x.pet_hp));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        chasun.showPetRune();
    }

    public void talcWork() {
        PetType chasun = searchPets("Talc");
        // Chasun
        RunePermutation.excludeRunes(Arrays.asList(608));
        RuneType.RuneSet.exceptPetRunes = "Hwa,Lisa,Belladeon,Copper,Chasun";

        RunePermutation
                .perMute("Will,Endure,Guard,Shield,Energy", x -> (x.hp > 190 && x.def > 120 && x.acc >= 60 && x.will() && x.shield()),
                        // RunePermutation.perMute("Violent,Will", x -> (x.haveWill() && x.spd>20 && x.hp>180 && x.res>20),
                        x -> {
                            if (x.def > 6 || x.hp > 6 || x.res > 6 || x.acc > 6) {
                                return Arrays.asList(x.hp, x.def);
                            }
                            return null;
                        }, x -> (x.hp));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        chasun.showPetRune();
    }

    public void rinaWork() {
        PetType chasun = searchPets("Rina");
        // Chasun
        RunePermutation.excludeRunes(Arrays.asList(611));
        RuneType.RuneSet.exceptPetRunes = "Hwa,Lisa,Belladeon,Talc,Fedora";

        RunePermutation.perMute("Energy,Endure,Guard,Shield", x -> (x.hp > 110 && x.res > 50 && x.def > 70),
                // RunePermutation.perMute("Violent,Will", x -> (x.haveWill() && x.spd>20 && x.hp>180 && x.res>20),
                x -> {
                    if (x.def > 10 || x.hp > 10) {
                        return Arrays.asList(x.hp, x.res);
                    }
                    return null;
                }, x -> (x.hp));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        chasun.showPetRune();
    }

    public void darionWork() {
        PetType chasun = searchPets("Darion");
        // Chasun
        RunePermutation.excludeRunes(Arrays.asList(754));
        RuneType.RuneSet.exceptPetRunes = "Hwa,Lisa,Belladeon";

        RunePermutation.perMute("Nemesis,Guard,Energy,Endure", x -> (x.acc >= 50 && x.hp > 110 && x.res > 50),
                // RunePermutation.perMute("Violent,Will", x -> (x.haveWill() && x.spd>20 && x.hp>180 && x.res>20),
                x -> {
                    if (x.def > 10 || x.hp > 10) {
                        return Arrays.asList(x.hp, x.def, x.res);
                    }
                    return null;
                }, x -> (x.def));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        System.out.println("Combo : " + chasun.comboMap);
        System.out.println("Final : " + chasun.statfixMap);
    }

    public void chasunWork() {
        PetType chasun = searchPets("Chasun");
        // Chasun
        // RunePermutation.excludeRunes(Arrays.asList(121,40,286));
        RuneType.RuneSet.exceptPetRunes = "Hwa,Lisa,Belladeon";

        RunePermutation.perMute("Violent,Nemesis", x -> (x.haveSet("Nemesis") && x.spd >= 80 && x.hp > 130 && x.res > 30),
                // RunePermutation.perMute("Violent,Will", x -> (x.haveWill() && x.spd>20 && x.hp>180 && x.res>20),
                x -> {
                    if (x.spd > 10 || x.hp > 10) {
                        return Arrays.asList(x.hp, x.spd, x.res);
                    }
                    return null;
                }, x -> (x.hp));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        chasun.showPetRune();
    }

    public void darkBearWork() {
        PetType chasun = searchPets("Eshir");
        // Chasun
        RunePermutation.excludeRunes(Arrays.asList(664));
        RuneType.RuneSet.exceptPetRunes = "Lushen,Kahli,Akhamamir,Katarina,Eshir,Jultan,Rina";

        // RunePermutation.perMute("Violent,Blade", x -> (x.hp>150 && x.cr>60),
        // RunePermutation.perMute("Rage,Blade", x -> (x.hp>160 && x.cd>140),
        RunePermutation.perMute("Rage,Energy", x -> (x.hp > 160),
                // RunePermutation.perMute("Violent,Will", x -> (x.haveWill() && x.spd>20 && x.hp>180 && x.res>20),
                x -> {
                    if (x.hp > 6 || x.cd > 6) {
                        return Arrays.asList(x.hp, x.cd);
                    }
                    return null;
                }, x -> (x.cd * x.hp));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        chasun.showPetRune();
    }

    public void jultanWork() {
        PetType chasun = searchPets("Jultan");
        // Chasun
        // RunePermutation.excludeRunes(Arrays.asList(693));
        RuneType.RuneSet.exceptPetRunes = "Galleon,Xiong Fei,Lushen,Kahli,Katarina,Lisa,Colleen,Raki,Theomars,Hwa,Belladeon,Chasun,Verdehile,Zaiross,Talc,Ramagos";

        // RunePermutation.perMute("Violent,Blade", x -> (x.hp>150 && x.cr>60),
        // RunePermutation.perMute("Rage,Blade", x -> (x.hp>160 && x.cd>140),
        RunePermutation.perMute("Violent,Will", x -> (x.hp > 200 && x.acc >= 40 && x.spd >= 10),
                // RunePermutation.perMute("Rage,Blade", x -> (x.hp>160 && x.cr>=65),
                // RunePermutation.perMute("Violent,Will", x -> (x.haveWill() && x.spd>20 && x.hp>180 && x.res>20),
                x -> {
                    if (x.hp > 10 || x.acc > 10) {
                        return Arrays.asList(x.hp, x.acc);
                    }
                    return null;
                }, x -> (x.hp));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        chasun.showPetRune();
    }

    public void eshirWork() {
        PetType chasun = searchPets("Eshir");
        // Chasun
        // RunePermutation.excludeRunes(Arrays.asList(664));
        RuneType.RuneSet.exceptPetRunes = "Lushen,Kahli,Katarina";

        // RunePermutation.perMute("Violent,Blade", x -> (x.hp>150 && x.cr>60),
        // RunePermutation.perMute("Rage,Blade", x -> (x.hp>160 && x.cd>140),
        RunePermutation.perMute("Rage", x -> (x.hp > 100 && x.cr >= 65 && x.cd >= 100 && x.acc >= 60),
                // RunePermutation.perMute("Rage,Blade", x -> (x.hp>160 && x.cr>=65),
                // RunePermutation.perMute("Violent,Will", x -> (x.haveWill() && x.spd>20 && x.hp>180 && x.res>20),
                x -> {
                    if (x.cr > 10 || x.hp > 10 || x.cd > 10 || x.acc > 5) {
                        return Arrays.asList(x.hp, x.cr, x.cd, x.acc);
                    }
                    return null;
                }, x -> (x.cd * x.hp));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        chasun.showPetRune();
    }

    public void rakiWork() {
        PetType chasun = searchPets("Raki");
        // Chasun
        // RunePermutation.excludeRunes(Arrays.asList(121,40,286));
        RuneType.RuneSet.exceptPetRunes = "Hwa,Lisa,Theomars,Lushen,Tyron,Katarina,Kahli,Eshir,Akhamamir,Copper";

        RunePermutation.perMute("Fatal,Blade", x -> (x.cr >= 50),
                // RunePermutation.perMute("Violent,Blade", x -> (x.cr>=50),
                // RunePermutation.perMute("Violent,Will", x -> (x.haveWill() && x.spd>20 && x.hp>180 && x.res>20),
                x -> {
                    if (x.cr > 6 || x.atk > 6 || x.cd > 6) {
                        return Arrays.asList(x.cr, x.atk, x.cd);
                    }
                    return null;
                }, x -> (x.cd * x.atk));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        chasun.showPetRune();
    }

    public void theomarWork() {
        PetType chasun = searchPets("Theomars");
        // Chasun
        // RunePermutation.excludeRunes(Arrays.asList(121,40,286));
        RuneType.RuneSet.exceptPetRunes = "Hwa,Lisa";

        RunePermutation.perMute("Violent,Will", x -> (x.cr >= 31 && x.will() && x.acc >= 50 && x.spd >= 30),
                // RunePermutation.perMute("Violent,Will", x -> (x.haveWill() && x.spd>20 && x.hp>180 && x.res>20),
                x -> {
                    if (x.cr > 6 || x.atk > 6 || x.cd > 6 || x.acc > 5) {
                        return Arrays.asList(x.cr, x.atk, x.cd);
                    }
                    return null;
                }, x -> (x.cd * x.atk));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        chasun.showPetRune();
    }

    public void tritonWork() {
        PetType chasun = searchPets("Triton");
        // Chasun
        // RunePermutation.excludeRunes(Arrays.asList(121,40,286));
        RuneType.RuneSet.exceptPetRunes = "Theomars,Chloe,Lushen,Tyron,Zaiross";
        RuneType.RuneSet.runePet = chasun;

        RunePermutation.perMute("Despair,Will", x -> (x.spd >= 102 && x.acc > 60 && x.will()),
                // RunePermutation.perMute("Rage", x -> (x.cr>=40 && x.spd>=100 && x.acc>30),
                // RunePermutation.perMute("Despair", x -> (x.cr>=30 && x.spd>=90 && x.acc>50),
                x -> {
                    if (x.spd > 10 || x.acc > 10 || x.hp > 10 || x.def > 10) {
                        return Arrays.asList(x.spd, x.hp, x.def, x.acc);
                    }
                    return null;
                }, x -> (x.def * x.hp));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        System.out.println("Combo : " + chasun.comboMap);
        System.out.println("Final : " + chasun.statfixMap);
    }

    public void tyronWork() {
        PetType chasun = searchPets("Tyron");
        // Chasun
        // RunePermutation.excludeRunes(Arrays.asList(121,40,286));
        RuneType.RuneSet.exceptPetRunes = "Theomars,Chloe,Lushen,Zaiross,Susano,Triton";
        RuneType.RuneSet.runePet = chasun;

        // RunePermutation.perMute("Despair", x -> (x.cr>=30 && x.spd>=100 && x.acc>68 && x.haveWill()),
        RunePermutation.genSlot246("SPD", "CDmg", "ATK%");
        RunePermutation.perMute("Despair", x -> (x.cr >= 50 && x.spd >= 97 && x.acc > 50),
                x -> {
                    if (x.spd > 10 || x.acc > 10 || x.cr > 10) {
                        return Arrays.asList(x.spd, x.cr, x.acc);
                    }
                    return null;
                }, x -> (x.finalDamage()));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        System.out.println("Combo : " + chasun.comboMap);
        System.out.println("Final : " + chasun.statfixMap);
    }

    public void poseidonWork() {
        RunePermutation.noBrokenSet = true;
        PetType chasun = searchPets("Poseidon");
        // Chasun
        // RunePermutation.excludeRunes(Arrays.asList(121,40,286));
        RuneType.RuneSet.exceptPetRunes = "Theomars,Chloe,Lushen,Lushen1,Zaiross,Akhamamir,Stella,Raki,Kahli,Tyron,Hwa,Copper";
        RuneType.RuneSet.runePet = chasun;
        RunePermutation.genSlot246("SPD", "CDmg", "ATK%");

        // RunePermutation.perMute("Despair", x -> (x.cr>=30 && x.spd>=100 && x.acc>68 && x.haveWill()),
        RunePermutation.perMute("Rage,Blade,Focus,Guard,Energy,will", x -> (x.cr >= 65 && x.spd >= 90),
                //RunePermutation.perMute("Rage,will", x -> (x.cr >= 65 && x.spd >= 90),
                //RunePermutation.perMute("Despair", x -> (x.cr>=30 && x.spd>=90 && x.acc>50),
                x -> {
                    if (x.spd > 10 || x.acc > 10 || x.cr > 10 || x.cd > 10 || x.atk > 10) {
                        return Arrays.asList(x.spd, x.cr, x.cd, x.atk);
                    }
                    return null;
                }, x -> (x.finalDamage()));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        System.out.println("Combo : " + chasun.comboMap);
        System.out.println("Final : " + chasun.statfixMap);
    }

    public static RuneType.RuneSet getPetRune(String petId) {
        System.out.println("Get Pet RUne : " + petId + " ; " + SwManager.runes.size());
        List<RuneType> runeList = new ArrayList();
        for (int i = 0; i < 6; i++) {
            runeList.add(new RuneType());
        }

        for (RuneType r : SwManager.runes) {
            if (r.monster.equalsIgnoreCase(petId)) {
                runeList.set(r.slot - 1, r);
            }
        }
        RuneType.RuneSet s1 = new RuneType.RuneSet(runeList);
        return s1;
    }

    public void testOtherRune(String curPet, String otherPet) {
        RunePermutation.noBrokenSet = true;
        PetType chasun = searchPets(curPet);
        RuneType.RuneSet g2 = getPetRune(otherPet);

        chasun.applyRuneSet(g2);

        System.out.println(g2.details());
        chasun.showPetRune();
    }

    public void zairossWork() {
        PetType chasun = searchPets("Zaiross");
        // Chasun
        // RunePermutation.excludeRunes(Arrays.asList(121,40,286));
        RuneType.RuneSet.exceptPetRunes = "Theomars,Chloe,Lushen";
        RuneType.RuneSet.runePet = chasun;

        //RunePermutation.perMute("Rage,Blade", x -> (x.cr>=50 && x.spd>=100),
        //RunePermutation.perMute("Despair", x -> (x.cr>=30 && x.spd>=100 && x.acc>68 && x.will()),
        RunePermutation.perMute("Despair,Blade,Focus", x -> (x.cr >= 50 && x.spd >= 105),
                // RunePermutation.perMute("Despair", x -> (x.cr>=30 && x.spd>=90 && x.acc>50),
                x -> {
                    if (x.spd > 10 || x.acc > 10 || x.cr > 10) {
                        return Arrays.asList(x.spd, x.cr, x.acc);
                    }
                    return null;
                }, x -> (x.finalDamage()));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);
        chasun.showPetRune();
    }

    public void hwaWork() {
        PetType chasun = searchPets("Hwa");
        // Chasun

        RuneType.RuneSet.exceptPetRunes = "Bernard,Chloe,Zaiross";
        RunePermutation
                .perMute("Swift,Focus,Blade,Energy,Revenge", x -> (x.spd >= 90 && x.hp > 100 && x.res > 30 && x.acc > 40 && x.cr > 50),
                        // RunePermutation.perMute("Violent,Will", x -> (x.haveWill() && x.spd>20 && x.hp>180 && x.res>20),
                        x -> {
                            if (x.spd > 10 || x.cr > 10 || x.acc > 10 || x.hp > 10 || x.cd > 10) {
                                return Arrays.asList(x.hp, x.spd, x.cr, x.cd);
                            }
                            return null;
                        }, x -> (x.atk * x.cd));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        chasun.showPetRune();
    }

    public void basaltWork() {
        getPet("Basalt").defDame = true;
        getPet("Basalt").def_leader = 30;
        PetType chasun = searchPets("Basalt", x -> (x.f_def * 4.6), 25);
        // Chasun
        // RunePermutation.excludeRunes(Arrays.asList(571,572,569,566));

        //RuneSet.exceptPetRunes = "Zaiross,Raki,Hwa,Kahli,Lushen,Tyron,Eshir,Tanya,Copper,Xiong Fei,Talc,Fedora";
        RuneType.RuneSet.exceptPetRunes = "Talc,Delphoi,Copper,Tyron,Bulldozer";
        RunePermutation.perMute("Guard,WilL,Blade", x -> (x.will() && x.cr >= 65),
                // RunePermutation.perMute("Violent,Will", x -> (x.haveWill() && x.spd>20 && x.hp>180 && x.res>20),
                x -> {
                    if (x.def > 10 || x.cr > 10 || x.cd > 8) {
                        return Arrays.asList(x.def, x.cd, x.cr);
                    }
                    return null;
                }, x -> (x.finalDamage()));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        chasun.showPetRune();
    }

    public void verdWork() {
        PetType chasun = searchPets("Verdehile");
        // Chasun

        RuneType.RuneSet.exceptPetRunes = "Lisa,Hwa,Theomars,Chasun,Bernard";
        RunePermutation.perMute("Violent,Revenge", x -> (x.cr >= 85 && x.cr < 95 && x.rev() && x.spd >= 80),
                // RunePermutation.perMute("Violent,Will", x -> (x.haveWill() && x.spd>20 && x.hp>180 && x.res>20),
                x -> {
                    if (x.cr > 6 || x.atk > 5 || x.spd > 5) {
                        return Arrays.asList(x.atk, x.cr, x.spd);
                    }
                    return null;
                }, x -> (x.spd * x.atk * x.cd));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        chasun.showPetRune();
    }

    public void varusWork() {
        PetType chasun = searchPets("Bernard");
        // Chasun

        RuneType.RuneSet.exceptPetRunes = "Bernard,Zaiross,Lisa,Copper,Colleen,Hwa,Fedora,Belladeon,Eshir";
        RunePermutation.perMute("Revenge,Revenge,Focus,Blade", x -> (x.cr >= 85 && x.cr < 95 && x.acc > 60 && x.rev(2)),
                // RunePermutation.perMute("Violent,Will", x -> (x.haveWill() && x.spd>20 && x.hp>180 && x.res>20),
                x -> {
                    if (x.cr > 6 || x.def > 5 || x.spd > 5) {
                        return Arrays.asList(x.acc, x.cr, x.spd);
                    }
                    return null;
                }, x -> (x.spd));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        chasun.showPetRune();
    }

    public void grogoWork() {
        PetType chasun = searchPets("Groggo");
        // Chasun

        RuneType.RuneSet.exceptPetRunes = "Bernard,Chloe,Zaiross,Verdehile,Raki,Hwa";
        RunePermutation.perMute("Violent,Revenge", x -> (x.def > 130 && x.cr > 50 && x.acc > 30 && x.rev()),
                // RunePermutation.perMute("Violent,Will", x -> (x.haveWill() && x.spd>20 && x.hp>180 && x.res>20),
                x -> {
                    if (x.cr > 6 || x.def > 5 || x.cd > 5) {
                        return Arrays.asList(x.def, x.cr, x.cd, x.acc);
                    }
                    return null;
                }, x -> (x.def * x.cd));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        chasun.showPetRune();
    }

    public void acasisWork() {
        PetType chasun = searchPets("Acasis");
        // Chasun
        ConfigInfo.getInstance().lowRuneLevel = 0;
        RunePermutation.perMute("Fatal,Enhance", x -> (true),
                // RunePermutation.perMute("Violent,Will", x -> (x.haveWill() && x.spd>20 && x.hp>180 && x.res>20),
                x -> {
                    if (true) {
                        return Arrays.asList(x.atk, x.cd, x.cr);
                    }
                    return null;
                }, x -> (x.atk * x.cd));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        chasun.showPetRune();
    }

    public void coleenWork() {
        PetType chasun = searchPets("Colleen");
        // Chasun
        // RunePermutation.excludeRunes(Arrays.asList(121,40,286));
        RuneType.RuneSet.exceptPetRunes = "Lisa,Belladeon,Hwa,Bernard";
        RunePermutation.perMute("Revenge,Revenge", x -> (x.hp > 100 && x.res > 30 && x.acc > 40 && x.def > 80),
                // RunePermutation.perMute("Violent,Will", x -> (x.haveWill() && x.spd>20 && x.hp>180 && x.res>20),
                x -> {
                    if (x.spd > 6 || x.hp > 6 || x.def > 6 || x.acc > 6) {
                        return Arrays.asList(x.hp, x.spd, x.res, x.acc, x.def);
                    }
                    return null;
                }, x -> (x.spd));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        chasun.showPetRune();
    }

    public void lisaWork() {
        PetType chasun = searchPets("Lisa");
        // Chasun
        RunePermutation.excludeRunes(Arrays.asList(121, 40, 286));

        RunePermutation.perMute("Violent,Revenge", x -> (x.haveSet("Revenge") && x.spd >= 80 && x.hp > 50 && x.res > 30 && x.acc > 50),
                // RunePermutation.perMute("Violent,Will", x -> (x.haveWill() && x.spd>20 && x.hp>180 && x.res>20),
                x -> {
                    if (x.spd > 10 || x.hp > 10) {
                        return Arrays.asList(x.hp, x.spd, x.res, x.acc);
                    }
                    return null;
                }, x -> (x.hp));
        chasun.applyRuneSet(RunePermutation.bestRuneSet);

        chasun.showPetRune();
    }

    public void testPests() {
        /*
         * for (int i=0;i<runes.size();i++){ RuneType r1 = runes.get(i); if (r1.spd>30 && r1.slot==2){ System.out.println("Rune : "+r1); } }
         */
        //poseidonWork();
        //zairossWork();
        //testOtherRune("Poseidon","Zaiross");

        //RunePermutation.perRunes = runes;
        // System.out.println("Runes : "+searchRunes("Will",x -> ((x.hp>=17 && x.slot==1))));
        //searchRunes("Swift", x -> ((x.acc >= 10 && x.slot == 6)));
        //searchRunes("Rage", x -> ((x.cr >= 8 && x.cd<=0)));
        // searchPets("Chasun1");
        // System.out.println("Runes : "+searchRunes("Blade",x -> ((x.cr>=5 && x.spd>=5) && x.slot==1)));
        // System.out.println("Runes : "+searchRunes("Energy",x -> ((x.acc>=10 && x.hp>=10) && x.slot==1)));
        // System.out.println("Runes : "+searchRunes("Swift",x -> (x.acc>=10&& x.slot==3)));
        // System.out.println("Runes : "+searchRunes("Will",x -> (x.slot==5 && (x.cr>=5 || x.def>5))));
        // System.out.println("Runes : "+searchRunes("Will",x -> (x.cr>=10 || x.def>15)));
        // System.out.println("Runes : "+searchRunes(null,x -> (x.slot==2 && x.atk>=20 && (x.cr>=5 && x.cd>=5))));
        // searchPets("Sigmarus");
        // PetType lushen = searchPets("Groggo");
        // coleenWork();
        // varusWork();
        // verdWork();
        // basaltWork();
        //zincWork();
        // katarinaWork();
        // bernardWork();
        // rakiWork();
        // theomarWork();
        //ramagodWork();
        //copperWork();
        // delphoyWork();
        //lushenWork();
        //lushenWorkWill();
        ///lushenWorkFast();
        // evaWork();
        // lushenWorkFastRage();
        // rinaWork();
        copperWork();
        // BulldozerWork();
        // meganWork();
        // julieWork();
        // amirWork();
        // eshirWork();
        // jultanWork();
        // darkBearWork();
        // talcWork();
        // hwaWork();
        // bernardWork();
        // tanyaWork();
        //zincWork();
        // droganWork();
        // stellaWork();
        //kahliWork();
        //taruWork();
        //zairossWork();
        // galleonWork();
        taruWork();
        // shumarWork();
        // shrenWork();
        // delphoyWork();
        // tritonWork();
        //tyronWork();
        // sieqWork();
        //basaltWork();
        //acasisWork();
    }

    public static void main(String[] args) {
        SwManager.getInstance().loadPets("optimizer.json");
        new ManualWork().testPests();
    }
}
