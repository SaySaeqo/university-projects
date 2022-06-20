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
import pl.strona.wirtualny_swiat.game.mechanics.plants.SosnowskysHogweed;

/**
 *
 * @author SaySaeqo
 */
public class CyberSheep extends Animal{
  
  public CyberSheep(World world, Point position){
    super(world, 4, 11, position);
  }
  
  @Override
  public Image getImage() throws IOException{
    try{
      URL url = getClass().getResource("/cyber_sheep.png");
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
    return new CyberSheep(getWorld(), getPosition());
  }
  
  @Override
  public Point action(String string){
    Point nearestHogweed = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
    for(Organism organism : getWorld().getQueue())
      if(organism instanceof SosnowskysHogweed)
        if(getPosition().distance(organism.getPosition()) 
            < getPosition().distance(nearestHogweed))
          nearestHogweed = organism.getPosition();
    
    if(nearestHogweed.x == Integer.MAX_VALUE) return super.action(string);
    
    double bestDistance= Double.MAX_VALUE;
    Direction bestDirection = Direction.DOWN;
    for(Direction dir : Direction.values()){
      try{
        if(getPositionOf(dir).distance(nearestHogweed)
            < bestDistance){
          bestDirection = dir;
          bestDistance = getPositionOf(dir).distance(nearestHogweed);
        }
          
      }
      catch(OutsideMapException e){
        ;
      }
    }
    
    try{
      return getPositionOf(bestDirection); 
    }
    catch(OutsideMapException e){
      getWorld().addError(getClass().getSimpleName()+" "+getPosition().toString()
                            +" nie mógła podążyć "+"najlepszą ścieżką :\"C");
      return getPosition();
    }
  }
  
  @Override
  public boolean willAttack(Organism victim){
    if(victim.getClass().getSimpleName() == null ? getClass().getSimpleName() == null : victim.getClass().getSimpleName().equals(getClass().getSimpleName()))
      return false;
    else
      return true;
  }
  
}
