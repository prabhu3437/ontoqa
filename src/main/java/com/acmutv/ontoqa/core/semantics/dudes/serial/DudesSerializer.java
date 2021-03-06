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

package com.acmutv.ontoqa.core.semantics.dudes.serial;
import com.acmutv.ontoqa.core.semantics.base.slot.Slot;
import com.acmutv.ontoqa.core.semantics.base.statement.Statement;
import com.acmutv.ontoqa.core.semantics.base.term.Term;
import com.acmutv.ontoqa.core.semantics.base.term.Variable;
import com.acmutv.ontoqa.core.semantics.drs.Drs;
import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * The JSON serializer for {@link Dudes}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Dudes
 * @see DudesDeserializer
 */
public class DudesSerializer extends StdSerializer<Dudes> {

  /**
   * The singleton of {@link DudesSerializer}.
   */
  private static DudesSerializer instance;

  /**
   * Returns the singleton of {@link DudesSerializer}.
   * @return the singleton.
   */
  public static DudesSerializer getInstance() {
    if (instance == null) {
      instance = new DudesSerializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link DudesSerializer}.
   */
  private DudesSerializer() {
    super((Class<Dudes>) null);
  }

  @Override
  public void serialize(Dudes value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();

    if (!value.getProjection().isEmpty()) {
      gen.writeArrayFieldStart("return");
      for (Term term : value.getProjection()) {
        gen.writeString(term.toString());
      }
      gen.writeEndArray();
    }

    boolean v = value.getMainVariable() != null;
    boolean d = value.getMainDrs() != 0;
    if (v || d) {
      String mainField = String.format("%s%s%s",
          (v) ? value.getMainVariable() : "",
          (d) ? "@" : "",
          (d) ? value.getMainDrs() : "");
      gen.writeStringField("main", mainField);
    }

    Drs drs = value.getDrs();

    if (drs.getLabel() != 0) {
      gen.writeNumberField("label", drs.getLabel());
    }

    if (!drs.getVariables().isEmpty()) {
      gen.writeArrayFieldStart("variables");
      for (Variable variable : drs.getVariables()) {
        gen.writeString(variable.toString());
      }
      gen.writeEndArray();
    }

    if (!drs.getStatements().isEmpty()) {
      gen.writeArrayFieldStart("statements");
      for (Statement statement : drs.getStatements()) {
        gen.writeString(statement.toString());
      }
      gen.writeEndArray();
    }

    if (!value.getSlots().isEmpty()) {
      gen.writeArrayFieldStart("slots");
      for (Slot slot : value.getSlots()) {
        gen.writeString(slot.toString());
      }
      gen.writeEndArray();
    }

    if (!value.isSelect()) {
      gen.writeBooleanField("select", value.isSelect());
    }

    gen.writeEndObject();
  }
}