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

package com.acmutv.ontoqa.benchmark.basic;

import com.acmutv.ontoqa.benchmark.Common;
import com.acmutv.ontoqa.core.CoreController;
import com.acmutv.ontoqa.core.exception.*;
import com.acmutv.ontoqa.core.knowledge.answer.Answer;
import com.acmutv.ontoqa.core.knowledge.answer.SimpleAnswer;
import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
import com.acmutv.ontoqa.core.semantics.sltag.SimpleSltag;
import com.acmutv.ontoqa.core.semantics.sltag.Sltag;
import com.acmutv.ontoqa.core.semantics.sltag.SltagBuilder;
import com.acmutv.ontoqa.core.syntax.ltag.LtagTemplates;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;

import java.io.IOException;

import static com.acmutv.ontoqa.benchmark.Common.*;

/**
 * JUnit tests for questions of class [CLASS BASIC-3].
 * `How many people founded Microsoft?`
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class QuestionB03Test {

  private static final Logger LOGGER = LogManager.getLogger(QuestionB03Test.class);

  private static final String QUESTION = "How many people founded Microsoft?";

  private static final Answer ANSWER = new SimpleAnswer("2");

  /**
   * Tests the question-answering with parsing.
   * @throws QuestionException when the question is malformed.
   * @throws OntoqaFatalException when the question cannot be processed due to some fatal errors.
   */
  @Test
  public void test_nlp() throws Exception {
    Common.loadSession();
    final Answer actual = CoreController.process(QUESTION);
    Assert.assertEquals(ANSWER, actual);
  }

  /**
   * Tests the question-answering with manual compilation of SLTAG.
   * @throws QuestionException when question is malformed.
   * @throws OntoqaFatalException when question cannot be processed due to some fatal errors.
   */
  @Test
  public void test_manual() throws OntoqaFatalException, QuestionException, QueryException, LTAGException, IOException {
    /* how many */
    Sltag howMany = new SimpleSltag(
        LtagTemplates.how("how", "many", "np"),
        DudesTemplates.howmany("np")
    );
    LOGGER.info("how many:\n{}", howMany.toPrettyString());

    /* people */
    Sltag people = new SimpleSltag(
        LtagTemplates.classNoun("people", false),
        DudesTemplates.type(RDF_TYPE_IRI, PERSON_IRI)
    );
    LOGGER.info("people:\n{}", people.toPrettyString());

    /* founded */
    Sltag founded = new SimpleSltag(
        LtagTemplates.transitiveVerbActiveIndicative("founded", "subj", "obj"),
        DudesTemplates.property(IS_FOUNDER_OF_IRI, "subj", "obj")
    );
    LOGGER.info("founded:\n{}", founded.toPrettyString());

    /* Microsoft */
    Sltag microsoft = new SimpleSltag(
        LtagTemplates.properNoun("Microsoft"),
        DudesTemplates.properNoun(MICROSOFT_IRI)
    );
    LOGGER.info("Microsoft:\n{}", microsoft.toPrettyString());

    /* how many people */
    LOGGER.info("how many people: processing...");
    Sltag howManyPeople = new SltagBuilder(howMany)
        .substitution(people, "np")
        .build();
    LOGGER.info("how many people:\n{}", howManyPeople.toPrettyString());

    /* how many people founded */
    LOGGER.info("how many people founded: processing...");
    Sltag howManyPeopleFounded = new SltagBuilder(founded)
        .substitution(howManyPeople, "subj")
        .build();
    LOGGER.info("how many people founded:\n{}", howManyPeopleFounded.toPrettyString());

    /* how many people founded Microsoft */
    LOGGER.info("how many people founded Microsoft: processing...");
    Sltag howManyPeopleFoundedMicrosoft = new SltagBuilder(howManyPeopleFounded)
        .substitution(microsoft, "obj")
        .build();
    LOGGER.info("how many people founded Microsoft:\n{}", howManyPeopleFoundedMicrosoft.toPrettyString());

    /* SPARQL */
    LOGGER.info("SPARQL query: processing...");
    Query query = howManyPeopleFoundedMicrosoft.convertToSPARQL();
    LOGGER.info("SPARQL query:\n{}", query);

    Common.test_query(query, ANSWER);
  }

  /**
   * Tests the ontology answering on raw SPARQL query submission.
   */
  @Test
  public void test_ontology() throws OntoqaFatalException, IOException, QueryException {
    String sparql = String.format("SELECT (COUNT(DISTINCT ?people) AS ?fout0) WHERE { ?people <%s> <%s> }", IS_FOUNDER_OF_IRI, MICROSOFT_IRI);
    Query query = QueryFactory.create(sparql);
    LOGGER.debug("SPARQL query:\n{}", query);
    Common.test_query(query, ANSWER);
  }
}
