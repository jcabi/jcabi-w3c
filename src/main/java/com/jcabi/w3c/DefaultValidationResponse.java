/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.w3c;

import com.jcabi.log.Logger;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.EqualsAndHashCode;

/**
 * Default implementaiton of validation response.
 *
 * @since 0.1
 */
@EqualsAndHashCode(
    of = { "ivalid", "validator", "type", "encoding", "ierrors", "iwarnings" }
)
final class DefaultValidationResponse implements ValidationResponse {

    /**
     * Is it valid?
     */
    private final transient boolean ivalid;

    /**
     * Who validated it?
     */
    private final transient URI validator;

    /**
     * DOCTYPE of the document.
     */
    private final transient String type;

    /**
     * The encoding.
     */
    private final transient Charset encoding;

    /**
     * Set of errors found.
     */
    private final transient Set<Defect> ierrors;

    /**
     * Set of warnings found.
     */
    private final transient Set<Defect> iwarnings;

    /**
     * Public ctor.
     * @param val The document is valid?
     * @param server Who validated it?
     * @param tpe DOCTYPE of the document
     * @param enc Charset of the document
     * @checkstyle ParameterNumber (3 lines)
     */
    DefaultValidationResponse(final boolean val,
        final URI server, final String tpe,
        final Charset enc) {
        this.ivalid = val;
        this.validator = server;
        this.type = tpe;
        this.encoding = enc;
        this.ierrors = new CopyOnWriteArraySet<>();
        this.iwarnings = new CopyOnWriteArraySet<>();
    }

    @Override
    public String toString() {
        return new StringBuilder(0)
            .append(Logger.format("Validity: %B\n", this.ivalid))
            .append(Logger.format("Validator: \"%s\"\n", this.validator))
            .append(Logger.format("DOCTYPE: \"%s\"\n", this.type))
            .append(Logger.format("Charset: \"%s\"\n", this.encoding))
            .append("Errors:\n")
            .append(DefaultValidationResponse.asText(this.ierrors))
            .append("Warnings:\n")
            .append(DefaultValidationResponse.asText(this.iwarnings))
            .toString();
    }

    @Override
    public boolean valid() {
        return this.ivalid;
    }

    @Override
    public URI checkedBy() {
        return this.validator;
    }

    @Override
    public String doctype() {
        return this.type;
    }

    @Override
    public Charset charset() {
        return this.encoding;
    }

    @Override
    public Set<Defect> errors() {
        return Collections.unmodifiableSet(this.ierrors);
    }

    @Override
    public Set<Defect> warnings() {
        return Collections.unmodifiableSet(this.iwarnings);
    }

    /**
     * Add error.
     * @param error The error to add
     */
    public void addError(final Defect error) {
        this.ierrors.add(error);
    }

    /**
     * Add warning.
     * @param warning The warning to add
     */
    public void addWarning(final Defect warning) {
        this.iwarnings.add(warning);
    }

    /**
     * Convert list of defects into string.
     * @param defects Set of them
     * @return The text
     */
    private static String asText(final Set<Defect> defects) {
        final StringBuilder text = new StringBuilder(100);
        for (final Defect defect : defects) {
            text.append("  ").append(defect.toString()).append('\n');
        }
        return text.toString();
    }

}
