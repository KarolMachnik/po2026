package agh.po.lab4.animals;

public abstract class Animal {
    protected String name;
    protected int legs;

    public Animal(String name, int legs) {
        this.name = name;
        this.legs = legs;
    }

    public abstract String getDescription();
    
    public abstract void makeSound();

    public int getLegs() {
        return legs;
    }
}