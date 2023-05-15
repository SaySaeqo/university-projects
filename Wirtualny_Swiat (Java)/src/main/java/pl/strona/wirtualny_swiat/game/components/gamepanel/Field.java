/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.strona.wirtualny_swiat.game.components.gamepanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import pl.strona.wirtualny_swiat.game.mechanics.Organism;
import pl.strona.wirtualny_swiat.game.mechanics.World;
import pl.strona.wirtualny_swiat.game.mechanics.animals.Antelope;
import pl.strona.wirtualny_swiat.game.mechanics.animals.CyberSheep;
import pl.strona.wirtualny_swiat.game.mechanics.animals.Fox;
import pl.strona.wirtualny_swiat.game.mechanics.animals.Human;
import pl.strona.wirtualny_swiat.game.mechanics.animals.Sheep;
import pl.strona.wirtualny_swiat.game.mechanics.animals.Turtle;
import pl.strona.wirtualny_swiat.game.mechanics.animals.Wolf;
import pl.strona.wirtualny_swiat.game.mechanics.plants.DeadlyNightshade;
import pl.strona.wirtualny_swiat.game.mechanics.plants.Grass;
import pl.strona.wirtualny_swiat.game.mechanics.plants.Guarana;
import pl.strona.wirtualny_swiat.game.mechanics.plants.SosnowskysHogweed;
import pl.strona.wirtualny_swiat.game.mechanics.plants.SowThistle;

/**
 *
 * @author SaySaeqo
 */
public class Field extends JButton implements ActionListener{
  private Organism organism;
  private World world;
  private Point position;
  
  public Field(World myWorld, Point position){
    super();
    world = myWorld;
    this.position = position;
    organism = null;
    addActionListener(this);
  }
  public Field(World myWorld, Point position, Organism organism){
    this(myWorld, position);
    this.organism = organism;
  }
  
  public void setOrganism(Organism organism){
    this.organism = organism;
  }
  
  @Override
  public void paintComponent(Graphics g){
    Graphics2D g2d = (Graphics2D)g;
    
    if (getModel().isPressed()) {
    g2d.setColor(Color.WHITE);
    }
    else if (getModel().isRollover()) {
    g2d.setColor(Color.darkGray);
    }
    else {
    g2d.setColor(getBackground());
    }
    g2d.fillRect(0, 0, getWidth(), getHeight());
    
    if (organism != null)
      try{
        g2d.drawImage(organism.getImage(), 0, 0, this);
      }
      catch(IOException e){
        world.addError(organism.getClass().getSimpleName()+
                       " źle wczytał swój obrazek");
        organism = null;
      }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(organism instanceof Human){
      if(world.getPlayer().getPower()>=100){
        world.getPlayer().setPower(5);
        world.setGlowPowerPanel(false);
        world.addEvent("CHEATS OFF");
      }
      else{
        world.getPlayer().setPower(100);
        world.setGlowPowerPanel(true);
        world.addEvent("CHEATS ON");
      }
    }
    else if (organism!=null){
      switch (organism.getClass().getSimpleName()){
        case "Antelope":
          organism = null;
          break;
        case "CyberSheep":
          organism = new Antelope(world, organism.getPosition());          
          break;
        case "Fox":
          organism = new CyberSheep(world, organism.getPosition());          
          break;
        case "Sheep":
          organism = new Fox(world, organism.getPosition());          
          break;
        case "Turtle":
          organism = new Sheep(world, organism.getPosition());          
          break;
        case "Wolf":
          organism = new Turtle(world, organism.getPosition());          
          break;
        case "DeadlyNightshade":
          organism = new Wolf(world, organism.getPosition());          
          break;
        case "Grass":
          organism = new DeadlyNightshade(world, organism.getPosition());          
          break;
        case "Guarana":
          organism = new Grass(world, organism.getPosition());          
          break;
        case "SosnowskysHogweed":
          organism = new Guarana(world, organism.getPosition());          
          break;
        case "SowThistle":
          organism = new SosnowskysHogweed(world, organism.getPosition());          
          break;
        default:
          world.addError("Nieobsługiwana zamiana gatunkowa.");
          break;
      }
      world.setOrganism(organism, position);
    }
    else{
      organism = organism = new SowThistle(world, position);
      world.setOrganism(organism, position);
    }
    repaint();
  }
}
