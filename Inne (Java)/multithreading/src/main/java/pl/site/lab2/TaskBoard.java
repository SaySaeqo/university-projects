/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.site.lab2;

import static java.lang.Thread.sleep;
import java.util.List;
import java.util.ArrayList;
import lombok.NoArgsConstructor;

/**
 *
 * @author SaySaeqo
 */
@NoArgsConstructor
public class TaskBoard implements Runnable{
  private final List<Task> tasks = new ArrayList<>();
  private final TaskGenerator generator = new TaskGenerator();
  
  public synchronized Task take() throws InterruptedException {
    while (tasks.isEmpty()){
      wait();
    }
    Task task = tasks.get(0);
    tasks.remove(0);
    return task;
  }
  
  public synchronized void put(Task task) throws InterruptedException {
    tasks.add(task);
    notifyAll();
  }
  
  public synchronized int getNumberOfTasks()
  {
    return tasks.size();
  }

  @Override
  public void run() {
    while (!Thread.interrupted()) {
      try {
        put(generator.generate());
        put(generator.generate());
        sleep(15000);
      } catch (InterruptedException ex) {
        break;
      }
    }
  }
  
}
