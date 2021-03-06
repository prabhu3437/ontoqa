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

package com.acmutv.ontoqa.benchmark.extra;

import com.acmutv.ontoqa.benchmark.Common;
import com.acmutv.ontoqa.core.CoreController;
import com.acmutv.ontoqa.core.exception.*;
import com.acmutv.ontoqa.core.grammar.CommonGrammar;
import com.acmutv.ontoqa.core.grammar.Grammar;
import com.acmutv.ontoqa.core.grammar.SimpleGrammar;
import com.acmutv.ontoqa.core.knowledge.answer.Answer;
import com.acmutv.ontoqa.core.knowledge.answer.SimpleAnswer;
import com.acmutv.ontoqa.core.knowledge.ontology.Ontology;
import com.acmutv.ontoqa.core.semantics.base.statement.OperatorType;
import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
import com.acmutv.ontoqa.core.semantics.sltag.SimpleElementarySltag;
import com.acmutv.ontoqa.core.semantics.sltag.SimpleSltag;
import com.acmutv.ontoqa.core.semantics.sltag.Sltag;
import com.acmutv.ontoqa.core.semantics.sltag.SltagBuilder;
import com.acmutv.ontoqa.core.syntax.ltag.LtagTemplates;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.acmutv.ontoqa.benchmark.Common.*;
import static com.acmutv.ontoqa.core.semantics.TestAllSemantics.RDF_TYPE_IRI;

/**
 * JUnit tests for questions of class [CLASS EXTRA-06].
 * `What is the most valuable company?`
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class QuestionE06Test {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuestionE06Test.class);

  private static final String QUESTION = "What is the most valuable company?";

  private static final Answer ANSWER = new SimpleAnswer(APPLE_IRI);

  private static final String QUERY_1 = String.format("SELECT DISTINCT  ?v4\n" +
      "WHERE\n" +
      "  { ?v4  <%s>  ?v7}\n" +
      "ORDER BY DESC(?v7)\n" +
      "OFFSET  0\n" +
      "LIMIT   1\n", HAS_COMPANY_VALUE_IRI);

  private static final String QUERY_2 = String.format("SELECT DISTINCT  ?v4\n" +
      "WHERE\n" +
      "  { ?v4  <%s>  ?v7}\n" +
      "ORDER BY DESC(?v7)\n" +
      "OFFSET  0\n" +
      "LIMIT   1\n", HAS_COMPANY_VALUE_IRI);

  /**
   * Tests the question-answering with parsing.
   * @throws QuestionException when the question is malformed.
   * @throws OntoqaFatalException when the question cannot be processed due to some fatal errors.
   */
  @Test
  public void test_nlp() throws Exception {
    final Grammar grammar = Common.getGrammar();
    final Ontology ontology = Common.getOntology();
    final Pair<Query,Answer> result = CoreController.process(QUESTION, grammar, ontology);
    final Query query = result.getKey();
    final Answer answer = result.getValue();
    LOGGER.info("Query: {}", query);
    LOGGER.info("Answer: {}", answer);
    Assert.assertEquals(QUERY_1, query.toString());
    Assert.assertEquals(ANSWER, answer);
  }

  /**
   * Tests the question-answering with parsing.
   * @throws QuestionException when the question is malformed.
   * @throws OntoqaFatalException when the question cannot be processed due to some fatal errors.
   */
  @Test
  public void test_nlp_wired() throws Exception {
    final Grammar grammar = CommonGrammar.build_completeGrammar();
    final Ontology ontology = Common.getOntology();
    final Pair<Query,Answer> result = CoreController.process(QUESTION, grammar, ontology);
    final Query query = result.getKey();
    final Answer answer = result.getValue();
    LOGGER.info("Query: {}", query);
    LOGGER.info("Answer: {}", answer);
    Assert.assertEquals(QUERY_2, query.toString());
    Assert.assertEquals(ANSWER, answer);
  }

  /**
   * Tests the question-answering with manual compilation of SLTAG.
   * @throws QuestionException when question is malformed.
   * @throws OntoqaFatalException when question cannot be processed due to some fatal errors.
   */
  @Test
  public void test_manual() throws OntoqaFatalException, QuestionException, QueryException, LTAGException, IOException {
    /* what */
    Sltag what = new SimpleSltag(LtagTemplates.wh("what"), DudesTemplates.what());
    LOGGER.info("what:\n{}", what.toPrettyString());

    /* is */
    Sltag is = new SimpleSltag(
        LtagTemplates.copula("is", "1", "2"),
        DudesTemplates.copula("1", "2")
    );
    LOGGER.info("is:\n{}", is.toPrettyString());

    /* the most valuable */
    Sltag theMostValuable = new SimpleSltag(
        LtagTemplates.adjectiveSuperlative("most valuable", "the", "company"),
        DudesTemplates.adjectiveSuperlative(OperatorType.MAX, HAS_COMPANY_VALUE_IRI, "company"));
    LOGGER.info("the most valuable:\n{}", theMostValuable.toPrettyString());

    /* company */
    Sltag company = new SimpleSltag(
        LtagTemplates.classNoun("company", false),
        DudesTemplates.type(RDF_TYPE_IRI, COMPANY_IRI)
    );
    LOGGER.info("company:\n{}", company.toPrettyString());

    /* the most valuable company */
    LOGGER.info("the most valuable company: processing...");
    Sltag theMostValuableCompany = new SltagBuilder(theMostValuable)
        .substitution(company, "company")
        .build();
    LOGGER.info("the most valuable company:\n{}", theMostValuableCompany.toPrettyString());

    /* what is */
    LOGGER.info("what is: processing...");
    Sltag whatIs = new SltagBuilder(is)
        .substitution(what, "1")
        .build();
    LOGGER.info("what is:\n{}", whatIs.toPrettyString());

    /* who is the most valuable company */
    LOGGER.info("what is the most valuable company: processing...");
    Sltag whatIsTheMostValuableCompany = new SltagBuilder(whatIs)
        .substitution(theMostValuableCompany, "2")
        .build();
    LOGGER.info("what is the most valuable company:\n{}", whatIsTheMostValuableCompany.toPrettyString());

    /* SPARQL */
    LOGGER.info("SPARQL query: processing...");
    Query query = whatIsTheMostValuableCompany.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", query);

    Common.test_query(query, ANSWER);
  }

  /**
   * Tests the ontology answering on raw SPARQL query submission.
   */
  @Test
  public void test_ontology() throws OntoqaFatalException, IOException, QueryException {
    String sparql = String.format("SELECT ?x ?value WHERE { ?x a <%s> . ?x <%s> ?value } ORDER BY DESC(?value) LIMIT 1",
        COMPANY_IRI, HAS_COMPANY_VALUE_IRI);
    Query query = QueryFactory.create(sparql);
    LOGGER.debug("SPARQL query:\n{}", query);
    Common.test_query(query, ANSWER);
  }
}
