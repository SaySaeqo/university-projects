/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.strona.wirtualny_swiat.game.components.gamepanel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author SaySaeqo
 */
public class InfoPanel extends JPanel {
  
  public InfoPanel(){
    super();
    setLayout(new GridBagLayout());
    setBorder(BorderFactory.createCompoundBorder
                            (BorderFactory.createLineBorder(Color.BLACK, 2), 
                             BorderFactory.createLineBorder(Color.DARK_GRAY, 4)));
    addInfoBox("ESC - pause");
    addInfoBox("ARROWS - move");
    addInfoBox("SPACE - special");
   
  }
  
  private void addInfoBox(String string){
    GridBagConstraints con = new GridBagConstraints();
    con.insets = new Insets(0,10,0,10);
    con.gridx = 0;
    con.gridy = GridBagConstraints.RELATIVE;
    con.anchor = GridBagConstraints.WEST;
    add(new JLabel(string), con);
  }
  
}
