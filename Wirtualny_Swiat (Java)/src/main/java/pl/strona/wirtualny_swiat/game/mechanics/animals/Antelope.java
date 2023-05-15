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
import pl.strona.wirtualny_swiat.game.mechanics.IncorrectOrganismPositionException;
import pl.strona.wirtualny_swiat.game.mechanics.Organism;
import pl.strona.wirtualny_swiat.game.mechanics.World;

/**
 *
 * @author SaySaeqo
 */
public class Antelope extends Animal{
  
  public Antelope(World world, Point position){
    super(world, 4, 4, position);
  }
  
  @Override
  public Image getImage() throws IOException{
    try{
      URL url = getClass().getResource("/antelope.png");
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
    return new Antelope(getWorld(), getPosition());
  }
  
  @Override
  public boolean willAttack(Organism victim){
    if(!super.willAttack(victim)) return false;
    
    if(random.nextBoolean()) return true;
    
    if(victim.isFreeSpotAround()){
      Point myPosition = getPosition();
      setPosition(victim.getPosition());
      Point freeSpot;
      do{
        freeSpot = super.action(null);
      } while (getWorld().getOrganism(freeSpot) != null);
      setPosition(myPosition);
      try{
        getWorld().moveOrganism(this, freeSpot);
      }
      catch(IncorrectOrganismPositionException e){
        getWorld().addError(e.getMessage());
      }
    }
    getWorld().addEvent(getClass().getSimpleName()+" unika walki z "
                      +victim.getClass().getSimpleName()+".");
    return false;
  }
  
  @Override
  public void defense(Organism aggressor){
    if(!willAttack(aggressor)) return;
    else super.defense(aggressor);
  }
  
  @Override
  public Point action(String string){
    if(isFreeSpotAround()){
      Point freeSpot;
      do{
        freeSpot = super.action(null);
      } while (getWorld().getOrganism(freeSpot) != null);
      try{
        getWorld().moveOrganism(this, freeSpot);
      }
      catch(IncorrectOrganismPositionException e){
        getWorld().addError(e.getMessage());
      }
    }
    return super.action(string);
  }
  
}
