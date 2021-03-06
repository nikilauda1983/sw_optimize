package com.swrunes.gui;

import com.swrunes.swrunes.ConfigInfo;
import com.swrunes.swrunes.Crawler;
import com.swrunes.swrunes.PetType;
import com.swrunes.swrunes.RuneType;
import com.swrunes.swrunes.RuneType.RuneSet;
import com.swrunes.swrunes.SwManager;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static com.swrunes.gui.Application.showPetFinalStats;

/**
 * @author tuanha
 */
public class PetCompare extends javax.swing.JFrame {

    public static PetType curpet1, curpet2;
    static PetCompare instance;
    boolean loadingData = false;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JComboBox jComboPets1;
    private JComboBox jComboPets2;
    private JComboBox jComboRunes1;
    private JComboBox jComboRunes2;
    private JComboBox<String> jComboSkill1;
    private JComboBox<String> jComboSkill2;
    private JLabel jLabelIcon1;
    private JLabel jLabelIcon2;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane10;
    private JScrollPane jScrollPane9;
    private JTable jTable1;
    private JTable jTableExtraPetInfo1;
    private JTable jTableExtraPetInfo2;

    /**
     * Creates new form PetCompare
     */
    public PetCompare() {

        UIManager.getLookAndFeelDefaults()
                .put("defaultFont", new Font("Tahoma", Font.PLAIN, 14));

        initComponents();
        loadData();
        updateAllContents();
    }

    public static PetType detectPet(String s2) {
        PetType curpet2 = SwManager.pets.get(s2.toLowerCase());
        if (curpet2 == null) {
            curpet2 = SwManager.petsBestiary.get(s2);
        }
        return curpet2;
    }

    public static PetCompare getInstance() {
        if (instance == null) {
            instance = new PetCompare();
        }
        return instance;
    }

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PetCompare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PetCompare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PetCompare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PetCompare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new PetCompare().setVisible(true));
    }

    void loadData() {
        loadingData = true;
        if (SwManager.runes.size() == 0) {
            SwManager.getInstance().loadPets("optimizer.json");
        }
        jComboPets1.removeAllItems();
        TreeSet<String> tr1 = new TreeSet();
        for (PetType p : SwManager.pets.values()) {
            if (p.stars < (6 - ConfigInfo.getInstance().loadPetLevel)) {
                continue;
            }
            tr1.add(p.name);
        }
        for (String s1 : tr1) {
            jComboPets1.addItem(s1);
        }

        jComboPets2.removeAllItems();
        tr1.clear();
        for (PetType p : SwManager.petsBestiary.values()) {
            if (!p.name.contains("("))
                tr1.add(p.name);
        }
        for (String s1 : tr1) {
            jComboPets2.addItem(s1);
        }

        jComboRunes1.removeAllItems();
        jComboRunes1.addItem("Current Equip Runes");

        jComboRunes2.removeAllItems();
        jComboRunes2.addItem("Current Equip Runes");
        for (PetType p1 : SwManager.pets.values()) {
            if (p1.equipRuneList.size() >= 6) {
                jComboRunes2.addItem(p1.name + "      Runes");
            }
        }
        loadingData = false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboPets1 = new javax.swing.JComboBox();
        jComboPets2 = new javax.swing.JComboBox();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTableExtraPetInfo2 = new javax.swing.JTable();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTableExtraPetInfo1 = new javax.swing.JTable();
        jComboRunes2 = new javax.swing.JComboBox();
        jComboRunes1 = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabelIcon2 = new javax.swing.JLabel();
        jLabelIcon1 = new javax.swing.JLabel();
        jComboSkill1 = new javax.swing.JComboBox<String>();
        jComboSkill2 = new javax.swing.JComboBox<String>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jComboPets1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboPets1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboPets1ActionPerformed(evt);
            }
        });

        jComboPets2.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboPets2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboPets2ActionPerformed(evt);
            }
        });

        jTableExtraPetInfo2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane9.setViewportView(jTableExtraPetInfo2);

        jTableExtraPetInfo1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane10.setViewportView(jTableExtraPetInfo1);

        jComboRunes2.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));

        jComboRunes1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String[]{
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabelIcon2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabelIcon2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelIcon2MouseClicked(evt);
            }
        });

        jLabelIcon1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabelIcon1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelIcon1MouseClicked(evt);
            }
        });

        jComboSkill1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboSkill1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboSkill1ActionPerformed(evt);
            }
        });

        jComboSkill2.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboSkill2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboSkill2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(163, 163, 163))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(83, 83, 83)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabelIcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboPets1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboRunes1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboSkill1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabelIcon2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboPets2, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jComboRunes2, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jComboSkill2, javax.swing.GroupLayout.Alignment.LEADING, 0, 206, Short.MAX_VALUE)))
                                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(7, 7, 7)
                                                .addComponent(jLabelIcon2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jComboPets2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabelIcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jComboPets1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboRunes2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboRunes1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboSkill1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboSkill2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void updateAllContents() {
        if (loadingData) return;
        //System.out.println("curpet : "+jComboPets1.getSelectedItem().toString());
        String s1 = jComboPets1.getSelectedItem().toString();
        String s2 = jComboPets2.getSelectedItem().toString();

        boolean reload1 = false;
        boolean reload2 = false;

        if (s1 != null && (curpet1 == null || !s1.equalsIgnoreCase(curpet1.name))) {
            curpet1 = SwManager.pets.get(s1.toLowerCase());
            reload1 = true;
        }

        if (s2 != null && (curpet2 == null || !s2.equalsIgnoreCase(curpet2.name))) {
            curpet2 = detectPet(s2);
            reload2 = true;
        }

        if (curpet1 != null) {
            if (reload1) {
                BufferedImage bf = Crawler.crawlPetPicture(curpet1.a_name);
                //System.out.println(curpet1.oname+" ; "+curpet2.oname+" ; "+bf);
                if (bf != null) {
                    jLabelIcon1.setSize(bf.getWidth(), bf.getHeight());
                    jLabelIcon1.setIcon(new ImageIcon(bf));
                    jLabelIcon1.invalidate();
                }
                jComboSkill1.removeAllItems();
                int k1 = 1;
                for (PetType.RuneSkill k : curpet1.skillList) {
                    jComboSkill1.addItem("" + k1 + ":" + k.skillName);
                    k1++;
                }
                jComboSkill1.setSelectedIndex(k1 - 2);
            }
            showPetFinalStats(curpet1, jTableExtraPetInfo1);
        }
        if (curpet2 != null) {
            if (curpet1.runesEquip == 6 && !SwManager.pets.containsKey(s2.toLowerCase())) {
                List<RuneType> p1 = new ArrayList();
                for (RuneType p2 : curpet1.currentEquip.set) {
                    p1.add(p2);
                }
                RuneSet s5 = new RuneType.RuneSet(p1);
                s5.equipOnPet(curpet2);
                curpet2.currentEquip = s5;
                curpet2.runesEquip = 6;
            }
            if (reload2) {
                BufferedImage bf = Crawler.crawlPetPicture(curpet2.a_name);
                if (bf != null) {
                    jLabelIcon2.setSize(bf.getWidth(), bf.getHeight());
                    jLabelIcon2.setIcon(new ImageIcon(bf));
                }
                jComboSkill2.removeAllItems();
                int k1 = 1;
                for (PetType.RuneSkill k : curpet2.skillList) {
                    jComboSkill2.addItem("" + k1 + ":" + k.skillName);
                    k1++;
                }
                jComboSkill2.setSelectedIndex(k1 - 2);
            }
            showPetFinalStats(curpet2, jTableExtraPetInfo2);
        }
    }

    private void jComboPets1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboPets1ActionPerformed
        // TODO add your handling code here:
        updateAllContents();
    }//GEN-LAST:event_jComboPets1ActionPerformed

    private void jComboPets2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboPets2ActionPerformed
        // TODO add your handling code here:
        updateAllContents();
    }//GEN-LAST:event_jComboPets2ActionPerformed

    private void jComboSkill1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboSkill1ActionPerformed
        // TODO add your handling code here:
        if (loadingData) return;
        if (jComboSkill1.getSelectedIndex() >= 0) {
            curpet1.skillItem = curpet1.skillList.get(jComboSkill1.getSelectedIndex());
            updateAllContents();
        }
    }//GEN-LAST:event_jComboSkill1ActionPerformed

    private void jComboSkill2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboSkill2ActionPerformed
        // TODO add your handling code here:
        if (loadingData) return;
        if (jComboSkill2.getSelectedIndex() >= 0) {
            curpet2.skillItem = curpet2.skillList.get(jComboSkill2.getSelectedIndex());
            updateAllContents();
        }
    }//GEN-LAST:event_jComboSkill2ActionPerformed

    private void jLabelIcon1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelIcon1MouseClicked
        // TODO add your handling code here:
        //System.out.println("Mouse click : ");
        PetManager.getInstance().showDialogDetail(curpet1.name);
    }//GEN-LAST:event_jLabelIcon1MouseClicked

    private void jLabelIcon2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelIcon2MouseClicked
        // TODO add your handling code here:
        PetManager.getInstance().showDialogDetail(curpet2.name);
    }//GEN-LAST:event_jLabelIcon2MouseClicked
    // End of variables declaration//GEN-END:variables
}
