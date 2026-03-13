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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link DefaultCssValidator}.
 * @since 0.1
 */
public final class DefaultCssValidatorTest {

    /**
     * DefaultCssValidator can validate CSS document.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void validatesCssDocument() throws Exception {
        final MkContainer container = new MkGrizzlyContainer().next(
            new MkAnswer.Simple(
                this.validResponse()
            )
        ).start();
        MatcherAssert.assertThat(
            "document should be valid",
            new DefaultCssValidator(container.home()).validate("* { }").valid(),
            Matchers.is(true)
        );
        container.stop();
    }

    /**
     * DefaultCssValidator can ignore the entire document.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void ignoresEntireDocument() throws Exception {
        final Validator validator = ValidatorBuilder.CSS;
        final ValidationResponse response = validator.validate(
            this.documentWithIgnore()
        );
        MatcherAssert.assertThat(response.toString(), response.valid(), Matchers.is(true));
    }

    /**
     * DefaultCssValidator throw IOException when W3C server error occurred.
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
                    new MkAnswer.Simple(
                        status,
                        "<env:Envelope />"
                    )
                ).start();
                new DefaultCssValidator(container.home()).validate("body { }");
            } catch (final IOException ex) {
                caught.add(status);
            } finally {
                container.stop();
            }
        }
        MatcherAssert.assertThat(
            "must be error-free",
            caught,
            Matchers.containsInAnyOrder(
                responses.toArray(new Integer[0])
            )
        );
    }

    /**
     * DefaultCssValidator will call server when JIGSAW IGNORE pattern is not
     * matched.
     * @throws Exception If fails
     */
    @Test
    public void callsServerWhenPatternNotMatched() throws Exception {
        final MkContainer container = new MkGrizzlyContainer().next(
            new MkAnswer.Simple(
                HttpURLConnection.HTTP_OK, this.validResponse()
            )
        );
        try {
            container.start();
            new DefaultCssValidator(container.home()).validate("html { }");
            MatcherAssert.assertThat("must be 1 match", container.queries(), Matchers.is(1));
        } finally {
            container.stop();
        }
    }

    /**
     * DefaultCssValidator will immediately reply when JIGSAW IGNORE pattern is
     * matched.
     * @throws Exception If fails
     */
    @Test
    public void replyWhenPatternMatched() throws Exception {
        final MkContainer container = new MkGrizzlyContainer();
        try {
            container.start();
            new DefaultCssValidator(container.home())
                .validate(this.documentWithIgnore());
            MatcherAssert.assertThat("should not respond", container.queries(), Matchers.is(0));
        } finally {
            container.stop();
        }
    }

    /**
     * Build a response with JIGSAW IGNORE.
     * @return Document with JIGSAW IGNORE.
     */
    private String documentWithIgnore() {
        return String.join(
            "",
            "/",
            "* hey */\n\n/",
            "* JIGSAW IGNORE: .. */\n\n* { abc: cde }\n"
        );
    }

    /**
     * Build a response with valid result from W3C.
     * @return Response from W3C.
     */
    private String validResponse() {
        return StringUtils.join(
            "<env:Envelope",
            " xmlns:env='http://www.w3.org/2003/05/soap-envelope'>",
            "<env:Body><m:cssvalidationresponse",
            " xmlns:m='http://www.w3.org/2005/07/css-validator'>",
            "<m:validity>true</m:validity>",
            "<m:checkedby>W3C</m:checkedby>",
            "</m:cssvalidationresponse></env:Body></env:Envelope>"
        );
    }
}
