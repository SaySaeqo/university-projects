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
public abstract class Animal extends Organism{
  
  static public final int SUCCES_REPRODUCTION_CHANCE = 2;
  
  public Animal(World world, int initiative, int power, Point position){
    super(world, initiative, power, position);
  }
  
  @Override
  public boolean willAttack(Organism victim){
    if(getClass().getSimpleName() == null ? victim.getClass().getSimpleName() == null : getClass().getSimpleName().equals(victim.getClass().getSimpleName())){
      if(isFreeSpotAround() && Math.abs(victim.getAge() - getAge()) <= 4
          && random.nextInt(SUCCES_REPRODUCTION_CHANCE) == 0){
      Point freeSpot;
      do{
        freeSpot = super.action(null);
      } while (getWorld().getOrganism(freeSpot) != null);
      getWorld().setOrganism(makeChild(), freeSpot);
      getWorld().addEvent(getClass().getSimpleName()+" rozmnożył się.");
      }
      return false;
    }
    else
      return true;
  }
  
}
