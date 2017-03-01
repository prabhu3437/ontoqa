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

package com.acmutv.ontoqa.core.grammar.serial;

import com.acmutv.ontoqa.core.grammar.Grammar;
import com.acmutv.ontoqa.core.semantics.sltag.ElementarySLTAG;
import com.acmutv.ontoqa.core.semantics.sltag.SLTAG;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * The JSON serializer for {@link Grammar}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Grammar
 * @see GrammarDeserializer
 */
public class GrammarSerializer extends StdSerializer<Grammar> {

  /**
   * The singleton of {@link GrammarSerializer}.
   */
  private static GrammarSerializer instance;

  /**
   * Returns the singleton of {@link GrammarSerializer}.
   * @return the singleton.
   */
  public static GrammarSerializer getInstance() {
    if (instance == null) {
      instance = new GrammarSerializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link GrammarSerializer}.
   */
  private GrammarSerializer() {
    super((Class<Grammar>) null);
  }

  @Override
  public void serialize(Grammar value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartArray();

    for (SLTAG sltag : value.getAllElementarySLTAG()) {
      provider.findValueSerializer(ElementarySLTAG.class).serialize(sltag, gen, provider);
    }

    gen.writeEndArray();
  }
}