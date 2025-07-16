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
package pl.site.pr10;

import java.util.List;
import java.util.LinkedList;
import javax.swing.JTextArea;
import lombok.AllArgsConstructor;

/**
 *
 * @author SaySaeqo
 * @param <T>
 */
@AllArgsConstructor
public class StorageLIFO<T> {
  
  private final List<T> stack = new LinkedList<>();
  private final JTextArea text_area;
  private final JTextArea queue_info;

  public synchronized void push(T e) throws InterruptedException {
    queue_info.setText(queue_info.getText() + "\nProducer " + 
                      Thread.currentThread().getId() + " with item: " + e.hashCode()%1000);

    System.out.println("Producer " + Thread.currentThread().getId() + ":");
    //System.out.println("Magazyn przyjął... " + e.hashCode());
    stack.add(0, e);
    //Thread.sleep(500);
    notifyAll();

    show();
  }

  public synchronized T pop() throws InterruptedException {
    queue_info.setText(queue_info.getText() + "\nConsumer " + 
                      Thread.currentThread().getId());
    
    System.out.println("Consumer " + Thread.currentThread().getId() + ":");
    while (stack.isEmpty()) {
        wait();
    }
    T firstElement = stack.remove(0);
    //System.out.println("Magazyn opuścił... " + firstElement.hashCode());
    //Thread.sleep(500);
    notifyAll();

    show();

    return firstElement;
  }
  
  private void show() throws InterruptedException
  {
    Thread.sleep(1000);
    
    System.out.println("Storage Stack\n{");
    System.out.print("  Items on stack: " + stack.size());
    int counter = Integer.MAX_VALUE - 1;
    for (T obj : stack) {
      if(counter++ > 10){
        System.out.print("\n  ");
        counter = 1;
      }
      System.out.print("\"" + obj.hashCode() % 1000 + "\" " );
    }
    System.out.println("\n} " + Thread.currentThread().getId());
    
    String stack_view = "Producer " + String.valueOf(Thread.currentThread().getId())
                        + " is\nadding item...\n\nStorage Stack:\n";
    for (int i = stack.size() - 1; i >= 0; i--)
      stack_view += String.valueOf(stack.get(i).hashCode()%1000) + "\n";
    text_area.setText(stack_view);
    queue_info.setText("");
        
  }
 
}
