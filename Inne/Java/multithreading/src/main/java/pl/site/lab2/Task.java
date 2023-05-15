/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.site.lab2;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author SaySaeqo
 */
@AllArgsConstructor
@Getter
public class Task {
  
  private final int number;
  
  public Boolean toDo() throws InterruptedException
  {
    // number is primal?
    //for (int i = 2; i<= Math.sqrt(number); i++)
    for (int i = 2; i< number; i++)
    {
      if (number % i == 0) return false;
    }
    return true;
  }
}
