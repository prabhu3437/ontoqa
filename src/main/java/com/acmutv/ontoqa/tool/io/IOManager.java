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

package com.acmutv.ontoqa.tool.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class realizes I/O services.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class IOManager {

  /**
   * Returns an {@link InputStream} from a general resource.
   * @param resource the resource locator (e.g.: path for local file, http url for remote file).
   * @return the open {@link InputStream}.
   * @throws IOException when the {@link InputStream} cannot be opened.
   */
  public static InputStream getInputStream(final String resource) throws IOException {
    if (resource.startsWith("http://") || resource.startsWith("https://")) {
      final URL url = new URL(resource);
      return url.openStream();
    } else {
      final Path path = FileSystems.getDefault().getPath(resource).toAbsolutePath();
      return Files.newInputStream(path);
    }
  }

  /**
   * Returns an {@link OutputStream} from a general resource.
   * @param resource the resource locator (e.g.: path for local file, http url for remote file).
   * @return the open {@link OutputStream}.
   * @throws IOException when the {@link OutputStream} cannot be opened.
   */
  public static OutputStream getOutputStream(final String resource) throws IOException {
    final Path path = FileSystems.getDefault().getPath(resource).toAbsolutePath();
    return Files.newOutputStream(path);
  }
}