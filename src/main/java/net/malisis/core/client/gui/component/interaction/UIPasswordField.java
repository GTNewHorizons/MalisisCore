package net.malisis.core.client.gui.component.interaction;

import net.malisis.core.client.gui.MalisisGui;
import net.malisis.core.client.gui.event.ComponentEvent;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

// TODO: Auto-generated Javadoc
/**
 * The Class UIPasswordField.
 */
public class UIPasswordField extends UITextField {

    /** Character to draw instead of real text. */
    private char passwordChar = '*';

    /** Actual stored password *. */
    private StringBuilder password = new StringBuilder();

    /**
     * Instantiates a new {@link UIPasswordField}.
     *
     * @param gui the gui
     */
    public UIPasswordField(MalisisGui gui) {
        super(gui, null, false);
    }

    /**
     * Instantiates a new {@link UIPasswordField}
     *
     * @param gui the gui
     * @param passwordChar the password char
     */
    public UIPasswordField(MalisisGui gui, char passwordChar) {
        this(gui);
        this.passwordChar = passwordChar;
    }

    /**
     * Gets the masking character used when rendering text.
     *
     * @return the masking character
     */
    public char getPasswordCharacter() {
        return passwordChar;
    }

    /**
     * Sets the masking character to use when rendering text.
     *
     * @param passwordChar the masking character to use
     */
    public void setPasswordCharacter(char passwordChar) {
        this.passwordChar = passwordChar;
    }

    @Override
    public String getText() {
        return password.toString();
    }

    /**
     * Updates the text from the password value
     */
    protected void updateText() {
        this.text.setLength(0);
        this.text.append(password.toString().replaceAll("(?s).", String.valueOf(passwordChar)));
    }

    /**
     * Adds the text.
     *
     * @param text the text
     */
    @Override
    public void addText(String text) {
        if (selectingText) deleteSelectedText();

        int position = cursorPosition.textPosition;
        String oldValue = password.toString();
        String newValue = new StringBuilder(oldValue).insert(position, text).toString();

        if (!validateText(newValue)) return;

        if (!fireEvent(new ComponentEvent.ValueChange(this, oldValue, newValue))) return;

        password.insert(position, text);
        cursorPosition.jumpBy(text.length());
        updateText();
    }

    /**
     * Sets the text.
     *
     * @param text the new text
     */
    @Override
    public void setText(String text) {
        if (!validateText(text)) return;

        password.setLength(0);
        password.append(text);
        selectingText = false;
        if (focused) cursorPosition.jumpToEnd();
        updateText();
    }

    /**
     * Delete selected text.
     */
    @Override
    public void deleteSelectedText() {
        if (!selectingText) return;

        int start = Math.min(selectionPosition.textPosition, cursorPosition.textPosition);
        int end = Math.max(selectionPosition.textPosition, cursorPosition.textPosition);

        password.delete(start, end);
        selectingText = false;
        cursorPosition.jumpTo(start);
        updateText();
    }

    /**
     * Handle ctrl key down.
     *
     * @param keyCode the key code
     * @return true, if successful
     */
    @Override
    protected boolean handleCtrlKeyDown(int keyCode) {
        return GuiScreen.isCtrlKeyDown()
                && !(keyCode == Keyboard.KEY_C || keyCode == Keyboard.KEY_X)
                && super.handleCtrlKeyDown(keyCode);
    }
}
