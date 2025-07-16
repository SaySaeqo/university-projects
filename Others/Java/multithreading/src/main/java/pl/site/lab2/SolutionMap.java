/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.site.lab2;

import java.util.TreeMap;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author SaySaeqo
 */
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class SolutionMap{
  
  private final TreeMap<Integer, Boolean> solutions = new TreeMap<>();;
  
  public synchronized void addSolution(Integer problem, Boolean solution)
  {
    solutions.put(problem,solution);
  }
  
  public synchronized void display()
  {
    solutions.forEach((problem, solution) -> {
      String no_word = "nie ";
      if(solution) no_word = "";
      System.out.println("Liczba " + problem + " " + no_word + "jest liczbą "
          + "pierwszą");
    });
  }
  
  
}
