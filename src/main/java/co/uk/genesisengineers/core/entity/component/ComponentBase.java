package co.uk.genesisengineers.core.entity.component;

import co.uk.genesisengineers.core.content.entityPrototypeFactory.ComponentAttributes;

public abstract class ComponentBase {

    protected int type;

    public int getType() {
        return type;
    }

    abstract public ComponentBase clone();

    public ComponentBase() {
    }

    public ComponentBase(ComponentAttributes componentAttributes) throws NoSuchMethodException {
        System.out.println(this.getClass().getName());
        this.getClass().getConstructor(ComponentAttributes.class);
    }
}
