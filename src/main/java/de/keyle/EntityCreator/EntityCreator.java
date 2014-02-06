/*
 * This file is part of DungeonCraft
 *
 * Copyright (C) 2013-2014 Keyle & xXLupoXx
 * DungeonCraft is licensed under the GNU Lesser General Public License.
 *
 * DungeonCraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DungeonCraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.keyle.EntityCreator;


import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.*;
import java.io.File;

public class EntityCreator {
    private  JButton loadButton;
    private JButton saveFileButton;
    private  JTree entityTemplateTree;
    private JButton createButton;
    private JButton editButton;
    static DefaultTreeModel entityTemplateTreeModel;
    private  EntityTemplateLoader loader;
    JFrame entityCreatorFrame;
    JPanel entityCreatorPanel;
    private JPanel templatePanel;
    private JComboBox mobTypeComboBox;
    private JCheckBox onFireCheckBox;
    private JButton saveButton1;
    private JComboBox combobox_Projectile;
    private JTextField field_Range_Damage;
    private JTextField field_Meele_Damage;
    private JTextField field_Template_ID;
    private JLabel label_template_ID;
    private JLabel label_Meele_Damage;
    private JLabel label_Range_Damage;
    private JLabel label_Projectile;
    private JLabel label_Age;
    private JTextField field_Age;
    private JCheckBox isAngryCheckBox;
    private JLabel label_armor;
    private JTextField field_Armor;
    private JCheckBox isBabyCheckBox;
    private JLabel label_CatType;
    private JComboBox catType_comboBox;
    private JCheckBox hasChestCheckBox;
    private JLabel label_color;
    private JComboBox color_comboBox;
    private JLabel label_horseType;
    private JComboBox horseType_comboBox;
    private JCheckBox isPoweredCheckBox;
    private JLabel label_profession;
    private JComboBox profession_comboBox;
    private JCheckBox hasSaddleCheckBox;
    private JCheckBox isSittingCheckBox;
    private JComboBox size_comboBox;
    private JCheckBox isTamedCheckBox;
    private JCheckBox isWitherCheckBox;
    private JComboBox variant_comboBox;
    private JComboBox helmet_comboBox;
    private JComboBox helmet_enchant_comboBox;
    private JComboBox chest_comboBox;
    private JComboBox chest_enchant_comboBox;
    private JComboBox leggins_comboBox;
    private JComboBox leggins_enchant_comboBox;
    private JComboBox boots_comboBox;
    private JComboBox boots_enchant_comboBox;
    private JComboBox weapon_comboBox;
    private JComboBox weapon_enchant_comboBox;
    private JTabbedPane editors_tabbedPane;

    String selectedMobtype;
    boolean editMode = false;

    public EntityCreator() {
        mobTypeComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    selectedMobtype = mobTypeComboBox.getSelectedItem().toString();
                    if(selectedMobtype.equals("Blaze")) {
                        showEntityTemplateID(false,null);
                        showMeeleDamage(false,null);
						showRangeDamage(false,null);
						showFire(false,null); 
						showSave();
                    } else {
                           hideAllTemplatePanel();
                    }
                }
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editMode = true;
            }
        });

        entityTemplateTree.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (entityTemplateTree.getSelectionPath().getPath().length == 2) {
                    if (entityTemplateTree.getSelectionPath().getPathComponent(1) instanceof TemplateNode) {
                        EntityTemplate template = ((TemplateNode) entityTemplateTree.getSelectionPath().getPathComponent(1)).getEntityTemplate();
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_ENTER:
                                hideAllTemplatePanel();
                                if(template.getEntityType().equals("Blaze")) {
                                    showEntityTemplateID(true, template);
                                    showMeeleDamage(true, template);
									showRangeDamage(true,template);
									showFire(true,template);                                    
                                }
								showSave();
                                break;
                            case KeyEvent.VK_DELETE:
                               /* selectedMobtype.removeSkillTree(template.getName());
                                skilltreeTreeSetSkilltrees();    */
                                break;
                        }
                    }
                }

            }
        });
        loader = new EntityTemplateLoader();
        loadCreaturesInTemplate(new File("D:" +File.separator + "Minecraft Testserver" +File.separator + "1.7.2" +File.separator + "plugins" +File.separator + "DungeonCraft" +File.separator + "dungeons" +File.separator + "test1" + File.separator + "entity-templates.json"));
    }

    public void createUIComponents() {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
        entityTemplateTreeModel = new DefaultTreeModel(root);
        entityTemplateTree = new JTree(entityTemplateTreeModel);
        entityTemplateTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        mobTypeComboBox = new JComboBox(GuiMain.mobTypes);

        //hideAllTemplatePanel();


    }
    public void templateTreeExpandAll() {
        for (int i = 0; i < entityTemplateTree.getRowCount(); i++) {
            entityTemplateTree.expandRow(i);
        }
    }

    public JFrame getFrame() {
        if (entityCreatorFrame == null) {
            entityCreatorFrame = new JFrame("EntityCreator - DungeonCraft");
        }
        return entityCreatorFrame;
    }

    public JTabbedPane getMainPanel() { return editors_tabbedPane; }

    public void loadCreaturesInTemplate(File templateFile) {

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Templates");
        entityTemplateTreeModel.setRoot(rootNode);

        for(EntityTemplate entityTemplate : loader.loadCreaturesInTemplate(templateFile)) {
            TemplateNode templateNode = new TemplateNode(entityTemplate);
            rootNode.add(templateNode);
        }
        templateTreeExpandAll();

    }
    private class TemplateNode extends DefaultMutableTreeNode {
        private EntityTemplate template;

        public TemplateNode(EntityTemplate template) {
            super(template.getTemplateId() + " - MobType: " + template.getEntityType());
            this.template = template;
        }

        public EntityTemplate getEntityTemplate() {
            return template;
        }
    }

	
	public void showMeeleDamage(boolean edit, EntityTemplate template) {
		label_Meele_Damage.setVisible(true);
		field_Meele_Damage.setVisible(true);

		if(edit) {	
			for(Component c : template.getComponents()) {
				if(c.getMeeleDamage() != null) {
					field_Meele_Damage.setText(c.getMeeleDamage().toString());
				}
			}
		}
	}
	
	public void showEntityTemplateID(boolean edit, EntityTemplate template) {
		label_template_ID.setVisible(true);
		field_Template_ID.setVisible(true);
		
		if(edit) {
			field_Template_ID.setText(template.getTemplateId());
		}
	}
	
	public void showRangeDamage(boolean edit, EntityTemplate template) {
		label_Range_Damage.setVisible(true);
		field_Range_Damage.setVisible(true);
		label_Projectile.setVisible(true);
		combobox_Projectile.setVisible(true);
		
		if(edit) {	
			for(Component c : template.getComponents()) {
				if(c.getRangeDamage() != null) {
					field_Range_Damage.setText(c.getRangeDamage().toString());
					combobox_Projectile.setSelectedItem(c.getProjectile());
				}
			}
		}
	}

	public void showFire(boolean edit, EntityTemplate template) {
		onFireCheckBox.setVisible(true);

		if(edit) {	
			for(Component c : template.getComponents()) {
				if(c.getFire() != null) {
					onFireCheckBox.setSelected(c.getFire());
				}
			}
        }
	}
	
	public void showSave() {
		saveButton1.setVisible(true);
	}

    public void hideAllTemplatePanel() {
        field_Meele_Damage.setVisible(false);
        field_Range_Damage.setVisible(false);
        field_Template_ID.setVisible(false);
        combobox_Projectile.setVisible(false);
        saveButton1.setVisible(false);
        onFireCheckBox.setVisible(false);
        label_Meele_Damage.setVisible(false);
        label_Range_Damage.setVisible(false);
        label_template_ID.setVisible(false);
        label_Projectile.setVisible(false);
    }
}