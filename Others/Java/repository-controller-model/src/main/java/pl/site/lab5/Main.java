package pl.site.lab5;

import java.util.Scanner;

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

/**
 *
 * @author SaySaeqo
 */
public class Main {
  
  public static void main(String[] args) {
    
    MageController ctrl = new MageController(new MageRepository());
    
    Scanner scan = new Scanner(System.in);
    boolean running = true;
    while(running)
    {
      String command = scan.next();
      switch (command){
        case "quit":
          running = false;
          break;
        case "find":
          System.out.println(ctrl.find(scan.next()));
          break;
        case "delete":
          System.out.println(ctrl.delete(scan.next()));
          break;
        default:
          System.out.println(ctrl.save(command, scan.next()));
          break;
      }
    }
  }
}
