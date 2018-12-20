package com.swrunes.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.nio.file.Files;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.swrunes.swrunes.ConfigInfo;
import com.swrunes.swrunes.ConfigInfo.PetSetting;
import com.swrunes.swrunes.Crawler;
import com.swrunes.swrunes.PetType;
import com.swrunes.swrunes.PetType.RuneSkill;
import com.swrunes.swrunes.RunePermutation;

import static com.swrunes.swrunes.RunePermutation.curBestRuneSet;
import static com.swrunes.swrunes.RunePermutation.excludeList;

import com.swrunes.swrunes.RuneType;
import com.swrunes.swrunes.RuneType.RuneSet;

import static com.swrunes.swrunes.RuneType.SPD;
import static com.swrunes.swrunes.RuneType.setBonnusNum;

import com.swrunes.swrunes.SwManager;

import static com.swrunes.swrunes.SwManager.runesIds;


/**
 * The main application
 */
public class Application extends javax.swing.JFrame {

    public void loadData() {
        jComboMainRune.removeAllItems();
        for (String s2 : RuneType.setBonnusLabel) {
            jComboMainRune.addItem(s2);
        }
        jComboMainRune.addItem("Rage/Fatal");
        jComboMainRune.addItem("Violent/Swift");
        jComboMainRune.addItem("Violent/Despair");
        jComboMainRune.addItem("Swift/Despair");
        jComboMainRune.addItem("Swift/Rage");
        jComboMainRune.addItem("Swift/Fatal");

        //System.out.println("jComboMainRune : " + jComboMainRune.getItemCount());

        //System.out.println("Hello");
        SwManager.getInstance().loadPets("optimizer.json");
        jComboPets.removeAllItems();

        TreeSet<String> tr1 = new TreeSet();
        for (PetType p : SwManager.pets.values()) {
            if (p.stars < (6 - ConfigInfo.getInstance().loadPetLevel)) {
                continue;
            }
            tr1.add(p.name);
        }
        for (String s1 : tr1) {
            jComboPets.addItem(s1);
        }

        ConfigInfo cf = ConfigInfo.getInstance();

        reloadFavouriteCombo();

        initFilterVar(firstFilterVar);
        initFilterVar(secondFilterVar);
        initFilterVar(thirdFilterVar);
        initFilterVar(fourthFilterVar);
        initFilterVar(fifthFilterVar);

        RunePermutation.useThreads = cf.useThreads;
        RunePermutation.numThreads = cf.numThreads;
        jCheckThreads.setSelected(cf.useThreads);
        textNumThreads.setValue(cf.numThreads);

        //{"hp","def","atk","cd","cr","acc","res","spd"};
        checkBoxFilters[0] = jCheckHP;
        checkBoxFilters[1] = jCheckDef;
        checkBoxFilters[2] = jCheckAtk;
        checkBoxFilters[3] = jCheckCritDmg;
        checkBoxFilters[4] = jCheckCrit;
        checkBoxFilters[5] = jCheckAcc;
        checkBoxFilters[6] = jCheckRes;
        checkBoxFilters[7] = jCheckSpd;
        //jComboPets.addItem("item2");

        jComboOptimizeFinal.removeAllItems();
        jComboOptimizeFinal.addItem("finalDamage");
        jComboOptimizeFinal.addItem("effectiveHP");
        jComboOptimizeFinal.addItem("atk*cd");
        jComboOptimizeFinal.addItem("hp*def");
        jComboOptimizeFinal.addItem("hp*spd");

        for (String s5 : RuneType.slabels) {
            jComboOptimizeFinal.addItem(s5);
        }
        jComboOptimizeFinal.addItem("effectHp*spd");
        jComboOptimizeFinal.addItem("DMG*spd");
        jComboOptimizeFinal.addItem("AVGDMG");
        jComboOptimizeFinal.addItem("AVGDMG*spd");

//        jComboOptimizeFinal.addItem("bruiser");
//        jComboOptimizeFinal.addItem("hp bruiser");
//        jComboOptimizeFinal.addItem("spd bruiser");
//        jComboOptimizeFinal.addItem("pve dd");
//        jComboOptimizeFinal.addItem("pve def dd");
//        jComboOptimizeFinal.addItem("pvp tank");
//        jComboOptimizeFinal.addItem("tank");
//        jComboOptimizeFinal.addItem("speed");
//        jComboOptimizeFinal.addItem("speed hp");

        jComboPetBuilds.removeAllItems();
        jComboPetBuilds.addItem("All pets");
        for (String s1 : cf.petBuilds.keySet()) {
            jComboPetBuilds.addItem(s1);
        }

        jComboSlot2.removeAllItems();
        jComboSlot2.addItem("");
        for (String s5 : RuneType.slabelsMainDisplay) {
            if (s5.equals("ACC") || s5.equals("CRate") || s5.equals("CDmg") || s5.equals("RES")) {
            } else {
                jComboSlot2.addItem(s5);
            }
        }
        jComboSlot4.removeAllItems();
        jComboSlot4.addItem("");
        //"HP%", "DEF%", "ATK%", "CDmg", "CRate", "ACC", "RES", "SPD"};
        for (String s5 : RuneType.slabelsMainDisplay) {
            if (s5.equals("ACC") || s5.equals("RES") || s5.equals("SPD")) {
            } else {
                jComboSlot4.addItem(s5);
            }
        }
        jComboSlot6.removeAllItems();
        jComboSlot6.addItem("");
        for (String s5 : RuneType.slabelsMainDisplay) {
            if (s5.equals("SPD") || s5.equals("CRate") || s5.equals("CDmg")) {
            } else {
                jComboSlot6.addItem(s5);
            }
        }
        jComboSlot4.addItem("CR,CD,ATK");
        jComboSlot2.addItem("SPD,ATK");
        jComboSlot2.addItem("HP,DEF");
        jComboSlot2.addItem("SPD,HP,DEF");
        jComboSlot2.addItem("SPD,HP");
        jComboSlot2.addItem("SPD,DEF");
        jComboSlot4.addItem("HP,DEF");
        jComboSlot6.addItem("HP,DEF");
        jComboSlot2.addItem("ATK,HP,DEF");
        jComboSlot6.addItem("ATK,HP,DEF");
        jComboSlot2.addItem("ATK,HP");
        jComboSlot4.addItem("ATK,HP");
        jComboSlot6.addItem("ATK,HP");

        jCheckUpPet40.setSelected(cf.upPet40);
        jComboRuneUpgrade.setSelectedIndex(cf.UpgradeAllRune);
        jComboPetLevel.setSelectedIndex(cf.loadPetLevel);
        jTextGlobalLocks.setText(cf.globalLocks);
    }

    public void loadConfig() {
        ConfigInfo cf = ConfigInfo.getInstance();
        tGloryCD.setValue(cf.glory_cd);
        tGloryDEF.setValue(cf.glory_def);
        tGloryATK.setValue(cf.glory_atk);
        tGloryHP.setValue(cf.glory_hp);
        tGlorySpd.setValue(cf.glory_spd);

        tGloryFireAtk.setValue(cf.gloryAtkElement[cf.GL_FIRE]);
        tGloryWaterAtk.setValue(cf.gloryAtkElement[cf.GL_WATER]);
        tGloryWindAtk.setValue(cf.gloryAtkElement[cf.GL_WIND]);
        tGloryDarkAtk.setValue(cf.gloryAtkElement[cf.GL_DARK]);
        tGloryLightAtk.setValue(cf.gloryAtkElement[cf.GL_LIGHT]);

        tWarAtk.setValue(cf.guildwar_atk);
        tWarCd.setValue(cf.guildwar_cd);
        tWarDef.setValue(cf.guildwar_def);
        tWarHp.setValue(cf.guildwar_hp);
        jTextRuneLevel.setValue(cf.lowRuneLevel);

        jCheckPet4StarRune.setSelected(cf.runePet4star);
        jCheckPet5StarRune.setSelected(cf.runePet5star);
        jComboFontSize.setSelectedIndex(cf.fontSize);
    }

    String oTitle = "";

    public void saveConfig() {
        ConfigInfo cf = ConfigInfo.getInstance();
        cf.glory_cd = Integer.parseInt(tGloryCD.getText());
        cf.glory_atk = Integer.parseInt(tGloryATK.getText());
        cf.glory_def = Integer.parseInt(tGloryDEF.getText());
        cf.glory_hp = Integer.parseInt(tGloryHP.getText());
        cf.glory_spd = Integer.parseInt(tGlorySpd.getText());

        cf.guildwar_atk = Integer.parseInt(tWarAtk.getText());
        cf.guildwar_hp = Integer.parseInt(tWarHp.getText());
        cf.guildwar_def = Integer.parseInt(tWarDef.getText());
        cf.guildwar_cd = Integer.parseInt(tWarCd.getText());

        cf.gloryAtkElement[cf.GL_FIRE] = Integer.parseInt(tGloryFireAtk.getText());
        cf.gloryAtkElement[cf.GL_WATER] = Integer.parseInt(tGloryWaterAtk.getText());
        cf.gloryAtkElement[cf.GL_WIND] = Integer.parseInt(tGloryWindAtk.getText());
        cf.gloryAtkElement[cf.GL_LIGHT] = Integer.parseInt(tGloryLightAtk.getText());
        cf.gloryAtkElement[cf.GL_DARK] = Integer.parseInt(tGloryDarkAtk.getText());

        cf.lowRuneLevel = getInt(jTextRuneLevel);
        cf.loadPetLevel = jComboPetLevel.getSelectedIndex();

        cf.runePet4star = jCheckPet4StarRune.isSelected();
        cf.runePet5star = jCheckPet5StarRune.isSelected();
        cf.globalLocks = jTextGlobalLocks.getText();
        cf.fontSize = jComboFontSize.getSelectedIndex();

        cf.saveFile();
    }

    void initFilterVar(javax.swing.JComboBox<String> firstFilterVar) {
        firstFilterVar.removeAllItems();
        firstFilterVar.addItem("-");
        for (String s1 : RuneType.slabels) {
            firstFilterVar.addItem(s1 + " >=");
            firstFilterVar.addItem(s1 + " <=");
        }
        firstFilterVar.addItem("spd==");
        firstFilterVar.addItem("res==");
        firstFilterVar.addItem("acc==");
        firstFilterVar.addItem("eHp>=");
        firstFilterVar.addItem("eHp<=");
    }

    public void reloadFavouriteCombo() {
        jComboFavourite.removeAllItems();

        TreeSet<String> tr1 = new TreeSet();
        for (String p : ConfigInfo.getInstance().favourite) {
            tr1.add(p);
        }

        for (String s1 : tr1) {
            if (SwManager.getPet(s1) != null) {
                jComboFavourite.addItem(s1);
            }
        }
        for (int i = 0; i < jComboFavourite.getItemCount(); i++) {
            if (curPet != null) {
                if (jComboFavourite.getItemAt(i).equalsIgnoreCase(curPet.name)) {
                    jComboFavourite.setSelectedIndex(i);
                }
            }
        }
    }

    boolean initOk = false;

    public void loadOneTime() {
        jTableCurRuneOptimized.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = jTableCurRuneOptimized.rowAtPoint(evt.getPoint());
                int col = jTableCurRuneOptimized.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    //System.out.println("Row : " + row);
                    //System.out.println("col : " + col);
                    if (row == 8) {
                        String pet = "" + jTableCurRuneOptimized.getValueAt(row - 1, col);
                        pet = pet.replaceAll("\\<.*?>", "");
                        //System.out.println("Pet : " + pet);

                        if (!pet.contains("Storage") && !pet.contains(curPet.name)) {
                            int dialogButton = JOptionPane.YES_NO_OPTION;
                            int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to exclude this pet's Runes : [" + pet + "] ? ", "Confirm", dialogButton);

                            if (dialogResult == 0) {
                                String s2 = jTextLocks.getText();
                                if (!s2.endsWith(","))
                                    s2 = s2 + ",";
                                if (!s2.contains(pet + ",")) {
                                    jTextLocks.setText(jTextLocks.getText() + "," + pet);
                                }
                            } else {
                                //System.out.println("No Option");
                            }
                        }
                    }
                }
            }
        });
    }

    public static int[] FONT_SIZE = {13, 14, 16, 18, 20};

    /**
     * Creates new form mainWindow
     */
    public Application() {
        int fontSize = FONT_SIZE[ConfigInfo.getInstance().fontSize];
        //System.out.println("Font size : " + fontSize);
        UIManager.getLookAndFeelDefaults()
                .put("defaultFont", new Font("Tahoma", Font.PLAIN, fontSize));
        initComponents();
        loadData();
        initOk = true;

        loadOneTime();

        jTableResults.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (jTableResults.getSelectedRow() > -1) {
                    // print first column value from selected row
                    //System.out.println(jTableResults.getValueAt(jTableResults.getSelectedRow(), 0).toString());
                    int runeId = Integer.parseInt(jTableResults.getValueAt(jTableResults.getSelectedRow(), 0).toString()) - 1;

                    if (runeId < displayRuneList.size()) {
                        RuneSet r1 = displayRuneList.get(runeId);
                        displayRune2Table(r1, jTableResultsOptimized);
                        updateOptimizedRuneStats(jTablePetStatResults, r1);
                    }
                }
            }
        });

        //System.out.println("Come here last pet : " + ConfigInfo.getInstance().lastPet);
        oTitle = this.getTitle();

        loadOnePet(ConfigInfo.getInstance().lastPet);
        jFrameOptimize.pack();
        jFrameOptimize.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialogGlorySetting = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        tGlorySpd = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        tGloryHP = new javax.swing.JFormattedTextField();
        tGloryCD = new javax.swing.JFormattedTextField();
        tGloryDEF = new javax.swing.JFormattedTextField();
        tGloryATK = new javax.swing.JFormattedTextField();
        tGloryWindAtk = new javax.swing.JFormattedTextField();
        tGloryWaterAtk = new javax.swing.JFormattedTextField();
        jLabel22 = new javax.swing.JLabel();
        tGloryFireAtk = new javax.swing.JFormattedTextField();
        tGloryDarkAtk = new javax.swing.JFormattedTextField();
        tGloryLightAtk = new javax.swing.JFormattedTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        tWarAtk = new javax.swing.JFormattedTextField();
        jLabel18 = new javax.swing.JLabel();
        tWarDef = new javax.swing.JFormattedTextField();
        tWarCd = new javax.swing.JFormattedTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        tWarHp = new javax.swing.JFormattedTextField();
        jPanel10 = new javax.swing.JPanel();
        jTextRuneLevel = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboPetLevel = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        jCheckPet4StarRune = new javax.swing.JCheckBox();
        jCheckPet5StarRune = new javax.swing.JCheckBox();
        jComboFontSize = new javax.swing.JComboBox<>();
        jLabel37 = new javax.swing.JLabel();
        jDialogRune = new javax.swing.JDialog();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableCurRune = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableCurRune2 = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextCurrentRune = new javax.swing.JTextPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextOutput = new javax.swing.JTextPane();
        jDialogRuneManage = new javax.swing.JDialog();
        jDialogBuilds = new javax.swing.JDialog();
        jButtonBuildSave = new javax.swing.JButton();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTableCurRuneBuild = new javax.swing.JTable();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTableStatBuild = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jComboPetBuilds = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTableBuilds = new javax.swing.JTable();
        jButtonDelete = new javax.swing.JButton();
        jFrameOptimize = new javax.swing.JFrame();
        jPanel4 = new javax.swing.JPanel();
        jCheckSpd = new javax.swing.JCheckBox();
        jCheckDef = new javax.swing.JCheckBox();
        jCheckAcc = new javax.swing.JCheckBox();
        jCheckCrit = new javax.swing.JCheckBox();
        jCheckCritDmg = new javax.swing.JCheckBox();
        jCheckAtk = new javax.swing.JCheckBox();
        jCheckHP = new javax.swing.JCheckBox();
        jCheckRes = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jTextFilterValue = new javax.swing.JFormattedTextField();
        jComboRuneUpgrade = new javax.swing.JComboBox<>();
        jCheckUpPet40 = new javax.swing.JCheckBox();
        jPanel11 = new javax.swing.JPanel();
        jTextAllPermus = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        jButtonCheckRunes = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jTextRuneProcess = new javax.swing.JFormattedTextField();
        jCheckOnlyStorge = new javax.swing.JCheckBox();
        jCheckExcludeLocked = new javax.swing.JCheckBox();
        jCheckThreads = new javax.swing.JCheckBox();
        textNumThreads = new javax.swing.JFormattedTextField();
        jLabel24 = new javax.swing.JLabel();
        jComboSkill = new javax.swing.JComboBox<>();
        jLabel33 = new javax.swing.JLabel();
        jTextIncludeRunes = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jTextExcludeRunes = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jTextGlobalLocks = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jTextSkillMulty = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jTextFoundSet = new javax.swing.JFormattedTextField();
        jFrameResults = new javax.swing.JFrame();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableResults = new javax.swing.JTable();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTableResultsOptimized = new javax.swing.JTable();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTablePetStatResults = new javax.swing.JTable();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jDialogHelp = new javax.swing.JDialog();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane19 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        tSecondRuneSet = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jComboMainRune = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jTextLocks = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jComboOptimizeFinal = new javax.swing.JComboBox();
        jComboDouble = new javax.swing.JComboBox<>();
        jButtonOptimize = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jComboSlot2 = new javax.swing.JComboBox();
        jComboSlot4 = new javax.swing.JComboBox();
        jComboSlot6 = new javax.swing.JComboBox();
        jLabel100 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jCheckNoBroken = new javax.swing.JCheckBox();
        jCheckGuildWars = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTableCurRuneOptimized = new javax.swing.JTable();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTablePetStatOptmized = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jLabelIcon = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jProgressBar = new javax.swing.JProgressBar();
        jCheckLockedBuild = new javax.swing.JCheckBox();
        jButtonStop = new javax.swing.JButton();
        jTextCurBest = new javax.swing.JFormattedTextField();
        jLabel30 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        firstFilterVar = new javax.swing.JComboBox<>();
        tFirstValue = new javax.swing.JFormattedTextField();
        secondFilterVar = new javax.swing.JComboBox<>();
        tSecondValue = new javax.swing.JFormattedTextField();
        thirdFilterVar = new javax.swing.JComboBox();
        tFourthValue = new javax.swing.JFormattedTextField();
        jCheckNemesis = new javax.swing.JCheckBox();
        fourthFilterVar = new javax.swing.JComboBox();
        tThirdValue = new javax.swing.JFormattedTextField();
        fifthFilterVar = new javax.swing.JComboBox();
        jCheckWill = new javax.swing.JCheckBox();
        jCheckRevenge = new javax.swing.JCheckBox();
        jCheckShield = new javax.swing.JCheckBox();
        tFifthValue = new javax.swing.JFormattedTextField();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableStatMain = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableCurRuneMain = new javax.swing.JTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTableExtraPetInfo = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        tLeaderSkill = new javax.swing.JFormattedTextField();
        jCheckFavourite = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        jComboFavourite = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        jComboPets = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButtonGWSetting = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();

        jDialogGlorySetting.setTitle("Glory/GW Setting");
        jDialogGlorySetting.setLocation(new java.awt.Point(200, 150));
        jDialogGlorySetting.setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Glory"));

        jLabel11.setText("HP");

        jLabel12.setText("DEF");

        jLabel13.setText("ATK");

        jLabel14.setText("CD");

        tGlorySpd.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        tGlorySpd.setText("0");
        tGlorySpd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tGlorySpdActionPerformed(evt);
            }
        });

        jLabel15.setText("SPD Totem");

        jLabel16.setText("Wind ATK");

        jLabel21.setText("Water ATK");

        tGloryHP.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        tGloryHP.setText("0");
        tGloryHP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tGloryHPActionPerformed(evt);
            }
        });

        tGloryCD.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        tGloryCD.setText("0");
        tGloryCD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tGloryCDActionPerformed(evt);
            }
        });

        tGloryDEF.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        tGloryDEF.setText("0");
        tGloryDEF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tGloryDEFActionPerformed(evt);
            }
        });

        tGloryATK.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        tGloryATK.setText("0");
        tGloryATK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tGloryATKActionPerformed(evt);
            }
        });

        tGloryWindAtk.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        tGloryWindAtk.setText("0");
        tGloryWindAtk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tGloryWindAtkActionPerformed(evt);
            }
        });

        tGloryWaterAtk.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        tGloryWaterAtk.setText("0");
        tGloryWaterAtk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tGloryWaterAtkActionPerformed(evt);
            }
        });

        jLabel22.setText("Fire ATK");

        tGloryFireAtk.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        tGloryFireAtk.setText("0");
        tGloryFireAtk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tGloryFireAtkActionPerformed(evt);
            }
        });

        tGloryDarkAtk.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        tGloryDarkAtk.setText("0");
        tGloryDarkAtk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tGloryDarkAtkActionPerformed(evt);
            }
        });

        tGloryLightAtk.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        tGloryLightAtk.setText("0");
        tGloryLightAtk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tGloryLightAtkActionPerformed(evt);
            }
        });

        jLabel34.setText("Dark ATK");

        jLabel35.setText("Light ATK");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel12)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tGloryDEF, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel11)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tGloryHP, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel13)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tGloryATK, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel14)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(tGloryCD, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel15)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tGlorySpd, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(48, 48, 48)
                                                .addComponent(jLabel22)
                                                .addGap(18, 18, 18)
                                                .addComponent(tGloryFireAtk, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)))
                                .addGap(51, 51, 51))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel16)
                                        .addComponent(jLabel35))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tGloryWindAtk, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tGloryLightAtk, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(36, 36, 36)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel34)
                                        .addComponent(jLabel21))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tGloryDarkAtk, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tGloryWaterAtk, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel11)
                                        .addComponent(jLabel13)
                                        .addComponent(tGlorySpd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel15)
                                        .addComponent(tGloryHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tGloryATK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel12)
                                        .addComponent(jLabel14)
                                        .addComponent(tGloryDEF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tGloryCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel22)
                                        .addComponent(tGloryFireAtk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tGloryDarkAtk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tGloryLightAtk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel34)
                                        .addComponent(jLabel35))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel16)
                                        .addComponent(jLabel21)
                                        .addComponent(tGloryWindAtk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tGloryWaterAtk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jButton2.setText("Save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Guild War Flags"));

        jLabel17.setText("ATK");

        tWarAtk.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        tWarAtk.setText("0");
        tWarAtk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tWarAtkActionPerformed(evt);
            }
        });

        jLabel18.setText("DEF");

        tWarDef.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        tWarDef.setText("0");
        tWarDef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tWarDefActionPerformed(evt);
            }
        });

        tWarCd.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        tWarCd.setText("0");
        tWarCd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tWarCdActionPerformed(evt);
            }
        });

        jLabel19.setText("CD");

        jLabel20.setText("HP");

        tWarHp.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        tWarHp.setText("0");
        tWarHp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tWarHpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap(24, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel19)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(tWarCd))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel17)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tWarAtk, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel20)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tWarHp, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel18)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tWarDef, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tWarAtk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel17)
                                        .addComponent(tWarDef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel18))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(tWarHp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel20))
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(tWarCd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel19)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Game Setting"));

        jTextRuneLevel.setText("6");
        jTextRuneLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextRuneLevelActionPerformed(evt);
            }
        });

        jLabel2.setText("Exclude Rune lv <");

        jComboPetLevel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"6* above", "5* above", "4* above"}));

        jLabel32.setText("Load only pets from");

        jCheckPet4StarRune.setText("Consider Runes from <=4* pet is Inventory");

        jCheckPet5StarRune.setText("Consider Runes from <=5* pet is Inventory");

        jComboFontSize.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"13", "14", "16", "18", "20"}));

        jLabel37.setText("Tool Font Size (Need Restart) ");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel10Layout.createSequentialGroup()
                                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel32)
                                                        .addComponent(jLabel2))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jTextRuneLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jComboPetLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(108, 108, 108)
                                                .addComponent(jLabel37)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComboFontSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jCheckPet4StarRune, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jCheckPet5StarRune, javax.swing.GroupLayout.Alignment.TRAILING)))
                                .addContainerGap(60, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextRuneLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboPetLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel32)
                                        .addComponent(jComboFontSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel37))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckPet4StarRune)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckPet5StarRune)
                                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialogGlorySettingLayout = new javax.swing.GroupLayout(jDialogGlorySetting.getContentPane());
        jDialogGlorySetting.getContentPane().setLayout(jDialogGlorySettingLayout);
        jDialogGlorySettingLayout.setHorizontalGroup(
                jDialogGlorySettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jDialogGlorySettingLayout.createSequentialGroup()
                                .addGroup(jDialogGlorySettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jDialogGlorySettingLayout.createSequentialGroup()
                                                .addGap(102, 102, 102)
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jDialogGlorySettingLayout.createSequentialGroup()
                                                .addGap(27, 27, 27)
                                                .addGroup(jDialogGlorySettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(jDialogGlorySettingLayout.createSequentialGroup()
                                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addContainerGap(115, Short.MAX_VALUE))
        );
        jDialogGlorySettingLayout.setVerticalGroup(
                jDialogGlorySettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jDialogGlorySettingLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jDialogGlorySettingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(21, 21, 21)
                                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36))
        );

        jTableCurRune.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null}
                },
                new String[]{
                        "Rune1", "Rune2", "Rune3", "Rune4", "Rune5", "Rune6"
                }
        ));
        jTableCurRune.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableCurRune.setAutoscrolls(false);
        jScrollPane4.setViewportView(jTableCurRune);

        jTableCurRune2.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null},
                        {"Set", null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null}
                },
                new String[]{
                        "Rune", "Main", "Rune3", "Rune4", "Rune5", "Rune6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11"
                }
        ));
        jTableCurRune2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableCurRune2.setAutoscrolls(false);
        jScrollPane6.setViewportView(jTableCurRune2);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {"Pet", null},
                        {"Final Damage", null},
                        {"Damage (GW)", null},
                        {"Effective HP", null},
                        {"HP", null},
                        {"DEF", null},
                        {"SPD", null}
                },
                new String[]{
                        "Title 1", "Title 2"
                }
        ));
        jScrollPane8.setViewportView(jTable1);

        jScrollPane2.setViewportView(jTextCurrentRune);

        jTextOutput.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jScrollPane1.setViewportView(jTextOutput);

        javax.swing.GroupLayout jDialogRuneLayout = new javax.swing.GroupLayout(jDialogRune.getContentPane());
        jDialogRune.getContentPane().setLayout(jDialogRuneLayout);
        jDialogRuneLayout.setHorizontalGroup(
                jDialogRuneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jDialogRuneLayout.createSequentialGroup()
                                .addGroup(jDialogRuneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jDialogRuneLayout.createSequentialGroup()
                                                .addGap(139, 139, 139)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jDialogRuneLayout.createSequentialGroup()
                                                .addGap(33, 33, 33)
                                                .addGroup(jDialogRuneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 786, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(jDialogRuneLayout.createSequentialGroup()
                                                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(48, 48, 48)
                                                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(32, 32, 32)
                                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addContainerGap(545, Short.MAX_VALUE))
        );
        jDialogRuneLayout.setVerticalGroup(
                jDialogRuneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jDialogRuneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jDialogRuneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jDialogRuneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                                .addContainerGap())
        );

        jDialogRuneManage.setTitle("Rune Manage");

        javax.swing.GroupLayout jDialogRuneManageLayout = new javax.swing.GroupLayout(jDialogRuneManage.getContentPane());
        jDialogRuneManage.getContentPane().setLayout(jDialogRuneManageLayout);
        jDialogRuneManageLayout.setHorizontalGroup(
                jDialogRuneManageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1022, Short.MAX_VALUE)
        );
        jDialogRuneManageLayout.setVerticalGroup(
                jDialogRuneManageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 507, Short.MAX_VALUE)
        );

        jDialogBuilds.setTitle("Manage saved Builds");

        jButtonBuildSave.setText("Save");
        jButtonBuildSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuildSaveActionPerformed(evt);
            }
        });

        jTableCurRuneBuild.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jTableCurRuneBuild.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null}
                },
                new String[]{
                        "Rune1", "Rune2", "Rune3", "Rune4", "Rune5", "Rune6"
                }
        ));
        jTableCurRuneBuild.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableCurRuneBuild.setAutoscrolls(false);
        jScrollPane14.setViewportView(jTableCurRuneBuild);

        jTableStatBuild.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jTableStatBuild.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String[]{
                        "Stat", "Base", "Rune", "Final"
                }
        ));
        jTableStatBuild.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane15.setViewportView(jTableStatBuild);

        jPanel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton6.setText("Unlock");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton5.setText("Lock");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jComboPetBuilds.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboPetBuilds.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboPetBuildsActionPerformed(evt);
            }
        });

        jLabel28.setText("Pet");

        jTableBuilds.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null}
                },
                new String[]{
                        "Num", "Pet Name", "RuneSet", "MainStat", "Spd", "Effective HP", "Final Damage", "Locked"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane10.setViewportView(jTableBuilds);

        jButtonDelete.setText("Delete");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 776, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel9Layout.createSequentialGroup()
                                                .addComponent(jLabel28)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComboPetBuilds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(176, 176, 176)
                                                .addComponent(jButton5)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton6)
                                                .addGap(75, 75, 75)
                                                .addComponent(jButtonDelete)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboPetBuilds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel28)
                                        .addComponent(jButton5)
                                        .addComponent(jButton6)
                                        .addComponent(jButtonDelete))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialogBuildsLayout = new javax.swing.GroupLayout(jDialogBuilds.getContentPane());
        jDialogBuilds.getContentPane().setLayout(jDialogBuildsLayout);
        jDialogBuildsLayout.setHorizontalGroup(
                jDialogBuildsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jDialogBuildsLayout.createSequentialGroup()
                                .addGap(438, 438, 438)
                                .addComponent(jButtonBuildSave, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 365, Short.MAX_VALUE))
                        .addGroup(jDialogBuildsLayout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addGroup(jDialogBuildsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jDialogBuildsLayout.createSequentialGroup()
                                                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialogBuildsLayout.setVerticalGroup(
                jDialogBuildsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jDialogBuildsLayout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addGroup(jDialogBuildsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonBuildSave)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jFrameOptimize.setTitle("Filter Option");
        jFrameOptimize.setResizable(false);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Stat Filter"));

        jCheckSpd.setText("SPD");
        jCheckSpd.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckSpdStateChanged(evt);
            }
        });
        jCheckSpd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckSpdActionPerformed(evt);
            }
        });

        jCheckDef.setText("DEF");
        jCheckDef.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckDefStateChanged(evt);
            }
        });
        jCheckDef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckDefActionPerformed(evt);
            }
        });

        jCheckAcc.setText("ACC");
        jCheckAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckAccActionPerformed(evt);
            }
        });

        jCheckCrit.setText("CRit");
        jCheckCrit.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckCritStateChanged(evt);
            }
        });
        jCheckCrit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckCritActionPerformed(evt);
            }
        });

        jCheckCritDmg.setText("CDmg");
        jCheckCritDmg.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckCritDmgStateChanged(evt);
            }
        });
        jCheckCritDmg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckCritDmgActionPerformed(evt);
            }
        });

        jCheckAtk.setText("Atk");
        jCheckAtk.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckAtkStateChanged(evt);
            }
        });
        jCheckAtk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckAtkActionPerformed(evt);
            }
        });

        jCheckHP.setText("HP");
        jCheckHP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckHPActionPerformed(evt);
            }
        });

        jCheckRes.setText("RES");
        jCheckRes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckResActionPerformed(evt);
            }
        });

        jLabel4.setText("Filter Values >");

        jTextFilterValue.setText("5");

        jComboRuneUpgrade.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Upgrade Rune 2,4,6 to +15", "Keep Original Rune level", "Upgrade Rune 2,4,6 to +12", "Upgrade All Runes to +15", "Up 2,4,6 to +15, 1,3,5 to +12"}));
        jComboRuneUpgrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboRuneUpgradeActionPerformed(evt);
            }
        });

        jCheckUpPet40.setText("Upgrade Pet stat to lv 40");
        jCheckUpPet40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckUpPet40ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jCheckUpPet40)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jTextFilterValue, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                                                .addComponent(jCheckSpd)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jCheckDef)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jCheckCrit))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                                                .addComponent(jCheckCritDmg)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jCheckAtk)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jCheckHP)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jCheckAcc)
                                                        .addComponent(jCheckRes)))
                                        .addComponent(jComboRuneUpgrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextFilterValue)
                                        .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jCheckSpd)
                                        .addComponent(jCheckCrit)
                                        .addComponent(jCheckDef)
                                        .addComponent(jCheckAcc))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jCheckAtk)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jCheckHP)
                                                .addComponent(jCheckCritDmg)
                                                .addComponent(jCheckRes)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboRuneUpgrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCheckUpPet40)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextAllPermus.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jLabel10.setText("Permus");

        jButtonCheckRunes.setText("Check");
        jButtonCheckRunes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckRunesActionPerformed(evt);
            }
        });

        jLabel9.setText("Runes");

        jTextRuneProcess.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jCheckOnlyStorge.setText("Only use Storage Runes And current equipped");
        jCheckOnlyStorge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckOnlyStorgeActionPerformed(evt);
            }
        });

        jCheckExcludeLocked.setText("Excluded Locked Builds");
        jCheckExcludeLocked.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckExcludeLockedActionPerformed(evt);
            }
        });

        jCheckThreads.setText("MultyThreading");
        jCheckThreads.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckThreadsActionPerformed(evt);
            }
        });

        textNumThreads.setText("0");
        textNumThreads.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textNumThreadsActionPerformed(evt);
            }
        });

        jLabel24.setText("Threads");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel11Layout.createSequentialGroup()
                                                .addComponent(jCheckThreads)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(textNumThreads, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel24))
                                        .addComponent(jCheckOnlyStorge)
                                        .addComponent(jCheckExcludeLocked)
                                        .addGroup(jPanel11Layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(jButtonCheckRunes, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel9)
                                                .addGap(18, 18, 18)
                                                .addComponent(jTextRuneProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel11Layout.createSequentialGroup()
                                                .addComponent(jLabel10)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextAllPermus, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel10)
                                        .addComponent(jTextAllPermus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButtonCheckRunes)
                                        .addComponent(jLabel9)
                                        .addComponent(jTextRuneProcess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCheckOnlyStorge)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckExcludeLocked)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jCheckThreads)
                                        .addComponent(textNumThreads, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel24))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jComboSkill.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboSkill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboSkillActionPerformed(evt);
            }
        });

        jLabel33.setText("Skill damage");

        jLabel26.setText("Include RuneID");

        jLabel27.setText("Excludes RuneIDS");

        jTextGlobalLocks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextGlobalLocksActionPerformed(evt);
            }
        });

        jLabel36.setText("Global Locks Pets");

        jLabel31.setText("Found :");

        jTextFoundSet.setText("0");

        javax.swing.GroupLayout jFrameOptimizeLayout = new javax.swing.GroupLayout(jFrameOptimize.getContentPane());
        jFrameOptimize.getContentPane().setLayout(jFrameOptimizeLayout);
        jFrameOptimizeLayout.setHorizontalGroup(
                jFrameOptimizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jFrameOptimizeLayout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addGroup(jFrameOptimizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jFrameOptimizeLayout.createSequentialGroup()
                                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jFrameOptimizeLayout.createSequentialGroup()
                                                .addGap(13, 13, 13)
                                                .addGroup(jFrameOptimizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(jFrameOptimizeLayout.createSequentialGroup()
                                                                .addGroup(jFrameOptimizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabel26)
                                                                        .addComponent(jLabel27)
                                                                        .addComponent(jLabel36))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jFrameOptimizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(jTextGlobalLocks, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGroup(jFrameOptimizeLayout.createSequentialGroup()
                                                                                .addGroup(jFrameOptimizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                                        .addComponent(jTextExcludeRunes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                                                                        .addComponent(jTextIncludeRunes, javax.swing.GroupLayout.Alignment.LEADING))
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                .addComponent(jLabel31)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(jTextFoundSet, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                        .addGroup(jFrameOptimizeLayout.createSequentialGroup()
                                                                .addComponent(jLabel33)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jComboSkill, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jTextSkillMulty)))))
                                .addContainerGap(60, Short.MAX_VALUE))
        );
        jFrameOptimizeLayout.setVerticalGroup(
                jFrameOptimizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jFrameOptimizeLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jFrameOptimizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(15, 15, 15)
                                .addGroup(jFrameOptimizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jFrameOptimizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jTextFoundSet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel31))
                                        .addGroup(jFrameOptimizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jTextIncludeRunes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel26)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jFrameOptimizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextExcludeRunes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel27))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jFrameOptimizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextGlobalLocks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel36))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jFrameOptimizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboSkill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel33)
                                        .addComponent(jTextSkillMulty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(7, 7, 7))
        );

        jFrameResults.setTitle("Optimize Results");
        jFrameResults.setResizable(false);

        jTableResults.setAutoCreateRowSorter(true);
        jTableResults.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTableResults.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null}
                },
                new String[]{
                        "No", "Rune Sets", "Main Stats", "eHP", "Spd", "Crit", "HP", "Def", "Acc", "RCost", "Final"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false, true, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTableResults);

        jTableResultsOptimized.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jTableResultsOptimized.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null}
                },
                new String[]{
                        "Rune1", "Rune2", "Rune3", "Rune4", "Rune5", "Rune6"
                }
        ));
        jTableResultsOptimized.setToolTipText("Click on the Lock icon at bottom to lock the Pet Runes.");
        jTableResultsOptimized.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableResultsOptimized.setAutoscrolls(false);
        jScrollPane16.setViewportView(jTableResultsOptimized);

        jTablePetStatResults.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null}
                },
                new String[]{
                        "Stat", "Optmized", "+"
                }
        ));
        jTablePetStatResults.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane17.setViewportView(jTablePetStatResults);

        jButton10.setText("Save Build");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setText("Manage Builds");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setText("Equip Build");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jFrameResultsLayout = new javax.swing.GroupLayout(jFrameResults.getContentPane());
        jFrameResults.getContentPane().setLayout(jFrameResultsLayout);
        jFrameResultsLayout.setHorizontalGroup(
                jFrameResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jFrameResultsLayout.createSequentialGroup()
                                .addGroup(jFrameResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jFrameResultsLayout.createSequentialGroup()
                                                .addGap(33, 33, 33)
                                                .addGroup(jFrameResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jScrollPane3)
                                                        .addGroup(jFrameResultsLayout.createSequentialGroup()
                                                                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 605, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(jFrameResultsLayout.createSequentialGroup()
                                                .addGap(123, 123, 123)
                                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(33, 33, 33)
                                                .addComponent(jButton11)
                                                .addGap(45, 45, 45)
                                                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(31, Short.MAX_VALUE))
        );
        jFrameResultsLayout.setVerticalGroup(
                jFrameResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jFrameResultsLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jFrameResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jFrameResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton10)
                                        .addComponent(jButton11)
                                        .addComponent(jButton12))
                                .addContainerGap(24, Short.MAX_VALUE))
        );

        jDialogHelp.setTitle("Help");

        jTextPane1.setEditable(false);
        jTextPane1.setText("**SETUP**\n\n* Need Java 8 : http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html\n* Need data from swproxy : https://www.reddit.com/r/summonerswar/comments/490u23/swproxy_new_version_released/\n* Download : https://github.com/nikilauda1983/sw_optimize/releases\n* Download file release.zip (latest release)\n* Extract and run the .jar file (Double click on the jar file, it will run if you installed java)\n\n**HOW TO USE**\n\n* Generate optimizer.json file from SwProxy. If you dont know how check this : \n\thttps://www.reddit.com/r/summonerswar/comments/490u23/swproxy_new_version_released/\n* Load your optimizer.json file from Menu File/Load. Or replace the content of file optimizer.json with the new file you got.\n* Choose the pet from Choose pet box, or Favourite ( if you already use this pet before). The pet info will be displayed, current stats, current runes set, current Damage (in gw or toa).\n\n**FEATURE**\n\n* Based on swproxy data (optimizer.json) file\n* Search for the best rune set that meet your requirement. \n* Run quite fast. 10 million permutations in 10s. Multithreading double the performance\n* Support real Damage base on glory/guildwar building you input. Already have damage method base on 3rd or 2nd skill of each Pet, with multiplier. Can test with Copper or Lushen (Ignore defense). Example Copper third skill : ATK x 3.0+DEF x 3.0\n* Easy to set filter, just need to set what you want (highest damage or highest hp, highest spd...). \n* Save all your build/filter/config for each pet.\n* All runes 2,4,6 are upgrade to lv 15\n* Turn off Multithread or decrease the number of Threads if your computer run laggy.");
        jScrollPane11.setViewportView(jTextPane1);

        jTabbedPane1.addTab("How to start", jScrollPane11);

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText("* Choose your pet stat to optimize near \"Optimize for\". MainSet set the main runeset, second Set is for secondSet, it can be blank for BrokenSet or :\"Blade\",\"Blade,Will\",\"Guard,Energy,Endure\"... you can type any set you want with comma. \n\t* Example for Bernard you want him best spd, choose spd, choose MainSet is Swift, SeconSet we left blank (it means all possible set, broken set). Then hit Optimize button. \n\t* For Lushen : Mainset choose Rage, SecondSet type Blade (We will use Rage,Blade set). optimize for finalDamage (Base on skill 3 of Lushen Amputation). This will generate the best output damage. On \"Pet filter\" panel, select Cr >= and 70. We want lushen to have at least 70 crit, if you want speed, choose speed at the below. We can have 3 more filters here.\n\t* For Chloe : Mainset will be Swift, Second set will be Will. but to be sure Chloe have will set, click on \"Will\" on Pet Filter (on the right top). Lockpet should be \"Bernard\", we dont want to use Bernard rune here. Bernard should have best spd runes.\n\t* For Copper : MainSet can be Rage/Will ; Rage/Blade ; Guard/Blade,Will... depend on what you want. Optimize for finalDamage (the display damage is real damage, you can test in TOA or guildwar). Crit >=70 filter. haveWill...\n\t* For Rina : Energy/Endure,Will... Choose optimize for HP, set filter RES>=90, DEF>=1000...\n\t* For Ramagod : Vampire/Will... Choose optimize for HP, set filter have Will.\n\t* For Theomars : Violent/Revenge.. Violent/Will ... Choose optimize for finalDamage, filter acc>=40,cr>=60,spd>=170...\n* Check button, to check number of runes that match our filter, the less, the faster. And estimated number of permutations.\n* Stop button. Stop while optimize if it take too long.\n* If you have 2 same pet (ex:  Lushen), Lushen1 will be the second.\n* \"Values>\" next to this text. Is the value to filter all Runes. All rune will have at least one stat >= this value. The higher this value, the less Rune will match and run faster. 7-10 is ok, set it too high, and we will have no runes match our filter. For endGame user, you want it to be 10, most your rune will have substats >=10, but for begginer/medium players set to 5.\n");
        jTextArea1.setWrapStyleWord(true);
        jScrollPane18.setViewportView(jTextArea1);

        jTabbedPane1.addTab("How to Optimize", jScrollPane18);

        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        jTextArea2.setRows(5);
        jScrollPane19.setViewportView(jTextArea2);

        jTabbedPane1.addTab("Frequently Ask Question", jScrollPane19);

        javax.swing.GroupLayout jDialogHelpLayout = new javax.swing.GroupLayout(jDialogHelp.getContentPane());
        jDialogHelp.getContentPane().setLayout(jDialogHelpLayout);
        jDialogHelpLayout.setHorizontalGroup(
                jDialogHelpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogHelpLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 746, Short.MAX_VALUE)
                                .addContainerGap())
        );
        jDialogHelpLayout.setVerticalGroup(
                jDialogHelpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jDialogHelpLayout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SwRune 3.0");
        setSize(new java.awt.Dimension(0, 0));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Rune Filter"));

        jLabel7.setText("Second Rune");

        jLabel6.setText("Main rune set");

        jComboMainRune.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboMainRune.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboMainRuneActionPerformed(evt);
            }
        });

        jLabel8.setText("Lock pets :");

        jTextLocks.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jTextLocks.setForeground(new java.awt.Color(102, 51, 255));
        jTextLocks.setText("Zaiross");
        jTextLocks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextLocksActionPerformed(evt);
            }
        });

        jLabel5.setText("Optimize for");

        jComboOptimizeFinal.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboOptimizeFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboOptimizeFinalActionPerformed(evt);
            }
        });

        jComboDouble.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"x1", "x2", "x3"}));
        jComboDouble.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboDoubleActionPerformed(evt);
            }
        });

        jButtonOptimize.setText("Optimize");
        jButtonOptimize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptimizeActionPerformed(evt);
            }
        });

        jButton7.setText("Filter");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jComboSlot2.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboSlot2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboSlot2ActionPerformed(evt);
            }
        });

        jComboSlot4.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboSlot4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboSlot4ActionPerformed(evt);
            }
        });

        jComboSlot6.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboSlot6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboSlot6ActionPerformed(evt);
            }
        });

        jLabel100.setText(" Slot4");

        jLabel3.setText(" Slot6");

        jLabel29.setText(" Slot2");

        jCheckNoBroken.setText("No Broken Set");
        jCheckNoBroken.setToolTipText("No broken set Rune. <br>\n(Exmp : Rage,Blade,Will)");
        jCheckNoBroken.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckNoBrokenActionPerformed(evt);
            }
        });

        jCheckGuildWars.setText("Guild Wars");
        jCheckGuildWars.setToolTipText("Optimize stats using guild war towers.");
        jCheckGuildWars.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckGuildWarsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addComponent(jLabel6)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jComboMainRune, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(2, 2, 2)
                                                .addComponent(jComboDouble, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel7)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tSecondRuneSet, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButtonOptimize, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton7)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel29)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComboSlot2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel100)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComboSlot4, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jComboSlot6, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComboOptimizeFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jCheckNoBroken)
                                                        .addComponent(jCheckGuildWars))
                                                .addGap(17, 17, 17)
                                                .addComponent(jLabel8)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextLocks, javax.swing.GroupLayout.PREFERRED_SIZE, 756, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboSlot2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel100)
                                        .addComponent(jLabel29)
                                        .addComponent(jComboSlot4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3)
                                        .addComponent(jComboSlot6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jComboOptimizeFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel5))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jTextLocks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel8)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tSecondRuneSet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel6)
                                        .addComponent(jComboMainRune, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboDouble, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButtonOptimize)
                                        .addComponent(jButton7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jCheckNoBroken)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckGuildWars)
                                .addGap(3, 3, 3))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Optmized"));

        jTableCurRuneOptimized.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jTableCurRuneOptimized.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null}
                },
                new String[]{
                        "Rune1", "Rune2", "Rune3", "Rune4", "Rune5", "Rune6"
                }
        ));
        jTableCurRuneOptimized.setToolTipText("Click on the Lock icon at bottom to lock the Pet Runes.");
        jTableCurRuneOptimized.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableCurRuneOptimized.setAutoscrolls(false);
        jScrollPane12.setViewportView(jTableCurRuneOptimized);

        jTablePetStatOptmized.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null}
                },
                new String[]{
                        "Stat", "Optmized", "+"
                }
        ));
        jTablePetStatOptmized.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane13.setViewportView(jTablePetStatOptmized);

        jButton4.setText("Build Manage");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabelIcon.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabelIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelIconMouseClicked(evt);
            }
        });

        jButton3.setText("Save Build");
        jButton3.setToolTipText("<html> If this build is good, we save it here<br>\nSo we can review later. Lock the runes.<br>\nSo there pets cant use it.\n</html>");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jCheckLockedBuild.setText("Locked build");
        jCheckLockedBuild.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckLockedBuildActionPerformed(evt);
            }
        });

        jButtonStop.setText("Stop");
        jButtonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStopActionPerformed(evt);
            }
        });

        jTextCurBest.setText("0");

        jLabel30.setText("Cur Best :");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pet Filter"));

        firstFilterVar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        firstFilterVar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstFilterVarActionPerformed(evt);
            }
        });

        tFirstValue.setText("0");

        secondFilterVar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        secondFilterVar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                secondFilterVarActionPerformed(evt);
            }
        });

        tSecondValue.setText("0");

        thirdFilterVar.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        thirdFilterVar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thirdFilterVarActionPerformed(evt);
            }
        });

        tFourthValue.setText("0");

        jCheckNemesis.setText("Nemesis");
        jCheckNemesis.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckNemesisStateChanged(evt);
            }
        });
        jCheckNemesis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckNemesisActionPerformed(evt);
            }
        });

        fourthFilterVar.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        fourthFilterVar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fourthFilterVarActionPerformed(evt);
            }
        });

        tThirdValue.setText("0");

        fifthFilterVar.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        fifthFilterVar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fifthFilterVarActionPerformed(evt);
            }
        });

        jCheckWill.setText("Will");
        jCheckWill.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckWillStateChanged(evt);
            }
        });
        jCheckWill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckWillActionPerformed(evt);
            }
        });

        jCheckRevenge.setText("Reveng");
        jCheckRevenge.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckRevengeStateChanged(evt);
            }
        });
        jCheckRevenge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckRevengeActionPerformed(evt);
            }
        });

        jCheckShield.setText("Shield");
        jCheckShield.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckShieldStateChanged(evt);
            }
        });
        jCheckShield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckShieldActionPerformed(evt);
            }
        });

        tFifthValue.setText("0");
        tFifthValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tFifthValueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jCheckWill)
                                                        .addComponent(jCheckRevenge))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jCheckShield)
                                                        .addComponent(jCheckNemesis)))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(firstFilterVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(secondFilterVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(thirdFilterVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(fourthFilterVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(tSecondValue, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(tFirstValue, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(tThirdValue, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(tFourthValue, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addComponent(fifthFilterVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(tFifthValue, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, Short.MAX_VALUE)))
                                                .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(firstFilterVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tFirstValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(secondFilterVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tSecondValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(thirdFilterVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tThirdValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(fourthFilterVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tFourthValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(fifthFilterVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tFifthValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jCheckWill)
                                        .addComponent(jCheckNemesis))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jCheckRevenge)
                                        .addComponent(jCheckShield))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton8.setText("Equip this");
        jButton8.setToolTipText("<html>If you think this build is good.<br>\nYou equip your pet in game with this runes. <br>\nSo instead of re-run SW Proxy to update json file.<br>\nWe can update by real-equip this rune set.</html>");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Results");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel38.setText("This final stat is after Glory building");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabelIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton3)
                                        .addComponent(jButton4)
                                        .addComponent(jCheckLockedBuild)
                                        .addComponent(jButtonStop)
                                        .addComponent(jButton8))
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addGap(4, 4, 4)
                                                .addComponent(jProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton9)
                                                .addGap(46, 46, 46))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addGap(39, 39, 39)
                                                .addComponent(jLabel30)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextCurBest, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel38))
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jTextCurBest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jLabel30))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(jButton9)
                                                                        .addComponent(jProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(14, 14, 14)))
                                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel38))))
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addComponent(jButtonStop)
                                                .addGap(1, 1, 1)
                                                .addComponent(jLabelIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jCheckLockedBuild)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton8)))
                                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Current Equip"));

        jTableStatMain.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String[]{
                        "Stat", "Base", "Rune", "Final"
                }
        ));
        jTableStatMain.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane5.setViewportView(jTableStatMain);

        jTableCurRuneMain.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jTableCurRuneMain.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null}
                },
                new String[]{
                        "Rune1", "Rune2", "Rune3", "Rune4", "Rune5", "Rune6"
                }
        ));
        jTableCurRuneMain.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableCurRuneMain.setAutoscrolls(false);
        jScrollPane7.setViewportView(jTableCurRuneMain);

        jTableExtraPetInfo.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {"Pet", null},
                        {"Final Damage", null},
                        {"Damage (GW)", null},
                        {"Effective HP", null},
                        {"HP", null},
                        {"DEF", null},
                        {"SPD", null},
                        {"RuneSet", null},
                        {"Skill", null},
                        {"Skill Multy", null}
                },
                new String[]{
                        "Stat", "Value"
                }
        ));
        jScrollPane9.setViewportView(jTableExtraPetInfo);

        jLabel25.setText("Leader Buff : ");

        tLeaderSkill.setText("30");
        tLeaderSkill.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tLeaderSkillFocusLost(evt);
            }
        });
        tLeaderSkill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tLeaderSkillActionPerformed(evt);
            }
        });
        tLeaderSkill.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tLeaderSkillPropertyChange(evt);
            }
        });

        jCheckFavourite.setText("favourite");
        jCheckFavourite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckFavouriteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addComponent(jLabel25)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(tLeaderSkill, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(1, 1, 1)
                                                .addComponent(jCheckFavourite))
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 652, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                                .addGap(11, 11, 11))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(178, 178, 178)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tLeaderSkill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel25)
                                        .addComponent(jCheckFavourite)))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jComboFavourite.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboFavourite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboFavouriteActionPerformed(evt);
            }
        });

        jLabel23.setText("Favourites");

        jComboPets.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboPets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboPetsActionPerformed(evt);
            }
        });

        jLabel1.setText("Choose Pet");

        jButton1.setText("Rune manage");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButtonGWSetting.setText("Setting");
        jButtonGWSetting.setToolTipText("Set your glory building (Attack,defesne, element attack...) \n level and guild war flag level here.");
        jButtonGWSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGWSettingActionPerformed(evt);
            }
        });

        jButton13.setText("Pet Detail");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel23)
                                        .addComponent(jButtonGWSetting)
                                        .addComponent(jButton1)
                                        .addComponent(jComboPets, 0, 143, Short.MAX_VALUE)
                                        .addComponent(jComboFavourite, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(29, 29, 29))
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButton13)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboPets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel23)
                                .addGap(1, 1, 1)
                                .addComponent(jComboFavourite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonGWSetting)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton13)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu1.setText("File");

        jMenuItem1.setText("Load file");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem4.setText("Setting");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem10.setText("Clear Favourites");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem10);

        jMenuItem12.setText("Reload json file");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem12);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Tools");

        jMenuItem2.setText("Rune Manager");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("Saved Builds Manage");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem6.setText("Pets Manager");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem7.setText("Pets Compare");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Help");

        jMenuItem5.setText("hatuan1983@gmail.com");
        jMenu3.add(jMenuItem5);

        jMenuItem9.setText("reddit /u hatuan1983");
        jMenu3.add(jMenuItem9);

        jMenuItem11.setText("Help");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem11);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Runes");

        jMenuItem8.setText("Unequit Runes");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem8);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Optimize");

        jMenuItem13.setText("Full Optimize (Old)");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem13);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(1, 1, 1)
                                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void displayStatMain(JTable jTableStatMain, PetType pet) {
        setupTable(jTableStatMain);
        jTableStatMain.getColumnModel().getColumn(0).setPreferredWidth(50);
        jTableStatMain.getColumnModel().getColumn(1).setPreferredWidth(60);
        jTableStatMain.getColumnModel().getColumn(2).setPreferredWidth(70);
        for (int i = 0; i < RuneType.slabels.length; i++) {
            jTableStatMain.getModel().setValueAt("<html><b><font color=\"blue\">" + RuneType.slabels[i].toUpperCase() + "</font></b></html>", i, 0);
            jTableStatMain.getModel().setValueAt(pet.baseStats[i], i, 1);
            jTableStatMain.getModel().setValueAt(pet.comboList[i], i, 2);
            jTableStatMain.getModel().setValueAt("<html><b><font color=\"red\">" + formatNumber(pet.statfixRune[i]) + "</font></b></html>", i, 3);
        }
    }

    public static RuneSet curRuneSet;

    boolean petLoading = false;

    public void loadOnePet(String petName) {
        if (petLoading) {
            return;
        }
        //System.out.println("Load one pet : " + petName);

        if (petName == null) {
            petLoading = false;
            return;
        }
        if (SwManager.getPet(petName) == null) {
            petLoading = false;
            return;
        }
        petLoading = true;

        if (!initOk) {
            petLoading = false;
            return;
        }

        //textMainPet.setText(petName);
        PetType pet = SwManager.getInstance().searchPets(petName);

        final String oname = pet.a_name;
        (new Thread() {
            public void run() {
                //jLabelIcon.setText("");
                //System.out.println("petName Picture : [" + oname + "]");
                BufferedImage bf = Crawler.crawlPetPicture(oname);
                if (bf != null) {
                    jLabelIcon.setSize(bf.getWidth(), bf.getHeight());
                    jLabelIcon.setIcon(new ImageIcon(bf));
                }
            }
        }).start();

        curPet = pet;
        //System.out.println("Curpet : " + curPet.name);
        loadPetSetting();
        if (curPet.skillItem.skillMulty == null) {
            curPet.skillItem.skillMulty = "(ATK*3)";
            curPet.skillItem.skillName = "Unknown";
        }

        //System.out.println("Curpet mainskill : " + ConfigInfo.getInstance().petMaps.get(petName).mainSkill);

        clearTable(jTableCurRuneOptimized);
        clearTable(jTablePetStatOptmized);

        String s2 = "Pet : " + pet + "\n";
        curRuneSet = pet.currentEquip;
        for (RuneType r1 : pet.currentEquip.set) {
            if (r1 != null)
                s2 += "\n" + r1.displayGui();
        }
        s2 += "\n\n Runeset : " + pet.currentEquip.runeSets + "  ; guildWar dmg = " + pet.currentEquip.finalDamage() + " ( leader : " + curPet.leader_skill + " ) ; normal Dmg(Toa,Arena) " + pet.currentEquip.finalDamage(0, 0, 0);
        s2 += "\n Final : " + pet.statfixMap;

        jTextCurrentRune.setText(s2);

        //System.out.println("Curpet mainskill : " + ConfigInfo.getInstance().petMaps.get(petName).mainSkill);

        displayStatMain(jTableStatMain, curPet);

        if (ConfigInfo.getInstance().favourite.contains(petName)) {
            jCheckFavourite.setSelected(true);
        } else {
            jCheckFavourite.setSelected(false);
        }
        applyCurrentRuneMain();

        jCheckFavourite.setSelected(true);
        ConfigInfo.getInstance().favourite.add(curPet.name);
        ConfigInfo.getInstance().lastPet = petName;

        ConfigInfo cf = ConfigInfo.getInstance();
        ConfigInfo.PetSetting cp = cf.petMaps.get(curPet.name);

        //System.out.println("Load pet : " + cp.petName + " ; " + cp.isSaved + " ; mainSkill : " + cp.mainSkill);
        if (cp != null && cp.isSaved) {
            loadOptimizedRune(getRuneSet(cp.buildUniqueId));
        }
        curPetSetting = cp;

        for (int i = 0; i < jComboFavourite.getItemCount(); i++) {
            if (jComboFavourite.getItemAt(i).equalsIgnoreCase(petName)) {
                jComboFavourite.setSelectedIndex(i);
            }
        }
        for (int i = 0; i < jComboPets.getItemCount(); i++) {
            if (jComboPets.getItemAt(i).toString().equalsIgnoreCase(petName)) {
                jComboPets.setSelectedIndex(i);
            }
        }

        //System.out.println("*******Done load pet : " + cp.mainSkill + "************");
        petLoading = false;
        this.setTitle(oTitle + " - " + curPet.full_name + " - lv_" + curPet.level + " ; id_" + curPet.id + " ; master_id : " + curPet.master_id);
        jFrameResults.setVisible(false);
        ((DefaultTableModel) jTableResults.getModel()).setNumRows(0);
    }

    public static PetType curPet = null;
    PetSetting curPetSetting = null;

    private void jComboPetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboPetsActionPerformed
        // TODO add your handling code here:
        //System.out.println("Came here comboPet : " + evt.getID() + "; " + jComboPets.getSelectedIndex() + " ; " + jComboPets.getSelectedItem());

        if (jComboPets.getSelectedItem() != null) {
            String petName = jComboPets.getSelectedItem().toString();
            loadOnePet(petName);
        }
    }//GEN-LAST:event_jComboPetsActionPerformed

    int paraValue = 0;
    String paraRunes = "";
    String mainSet = "";
    JCheckBox[] checkBoxFilters = new JCheckBox[RuneType.slabels.length];

    //preoptimize; pre optimize
    void updateTotalPermutation() {
        //System.out.println("*************updateTotalPermutation***********");
        paraRunes = "";
        paraValue = 0;
        excludeList.clear();

        loadIncludedList(jTextIncludeRunes.getText());
        if (jCheckExcludeLocked.isSelected()) {
            RunePermutation.excludeRunes(getLockedList());
        }
        RunePermutation.excludeRunes(getExcludedList(jTextExcludeRunes.getText()));

        int j = 0;
        int count2 = 0;
        paraValue = Integer.parseInt(jTextFilterValue.getText());
        RunePermutation.useStorage = jCheckOnlyStorge.isSelected();
        RunePermutation.noBrokenSet = jCheckNoBroken.isSelected();

        for (JCheckBox jc : checkBoxFilters) {
            if (jc.isSelected()) {
                paraRunes += "," + RuneType.slabels[j];
                count2++;
            }
            j++;
        }
        if (count2 == 1) {
            paraValue = 0;
        }

        RunePermutation.slotData[0] = RunePermutation.updateSlotData(jComboSlot2);
        RunePermutation.slotData[1] = RunePermutation.updateSlotData(jComboSlot4);
        RunePermutation.slotData[2] = RunePermutation.updateSlotData(jComboSlot6);

        RuneType.RuneSet.exceptPetRunes = jTextLocks.getText() + "," + jTextGlobalLocks.getText();
        mainSet = "" + jComboMainRune.getSelectedItem();
        if (jComboDouble.getSelectedIndex() > 0 && !"All Broken".equals(mainSet)) {
            mainSet += "" + jComboDouble.getSelectedItem();
        }

        if (tSecondRuneSet.getText().trim().length() > 0 && !"All Broken".equals(mainSet)) {
            mainSet += "," + tSecondRuneSet.getText().trim();
        }
        RunePermutation.preOptimizeGui(mainSet, paraRunes, paraValue);

        jTextAllPermus.setValue(RunePermutation.preCalc);
        jTextRuneProcess.setValue(RunePermutation.preRunes);
    }

    private void jCheckSpdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckSpdActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jCheckSpdActionPerformed

    private void jCheckCritActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckCritActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jCheckCritActionPerformed

    private void jCheckHPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckHPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckHPActionPerformed

    private void jCheckDefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckDefActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckDefActionPerformed

    private void jTextLocksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextLocksActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextLocksActionPerformed

    private void jCheckCritStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckCritStateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_jCheckCritStateChanged

    private void jCheckSpdStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckSpdStateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_jCheckSpdStateChanged

    private void jButtonGWSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGWSettingActionPerformed
        // TODO add your handling code here:
        loadConfig();
        jDialogGlorySetting.pack();
        jDialogGlorySetting.setVisible(true);
        //jFrame1.setVisible(true);
    }//GEN-LAST:event_jButtonGWSettingActionPerformed

    private void tGlorySpdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tGlorySpdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tGlorySpdActionPerformed

    private void tWarAtkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tWarAtkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tWarAtkActionPerformed

    private void tWarDefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tWarDefActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tWarDefActionPerformed

    private void tWarCdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tWarCdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tWarCdActionPerformed

    private void tWarHpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tWarHpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tWarHpActionPerformed

    //save Button
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if (curPet == null) {
            return;
        }

        boolean needReload = false;
        if (jComboPetLevel.getSelectedIndex() != ConfigInfo.getInstance().loadPetLevel) {
            needReload = true;
        }

        String oldPet = curPet.name;
        saveConfig();
        jDialogGlorySetting.setVisible(false);

        if (needReload) {
            SwManager.getInstance().loadPets("optimizer.json");
            loadData();
        }
        loadOnePet(oldPet);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tGloryHPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tGloryHPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tGloryHPActionPerformed

    private void tGloryCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tGloryCDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tGloryCDActionPerformed

    private void tGloryDEFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tGloryDEFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tGloryDEFActionPerformed

    private void tGloryATKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tGloryATKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tGloryATKActionPerformed

    private void tGloryWindAtkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tGloryWindAtkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tGloryWindAtkActionPerformed

    private void tGloryWaterAtkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tGloryWaterAtkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tGloryWaterAtkActionPerformed

    private void tGloryFireAtkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tGloryFireAtkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tGloryFireAtkActionPerformed

    private void jComboMainRuneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboMainRuneActionPerformed
        // TODO add your handling code here:
        validateRuneDouble();
    }//GEN-LAST:event_jComboMainRuneActionPerformed

    private void jCheckDefStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckDefStateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_jCheckDefStateChanged

    void loadPetSetting() {
        ConfigInfo cf = ConfigInfo.getInstance();
        ConfigInfo.PetSetting cp = cf.petMaps.get(curPet.name);
        if (cp == null) {
            cf.petMaps.put(curPet.name, new ConfigInfo.PetSetting());
            cp = cf.petMaps.get(curPet.name);
            //System.out.println("Create new pet Setting : " + cp.petName);
        }
        //System.out.println("Load pet : mainSKill : " + cp.mainSkill + " ; " + cp.petName);

        cp.petName = curPet.name;
        if (cp != null) {
            //System.out.println("Load : " + cp.mainSet + " ; finalOptimize = " + cp.finalOptimize + " ; " + jComboOptimizeFinal.getItemCount());

            jComboMainRune.setSelectedIndex(cp.mainSet);
            tSecondRuneSet.setText(cp.secondSet);
            jComboOptimizeFinal.setSelectedIndex(cp.finalOptimize);

            jCheckAcc.setSelected(cp.filterAcc);
            jCheckRes.setSelected(cp.filterRes);
            jCheckHP.setSelected(cp.filterHp);
            jCheckDef.setSelected(cp.filterDef);
            jCheckCrit.setSelected(cp.filterCr);
            jCheckCritDmg.setSelected(cp.filterCDmg);
            jCheckAtk.setSelected(cp.filterAtk);
            jCheckSpd.setSelected(cp.filterSpd);

            jCheckNemesis.setSelected(cp.haveNemesis);
            jCheckWill.setSelected(cp.haveWill);
            jCheckShield.setSelected(cp.haveShield);
            jCheckRevenge.setSelected(cp.haveRevenge);

            tFirstValue.setValue(cp.firstFilterValue);
            firstFilterVar.setSelectedIndex(cp.firstFilterCombo);

            tSecondValue.setValue(cp.secondFilterValue);
            secondFilterVar.setSelectedIndex(cp.secondFilterCombo);
            if (cp.secondFilterCombo == 0) {
                tSecondValue.setValue(0);
            }

            tThirdValue.setValue(cp.thirdFilterValue);
            thirdFilterVar.setSelectedIndex(cp.thirdFilterCombo);
            if (cp.thirdFilterCombo == 0) {
                tThirdValue.setValue(0);
            }

            tFourthValue.setValue(cp.fourthFilterValue);
            fourthFilterVar.setSelectedIndex(cp.fourthFilterCombo);
            if (cp.fourthFilterCombo == 0) {
                tFourthValue.setValue(0);
            }

            tFifthValue.setValue(cp.fifthFilterValue);
            fifthFilterVar.setSelectedIndex(cp.fifthFilterCombo);
            if (cp.fifthFilterCombo == 0) {
                tFifthValue.setValue(0);
            }

            jTextFilterValue.setValue(cp.runeFilterValue);
            jTextLocks.setText(cp.petsLock);

            tLeaderSkill.setValue(cp.leaderBuff);
            curPet.leader_skill = cp.leaderBuff;
            curPet.atk_leader = getInt(tLeaderSkill);
            curPet.def_leader = 0;
            if (curPet.defDame) {
                curPet.def_leader = getInt(tLeaderSkill);
                curPet.atk_leader = 0;
            }

            jCheckOnlyStorge.setSelected(cp.useOnlyStorage);
            jCheckLockedBuild.setSelected(cp.isLocked);
            jCheckExcludeLocked.setSelected(cp.excludedLocked);

            jComboSlot2.setSelectedIndex(cp.slot246[0]);
            jComboSlot4.setSelectedIndex(cp.slot246[1]);
            jComboSlot6.setSelectedIndex(cp.slot246[2]);
            jComboDouble.setSelectedIndex(cp.mainDouble);

            jTextExcludeRunes.setText(genExcludedListText(cp.excludeRunes));
            jTextIncludeRunes.setText(genExcludedListText(cp.includeRunes));

            jComboSkill.removeAllItems();
            int k1 = 1;
            for (RuneSkill k : curPet.skillList) {
                jComboSkill.addItem("" + k1 + ":" + k.skillName);
                k1++;
            }
            //System.out.println("cp.mainSkill : " + cp.mainSkill);
            if (cp.mainSkill == 0) {
                //System.out.println("Main Skill is not set !");
                jComboSkill.setSelectedIndex(curPet.skillList.size() - 1);
            } else {
                jComboSkill.setSelectedIndex(cp.mainSkill - 1);
            }
            jTextSkillMulty.setText(formatSkill(curPet.skillItem.skillMulty));

            jCheckNoBroken.setSelected(cp.noBroken);
            jCheckGuildWars.setSelected(cp.guildWars);
        }
    }

    public static int getInt(JFormattedTextField text) {
        try {
            // TODO add your handling code here:
            text.commitEdit();
        } catch (ParseException ex) {
            //ex.printStackTrace();
            return 0;
        }
        if (text.getValue() != null) {
            return ((Number) text.getValue()).intValue();
        }
        return 0;
    }

    public String genExcludedListText(Set<String> l) {
        String res = "";
        for (String s1 : l) {
            RuneType r1 = SwManager.runesIds.get(s1);
            if (r1 != null) {
                res += "," + r1.id;
            }
        }
        //System.out.println("genExcludedListText : " + res);
        return res;
    }

    public Set<String> genExcludedList(String l) {
        Set<String> list = new HashSet<>();
        String[] ss = l.split(",");
        for (String s : ss) {
            if (s.trim().length() > 0) {
                int t2 = Integer.parseInt(s.trim());
                RuneType r1 = SwManager.runesSimpleIds.get(t2);
                if (r1 != null) {
                    list.add(r1.uniqueId);
                }
            }
        }
        //System.out.println("Excluded list : " + list);
        return list;
    }

    void savePetSetting() {
        ConfigInfo cf = ConfigInfo.getInstance();
        PetSetting cp = curPetSetting;

        cp.mainSet = jComboMainRune.getSelectedIndex();
        cp.secondSet = (String) tSecondRuneSet.getText();
        cp.finalOptimize = jComboOptimizeFinal.getSelectedIndex();

        cp.haveNemesis = jCheckNemesis.isSelected();
        cp.haveRevenge = jCheckRevenge.isSelected();
        cp.haveWill = jCheckWill.isSelected();
        cp.haveShield = jCheckShield.isSelected();

        cp.filterCDmg = jCheckCritDmg.isSelected();
        cp.filterSpd = jCheckSpd.isSelected();
        cp.filterCr = jCheckCrit.isSelected();
        cp.filterDef = jCheckDef.isSelected();
        cp.filterHp = jCheckHP.isSelected();
        cp.filterRes = jCheckRes.isSelected();
        cp.filterAcc = jCheckAcc.isSelected();
        cp.filterAtk = jCheckAtk.isSelected();

        cp.excludeRunes = genExcludedList(jTextExcludeRunes.getText());
        cp.includeRunes = genExcludedList(jTextIncludeRunes.getText());

        cp.firstFilterCombo = firstFilterVar.getSelectedIndex();
        cp.firstFilterValue = getInt(tFirstValue);

        cp.secondFilterCombo = secondFilterVar.getSelectedIndex();
        cp.secondFilterValue = getInt(tSecondValue);

        cp.thirdFilterCombo = thirdFilterVar.getSelectedIndex();
        cp.thirdFilterValue = getInt(tThirdValue);

        cp.fourthFilterCombo = fourthFilterVar.getSelectedIndex();
        cp.fourthFilterValue = getInt(tFourthValue);

        cp.fifthFilterCombo = fifthFilterVar.getSelectedIndex();
        cp.fifthFilterValue = getInt(tFifthValue);

        cp.isLocked = jCheckLockedBuild.isSelected();
        cp.excludedLocked = jCheckExcludeLocked.isSelected();

        cp.leaderBuff = getInt(tLeaderSkill);
        cp.runeFilterValue = Integer.parseInt(jTextFilterValue.getText());

        cp.slot246[0] = jComboSlot2.getSelectedIndex();
        cp.slot246[1] = jComboSlot4.getSelectedIndex();
        cp.slot246[2] = jComboSlot6.getSelectedIndex();

        cp.mainDouble = jComboDouble.getSelectedIndex();
        cp.useOnlyStorage = jCheckOnlyStorge.isSelected();
        cp.petsLock = jTextLocks.getText();
        cf.petMaps.put(curPet.name, cp);

        cp.mainSkill = jComboSkill.getSelectedIndex() + 1;

        cp.noBroken = jCheckNoBroken.isSelected();
        cp.guildWars = jCheckGuildWars.isSelected();

        cf.saveFile();
    }

    public void updateResultsTable() {

    }

    RuneSet curGoodSet = new RuneSet();
    long timerCount = 0;
    List<RuneSet> displayRuneList = new ArrayList();

    public void updateOptimizeResults() {
        //jTableResults;
        ((DefaultTableModel) jTableResults.getModel()).setNumRows(0);
        jTableResults.getColumnModel().getColumn(0).setPreferredWidth(40);
        jTableResults.getColumnModel().getColumn(1).setPreferredWidth(140);
        jTableResults.getColumnModel().getColumn(2).setPreferredWidth(120);
        DefaultTableModel model = (DefaultTableModel) jTableResults.getModel();
        int numCol = jTableResults.getColumnModel().getColumnCount();
        jTableResults.getColumnModel().getColumn(numCol - 1).setHeaderValue(jComboOptimizeFinal.getSelectedItem());
        jTableResults.setRowHeight(30);

        int i = 0;
        displayRuneList.clear();

        for (RuneSet r1 : RunePermutation.resultTreeRunes) {
            displayRuneList.add(r1);
            model.addRow(new Object[numCol]);
            jTableResults.getModel().setValueAt(i + 1, i, 0);
            jTableResults.getModel().setValueAt(r1.runeSets, i, 1);
            jTableResults.getModel().setValueAt(r1.mainStat, i, 2);
            jTableResults.getModel().setValueAt(r1.effectiveHP(), i, 3);
            jTableResults.getModel().setValueAt(r1.pet_spd, i, 4);
            jTableResults.getModel().setValueAt(r1.pet_cr, i, 5);
            jTableResults.getModel().setValueAt(r1.pet_hp, i, 6);
            jTableResults.getModel().setValueAt(r1.pet_def, i, 7);
            jTableResults.getModel().setValueAt(r1.pet_acc, i, 8);

            jTableResults.getModel().setValueAt(r1.bestValue, i, numCol - 1);
            jTableResults.getModel().setValueAt(r1.removeCost, i, numCol - 2);
            i++;
        }
    }

    ActionListener updateClockAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            //System.out.println("Say hello : "+RunePermutation.totalCount);
            jTextAllPermus.setValue(RunePermutation.totalCount);
            jProgressBar.setStringPainted(true);
            jProgressBar.setString((System.currentTimeMillis() - RunePermutation.startTime) / 1000 + " s");

            jTextCurBest.setValue(RunePermutation.allBest);
            setTitle("" + jProgressBar.getValue() * 100 / jProgressBar.getMaximum() + " % ; " + jProgressBar.getString() + " ; " + RunePermutation.estimateTime);

            //System.out.println("curBestRuneSet : "+curBestRuneSet);
            //System.out.println("curGoodSet : "+curGoodSet);
            jTextFoundSet.setValue(RunePermutation.foundBest);

            if (curBestRuneSet != null && curBestRuneSet != curGoodSet) {
                curGoodSet = RunePermutation.curBestRuneSet;
                loadOptimizedRune(curGoodSet);
            }
            timerCount++;
            if (timerCount % 2 == 0) {
                //updateOptimizeResults();
            }
        }
    };

    void actionFilterVar(javax.swing.JComboBox<String> firstFilterVar,
                         javax.swing.JFormattedTextField tFirstValue) {
        if (firstFilterVar.getSelectedIndex() > 0) {
            String s2 = firstFilterVar.getSelectedItem().toString();
            if (s2.contains(">=")) {
                final String s1 = s2.replace(">=", "").trim();
                final int d1 = getInt(tFirstValue);
                //System.out.println("Add filter : " + s1 + ">=" + d1 + " parasize : " + RunePermutation.paraRuneList.size());
                RunePermutation.paraRuneList.add(x -> (x.getPetValue(s1) >= d1));
            }
            if (s2.contains("<=")) {
                final String s1 = s2.replace("<=", "").trim();
                final int d1 = getInt(tFirstValue);
                //System.out.println("Add filter : " + s1 + "<=" + d1 + " parasize : " + RunePermutation.paraRuneList.size());
                RunePermutation.paraRuneList.add(x -> (x.getPetValue(s1) <= d1));
            }
            if (s2.contains("==")) {
                final String s1 = s2.replace("==", "").trim();
                final int d1 = getInt(tFirstValue);
                //System.out.println("Add filter : " + s1 + "==" + d1 + " parasize : " + RunePermutation.paraRuneList.size());
                RunePermutation.paraRuneList.add(x -> (x.getPetValue(s1) == d1));
            }
        }
    }

    public void addLog(String text) {
        jTextOutput.setText(jTextOutput.getText() + text + "\n");
    }

    public void updateOptimizedRuneStats(JTable jTablePetStatOptmized, RuneSet rs) {
        setupTable(jTablePetStatOptmized);
        jTablePetStatOptmized.getColumnModel().getColumn(0).setPreferredWidth(150);
        jTablePetStatOptmized.getColumnModel().getColumn(1).setPreferredWidth(80);
        //jTablePetStatOptmized.getColumnModel().getColumn(2).setPreferredWidth(20);
        for (int i = 0; i < RuneType.slabels.length; i++) {
            jTablePetStatOptmized.getModel().setValueAt(RuneType.slabels[i].toUpperCase(), i, 0);
            jTablePetStatOptmized.getModel().setValueAt(formatNumber(rs.statfixRune[i]), i, 1);
            jTablePetStatOptmized.getModel().setValueAt(formatPlus(rs.statfixRune[i] - curRuneSet.statfixRune[i]), i, 2);
        }

        jTablePetStatOptmized.getModel().setValueAt("dmg", 8, 0);
        jTablePetStatOptmized.getModel().setValueAt(formatNumber(rs.finalDamage(0, 0, 0)), 8, 1);
        jTablePetStatOptmized.getModel().setValueAt(formatPlus(rs.finalDamage(0, 0, 0) - curRuneSet.finalDamage(0, 0, 0)), 8, 2);
        
        /*jTablePetStatOptmized.getModel().setValueAt("effectHP", 8, 0);
        jTablePetStatOptmized.getModel().setValueAt(formatNumber(rs.effectiveHP()), 8, 1);
        jTablePetStatOptmized.getModel().setValueAt("<html><b><font color=\"orange\">"
                + rs.runeSets + "</font></b></html>", 9, 0);*/
    }

    public void loadOptimizedRune(RuneSet rs) {
        //System.out.println("loadOptimizedRune : " + rs);
        if (rs == null) {
            return;
        }
        rs.equipOnPet(curPet);

        Function<RuneSet, Integer> bestPara = RunePermutation.detectBestPara((String) jComboOptimizeFinal.getSelectedItem());
        displayRune2Table(rs, jTableCurRuneOptimized);

        jTableCurRuneOptimized.setRowHeight(8, 25);
        for (int i = 0; i < 6; i++) {
            ImageIcon img = new ImageIcon(scaleImage(getResImg("lock2"), 0.8));
            JLabel label = new JLabel(img);
            label.setOpaque(true);

            String pet = "" + jTableCurRuneOptimized.getValueAt(7, i);
            pet = pet.replaceAll("\\<.*?>", "");

            if (!pet.contains("Storage") && !pet.contains(curPet.name)) {
                jTableCurRuneOptimized.getModel().setValueAt(label, 8, i);
            }
        }

        updateOptimizedRuneStats(jTablePetStatOptmized, rs);
        jTablePetStatOptmized.getModel().setValueAt("<html><b><font color=\"blue\">"
                + jComboOptimizeFinal.getSelectedItem().toString().toUpperCase() + "</font></b></html>", 10, 0);
        jTablePetStatOptmized.getModel().setValueAt("<html><b><font color=\"red\">"
                + formatNumber(bestPara.apply(rs)) + "</font></b></html>", 10, 1);
        jTablePetStatOptmized.getModel().setValueAt(formatPlus(bestPara.apply(rs) - bestPara.apply(curRuneSet)), 10, 2);
    }

    public void loadIncludedList(String l) {
        List<Integer> list = new ArrayList();
        String[] ss = l.split(",");
        for (String s : ss) {
            if (s.trim().length() > 0) {
                int t2 = Integer.parseInt(s.trim());
                list.add(t2);
            }
        }

        for (int i = 0; i < RunePermutation.includeRunes.length; i++) {
            RunePermutation.includeRunes[i] = -1;
            for (int i2 : list) {
                RuneType r1 = SwManager.runesSimpleIds.get(i2);
                //System.out.println("Found rune : "+r1);
                if (r1 != null && r1.slot == i) {
                    RunePermutation.includeRunes[i] = i2;
                }
            }
        }

        //System.out.println("included list : " + list + " ; " + Arrays.toString(RunePermutation.includeRunes));
    }

    public List<Integer> getExcludedList(String l) {
        List<Integer> list = new ArrayList();
        String[] ss = l.split(",");
        for (String s : ss) {
            if (s.trim().length() > 0) {
                int t2 = Integer.parseInt(s.trim());
                list.add(t2);
            }
        }
        //System.out.println("Excluded list : " + list);
        return list;
    }

    public List<Integer> getLockedList() {
        List<Integer> list = new ArrayList();
        for (PetSetting p1 : ConfigInfo.getInstance().petMaps.values()) {
            if (p1.isSaved && p1.isLocked && p1.lastOptimize != null && !p1.petName.equals(curPet.name)) {
                List<Integer> list2 = new ArrayList();
                for (String idu : p1.buildUniqueId) {
                    RuneType r1 = runesIds.get(idu);
                    if (r1 != null) {
                        list.add(r1.id);
                        list2.add(r1.id);
                    }
                    //System.out.println(idu+" : "+r1);
                }
                //System.out.println("Lock list : " + p1.petName + " : " + list2);
            }
        }
        //System.out.println("Locked list : " + list);
        return list;
    }

    void performOptimize() {
        // TODO add your handling code here:
        RunePermutation.excludeList.clear();
        if (jCheckExcludeLocked.isSelected()) {
            RunePermutation.excludeRunes(getLockedList());
        }
        jButtonOptimize.setEnabled(false);
        jButtonStop.setEnabled(true);
        jTextOutput.setText("Start optimize... \n");

        clearTable(jTableCurRuneOptimized);
        clearTable(jTablePetStatOptmized);
        RunePermutation.resultTreeRunes.clear();

        if (RunePermutation.useThreads) {
            jButtonOptimize.setText("Optimze (" + RunePermutation.numThreads + " threads)");
        }

        ConfigInfo.getInstance().globalLocks = jTextGlobalLocks.getText();

        jProgressBar.setStringPainted(true);
        jProgressBar.setString("");
        setTitle("0%");

        if (jComboFavourite.getSelectedItem() == null || !jComboFavourite.getSelectedItem().toString().equalsIgnoreCase(curPet.name)) {
            ConfigInfo.getInstance().favourite.add(curPet.name);
            petLoading = true;
            reloadFavouriteCombo();
            petLoading = false;
        }

        RunePermutation.pBar = jProgressBar;
        (new Thread() {
            public void run() {
                updateTotalPermutation();
                savePetSetting();
                addLog("LockPets : " + RunePermutation.lockPetLists);
                addLog("Total pets : " + SwManager.pets.size());
                addLog("Total runes : " + SwManager.runes.size() + "\n\n");

                String noFoundMsg = "No runeset found. Pls lower your filters ! \nCurrentFilter : " + paraValue
                        + " or rune level.\nCurrently Exclude all Rune level < " + ConfigInfo.getInstance().lowRuneLevel
                        + " (You can change in Setting)"

                        + "\nCheck Includes Runes ID : " + jTextIncludeRunes.getText()
                        + "\nExcludes Runes ID : " + jTextExcludeRunes.getText()
                        + "\nUse only storage Runes : " + jCheckOnlyStorge.isSelected()
                        + "\n---------------------------------------------------------"
                        + "\n In the filter this is final stat, if you set spd <= 60, "
                        + "\nthere no result,pet base speed are at least 90";
                if (RunePermutation.preCalc == 0) {
                    //System.out.println("preCalc = 0 . paraValue = " + paraValue);

                    jTextOutput.setText(jTextOutput.getText() + " \n No runeset found !");

                    JOptionPane.showMessageDialog(null, noFoundMsg, "Info", JOptionPane.ERROR_MESSAGE);

                    String found = "";
                    int count = 0;
                    for (JCheckBox jc : checkBoxFilters) {
                        if (jc.isSelected()) {
                            count++;
                            found = jc.getText();
                        }
                    }
                }

                if (RunePermutation.preCalc > 0) {

                    //jLabelIcon.setText("");
                    Timer t = new Timer(500, updateClockAction);
                    t.start();

                    List<Function<RuneType.RuneSet, Boolean>> l1 = RunePermutation.paraRuneList;
                    l1.clear();
                    if (jCheckWill.isSelected()) {
                        l1.add(x -> (x.will()));
                    }

                    if (jCheckRevenge.isSelected()) {
                        l1.add(x -> (x.rev()));
                    }
                    if (jCheckNemesis.isSelected()) {
                        l1.add(x -> (x.nemesis()));
                    }
                    if (jCheckShield.isSelected()) {
                        l1.add(x -> (x.shield()));
                    }

                    actionFilterVar(firstFilterVar, tFirstValue);
                    actionFilterVar(secondFilterVar, tSecondValue);
                    actionFilterVar(thirdFilterVar, tThirdValue);
                    actionFilterVar(fourthFilterVar, tFourthValue);
                    actionFilterVar(fifthFilterVar, tFifthValue);

                    String s1 = (String) jComboOptimizeFinal.getSelectedItem();
                    int t1 = Integer.parseInt(jTextFilterValue.getText());

                    curPet.isGuildWars = jCheckGuildWars.isSelected();

                    RunePermutation.perMuteGUI(mainSet, paraRunes, paraValue, s1);
                    PetType p = RuneType.RuneSet.runePet;

                    t.stop();
                    setTitle(oTitle + " - Done - " + curPet.name);
                    jProgressBar.setString(jProgressBar.getString() + " ; Done !");

                    if (RunePermutation.bestRuneSet != null) {

                        jTextOutput.setText("");
                        p.applyRuneSet(RunePermutation.bestRuneSet);
                        jTextOutput.setText(jTextOutput.getText() + RunePermutation.bestRuneSet.details());
                        jTextOutput.setText(jTextOutput.getText() + p.showPetRune());
                        loadOptimizedRune(RunePermutation.bestRuneSet);

                        jFrameResults.pack();
                        jFrameResults.setVisible(true);
                        updateOptimizeResults();

                        JOptionPane.showMessageDialog(null, "Done. Found the best runeSet !\n Click con Save Build button to save this Build \n"
                                + "Click con Buid Manage to view all saved Builds", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        jTextOutput.setText(jTextOutput.getText() + " \n No runeset found !");
                        JOptionPane.showMessageDialog(null, noFoundMsg, "Info", JOptionPane.ERROR_MESSAGE);
                    }

                }

                jTextOutput.setCaretPosition(jTextOutput.getDocument().getLength());

                jTextOutput.setText(jTextOutput.getText() + " \n Done !");
                jButtonOptimize.setEnabled(true);
                jButtonStop.setEnabled(false);
            }
        }).start();
    }

    //main optimize
    private void jButtonOptimizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptimizeActionPerformed
        RunePermutation.fullSearch = false;
        performOptimize();
    }//GEN-LAST:event_jButtonOptimizeActionPerformed

    void updateAllRuneFilterVal(JComboBox jc) {
        if (jc.getSelectedItem() == null) {
            return;
        }

        /*if (jc.getSelectedIndex() > 0 && jc.getSelectedIndex() <= RuneType.slabels.length) {
            checkBoxFilters[jc.getSelectedIndex() - 1].setSelected(true);
        }*/
        String s1 = jc.getSelectedItem().toString();
        for (int i = 0; i < RuneType.slabels.length; i++) {
            if (s1.contains(RuneType.slabels[i])) {
                checkBoxFilters[i].setSelected(true);
            }
        }
        if (s1.contains("spd")) {
            checkBoxFilters[SPD].setSelected(true);
        }
    }

    void updateAllRuneFilter() {
        for (JCheckBox jc : checkBoxFilters) {
            if (jc == null) {
                return;
            }
            jc.setSelected(false);
        }

        String s = (String) jComboOptimizeFinal.getSelectedItem();
        if (s == null) {
            return;
        }
        s = s.toLowerCase();
        int j = 0;

        updateAllRuneFilterVal(firstFilterVar);
        updateAllRuneFilterVal(secondFilterVar);
        updateAllRuneFilterVal(thirdFilterVar);
        updateAllRuneFilterVal(fourthFilterVar);
        updateAllRuneFilterVal(fifthFilterVar);

        if (s.equalsIgnoreCase("effectiveHP")) {
            jCheckDef.setSelected(true);
            jCheckHP.setSelected(true);
        }
        if (s.contains("hp"))
            jCheckHP.setSelected(true);
        if (s.contains("def"))
            jCheckDef.setSelected(true);
        if (s.contains("spd"))
            jCheckSpd.setSelected(true);
        if (s.contains("atk"))
            jCheckAtk.setSelected(true);
        if (s.contains("cd"))
            jCheckCritDmg.setSelected(true);

        if (s.equalsIgnoreCase("effectHp*spd")) {
            jCheckSpd.setSelected(true);
            jCheckHP.setSelected(true);
            jCheckDef.setSelected(true);
        }

        if (s.equalsIgnoreCase("hp bruiser")) {
            jCheckCrit.setSelected(true);
            jCheckCritDmg.setSelected(true);
            jCheckAtk.setSelected(true);
            jCheckSpd.setSelected(true);
            jCheckHP.setSelected(true);
            jCheckDef.setSelected(true);
        }
        if (s.equalsIgnoreCase("bruiser")) {
            jCheckCrit.setSelected(true);
            jCheckCritDmg.setSelected(true);
            jCheckAtk.setSelected(true);
            jCheckSpd.setSelected(true);
            jCheckHP.setSelected(true);
            jCheckDef.setSelected(true);
        }
        if (s.equalsIgnoreCase("spd bruiser")) {
            jCheckCrit.setSelected(true);
            jCheckCritDmg.setSelected(true);
            jCheckAtk.setSelected(true);
            jCheckSpd.setSelected(true);
            jCheckHP.setSelected(true);
            jCheckDef.setSelected(true);
        }
        if (s.equalsIgnoreCase("pve dd")) {
            jCheckCrit.setSelected(true);
            jCheckCritDmg.setSelected(true);
            jCheckAtk.setSelected(true);
            jCheckSpd.setSelected(true);
            jCheckHP.setSelected(true);
            jCheckDef.setSelected(true);
        }
        if (s.equalsIgnoreCase("pve def dd")) {
            jCheckCrit.setSelected(true);
            jCheckCritDmg.setSelected(true);
            jCheckSpd.setSelected(true);
            jCheckHP.setSelected(true);
            jCheckDef.setSelected(true);
        }
        if (s.equalsIgnoreCase("pvp tank")) {
            jCheckSpd.setSelected(true);
            jCheckHP.setSelected(true);
            jCheckDef.setSelected(true);
        }
        if (s.equalsIgnoreCase("tank")) {
            jCheckSpd.setSelected(true);
            jCheckHP.setSelected(true);
            jCheckDef.setSelected(true);
        }
        if (s.equalsIgnoreCase("speed")) {
            jCheckSpd.setSelected(true);
            jCheckHP.setSelected(true);
            jCheckDef.setSelected(true);
        }
        if (s.equalsIgnoreCase("speed hp")) {
            jCheckSpd.setSelected(true);
            jCheckHP.setSelected(true);
            jCheckDef.setSelected(true);
        }

        if (curPet != null && (s.equalsIgnoreCase("finalDamage") || s.contains("dmg"))) {
            //System.out.println("curPet : " + curPet.name);
            //System.out.println("Main Skill : " + curPet.skillItem + " ; Bomb : " + curPet.skillItem.isBomb + " ; curPet : " + curPet.name);

            if (curPet.skillItem.skillMulty == null) {
                curPet.skillItem.skillMulty = "(ATK*3)";
                curPet.skillItem.skillName = "Unknown";
            }

            if (curPet.skillItem.skillMulty.contains("ATK")) {
                jCheckAtk.setSelected(true);
            }
            if (curPet.skillItem.skillMulty.contains("DEF")) {
                jCheckDef.setSelected(true);
            }
            if (curPet.skillItem.skillMulty.contains("SPEED")) {
                jCheckSpd.setSelected(true);
            }

            if (!curPet.skillItem.isBomb) {
                jCheckCrit.setSelected(true);
                jCheckCritDmg.setSelected(true);
            }
            /*if (curPet.isBomb) {
                jCheckAtk.setSelected(true);
            } else if (curPet.defDame) {
                jCheckDef.setSelected(true);
                jCheckCrit.setSelected(true);
                jCheckCritDmg.setSelected(true);
            } else if (curPet.hpDame) {
                jCheckHP.setSelected(true);
                jCheckCrit.setSelected(true);
                jCheckCritDmg.setSelected(true);
            } else {
                jCheckAtk.setSelected(true);
                jCheckCrit.setSelected(true);
                jCheckCritDmg.setSelected(true);
            }*/
        }

        for (String s1 : RuneType.slabels) {
            if (s.equalsIgnoreCase(s1)) {
                //jCheckSpd.setSelected(true);
                checkBoxFilters[j].setSelected(true);
            }
            j++;
        }

    }

    private void jComboOptimizeFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboOptimizeFinalActionPerformed
        // TODO add your handling code here:
        if (curPet != null) {
            updateAllRuneFilter();
        }
    }//GEN-LAST:event_jComboOptimizeFinalActionPerformed

    private void jButtonCheckRunesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckRunesActionPerformed
        // TODO add your handling code here:
        updateTotalPermutation();
    }//GEN-LAST:event_jButtonCheckRunesActionPerformed

    private void jCheckCritDmgStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckCritDmgStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckCritDmgStateChanged

    private void jCheckCritDmgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckCritDmgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckCritDmgActionPerformed

    private void jCheckRevengeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckRevengeStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckRevengeStateChanged

    private void jCheckRevengeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckRevengeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckRevengeActionPerformed

    private void jCheckWillStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckWillStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckWillStateChanged

    private void jCheckWillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckWillActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckWillActionPerformed

    private void jCheckAtkStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckAtkStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckAtkStateChanged

    private void jCheckAtkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckAtkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckAtkActionPerformed

    private void jCheckShieldStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckShieldStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckShieldStateChanged

    private void jCheckShieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckShieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckShieldActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        System.out.println("Window closed!");
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        System.out.println("Closing");
        ConfigInfo.getInstance().saveFile();
    }//GEN-LAST:event_formWindowClosing

    private void jCheckFavouriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckFavouriteActionPerformed
        // TODO add your handling code here:
        String name = curPet.name;
        Set<String> s1 = ConfigInfo.getInstance().favourite;
        if (jCheckFavourite.isSelected()) {
            s1.add(curPet.name);
        } else {
            s1.remove(curPet.name);
        }

        reloadFavouriteCombo();
        loadOnePet(name);
        System.out.println("Add favourite : " + ConfigInfo.getInstance().favourite);
    }//GEN-LAST:event_jCheckFavouriteActionPerformed

    private void jComboFavouriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboFavouriteActionPerformed
        // TODO add your handling code here:
        System.out.println("Jfavourite come here !" + jComboFavourite.getSelectedItem());
        if (jComboFavourite.getSelectedItem() != null) {
            String petName = jComboFavourite.getSelectedItem().toString();
            loadOnePet(petName);
        }
    }//GEN-LAST:event_jComboFavouriteActionPerformed

    private void jButtonStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStopActionPerformed
        // TODO add your handling code here:
        RunePermutation.fullStop = true;
        System.out.println("Press stop button !");
    }//GEN-LAST:event_jButtonStopActionPerformed

    private void jCheckThreadsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckThreadsActionPerformed
        // TODO add your handling code here:
        if (jCheckThreads.isSelected()) {
            jButtonOptimize.setText("Optimze (" + RunePermutation.numThreads + " threads)");
            RunePermutation.useThreads = true;
        } else {
            jButtonOptimize.setText("Optimze");
            RunePermutation.useThreads = false;
        }
        ConfigInfo.getInstance().useThreads = RunePermutation.useThreads;
    }//GEN-LAST:event_jCheckThreadsActionPerformed

    private void textNumThreadsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textNumThreadsActionPerformed
        // TODO add your handling code here:
        RunePermutation.numThreads = Integer.parseInt(textNumThreads.getText());
        ConfigInfo.getInstance().numThreads = RunePermutation.numThreads;
        if (jCheckThreads.isSelected()) {
            jButtonOptimize.setText("Optimze (" + RunePermutation.numThreads + " threads)");
            RunePermutation.useThreads = true;
        }
    }//GEN-LAST:event_textNumThreadsActionPerformed

    private void jCheckResActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckResActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckResActionPerformed

    private void jCheckAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckAccActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckAccActionPerformed

    private void firstFilterVarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstFilterVarActionPerformed
        // TODO add your handling code here:
        if (curPet != null) {
            updateAllRuneFilter();
        }
    }//GEN-LAST:event_firstFilterVarActionPerformed

    private void secondFilterVarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_secondFilterVarActionPerformed
        // TODO add your handling code here:
        if (curPet != null) {
            updateAllRuneFilter();
        }
    }//GEN-LAST:event_secondFilterVarActionPerformed

    private void thirdFilterVarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thirdFilterVarActionPerformed
        // TODO add your handling code here:
        if (curPet != null) {
            updateAllRuneFilter();
        }
    }//GEN-LAST:event_thirdFilterVarActionPerformed

    private void tLeaderSkillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tLeaderSkillActionPerformed
        // TODO add your handling code here:
        //System.out.println("Came here ! tLeaderSkillActionPerformed");
        if (curPet != null) {
            savePetSetting();

            //System.out.println("Came here pet ! " + curPetSetting.mainSkill);
            loadOnePet(curPet.name);
        }
    }//GEN-LAST:event_tLeaderSkillActionPerformed

    private void tLeaderSkillPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tLeaderSkillPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_tLeaderSkillPropertyChange

    private void tLeaderSkillFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tLeaderSkillFocusLost

        //System.out.println("Focus lost : " + getInt(tLeaderSkill) + " ; " + curPet.leader_skill);
        if (getInt(tLeaderSkill) != curPet.leader_skill) {
            curPet.leader_skill = getInt(tLeaderSkill);
            savePetSetting();
            loadOnePet(curPet.name);
        }
    }//GEN-LAST:event_tLeaderSkillFocusLost

    private void jCheckNemesisStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckNemesisStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckNemesisStateChanged

    private void jCheckNemesisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckNemesisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckNemesisActionPerformed

    public static void drawImageScale(BufferedImage bf, BufferedImage legend, int x, int y, double scale) {
        bf.getGraphics().drawImage(legend, x, y, (int) (legend.getWidth() * scale), (int) (legend.getHeight() * scale), null);
    }

    public static BufferedImage tint2(BufferedImage image, Color color) {
        BufferedImage dt = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color pixelColor = new Color(image.getRGB(x, y), true);
                int r = (pixelColor.getRed() + color.getRed()) / 2;
                int g = (pixelColor.getGreen() + color.getGreen()) / 2;
                int b = (pixelColor.getBlue() + color.getBlue()) / 2;
                int a = pixelColor.getAlpha();
                int rgba = (a << 24) | (r << 16) | (g << 8) | b;

                //image.setRGB(x, y, rgba);
                dt.setRGB(x, y, rgba);
            }
        }
        return dt;
    }

    public static BufferedImage tint(BufferedImage loadImg, Color color) {
        BufferedImage img = new BufferedImage(loadImg.getWidth(), loadImg.getHeight(),
                BufferedImage.TRANSLUCENT);
        final float tintOpacity = 0.45f;
        Graphics2D g2d = img.createGraphics();

        //Draw the base image
        g2d.drawImage(loadImg, null, 0, 0);
        //Set the color to a transparent version of the input color
        g2d.setColor(new Color(color.getRed() / 255f, color.getGreen() / 255f,
                color.getBlue() / 255f, tintOpacity));

        //Iterate over every pixel, if it isn't transparent paint over it
        Raster data = loadImg.getData();
        for (int x = data.getMinX(); x < data.getWidth(); x++) {
            for (int y = data.getMinY(); y < data.getHeight(); y++) {
                int[] pixel = data.getPixel(x, y, new int[4]);
                if (pixel[3] > 0) { //If pixel isn't full alpha. Could also be pixel[3]==255
                    g2d.fillRect(x, y, 1, 1);
                }
            }
        }
        g2d.dispose();
        return img;
    }

    public static BufferedImage scaleImage(BufferedImage background, double scale) {
        BufferedImage bf = new BufferedImage((int) (background.getWidth() * scale), (int) (background.getHeight() * scale), BufferedImage.TYPE_INT_ARGB);
        drawImageScale(bf, background, 0, 0, scale);
        return bf;
    }

    public static Map<String, BufferedImage> runeIconCache = new HashMap<>();

    public static BufferedImage getRuneIcon(RuneType r1) {
        int rune_quality = r1.subStatGuis.size();
        String iname = r1.runeType + "_" + r1.slot + "_" + r1.runeTypeIndex + "_" + rune_quality + "_" + r1.level + "_" + r1.grade;

        if (!runeIconCache.containsKey(iname)) {
            runeIconCache.put(iname, getRuneIcon2(r1));
        }
        return runeIconCache.get(iname);
    }

    public static BufferedImage getRuneIcon2(RuneType r1) {
        try {
            double scale = 0.8;
            String[] bg_files = {"normal", "magic", "rare", "hero", "legend"};

            int rune_quality = r1.subStatGuis.size();
            BufferedImage background = getResImg("bg_" + bg_files[rune_quality]);
            BufferedImage rune1 = getResImg("rune" + r1.slot);
            BufferedImage violent = getResImg(RuneType.setBonnusLabel[r1.runeTypeIndex].toLowerCase());
            BufferedImage star1 = getResImg("star-unawakened");
            BufferedImage star2 = getResImg("star-awakened");
            //bf.getGraphics().fillRect(0, 0, 80, 80);

            BufferedImage bf = new BufferedImage((int) (background.getWidth() * scale), (int) (background.getHeight() * scale), BufferedImage.TYPE_INT_ARGB);

            int x = 0;
            int y = 0;
            int w = bf.getWidth();
            int h = bf.getHeight();

            Graphics g = bf.getGraphics();

            drawImageScale(bf, background, x, y, scale);

            //g.drawImage(rune1, x+9, y+6, null);
            g.drawImage(rune1, x + w / 2 - rune1.getWidth() / 2, y + h / 2 - rune1.getHeight() / 2, null);

            Color[] tintColors = {new Color(255, 255, 255), new Color(5, 222, 105), new Color(80, 255, 237), new Color(255, 144, 255), new Color(250, 170, 81)};

            double d2 = 0.5;
            int[] offsets = {5, 0, 0, -2, 0, 0};
            int[] offsetsX = {0, -2, -2, 0, 2, 2};

            //System.out.println("Draw rune : "+r1+" ; "+rune_quality);
            if (violent != null && rune_quality >= 0) {
                drawImageScale(bf, tint2(violent, tintColors[rune_quality]), x + offsetsX[r1.slot - 1] + w / 2 - (int) (violent.getWidth() * d2 / 2), y + offsets[r1.slot - 1] + h / 2 - (int) (violent.getHeight() * d2 / 2), d2);
            }

            if (r1.level == 15) {
                star1 = star2;
            }

            for (int i = 0; i < r1.grade; i++) {
                drawImageScale(bf, star1, x + 2 + (int) (i * 8), y + 2, 0.5);
            }
            //Font myFont = new Font ("Courier New", 1, 10);
            g.setFont(new Font("default", Font.BOLD, 12));

            int of2 = 0;
            if (r1.level < 10) {
                of2 = 7;
            }

            if (r1.level > 0) {
                g.drawString("+" + r1.level, x + 36 + of2, y + 56);
            }

            return bf;

        } catch (Exception e) {
            System.out.println("Rune error : " + r1);
            e.printStackTrace();
        }
        return null;
    }

    public static void drawRune(RuneType r1, JLabel p1) {
        drawRune(r1, p1, false);
    }

    public static void drawRune(RuneType r1, JLabel p1, boolean showPowerUp) {
        try {
            if (p1.getWidth() == 0) {
                return;
            }

            BufferedImage bf = new BufferedImage(p1.getWidth(), p1.getHeight(), BufferedImage.TYPE_INT_ARGB);
            String[] bg_files = {"normal", "magic", "rare", "hero", "legend"};

            int rune_quality = r1.subStatGuis.size();
            BufferedImage background = getResImg("bg_" + bg_files[rune_quality]);
            BufferedImage rune1 = getResImg("rune1");
            BufferedImage violent = getResImg(RuneType.setBonnusLabel[r1.runeTypeIndex].toLowerCase());
            BufferedImage star1 = getResImg("star-unawakened");
            BufferedImage star2 = getResImg("star-awakened");

            //bf.getGraphics().fillRect(0, 0, 80, 80);
            double scale = 0.8;
            int x = 6;
            int y = 16;
            Graphics g = bf.getGraphics();

            /*drawImageScale(bf, background, x, y, scale);
            g.drawImage(rune1, x+9, y+6, null);

            Color magic = new Color(5,222,105);
            Color rare = new Color(80,255,237);
            Color hero = new Color(255,144,255);
            Color legend = new Color(250,170,81);


            Color[] tintColors = {null,new Color(5,222,105),new Color(80,255,237),new Color(255,144,255),new Color(250,170,81)};

            drawImageScale(bf,tint2(violent,tintColors[rune_quality]), x+15, y+19, 0.5);   */
            g.drawImage(getRuneIcon(r1), x, y, null);

            //Font myFont = new Font ("Courier New", 1, 10);
            g.setFont(new Font("default", Font.BOLD, 12));
            //g.drawString("+"+r1.level, x+36, y+56);

            g.setFont(new Font("default", Font.BOLD, 16));
            g.setColor(Color.BLUE);
            g.drawString(r1.mainStatGui + " +" + r1.mainStatVal, x + 68, y + 20);

            /*System.out.println("subStat1 : "+r1.subStat1);
            System.out.println("subStat2 : "+r1.subStat2);
            System.out.println("subStatGuis : "+r1.subStatGuis);
            System.out.println("grinds : "+r1.grinds);*/

            if (showPowerUp)
                g.setFont(new Font("default", Font.BOLD, 14));

            for (int i = 0; i < rune_quality; i++) {
                String s1 = r1.subStatGuis.get(i);
                g.setColor(Color.BLUE);
                s1 = s1.replace("CRt", "CRate");
                s1 = s1.replace("CDm", "CDmg");
                if (showPowerUp) {
                    //g.setColor(Color.RED);                    
                    int d2 = r1.getSingleRarity(i);
                    if (d2 > 0) {
                        s1 = s1 + " (" + (r1.subStat2.get(i) - r1.grinds.get(i)) + ")";
                        s1 = s1 + " (+" + d2 + ")";
                    }
                } else {
                    if (r1.grinds.get(i) > 0) {
                        g.setColor(Color.RED);
                        s1 = s1 + " (+" + r1.grinds.get(i) + ")";
                    }
                    if (r1.enchanted.get(i)) {
                        g.setColor(Color.MAGENTA);
                        s1 = s1 + " @";
                    }
                }
                g.drawString(s1, x + 5, y + 80 + i * 19);
            }

            g.setFont(new Font("default", Font.BOLD, 14));
            g.drawString(r1.optionStatGui, x + 68, y + 40);

            p1.setIcon(new ImageIcon(bf));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, BufferedImage> cachedImages = new HashMap();

    public static BufferedImage getResImg(String name) {
        if (name == null) {
            return null;
        }
        if (!cachedImages.containsKey(name)) {
            try {
                BufferedImage star2 = ImageIO.read(Application.class.getResourceAsStream("/" + name + ".png"));
                cachedImages.put(name, star2);
            } catch (Exception e) {
                System.out.println("Error name : " + name);
                e.printStackTrace();
            }
        }
        return cachedImages.get(name);
    }

    public static String addBold(String s) {
        s = "<html><b><font color=\"blue\">" + s;
        //s=s.replace("+", "</font></b></html>+");
        return s;
    }

    public static void setupTable(JTable table) {
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);

        int numCol = table.getColumnCount();
        for (int i = 0; i < numCol; i++) {
            table.getColumnModel().getColumn(i).setResizable(false);
        }
    }

    public String formatPlus(int num) {
        if (num >= 0) {
            return "+" + num;
        }
        return "" + num;
    }

    public static String formatNumber(long num) {
        return NumberFormat.getIntegerInstance().format(num);
    }

    public static String formatSkill(String skill) {
        if (skill == null) {
            return "";
        }
        skill = skill.replace(",*,", "*");
        skill = skill.replace(",+,", "+");
        skill = skill.replace(",(/),", "/");
        skill = skill.replace("ATTACK_TOT_", "");
        skill = skill.replace("ATTACK_SPEED", "SPD");
        skill = skill.replace("ATTACK_DEF", "DEF");
        skill = skill.replace("TARGET_CUR_HP_RATE", "eHP");
        skill = skill.replace("ATTACK_CUR_HP_RATE", "cHP");
        skill = skill.replace("TARGET_TOT_HP", "eHP");
        skill = skill.replace("(ATK*1.0)", "ATK");
        skill = skill.replace("100%*", "");
        skill = skill.replace(",/,", "/");

        if (skill.startsWith("(") && skill.endsWith(")")) {
            skill = skill.substring(1, skill.length() - 1);
        }
        return skill;
    }

    public static void clearTable(final JTable table) {
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                table.setValueAt("", i, j);
            }
        }
    }

    public static void displayRune2Table(RuneSet rs, JTable table) {
        if (rs == null) {
            System.out.println("Oops this runeset is Null");
            return;
        }
        //System.out.println("displayRune2Table: " + rs);
        setupTable(table);

        //jTableCurRuneMain.setRowHeight(1, 30);
        table.validate();

        DefaultTableModel model = ((DefaultTableModel) table.getModel());
        clearTable(table);

        table.setRowHeight(0, 60);
        for (int i = 0; i < 6; i++) {
            if (rs.set[i] == null || rs.set[i].slot <= 0 || rs.set[i].runeTypeIndex < 0) {
                continue;
            }
            table.getColumnModel().getColumn(i).setHeaderValue("id_" + rs.set[i].id);

            table.getColumnModel().getColumn(i).setCellRenderer(new IconRenderer2());
            table.getModel().setValueAt(addBold(" " + rs.set[i].mainStatGui + "+" + rs.set[i].futureMainStat), 1, i);
            table.getModel().setValueAt(new ImageIcon(getRuneIcon(rs.set[i])), 0, i);
            int k = 2;
            table.getModel().setValueAt(" " + rs.set[i].optionStatGui, 2, i);
            //jTableCurRune.getColumnModel().getColumn(i).setCellRenderer(jTableCurRune.getDefaultRenderer(String.class));
            for (String p1 : rs.set[i].subStatGuis) {
                k++;
                table.getModel().setValueAt(" " + p1.replace(" ", ""), k, i);
            }
            if (table.getRowCount() > 7) {
                table.getModel().setValueAt(addBold(rs.set[i].monster), 7, i);
            }
        }
        JTableHeader th = table.getTableHeader();
        th.repaint();
        table.invalidate();
    }

    public static void showPetFinalStats(PetType curPet, JTable jTableExtraPetInfo) {
        int k1 = 0;
        RuneSet.runePet = curPet;
        RuneType.RuneSet rs = curPet.currentEquip;
        setupTable(jTableExtraPetInfo);
        jTableExtraPetInfo.getColumnModel().getColumn(0).setPreferredWidth(50);
        jTableExtraPetInfo.getModel().setValueAt(curPet.name, k1++, 1);
        jTableExtraPetInfo.getModel().setValueAt(formatNumber(rs.finalDamage(0, 0, 0)), k1++, 1);
        jTableExtraPetInfo.getModel().setValueAt(formatNumber(rs.finalDamage()), k1++, 1);
        jTableExtraPetInfo.getModel().setValueAt(formatNumber(rs.effectiveHP()), k1++, 1);
        jTableExtraPetInfo.getModel().setValueAt(formatNumber(rs.pet_hp), k1++, 1);
        jTableExtraPetInfo.getModel().setValueAt(formatNumber(rs.pet_def), k1++, 1);
        jTableExtraPetInfo.getModel().setValueAt(rs.pet_spd, k1++, 1);
        jTableExtraPetInfo.getModel().setValueAt(rs.runeSets, k1++, 1);
        jTableExtraPetInfo.getModel().setValueAt(curPet.skillItem.skillName, k1++, 1);
        jTableExtraPetInfo.getModel().setValueAt(formatSkill(curPet.skillItem.skillMulty), k1++, 1);
    }

    public void applyCurrentRuneMain() {
        if (curPet != null) {
            RuneType.RuneSet rs = curPet.currentEquip;
            displayRune2Table(rs, jTableCurRuneMain);
            showPetFinalStats(curPet, jTableExtraPetInfo);
        }
    }

    public void applyCurrentRune() {
        //jTableCurRune.
        //jTableCurRune.setAutoResizeMode(JTable.au);
        if (curPet != null) {
            RuneType.RuneSet rs = curPet.currentEquip;
            jTableCurRune.setRowHeight(0, 60);
            jTableCurRune.setRowHeight(1, 40);

            for (int i = 0; i < 6; i++) {
                jTableCurRune.getColumnModel().getColumn(i).setCellRenderer(new IconRenderer2());
                jTableCurRune.getModel().setValueAt(rs.set[i].mainStatGui + " +" + rs.set[i].mainStatVal, 1, i);
                jTableCurRune.getModel().setValueAt(new ImageIcon(getRuneIcon(rs.set[i])), 0, i);
                int k = 1;
                //jTableCurRune.getColumnModel().getColumn(i).setCellRenderer(jTableCurRune.getDefaultRenderer(String.class));
                for (String p1 : rs.set[i].subStatGuis) {
                    k++;
                    jTableCurRune.getModel().setValueAt(p1, k, i);
                }
            }

            for (int i = 0; i < RuneType.slabelsMainDisplay.length; i++) {
                jTableCurRune2.getColumnModel().getColumn(i + 3).setHeaderValue(RuneType.slabelsMainDisplay[i]);
            }

            jTableCurRune2.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTableCurRune2.getColumnModel().getColumn(1).setPreferredWidth(60);
            jTableCurRune2.getColumnModel().getColumn(2).setPreferredWidth(80);
            for (int i = 0; i < 6; i++) {
                RuneType r1 = rs.set[i];

                jTableCurRune2.getModel().setValueAt(r1.runeType, i, 1);
                jTableCurRune2.getModel().setValueAt(r1.mainStatGui + " +" + r1.mainStatVal, i, 2);
                jTableCurRune2.getModel().setValueAt(i + 1, i, 0);
                //jTableCurRune2.getModel().setValueAt(r1.monster, i, 11);
                for (int j = 0; j < RuneType.slabelsMainDisplay.length; j++) {
                    if (r1.statfix[j] > 0) {
                        jTableCurRune2.getModel().setValueAt(r1.statfix[j], i, 3 + j);
                    }
                }
            }
            for (int j = 0; j < RuneType.slabelsMainDisplay.length; j++) {
                jTableCurRune2.getModel().setValueAt(RuneType.slabelsMainDisplay[j] + " +" + rs.totalStats[j], 6, 3 + j);
                jTableCurRune2.getModel().setValueAt(RuneType.slabels[j].toUpperCase() + "=" + rs.statfixRune[j], 7, 3 + j);
            }
        }
    }

    public static class IconRenderer2 extends DefaultTableCellRenderer {

        public IconRenderer2() {
            super();
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel label = new JLabel();
            label.setOpaque(true);

            if (value != null && value instanceof String) { //Checking if  cell´s values isnt null and the condition is true
                //label.setText((String)value);
                setValue((String) value);
                return this;
            } else if (value instanceof ImageIcon) {
                label.setIcon((ImageIcon) value);
                //System.out.println("Came here : ");
                return label;
            }
            if (value instanceof JButton) {
                //this.setSize(new Dimension(20, 20));
                JButton b1 = (JButton) value;
                b1.setPreferredSize(new Dimension(10, 10));
            }
            return (Component) value;
        }

    }

    public static class IconRenderer extends DefaultTableCellRenderer {

        public IconRenderer() {
            super();
        }

        public void setValue(Object value) {
            if (value == null) {
                setText("");
            } else if (value instanceof String) {
                setText((String) value);
            } else {
                setIcon((ImageIcon) value);
            }
        }
    }


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        /*jDialogRuneManage.pack();
        jDialogRuneManage.setVisible(true);

        updateAllrunesTable();*/
        RuneManager.getInstance().setVisible(true);
        RuneManager.getInstance().main2 = this;

    }//GEN-LAST:event_jButton1ActionPerformed

    public String checkBuildExist(RuneType.RuneSet s1) {
        List<ConfigInfo.PetBuild> list = ConfigInfo.getInstance().petBuilds.get(curPet.name);
        if (list == null) {
            return null;
        }
        for (ConfigInfo.PetBuild p2 : list) {
            boolean ok = true;
            for (int i = 0; i < 6; i++) {
                if (s1.set[i].id != p2.runes[i]) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                return p2.name;
            }
        }

        return null;
    }

    public boolean checkBuildName(String s) {
        List<ConfigInfo.PetBuild> list = ConfigInfo.getInstance().petBuilds.get(curPet.name);
        if (list == null) {
            return true;
        }

        for (ConfigInfo.PetBuild p2 : list) {
            if (p2.name.equalsIgnoreCase(s)) {
                return false;
            }
        }
        return true;
    }

    public void saveThisBuild(RuneSet build) {
        if (build != null && curPet != null) {
            int dialogButton = JOptionPane.YES_NO_OPTION;
            PetSetting cp = ConfigInfo.getInstance().petMaps.get(curPet.name);

            int dialogResult = JOptionPane.showConfirmDialog(this, "Do you want to save this build : " + build.runeSets + " ; Final Value : " + build.bestValue + " ? "
                            + "\n" + build.getShortInfo()
                            + "\n It will overwrite current saved build for this pet : " + curPet.name,
                    "Confirm", dialogButton);

            if (dialogResult == 0) {
            } else {
                System.out.println("No Option");
                return;
            }

            cp.isSaved = true;
            System.out.println("Save build done : " + cp.petName + " ; " + cp.lastOptimize);

            for (int i = 0; i < 6; i++) {
                cp.lastOptimize[i] = build.set[i].id;
                cp.buildUniqueId[i] = build.set[i].uniqueId;
            }

            cp.effectiveHp = build.effectiveHP();
            cp.finalDamage = build.finalDamage();
            cp.optimizeSpd = build.pet_spd;

            System.out.println("Save build done : " + cp.petName);
            loadOptimizedRune(build);
            RunePermutation.bestRuneSet = build;
        }
    }

    //saveBuild
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        saveThisBuild(RunePermutation.bestRuneSet);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        jDialogBuilds.pack();
        jDialogBuilds.setVisible(true);

        updateAllBuilds();
    }//GEN-LAST:event_jButton4ActionPerformed

    public RuneSet getRuneSet(String[] list) {

        List<RuneType> r1 = new ArrayList();
        for (int i = 0; i < 6; i++) {
            if (SwManager.runesIds.get(list[i]) == null) {
                return null;
            }

            r1.add(SwManager.runesIds.get(list[i]));
        }
        System.out.println("r1 : " + r1);
        RuneSet r2 = new RuneType.RuneSet(r1);

        return r2;
    }

    public void updateAllBuildsSimple() {
        if (!initOk) {
            return;
        }
        setupTable(jTableBuilds);

        int k = 0;
        for (PetSetting p1 : ConfigInfo.getInstance().petMaps.values()) {
            if (p1.isSaved && p1.lastOptimize != null) {
                jTableBuilds.setValueAt(k, k, 0);
                jTableBuilds.setValueAt("##", k, 1);
                jTableBuilds.setValueAt(p1.petName, k, 2);

                RuneSet r2 = getRuneSet(p1.buildUniqueId);
                if (r2 != null) {
                    jTableBuilds.setValueAt(r2.runeSets, k, 3);
                }
                k++;
            }
        }
    }

    //load builds
    public void updateAllBuilds() {

        if (!initOk) {
            return;
        }

        //System.out.println("Came here !");

        setupTable(jTableBuilds);
        jTableBuilds.getColumnModel().getColumn(3).setPreferredWidth(120);
        jTableBuilds.getColumnModel().getColumn(0).setPreferredWidth(20);
        jTableBuilds.getColumn("Locked").setPreferredWidth(20);

        DefaultTableModel model = (DefaultTableModel) jTableBuilds.getModel();
        int k = 0;
        ((DefaultTableModel) jTableBuilds.getModel()).setNumRows(0);

        for (PetSetting p1 : ConfigInfo.getInstance().petMaps.values()) {
            if (p1.isSaved) {

                RuneSet rs = getRuneSet(p1.buildUniqueId);
                if (rs == null) {
                    continue;
                }

                model.addRow(new Object[jTableBuilds.getModel().getColumnCount()]);
                SwManager.getPet(p1.petName).savedBuild = rs;

                //System.out.println("Build : " + k + " : " + p1.petName + " ; " + p1.isSaved + " ; " + rs);

                jTableBuilds.setValueAt(k, k, 0);
                jTableBuilds.setValueAt(p1.petName, k, 1);
                jTableBuilds.setValueAt(rs.runeSets, k, 2);
                jTableBuilds.setValueAt(rs.mainStat, k, 3);
                jTableBuilds.setValueAt(p1.optimizeSpd, k, 4);

                jTableBuilds.setValueAt(p1.isLocked, k, 7);
                jTableBuilds.setValueAt(formatNumber(p1.finalDamage), k, 6);
                jTableBuilds.setValueAt(formatNumber(p1.effectiveHp), k, 5);
                k++;
            }
        }

        jTableBuilds.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (jTableBuilds.getSelectedRow() > -1) {
                    // print first column value from selected row
                    System.out.println(jTableBuilds.getValueAt(jTableBuilds.getSelectedRow(), 1).toString());
                    String runeId = jTableBuilds.getValueAt(jTableBuilds.getSelectedRow(), 1).toString();

                    PetSetting p1 = getPetSetting(runeId);
                    RuneSet r1 = getRuneSet(p1.buildUniqueId);
                    displayRune2Table(r1, jTableCurRuneBuild);
                    PetType pet = SwManager.getPet(runeId);
                    if (pet.savedBuild != null) {
                        pet.applyRuneSet(r1);
                    }
                    displayStatMain(jTableStatBuild, pet);
                    //drawRune(r1,jLabelSelectedRune);
                }
            }
        });

        /*for (String s1:ConfigInfo.getInstance().petBuilds.keySet()){
            List<ConfigInfo.PetBuild> list = ConfigInfo.getInstance().petBuilds.get(s1);
            for (ConfigInfo.PetBuild b1:list){
                jTableBuilds.setValueAt(k, k, 0);
                jTableBuilds.setValueAt(b1.name, k, 1);
                jTableBuilds.setValueAt(b1.petName, k, 2);

                List<RuneType> r1 = new ArrayList();
                for (int i=0;i<6;i++){
                    r1.add(SwManager.runesIds.get(""+b1.runes[i]));
                }
                System.out.println("r1 : "+r1);
                RuneSet r2= new RuneType.RuneSet(r1);

                jTableBuilds.setValueAt(r2.runeSets, k, 3);
                k++;
            }
        }*/
    }

    private void jComboPetBuildsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboPetBuildsActionPerformed
        // TODO add your handling code here:
        updateAllBuildsSimple();
    }//GEN-LAST:event_jComboPetBuildsActionPerformed

    private void fourthFilterVarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fourthFilterVarActionPerformed
        // TODO add your handling code here:
        if (curPet != null) {
            updateAllRuneFilter();
        }
    }//GEN-LAST:event_fourthFilterVarActionPerformed

    public PetSetting getPetSetting(String name) {
        return ConfigInfo.getInstance().petMaps.get(name);
    }

    //lock build
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        //System.out.println("Selected : " + jTableBuilds.getSelectedRow() + " ; " + jTableBuilds.getSelectedRowCount());
        if (jTableBuilds.getSelectedRowCount() > 0) {
            for (int i = 0; i < jTableBuilds.getSelectedRowCount(); i++) {
                int row = jTableBuilds.getSelectedRow() + i;
                String name = (String) jTableBuilds.getValueAt(row, 1);
                System.out.println("Pet selected : " + name);
                getPetSetting(name).isLocked = true;
                jTableBuilds.setValueAt(true, row, 7);
                if (curPet.name.equals(name)) {
                    jCheckLockedBuild.setSelected(true);
                }
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        if (jTableBuilds.getSelectedRowCount() > 0) {
            for (int i = 0; i < jTableBuilds.getSelectedRowCount(); i++) {
                int row = jTableBuilds.getSelectedRow() + i;
                String name = (String) jTableBuilds.getValueAt(row, 1);
                System.out.println("Pet selected : " + name);
                getPetSetting(name).isLocked = false;
                jTableBuilds.setValueAt(false, row, 7);
                if (curPet.name.equals(name)) {
                    jCheckLockedBuild.setSelected(false);
                }
            }
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTextRuneLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextRuneLevelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextRuneLevelActionPerformed

    private void jCheckLockedBuildActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckLockedBuildActionPerformed
        // TODO add your handling code here:
        curPetSetting.isLocked = jCheckLockedBuild.isSelected();
    }//GEN-LAST:event_jCheckLockedBuildActionPerformed

    void updateComboSlot() {
        if (curPetSetting != null && !petLoading) {
            //System.out.println("PetSetting : " + curPetSetting.petName);
            curPetSetting.slot246[0] = jComboSlot2.getSelectedIndex();
            curPetSetting.slot246[1] = jComboSlot4.getSelectedIndex();
            curPetSetting.slot246[2] = jComboSlot6.getSelectedIndex();
        }
    }

    private void jComboSlot2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboSlot2ActionPerformed
        // TODO add your handling code here:
        updateComboSlot();
    }//GEN-LAST:event_jComboSlot2ActionPerformed

    private void jComboSlot4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboSlot4ActionPerformed
        // TODO add your handling code here:
        updateComboSlot();
    }//GEN-LAST:event_jComboSlot4ActionPerformed

    private void jComboSlot6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboSlot6ActionPerformed
        // TODO add your handling code here:
        updateComboSlot();
    }//GEN-LAST:event_jComboSlot6ActionPerformed

    private void jButtonBuildSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuildSaveActionPerformed
        // TODO add your handling code here:
        jDialogBuilds.setVisible(false);
    }//GEN-LAST:event_jButtonBuildSaveActionPerformed

    private void jCheckExcludeLockedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckExcludeLockedActionPerformed
        // TODO add your handling code here:
        if (initOk) {
            updateTotalPermutation();
        }
    }//GEN-LAST:event_jCheckExcludeLockedActionPerformed

    public void loadJsonFile(File file) {
        try {
            Files.copy(file.toPath(), new File("optimizer.json").toPath(), REPLACE_EXISTING, COPY_ATTRIBUTES, NOFOLLOW_LINKS);
        } catch (Exception e) {

        }
        System.out.println("Copy done: " + file.getName());
        boolean success = SwManager.getInstance().loadPets(file.toString());
        if (!success) {
            JOptionPane.showMessageDialog(null, "Load data fail : " + file.getName()
                    + "\n Pls choose file with name like xxxxx-optimizer.json", "Fail", JOptionPane.ERROR_MESSAGE);
            return;
        }
        loadData();
        ConfigInfo.getInstance().lastJsonFile = file.getPath();
        int stars = 6 - ConfigInfo.getInstance().loadPetLevel;
        JOptionPane.showMessageDialog(null, "Load data succest \n"
                + "Num runes loaded : " + SwManager.runes.size() + "\n"
                + "Num pets loaded : " + SwManager.pets.size() + "\n"
                + "Only load pets from " + stars + "* above.\n You can change this in Setting!", "Info", JOptionPane.INFORMATION_MESSAGE);

    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        final JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Json file data", "json", "gif");
        fc.setFileFilter(filter);

        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            loadJsonFile(file);
            //This is where a real application would open the file.
        } else {
            System.out.println("Open command cancelled by user.");
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        // TODO add your handling code here:
        //delete Build
        if (jTableBuilds.getSelectedRowCount() > 0) {
            if (jTableBuilds.getSelectedRowCount() > 0) {
                int row = jTableBuilds.getSelectedRow();
                String name = (String) jTableBuilds.getValueAt(row, 1);
                System.out.println("Pet selected : " + name);

                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(this, "Do you want to delete build : [" + name + "] ? ", "Confirm", dialogButton);

                if (dialogResult == 0) {
                    getPetSetting(name).isSaved = false;
                    updateAllBuilds();
                } else {
                    System.out.println("No Option");
                }
            }
        }
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jCheckOnlyStorgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckOnlyStorgeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckOnlyStorgeActionPerformed

    void validateRuneDouble() {
        if (initOk) {
            String mainRune = "" + jComboMainRune.getSelectedItem();
            if (!mainRune.contains("/")) {
                if (mainRune == null) {
                    return;
                }

                System.out.println("mainRune : " + mainRune);
                int mainId = RuneType.getSetId(mainRune);
                if (mainId == -1) {
                    return;
                }

                int numSet = setBonnusNum[mainId];
                if (numSet >= 4) {
                    jComboDouble.setSelectedIndex(0);
                }
            }
        }
    }

    private void jComboDoubleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboDoubleActionPerformed
        // TODO add your handling code here:
        validateRuneDouble();
    }//GEN-LAST:event_jComboDoubleActionPerformed

    private void jComboRuneUpgradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboRuneUpgradeActionPerformed
        // TODO add your handling code here:
        if (initOk && ConfigInfo.getInstance().UpgradeAllRune != jComboRuneUpgrade.getSelectedIndex()) {
            ConfigInfo.getInstance().UpgradeAllRune = jComboRuneUpgrade.getSelectedIndex();
            SwManager.getInstance().loadPets("optimizer.json");
            loadOnePet(curPet.name);
            System.out.println("Update all runes now : " + jComboRuneUpgrade.getSelectedIndex() + " : mainSkill : " + curPetSetting.mainSkill);
        }
    }//GEN-LAST:event_jComboRuneUpgradeActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        /*jDialogRuneManage.pack();
        jDialogRuneManage.setVisible(true);
        updateAllrunesTable();*/
        RuneManager.getInstance().setVisible(true);
        RuneManager.getInstance().main2 = this;
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        jDialogBuilds.pack();
        jDialogBuilds.setVisible(true);

        updateAllBuilds();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        loadConfig();
        jDialogGlorySetting.pack();
        jDialogGlorySetting.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jComboSkillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboSkillActionPerformed
        // TODO add your handling code here:
        if (initOk && jComboSkill.getSelectedIndex() >= 0 && !petLoading) {
            System.out.println("jComboSkillActionPerformed : " + jComboSkill.getSelectedIndex());
            curPet.skillItem = curPet.skillList.get(jComboSkill.getSelectedIndex());

            if (curPetSetting != null) {
                curPetSetting.mainSkill = jComboSkill.getSelectedIndex() + 1;
                savePetSetting();
                loadOnePet(curPet.name);
                jTextSkillMulty.setText(formatSkill(curPet.skillItem.skillMulty));
            }
        }
    }//GEN-LAST:event_jComboSkillActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        jFrameOptimize.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    public void equipBuildRunes(RuneSet build) {
        if (build != null) {
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to equip this Runes " + build.runeSets + " ; best value : " + build.bestValue
                    + "\n to pet : [" + curPet.name + "] ? \nBe carefull, this will change the json File, Better back it up!", "Confirm", dialogButton);

            if (dialogResult == 0) {
                System.out.println("Yes");
                for (RuneType r1 : SwManager.runes) {
                    if (r1.monsterId == curPet.id) {
                        r1.saveEquipInfo(0, "In Storage");
                    }
                }
                for (RuneType r1 : build.set) {
                    r1.saveEquipInfo(curPet.id, curPet.name);
                }
                SwManager.saveFile();
                loadOnePet(curPet.name);
            } else {
                System.out.println("No Option");
            }
        }
    }

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        equipBuildRunes(RunePermutation.bestRuneSet);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void tGloryDarkAtkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tGloryDarkAtkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tGloryDarkAtkActionPerformed

    private void tGloryLightAtkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tGloryLightAtkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tGloryLightAtkActionPerformed

    private void jTextGlobalLocksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextGlobalLocksActionPerformed
        // TODO add your handling code here:
        ConfigInfo.getInstance().globalLocks = jTextGlobalLocks.getText();
    }//GEN-LAST:event_jTextGlobalLocksActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        jFrameResults.pack();
        jFrameResults.setVisible(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        if (jTableResults.getSelectedRow() > -1) {
            // print first column value from selected row
            System.out.println(jTableResults.getValueAt(jTableResults.getSelectedRow(), 0).toString());
            int runeId = Integer.parseInt(jTableResults.getValueAt(jTableResults.getSelectedRow(), 0).toString()) - 1;

            if (runeId < displayRuneList.size()) {
                RuneSet r1 = displayRuneList.get(runeId);
                saveThisBuild(r1);
            }
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        jDialogBuilds.pack();
        jDialogBuilds.setVisible(true);
        updateAllBuilds();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        if (jTableResults.getSelectedRow() > -1) {
            // print first column value from selected row
            System.out.println(jTableResults.getValueAt(jTableResults.getSelectedRow(), 0).toString());
            int runeId = Integer.parseInt(jTableResults.getValueAt(jTableResults.getSelectedRow(), 0).toString()) - 1;
            if (runeId < displayRuneList.size()) {
                RuneSet r1 = displayRuneList.get(runeId);
                //saveThisBuild(r1);
                equipBuildRunes(r1);
            }
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
        PetManager.getInstance().setVisible(true);
        //RuneManager.getInstance().main2 = this;
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
        PetCompare.getInstance().pack();
        PetCompare.getInstance().setVisible(true);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jLabelIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelIconMouseClicked
        // TODO add your handling code here:
        if (curPet != null)
            showPetDetail(curPet);
    }//GEN-LAST:event_jLabelIconMouseClicked

    private void jCheckUpPet40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckUpPet40ActionPerformed
        // TODO add your handling code here:
        if (initOk && ConfigInfo.getInstance().upPet40 != jCheckUpPet40.isSelected()) {
            ConfigInfo.getInstance().upPet40 = jCheckUpPet40.isSelected();
            SwManager.getInstance().loadPets("optimizer.json");
            loadOnePet(curPet.name);
            System.out.println("Update pet lv40 : " + jComboRuneUpgrade.getSelectedIndex() + " : mainSkill : " + curPetSetting.mainSkill);
        }
    }//GEN-LAST:event_jCheckUpPet40ActionPerformed

    private void jCheckNoBrokenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckNoBrokenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckNoBrokenActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
        if (curPet != null) {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to remove all runes from this pet " + curPet.name +
                    " ? \nBe carefull, this will change the json File, Better back it up!", "Confirm", JOptionPane.YES_NO_OPTION);
            if (dialogResult == 0) {
                for (RuneType r1 : SwManager.runes) {
                    if (r1.monsterId == curPet.id) {
                        r1.saveEquipInfo(0, "In Storage");
                    }
                }
                SwManager.saveFile();
                loadOnePet(curPet.name);
            }
        }

    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        ConfigInfo.getInstance().favourite.clear();
        jComboFavourite.removeAllItems();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    public void showPetDetail(PetType pet) {
        String name = pet.a_name;
        if (name.startsWith("Homun"))
            name = "Homun" + pet.attribute;
        PetManager.getInstance().showDialogDetail(name);
    }

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        if (curPet != null)
            showPetDetail(curPet);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        // TODO add your handling code here:
        jDialogHelp.pack();
        jDialogHelp.setVisible(true);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jCheckGuildWarsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckGuildWarsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckGuildWarsActionPerformed

    private void fifthFilterVarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fifthFilterVarActionPerformed
        // TODO add your handling code here:
        if (curPet != null) {
            updateAllRuneFilter();
        }
    }//GEN-LAST:event_fifthFilterVarActionPerformed

    private void tFifthValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tFifthValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tFifthValueActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // TODO add your handling code here:
        if (ConfigInfo.getInstance().lastJsonFile.length() > 0) {
            File file = new File(ConfigInfo.getInstance().lastJsonFile);
            loadJsonFile(file);
        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        // TODO add your handling code here:
        RunePermutation.fullSearch = true;
        performOptimize();
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Application.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Application().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox fifthFilterVar;
    private javax.swing.JComboBox<String> firstFilterVar;
    private javax.swing.JComboBox fourthFilterVar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonBuildSave;
    private javax.swing.JButton jButtonCheckRunes;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonGWSetting;
    private javax.swing.JButton jButtonOptimize;
    private javax.swing.JButton jButtonStop;
    private javax.swing.JCheckBox jCheckAcc;
    private javax.swing.JCheckBox jCheckAtk;
    private javax.swing.JCheckBox jCheckCrit;
    private javax.swing.JCheckBox jCheckCritDmg;
    private javax.swing.JCheckBox jCheckDef;
    private javax.swing.JCheckBox jCheckExcludeLocked;
    private javax.swing.JCheckBox jCheckFavourite;
    private javax.swing.JCheckBox jCheckGuildWars;
    private javax.swing.JCheckBox jCheckHP;
    private javax.swing.JCheckBox jCheckLockedBuild;
    private javax.swing.JCheckBox jCheckNemesis;
    private javax.swing.JCheckBox jCheckNoBroken;
    private javax.swing.JCheckBox jCheckOnlyStorge;
    private javax.swing.JCheckBox jCheckPet4StarRune;
    private javax.swing.JCheckBox jCheckPet5StarRune;
    private javax.swing.JCheckBox jCheckRes;
    private javax.swing.JCheckBox jCheckRevenge;
    private javax.swing.JCheckBox jCheckShield;
    private javax.swing.JCheckBox jCheckSpd;
    private javax.swing.JCheckBox jCheckThreads;
    private javax.swing.JCheckBox jCheckUpPet40;
    private javax.swing.JCheckBox jCheckWill;
    private javax.swing.JComboBox<String> jComboDouble;
    private javax.swing.JComboBox<String> jComboFavourite;
    private javax.swing.JComboBox<String> jComboFontSize;
    private javax.swing.JComboBox jComboMainRune;
    private javax.swing.JComboBox jComboOptimizeFinal;
    private javax.swing.JComboBox<String> jComboPetBuilds;
    private javax.swing.JComboBox<String> jComboPetLevel;
    private javax.swing.JComboBox jComboPets;
    private javax.swing.JComboBox<String> jComboRuneUpgrade;
    private javax.swing.JComboBox<String> jComboSkill;
    private javax.swing.JComboBox jComboSlot2;
    private javax.swing.JComboBox jComboSlot4;
    private javax.swing.JComboBox jComboSlot6;
    private javax.swing.JDialog jDialogBuilds;
    private javax.swing.JDialog jDialogGlorySetting;
    private javax.swing.JDialog jDialogHelp;
    private javax.swing.JDialog jDialogRune;
    private javax.swing.JDialog jDialogRuneManage;
    private javax.swing.JFrame jFrameOptimize;
    private javax.swing.JFrame jFrameResults;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelIcon;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTableBuilds;
    private javax.swing.JTable jTableCurRune;
    private javax.swing.JTable jTableCurRune2;
    private javax.swing.JTable jTableCurRuneBuild;
    private javax.swing.JTable jTableCurRuneMain;
    private javax.swing.JTable jTableCurRuneOptimized;
    private javax.swing.JTable jTableExtraPetInfo;
    private javax.swing.JTable jTablePetStatOptmized;
    private javax.swing.JTable jTablePetStatResults;
    private javax.swing.JTable jTableResults;
    private javax.swing.JTable jTableResultsOptimized;
    private javax.swing.JTable jTableStatBuild;
    private javax.swing.JTable jTableStatMain;
    private javax.swing.JFormattedTextField jTextAllPermus;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JFormattedTextField jTextCurBest;
    private javax.swing.JTextPane jTextCurrentRune;
    private javax.swing.JTextField jTextExcludeRunes;
    private javax.swing.JFormattedTextField jTextFilterValue;
    private javax.swing.JFormattedTextField jTextFoundSet;
    private javax.swing.JTextField jTextGlobalLocks;
    private javax.swing.JTextField jTextIncludeRunes;
    private javax.swing.JTextField jTextLocks;
    private javax.swing.JTextPane jTextOutput;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JFormattedTextField jTextRuneLevel;
    private javax.swing.JFormattedTextField jTextRuneProcess;
    private javax.swing.JTextField jTextSkillMulty;
    private javax.swing.JComboBox<String> secondFilterVar;
    private javax.swing.JFormattedTextField tFifthValue;
    private javax.swing.JFormattedTextField tFirstValue;
    private javax.swing.JFormattedTextField tFourthValue;
    private javax.swing.JFormattedTextField tGloryATK;
    private javax.swing.JFormattedTextField tGloryCD;
    private javax.swing.JFormattedTextField tGloryDEF;
    private javax.swing.JFormattedTextField tGloryDarkAtk;
    private javax.swing.JFormattedTextField tGloryFireAtk;
    private javax.swing.JFormattedTextField tGloryHP;
    private javax.swing.JFormattedTextField tGloryLightAtk;
    private javax.swing.JFormattedTextField tGlorySpd;
    private javax.swing.JFormattedTextField tGloryWaterAtk;
    private javax.swing.JFormattedTextField tGloryWindAtk;
    private javax.swing.JFormattedTextField tLeaderSkill;
    private javax.swing.JTextField tSecondRuneSet;
    private javax.swing.JFormattedTextField tSecondValue;
    private javax.swing.JFormattedTextField tThirdValue;
    private javax.swing.JFormattedTextField tWarAtk;
    private javax.swing.JFormattedTextField tWarCd;
    private javax.swing.JFormattedTextField tWarDef;
    private javax.swing.JFormattedTextField tWarHp;
    private javax.swing.JFormattedTextField textNumThreads;
    private javax.swing.JComboBox thirdFilterVar;
    // End of variables declaration//GEN-END:variables
}
