/*
 * The MIT License (MIT) Copyright (c) 2014 Ordinastie Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions: The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.malisis.core.client.gui.component.control;

import net.malisis.core.client.gui.Anchor;
import net.malisis.core.client.gui.GuiRenderer;
import net.malisis.core.client.gui.MalisisGui;
import net.malisis.core.client.gui.component.UIComponent;
import net.malisis.core.client.gui.component.container.UIContainer;
import net.malisis.core.util.MouseButton;

/**
 * @author Ordinastie
 *
 */
public class UIMoveHandle extends UIComponent<UIMoveHandle> implements IControlComponent {

    public enum Type {
        BOTH,
        HORIZONTAL,
        VERTICAL
    }

    private final Type type;

    public UIMoveHandle(MalisisGui gui, UIComponent parent, Type type) {
        super(gui);
        this.type = type != null ? type : Type.BOTH;

        int x = 1;
        int y = 1;
        if (parent instanceof UIContainer) {
            x -= ((UIContainer) parent).getHorizontalPadding();
            y -= ((UIContainer) parent).getVerticalPadding();
        }
        setPosition(x, y);
        setSize(5, 5);
        setZIndex(10);
        register(this);

        parent.addControlComponent(this);

        icon = gui.getGuiTexture().getIcon(268, 15, 15, 15);
    }

    public UIMoveHandle(MalisisGui gui, UIComponent parent) {
        this(gui, parent, Type.BOTH);
    }

    @Override
    public boolean onDrag(int lastX, int lastY, int x, int y, MouseButton button) {
        if (button != MouseButton.LEFT) return super.onDrag(lastX, lastY, x, y, button);

        UIComponent parentCont = getParent().getParent();
        if (parentCont == null) return super.onDrag(lastX, lastY, x, y, button);

        int px = parent.getX();
        if (type == Type.BOTH || type == Type.HORIZONTAL) px = parentCont.relativeX(x);
        int py = parent.getY();
        if (type == Type.BOTH || type == Type.VERTICAL) py = parentCont.relativeY(y);
        if (px < 0) px = 0;
        if (py < 0) py = 0;

        getParent().setPosition(px, py, Anchor.NONE);
        return true;
    }

    @Override
    public void drawBackground(GuiRenderer renderer, int mouseX, int mouseY, float partialTick) {}

    @Override
    public void drawForeground(GuiRenderer renderer, int mouseX, int mouseY, float partialTick) {
        rp.icon.set(icon);
        renderer.drawShape(shape, rp);
    }
}
