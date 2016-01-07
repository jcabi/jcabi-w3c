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

import com.jcabi.aspects.Immutable;
import com.jcabi.http.Request;
import com.jcabi.http.response.XmlResponse;
import java.io.IOException;
import java.net.URI;
import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Default implementation of CSS validator.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @see <a href="http://jigsaw.w3.org/css-validator/api.html">W3C API</a>
 */
@Immutable
@ToString
@EqualsAndHashCode(callSuper = false, of = "uri")
final class DefaultCssValidator extends BaseValidator implements Validator {

    /**
     * The URI to use in W3C.
     */
    private final transient String uri;

    /**
     * Public ctor.
     * @param entry Entry point to use
     */
    DefaultCssValidator(final URI entry) {
        super();
        this.uri = entry.toString();
    }

    @Override
    public ValidationResponse validate(final String css)
        throws IOException {
        final ValidationResponse response;
        final Pattern pattern = Pattern.compile(
            ".*^/\\* JIGSAW IGNORE: [^\\n]+\\*/$.*",
            Pattern.MULTILINE | Pattern.DOTALL
            );
        try {
            if (pattern.matcher(css).matches())
                return this.success("");
            return this.processed(css);
        } catch (IOException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Return a response after real processing of the CSS.
     * @param css The CSS stylesheet to check
     * @return The response
     * @throws IOException if fails
     */
    private ValidationResponse processed(final String css) throws IOException {
        final Request req = this.request(
            this.uri,
            this.entity("file", DefaultCssValidator.filter(css), "text/css")
            );
        return this.build(
            req.fetch().as(XmlResponse.class)
                .registerNs("env", "http://www.w3.org/2003/05/soap-envelope")
                .registerNs("m", "http://www.w3.org/2005/07/css-validator")
                .assertXPath("//m:validity")
                .assertXPath("//m:checkedby")
                .xml()
            );
    }

    /**
     * Exclude problematic lines from CSS.
     * @param css The css document
     * @return New document, with lines excluded
     */
    private static String filter(final String css) {
        return Pattern.compile(
            "^/\\* JIGSAW: [^\\n]+\\*/$",
            Pattern.MULTILINE | Pattern.DOTALL
            ).matcher(css).replaceAll("");
    }

}
