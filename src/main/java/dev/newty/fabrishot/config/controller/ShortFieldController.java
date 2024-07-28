package dev.newty.fabrishot.config.controller;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ValueFormatter;
import dev.isxander.yacl3.gui.controllers.string.number.NumberFieldController;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;
import java.util.function.Function;

/**
 * {@inheritDoc}
 */
public class ShortFieldController extends NumberFieldController<Short> {
    private final int min, max;
    public static final Function<Short, Text> DEFAULT_FORMATTER = value -> Text.literal(String.format("%,d", value).replaceAll("[\u00a0\u202F]", " "));

    public ShortFieldController(Option<Short> option, short min, short max, Function<Short, Text> formatter) {
        super(option, formatter);
        this.min = min;
        this.max = max;
    }

    public ShortFieldController(Option<Short> option, short min, short max) {
        this(option, min, max, DEFAULT_FORMATTER);
    }

    public ShortFieldController(Option<Short> option, Function<Short, Text> formatter) {
        this(option, Short.MIN_VALUE, Short.MAX_VALUE, formatter);
    }

    public ShortFieldController(Option<Short> option) {
        this(option, Short.MIN_VALUE, Short.MAX_VALUE, DEFAULT_FORMATTER);
    }

    @ApiStatus.Internal
    public static ShortFieldController createInternal(Option<Short> option, short min, short max, ValueFormatter<Short> formatter) {
        return new ShortFieldController(option, min, max, formatter::format);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double min() {
        return this.min;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double max() {
        return this.max;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString() {
        return NUMBER_FORMAT.format(option().pendingValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPendingValue(double value) {
        option().requestSet((short) value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double pendingValue() {
        return option().pendingValue();
    }
}
