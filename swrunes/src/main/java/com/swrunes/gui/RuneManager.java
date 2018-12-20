/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swrunes.gui;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import com.swrunes.swrunes.Crawler;
import com.swrunes.swrunes.PetType;
import com.swrunes.swrunes.RuneType;
import static com.swrunes.swrunes.RuneType.ATK;
import static com.swrunes.swrunes.RuneType.CD;
import static com.swrunes.swrunes.RuneType.CR;
import static com.swrunes.swrunes.RuneType.DEF;
import static com.swrunes.swrunes.RuneType.HP;
import static com.swrunes.swrunes.RuneType.SPD;
import com.swrunes.swrunes.SwManager;

/**
 *
 * @author tuanha
 */
public class RuneManager extends javax.swing.JFrame {
    RuneType focusRune = null;
    /**
     * Creates new form RuneManager
     */
    public RuneManager() {
        
        UIManager.getLookAndFeelDefaults()
        .put("defaultFont", new Font("Tahoma", Font.PLAIN, 16));
        
        initComponents();
        
        jTableAllRunes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (jTableAllRunes.getSelectedRow() > -1) {
                    // print first column value from selected row
                    //System.out.println(jTableAllRunes.getValueAt(jTableAllRunes.getSelectedRow(), 1).toString());
                    String runeId = jTableAllRunes.getValueAt(jTableAllRunes.getSelectedRow(), 1).toString();
                    RuneType r1 = SwManager.runeMaps.get(runeId);
                    Application.drawRune(r1, jLabelSelectedRune,jCheckCountPower.isSelected());
                    focusRune = r1;
                    
                    r1.getOriginRarity();
                    final int petId = r1.monsterId;
                    jTextPetName.setText(r1.monster);
                        
                    (new Thread() {
                        public void run() {
                            //jLabelIcon.setText("");
                            PetType p1=SwManager.petsIds.get(petId);                            
                            String s1="Inventory";
                            if (p1!=null) s1=p1.a_name;
                            
                            BufferedImage bf = Crawler.crawlPetPicture(s1);
                            if (bf != null) {
                                jLabelRuneOwner.setSize(bf.getWidth(), bf.getHeight());
                                jLabelRuneOwner.setIcon(new ImageIcon(bf));
                            }
                        }
                    }).start();
                }
            }
        });
        
        loadData();
        updateAllrunesTable();
    }

    List<JComboBox> comboList = new ArrayList();
    List<JFormattedTextField> textList = new ArrayList();
    
    void loadData(){
        loadingData = true;
        if (SwManager.runes.size()==0){
            SwManager.getInstance().loadPets("optimizer.json");
        }
        jComboSearchRuneType.removeAllItems();
        jComboSearchRuneType.addItem("All set");
        for (String s5 : RuneType.setBonnusLabel) {
            jComboSearchRuneType.addItem(s5);
        }

        jComboSearchRuneSlot.removeAllItems();
        jComboSearchRuneSlot.addItem("All slot");
        for (int i = 1; i < 7; i++) {
            jComboSearchRuneSlot.addItem("Slot " + i);
        }
        jComboSearchRuneSlot.addItem("2 4 6");
        jComboSearchRuneSlot.addItem("1 3 5");
        
        jComboSearchRuneLevel.removeAllItems();
        jComboSearchRuneLevel.addItem("All levels");
        for (int i = 0; i <= 15; i++) {
            jComboSearchRuneLevel.addItem("lv " + i);
        }
        jComboSearchRuneLevel.addItem("lv <= 6");
        jComboSearchRuneLevel.addItem("lv < 9");
        jComboSearchRuneLevel.addItem("lv < 12");
        jComboSearchRuneLevel.addItem("lv < 6");
        
        jComboRunePet.removeAllItems();
        jComboRunePet.addItem("All pets");
        jComboRunePet.addItem("In Storage");
        
        TreeSet<String> tr1 = new TreeSet();
        for (PetType p1 :SwManager.petsIds.values()){  
            if (p1.runesEquip>0){
                tr1.add(p1.name);
            }
        }
        for (String s1:tr1)
            jComboRunePet.addItem(s1);
                
        jComboSearchRuneStat.removeAllItems();
        jComboSearchRuneStat.addItem("All Stats");
        for (String s2: RuneType.slabelsMainDisplay) {
            jComboSearchRuneStat.addItem(s2);
        }
        
        jComboUngrind.removeAllItems();
        jComboUngrind.addItem("");
        for (String s2: RuneType.slabelsMain2) {
            jComboUngrind.addItem(s2);
        }

        jRuneSearchFilter.removeAllItems();
        jRuneSearchFilter.addItem("");
        for (String s5 : RuneType.slabelsMainDisplay) {
            jRuneSearchFilter.addItem(s5 + " >=");
        }

        jTextSearchRuneFilter.setValue(0);
        
        comboList.clear();
        comboList.add(jComboBox1);
        comboList.add(jComboBox2);
        comboList.add(jComboBox3);
        comboList.add(jComboBox4);
        comboList.add(jComboBox6);
        
        textList.clear();
        textList.add(jFormattedTextField1);
        textList.add(jFormattedTextField2);
        textList.add(jFormattedTextField3);
        textList.add(jFormattedTextField4);
        textList.add(jFormattedTextField5);
        
        for (JComboBox b1:comboList){
            b1.removeAllItems();
            b1.addItem("");
            for (int i=0;i<=10;i++){
                b1.addItem(RuneType.slabelsMain[i]);
            }
        }
        for (JFormattedTextField b1:textList)
            b1.setValue(0);
        
        jComboLevel.removeAllItems();
        for (int i=0;i<=15;i++)
            jComboLevel.addItem(""+i);        
        loadingData = false;
    }
    
    static RuneManager instance;
    public static RuneManager getInstance(){
        if (instance==null){
            instance = new RuneManager();
        }
        return instance;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrameRuneEdit = new javax.swing.JFrame();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jTextMainStat = new javax.swing.JFormattedTextField();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jFormattedTextField4 = new javax.swing.JFormattedTextField();
        jComboLevel = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabelRuneEdit = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jComboBox1 = new javax.swing.JComboBox<>();
        jFormattedTextField5 = new javax.swing.JFormattedTextField();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jButtonSave = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jComboSearchRuneGrade = new javax.swing.JComboBox();
        jComboSearchRuneType = new javax.swing.JComboBox<>();
        jLabel35 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jComboSearchRuneStat = new javax.swing.JComboBox();
        jComboSearchRuneSlot = new javax.swing.JComboBox<>();
        jLabel36 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jRuneSearchFilter = new javax.swing.JComboBox<>();
        jTextSearchRuneFilter = new javax.swing.JFormattedTextField();
        jButtonRuneSearch = new javax.swing.JButton();
        jTextNumRows = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableAllRunes = new javax.swing.JTable();
        jComboSearchRuneLevel = new javax.swing.JComboBox();
        jLabel34 = new javax.swing.JLabel();
        jLabelSelectedRune = new javax.swing.JLabel();
        jButtonViewAll = new javax.swing.JButton();
        jLabelRuneOwner = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jCheckStorageRunes = new javax.swing.JCheckBox();
        jComboRunePet = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jCheckEditedRunes = new javax.swing.JCheckBox();
        jComboUngrind = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jTextPetName = new javax.swing.JTextField();
        jComboLegend = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jComboOrigin = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jCheckCountPower = new javax.swing.JCheckBox();

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        jLabel5.setText("Main Stat");

        jFormattedTextField2.setText("0");
        jFormattedTextField2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jFormattedTextField2PropertyChange(evt);
            }
        });

        jTextMainStat.setText("0");
        jTextMainStat.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTextMainStatPropertyChange(evt);
            }
        });

        jFormattedTextField3.setText("0");
        jFormattedTextField3.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jFormattedTextField3PropertyChange(evt);
            }
        });

        jFormattedTextField4.setText("0");
        jFormattedTextField4.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jFormattedTextField4PropertyChange(evt);
            }
        });

        jComboLevel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboLevelActionPerformed(evt);
            }
        });

        jLabel2.setText("Level");

        jLabelRuneEdit.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox6.setEnabled(false);
        jComboBox6.setFocusable(false);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jFormattedTextField5.setText("0");
        jFormattedTextField5.setFocusable(false);

        jFormattedTextField1.setText("0");
        jFormattedTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField1ActionPerformed(evt);
            }
        });
        jFormattedTextField1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jFormattedTextField1PropertyChange(evt);
            }
        });

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel3.setText(" Option");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jLabel4.setText(" Sub");

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jFrameRuneEditLayout = new javax.swing.GroupLayout(jFrameRuneEdit.getContentPane());
        jFrameRuneEdit.getContentPane().setLayout(jFrameRuneEditLayout);
        jFrameRuneEditLayout.setHorizontalGroup(
            jFrameRuneEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameRuneEditLayout.createSequentialGroup()
                .addGroup(jFrameRuneEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrameRuneEditLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jFrameRuneEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(jFrameRuneEditLayout.createSequentialGroup()
                                .addGroup(jFrameRuneEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jFrameRuneEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel3)
                            .addGroup(jFrameRuneEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrameRuneEditLayout.createSequentialGroup()
                                    .addGroup(jFrameRuneEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jFrameRuneEditLayout.createSequentialGroup()
                                            .addComponent(jComboBox6, 0, 162, Short.MAX_VALUE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                        .addGroup(jFrameRuneEditLayout.createSequentialGroup()
                                            .addComponent(jLabel2)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jComboLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(28, 28, 28)))
                                    .addComponent(jFormattedTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jFrameRuneEditLayout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addGap(18, 18, 18)
                                    .addComponent(jTextMainStat))))
                        .addGap(18, 18, 18)
                        .addComponent(jLabelRuneEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jFrameRuneEditLayout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(jButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jFrameRuneEditLayout.setVerticalGroup(
            jFrameRuneEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameRuneEditLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jFrameRuneEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jFrameRuneEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jFrameRuneEditLayout.createSequentialGroup()
                        .addGroup(jFrameRuneEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextMainStat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addComponent(jLabel3)
                        .addGap(3, 3, 3)
                        .addGroup(jFrameRuneEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jFrameRuneEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jFrameRuneEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jFrameRuneEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jFrameRuneEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabelRuneEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jFrameRuneEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSave)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        setTitle("Rune Manage");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jComboSearchRuneGrade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All Grade", "4*", "5*", "6*" }));
        jComboSearchRuneGrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboSearchRuneGradeActionPerformed(evt);
            }
        });

        jComboSearchRuneType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboSearchRuneType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboSearchRuneTypeActionPerformed(evt);
            }
        });

        jLabel35.setText("Rune Grade");

        jLabel26.setText("Rune Type");

        jComboSearchRuneStat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboSearchRuneStat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboSearchRuneStatActionPerformed(evt);
            }
        });

        jComboSearchRuneSlot.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboSearchRuneSlot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboSearchRuneSlotActionPerformed(evt);
            }
        });

        jLabel36.setText("Stat Search");

        jLabel27.setText("Rune Slot");

        jLabel37.setText("Rune Stat");

        jRuneSearchFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextSearchRuneFilter.setText("0");

        jButtonRuneSearch.setText("Search");
        jButtonRuneSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRuneSearchActionPerformed(evt);
            }
        });

        jTextNumRows.setText("4 rows");

        jTableAllRunes.setAutoCreateRowSorter(true);
        jTableAllRunes.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTableAllRunes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Rune", "Id", "Main", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11", "All", "AC", "DC", "Pet"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTableAllRunes);

        jComboSearchRuneLevel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboSearchRuneLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboSearchRuneLevelActionPerformed(evt);
            }
        });

        jLabel34.setText("Rune Level");

        jLabelSelectedRune.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButtonViewAll.setText("View All Runes");
        jButtonViewAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewAllActionPerformed(evt);
            }
        });

        jLabelRuneOwner.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton1.setText("Equip Rune");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jCheckStorageRunes.setText("Only Storage Runes");
        jCheckStorageRunes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckStorageRunesActionPerformed(evt);
            }
        });

        jComboRunePet.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboRunePet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboRunePetActionPerformed(evt);
            }
        });

        jLabel1.setText("Choose Pet");

        jButton3.setText("Edit Rune");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jCheckEditedRunes.setText("Show Edited Runes");
        jCheckEditedRunes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckEditedRunesActionPerformed(evt);
            }
        });

        jComboUngrind.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboUngrind.setToolTipText("When you want to grind a rune.\nSearch for the rune that have the stat ungrinded.");
        jComboUngrind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboUngrindActionPerformed(evt);
            }
        });

        jLabel6.setText("UnGrind");

        jTextPetName.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N

        jComboLegend.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Normal", "Rare", "Magic", "Hero", "Legend" }));
        jComboLegend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboLegendActionPerformed(evt);
            }
        });

        jLabel7.setText("Legend");

        jLabel8.setText("Rune rating stat : ALL = total sub stat ; AC = total attack stat (CR,CD,ATK,SPD) ; AD = Total support stat (DEF,HP,SPD)");

        jComboOrigin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "100% chance legend", "High chance Legend" }));
        jComboOrigin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboOriginActionPerformed(evt);
            }
        });

        jLabel9.setText("Origin Rarity");

        jCheckCountPower.setText("Show power up");
        jCheckCountPower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckCountPowerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelSelectedRune, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelRuneOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextPetName, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextNumRows, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addComponent(jButtonViewAll)
                                .addGap(36, 36, 36)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboRunePet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckStorageRunes)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckEditedRunes)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(1, 1, 1)
                        .addComponent(jComboOrigin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboSearchRuneGrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboSearchRuneType, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboSearchRuneSlot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel34)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboSearchRuneLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel36)
                            .addComponent(jLabel37))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jRuneSearchFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jTextSearchRuneFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(45, 45, 45)
                                .addComponent(jButtonRuneSearch))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jComboSearchRuneStat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1)))
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboLegend, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboUngrind, 0, 96, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckCountPower)
                .addContainerGap(72, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboOrigin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jCheckCountPower))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboSearchRuneLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34)
                    .addComponent(jComboSearchRuneGrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35)
                    .addComponent(jComboSearchRuneStat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37)
                    .addComponent(jButton1)
                    .addComponent(jComboUngrind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboSearchRuneType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(jComboSearchRuneSlot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(jRuneSearchFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextSearchRuneFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonRuneSearch)
                    .addComponent(jLabel36)
                    .addComponent(jComboLegend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextNumRows, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonViewAll)
                    .addComponent(jComboRunePet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jCheckStorageRunes)
                    .addComponent(jCheckEditedRunes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelSelectedRune, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelRuneOwner, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextPetName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboSearchRuneTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboSearchRuneTypeActionPerformed
        // TODO add your handling code here:                    
            updateAllrunesTable();
    }//GEN-LAST:event_jComboSearchRuneTypeActionPerformed

    boolean loadingData = false;
    //manage rune, all rune
    public void updateAllrunesTable() {
        if (loadingData) return;
        
        //System.out.println("************* updateAllrunesTable : ******* : RuneType : "+jComboSearchRuneType.getSelectedItem()+" : "+(jComboSearchRuneType.getSelectedIndex()-1)
        //        +" slot = "+jComboSearchRuneSlot.getSelectedItem());
        ((DefaultTableModel) jTableAllRunes.getModel()).setNumRows(0);
        //applyCurrentRune();
        for (int i = 0; i < RuneType.slabelsMainDisplay.length; i++) {
            jTableAllRunes.getColumnModel().getColumn(i + 3).setHeaderValue(RuneType.slabelsMainDisplay[i]);
        }

        jTableAllRunes.getColumnModel().getColumn(0).setCellRenderer(new Application.IconRenderer());

        double d1 = 1.0;
        BufferedImage ic1 = Application.scaleImage(Application.getRuneIcon(SwManager.runes.get(0)), d1);

        Application.setupTable(jTableAllRunes);

        jTableAllRunes.setRowHeight(ic1.getHeight());
        jTableAllRunes.getColumnModel().getColumn(0).setPreferredWidth(ic1.getWidth() + 10);
        jTableAllRunes.getColumnModel().getColumn(1).setPreferredWidth(60);
        jTableAllRunes.getColumnModel().getColumn(2).setPreferredWidth(120);
        jTableAllRunes.getColumnModel().getColumn(13).setPreferredWidth(120);
        jTableAllRunes.getColumnModel().getColumn(11).setPreferredWidth(60);
        jTableAllRunes.getColumnModel().getColumn(12).setPreferredWidth(60);
        jTableAllRunes.getColumnModel().getColumn(13).setPreferredWidth(60);
        
        DefaultTableModel model = (DefaultTableModel) jTableAllRunes.getModel();
        int numCol = jTableAllRunes.getColumnModel().getColumnCount();

        for (int i = 0; i < RuneType.slabelsMainDisplay.length; i++) {
            jTableAllRunes.getColumnModel().getColumn(i + 3).setPreferredWidth(60);
        }

        List<RuneType> runeList = new ArrayList();

        //System.out.println("jTextSearchRuneFilter : " + getInt(jTextSearchRuneFilter) + " ; " + jTextSearchRuneFilter.getText());
        for (RuneType r1 : SwManager.runes) {
            boolean ok = true;
            if (jComboSearchRuneType.getSelectedIndex() > 0 && r1.runeTypeIndex != jComboSearchRuneType.getSelectedIndex() - 1)
                ok = false;            
            if (jComboSearchRuneLevel.getSelectedIndex() > 0 && jComboSearchRuneLevel.getSelectedIndex() <=16 && r1.level != (jComboSearchRuneLevel.getSelectedIndex()-1))
                ok = false;         
            
            if (jComboSearchRuneLevel.getSelectedIndex() ==17 && r1.level >6 )
                ok = false;     
            if (jComboSearchRuneLevel.getSelectedIndex() ==18 && r1.level >=9 )
                ok = false;
            if (jComboSearchRuneLevel.getSelectedIndex() ==19 && r1.level >=12 )
                ok = false;
            if (jComboSearchRuneLevel.getSelectedIndex() ==20 && r1.level >=6 )
                ok = false;
            
            if (jComboOrigin.getSelectedIndex()==1 && r1.getOriginRarity()<5){
                ok=false;
            }
            if (jComboOrigin.getSelectedIndex()==2 && r1.getOriginRarity()<4){
                ok=false;
            }
            
            int rune_quality = r1.subStatGuis.size()+1;
            if (jComboLegend.getSelectedIndex() >0 && rune_quality != jComboLegend.getSelectedIndex() )
                ok = false;
            
            if (jComboSearchRuneGrade.getSelectedIndex() > 0 && r1.grade != (jComboSearchRuneGrade.getSelectedIndex()+3))
                ok = false;         
            
            if (jComboSearchRuneSlot.getSelectedIndex() > 0 &&jComboSearchRuneSlot.getSelectedIndex() <=6 && r1.slot != jComboSearchRuneSlot.getSelectedIndex()) 
                ok = false;
            
            if (jComboSearchRuneSlot.getSelectedIndex() ==7 && r1.slot%2 ==1) 
                ok = false;
            if (jComboSearchRuneSlot.getSelectedIndex() ==8 && r1.slot%2 ==0) 
                ok = false;
            
            if (jComboUngrind.getSelectedIndex() > 0 && !r1.checkSubGrinded(jComboUngrind.getSelectedIndex()-1)) 
                ok = false;      
            
            if (jCheckStorageRunes.isSelected() && r1.monsterId!=0){
                ok = false;
            }
            if (jCheckEditedRunes.isSelected() && !r1.isEdited){
                ok = false;
            }
            
            if (jComboSearchRuneStat.getSelectedIndex() > 0 && r1.mainStatIndex != jComboSearchRuneStat.getSelectedIndex()-1) 
                ok = false;
            
            if (jComboRunePet.getSelectedIndex() > 0 && !r1.monster.equals(jComboRunePet.getSelectedItem().toString())) 
                ok = false;
            
            if (jRuneSearchFilter.getSelectedIndex() > 0) {
                if (r1.statfix[jRuneSearchFilter.getSelectedIndex() - 1] < Application.getInt(jTextSearchRuneFilter)) {
                    ok = false;
                }
                if (r1.mainStatIndex == jRuneSearchFilter.getSelectedIndex() - 1) {
                    ok = false;
                }
            }

            if (ok) {
                runeList.add(r1);
            }
        }
        
        if (runeList.size()==0) return;

        int curRow = jTableAllRunes.getRowCount();
        for (int i = 0; i < runeList.size() - curRow; i++) {
            model.addRow(new Object[numCol]);
        }

        Application.drawRune(runeList.get(0), jLabelSelectedRune);
        for (int i = 0; i < runeList.size(); i++) {
            RuneType r1 = runeList.get(i);
            jTableAllRunes.getModel().setValueAt(r1.id, i, 1);
            jTableAllRunes.getModel().setValueAt(r1.mainStatGui + " +" + r1.originMainStatVal, i, 2);

            

            jTableAllRunes.getModel().setValueAt(new ImageIcon(Application.scaleImage(Application.getRuneIcon(r1), d1)), i, 0);
            int totalSub = 0;
            int atkSub = 0;
            int defSub = 0;
            for (int j = 0; j < RuneType.slabelsMainDisplay.length; j++) {
                if (r1.statfix[j] > 0) {
                    if (r1.mainStatIndex != j) {
                        jTableAllRunes.getModel().setValueAt(r1.statfix[j], i, 3 + j);
                        totalSub += r1.statfix[j];
                        
                        if (j==ATK || j==CD || j==SPD || j==CR)
                            atkSub += r1.statfix[j];
                        if (j==HP || j==DEF || j==SPD)
                            defSub += r1.statfix[j];
                    }
                }
            }            
            jTableAllRunes.getModel().setValueAt(totalSub, i, 11);
            jTableAllRunes.getModel().setValueAt(atkSub, i, 12);
            jTableAllRunes.getModel().setValueAt(defSub, i, 13);
            jTableAllRunes.getModel().setValueAt(r1.monster, i, 14);
        }
        if (runeList.size()>0){
            jTableAllRunes.getSelectionModel().setSelectionInterval(0, 0);
        }
        jTextNumRows.setText("" + runeList.size() + " runes");
        
    }
    private void jComboSearchRuneSlotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboSearchRuneSlotActionPerformed
        // TODO add your handling code here:                    
        updateAllrunesTable();        
    }//GEN-LAST:event_jComboSearchRuneSlotActionPerformed

    private void jButtonRuneSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRuneSearchActionPerformed
        // TODO add your handling code here:
        
        updateAllrunesTable();
    }//GEN-LAST:event_jButtonRuneSearchActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        System.out.println("Window closing : "+runAlone);
        if (runAlone){
            System.exit(0);
        }
    }//GEN-LAST:event_formWindowClosing

    private void jComboSearchRuneLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboSearchRuneLevelActionPerformed
        // TODO add your handling code here:
        updateAllrunesTable();
    }//GEN-LAST:event_jComboSearchRuneLevelActionPerformed

    private void jButtonViewAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewAllActionPerformed
        // TODO add your handling code here:
        loadingData = true;
        jComboSearchRuneGrade.setSelectedIndex(0);
        jComboSearchRuneType.setSelectedIndex(0);
        jComboSearchRuneLevel.setSelectedIndex(0);
        jComboSearchRuneSlot.setSelectedIndex(0);
        jComboSearchRuneStat.setSelectedIndex(0);
        jComboRunePet.setSelectedIndex(0);
        jCheckStorageRunes.setSelected(false);
        jCheckEditedRunes.setSelected(false);
        loadingData = false;
        updateAllrunesTable();
    }//GEN-LAST:event_jButtonViewAllActionPerformed

    private void jComboSearchRuneStatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboSearchRuneStatActionPerformed
        // TODO add your handling code here:
        updateAllrunesTable();
    }//GEN-LAST:event_jComboSearchRuneStatActionPerformed

    private void jComboSearchRuneGradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboSearchRuneGradeActionPerformed
        // TODO add your handling code here:
        updateAllrunesTable();
    }//GEN-LAST:event_jComboSearchRuneGradeActionPerformed

    public Application main2;
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:        
        PetType curPet = Application.curPet;
                
        if (curPet!=null && jTableAllRunes.getSelectedRow() > -1){
            String runeId = jTableAllRunes.getValueAt(jTableAllRunes.getSelectedRow(), 1).toString();
            RuneType r1 = SwManager.runeMaps.get(runeId);
            
            int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to equip this Rune " + r1.runeType+" ; slot="+r1.slot
                    + "\n to pet : [" + curPet.name + "] ? \nBe carefull, this will change the json File, Better back it up!", "Confirm", JOptionPane.YES_NO_OPTION);

            // print first column value from selected row
            System.out.println(jTableAllRunes.getValueAt(jTableAllRunes.getSelectedRow(), 1).toString());            
            if (dialogResult == 0) {
                System.out.println("Yes");
                
                for (RuneType r2:SwManager.runes){
                    if (r2.monsterId==curPet.id && r2.slot==r1.slot){
                        r2.saveEquipInfo(curPet.id, curPet.name);
                    }
                }
                r1.saveEquipInfo(curPet.id, curPet.name); 
                
                if (main2!=null)
                    main2.loadOnePet(curPet.name);
                SwManager.saveFile();
            } else {
                System.out.println("No Option");
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jCheckStorageRunesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckStorageRunesActionPerformed
        // TODO add your handling code here:
        updateAllrunesTable();
    }//GEN-LAST:event_jCheckStorageRunesActionPerformed

    private void jComboRunePetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboRunePetActionPerformed
        // TODO add your handling code here:
        updateAllrunesTable();
    }//GEN-LAST:event_jComboRunePetActionPerformed

    public RuneType curRuneEdit = null;
    public RuneType oldRuneEdit = null;
    
    public void showRuneEdit(RuneType r1){        
        if (r1==null) return;
        loadingData = true;
        
        System.out.println("Rune : "+r1.mainStatVal+" ; "+r1.mainStat);
        
        curRuneEdit = r1;
        System.out.println("Edit rune : "+r1.runeType);
        System.out.println("subStat1 : "+r1.subStat1);
        System.out.println("subStat2 : "+r1.subStat2);
        for (int i=0;i<5;i++){
            comboList.get(i).setSelectedIndex(0);
            textList.get(i).setText("");
        }
        
        jTextMainStat.setValue(r1.mainStatVal);
        for (int i=0;i<r1.subStat1.size();i++){            
                //System.out.println(i+" : "+comboList.get(i).getItemCount()+" ; "+r1.subStat1.get(i));
                comboList.get(i).setSelectedIndex(r1.subStat1.get(i)+1);
            textList.get(i).setValue(r1.subStat2.get(i));            
        }
        if (r1.it>=0){
            comboList.get(4).setSelectedIndex(r1.it+1);
            textList.get(4).setValue(r1.iv); 
        }
        Application.drawRune(r1, jLabelRuneEdit);        
        jComboLevel.setSelectedIndex(r1.level);
        
        loadingData = false;
    }
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        jFrameRuneEdit.pack();
        jFrameRuneEdit.setVisible(true);
        
        if (jTableAllRunes.getSelectedRow() > -1){
            String runeId = jTableAllRunes.getValueAt(jTableAllRunes.getSelectedRow(), 1).toString();
            RuneType r1 = SwManager.runeMaps.get(runeId);
            if (r1!=null){
                oldRuneEdit = r1;
                RuneType r2= new RuneType(r1.jsonData, r1.optimizerFile, r1.id);
                showRuneEdit(r2);                
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed
    
    int getRuneMainStat(RuneType r1){
        if (r1.slot%2==1 && r1.grade>=4){
            return SwManager.get135MainStat(r1.level, r1.grade, r1.slot);
        }
        for (RuneType r:SwManager.runes){
            if (r1.id!=r.id && r1.level == r.level && r1.grade==r.grade && !r.isEdited){
                if (RuneType.sameStat(r1.mainStatIndex,r.mainStatIndex)){   
                    System.out.println("Found rune same : "+r.slot+" ; "+r.runeType+" ; "+r.mainStatIndex);
                    return r.originMainStatVal;
                }
            }
        }
        if (r1.level==15 && r1.grade>=4 && r1.runeTypeIndex<RuneType.setRuneMaxValue.length){
            return RuneType.setRuneMaxValue[r1.runeTypeIndex][r1.grade-4];
        }
        if (r1.level==12 && r1.grade>=4 && r1.runeTypeIndex<RuneType.setRuneMaxValue12.length){
            return RuneType.setRuneMaxValue12[r1.runeTypeIndex][r1.grade-4];
        }
        return 0;
    }
    
    private void jComboLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboLevelActionPerformed
        // TODO add your handling code here:
        if (!loadingData && curRuneEdit!=null){
            curRuneEdit.level = jComboLevel.getSelectedIndex();
            int value = getRuneMainStat(curRuneEdit);
            System.out.println("Rune : "+value+" ; "+curRuneEdit.level+" ; slot_"+curRuneEdit.slot+";"+curRuneEdit.mainStatIndex);
            if (value>0){                
                jTextMainStat.setValue(value);
                curRuneEdit.mainStatVal = value;
                Application.drawRune(curRuneEdit, jLabelRuneEdit);       
            }
        }
    }//GEN-LAST:event_jComboLevelActionPerformed

    private void jFormattedTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField1ActionPerformed
        // TODO add your handling code here:      
    }//GEN-LAST:event_jFormattedTextField1ActionPerformed

    public void updateSubStat(){
        if (loadingData) return;
        if (curRuneEdit==null) return;
        
        curRuneEdit.subStat2.clear();
        curRuneEdit.subStat1.clear();
        for (int i=0;i<4;i++){    
            if (comboList.get(i).getSelectedIndex()>0){
                curRuneEdit.subStat1.add(comboList.get(i).getSelectedIndex()-1);
                curRuneEdit.subStat2.add(Application.getInt(textList.get(i)));
            }
        }  
        curRuneEdit.updateSubStats();
        curRuneEdit.mainStatVal = Application.getInt(jTextMainStat);
        Application.drawRune(curRuneEdit, jLabelRuneEdit);              
    }
    private void jFormattedTextField1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jFormattedTextField1PropertyChange
        // TODO add your handling code here:       
        updateSubStat();
    }//GEN-LAST:event_jFormattedTextField1PropertyChange

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
        updateSubStat();
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
        updateSubStat();
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
        updateSubStat();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        updateSubStat();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        // TODO add your handling code here:
        if (curRuneEdit!=null){
            String subs="";
            for (String s1:curRuneEdit.subStatGuis){
                subs+="\n "+s1;
            }
            int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to equip this Rune ? \n " + curRuneEdit.runeType+" ; slot="+curRuneEdit.slot
                    +"\n Lv ="+curRuneEdit.level+" ; mainStat="+curRuneEdit.mainStatGui+"="+curRuneEdit.mainStatVal
                    + subs
                    + "\nBe carefull, this will change the json File, Better back it up!", "Confirm", JOptionPane.YES_NO_OPTION);
            if (dialogResult == 0) {
                System.out.println("Yes");  
                curRuneEdit.upateRuneEdit();
                oldRuneEdit.loadJson(curRuneEdit.jsonData, curRuneEdit.optimizerFile, curRuneEdit.id);
                SwManager.saveFile();
                int oldFocus = jTableAllRunes.getSelectedRow();
                updateAllrunesTable();                
                jTableAllRunes.getSelectionModel().setSelectionInterval(oldFocus, oldFocus);
            }else
            {
                System.out.println("No");
            }
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jCheckEditedRunesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckEditedRunesActionPerformed
        // TODO add your handling code here:
        updateAllrunesTable();
    }//GEN-LAST:event_jCheckEditedRunesActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        jFrameRuneEdit.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jFormattedTextField2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jFormattedTextField2PropertyChange
        // TODO add your handling code here:
        updateSubStat();
    }//GEN-LAST:event_jFormattedTextField2PropertyChange

    private void jFormattedTextField3PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jFormattedTextField3PropertyChange
        // TODO add your handling code here:
        updateSubStat();
    }//GEN-LAST:event_jFormattedTextField3PropertyChange

    private void jFormattedTextField4PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jFormattedTextField4PropertyChange
        // TODO add your handling code here:
        updateSubStat();
    }//GEN-LAST:event_jFormattedTextField4PropertyChange

    private void jTextMainStatPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTextMainStatPropertyChange
        // TODO add your handling code here:
        updateSubStat();
    }//GEN-LAST:event_jTextMainStatPropertyChange

    private void jComboUngrindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboUngrindActionPerformed
        // TODO add your handling code here:
        updateAllrunesTable();
    }//GEN-LAST:event_jComboUngrindActionPerformed

    private void jComboLegendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboLegendActionPerformed
        // TODO add your handling code here:
        updateAllrunesTable();
    }//GEN-LAST:event_jComboLegendActionPerformed

    private void jComboOriginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboOriginActionPerformed
        // TODO add your handling code here:
        updateAllrunesTable();
    }//GEN-LAST:event_jComboOriginActionPerformed

    private void jCheckCountPowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckCountPowerActionPerformed
        // TODO add your handling code here:
        if (focusRune!=null){
            Application.drawRune(focusRune, jLabelSelectedRune,jCheckCountPower.isSelected());
        }
    }//GEN-LAST:event_jCheckCountPowerActionPerformed

    /**
     * @param args the command line arguments
     */
    public static boolean runAlone = false;
    public static void main(String args[]) {
        System.out.println("Run Rune Manage alone");
        runAlone = true;
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RuneManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RuneManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RuneManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RuneManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RuneManager().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonRuneSearch;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonViewAll;
    private javax.swing.JCheckBox jCheckCountPower;
    private javax.swing.JCheckBox jCheckEditedRunes;
    private javax.swing.JCheckBox jCheckStorageRunes;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboLegend;
    private javax.swing.JComboBox<String> jComboLevel;
    private javax.swing.JComboBox<String> jComboOrigin;
    private javax.swing.JComboBox jComboRunePet;
    private javax.swing.JComboBox jComboSearchRuneGrade;
    private javax.swing.JComboBox jComboSearchRuneLevel;
    private javax.swing.JComboBox<String> jComboSearchRuneSlot;
    private javax.swing.JComboBox jComboSearchRuneStat;
    private javax.swing.JComboBox<String> jComboSearchRuneType;
    private javax.swing.JComboBox<String> jComboUngrind;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JFormattedTextField jFormattedTextField4;
    private javax.swing.JFormattedTextField jFormattedTextField5;
    private javax.swing.JFrame jFrameRuneEdit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelRuneEdit;
    private javax.swing.JLabel jLabelRuneOwner;
    private javax.swing.JLabel jLabelSelectedRune;
    private javax.swing.JComboBox<String> jRuneSearchFilter;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableAllRunes;
    private javax.swing.JFormattedTextField jTextMainStat;
    private javax.swing.JTextField jTextNumRows;
    private javax.swing.JTextField jTextPetName;
    private javax.swing.JFormattedTextField jTextSearchRuneFilter;
    // End of variables declaration//GEN-END:variables
}
