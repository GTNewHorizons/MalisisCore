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

package net.malisis.core.renderer.animation;

import net.malisis.core.renderer.animation.transformation.ITransformable;
import net.malisis.core.renderer.animation.transformation.Transformation;
import net.malisis.core.util.Timer;

/**
 * @author Ordinastie
 *
 */
public class Animation {

    private final ITransformable transformable;
    private final Transformation transform;
    private int delay;
    private boolean started = false;
    private boolean finished = false;

    private boolean renderBefore = true;
    private boolean renderAfter = true;

    public Animation(ITransformable transformable, Transformation transform) {
        this.transformable = transformable;
        this.transform = transform;
    }

    public void setRender(boolean before, boolean after) {
        renderBefore = before;
        renderAfter = after;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public ITransformable animate(long elapsedTime) {
        if (transform == null || transformable == null) return transformable;

        long elapsed = elapsedTime - Timer.tickToTime(delay);
        started = elapsed > transform.getDelay();
        finished = elapsed > transform.totalDuration() && transform.getLoops() != -1;

        if (!started && !renderBefore) return null;
        if (finished && !renderAfter) return null;

        transform.transform(transformable, elapsed);
        return transformable;
    }
}
