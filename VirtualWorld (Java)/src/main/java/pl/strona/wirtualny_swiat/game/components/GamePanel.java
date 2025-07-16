/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.strona.wirtualny_swiat.game.components;

import pl.strona.wirtualny_swiat.game.components.gamepanel.EventPanel;
import pl.strona.wirtualny_swiat.game.components.gamepanel.GameMap;
import pl.strona.wirtualny_swiat.game.components.gamepanel.InfoPanel;
import pl.strona.wirtualny_swiat.game.components.gamepanel.PowerPanel;
import pl.strona.wirtualny_swiat.game.components.gamepanel.RoundPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import pl.strona.wirtualny_swiat.Game;
import pl.strona.wirtualny_swiat.game.mechanics.Organism;
import pl.strona.wirtualny_swiat.game.mechanics.World;
import pl.strona.wirtualny_swiat.game.mechanics.animals.Human;

/**
 *
 * @author SaySaeqo
 */
public class GamePanel extends JPanel {
  
  public final RoundPanel roundPanel;
  public final PowerPanel powerPanel;
  private final InfoPanel infoPanel;
  public final GameMap gameMap;
  public final EventPanel logs;
  
  public GamePanel(Game myGame){
    super(new GridBagLayout());
    setBackground(Color.decode("#800000"));
    // Mapa świata
    gameMap = new GameMap(new World(this, new Dimension(30,20)));
    
    // Info o turze
    roundPanel = new RoundPanel();
    
    // Info o sile gracza
    powerPanel = new PowerPanel();
    
    // Info o przyciskach funkcyjnych
    infoPanel = new InfoPanel();
    
    // Miejsce na opis wydarzeń z gry
    logs = new EventPanel();
    
    // Ustawienie względne komponentów
    GridBagConstraints con = new GridBagConstraints();
    con.fill = GridBagConstraints.BOTH;
    con.anchor = GridBagConstraints.CENTER;
    con.gridx = 0;
    con.gridy = 0;
    con.weightx= 1;
    con.weighty= 1;
    add(roundPanel, con);
    con.gridx = GridBagConstraints.RELATIVE;
    con.weightx = 0.1;
    add(powerPanel, con);
    con.weightx = 0.5;
    add(infoPanel, con);
    con.fill = GridBagConstraints.NONE;
    con.gridx = 0;
    con.gridy = 1;
    con.gridwidth= 3;
    con.insets = new java.awt.Insets(5,5,5,5);
    con.weightx=0;
    con.weighty=0;
    add(gameMap, con);
    con.fill = GridBagConstraints.BOTH;
    con.gridx = GridBagConstraints.RELATIVE;
    con.gridy = 0;
    con.gridheight = GridBagConstraints.REMAINDER;
    con.insets = new java.awt.Insets(0,0,0,0);
    con.weightx = 1;
    con.weighty = 1;
    add(logs, con);
    // Author info
    con.gridx = 0;
    con.gridy = 2;
    con.fill = GridBagConstraints.HORIZONTAL;
    JPanel authorPanel = new JPanel();
    authorPanel.add(new JLabel("Postaraj się nie zginąć C;", SwingConstants.CENTER));
    add(authorPanel, con);
    // -----------

    // Ustawianie KeyListenera
    setFocusable(true);
    setEnabled(true);
    Action pauseGame = new AbstractAction("pause game"){
      @Override
      public void actionPerformed(ActionEvent event) {
        logs.clear();
        myGame.pack();
        myGame.showMenu();
      }
    };
    getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("ESCAPE"), "pause");
    getActionMap().put("pause", pauseGame);

  }
  
  @Override
  public String toString(){
    World w = gameMap.getWorld();
    String save = new String();
    save += String.valueOf(w.getSize().width);
    save += ",";
    save += String.valueOf(w.getSize().height);
    save += "-";
    save += String.valueOf(w.getRound());
    save += "\n";
    for(Organism org : w.getQueue()){
      save += org.getClass().getSimpleName();
      save += "-";
      save += org.getAge();
      save += "-";
      save += org.getPower();
      save += "-";
      save += String.valueOf(org.getPosition().x);
      save += ",";
      save += String.valueOf(org.getPosition().y);
      if(org instanceof Human){
        save += "-";
        save += String.valueOf(((Human) org).getPoints());
        save += "-";
        save += String.valueOf(((Human) org).getTimeUntilSpecialAbilityReadyToUse());
      }
      save += "\n";
    }
    return save;
  }
}