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
import javax.swing.SwingConstants;

/**
 *
 * @author SaySaeqo
 */
public class RoundPanel extends JPanel {
  
  private JLabel roundCountLabel;

  public RoundPanel() {
    super();
    setLayout(new GridBagLayout());
    setBorder(BorderFactory.createCompoundBorder
                            (BorderFactory.createLineBorder(Color.BLACK, 2), 
                             BorderFactory.createLineBorder(Color.ORANGE, 4)));
    
    // podelementy
    JLabel roundText = new JLabel("Runda:");
    GridBagConstraints con = new GridBagConstraints();
    con.insets = new Insets(10,20,10,20);
    con.gridx = 0;
    con.gridy = 0;
    con.weightx = 1;
    con.anchor = GridBagConstraints.LINE_END;
    add(roundText, con);
    roundCountLabel = new JLabel("0");
    con.gridx = GridBagConstraints.RELATIVE;
    con.anchor = GridBagConstraints.LINE_START;
    add(roundCountLabel, con);
  }
  
  public int getRoundCount(){
    int result;
    try{
      result = Integer.parseInt(roundCountLabel.getText());
    }
    catch(NumberFormatException e){
      result = -1;
    }
    return result;
  }
  
  public void increaseRoundCount(){
    int round = getRoundCount() + 1;
    if(round == 0)
      roundCountLabel.setText("ERROR");
    else
      roundCountLabel.setText(String.valueOf(round));
  }
  
  public void setRoundCount(String string){
    roundCountLabel.setText(string);
  }
  
  public void setRoundCount(int number){
    roundCountLabel.setText(String.valueOf(number));
  }
  
}
