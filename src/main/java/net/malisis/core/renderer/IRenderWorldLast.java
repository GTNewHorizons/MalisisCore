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

package net.malisis.core.renderer;

import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * This interface defines the class as being able to render with the {@link RenderWorldLastEvent}.
 *
 * @author Ordinastie
 */
public interface IRenderWorldLast {

    /**
     * Whether to set the translations based on player current position and partialTick.
     *
     * @return true, if the renderer should set the translations
     */
    boolean shouldSetViewportPosition();

    /**
     * Whether the renderer should render.
     *
     * @param event the event
     * @param world the world
     * @return true, if rendering should be done
     */
    boolean shouldRender(RenderWorldLastEvent event, IBlockAccess world);

    /**
     * Renders when {@link RenderWorldLastEvent} is called.
     *
     * @param event the event
     * @param world the world
     */
    void renderWorldLastEvent(RenderWorldLastEvent event, IBlockAccess world);
}
