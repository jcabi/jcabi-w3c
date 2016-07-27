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
import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.XmlResponse;
import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.CharEncoding;

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
        final Request req = this.request(html);
        final Response response = req.fetch();
        if (response.status() != HttpURLConnection.HTTP_OK) {
            throw new IOException(
                response.reason()
            );
        }
        return this.build(
            response.as(XmlResponse.class)
                .registerNs("nu", "http://n.validator.nu/messages/")
                .assertXPath("//nu:messages")
                .assertXPath("//nu:source")
                .xml()
        );
    }

    /**
     * Send request and return response.
     * @param entity The entity to POST
     * @return The response
     */
    private Request request(final String entity) {
        return new JdkRequest(this.uri)
            .method(Request.POST)
            .body().set(entity).back()
            .header(HttpHeaders.USER_AGENT, BaseValidator.USER_AGENT)
            .header(HttpHeaders.ACCEPT, MediaType.TEXT_HTML)
            .header(
                HttpHeaders.CONTENT_TYPE,
                Logger.format(
                    "%s; charset=%s",
                    MediaType.TEXT_HTML,
                    CharEncoding.UTF_8
                )
            );
    }

    /**
     * Build response from XML.
     * @param xml The response
     * @return The validation response just built
     */
    private ValidationResponse build(final XML xml) {
        final List<XML> errors = xml.nodes("//nu:error");
        final List<XML> warnings = xml.nodes("//nu:info");
        final DefaultValidationResponse resp = new DefaultValidationResponse(
            errors.isEmpty() && warnings.isEmpty(),
            URI.create(this.uri),
            BaseValidator.textOf(xml.xpath("//nu:source/@type")),
            BaseValidator.charset(
                BaseValidator.textOf(xml.xpath("//nu:source/@encoding"))
            )
        );
        for (final XML node : errors) {
            resp.addError(this.defect(node));
        }
        for (final XML node : warnings) {
            resp.addWarning(this.defect(node));
        }
        return resp;
    }

    /**
     * Convert XML node to defect.
     * @param node The node
     * @return The defect
     */
    private Defect defect(final XML node) {
        return new Defect(
            BaseValidator.intOf(node.xpath("nu:error/@last-line")),
            BaseValidator.intOf(node.xpath("nu:error/@last-column")),
            BaseValidator.textOf(node.xpath("nu:extract/text()")),
            BaseValidator.textOf(node.xpath("nu:elaboration/text()")),
            "",
            BaseValidator.textOf(node.xpath("nu:message/text()"))
        );
    }
}
