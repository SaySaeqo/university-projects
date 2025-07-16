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
public class Fox extends Animal{
  
  public Fox(World world, Point position){
    super(world, 7, 3, position);
  }
  
  @Override
  public Image getImage() throws IOException{
    try{
      URL url = getClass().getResource("/fox.png");
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
    return new Fox(getWorld(), getPosition());
  }
  
  @Override
  public Point action(String string){
    int counter = 30;
    Point spot;
    do{
      spot = super.action(string);
      counter--;
    } while(counter > 0 && getWorld().getOrganism(spot) != null
          && getWorld().getOrganism(spot).getPower() > getPower());
    
    if(counter <= 0){
      getWorld().addEvent(getClass().getSimpleName()+" decyduje się nie ruszać.");
      return getPosition();
    }
    else return spot;
  }
  
}
