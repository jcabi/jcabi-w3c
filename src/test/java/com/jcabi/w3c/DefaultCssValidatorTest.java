/**
 * Copyright (c) 2011-2014, jcabi.com
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
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
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
                StringUtils.join(
                    "<env:Envelope",
                    " xmlns:env='http://www.w3.org/2003/05/soap-envelope'>",
                    "<env:Body><m:cssvalidationresponse",
                    " xmlns:m='http://www.w3.org/2005/07/css-validator'>",
                    "<m:validity>true</m:validity>",
                    "<m:checkedby>W3C</m:checkedby>",
                    "</m:cssvalidationresponse></env:Body></env:Envelope>"
                )
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
            // @checkstyle RegexpSingleline (1 line)
            "/* hey */\n\n/* JIGSAW IGNORE: .. */\n\n* { abc: cde }\n"
        );
        MatcherAssert.assertThat(response.toString(), response.valid());
    }

}
