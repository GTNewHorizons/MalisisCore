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

public class Scale extends Transformation<Scale, ITransformable.Scale> {

    protected float fromX = 1, fromY = 1, fromZ = 1;
    protected float toX = 1, toY = 1, toZ = 1;
    protected float offsetX = 0, offsetY = 0, offsetZ = 0;

    public Scale() {}

    public Scale(float x, float y, float z) {
        to(x, y, z);
    }

    public Scale(float fromX, float fromY, float fromZ, float toX, float toY, float toZ) {
        from(fromX, fromY, fromZ);
        to(toX, toY, toZ);
    }

    protected Scale from(float x, float y, float z) {
        fromX = x;
        fromY = y;
        fromZ = z;
        return this;
    }

    protected Scale to(float x, float y, float z) {
        toX = x;
        toY = y;
        toZ = z;
        return this;
    }

    public Scale offset(float x, float y, float z) {
        offsetX = x;
        offsetY = y;
        offsetZ = z;
        return this;
    }

    @Override
    protected void doTransform(ITransformable.Scale transformable, float comp) {
        float fromX = reversed ? this.toX : this.fromX;
        float toX = reversed ? this.fromX : this.toX;
        float fromY = reversed ? this.toY : this.fromY;
        float toY = reversed ? this.fromY : this.toY;
        float fromZ = reversed ? this.toZ : this.fromZ;
        float toZ = reversed ? this.fromZ : this.toZ;

        transformable.scale(
                fromX + (toX - fromX) * comp,
                fromY + (toY - fromY) * comp,
                fromZ + (toZ - fromZ) * comp,
                offsetX,
                offsetY,
                offsetZ);
    }
}
