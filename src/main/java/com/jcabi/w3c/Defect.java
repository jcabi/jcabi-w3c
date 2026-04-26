/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.w3c;

import com.jcabi.log.Logger;
import lombok.EqualsAndHashCode;

/**
 * Validation defect (error or warning) produced by {@link ValidationResponse}.
 *
 * <p>Objects of this class are immutable and thread-safe.
 *
 * @see <a href="http://validator.w3.org/docs/api.html">W3C API</a>
 * @since 0.1
 */
@EqualsAndHashCode(
    of = { "iline", "icolumn", "isource", "iexplanation", "msg", "imessage" }
)
public final class Defect {

    /**
     * Line.
     */
    private final transient int iline;

    /**
     * Column.
     */
    private final transient int icolumn;

    /**
     * Source line.
     */
    private final transient String isource;

    /**
     * Explanation.
     */
    private final transient String iexplanation;

    /**
     * Message id.
     */
    private final transient String msg;

    /**
     * The message.
     */
    private final transient String imessage;

    /**
     * Protected ctor, to be called only from this package.
     * @param line Line number
     * @param column Column number
     * @param source Source line
     * @param explanation The explanation
     * @param mid ID of the message
     * @param message Message text
     */
    // @checkstyle ParameterNumberCheck (5 lines)
    // @checkstyle ConstructorsCodeFreeCheck (10 lines)
    Defect(final int line, final int column, final String source,
        final String explanation, final String mid,
        final String message) {
        this.iline = line;
        this.icolumn = column;
        this.isource = source.trim();
        this.iexplanation = explanation.trim();
        this.msg = mid.trim();
        this.imessage = message.trim();
    }

    @Override
    public String toString() {
        return Logger.format(
            "[%d:%d] \"%s\", \"%s\", \"%s\", \"%s\"",
            this.iline,
            this.icolumn,
            this.isource,
            this.iexplanation,
            this.msg,
            this.imessage
        );
    }

    /**
     * Line number, where the defect was found.
     * @return Line number
     */
    public int line() {
        return this.iline;
    }

    /**
     * Column number inside the line.
     * @return Column number
     */
    public int column() {
        return this.icolumn;
    }

    /**
     * Source line, as quoted by W3C validator.
     * @return Full text of the source line
     */
    public String source() {
        return this.isource;
    }

    /**
     * Explanation of the problem.
     * @return Text
     */
    public String explanation() {
        return this.iexplanation;
    }

    /**
     * Message ID, according to W3C API.
     * @return The ID
     */
    public String messageId() {
        return this.msg;
    }

    /**
     * Text of the message.
     * @return The message returned by W3C server
     */
    public String message() {
        return this.imessage;
    }
}
