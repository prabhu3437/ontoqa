/*
  The MIT License (MIT)

  Copyright (c) 2016 Antonella Botte, Giacomo Marciani and Debora Partigianoni

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

package com.acmutv.ontoqa.config.serial;

import com.acmutv.ontoqa.config.AppConfiguration;
import com.acmutv.ontoqa.core.grammar.GrammarFormat;
import com.acmutv.ontoqa.core.knowledge.ontology.OntologyFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * The JSON deserializer for {@link AppConfiguration}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see AppConfiguration
 * @see AppConfigurationSerializer
 */
public class AppConfigurationDeserializer extends StdDeserializer<AppConfiguration> {

  /**
   * The singleton of {@link AppConfigurationDeserializer}.
   */
  private static AppConfigurationDeserializer instance;

  /**
   * Returns the singleton of {@link AppConfigurationDeserializer}.
   * @return the singleton.
   */
  public static AppConfigurationDeserializer getInstance() {
    if (instance == null) {
      instance = new AppConfigurationDeserializer();
    }
    return instance;
  }

  /**
   * Initializes the singleton of {@link AppConfigurationDeserializer}.
   */
  private AppConfigurationDeserializer() {
    super((Class<AppConfiguration>) null);
  }


  @Override
  public AppConfiguration deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    AppConfiguration config = new AppConfiguration();
    JsonNode node = parser.getCodec().readTree(parser);

    if (node.hasNonNull("ontologyPath")) {
      final String ontologyPath = node.get("ontologyPath").asText();
      config.setOntologyPath(ontologyPath);
    }

    if (node.hasNonNull("ontologyFormat")) {
      final OntologyFormat ontologyFormat = OntologyFormat.valueOf(node.get("ontologyFormat").asText());
      config.setOntologyFormat(ontologyFormat);
    }

    if (node.hasNonNull("grammarPath")) {
      final String grammarPath = node.get("grammarPath").asText();
      config.setGrammarPath(grammarPath);
    }

    if (node.hasNonNull("grammarFormat")) {
      final GrammarFormat grammarFormat = GrammarFormat.valueOf(node.get("grammarFormat").asText());
      config.setGrammarFormat(grammarFormat);
    }

    return config;
  }
}
