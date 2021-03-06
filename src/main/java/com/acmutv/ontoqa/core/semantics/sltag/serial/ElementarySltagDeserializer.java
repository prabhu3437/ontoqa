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

package com.acmutv.ontoqa.core.semantics.sltag.serial;

import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.acmutv.ontoqa.core.semantics.sltag.ElementarySltag;
import com.acmutv.ontoqa.core.semantics.sltag.SimpleElementarySltag;
import com.acmutv.ontoqa.core.syntax.ltag.Ltag;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * The JSON deserializer for {@link ElementarySltag}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see ElementarySltag
 * @see ElementarySltagSerializer
 */
public class ElementarySltagDeserializer extends StdDeserializer<ElementarySltag> {

  /**
   * The singleton of {@link ElementarySltagDeserializer}.
   */
  private static ElementarySltagDeserializer instance;

  /**
   * Returns the singleton of {@link ElementarySltagDeserializer}.
   * @return the singleton.
   */
  public static ElementarySltagDeserializer getInstance() {
    if (instance == null) {
      instance = new ElementarySltagDeserializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link ElementarySltagDeserializer}.
   */
  private ElementarySltagDeserializer() {
    super((Class<ElementarySltag>) null);
  }


  @Override
  public ElementarySltag deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);

    if (!node.hasNonNull("entry") ||
        !node.hasNonNull("syntax") ||
        !node.hasNonNull("semantics")) {
      throw new IOException("Cannot read [entry,syntax,semantics].");
    }

    final String entry = node.get("entry").asText();

    final Ltag ltag = ctx.readValue(node.get("syntax").traverse(parser.getCodec()), Ltag.class);

    final Dudes dudes = ctx.readValue(node.get("semantics").traverse(parser.getCodec()), Dudes.class);

    return new SimpleElementarySltag(entry, ltag, dudes);
  }
}
