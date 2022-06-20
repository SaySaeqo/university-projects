/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.strona.wirtualny_swiat.game.mechanics;

/**
 *
 * @author SaySaeqo
 */
public class IncorrectOrganismPositionException extends Exception {

  /**
   * Creates a new instance of <code>OrganismPositionIsIncorrectException</code>
   * without detail message.
   */
  public IncorrectOrganismPositionException() {
    super("Organizm, który chciał się ruszyć, nie był wcześniej na zadeklarowanym przez siebie miejscu.");
  }

  /**
   * Constructs an instance of <code>OrganismPositionIsIncorrectException</code>
   * with the specified detail message.
   *
   * @param msg the detail message.
   */
  public IncorrectOrganismPositionException(String msg) {
    super(msg);
  }
}
