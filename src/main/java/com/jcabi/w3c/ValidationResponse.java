/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
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
 * <p>Implementation may be mutable but thread-safe.
 *
 * @see ValidatorBuilder
 * @see Validator#validate(String)
 * @see <a href="http://validator.w3.org/docs/api.html">W3C API, HTML</a>
 * @see <a href="http://jigsaw.w3.org/css-validator/api.html">W3C API, CSS</a>
 * @since 0.1
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
