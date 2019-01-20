package org.jaxxy.util.reflect;

import java.util.List;
import java.util.function.Predicate;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Builder
@RequiredArgsConstructor
public class SupportedTypesPredicate implements Predicate<Class<?>> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Singular
    private final List<Class<?>> whitelists;

    @Singular
    private final List<Class<?>> blacklists;

//----------------------------------------------------------------------------------------------------------------------
// Predicate Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean test(Class<?> type) {
        return isWhitelisted(type) && isNotBlacklisted(type);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private boolean isNotBlacklisted(Class<?> type) {
        return blacklists.isEmpty() || blacklists.stream().noneMatch(isAssignable(type));
    }

    private Predicate<Class<?>> isAssignable(Class<?> type) {
        return t -> t.isAssignableFrom(type);
    }

    private boolean isWhitelisted(Class<?> type) {
        return whitelists.isEmpty() || whitelists.stream().anyMatch(isAssignable(type));
    }
}
