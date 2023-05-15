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
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


/**
 *
 * @author SaySaeqo
 */
public class Client {
  

 public static void main(String args[]) throws IOException, ClassNotFoundException
 {
   
  Socket socket = new Socket("localhost",1234);
  Scanner scan = new Scanner(System.in);
   
  OutputStream out_stream = socket.getOutputStream();
  InputStream in_stream = socket.getInputStream();
  // init printer
  PrintWriter printer = new PrintWriter(out_stream);
  // init std reader
  InputStreamReader in_reader = new InputStreamReader(in_stream);
  BufferedReader reader = new BufferedReader(in_reader);
  // init obj reader
  ObjectOutputStream object_writer = new ObjectOutputStream(out_stream);
  
  String respond;
      
  // 1st round
  respond = reader.readLine();
  System.out.println("server: " + respond);

  int n = scan.nextInt();
  printer.print(n);
  printer.flush();
  // 2n round
  respond = reader.readLine();
  System.out.println("server: " + respond);

  for (int i =0; i< n; i++){
    Message parcel = new Message(i, "message number " + i);
    object_writer.writeObject(parcel);
    object_writer.flush();
  }
  
  // finish
  respond = reader.readLine();
  System.out.println("server: " + respond);

  out_stream.close();
  in_stream.close();
   
 }

}
