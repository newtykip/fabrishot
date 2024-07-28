package dev.newty.fabrishot.config.controller;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ValueFormatter;
import dev.isxander.yacl3.impl.controller.AbstractControllerBuilderImpl;

public class ShortFieldControllerBuilderImpl extends AbstractControllerBuilderImpl<Short> implements ShortFieldControllerBuilder {
    private short min = Short.MIN_VALUE;
    private short max = Short.MAX_VALUE;
    private ValueFormatter<Short> formatter = ShortFieldController.DEFAULT_FORMATTER::apply;

    public ShortFieldControllerBuilderImpl(Option<Short> option) {
        super(option);
    }

    @Override
    public ShortFieldControllerBuilder min(Short min) {
        this.min = min;
        return this;
    }

    @Override
    public ShortFieldControllerBuilder max(Short max) {
        this.max = max;
        return this;
    }

    @Override
    public ShortFieldControllerBuilder range(Short min, Short max) {
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public ShortFieldControllerBuilder formatValue(ValueFormatter<Short> formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public Controller<Short> build() {
        return ShortFieldController.createInternal(option, min, max, formatter);
    }
}
