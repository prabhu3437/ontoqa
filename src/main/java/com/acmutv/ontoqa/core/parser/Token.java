/*
  The MIT License (MIT)

  Copyright (c) 2017 Giacomo Marciani

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
package com.acmutv.ontoqa.core.parser;

import com.acmutv.ontoqa.core.semantics.sltag.ElementarySltag;
import com.acmutv.ontoqa.core.semantics.sltag.Sltag;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * An SLTAG parser token.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @since 1.0
 */
@Data
public class Token {

  /**
   * The lexical pattern.
   */
  @NonNull
  private String lexicalPattern;

  /**
   * The SLTAG candidates for the lexical pattern.
   */
  @NonNull
  private List<ElementarySltag> candidates;

  /**
   * The index of the previous matched lexical entry.
   */
  private Integer prev;

  /**
   * Creates a new token.
   * @param lexicalPattern the lexical pattern.
   * @param candidates the SLTAG candidates for the given lexical pattern.
   * @param prev the index of the previous lexical entry.
   */
  public Token(String lexicalPattern, List<ElementarySltag> candidates, Integer prev) {
    this.lexicalPattern = lexicalPattern;
    this.candidates = candidates;
    this.prev = prev;
  }
}