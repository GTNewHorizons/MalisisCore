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

package net.malisis.core.util.replacement;

import java.lang.reflect.Field;

import net.malisis.core.asm.AsmUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * @author Ordinastie
 *
 */
public class ShapedOreRecipeHandler extends ReplacementHandler<ShapedOreRecipe> {

    private final Field inputField;
    private final Field outputField;

    public ShapedOreRecipeHandler() {
        super(ShapedOreRecipe.class);
        inputField = AsmUtils.changeFieldAccess(ShapedOreRecipe.class, "input");
        outputField = AsmUtils.changeFieldAccess(ShapedOreRecipe.class, "output");
    }

    @Override
    public boolean replace(ShapedOreRecipe recipe, Object vanilla, Object replacement) {
        boolean replaced = false;
        try {
            if (isMatched(recipe.getRecipeOutput(), vanilla)) {
                outputField.set(recipe, getItemStack(replacement));
                replaced = true;
            }

            Object[] input = (Object[]) inputField.get(recipe);

            for (int i = 0; i < input.length; i++) {
                if (input[i] instanceof ItemStack && isMatched((ItemStack) input[i], vanilla)) {
                    input[i] = getItemStack(replacement);
                    replaced = true;
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return replaced;
    }
}
