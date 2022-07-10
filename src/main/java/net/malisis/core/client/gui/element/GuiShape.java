/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Ordinastie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.malisis.core.client.gui.element;

import net.malisis.core.renderer.element.Face;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.element.Vertex;

/**
 * @author Ordinastie
 *
 */
public abstract class GuiShape extends Shape {
    public GuiShape(Face... faces) {
        super(faces);
    }

    public GuiShape(int faceCount) {
        faces = new Face[faceCount];
        for (int i = 0; i < faceCount; i++) faces[i] = new GuiFace();
    }

    public void setPosition(float x, float y) {
        translate(x, y, 0);
    }

    @Override
    public void translate(float x, float y, float z) {
        super.translate(x, y, z);
        applyMatrix();
    }

    public void translate(int x, int y) {
        translate(x, y, 0);
    }

    @Override
    public void rotate(float angle, float x, float y, float z) {
        rotate(angle, 0, 0, 1, x, y, z);
    }

    public void rotate(float angle) {
        //		rotate(angle, x + (x + width) / 2, y + (y + height) / 2, 0);
        //		applyMatrix();
    }

    @Override
    public void scale(float scale) {
        scale(scale, scale);
    }

    public abstract void setSize(float width, float height);

    public abstract void scale(float x, float y);

    public static class GuiFace extends Face {
        public GuiFace() {
            super(
                    new Vertex.BottomSouthWest().setBaseName("TopLeft"),
                    new Vertex.TopSouthWest().setBaseName("BottomLeft"),
                    new Vertex.TopSouthEast().setBaseName("BottomRight"),
                    new Vertex.BottomSouthEast().setBaseName("TopRight"));
            setStandardUV();
        }

        public GuiFace(int width, int height) {
            this();
            factor(width, height, 0);
        }
    }
}
