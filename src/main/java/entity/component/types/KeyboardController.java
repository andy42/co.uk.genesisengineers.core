package entity.component.types;

import content.entityPrototypeFactory.ComponentAttributes;
import entity.component.ComponentBase;

public class KeyboardController extends ComponentBase {
    public KeyboardController () {
        this.type = Type.KEYBOARD_CONTROLLER;
    }

    public KeyboardController (ComponentAttributes componentAttributes) {
        this();
    }

    @Override
    public ComponentBase clone() {
        return new KeyboardController();
    }
}
