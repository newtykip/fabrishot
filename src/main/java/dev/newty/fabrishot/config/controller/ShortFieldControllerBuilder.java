package dev.newty.fabrishot.config.controller;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.NumberFieldControllerBuilder;

public interface ShortFieldControllerBuilder extends NumberFieldControllerBuilder<Short, ShortFieldControllerBuilder> {
    static ShortFieldControllerBuilder create(Option<Short> option) {
        return new ShortFieldControllerBuilderImpl(option);
    }
}


