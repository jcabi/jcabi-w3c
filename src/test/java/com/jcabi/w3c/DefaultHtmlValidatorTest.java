/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.w3c;

import com.jcabi.http.mock.MkAnswer;
import com.jcabi.http.mock.MkContainer;
import com.jcabi.http.mock.MkGrizzlyContainer;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link DefaultHtmlValidator}.
 * @since 0.1
 */
public final class DefaultHtmlValidatorTest {

    /**
     * DefaultHtmlValidator can validate HTML document.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void validatesHtmlDocument() throws Exception {
        final MkContainer container = new MkGrizzlyContainer().next(
            new MkAnswer.Simple(
                this.validReturn()
            )
        ).start();
        final Validator validator = new DefaultHtmlValidator(container.home());
        final ValidationResponse response = validator.validate("<html/>");
        container.stop();
        MatcherAssert.assertThat(
            response.toString(),
            response.valid(),
            Matchers.is(true)
        );
    }

    /**
     * Test if {@link DefaultHtmlValidator} validates invalid html.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void validateInvalidHtml() throws Exception {
        final MkContainer container = new MkGrizzlyContainer().next(
            new MkAnswer.Simple(
                this.invalidHtmlResponse()
            )
        ).start();
        final ValidationResponse response = new DefaultHtmlValidator(
            container.home()
        ).validate("this is an invalid html");
        container.stop();
        MatcherAssert.assertThat(
            "Validity must be invalid",
            !response.valid()
                && !response.errors().isEmpty()
                && !response.warnings().isEmpty(),
            Matchers.is(true)
        );
    }

    /**
     * DefaultHtmlValidator throw IOException when W3C server error occurred.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void throwsIoExceptionWhenValidationServerErrorOccurred()
        throws Exception {
        final Set<Integer> responses = new HashSet<>(
            Arrays.asList(
                HttpURLConnection.HTTP_INTERNAL_ERROR,
                HttpURLConnection.HTTP_NOT_IMPLEMENTED,
                HttpURLConnection.HTTP_BAD_GATEWAY,
                HttpURLConnection.HTTP_UNAVAILABLE,
                HttpURLConnection.HTTP_GATEWAY_TIMEOUT,
                HttpURLConnection.HTTP_VERSION
            )
        );
        final Set<Integer> caught = new HashSet<>();
        for (final Integer status : responses) {
            MkContainer container = null;
            try {
                container = new MkGrizzlyContainer().next(
                    new MkAnswer.Simple(status)
                ).start();
                new DefaultHtmlValidator(
                    container.home()
                ).validate(
                    "<html></html>"
                );
            } catch (final IOException ex) {
                caught.add(status);
            } finally {
                container.stop();
            }
        }
        MatcherAssert.assertThat(
            "must be error-free",
            caught,
            Matchers.containsInAnyOrder(responses.toArray(new Integer[0]))
        );
    }

    /**
     * Build a response with valid result from W3C.
     * @return Response from W3C
     */
    private String validReturn() {
        return StringUtils.join(
            "<?xml version='1.0' encoding='utf-8' standalone='no'?>",
            "<messages xmlns='http://n.validator.nu/messages/'>",
            "<source encoding='UTF-8' type='text/html'>&lt;html/&gt;</source>",
            "</messages>"
        );
    }

    /**
     * Use a file to build the request.
     * @return Request inside the file
     * @throws IOException if something goes wrong
     */
    private String invalidHtmlResponse() throws IOException {
        return IOUtils.toString(
            DefaultHtmlValidator.class.getResourceAsStream(
                "invalid-html-response.xml"
            ),
            StandardCharsets.UTF_8
        );
    }
}
