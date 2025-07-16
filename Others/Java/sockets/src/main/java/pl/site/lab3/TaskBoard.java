/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.site.lab3;

import java.net.Socket;
import java.util.Stack;
import lombok.NoArgsConstructor;

/**
 *
 * @author SaySaeqo
 */
@NoArgsConstructor
public class TaskBoard {
  
  private Stack<Socket> connections = new Stack<>();
  
  public synchronized void put(Socket new_connection)
  {
    connections.push(new_connection);
    notifyAll();
  }
  
  public synchronized Socket take() throws InterruptedException
  {
    while (connections.empty())
    {
      wait();
    }
    return connections.pop();
  }
  
  public synchronized boolean isEmpty(){
    return connections.empty();
  }
}
