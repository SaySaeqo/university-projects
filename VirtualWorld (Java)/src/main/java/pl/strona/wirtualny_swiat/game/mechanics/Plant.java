/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.strona.wirtualny_swiat.game.mechanics;

import java.awt.Point;

/**
 *
 * @author SaySaeqo
 */
public abstract class Plant extends Organism{
  
  static public final int SPREAD_FREQUENCY = 30;
  
  public Plant(World world, int power, Point position){
    super(world, 0, power, position);
  }
  
  @Override
  public Point action(String string){
    if(isFreeSpotAround() && random.nextInt(SPREAD_FREQUENCY) == 0){
      Point freeSpot;
      do{
        freeSpot = super.action(null);
      } while (getWorld().getOrganism(freeSpot) != null);
      getWorld().setOrganism(makeChild(), freeSpot);
      getWorld().addEvent(getClass().getSimpleName()+" rozprzestrzenia się.");
    }
    return getPosition();
  }
}
