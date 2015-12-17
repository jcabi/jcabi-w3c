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
import com.jcabi.http.Response;
import com.jcabi.http.response.XmlResponse;
import java.io.IOException;
import java.net.URI;
import javax.ws.rs.core.MediaType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Implementation of (X)HTML validator.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @see <a href="http://validator.w3.org/docs/api.html">W3C API</a>
 */
@Immutable
@ToString
@EqualsAndHashCode(callSuper = false, of = "uri")
final class DefaultHtmlValidator extends BaseValidator implements Validator {

    /**
     * The HTTP_OK code
     */
    private static final int HTTP_OK = 200;

    /**
     * The URI to use in W3C.
     */
    private final transient String uri;

    /**
     * Public ctor.
     * @param entry Entry point to use
     */
    DefaultHtmlValidator(final URI entry) {
        super();
        this.uri = entry.toString();
    }

    @Override
    public ValidationResponse validate(final String html)
        throws IOException {
        final Request req = this.request(
            this.uri,
            this.entity("uploaded_file", html, MediaType.TEXT_HTML)
        );
        final Response response = req.fetch();
        if (response.status() != HTTP_OK) {
            throw new IOException(
                response.reason()
            );
        }
        return this.build(
            response.as(XmlResponse.class)
                .registerNs("env", "http://www.w3.org/2003/05/soap-envelope")
                .registerNs("m", "http://www.w3.org/2005/10/markup-validator")
                .assertXPath("//m:validity")
                .assertXPath("//m:checkedby")
                .assertXPath("//m:doctype")
                .assertXPath("//m:charset")
                .xml()
        );
    }

}
