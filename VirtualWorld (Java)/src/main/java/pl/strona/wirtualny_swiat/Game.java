/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.strona.wirtualny_swiat;

/**
 *
 * @author SaySaeqo
 */
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.LinkedList;
import javax.swing.JFrame;
import pl.strona.wirtualny_swiat.game.components.GamePanel;
import pl.strona.wirtualny_swiat.game.components.MenuPanel;
import pl.strona.wirtualny_swiat.game.components.PausePanel;
import pl.strona.wirtualny_swiat.game.mechanics.Organism;
import pl.strona.wirtualny_swiat.game.mechanics.World;
import pl.strona.wirtualny_swiat.game.mechanics.animals.Antelope;
import pl.strona.wirtualny_swiat.game.mechanics.animals.CyberSheep;
import pl.strona.wirtualny_swiat.game.mechanics.animals.Fox;
import pl.strona.wirtualny_swiat.game.mechanics.animals.Human;
import pl.strona.wirtualny_swiat.game.mechanics.animals.Sheep;
import pl.strona.wirtualny_swiat.game.mechanics.animals.Turtle;
import pl.strona.wirtualny_swiat.game.mechanics.animals.Wolf;
import pl.strona.wirtualny_swiat.game.mechanics.plants.DeadlyNightshade;
import pl.strona.wirtualny_swiat.game.mechanics.plants.Grass;
import pl.strona.wirtualny_swiat.game.mechanics.plants.Guarana;
import pl.strona.wirtualny_swiat.game.mechanics.plants.SosnowskysHogweed;
import pl.strona.wirtualny_swiat.game.mechanics.plants.SowThistle;

public class Game extends JFrame {
  
  private final MenuPanel menuPanel;
  private final GamePanel gamePanel;
  private final PausePanel pausePanel;

  public Game() {
    super("Wirtualny Swiat");
    menuPanel = new MenuPanel(this);
    gamePanel = new GamePanel(this);
    pausePanel = new PausePanel(this);
    setLayout(new CardLayout());
    add(gamePanel, "game");
    add(pausePanel, "pause");
    add(menuPanel, "menu");
    CardLayout cl = (CardLayout)getContentPane().getLayout();
    cl.show(getContentPane(), "menu");
    
    pack();
    setLocationRelativeTo(null);
    // Standardowe operacje
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
  }
  
  public final void showMenu(){
    CardLayout cl = (CardLayout)getContentPane().getLayout();
    cl.show(getContentPane(), "pause");
    pausePanel.requestFocusInWindow();
  }
  
  public final void showGame(){
    CardLayout cl = (CardLayout)getContentPane().getLayout();
    cl.show(getContentPane(), "game");
    gamePanel.gameMap.requestFocusInWindow();
  } 
  
  public final void saveGame(){
    try{
      BufferedWriter writer = new BufferedWriter(new FileWriter("saved_game.txt"));
      writer.write(gamePanel.toString());
      writer.close();
    }
    catch (Exception e){
      gamePanel.logs.addError(e.getMessage());
    }
    showGame();
  }
  
  public final void loadGame(){
    try{
      BufferedReader reader = new BufferedReader(new FileReader("saved_game.txt"));
      String line = reader.readLine();
      if(line == null) throw new Exception("Pusty zapis stanu gry!");
      
      // Pierwsza linia
      LinkedList<String> lineSplited = new LinkedList();
      lineSplited.addAll(Arrays.asList(line.split("-")));
      int round = Integer.parseInt(lineSplited.getLast());
      
      LinkedList<String> pointSplited = new LinkedList();
      pointSplited.addAll(Arrays.asList(lineSplited.getFirst().split(",")));
      Dimension worldSize = new Dimension(Integer.parseInt(pointSplited.getFirst()),
                          Integer.parseInt(pointSplited.getLast()));
      
      World w = new World(gamePanel, worldSize, round);
      
      // Organizmy
      Point position = new Point();
      while( (line = reader.readLine()) != null ){
        lineSplited.clear();
        lineSplited.addAll(Arrays.asList(line.split("-")));
        Organism org;
        pointSplited.clear();
        pointSplited.addAll(Arrays.asList(lineSplited.get(3).split(",")));
        position = new Point(Integer.parseInt(pointSplited.getFirst()),
                          Integer.parseInt(pointSplited.getLast()));
        switch (lineSplited.getFirst()){
          case "Antelope":
            org = new Antelope(w, position);
            break;
          case "CyberSheep":
            org = new CyberSheep(w, position);
            break;
          case "Fox":
            org = new Fox(w, position);
            break;
          case "Sheep":
            org = new Sheep(w, position);
            break;
          case "Turtle":
            org = new Turtle(w, position);
            break;
          case "Wolf":
            org = new Wolf(w, position);
            break;
          case "DeadlyNightshade":
            org = new DeadlyNightshade(w, position);
            break;
          case "Grass":
            org = new Grass(w, position);
            break;
          case "Guarana":
            org = new Guarana(w, position);
            break;
          case "SosnowskysHogweed":
            org = new SosnowskysHogweed(w, position);
            break;
          case "SowThistle":
            org = new SowThistle(w, position);
            break;
          case "Human":
            org = new Human(w, position);
            break;
          default:
            throw new Exception("Niepoprawna nazwa gatunkowa.");
        }
        org.setAge(Integer.parseInt(lineSplited.get(1)));
        org.setPower(Integer.parseInt(lineSplited.get(2)));
        w.setOrganism(org, position);
        
        if(org instanceof Human){
          Human human = (Human)org;
          human.setPoints(Integer.parseInt(lineSplited.get(4)));
          human.setTimeUntilSpecialAbilityReadyToUse
                                        (Integer.parseInt(lineSplited.get(5)));
          w.setPlayer(human);
          if(human.getTimeUntilSpecialAbilityReadyToUse() > 0)
            w.setGlowPowerPanel(true);
        }
      }
      if(w.getPlayer() == null) throw new Exception("Rozgrywka była już zakończona");
      gamePanel.powerPanel.setPower(w.getPlayer().getPower());
      gamePanel.gameMap.changeWorld(w);
    }
    catch (Exception e){
      gamePanel.logs.addError("NIE UDAŁO SIĘ WCZYTAĆ STANU GRY.");
      gamePanel.logs.addError(e.getMessage());
    }
    showGame();
  }
  
  public void restartGame(){
    gamePanel.powerPanel.setPower(5);
    gamePanel.roundPanel.setRoundCount(0);
    gamePanel.powerPanel.setGlow(false);
    gamePanel.gameMap.changeWorld(new World(gamePanel, new Dimension(30,20)));
    
    showGame();
  }

}
