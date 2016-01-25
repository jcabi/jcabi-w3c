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
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
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
        final Validator validator = new DefaultCssValidator(container.home());
        final ValidationResponse response = validator.validate("* { }");
        container.stop();
        MatcherAssert.assertThat(response.toString(), response.valid());
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
        MatcherAssert.assertThat(response.toString(), response.valid());
    }

    /**
     * DefaultCssValidator throw IOException when W3C server error occurred.
     * @throws Exception If something goes wrong inside
     */
    @Test
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public void throwsIOExceptionWhenValidationServerErrorOccurred()
        throws Exception {
        final Set<Integer> responses = new HashSet<Integer>(
            Arrays.asList(
                HttpURLConnection.HTTP_INTERNAL_ERROR,
                HttpURLConnection.HTTP_NOT_IMPLEMENTED,
                HttpURLConnection.HTTP_BAD_GATEWAY,
                HttpURLConnection.HTTP_UNAVAILABLE,
                HttpURLConnection.HTTP_GATEWAY_TIMEOUT,
                HttpURLConnection.HTTP_VERSION
            )
        );
        final Set<Integer> caught = new HashSet<Integer>();
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
            caught,
            Matchers.containsInAnyOrder(
                responses.toArray(new Integer[responses.size()])
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
            MatcherAssert.assertThat(container.queries(), Matchers.is(1));
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
            MatcherAssert.assertThat(container.queries(), Matchers.is(0));
        } finally {
            container.stop();
        }
    }

    /**
     * Build a response with JIGSAW IGNORE.
     * @return Document with JIGSAW IGNORE.
     */
    private String documentWithIgnore() {
        // @checkstyle RegexpSingleline (1 line)
        return "/* hey */\n\n/* JIGSAW IGNORE: .. */\n\n* { abc: cde }\n";
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
