package de.keyle.dungeoncraft.editor.editors.entity.Components;

public interface IComponent<T> {

    public String getName();
    public T getValue();
    public void setValue(T value);
}
