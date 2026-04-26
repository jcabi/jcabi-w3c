/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
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
@FunctionalInterface
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
