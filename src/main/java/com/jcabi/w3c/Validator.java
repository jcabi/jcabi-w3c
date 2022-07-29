/*
 * Copyright (c) 2011-2022, jcabi.com
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

import java.io.IOException;

/**
 * Validator of CSS pages through W3C validation API.
 *
 * <p>Retrieve it from {@link ValidatorBuilder} and use like this:
 *
 * <pre> HtmlValidator validator = new ValidatorBuilder().css();
 * ValidationResponse response = validator.validate(text);
 * if (!response.valid()) {
 *   // report about a problem
 * }</pre>
 *
 * <p>Sometimes this Jigsaw validator makes mistakes, sometimes you just need to
 * use some IE-specific feature, which is not CSS compliant. In this case you
 * may add comments to the document, at the end of every line you want to ignore
 * during validation, for example:
 *
 * <pre> div.test {
 *   color: red;
 *   #position: 50%; &#47;* JIGSAW: this is a IE_-related hack *&#47;
 * }</pre>
 *
 * <p>You can also instruct the validator to ingore the entire file, by means
 * of adding {@code JIGSAW IGNORE} comment anywhere in the text, for example:
 *
 * <pre> &#47;* JIGSAW IGNORE: it's an experimental file *&#47;
 * div.test {
 *   something-totally-incorrect:...
 * }</pre>
 *
 * <p>Validation will happen anyway, but {@link ValidationResponse#valid()}
 * will return {@code TRUE}. You will still be able to read errors and warnings
 * from it, but it will be valid.
 *
 * <p>Objects of this interface should be immutable and thread-safe.
 *
 * @see <a href="http://jigsaw.w3.org/css-validator/api.html">W3C API, CSS</a>
 * @see ValidatorBuilder
 * @since 0.1
 */
public interface Validator {

    /**
     * Validate and return validation response.
     *
     * <p>The method should never throw runtime exceptions, no matter what
     * happened with the HTTP connection to the W3C server. It will return
     * an invalid response, but will never throw.
     *
     * <p>This method expects content of a document which should be
     * validated, not URL to the document
     *
     * @param content The HTML/CSS content to validate
     * @return The response
     * @throws IOException If fails
     */
    ValidationResponse validate(String content) throws IOException;

}
