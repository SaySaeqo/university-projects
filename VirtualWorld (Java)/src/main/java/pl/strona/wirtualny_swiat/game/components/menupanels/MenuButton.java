/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.strona.wirtualny_swiat.game.components.menupanels;

import java.awt.Font;
import javax.swing.JButton;

/**
 *
 * @author SaySaeqo
 */
public class MenuButton extends JButton {
  
  public MenuButton(String string) {
    super(string);
    setFont(new Font(Font.SANS_SERIF,Font.BOLD,48));
    setContentAreaFilled(false);
    setBorderPainted(false);
  }
}
