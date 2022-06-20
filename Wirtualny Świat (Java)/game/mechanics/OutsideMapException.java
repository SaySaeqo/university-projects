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
public class OutsideMapException extends Exception {

  /**
   * Creates a new instance of <code>OutsideMapException</code> without detail
   * message.
   */
  public OutsideMapException() {
  }

  /**
   * Constructs an instance of <code>OutsideMapException</code> with the
   * specified detail message.
   *
   * @param msg the detail message.
   */
  public OutsideMapException(String msg) {
    super(msg);
  }
}
