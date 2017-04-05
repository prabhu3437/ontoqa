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

package com.acmutv.ontoqa.core.grammar;

import com.acmutv.ontoqa.core.semantics.dudes.DudesTemplates;
import com.acmutv.ontoqa.core.semantics.sltag.ElementarySltag;
import com.acmutv.ontoqa.core.semantics.sltag.SimpleElementarySltag;
import com.acmutv.ontoqa.core.semantics.sltag.SimpleSltag;
import com.acmutv.ontoqa.core.semantics.sltag.Sltag;
import com.acmutv.ontoqa.core.syntax.ltag.LtagTemplates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.acmutv.ontoqa.benchmark.Common.IS_FOUNDER_OF_IRI;
import static com.acmutv.ontoqa.benchmark.Common.IS_HEADQUARTERED_IRI;
import static com.acmutv.ontoqa.benchmark.Common.MICROSOFT_IRI;

/**
 * JUnit tests for {@link GrammarManager}.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Grammar
 * @see GrammarManager
 */
public class GrammarTest {

  private static final Logger LOGGER = LogManager.getLogger(GrammarTest.class);

  private Grammar build() {
    Grammar grammar = new SimpleGrammar();

    /* where */
    Sltag where = new SimpleSltag(LtagTemplates.wh("where"), DudesTemplates.where());
    LOGGER.info("where:\n{}", where.toPrettyString());

    /* is */
    Sltag is = new SimpleSltag(
        LtagTemplates.copula("is", "1","2"),
        DudesTemplates.copula("1", "2"));
    LOGGER.info("is:\n{}", is.toPrettyString());

    /* is * headquartered (interrogative) */
    Sltag isHeadquartered = new SimpleSltag(
        LtagTemplates.transitiveVerbPassiveIndicativeInterrogative("headquartered", "is","subj", "obj"),
        DudesTemplates.property(IS_HEADQUARTERED_IRI,"subj", "obj"));
    LOGGER.info("is * headquartered:\n{}", isHeadquartered.toPrettyString());

    /* Microsoft */
    Sltag microsoft = new SimpleSltag(
        LtagTemplates.properNoun("Microsoft"),
        DudesTemplates.properNoun(MICROSOFT_IRI)
    );
    LOGGER.info("Microsoft:\n{}", microsoft.toPrettyString());

    /* founders of */
    Sltag foundersOf = new SimpleSltag(
        LtagTemplates.relationalPrepositionalNoun("founders", "of", "obj", false),
        DudesTemplates.relationalNounInverse(IS_FOUNDER_OF_IRI, "obj",false)
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("where", where)
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("is", is)
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("is \\w* headquartered", isHeadquartered)
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("Microsoft", microsoft)
    );

    grammar.addElementarySLTAG(
        new SimpleElementarySltag("founders of", foundersOf)
    );

    return grammar;
  }


  /**
   * Tests grammar match type.
   */
  @Test
  public void test_matchType() {
    Grammar grammar = CommonGrammar.build_completeGrammar();

    String[] entries = {
        "what",
        "who",
        "where",
        "how many",

        "a",
        "an",
        "the",

        "Apple",
        "Google",
        "Italy",
        "Microsoft",
        "Satya Nadella",

        "CEO",
        "CEO of",

        "chairman",
        "chairman of",

        "chief",
        "chief executive",
        "chief executive officer",
        "chief executive officer of",

        "chief financial",
        "chief financial officer",
        "chief financial officer of",

        "company",

        "corporate",
        "corporate officers",
        "corporate officers of",

        "founders",
        "founders of",

        "name",
        "name of",

        "net",
        "net income",
        "net income of",

        "people",

        "president",
        "president of",

        "acquire",
        "are",
        "did",
        "is",
        //"is Microsoft", // WARNING
        //"is Microsoft headquartered", // WARNING
        "founded",

        "headquartered",
        "headquartered in",
        "italian",
        "the most",
        "the most valuable",

        "is the",
        "are the"
    };

    GrammarMatchType[] results = {
        GrammarMatchType.FULL,
        GrammarMatchType.FULL,
        GrammarMatchType.FULL,
        GrammarMatchType.FULL,

        GrammarMatchType.FULL,
        GrammarMatchType.FULL,
        GrammarMatchType.FULL,

        GrammarMatchType.FULL,
        GrammarMatchType.FULL,
        GrammarMatchType.FULL,
        GrammarMatchType.FULL,
        GrammarMatchType.FULL,

        GrammarMatchType.PART,
        GrammarMatchType.FULL,

        GrammarMatchType.PART,
        GrammarMatchType.FULL,

        GrammarMatchType.PART,
        GrammarMatchType.PART,
        GrammarMatchType.PART,
        GrammarMatchType.FULL,

        GrammarMatchType.PART,
        GrammarMatchType.PART,
        GrammarMatchType.FULL,

        GrammarMatchType.FULL,

        GrammarMatchType.PART,
        GrammarMatchType.PART,
        GrammarMatchType.FULL,

        GrammarMatchType.PART,
        GrammarMatchType.FULL,

        GrammarMatchType.PART,
        GrammarMatchType.FULL,

        GrammarMatchType.PART,
        GrammarMatchType.PART,
        GrammarMatchType.FULL,

        GrammarMatchType.FULL,

        GrammarMatchType.PART,
        GrammarMatchType.FULL,

        GrammarMatchType.FULL,
        GrammarMatchType.FULL,
        GrammarMatchType.FULL,
        GrammarMatchType.FULL,
        //GrammarMatchType.PART_STAR, // WARNING
        //GrammarMatchType.FULL, // WARNING
        GrammarMatchType.FULL,

        GrammarMatchType.PART,
        GrammarMatchType.FULL,
        GrammarMatchType.FULL,
        GrammarMatchType.PART,
        GrammarMatchType.FULL,

        GrammarMatchType.NONE,
        GrammarMatchType.NONE
    };

    for (int i = 0; i < entries.length; i++) {
      String entry = entries[i];
      LOGGER.info("\n\n#=====#\nMatching {}\n#=====#", entry);
      GrammarMatchType expected = results[i];
      GrammarMatchType actual = grammar.matchType(entry);
      Assert.assertEquals("entry: " + entry, expected, actual);
    }
  }

  /**
   * Tests match start
   */
  @Test
  public void test_matchStart() {
    Grammar grammar = build();

    String[] entries = {
        "",
        "where",
        "is",
        "Microsoft",
        "is Microsoft",
        "is Microsoft headquartered",
        "is Microsoft headquartered in"
    };

    boolean[] results = {
        true,
        true,
        true,
        true,
        false,
        false,
        false
    };

    for (int i = 0; i < entries.length; i++) {
      String entry = entries[i];
      boolean expected = results[i];
      boolean actual = grammar.matchStart(entry);
      Assert.assertEquals("entry: " + entry, expected, actual);
    }
  }

  /**
   * Tests grammar match.
   */
  @Test
  public void test_match() {
    Grammar grammar = build();

    String[] entries = {
        "",
        "where",
        "is",
        "Microsoft",
        "is Microsoft",
        "is Microsoft headquartered",
        "is Microsoft headquartered in"
    };

    boolean[] results = {
        true,
        true,
        true,
        true,
        true,
        true,
        false
    };

    for (int i = 0; i < entries.length; i++) {
      String entry = entries[i];
      boolean expected = results[i];
      boolean actual = grammar.match(entry);
      Assert.assertEquals("entry: " + entry, expected, actual);
    }
  }

  /**
   * Tests the matching of Elementary SLTAG.
   */
  @Test
  @Ignore
  public void test_getAllMatchingElementaryTree() {
    Grammar grammar = build();

    String[] lexicalEntries = {
        "",
        "where",
        "is",
        "is Microsoft",
        "is Microsoft headquartered",
        "is Microsoft headquartered in",
        "Microsoft"
    };

    ElementarySltag where = new SimpleElementarySltag("where", new SimpleSltag(LtagTemplates.wh("where"), DudesTemplates.where()));
    ElementarySltag isHeadquartered = new SimpleElementarySltag("is \\w* headquartered", new SimpleSltag(
        LtagTemplates.transitiveVerbPassiveIndicativeInterrogative("headquartered", "is","subj", "obj"),
        DudesTemplates.property(IS_HEADQUARTERED_IRI,"subj", "obj")));
    ElementarySltag microsoft = new SimpleElementarySltag("Microsoft",  new SimpleSltag(
        LtagTemplates.properNoun("Microsoft"),
        DudesTemplates.properNoun(MICROSOFT_IRI)
    ));

    List<List<ElementarySltag>> results = new ArrayList<>();
    results.add(new ArrayList<>());
    results.add(new ArrayList<ElementarySltag>(){{
      add(where);
    }});
    results.add(new ArrayList<>());
    results.add(new ArrayList<>());
    results.add(new ArrayList<ElementarySltag>(){{
      add(isHeadquartered);
    }});
    results.add(new ArrayList<>());
    results.add(new ArrayList<ElementarySltag>(){{
      add(microsoft);
    }});

    for (int i = 0; i < lexicalEntries.length; i++) {
      String lexicalEntry = lexicalEntries[i];
      List<ElementarySltag> expected = results.get(i);
      List<ElementarySltag> actual = grammar.getAllMatchingElementarySLTAG(lexicalEntry);
      Assert.assertEquals(lexicalEntry, expected, actual);
    }
  }

}
