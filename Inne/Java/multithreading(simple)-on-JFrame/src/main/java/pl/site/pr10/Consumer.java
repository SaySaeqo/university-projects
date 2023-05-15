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

import java.util.Random;
import javax.swing.JTextField;
import lombok.AllArgsConstructor;

/**
 *
 * @author SaySaeqo
 */
@AllArgsConstructor
public class Consumer implements Runnable{
  
  private final StorageLIFO<Object> storage;
  private final Random rng = new Random();
  private final JTextField text_field;
  
  void consume(Object o) throws InterruptedException{
    text_field.setText(String.valueOf("Last consumed: " + o.hashCode()%1000));
    Thread.sleep(rng.nextInt(10)*1000);
    //System.out.println("Uzywam produktu... " + o.hashCode());
  }

  @Override
  public void run() {
    while (! Thread.interrupted())
    {
      try {
        //System.out.println("Consumer " + Thread.currentThread().getId() + ":");
        Object product = storage.pop();
        consume(product);
        //storage.show();
      } catch (InterruptedException ex) {
        break;
      }
      
    }
  }
  
}
