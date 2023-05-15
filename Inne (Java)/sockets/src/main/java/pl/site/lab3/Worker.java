/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.site.lab3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author SaySaeqo
 */
public class Worker implements Runnable{

  private final TaskBoard board;
  private Socket connection = null;
  
  public Worker(TaskBoard b){
    board = b;
  }
  
  @Override
  public void run() {
    while (!Thread.interrupted()) {
      try {
        connection = board.take();
      } catch (InterruptedException ex) {
        break;
      }
      
      try(OutputStream out_stream = connection.getOutputStream();
        InputStream in_stream = connection.getInputStream();)
      {
        // init printer
        PrintWriter printer = new PrintWriter(out_stream);
        // init std reader
        InputStreamReader in_reader = new InputStreamReader(in_stream);
        BufferedReader reader = new BufferedReader(in_reader);
        // init obj reader
        ObjectInputStream object_reader = new ObjectInputStream(in_stream);
        
        // 1st rounf
        printer.println("ready");
        printer.flush();

        int number = reader.read() - '0';

        Stack<Message> stack = new Stack<>();
        // 2nd round
        printer.println("ready for messages");
        printer.flush();

        for(int i =0; i< number; i++)
        {
          Message parcel = (Message)object_reader.readObject();
          stack.push(parcel);
        }
        // finish
        printer.println("finished");
        printer.flush();
        
      }catch(IOException | ClassNotFoundException ex)
      {
        System.err.println("Worker: " + ex.getMessage());
      }

    }
  }
  
}
