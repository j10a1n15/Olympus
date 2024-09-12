package earth.terrarium.olympus.client.components.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.teamresourceful.resourcefullib.client.components.CursorWidget;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import earth.terrarium.olympus.client.components.base.BaseWidget;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRendererContext;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;

import java.util.function.Consumer;

public class Button extends BaseWidget implements CursorWidget {

    private WidgetRenderer<? super Button> renderer = WidgetRenderer.empty();
    private Runnable onPress = () -> {};
    private WidgetSprites sprites = UIConstants.BUTTON;

    private Button() {
        super();
    }

    public static Button create(Consumer<Button> factory) {
        Button button = new Button();
        factory.accept(button);
        return button;
    }

    public static Button create() {
        return create(button -> {});
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        graphics.blitSprite(sprites.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
        this.renderer.render(graphics, new WidgetRendererContext<>(this, mouseX, mouseY), partialTick);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.onPress.run();
    }

    @Override
    public CursorScreen.Cursor getCursor() {
        return !this.isActive() ? CursorScreen.Cursor.DISABLED : CursorScreen.Cursor.POINTER;
    }

    public Button withRenderer(WidgetRenderer<? super Button> renderer) {
        this.renderer = renderer;
        return this;
    }

    public Button withCallback(Runnable onPress) {
        this.onPress = onPress;
        return this;
    }

    public Button withTexture(WidgetSprites sprites) {
        this.sprites = sprites;
        return this;
    }
}