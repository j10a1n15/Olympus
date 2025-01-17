package earth.terrarium.olympus.client.components.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.teamresourceful.resourcefullib.client.components.CursorWidget;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import earth.terrarium.olympus.client.components.base.BaseWidget;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRendererContext;
import earth.terrarium.olympus.client.components.dropdown.DropdownBuilder;
import earth.terrarium.olympus.client.components.dropdown.DropdownState;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.navigation.CommonInputs;
import org.jetbrains.annotations.Nullable;

public class Button extends BaseWidget implements CursorWidget {

    private WidgetRenderer<? super Button> renderer = WidgetRenderer.empty();
    private Runnable onPress = () -> {};
    private WidgetSprites sprites = UIConstants.BUTTON;

    public Button() {
        super();
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        if (this.sprites != null) {
            graphics.blitSprite(this.sprites.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
        }
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

    public Button withTexture(@Nullable WidgetSprites sprites) {
        this.sprites = sprites;
        return this;
    }

    @Override
    public Button withSize(int width, int height) {
        return (Button) super.withSize(width, height);
    }

    public <T> DropdownBuilder<T> withDropdown(DropdownState<T> state) {
        state.setButton(this);
        return new DropdownBuilder<>(state);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.active && this.visible) {
            if (CommonInputs.selected(keyCode)) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                this.onPress.run();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
