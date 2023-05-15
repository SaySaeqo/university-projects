/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.strona.wirtualny_swiat.game.components;

/**
 *
 * @author SaySaeqo
 */

import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import pl.strona.wirtualny_swiat.Game;
import pl.strona.wirtualny_swiat.game.components.menupanels.MenuButton;
import pl.strona.wirtualny_swiat.game.components.menupanels.MenuTitle;

public class MenuPanel extends JPanel {
  
  private final MenuButton startButton;
  private final MenuButton loadButton;
  private final MenuButton exitButton;
  private final MenuTitle title;
  
  public MenuPanel(Game myGame){
    super(new GridBagLayout());
    JPanel innerPanel = new JPanel(new GridLayout(0,1));
    startButton = new MenuButton("Start");
    loadButton = new MenuButton("Load");
    exitButton = new MenuButton("Exit");
    title = new MenuTitle("MENU");
    
    innerPanel.add(title);
    innerPanel.add(startButton);
    innerPanel.add(loadButton);
    innerPanel.add(exitButton);
    add(innerPanel);
    
    // Ustawienie dzialania przycisków
    setFocusable(true);
    setEnabled(true);
    Action startGame = new AbstractAction("start game"){
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
    Action loadGame = new AbstractAction("load game"){
      @Override
      public void actionPerformed(ActionEvent event) {
        myGame.loadGame();
      }
    };
    startButton.addActionListener(startGame);
    exitButton.addActionListener(exitGame);
    loadButton.addActionListener(loadGame);
    
    // Ustawienie KeyBinding
    getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("ESCAPE"), "exit game");
    getActionMap().put("exit game", exitGame);
  }
}
