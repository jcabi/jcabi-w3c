/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.w3c;

import com.jcabi.manifests.Manifests;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.ToString;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

/**
 * Abstract implementation of (X)HTML validator.
 * @since 0.1
 */
@ToString
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
abstract class AbstractBaseValidator {

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
    protected static String entity(final String name, final String content,
        final String type) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MultipartEntityBuilder.create().setStrictMode()
            .setCharset(StandardCharsets.UTF_8)
            .setBoundary(AbstractBaseValidator.BOUNDARY).addBinaryBody(
                name,
                content.getBytes(StandardCharsets.UTF_8),
                ContentType.create(type, StandardCharsets.UTF_8),
                "file"
            )
            .addTextBody("output", "soap12")
            .build()
            .writeTo(baos);
        return baos.toString(StandardCharsets.UTF_8);
    }

    /**
     * Build a success response.
     * @param type Media type of resource just processed
     * @return The validation response just built
     */
    protected static ValidationResponse success(final String type) {
        return new DefaultValidationResponse(
            true,
            URI.create("http://localhost/success"),
            type,
            Charset.defaultCharset()
        );
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
