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

package net.malisis.core.configuration.setting;

import net.malisis.core.client.gui.MalisisGui;
import net.malisis.core.client.gui.component.UIComponent;
import net.malisis.core.client.gui.component.interaction.UICheckBox;
import net.minecraftforge.common.config.Property;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Ordinastie
 *
 */
public class BooleanSetting extends Setting<Boolean> {

    private UICheckBox checkbox;

    public BooleanSetting(String key, boolean defaultValue) {
        super(key, defaultValue);
        type = Property.Type.BOOLEAN;
    }

    @Override
    public Boolean readValue(String stringValue) {
        return Boolean.parseBoolean(stringValue);
    }

    @Override
    public String writeValue(Boolean value) {
        return value.toString();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public UIComponent getComponent(MalisisGui gui) {
        checkbox = new UICheckBox(gui, key).setChecked(value);
        return checkbox;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Boolean getValueFromComponent() {
        return checkbox.isChecked();
    }
}
