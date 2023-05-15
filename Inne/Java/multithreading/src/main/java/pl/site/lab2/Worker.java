/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.site.lab2;

import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author SaySaeqo
 */
@AllArgsConstructor
@Getter
public class Worker implements Runnable{
  
  private TaskBoard taskBoard;
  private final SolutionMap solutions;
  private boolean is_working = false;

  @Override
  public void run() {
    Task task = null;
    while (!Thread.interrupted()) {
      try {
        task = taskBoard.take();
        is_working =true;
        solutions.addSolution(task.getNumber(), task.toDo());
        is_working = false;
      } catch (InterruptedException ex) {
        if (is_working && task != null)
        {
          try {
            solutions.addSolution(task.getNumber(), task.toDo());
          } catch (InterruptedException ex1) {}
        }
        break;
      }
    }
  }
  
}
