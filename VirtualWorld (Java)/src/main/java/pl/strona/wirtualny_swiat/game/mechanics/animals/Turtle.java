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
public class Turtle extends Animal{
  
  public Turtle(World world, Point position){
    super(world, 1, 2, position);
  }
  
  @Override
  public Image getImage() throws IOException{
    try{
      URL url = getClass().getResource("/turtle.png");
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
    return new Turtle(getWorld(), getPosition());
  }
  
  @Override
  public void defense(Organism aggressor){
    if(!aggressor.willAttack(this)) return;
    
    if(aggressor.getPower() >= 5){
      getWorld().addEvent(aggressor.getClass().getSimpleName()+" zabija "+
                          getClass().getSimpleName()+".");
      try{
        getWorld().moveOrganism(aggressor, getPosition());
      }
      catch(IncorrectOrganismPositionException e){
        getWorld().addError(e.getMessage());
      }   
    }
    else{
      getWorld().addEvent(aggressor.getClass().getSimpleName()+" odbija się od "
                       + "skorupy :o");
    }
    if(aggressor.isAlive())
      aggressor.afterAttack(this);
  }
  
  @Override
  public Point action(String string){
    if(random.nextInt(4) != 0)
      return getPosition();
    else
      return super.action(string);
  }
  
}
