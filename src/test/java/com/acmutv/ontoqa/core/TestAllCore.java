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

import com.acmutv.ontoqa.core.grammar.TestAllGrammar;
import com.acmutv.ontoqa.core.knowledge.TestAllKnowledge;
import com.acmutv.ontoqa.core.parser.TestAllParser;
import com.acmutv.ontoqa.core.semantics.TestAllSemantics;
import com.acmutv.ontoqa.core.syntax.TestAllSyntax;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * JUnit test suite for core services.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see TestAllGrammar
 * @see TestAllKnowledge
 * @see TestAllParser
 * @see TestAllSemantics
 * @see TestAllSyntax
 * @see CoreControllerTest
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestAllGrammar.class,
    TestAllKnowledge.class,
    TestAllParser.class,
    TestAllSemantics.class,
    TestAllSyntax.class,
    CoreControllerTest.class
})
public class TestAllCore {
}
