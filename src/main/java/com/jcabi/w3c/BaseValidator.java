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
import com.jcabi.manifests.Manifests;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import lombok.ToString;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.CharEncoding;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

/**
 * Abstract implementation of (X)HTML validator.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 */
@ToString
class BaseValidator {

    /**
     * User agent.
     */
    protected static final String USER_AGENT = String.format(
        "ReXSL-W3C %s %s %s",
        Manifests.read("JCabi-Version"),
        Manifests.read("JCabi-Build"),
        Manifests.read("JCabi-Date")
    );

    /**
     * Boundary for HTTP POST form data (just some random data).
     */
    protected static final String BOUNDARY = "vV9olNqRj00PC4OIlM7";

    /**
     * Convert HTML to HTTP FORM entity.
     * @param name Name of HTTP form field
     * @param content The content of it
     * @param type Media type of it
     * @return The HTTP post body
     * @throws IOException if fails
     */
    protected final String entity(final String name, final String content,
        final String type) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MultipartEntityBuilder.create()
            .setStrictMode()
            .setCharset(Charsets.UTF_8)
            .setBoundary(BaseValidator.BOUNDARY)
            .addBinaryBody(
                name,
                content.getBytes(Charsets.UTF_8),
                ContentType.create(type, Charsets.UTF_8),
                "file"
            )
            .addTextBody("output", "soap12")
            .build()
            .writeTo(baos);
        return baos.toString(CharEncoding.UTF_8);
    }

    /**
     * Build response from error that just happened.
     * @param error The exception
     * @return The validation response just built
     */
    protected final ValidationResponse failure(final Throwable error) {
        final DefaultValidationResponse resp = new DefaultValidationResponse(
            false,
            URI.create("http://localhost/failure"),
            "unknown-doctype",
            Charset.defaultCharset()
        );
        String message = error.getMessage();
        if (message == null) {
            message = "";
        }
        resp.addError(
            new Defect(
                0,
                0,
                "",
                Logger.format("%[exception]s", error),
                "",
                message
            )
        );
        return resp;
    }

    /**
     * Build a success response.
     * @param type Media type of resource just processed
     * @return The validation response just built
     */
    protected final ValidationResponse success(final String type) {
        final DefaultValidationResponse resp = new DefaultValidationResponse(
            true,
            URI.create("http://localhost/success"),
            type,
            Charset.defaultCharset()
        );
        return resp;
    }

    /**
     * Get text from list of strings, returned by
     * {@link XML#xpath(String)}.
     *
     * <p>This method is required to simplify manipulations with XPath returned
     * list of strings (returned by {@link XML#xpath(String)} above).
     * The list of strings normally (!) contains one element or no elements. If
     * there are no elements it means that the XPath is not found in the
     * document. In this case we should return an empty string. If any elements
     * are found - we're interested only in the first one. All others are
     * ignored, because simply should not exist (if our XPath query is correct).
     *
     * @param lines The lines to work with
     * @return The value
     * @see #intOf(List)
     */
    protected static String textOf(final List<String> lines) {
        final String text;
        if (lines.isEmpty()) {
            text = "";
        } else {
            text = lines.get(0);
        }
        return text;
    }

    /**
     * Get text from list of strings.
     *
     * <p>See explanation of {@link #textOf(List)}.
     *
     * @param lines The lines to work with
     * @return The value
     * @see #textOf(List)
     */
    protected static int intOf(final List<String> lines) {
        final int value;
        if (lines.isEmpty()) {
            value = 0;
        } else {
            value = Integer.parseInt(lines.get(0));
        }
        return value;
    }

    /**
     * Convert text to charset.
     * @param text Text representation of charset
     * @return The charset
     */
    protected static Charset charset(final String text) {
        final Charset charset;
        if (text.isEmpty()) {
            charset = Charset.defaultCharset();
        } else {
            charset = Charset.forName(text);
        }
        return charset;
    }

}
