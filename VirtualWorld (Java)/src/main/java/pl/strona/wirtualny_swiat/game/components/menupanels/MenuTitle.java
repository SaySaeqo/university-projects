/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.strona.wirtualny_swiat.game.components.menupanels;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author SaySaeqo
 */
public class MenuTitle extends JLabel {
  
  public MenuTitle(String string){
    super(string);
    setFont(new Font(Font.SANS_SERIF,Font.BOLD,48));
    setHorizontalAlignment(SwingConstants.CENTER);
    setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
  }
}
