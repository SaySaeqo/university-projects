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

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author SaySaeqo
 */
public class Storage<T> {
  
  private final List<T> queue = new ArrayList<>();
  private final int capacity;
  
  public Storage(int capacity)
  {
    this.capacity = capacity;
    //for(int i = 0; i < this.capacity; i++) queue.add((T) new Object());
  }

  public synchronized void put(T e) throws InterruptedException {
      System.out.println("Producer " + Thread.currentThread().getId() + ":");
      while (queue.size() >= capacity) {
          wait();
      }
      //System.out.println("Magazyn przyjął... " + e.hashCode());
      queue.add(e);
      //Thread.sleep(500);
      notifyAll();
      
      show();
  }

  public synchronized T take() throws InterruptedException {
      System.out.println("Consumer " + Thread.currentThread().getId() + ":");
      while (queue.isEmpty()) {
          wait();
      }
      T firstElement = queue.get(0);
      //System.out.println("Magazyn opuścił... " + firstElement.hashCode());
          
      queue.remove(0);
      //Thread.sleep(500);
      notifyAll();
      
      show();
      
      return firstElement;
  }
  
  private void show()
  {
    System.out.println("Storage\n{");
    System.out.print("  Items: " + queue.size() + "/" + capacity);
    int counter = Integer.MAX_VALUE - 1;
    for (T obj : queue){
      if(counter++ > 10){
        System.out.print("\n  ");
        counter = 1;
      }
      System.out.print("\"" + obj.hashCode() % 1000 + "\" " );
    }
    System.out.println("\n} " + Thread.currentThread().getId());
        
  }
 
}
