/*
 * The MIT License
 * Copyright © 2014-2022 Ilkka Seppälä
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

package com.iluwatar.lazy.loading;

import java.lang.reflect.Field;

/**
 * Date: 12/19/15 - 12:05 PM
 *
 * @author Jeroen Meulemeester
 */
public class HolderNaiveTest extends AbstractHolderTest {

  private final HolderNaive holder = new HolderNaive();

  @Override
  Heavy getInternalHeavyValue() throws Exception {
    final var holderField = HolderNaive.class.getDeclaredField("heavy");
    holderField.setAccessible(true);
    return (Heavy) holderField.get(this.holder);
  }

  @Override
  Heavy getHeavy() {
    return holder.getHeavy();
  }

}