/**
 * Copyright (c) 2011-2014, jcabi.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jcabi.w3c;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * Response of HTML or CSS validation.
 *
 * <p>See {@link ValidatorBuilder} for explanation of how to get an instance
 * of this interface.
 *
 * <p>Implementation must be immutable and thread-safe.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @see ValidatorBuilder
 * @see Validator#validate(String)
 * @see <a href="http://validator.w3.org/docs/api.html">W3C API, HTML</a>
 * @see <a href="http://jigsaw.w3.org/css-validator/api.html">W3C API, CSS</a>
 */
public interface ValidationResponse {

    /**
     * The document is valid and has no errors or warnings?
     * @return Is it valid?
     */
    boolean valid();

    /**
     * Who checked the document (normally contains a URL of W3C server).
     * @return URI of the server
     */
    URI checkedBy();

    /**
     * DOCTYPE of the document, if detected by the validator (may be empty
     * if {@code DOCTYPE} is not detected or if it's a CSS document).
     * @return Doctype or empty string
     */
    String doctype();

    /**
     * Charset of the document, if detected by the server (may be empty
     * if charset is not detected or it's a CSS document).
     * @return Charset of the document, e.g. {@code "UTF-8"}
     */
    Charset charset();

    /**
     * Returns list of errors found during validation.
     * @return List of errors or an empty list if no errors found
     */
    Set<Defect> errors();

    /**
     * Returns lsit of warnings found during validation.
     * @return List of warnings
     */
    Set<Defect> warnings();

}
