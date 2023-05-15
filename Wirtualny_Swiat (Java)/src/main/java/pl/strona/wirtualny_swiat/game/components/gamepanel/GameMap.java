/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.strona.wirtualny_swiat.game.components.gamepanel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import pl.strona.wirtualny_swiat.game.components.menupanels.MenuTitle;
import pl.strona.wirtualny_swiat.game.mechanics.World;

/**
 *
 * @author SaySaeqo
 */
public class GameMap extends JPanel {
  
  private World world;
  private Field[][] fields;
  private boolean isGameEnd;
  public final Dimension BUTTON_SIZE = new Dimension (20,20);
  
  public GameMap(World inputWorld){
    super(new GridLayout(inputWorld.getSize().height, inputWorld.getSize().width));
    world = inputWorld;
    setPreferredSize(new Dimension(world.getSize().width*BUTTON_SIZE.width, 
                                   world.getSize().height*BUTTON_SIZE.height));
    fields = new Field[world.getSize().width][world.getSize().height];
    generateFields();
    for(int i = 0; i < world.getSize().height; i++)
      for(int j = 0; j < world.getSize().width; j++)
        add(fields[j][i]);
    update();


    isGameEnd = false;
    
    // KeyBinding
    setFocusable(true);
    setEnabled(true);
    Action goDown = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        runNextRound("DOWN");
      }
    };
    Action goUp = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        runNextRound("UP");
      }
    };
    Action goLeft = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        runNextRound("LEFT");
      }
    };
    Action goRight = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        runNextRound("RIGHT");
      }
    };
    Action useSpecial = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        runNextRound("SPACE");
      }
    };
    
    getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("DOWN"), "go down");
    getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("UP"), "go up");
    getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("LEFT"), "go left");
    getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("RIGHT"), "go right");
    getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke(' '), "use special");
    
    getActionMap().put("go down", goDown);
    getActionMap().put("go up", goUp);
    getActionMap().put("go left", goLeft);
    getActionMap().put("go right", goRight);
    getActionMap().put("use special", useSpecial);
    
  }
  
  private void generateFields(){
    for(int i = 0; i < world.getSize().width; i++)
      for(int j = 0; j < world.getSize().height; j++){
        fields[i][j] = new Field(world, new Point(i, j));
      }
  }
  
  public void update(){
    for(int i = 0; i < world.getSize().width; i++)
      for(int j = 0; j < world.getSize().height; j++){
        fields[i][j].setOrganism(world.getOrganism(new Point(i,j)));
      }
  }

  private void runNextRound(String string){
    if(!isGameEnd)
      if(!world.nextRound(string))
        GAMEOVER();
  }
  
  public World getWorld(){
    return world;
  }
  
  public void changeWorld(World newWorld){
    world = newWorld;
    removeAll();
    setLayout(new GridLayout(world.getSize().height, world.getSize().width));
    setPreferredSize(new Dimension(world.getSize().width*BUTTON_SIZE.width, 
                                   world.getSize().height*BUTTON_SIZE.height));
    fields = new Field[world.getSize().width][world.getSize().height];
    generateFields();
    for(int i = 0; i < world.getSize().height; i++)
      for(int j = 0; j < world.getSize().width; j++)
        add(fields[j][i]);
    update();
    
    isGameEnd = false;
    
    SwingUtilities.getWindowAncestor(this).pack();
    SwingUtilities.getWindowAncestor(this).revalidate();
    SwingUtilities.getWindowAncestor(this).repaint();
  }
  
  private void GAMEOVER(){
    setLayout(new FlowLayout());
    isGameEnd = true;
    removeAll();
    JPanel gameOver = new JPanel();
    gameOver.setLayout(new BoxLayout(gameOver, BoxLayout.PAGE_AXIS ));
    gameOver.add(new MenuTitle("GAME OVER"));
    gameOver.add(new MenuTitle("POINTS: "
                              +String.valueOf(world.getPlayer().getPoints())));
    add(gameOver);
    
    revalidate();
    repaint();
  }
}
