/**
 * Copyright (c) 2010-2012, Christopher J. Kucera
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Minecraft X-Ray team nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL VINCENT VOLLERS OR CJ KUCERA BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.keyle.dungeoncraft.editor.editors.world.render.dialog;

import de.keyle.dungeoncraft.editor.editors.world.render.BlockType;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.blockCollection;

/**
 * A dialog to both show and set keybindings
 */
public class BlockBindChooserDialog
        extends JDialog
        implements DocumentListener {
    private static final int FRAMEWIDTH = 450;
    private static final int FRAMEHEIGHT = 620;

    private static String window_title = "WorldViewer Set Block Highlighting";
    private JButton cancelButton;

    private GridBagLayout gridBagLayoutManager;

    private HashMap<Short, ImageIcon> ore_icons;
    private JTextField searchField;
    private ArrayList<BlockBindChooserButton> blockButtons;

    private BlockBindChooserButton clickedButton;

    /**
     * Centers this dialog on the screen
     */
    private void centerDialogOnScreen() {
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension screenSize = t.getScreenSize();

        int x = (screenSize.width / 2) - (this.getWidth() / 2);
        int y = (screenSize.height / 2) - (this.getHeight() / 2);

        gridBagLayoutManager = new GridBagLayout();

        this.setLocation(x, y);
    }

    /**
     * Layouts all the controls and labels on the dialog using a gridbaglayout
     */
    private void layoutControlsOnDialog() {

        this.getContentPane().setLayout(gridBagLayoutManager);
        GridBagConstraints c = new GridBagConstraints();

        float flist = 1.9f;

        JLabel titleLabel = new JLabel(window_title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel statusLabel = new JLabel("Click on the buttons to change bindings.");

        Insets standardInsets = new Insets(5, 5, 5, 5);
        new Insets(20, 5, 5, 5);
        new Insets(5, 5, 0, 5);
        Insets noTopInsets = new Insets(0, 5, 5, 5);
        Insets buttonInsets = new Insets(5, 20, 5, 20);
        c.insets = standardInsets;
        c.weighty = .1f;

        // Scrollpane to put stuff into
        JPanel blockPanel = new JPanel();
        GridBagLayout blockLayout = new GridBagLayout();
        blockPanel.setLayout(blockLayout);
        JScrollPane blockScroll = new JScrollPane(blockPanel);
        blockScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        blockScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        blockScroll.setBorder(null);

        int current_grid_y = 0;
        // Our list of assigned highlights
        this.blockButtons = new ArrayList<BlockBindChooserButton>();
        for (BlockType block : blockCollection.getBlocksFullSorted()) {
            current_grid_y++;
            c.weightx = 1f;
            c.weighty = 0f;
            c.gridx = 0;
            c.gridy = current_grid_y;
            c.insets = buttonInsets;
            c.anchor = GridBagConstraints.NORTHWEST;
            BlockBindChooserButton blockButton = new BlockBindChooserButton(block, this.ore_icons, this);
            this.blockButtons.add(blockButton);
            addComponent(blockPanel, blockButton, c, blockLayout);
        }

        // This empty JLabel is only here because I wasn't finding an actual reasonable
        // way to have the various buttons stay at the top of the scrollpane when
        // searching (when there weren't enough to fill up the entire pane)
        current_grid_y++;
        c.gridy = current_grid_y;
        c.weighty = 1f;
        addComponent(blockPanel, new JLabel(""), c, blockLayout);

        current_grid_y = 0;

        // A title
        current_grid_y++;
        c.weightx = 1f;
        c.weighty = 0f;
        c.gridx = 0;
        c.gridy = current_grid_y;
        c.anchor = GridBagConstraints.CENTER;
        addComponent(this.getContentPane(), titleLabel, c);

        // Link
        current_grid_y++;
        c.gridx = 0;
        c.gridy = current_grid_y;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = noTopInsets;
        addComponent(this.getContentPane(), statusLabel, c);
        c.insets = standardInsets;

        // Search pane
        current_grid_y++;
        c.gridx = 0;
        c.gridy = current_grid_y;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1f;
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        addComponent(this.getContentPane(), searchPanel, c);

        // Contents of search pane
        this.searchField = new JTextField(20);
        this.searchField.getDocument().addDocumentListener(this);
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(this.searchField);

        // Add our scrollpane to the window
        current_grid_y++;
        c.weightx = 1f;
        c.weighty = 1f;
        c.gridx = 0;
        c.gridy = current_grid_y;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTHWEST;
        addComponent(this.getContentPane(), blockScroll, c);

        // Now add the buttons
        c.insets = new Insets(5, 15, 5, 15);

        current_grid_y++;
        c.weightx = flist;
        c.weighty = 0f;
        c.gridx = 0;
        c.gridy = current_grid_y;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.HORIZONTAL;
        addComponent(this.getContentPane(), cancelButton, c);
    }

    /**
     * One of our buttons has been clicked
     *
     * @param whichButton The button that was clicked
     */
    public void notifyHighlightClicked(BlockBindChooserButton whichButton) {
        this.clickedButton = whichButton;
        this.dialogCancel();
    }

    /**
     * Returns the selected block type
     */
    public BlockBindButton getClickedButton() {
        return this.clickedButton;
    }

    /**
     * Adds a component to the container and updates the constraints for that component
     *
     * @param root        The contiainer to add the component to
     * @param comp        The component to add to the container
     * @param constraints The constraints which affect the component
     */
    private void addComponent(Container root, Component comp, GridBagConstraints constraints) {
        gridBagLayoutManager.setConstraints(comp, constraints);
        root.add(comp);
    }

    /**
     * Adds a component to the container and updates the constraints for that component
     *
     * @param root        The contiainer to add the component to
     * @param comp        The component to add to the container
     * @param constraints The constraints which affect the component
     * @param manager     The GridBagLayout to operate on
     */
    private void addComponent(Container root, Component comp, GridBagConstraints constraints, GridBagLayout manager) {
        manager.setConstraints(comp, constraints);
        root.add(comp);
    }

    /**
     * Builds the Go and Exit Buttons and attaches the actions to them
     */
    private void buildButtons() {
        JRootPane rootPane = this.getRootPane();

        // The "OK" button
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialogCancel();
            }
        });

        // Key mapping for the Jump button
        KeyStroke enterStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(enterStroke, "ENTER");
        KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, "ENTER");
        rootPane.getActionMap().put("ENTER", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                dialogCancel();
            }
        });
    }

    /**
     * Actions to perform if our master "OK" button has been hit
     */
    private void dialogCancel() {
        setVisible(false);
        dispose();
        synchronized (BlockBindChooserDialog.this) {
            BlockBindChooserDialog.this.notify();
        }
    }

    /**
     * Creates a new BlockBindChooserDialog
     *
     * @param ore_icons the current ore highlight settings
     * @param parent    the title of the dialog
     */
    protected BlockBindChooserDialog(HashMap<Short, ImageIcon> ore_icons, Frame parent) {
        super(parent, window_title, true);
        this.ore_icons = ore_icons;
        this.clickedButton = null;

        // Now continue
        this.setSize(FRAMEWIDTH, FRAMEHEIGHT);
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent e) {
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                dialogCancel();
            }

            public void windowDeactivated(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowOpened(WindowEvent e) {
            }
        });
        this.setMinimumSize(new Dimension(FRAMEWIDTH, FRAMEHEIGHT));

        centerDialogOnScreen();

        buildButtons();
        layoutControlsOnDialog();

        validate();

        this.setVisible(true);
    }

    /**
     * DocumentListener Functions
     */
    public void changedUpdate(DocumentEvent e) {
    }

    public void insertUpdate(DocumentEvent e) {
        String text = this.searchField.getText();
        for (BlockBindChooserButton button : this.blockButtons) {
            if (text.length() == 0 || button.getBlock().matches(text)) {
                button.setVisible(true);
            } else {
                button.setVisible(false);
            }
        }
    }

    public void removeUpdate(DocumentEvent e) {
        this.insertUpdate(e);
    }
}