package agh.po.lab4.animals;

public class Dog extends Animal {
    public Dog(String name) {
        super(name, 4);
    }

    @Override
    public String getDescription() {
        return "Pies o imieniu " + name;
    }

    @Override
    public void makeSound() {
        System.out.println("Woof woof!");
    }
}