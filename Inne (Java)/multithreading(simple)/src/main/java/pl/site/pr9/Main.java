/*
 * The MIT License
 *
 * Copyright 2021 SaySaeqo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pl.site.pr9;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author SaySaeqo
 */
public class Main {
  
  public static void main(String[] args) {
    
    int consumers_number = 4;
    int producers_number = 6;
    List<Thread> threads = new ArrayList<>();
    Storage<Object> storage = new Storage<>(50);
    Random rng = new Random();
    
    for(int i=0; i < producers_number; i++){
      Thread th = new Thread(new Producer(storage, rng.nextInt(10) * 1000));
      threads.add(th);
    }
    for(int i=0; i < consumers_number; i++){
      Thread th = new Thread(new Consumer(storage, rng.nextInt(10) * 1000));
      threads.add(th);
    }
    
    threads.forEach(th -> th.start());
    
    Scanner scan = new Scanner(System.in);
    boolean running = true;
    while (running)
    {
      String command = scan.next();
      switch (command){
        default:
          running = false;
      }
    }
    
    threads.forEach(th -> th.interrupt());
    threads.forEach(th -> {
      try{
        th.join();
      } catch (InterruptedException ex){
        ;
      }
    });
  }
}
