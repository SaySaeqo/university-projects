/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.strona.wirtualny_swiat.game.mechanics;

import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;
import pl.strona.wirtualny_swiat.game.components.GamePanel;
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

/**
 *
 * @author SaySaeqo
 */
public class World {
  
  private final Dimension size;
  private int round;
  private Organism[][] map;
  private Human player;
  private GamePanel gamePanel;
  private Random random;
  
  public World(GamePanel gamePanel, Dimension worldSize){
    
    if(worldSize.height < 1) worldSize.height = 1;
    if(worldSize.height > 30) worldSize.height = 30;
    if(worldSize.width < 1) worldSize.width = 1;
    if(worldSize.width > 70) worldSize.width = 70;
    
    this.gamePanel = gamePanel;
    size = worldSize;
    round = 0;
    map = new Organism[size.width][size.height];
    
    player = new Human(this, new Point(0, size.height - 1));
    setOrganism(player,player.getPosition());
    
    random = new Random();
    for(int i = 0; i < size.width; i++)
      for(int j = 0; j < size.height; j++){
        if(i == player.getPosition().x && j == player.getPosition().y) continue;
        if(random.nextInt(4) == 0)
          try{
            setRandomOrganism(new Point(i, j));
          }
          catch(IncorrectRandomOrganismException e){
            e.getMessage();
            setOrganism(null, new Point(i, j));
          }
        else
          setOrganism(null, new Point(i, j));
      }
  }
  
  /* 
  * Only to use, when loading game from file
  */
  public World(GamePanel gamePanel, Dimension worldSize, int round){
    if(worldSize.height < 1) worldSize.height = 1;
    if(worldSize.height > 30) worldSize.height = 30;
    if(worldSize.width < 1) worldSize.width = 1;
    if(worldSize.width > 70) worldSize.width = 70;
    
    this.gamePanel = gamePanel;
    size = worldSize;
    this.round = round;
    this.gamePanel.roundPanel.setRoundCount(round);
    map = new Organism[size.width][size.height];
    player = null;
    random = new Random();
    
    for(int i = 0; i < size.width; i++)
      for(int j = 0; j < size.height; j++){
        map[i][j]=null;
      }
  }
  
  private void setRandomOrganism(Point position)
    throws IncorrectRandomOrganismException{
    OrganismType[] genres = {OrganismType.ANTELOPE,
                              OrganismType.CYBERSHEEP,
                              OrganismType.FOX,
                              OrganismType.SHEEP,
                              OrganismType.TURTLE,
                              OrganismType.WOLF,
                              OrganismType.DEADLY_NIGHTSHADE,
                              OrganismType.GRASS,
                              OrganismType.GUARANA,
                              OrganismType.SOSNOWSKYS_HOGWEED,
                              OrganismType.SOW_THISTLE};
    Organism organism;
    switch(genres[random.nextInt(genres.length)]){
      case ANTELOPE :
        organism = new Antelope(this, position);
        break;
      case CYBERSHEEP :
        organism = new CyberSheep(this, position);
        break;
      case FOX :
        organism = new Fox(this, position);
        break;
      case SHEEP :
        organism = new Sheep(this, position);
        break;
      case TURTLE :
        organism = new Turtle(this, position);
        break;
      case WOLF :
        organism = new Wolf(this, position);
        break;
      case DEADLY_NIGHTSHADE :
        organism = new DeadlyNightshade(this, position);
        break;
      case GRASS :
        organism = new Grass(this, position);
        break;
      case GUARANA :
        organism = new Guarana(this, position);
        break;
      case SOSNOWSKYS_HOGWEED :
        organism = new SosnowskysHogweed(this, position);
        break;
      case SOW_THISTLE :
        organism = new SowThistle(this, position);
        break;
      default :
        throw new IncorrectRandomOrganismException();
    }
    setOrganism(organism, position); 
  }
  
  public Dimension getSize(){
    return size;
  }
  
  public int getRound(){
    return round;
  }
  
  public Human getPlayer(){
    return player;
  }
  
  public Organism getOrganism(Point position){
    return map[position.x][position.y];
  }
  
  public LinkedList<Organism> getQueue(){
    LinkedList<Organism> actionQueue = new LinkedList();
    for(int i = 0; i < size.width; i++)
      for(int j = 0; j < size.height; j++)
        if(map[i][j] instanceof Organism)
          actionQueue.add(map[i][j]);
    actionQueue.sort(null);
    return actionQueue;
  }
  
  public void setPlayer(Human human){
    player = human;
  }
  
  public void setOrganism(Organism organism, Point position){
    map[position.x][position.y] = organism;
    if(organism != null)
      organism.setPosition(position);
  }
  
  public void moveOrganism(Organism organism, Point movePosition)
    throws IncorrectOrganismPositionException{
    
    if(map[organism.getPosition().x][organism.getPosition().y] != organism)
      throw new IncorrectOrganismPositionException(organism.getClass().getSimpleName()+
          " "+organism.getPosition().toString()+" nie był na swojej pozycji.");
    
    if(organism.getPosition().equals(movePosition)) return;
    
    if(getOrganism(movePosition) instanceof Organism) 
      getOrganism(movePosition).makeDead();
    
    setOrganism(null, organism.getPosition());
    setOrganism(organism, movePosition);
  }
  
  public void addEvent(String string){
    gamePanel.logs.addEvent(string);
  }
  
  public void addError(String string){
    gamePanel.logs.addError(string);
  }
  
  public void setGlowPowerPanel(boolean b){
    gamePanel.powerPanel.setGlow(b);
  }
  
  
  
  public boolean nextRound(String string){
    
    gamePanel.logs.clear();
    
    LinkedList<Organism> queue = getQueue();
    queue.forEach((organism) -> {
      organism.makeOlder();
    });
    
    boolean isEnd = false;
    for(Organism organism : queue){
      if(!organism.isAlive()) continue;
      
      Point movePosition = organism.action(string);
      if(getOrganism(movePosition) == null)
        try{
          moveOrganism(organism, movePosition);
        }
        catch(IncorrectOrganismPositionException e){
          addError(e.getMessage());
        }
      else if (getOrganism(movePosition) != organism){
        getOrganism(movePosition).defense(organism);
        if(organism instanceof Plant)
          addError("EUREKA!");
      }
        
      
      if(!player.isAlive()){
        isEnd = true;
        break;
      }
    }
   
    gamePanel.roundPanel.increaseRoundCount();
    round++;
    gamePanel.powerPanel.setPower(player.getPower());
    gamePanel.gameMap.update();
    gamePanel.revalidate();
    gamePanel.repaint();
    
    if(isEnd) return false;
    else return true;
  }
}
