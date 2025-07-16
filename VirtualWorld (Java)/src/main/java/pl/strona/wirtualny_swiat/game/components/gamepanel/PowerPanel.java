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

/**
 *
 * @author SaySaeqo
 */
public class PowerPanel extends JPanel {
  
  private JLabel powerLabel;
  
  public PowerPanel() {
    super();
    setLayout(new GridBagLayout());
    setGlow(false);
    
    // podelementy
    GridBagConstraints con = new GridBagConstraints();
    con.insets = new Insets(10,5,10,20);
    con.gridx = 0;
    con.gridy = 0;
    con.weightx = 1;
    con.anchor = GridBagConstraints.LINE_END;
    add(new JLabel("TWOJA SIŁA :"), con);
    powerLabel = new JLabel("5");
    con.gridx = GridBagConstraints.RELATIVE;
    con.anchor = GridBagConstraints.LINE_START;
    add(powerLabel, con);
  }

  public void setPower(int power) {
    powerLabel.setText(String.valueOf(power));
  }
  
  public void setGlow(boolean shouldGlow){
    if(shouldGlow)
      setBorder(BorderFactory.createCompoundBorder
                            (BorderFactory.createLineBorder(Color.BLACK, 2), 
                             BorderFactory.createLineBorder(Color.RED, 4)));
    else
      setBorder(BorderFactory.createCompoundBorder
                            (BorderFactory.createLineBorder(Color.BLACK, 2), 
                             BorderFactory.createLineBorder(Color.BLUE, 4)));
  }
}
