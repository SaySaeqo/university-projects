/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.strona.wirtualny_swiat.game.mechanics.animals;

import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import pl.strona.wirtualny_swiat.game.mechanics.Animal;
import pl.strona.wirtualny_swiat.game.mechanics.Direction;
import pl.strona.wirtualny_swiat.game.mechanics.Organism;
import pl.strona.wirtualny_swiat.game.mechanics.OutsideMapException;
import pl.strona.wirtualny_swiat.game.mechanics.World;

/**
 *
 * @author SaySaeqo
 */
public class Human extends Animal{
  
  private int points;
  private int timeUntilSpecialAbilityReadyToUse;
  
  public Human(World world, Point position){
    super(world, 4, 5, position);
    points = 0;
    timeUntilSpecialAbilityReadyToUse = 0;
  }
  
  public int getPoints(){
    return points;
  }
  
  public void givePoints(){
    points += 1;
  }
  
  public void setPoints(int points){
    this.points = points;
  }
  
  public int getTimeUntilSpecialAbilityReadyToUse(){
    return timeUntilSpecialAbilityReadyToUse;
  }
  
  public void setTimeUntilSpecialAbilityReadyToUse(int timeUntil){
    timeUntilSpecialAbilityReadyToUse = timeUntil;
  }
  
  @Override
  public Image getImage() throws IOException{
    try{
      URL url = getClass().getResource("/human.png");
      if(url == null)
        throw new IOException();
      return ImageIO.read(url);
    }
    catch (IOException e){
      throw e;
    }
  }

  @Override
  public Organism makeChild() {
    return new Human(getWorld(), getPosition());
  }
  
  @Override
  @SuppressWarnings("empty-statement")
  public Point action(String string){
    
    // SPECJALNA UMIEJĘTNOŚĆ
    if (timeUntilSpecialAbilityReadyToUse > 0){
      if(timeUntilSpecialAbilityReadyToUse >= 5)
        setPower(getPower() - 1);
      timeUntilSpecialAbilityReadyToUse--;
      if(timeUntilSpecialAbilityReadyToUse == 0){
        getWorld().setGlowPowerPanel(false);
        getWorld().addEvent("ZNOWU MOŻESZ WYPIĆ ELIKSIRU!");
      }
    }
    //
    
    try{
      switch(string){
        case "RIGHT" :
          return getPositionOf(Direction.RIGHT);
        case "LEFT" :
          return getPositionOf(Direction.LEFT);
        case "UP" :
          return getPositionOf(Direction.UP);
        case "DOWN" :
          return getPositionOf(Direction.DOWN);
        case "SPACE" :
          if(timeUntilSpecialAbilityReadyToUse == 0){
            timeUntilSpecialAbilityReadyToUse = 9;
            getWorld().addEvent("WYPIŁEŚ ELIKSIR! ROZPIERA CIĘ NOWA SIŁA!");
            getWorld().setGlowPowerPanel(true);
            setPower(getPower()+5);
          }
          break;
        default:
          break;
      }
    }
    catch(OutsideMapException e){
      ;
    }
    return getPosition();
  }
  
  @Override
  public void defense(Organism aggressor){
    super.defense(aggressor);
    if(isAlive())
      afterAttack(aggressor);
  }
  
  @Override
  public void afterAttack(Organism victim){
    if(victim instanceof Animal) {
      givePoints();
      getWorld().addEvent("PRZYZNANO PUNKT!");
    }
  }
  
}
