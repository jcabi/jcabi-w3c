/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
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
 * @see ValidationResponse
 * @see Validator
 * @see <a href="http://validator.w3.org/docs/api.html">W3C API</a>
 * @since 0.1
 */
// @checkstyle NonStaticMethodCheck (500 lines)
@ToString
@EqualsAndHashCode
public final class ValidatorBuilder {

    /**
     * Static instance of HTML validator.
     */
    public static final Validator HTML = new DefaultHtmlValidator(
        URI.create("https://validator.w3.org/nu/?out=xml&showsource=yes")
    );

    /**
     * Static instance of CSS validator.
     */
    public static final Validator CSS = new DefaultCssValidator(
        URI.create("https://jigsaw.w3.org/css-validator/validator")
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
