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
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Builder of HTML and CSS validators.
 *
 * <p>This is your entry point to the module. Start with creating a new
 * validator:
 *
 * <pre> HtmlValidator validator = new ValidatorBuilder().html();</pre>
 *
 * <p>Now you can use it in order to validate your HTML document against
 * W3C rules:
 *
 * <pre> ValidationResponse response = validator.validate(
 *   "&lt;html&gt;&lt;body&gt;...&lt;/body&gt;&lt;/html&gt;"
 * );</pre>
 *
 * <p>The response contains all information provided by W3C server. You can
 * work with details from {@link ValidationResponse} or just output it to
 * console:
 *
 * <pre> System.out.println(response.toString());</pre>
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @see ValidationResponse
 * @see Validator
 * @see <a href="http://validator.w3.org/docs/api.html">W3C API</a>
 */
@ToString
@EqualsAndHashCode
public final class ValidatorBuilder {

    /**
     * Static instance of HTML validator.
     */
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    public static final Validator HTML = new DefaultHtmlValidator(
        URI.create("http://validator.w3.org/check")
    );

    /**
     * Static instance of CSS validator.
     */
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    public static final Validator CSS = new DefaultCssValidator(
        URI.create("http://jigsaw.w3.org/css-validator/validator")
    );

    /**
     * Build HTML validator.
     * @return The validator
     */
    public Validator html() {
        return ValidatorBuilder.HTML;
    }

    /**
     * Build CSS validator.
     * @return The validator
     */
    public Validator css() {
        return ValidatorBuilder.CSS;
    }

    /**
     * Build HTML validator, pointing to the given URI of W3C engine.
     * @param uri URI of validator
     * @return The validator
     */
    public Validator html(final URI uri) {
        return new DefaultHtmlValidator(uri);
    }

    /**
     * Build CSS validator, pointing to the given URI of W3C engine.
     * @param uri URI of validator
     * @return The validator
     */
    public Validator css(final URI uri) {
        return new DefaultCssValidator(uri);
    }

}
