/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.w3c;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link Defect}.
 * @since 0.1
 */
final class DefectTest {

    /**
     * Defect can be instantiated and transformed to string.
     * @throws Exception If something goes wrong inside
     */
    @Test
    void transformsItselfToString() throws Exception {
        MatcherAssert.assertThat(
            "defect should be transformed to string",
            new Defect(
                1,
                1,
                " some source ",
                " some explanation ",
                " some message ID ",
                " some message "
            ),
            Matchers.hasToString(
                "[1:1] \"some source\", \"some explanation\", \"some message ID\", \"some message\""
            )
        );
    }
}
