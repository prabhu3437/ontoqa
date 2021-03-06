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

package com.acmutv.ontoqa.core;

import com.acmutv.ontoqa.core.exception.OntoqaParsingException;
import com.acmutv.ontoqa.core.exception.QueryException;
import com.acmutv.ontoqa.core.exception.OntoqaFatalException;
import com.acmutv.ontoqa.core.exception.QuestionException;
import com.acmutv.ontoqa.core.grammar.Grammar;
import com.acmutv.ontoqa.core.knowledge.answer.Answer;
import com.acmutv.ontoqa.core.knowledge.ontology.Ontology;
import com.acmutv.ontoqa.core.knowledge.query.QueryResult;
import com.acmutv.ontoqa.core.parser.AdvancedSltagParser;
import com.acmutv.ontoqa.core.parser.ReasoningSltagParser;
import com.acmutv.ontoqa.core.parser.SimpleSltagParserNew;
import com.acmutv.ontoqa.core.parser.SltagParser;
import com.acmutv.ontoqa.core.semantics.dudes.Dudes;
import com.acmutv.ontoqa.core.knowledge.KnowledgeManager;
import com.acmutv.ontoqa.core.semantics.sltag.Sltag;
import com.acmutv.ontoqa.model.QAResponse;
import com.acmutv.ontoqa.session.SessionManager;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The core business logic.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Dudes
 */
public class CoreController {

  private static final Logger LOGGER = LoggerFactory.getLogger(CoreController.class);

  /**
   * The SLTAG parser.
   */
  //private static SltagParser parser = new SimpleSltagParserNew();
  private static ReasoningSltagParser parser = new AdvancedSltagParser();

  /**
   * The core main method.
   * It realizes the question-answering process, retrieving an answer for the given question.
   * The underlying ontology and lexicon are specified in the app configuration.
   * @param question the question.
   * @return the answer.
   * @throws QuestionException when question is malformed.
   * @throws QueryException when the SPARQL query cannot be submitted.
   * @throws OntoqaFatalException when question cannot be processed.
   * @throws OntoqaParsingException when parsing error occurs.
   */
  public static Answer process(String question) throws Exception {
    LOGGER.debug("Question: {}", question);
    final String normalizedQuestion = normalizeQuestion(question);
    LOGGER.debug("Normalized question: {}", normalizedQuestion);
    Sltag sltag = parser.parse(normalizedQuestion, SessionManager.getGrammar(), SessionManager.getOntology());
    Dudes dudes = sltag.getSemantics();
    Query query = dudes.convertToSPARQL();
    QueryResult qQueryResult = KnowledgeManager.submit(SessionManager.getOntology(), query);
    return qQueryResult.toAnswer();
  }

  /**
   * Realizes the question-answering process.
   * Fills {@code response} with everything about the process.
   * The underlying ontology and lexicon are specified in the app configuration.
   * @param question the question.
   * @param response the response about the process.
   * @throws QuestionException when question is malformed.
   * @throws QueryException when the SPARQL query cannot be submitted.
   * @throws OntoqaFatalException when question cannot be processed.
   * @throws OntoqaParsingException when parsing error occurs.
   */
  public static void process(String question, QAResponse response) throws Exception {
    LOGGER.debug("Question: {}", question);
    final String normalizedQuestion = normalizeQuestion(question);
    LOGGER.debug("Normalized question: {}", normalizedQuestion);
    Sltag sltag = parser.parse(normalizedQuestion, SessionManager.getGrammar(), SessionManager.getOntology());
    Dudes dudes = sltag.getSemantics();
    Query query = dudes.convertToSPARQL();
    QueryResult qQueryResult = KnowledgeManager.submit(SessionManager.getOntology(), query);
    Answer answer = qQueryResult.toAnswer();
    if (response != null) {
      response.setQuestion(normalizedQuestion);
      response.setAnswer(answer);
      response.setQuery(query.toString());
      response.setSltag(sltag);
    }
  }

  /**
   * The core main method.
   * It realizes the question-answering process, retrieving an answer for the given question.
   * The underlying ontology and lexicon are specified in the app configuration.
   * @param question the question.
   * @param grammar the SLTAG grammar.
   * @param ontology the ontology.
   * @return the answer.
   * @throws QuestionException when question is malformed.
   * @throws QueryException when the SPARQL query cannot be submitted.
   * @throws OntoqaFatalException when question cannot be processed.
   * @throws OntoqaParsingException when parsing error occurs.
   */
  public static Pair<Query,Answer> process(String question, Grammar grammar, Ontology ontology)
      throws Exception {
    LOGGER.debug("Question: {}", question);
    final String normalizedQuestion = normalizeQuestion(question);
    LOGGER.debug("Normalized question: {}", normalizedQuestion);
    Sltag sltag = parser.parse(normalizedQuestion, grammar, ontology);
    Dudes dudes = sltag.getSemantics();
    Query query = dudes.convertToSPARQL();
    LOGGER.debug("SPARQL Query:\n{}", query.toString());
    QueryResult qQueryResult = KnowledgeManager.submit(ontology, query);
    Answer answer = qQueryResult.toAnswer();
    LOGGER.trace(answer.toPrettyString());
    return new ImmutablePair<>(query, answer);
  }

  /**
   * Returns the normalized version of {@code question}.
   * @param question the question to normalize.
   * @return the normalized version of {@code question}; empty, if question is null or empty.
   */
  public static String normalizeQuestion(final String question) {
    if (question == null || question.isEmpty()) return "";
    String cleaned =  question.replaceAll("((?:\\s)+)", " ").replaceAll("((?:\\s)*\\?)", "");
    return Character.toLowerCase(cleaned.charAt(0)) + cleaned.substring(1);
  }

}
