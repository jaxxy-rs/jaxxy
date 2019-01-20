package org.jaxxy.util.reflect;

import java.io.Serializable;
import java.util.Date;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SupportedTypesPredicateTest {

    @Test
    public void emptyShouldAccept() {
        final SupportedTypesPredicate predicate = SupportedTypesPredicate.builder().build();
        assertThat(predicate.test(String.class)).isTrue();
        assertThat(predicate.test(Object.class)).isTrue();
    }

    @Test
    public void singleWhitelistExactMatch() {
        final SupportedTypesPredicate predicate = SupportedTypesPredicate.builder()
                .whitelist(String.class)
                .build();

        assertThat(predicate.test(String.class)).isTrue();
    }

    @Test
    public void singleWhitelistSubtypeMatch() {
        final SupportedTypesPredicate predicate = SupportedTypesPredicate.builder()
                .whitelist(Serializable.class)
                .build();

        assertThat(predicate.test(String.class)).isTrue();
    }

    @Test
    public void singleBlacklistExactMatch() {
        final SupportedTypesPredicate predicate = SupportedTypesPredicate.builder()
                .blacklist(String.class)
                .build();

        assertThat(predicate.test(String.class)).isFalse();
    }

    @Test
    public void singleBlacklistSubtypeMatch() {
        final SupportedTypesPredicate predicate = SupportedTypesPredicate.builder()
                .blacklist(Serializable.class)
                .build();

        assertThat(predicate.test(String.class)).isFalse();
    }

    @Test
    public void whitelistedAndBlacklisted() {
        final SupportedTypesPredicate predicate = SupportedTypesPredicate.builder()
                .whitelist(Object.class)
                .blacklist(String.class)
                .build();

        assertThat(predicate.test(String.class)).isFalse();
    }

    @Test
    public void whitelistedAndNotBlacklisted() {
        final SupportedTypesPredicate predicate = SupportedTypesPredicate.builder()
                .whitelist(Object.class)
                .blacklist(Date.class)
                .build();

        assertThat(predicate.test(String.class)).isTrue();
    }

    @Test
    public void notWhitelisted() {
        final SupportedTypesPredicate predicate = SupportedTypesPredicate.builder()
                .whitelist(String.class)
                .build();

        assertThat(predicate.test(Date.class)).isFalse();
    }

}