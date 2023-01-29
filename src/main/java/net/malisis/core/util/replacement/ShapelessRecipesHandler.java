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
import java.util.List;

import net.malisis.core.asm.AsmUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;

/**
 * @author Ordinastie
 *
 */
public class ShapelessRecipesHandler extends ReplacementHandler<ShapelessRecipes> {

    private Field outputField;

    public ShapelessRecipesHandler() {
        super(ShapelessRecipes.class);
        outputField = AsmUtils.changeFieldAccess(ShapelessRecipes.class, "recipeOutput", "field_77580_a");
    }

    @Override
    public boolean replace(ShapelessRecipes recipe, Object vanilla, Object replacement) {
        boolean replaced = false;
        try {
            if (isMatched(recipe.getRecipeOutput(), vanilla)) {
                outputField.set(recipe, getItemStack(replacement));
                replaced = true;
            }

            List input = recipe.recipeItems;
            for (int i = 0; i < input.size(); i++) {
                if (input.get(i) instanceof ItemStack && isMatched((ItemStack) input.get(i), vanilla)) {
                    input.set(i, getItemStack(replacement));
                    replaced = true;
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return replaced;
    }
}
