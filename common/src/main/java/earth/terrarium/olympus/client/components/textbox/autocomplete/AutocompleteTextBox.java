package earth.terrarium.olympus.client.components.textbox.autocomplete;

import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import earth.terrarium.olympus.client.components.base.BaseWidget;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.Optionull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class AutocompleteTextBox<T> extends BaseWidget {

    private static final WidgetSprites SPRITES = new WidgetSprites(
        new ResourceLocation(UIConstants.MOD_ID, "textbox/normal"),
        new ResourceLocation(UIConstants.MOD_ID, "textbox/hovered"),
        new ResourceLocation(UIConstants.MOD_ID, "textbox/focused")
    );
    private static final int TEXT_COLOR = 0xe0e0e0;
    private static final int PADDING = 4;

    final List<T> suggestions;
    final BiPredicate<String, T> filter;
    final Function<T, String> mapper;

    AutocompleteScreen<T> screen;
    String value;

    public AutocompleteTextBox(AutocompleteTextBox<T> box, String value, int width, int height, List<T> suggestions, BiPredicate<String, T> filter, Function<T, String> mapper) {
        super(width, height);
        this.screen = box != null ? box.screen : null;
        this.suggestions = suggestions;
        this.filter = filter;
        this.mapper = mapper;
        this.value = value;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.value = Optionull.mapOrDefault(this.screen, AutocompleteScreen::text, this.value);

        Font font = Minecraft.getInstance().font;
        ResourceLocation texture = SPRITES.get(this.isHoveredOrFocused(), !this.isActive());

        graphics.blitSprite(texture, this.getX(), this.getY(), this.width, this.height);


        String truncatedValue = font.plainSubstrByWidth(this.value, this.width - PADDING * 2);
        if (!truncatedValue.isEmpty()) {
            graphics.drawString(font, truncatedValue, this.getX() + PADDING, this.getY() + (this.height - PADDING * 2) / 2, TEXT_COLOR);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        Screen screen = Minecraft.getInstance().screen;
        this.screen = new AutocompleteScreen<>(screen, this);
        Minecraft.getInstance().setScreen(this.screen);
    }

    @Override
    public CursorScreen.Cursor getCursor() {
        return CursorScreen.Cursor.TEXT;
    }

    public T value() {
        if (this.screen != null) {
            return this.screen.value();
        }
        for (T suggestion : this.suggestions) {
            if (this.mapper.apply(suggestion).equals(this.value)) {
                return suggestion;
            }
        }
        return null;
    }

    public String getRawValue() {
        if (this.screen != null) {
            return this.screen.text();
        }
        return this.value;
    }

    public void clear() {
        if (this.screen != null) {
            this.screen.clear();
            return;
        }
        this.value = "";
    }
}
