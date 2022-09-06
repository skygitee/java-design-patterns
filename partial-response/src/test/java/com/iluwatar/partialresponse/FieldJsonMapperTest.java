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

package com.iluwatar.partialresponse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * tests {@link FieldJsonMapper}.
 */
class FieldJsonMapperTest {
  private static FieldJsonMapper mapper;

  @BeforeAll
  static void setUp() {
    mapper = new FieldJsonMapper();
  }

  @Test
  void shouldReturnJsonForSpecifiedFieldsInVideo() throws Exception {
    var fields = new String[]{"id", "title", "length"};
    var video = new Video(
        2, "Godzilla Resurgence", 120,
        "Action & drama movie|", "Hideaki Anno", "Japanese"
    );

    var jsonFieldResponse = mapper.toJson(video, fields);

    var expectedDetails = "{\"id\": 2,\"title\": \"Godzilla Resurgence\",\"length\": 120}";
    Assertions.assertEquals(expectedDetails, jsonFieldResponse);
  }
}