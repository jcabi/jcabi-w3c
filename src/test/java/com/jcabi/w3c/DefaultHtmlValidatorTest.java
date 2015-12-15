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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test case for {@link DefaultHtmlValidator}.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 */
public final class DefaultHtmlValidatorTest {

    /**
     * DefaultHtmlValidator can validate HTML document.
     *
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void validatesHtmlDocument() throws Exception {
        final MkContainer container = new MkGrizzlyContainer().next(
                        new MkAnswer.Simple(this.validReturn())
        ).start();
        final Validator validator = new DefaultHtmlValidator(container.home());
        final ValidationResponse response = validator.validate("<html/>");
        container.stop();
        MatcherAssert.assertThat(response.toString(), response.valid());
    }

    /**
     * Test if {@link DefaultHtmlValidator} validades invalid html.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void validateInvalidHtml() throws Exception {
        final MkContainer container = new MkGrizzlyContainer().next(
            new MkAnswer.Simple(
                this.invalidHtmlResponse()
            )
        ).start();
        final Validator validator = new DefaultHtmlValidator(container.home());
        final ValidationResponse response = validator
            .validate("this is an invalid html");
        container.stop();
        MatcherAssert.assertThat(
            "Validity must be invalid!",
            !response.valid()
        );
        MatcherAssert.assertThat(
            "Must has at least one error",
            response.errors(),
            this.withoutDefects()
        );
        MatcherAssert.assertThat(
            "Must has at least one warning",
            response.warnings(),
            this.withoutDefects()
        );
    }

    /**
     * DefaultHtmlValidator throw IOException when W3C server error occurred.
     *
     * @throws Exception If something goes wrong inside
     * @todo #10:30min DefaultHtmlValidator have to be updated to throw only
     *  IOException when W3C validation server is unavailable. Any other
     *  exception type can be confusing for users. Remove @Ignore
     *  annotation after finishing implementation.
     */
    @Ignore
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
                    new MkAnswer.Simple(status)
                ).start();
                new DefaultHtmlValidator(container.home())
                    .validate("<html></html>");
            } catch (final IOException ex) {
                caught.add(status);
            } finally {
                container.stop();
            }
        }
        final Integer[] data = responses.toArray(new Integer[responses.size()]);
        MatcherAssert.assertThat(caught, Matchers.containsInAnyOrder(data));
    }

    /**
     * Build a response with valid result from W3C.
     * @return Response from W3C.
     */
    private String validReturn() {
        return StringUtils.join(
            "<env:Envelope",
            " xmlns:env='http://www.w3.org/2003/05/soap-envelope'>",
            "<env:Body><m:markupvalidationresponse",
            " xmlns:m='http://www.w3.org/2005/10/markup-validator'>",
            "<m:validity>true</m:validity>",
            "<m:checkedby>W3C</m:checkedby>",
            "<m:doctype>text/html</m:doctype>",
            "<m:charset>UTF-8</m:charset>",
            "</m:markupvalidationresponse></env:Body></env:Envelope>"
        );
    }

    /**
     * Use a file to build the request.
     * @return Request inside the file.
     * @throws IOException if something goes wrong.
     */
    private String invalidHtmlResponse() throws IOException {
        final InputStream file = DefaultHtmlValidator.class
                        .getResourceAsStream("invalid-html-response.xml");
        final String xml = IOUtils.toString(file);
        IOUtils.closeQuietly(file);
        return xml;
    }

    /**
     * Matcher that checks if has no errors.
     * @return Matcher
     */
    private Matcher<Collection<Defect>> withoutDefects() {
        return Matchers.not(Matchers.emptyCollectionOf(Defect.class));
    }

}
