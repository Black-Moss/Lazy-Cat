package com.blackmoss.lazycat.api.model;

import java.util.Objects;

public abstract class AbstractModelBuilder<B extends AbstractModelBuilder<B>> {
    private final ModelContext context;

    protected AbstractModelBuilder(ModelContext context) {
        this.context = Objects.requireNonNull(context, "context");
    }

    protected abstract B self();

    protected final ModelContext context() {
        return this.context;
    }
}
