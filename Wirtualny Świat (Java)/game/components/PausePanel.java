/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.strona.wirtualny_swiat.game.components;

import pl.strona.wirtualny_swiat.game.components.menupanels.MenuTitle;
import pl.strona.wirtualny_swiat.game.components.menupanels.MenuButton;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import pl.strona.wirtualny_swiat.Game;

/**
 *
 * @author SaySaeqo
 */
public class PausePanel extends JPanel {
  
  private final MenuButton resumeButton;
  private final MenuButton saveButton;
  private final MenuButton restartButton;
  private final MenuButton exitButton;
  private final MenuTitle title;
  
  public PausePanel(Game myGame){
    super(new GridBagLayout());
    JPanel innerPanel = new JPanel(new GridLayout(0,1));
    resumeButton = new MenuButton("Resume");
    saveButton = new MenuButton("Save");
    restartButton = new MenuButton("Restart");
    exitButton = new MenuButton("Exit");
    title = new MenuTitle("PAUSE");
    
    innerPanel.add(title);
    innerPanel.add(resumeButton);
    innerPanel.add(restartButton);
    innerPanel.add(saveButton);
    innerPanel.add(exitButton);
    add(innerPanel);
    
    // Ustawienie dzialania przycisków
    setFocusable(true);
    setEnabled(true);
    Action resumeGame = new AbstractAction("resume game"){
      @Override
      public void actionPerformed(ActionEvent event) {
        myGame.showGame();
      }
    };
    Action exitGame = new AbstractAction("exit game"){
      @Override
      public void actionPerformed(ActionEvent event) {
        myGame.dispose();
      }
    };
    Action saveGame = new AbstractAction("save game"){
      @Override
      public void actionPerformed(ActionEvent event) {
        myGame.saveGame();
      }
    };
    Action restartGame = new AbstractAction("restart game"){
      @Override
      public void actionPerformed(ActionEvent event) {
        myGame.restartGame();
      }
    };
    resumeButton.addActionListener(resumeGame);
    exitButton.addActionListener(exitGame);
    saveButton.addActionListener(saveGame);
    restartButton.addActionListener(restartGame);
    
    // Ustawienie KeyBinding
    getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("ESCAPE"), "exit game");
    getActionMap().put("exit game", exitGame);
  }
}
