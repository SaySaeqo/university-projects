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
import pl.strona.wirtualny_swiat.game.mechanics.Organism;
import pl.strona.wirtualny_swiat.game.mechanics.World;

/**
 *
 * @author SaySaeqo
 */
public class Sheep extends Animal{
  
  public Sheep(World world, Point position){
    super(world, 4, 4, position);
  }
  
  @Override
  public Image getImage() throws IOException{
    try{
      URL url = getClass().getResource("/sheep.png");
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
    return new Sheep(getWorld(),getPosition());
  }
  
}
