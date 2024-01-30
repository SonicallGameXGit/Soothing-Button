package me.sgx.sb.hud.options;

import me.sgx.sb.util.Maths;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class OptionBuilder {
    public static SimpleOption<Boolean> buildBooleanButton(String translationKey, Text tooltip, boolean defaultValue, Consumer<Boolean> onValueChange) {
        return new SimpleOption<>(translationKey, new SimpleOption.TooltipFactory<>() {
            @Override
            public @NotNull Tooltip apply(Boolean value) {
                return Tooltip.of(tooltip);
            }
        }, (optionText, value) -> Text.of(String.valueOf(value)), SimpleOption.BOOLEAN, defaultValue, onValueChange);
    }
    public static SimpleOption<Double> buildDoubleSlider(String translationKey, Text tooltip, double defaultValue, Maths.Range range, double resolution, Consumer<Double> onValueChange) {
        return new SimpleOption<>(translationKey, new SimpleOption.TooltipFactory<>() {
            @Override
            public @NotNull Tooltip apply(Double value) {
                return Tooltip.of(tooltip);
            }
        }, (optionText, value) -> Text.translatable(translationKey).append(Text.of(": " + Math.floor(Maths.scaleRange(value, new Maths.Range(0.0, 1.0), new Maths.Range(range.min(), range.max())) * resolution) / resolution)), SimpleOption.DoubleSliderCallbacks.INSTANCE, Maths.scaleRange(defaultValue, new Maths.Range(range.min(), range.max()), new Maths.Range(0.0, 1.0)), value -> onValueChange.accept(Maths.scaleRange(value, new Maths.Range(0.0, 1.0), new Maths.Range(range.min(), range.max()))));
    }
}