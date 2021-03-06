/*
  The MIT License (MIT)

  Copyright (c) 2016 Giacomo Marciani and Michele Porretta

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:


  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.


  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
 */

package com.acmutv.ontoqa.core.semantics.dudes;

import com.acmutv.ontoqa.core.semantics.dudes.serial.DudesJsonMapper;
import com.acmutv.ontoqa.core.semantics.dudes.serial.DudesYamlMapper;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * JUnit tests for {@link Dudes} serialization.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Dudes
 * @see DudesJsonMapper
 */
public class DudesSerializationTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(DudesSerializationTest.class);

  /**
   * Asserts the serialization correctness.
   * @param expected the expected value.
   * @throws IOException when value cannot be serialized or deserialized.
   */
  private void testSerialization(Dudes expected) throws IOException {
    DudesJsonMapper jsonMapper = new DudesJsonMapper();
    String json = jsonMapper.writeValueAsString(expected);
    LOGGER.debug("DUDES JSON serialization: \n{}", json);
    Dudes actualJson = jsonMapper.readValue(json, Dudes.class);
    Assert.assertEquals(expected, actualJson);

    DudesYamlMapper yamlMapper = new DudesYamlMapper();
    String yaml = yamlMapper.writeValueAsString(expected);
    LOGGER.debug("DUDES YAML serialization: \n{}", yaml);
    Dudes actualYaml = yamlMapper.readValue(yaml, Dudes.class);
    Assert.assertEquals(expected, actualYaml);
  }

  /**
   * Tests {@link Dudes} serialization/deserialization.
   * @throws IOException when DUDES cannot be serialized/deserialized.
   */
  @Test
  public void test_simple() throws IOException {
    Dudes expected = DudesTemplates.properNoun("http://dbpedia.org/resource/Albert_Einstein");

    testSerialization(expected);
  }

}
