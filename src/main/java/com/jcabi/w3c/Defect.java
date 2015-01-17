/**
 * Copyright (c) 2011-2015, jcabi.com
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

import com.jcabi.log.Logger;
import lombok.EqualsAndHashCode;

/**
 * Validation defect (error or warning) produced by {@link ValidationResponse}.
 *
 * <p>Objects of this class are immutable and thread-safe.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @see <a href="http://validator.w3.org/docs/api.html">W3C API</a>
 * @checkstyle LineLength (2 lines)
 */
@EqualsAndHashCode(of = { "iline", "icolumn", "isource", "iexplanation", "msg", "imessage" })
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
     * @checkstyle ParameterNumber (5 lines)
     */
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
