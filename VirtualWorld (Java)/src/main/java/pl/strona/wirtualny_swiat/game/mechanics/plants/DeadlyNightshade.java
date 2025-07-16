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
import pl.strona.wirtualny_swiat.game.mechanics.Organism;
import pl.strona.wirtualny_swiat.game.mechanics.Plant;
import pl.strona.wirtualny_swiat.game.mechanics.World;

/**
 *
 * @author SaySaeqo
 */
public class DeadlyNightshade extends Plant{
  
  public DeadlyNightshade(World world, Point position){
    super(world, 99, position);
  }
  
  @Override
  public Image getImage() throws IOException{
    try{
      URL url = getClass().getResource("/deadly_nightshade.png");
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
    return new DeadlyNightshade(getWorld(), getPosition());
  }
  
  @Override
  public void defense(Organism aggressor){
    getWorld().addEvent(aggressor.getClass().getSimpleName()+" zjada "+
                        getClass().getSimpleName()+" i umiera.");
    makeDead();
    aggressor.makeDead();
  }
  
}
