/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.strona.wirtualny_swiat.game.mechanics;

import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author SaySaeqo
 */
public abstract class Organism implements Comparable<Organism>{
  
  private final World world;
  private int initiative;
  private int age;
  private int power;
  private Point position;
  private boolean isAlive;
  protected Random random;
  
  public Organism(World world, int initiative, int power, Point position){
    this.world = world;
    this.initiative = initiative;
    this.power = power;
    this.position = position;
    this.age = 0;
    this.isAlive = true;
    random = new Random();
  }
  
  public Point getPosition(){
    return position;
  }
  
  public int getPower(){
    return power;
  }
  
  public int getAge(){
    return age;
  }
  
  public int getInitiative(){
    return initiative;
  }
  
  public boolean isAlive(){
    return isAlive;
  }
  
  public World getWorld(){
    return world;
  }
  
  public void setPower(int power){
    this.power = power;
  }
  
  public void setPosition(Point position){
    this.position = position;
  }
  
  public void setAge(int age){
    this.age = age;
  }
  
  
  
  public void makeOlder(){
    age += 1;
  }
  
  public void makeDead(){
    getWorld().setOrganism(null, position);
    isAlive = false;
  }
  
  
  
  public Point getPositionOf(Direction direction)
   throws OutsideMapException{
    Point point;
    switch (direction){
      case LEFT  :
        point = new Point(position.x - 1, position.y);
        break;
      case RIGHT :
        point = new Point(position.x + 1, position.y);
        break;
      case UP :
        point = new Point(position.x, position.y - 1);
        break;
      case DOWN :
        point = new Point(position.x, position.y + 1);
        break;
      case LEFT_UP :
        point = new Point(position.x - 1, position.y - 1);
        break;
      case LEFT_DOWN :
        point = new Point(position.x - 1, position.y + 1);
        break;
      case RIGHT_UP :
        point = new Point(position.x + 1, position.y - 1);
        break;
      case RIGHT_DOWN :
        point = new Point(position.x + 1, position.y + 1);
        break;
      default :
        throw new AssertionError(direction.name());
    }
    if (point.x >= getWorld().getSize().width
        || point.x < 0
        || point.y >= getWorld().getSize().height
        || point.y < 0)
      throw new OutsideMapException();
    else
      return point;
  }
  
  @SuppressWarnings("empty-statement")
  public boolean isFreeSpotAround(){
    for(Direction dir : Direction.values()){
      try{
        if (getWorld().getOrganism(getPositionOf(dir)) == null)
          return true;
      }
      catch(OutsideMapException e){
        ;
      }
    }
    return false;
  }
  
  @SuppressWarnings("empty-statement")
  public Point action(String string){
    while (true){
      try{
        return getPositionOf(Direction.values()[random.nextInt(8)]);
      }
      catch (OutsideMapException e){
        ;
      }
    }
  }
  
  public boolean willAttack(Organism victim){
    return true;
  }
  
  @SuppressWarnings("empty-statement")
  public void afterAttack(Organism victim){
    ;
  }
  
  public void defense(Organism aggressor){
    if(!aggressor.willAttack(this)) return;
    
    if(getPower() > aggressor.getPower()){
      getWorld().addEvent(getClass().getSimpleName()+" pokonuje "+
                          aggressor.getClass().getSimpleName()+".");
      aggressor.makeDead();
    }
    else{
      getWorld().addEvent(aggressor.getClass().getSimpleName()+" pokonuje "+
                          getClass().getSimpleName()+".");
      try{
        getWorld().moveOrganism(aggressor, getPosition());
      }
      catch(IncorrectOrganismPositionException e){
        getWorld().addError(e.getMessage());
      }
    }
    
    if(aggressor.isAlive) aggressor.afterAttack(this);
  }
  
  
  
  public abstract Image getImage() throws IOException;
  public abstract Organism makeChild();

  @Override
  public int compareTo(Organism o) {
    if(initiative > o.getInitiative()) return -1;
    else if (initiative < o.getInitiative()) return 1;
    else if (age > o.getAge()) return -1;
    else if (age < o.getAge()) return 1;
    else return 0;
  }
}
