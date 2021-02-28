/*
 * The MIT License
 * Copyright © 2014-2021 Ilkka Seppälä
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

package com.iluwatar.activeobject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Active Object pattern helps to solve synchronization difficulties without using 
 * 'synchronized' methods.The active object will contain a thread-safe data structure 
 * (such as BlockingQueue) and use to synchronize method calls by moving the logic of the method
 * into an invocator(usually a Runnable) and store it in the DSA.
 * 
 * <p>In this example, we fire 20 threads to modify a value in the target class.
 */
public class App implements Runnable {
  
  private final Logger logger = LoggerFactory.getLogger(ActiveCreature.class.getName());
  
  private final Integer creatures = 3;

  /**
   * Program entry point.
   *
   * @param args command line args
   */
  public static void main(String[] args) {  
    var app = new App();
    app.run();
  }
  
  @Override
  public void run() {
    ActiveCreature creature;
    try {
      for (int i = 0;i < creatures;i++) {
        creature = new Orc(Orc.class.getSimpleName().toString() + i);
        creature.eat();
        creature.roam();
      }
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      logger.error(e.getMessage());
    }
    Runtime.getRuntime().exit(1);
  }
}
