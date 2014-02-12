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
import de.keyle.dungeoncraft.editor.editors.world.render.Texture;
import de.keyle.dungeoncraft.editor.editors.world.render.WorldViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.blockArray;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.blockCollection;

/**
 * A dialog to both show and set keybindings
 */
public class BlockBindDialog
        extends JFrame {
    private static final int FRAMEWIDTH = 450;
    private static final int FRAMEHEIGHT = 520;

    private static String window_title = "X-Ray Block Highlight Binding";
    private JButton okButton;
    private JButton cancelButton;

    private GridBagLayout gridBagLayoutManager;

    private static boolean dialog_showing = false;
    private static BlockBindDialog keyhelp_dialog;

    private WorldViewer worldViewerInstance;

    public static Image iconImage;

    private short[] ore_highlights;
    private BlockBindMainButton[] blockButtons;

    private HashMap<Short, ImageIcon> ore_icons;

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

        JLabel titleLabel = new JLabel(window_title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        String defaultStatusText = "Click on the buttons to change bindings.";
        JLabel statusLabel = new JLabel(defaultStatusText);
        Insets standardInsets = new Insets(5, 5, 5, 5);
        new Insets(20, 5, 5, 5);
        new Insets(5, 5, 0, 5);
        Insets noTopInsets = new Insets(0, 5, 5, 5);
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

        // Our list of assigned highlights
        int current_grid_y = 0;
        this.blockButtons = new BlockBindMainButton[this.ore_highlights.length];
        for (int i = 0; i < this.ore_highlights.length; i++) {
            current_grid_y++;
            c.weightx = 1f;
            c.weighty = 0f;
            c.gridx = 0;
            c.gridy = current_grid_y;
            c.insets = standardInsets;
            c.anchor = GridBagConstraints.EAST;
            addComponent(blockPanel, new JLabel("Block Highlight " + (i + 1) + ": "), c, blockLayout);

            c.anchor = GridBagConstraints.WEST;
            c.gridx = 1;
            BlockBindMainButton blockButton = new BlockBindMainButton(blockArray[this.ore_highlights[i]], this.ore_icons, this, i);
            this.blockButtons[i] = blockButton;
            addComponent(blockPanel, blockButton, c, blockLayout);
        }

        current_grid_y = 0;

        // A title
        current_grid_y++;
        c.weightx = 1f;
        c.weighty = 0f;
        c.gridwidth = 2;
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

        // Add our scrollpane to the window
        current_grid_y++;
        c.weightx = 1f;
        c.weighty = 1f;
        c.gridx = 0;
        c.gridy = current_grid_y;
        c.fill = GridBagConstraints.BOTH;
        addComponent(this.getContentPane(), blockScroll, c);

        // Now add the buttons
        c.insets = new Insets(5, 15, 5, 15);

        current_grid_y++;
        c.weightx = 1f;
        c.weighty = 0f;
        c.gridx = 0;
        c.gridy = current_grid_y;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.HORIZONTAL;
        addComponent(this.getContentPane(), cancelButton, c);

        c.gridx = 1;
        addComponent(this.getContentPane(), okButton, c);
    }

    /**
     * One of our highlighting buttons has been clicked
     *
     * @param whichButton The button that was clicked
     */
    public void notifyHighlightClicked(BlockBindMainButton whichButton) {
        BlockBindChooserDialog dialog = new BlockBindChooserDialog(this.ore_icons, this);
        BlockBindButton newButton = dialog.getClickedButton();
        if (newButton != null) {
            if (whichButton.getBlock() != newButton.getBlock()) {
                whichButton.setBlock(newButton.getBlock());
            }
        }
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
        okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialogOK();
            }
        });

        // The "Cancel" button
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialogCancel();
            }
        });

        // Key mapping for the Jump button
        KeyStroke enterStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(enterStroke, "ENTER");
        rootPane.getActionMap().put("ENTER", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                dialogOK();
            }
        });

        // Key mapping for the Cancel button
        KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, "ESCAPE");
        rootPane.getActionMap().put("ESCAPE", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                dialogCancel();
            }
        });
    }

    /**
     * Actions to perform if our master "OK" button has been hit
     */
    private void dialogOK() {
        boolean changed = false;
        for (BlockBindMainButton button : this.blockButtons) {
            short new_id = button.getBlock().getId();
            int position = button.getPosition();
            if (new_id != ore_highlights[position]) {
                changed = true;
                ore_highlights[position] = new_id;
            }
        }

        if (changed) {
            this.worldViewerInstance.updateHighlightBindings();
        }

        this.dialogCancel();
    }

    /**
     * Actions to perform if our master "Cancel" button has been hit
     */
    private void dialogCancel() {
        setVisible(false);
        dispose();
        BlockBindDialog.dialog_showing = false;
        synchronized (BlockBindDialog.this) {
            BlockBindDialog.this.notify();
        }
    }

    /**
     * Creates a new BlockBindDialog
     *
     * @param ore_highlights      the current ore highlight settings
     * @param ore_textures        the title of the dialog
     * @param worldViewerInstance
     */
    protected BlockBindDialog(short[] ore_highlights, ArrayList<Texture> ore_textures, WorldViewer worldViewerInstance) {
        super(window_title);
        this.ore_highlights = ore_highlights;
        this.worldViewerInstance = worldViewerInstance;

        // First up, let's create a bunch of ImageIcons
        this.ore_icons = new HashMap<Short, ImageIcon>();
        for (int texnum = 0; texnum < ore_textures.size(); texnum++) {
            BufferedImage image = ore_textures.get(texnum).getImage();
            if (image.getWidth() > 256) {
                // Resize the image down so its blocks are 16x16
                BufferedImage newImage = new BufferedImage(256, 512, image.getType());
                Graphics2D g = newImage.createGraphics();
                g.drawImage(image, 0, 0, 256, 512, null);
                g.dispose();
                image = newImage;
            }

            for (BlockType block : blockCollection.getBlocksFull()) {
                if (block.getTexSheet() == texnum) {
                    int[] coords = block.getTexCoordsArr();
                    this.ore_icons.put(block.getId(), new ImageIcon(image.getSubimage(coords[0] * 16, coords[1] * 16, 16, 16)));
                }
            }
        }

        // Now continue
        if (BlockBindDialog.iconImage != null) {
            this.setIconImage(BlockBindDialog.iconImage);
        }

        this.setSize(FRAMEWIDTH, FRAMEHEIGHT);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent e) {
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                dialogOK();
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
     * Pops up the dialog window
     *
     * @param ore_highlights      the current ore highlight settings
     * @param ore_textures        the title of the dialog
     * @param worldViewerInstance
     */
    public static void presentDialog(short[] ore_highlights, ArrayList<Texture> ore_textures, WorldViewer worldViewerInstance) {
        if (dialog_showing) {
            BlockBindDialog.keyhelp_dialog.toFront();
            BlockBindDialog.keyhelp_dialog.requestFocus();
        } else {
            BlockBindDialog.dialog_showing = true;
            BlockBindDialog.keyhelp_dialog = new BlockBindDialog(ore_highlights, ore_textures, worldViewerInstance);
        }
    }

    /**
     * Closes out our dialog
     */
    public static void closeDialog() {
        if (BlockBindDialog.dialog_showing && BlockBindDialog.keyhelp_dialog != null) {
            BlockBindDialog.keyhelp_dialog.dialogOK();
        }
    }
}