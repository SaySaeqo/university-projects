/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.strona.wirtualny_swiat.game.components.gamepanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author SaySaeqo
 */
public class EventPanel extends JPanel{
  
  private JPanel events;
  private int eventCount;
  
  public EventPanel(){
    super();
    setLayout(new GridBagLayout());
    setBorder(BorderFactory.createLineBorder(Color.BLACK, 4, true));
    setPreferredSize(new Dimension(400,200));
    GridBagConstraints con = new GridBagConstraints();
    con.insets = new Insets(0,10,0,10);
    con.anchor = GridBagConstraints.NORTHWEST;
    con.weightx = 1;
    con.weighty = 1;
    events = new JPanel(new GridBagLayout());
    add(events, con);
    eventCount = 0;
  }
  
  public void addEvent(String string){
    eventCount++;
    GridBagConstraints con = new GridBagConstraints();
    con.insets = new Insets(0,10,0,10);
    con.anchor = GridBagConstraints.NORTHWEST;
    con.fill = GridBagConstraints.NONE;
    con.gridx = 0;
    con.gridy = GridBagConstraints.RELATIVE;
    con.weightx = 1;
    con.weighty = 1;
    JLabel event = new JLabel(String.format("%3d - ",
                              eventCount)+string);
    events.add(event, con);
    
    SwingUtilities.getWindowAncestor(this).pack();

    if(event.getHeight() <16){
      events.removeAll();
      events.add(event, con);
    }
    
    revalidate();
    repaint();  
  }
  
  public void addError(String string){
    GridBagConstraints con = new GridBagConstraints();
    con.insets = new Insets(0,10,0,10);
    con.anchor = GridBagConstraints.NORTHWEST;
    con.fill = GridBagConstraints.NONE;
    con.gridx = 0;
    con.gridy = GridBagConstraints.RELATIVE;
    con.weightx = 1;
    con.weighty = 1;
    JLabel error = new JLabel("ERROR : "+string);
    error.setForeground(Color.red);
    events.add(error, con);
    
    SwingUtilities.getWindowAncestor(this).pack();

    if(error.getHeight() <16){
      events.removeAll();
      events.add(error, con);
    }
    
    revalidate();
    repaint();  
  }
  
  public void clear(){
    events.removeAll();
    eventCount = 0;
  }
  
}
