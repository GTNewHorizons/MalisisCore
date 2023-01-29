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

package net.malisis.core.renderer.animation.transformation;

/**
 * @author Ordinastie
 *
 */
public class AlphaTransform extends Transformation<AlphaTransform, ITransformable.Alpha> {

    protected int fromAlpha;
    protected int toAlpha;

    public AlphaTransform(int fromAlpa, int toAlpha) {
        this.fromAlpha = fromAlpa;
        this.toAlpha = toAlpha;
    }

    @Override
    protected void doTransform(ITransformable.Alpha transformable, float comp) {
        if (comp <= 0) return;

        float from = reversed ? toAlpha : fromAlpha;
        float to = reversed ? fromAlpha : toAlpha;

        transformable.setAlpha((int) (from + (to - from) * comp));
    }
}
