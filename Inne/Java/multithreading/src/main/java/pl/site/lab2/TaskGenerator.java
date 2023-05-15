/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.site.lab2;

import lombok.NoArgsConstructor;

/**
 *
 * @author SaySaeqo
 */
@NoArgsConstructor
public class TaskGenerator {
  
  private Integer iterator = 1;
  
  public Task generate()
  {
    return new Task(iterator++);
  }
}
