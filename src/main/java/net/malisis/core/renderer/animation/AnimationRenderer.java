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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.malisis.core.renderer.animation.transformation.ITransformable;
import net.malisis.core.renderer.animation.transformation.Transformation;
import net.minecraft.client.Minecraft;

/**
 * @author Ordinastie
 *
 */
public class AnimationRenderer {

    private long startTime = -1;
    private boolean clearFinished = false;
    private LinkedList<Animation> animations = new LinkedList<>();
    private List<ITransformable> tranformables = new ArrayList<>();
    private List<Animation> toClear = new ArrayList<>();

    public AnimationRenderer() {
        setStartTime();
    }

    public void setStartTime(long start) {
        this.startTime = start;
    }

    public void setStartTime() {
        setStartTime(System.currentTimeMillis());
    }

    public void setStartTick(long start) {
        setStartTime(System.currentTimeMillis() - (getWorldTime() - start) * 1000 / 20);
    }

    public long getWorldTime() {
        if (Minecraft.getMinecraft().theWorld != null) return Minecraft.getMinecraft().theWorld.getTotalWorldTime();
        else return 0;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    public float getElapsedTicks() {
        return (float) (((double) getElapsedTime() / 1000) * 20);
    }

    public void addAnimation(Animation animation) {
        animations.add(animation);
    }

    public void deleteAnimation(Animation animation) {
        animations.remove(animation);
    }

    public void clearAnimations() {
        animations.clear();
    }

    public void autoClearAnimations() {
        clearFinished = true;
    }

    public List<ITransformable> animate(Animation... animations) {
        tranformables.clear();
        toClear.clear();

        if (animations == null || animations.length == 0) return tranformables;

        ITransformable tr = null;
        long elapsedTime = getElapsedTime();

        for (Animation animation : animations) {
            tr = animation.animate(elapsedTime);
            if (tr != null) tranformables.add(tr);

            if (animation.isFinished() && clearFinished) toClear.add(animation);
        }

        return tranformables;
    }

    public List<ITransformable> animate() {
        List<ITransformable> anims = animate(animations.toArray(new Animation[0]));

        for (Animation animation : toClear) animations.remove(animation);

        return anims;
    }

    public void animate(ITransformable transformable, Transformation animation) {
        if (transformable == null) return;

        animation.transform(transformable, getElapsedTime());
    }
}
