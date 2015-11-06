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
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test case for {@link DefaultHtmlValidator}.
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
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
                StringUtils.join(
                    "<env:Envelope",
                    " xmlns:env='http://www.w3.org/2003/05/soap-envelope'>",
                    "<env:Body><m:markupvalidationresponse",
                    " xmlns:m='http://www.w3.org/2005/10/markup-validator'>",
                    "<m:validity>true</m:validity>",
                    "<m:checkedby>W3C</m:checkedby>",
                    "<m:doctype>text/html</m:doctype>",
                    "<m:charset>UTF-8</m:charset>",
                    "</m:markupvalidationresponse></env:Body></env:Envelope>"
                )
            )
        ).start();
        final Validator validator = new DefaultHtmlValidator(container.home());
        final ValidationResponse response = validator.validate("<html/>");
        container.stop();
        MatcherAssert.assertThat(response.toString(), response.valid());
    }

    /**
     * DefaultHtmlValidator throw IOException when W3C server is unavailable.
     * @throws Exception If something goes wrong inside
     * @todo #10:30min DefaultHtmlValidator have to be updated to throw only
     *  IOException when W3C validation server is unavailable. Any other
     *  exception type can be confusing for users. Remove @Ignore annotation
     *  after finishing implementation.
     */
    @Ignore
    @Test(expected = IOException.class)
    public void throwsIOExceptionWhenValidationServerIsUnavailable()
        throws Exception {
        final MkContainer container = new MkGrizzlyContainer().next(
            new MkAnswer.Simple(HttpURLConnection.HTTP_UNAVAILABLE)
        ).start();
        new DefaultHtmlValidator(container.home()).validate("<html></html>");
        container.stop();
    }

}
