/*
  The MIT License (MIT)

  Copyright (c) 2017 Antonella Botte, Giacomo Marciani and Debora Partigianoni

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

import com.acmutv.ontoqa.core.semantics.sltag.ElementarySltag;

import java.util.Set;

/**
 * A grammar is a collection of elementary SLTAGs.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public interface Grammar {

  /**
   * Returns the set of all elementary SLTAGs.
   * @return the set of all elementary SLTAGs.
   */
  Set<ElementarySltag> getAllElementarySLTAG();

  /**
   * Adds {@code sltag} to the grammar, as a new elementary Sltag for {@code word}.
   * @param sltag the Sltag to add.
   * @return true if {@code sltag} has been added; false otherwise.
   */
  boolean addElementarySLTAG(ElementarySltag sltag);

  /**
   * Returns the set of elementary Sltag for {@code word}.
   * @param word the word.
   * @return the set of elementary Sltag for {@code word}.
   */
  Set<ElementarySltag> getAllElementarySLTAG(String word);
}
