/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.site.lab3;



import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author SaySaeqo
 */
public class Server {
   public static void main(String args[]) throws IOException
 {
   int total_threads = 2;
   ServerSocket ss = new ServerSocket(1234);
   TaskBoard tasks = new TaskBoard();
   List<Thread> threads = new ArrayList<>(); 
   
   for (int i =0; i< total_threads ; i++)
   {
     Thread thread = new Thread(new Worker(tasks));
     threads.add(thread);
     thread.start();
   }
   
   boolean running = true;
   while (running)
   {
     if ( tasks.isEmpty())
     {
       Socket s = ss.accept();
       tasks.put(s);
     }
   }
   
  threads.forEach((thread)->{
            thread.interrupt();
          });
  threads.forEach((thread)->{
            try {
              thread.join();
            } catch (InterruptedException ex) {}
          });
 }
   
}
