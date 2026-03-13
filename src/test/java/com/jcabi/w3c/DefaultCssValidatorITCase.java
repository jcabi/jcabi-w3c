/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.w3c;

import com.jcabi.aspects.RetryOnFailure;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Integration case for {@link DefaultCssValidator}.
 * @since 0.8
 */
public final class DefaultCssValidatorITCase {

    /**
     * DefaultCssValidator can validate CSS document.
     * @throws Exception If something goes wrong inside
     */
    @Test
    @RetryOnFailure(verbose = false)
    public void validatesCssDocument() throws Exception {
        MatcherAssert.assertThat(
            "css document should be valid",
            ValidatorBuilder.CSS.validate("* { }").errors(),
            Matchers.empty()
        );
    }

}
