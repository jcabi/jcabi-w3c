/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.w3c;

import com.jcabi.aspects.RetryOnFailure;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Integration case for {@link DefaultHtmlValidator}.
 *
 * @since 0.8
 */
public final class DefaultHtmlValidatorITCase {

    /**
     * DefaultHtmlValidator can validate HTML document.
     *
     * @throws Exception If something goes wrong inside
     */
    @Test
    @RetryOnFailure(verbose = false)
    public void validatesHtmlDocument() throws Exception {
        MatcherAssert.assertThat(
            "html document should be error-free",
            ValidatorBuilder.HTML.validate(
                StringUtils.join(
                    "<!DOCTYPE html>",
                    "<html><head><meta charset='UTF-8'>",
                    "<title>hey</title></head>",
                    "<body></body></html>"
                )
            ).errors(),
            Matchers.empty()
        );
    }

    /**
     * DefaultHtmlValidator can validate invalid HTML document.
     *
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void validatesInvalidHtmlDocument() throws Exception {
        MatcherAssert.assertThat(
            "html document should be with errors",
            ValidatorBuilder.HTML.validate(
                "this is an invalid html"
            ).errors(),
            Matchers.not(Matchers.empty())
        );
    }
}
