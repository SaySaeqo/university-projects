/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.site.lab2;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SaySaeqo
 */
public class Main {
  
  public static List<Integer> decrypt(String command) throws InvalidInputException
  {
    List<Integer> list = new ArrayList<>();
    int start_idx=0;
    boolean comma = false;
    for (int i=0; i< command.length(); i++)
    {
      if (command.charAt(i) <= '9' && command.charAt(i) >= '0')
      {
        // digit
        comma = false;
      }
      else if (command.charAt(i) == ',')
      {
        // comma
        if (comma == true)
        {
          start_idx ++;
        }
        else
        {
          comma = true;
          String number_as_word = command.substring(start_idx, i);
          Integer number = Integer.decode(number_as_word);
          list.add(number);
          start_idx = i+1;
        }
      }
      else
      {
        throw new InvalidInputException();
      }
    }
    int tmp = comma ? 1: 0;
    String number_as_word = command.substring(start_idx, command.length() - tmp);
    if ( number_as_word.length() > 0)
    {
      Integer number = Integer.decode(number_as_word);
      list.add(number);
    }
    
    return list;
  }
  
  public static void main(String args[])
  {
    Integer number_of_workers = Integer.decode(args[0]);
    TaskBoard taskBoard = new TaskBoard();
    SolutionMap solutions = new SolutionMap();
    Scanner scanner = new Scanner(System.in);
    List<Thread> workers = new ArrayList<>();
    
    for(int i=0;i<number_of_workers;i++)
    {
      Thread worker = new Thread(new Worker(taskBoard,solutions, false));
      workers.add(worker);
    }
    //workers.add(new Thread(taskBoard));
    
    workers.forEach((thread)->{
      thread.start();
    });
    
    boolean quit=false;
    while (!quit)
    {
      String command = scanner.next();
      switch (command){
        case "exit":
          quit = true;
          while (taskBoard.getNumberOfTasks() > 0)
          {
            try {
              sleep(5000);
            } catch (InterruptedException ex) {
              Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
          workers.forEach((thread)->{
            thread.interrupt();
          });
          workers.forEach((thread)->{
            try {
              thread.join();
            } catch (InterruptedException ex) {
              Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
          });
          //break;
        case "list":
          solutions.display();
          break;
        default:
          try {
            List<Integer> numbers = decrypt(command);
            for (Integer number : numbers)
            {
              Task task = new Task(number);
              taskBoard.put(task);
            }
          } catch (InterruptedException | InvalidInputException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
          }
          break;


      }
    }
    
  }
}
