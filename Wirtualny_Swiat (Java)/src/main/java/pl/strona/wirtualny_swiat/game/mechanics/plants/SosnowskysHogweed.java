/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.strona.wirtualny_swiat.game.mechanics.plants;

import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import pl.strona.wirtualny_swiat.game.mechanics.Animal;
import pl.strona.wirtualny_swiat.game.mechanics.Direction;
import pl.strona.wirtualny_swiat.game.mechanics.IncorrectOrganismPositionException;
import pl.strona.wirtualny_swiat.game.mechanics.Organism;
import pl.strona.wirtualny_swiat.game.mechanics.OutsideMapException;
import pl.strona.wirtualny_swiat.game.mechanics.Plant;
import pl.strona.wirtualny_swiat.game.mechanics.World;
import pl.strona.wirtualny_swiat.game.mechanics.animals.CyberSheep;

/**
 *
 * @author SaySaeqo
 */
public class SosnowskysHogweed extends Plant{
  
  public SosnowskysHogweed(World world, Point position){
    super(world, 10, position);
  }
  
  @Override
  public Image getImage() throws IOException{
    try{
      URL url = getClass().getResource("/sosnowskys_hogweed.png");
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
    return new SosnowskysHogweed(getWorld(), getPosition());
  }
  
  @Override
  @SuppressWarnings("empty-statement")
  public Point action(String string){
    for(Direction dir : Direction.values()){
      try{
        Organism victim = getWorld().getOrganism(getPositionOf(dir));
        if( victim instanceof Animal){
          if(victim instanceof CyberSheep) continue;
          getWorld().addEvent(victim.getClass().getSimpleName()+" umiera cały"
              + " pokaleczony i w bąblach :c");
          victim.makeDead();
        }
      }
      catch(OutsideMapException e){
        ;
      }
    }
    return super.action(string);
  }
  
  @Override
  public void defense(Organism aggressor){
    if(aggressor instanceof CyberSheep){
      getWorld().addEvent(aggressor.getClass().getSimpleName()+" zjada "+
                          getClass().getSimpleName()+"! OH YEAH!");
      try{
        getWorld().moveOrganism(aggressor, getPosition());
      }
      catch(IncorrectOrganismPositionException e){
        getWorld().addError(e.getMessage());
      }
    }
    else{
      getWorld().addEvent(aggressor.getClass().getSimpleName()+" zjada "+
                          getClass().getSimpleName()+" i umiera.");
      makeDead();
      aggressor.makeDead();
    }
  }
  
}
