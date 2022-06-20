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
public class IncorrectRandomOrganismException extends Exception {

  /**
   * Creates a new instance of <code>IncorrectRandomOrganism</code> without
   * detail message.
   */
  public IncorrectRandomOrganismException() {
    super("Za dużo organizmów w prywatnej tablicy funckji setRandomOrganism w klasie World.");
  }

  /**
   * Constructs an instance of <code>IncorrectRandomOrganism</code> with the
   * specified detail message.
   *
   * @param msg the detail message.
   */
  public IncorrectRandomOrganismException(String msg) {
    super(msg);
  }
}
