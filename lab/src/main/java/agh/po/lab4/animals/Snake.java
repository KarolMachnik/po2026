package agh.po.lab4.animals;

public class Snake extends Animal {
    public Snake(String name) {
        super(name, 0);
    }

    @Override
    public String getDescription() {
        return "Wąż o imieniu " + name;
    }

    @Override
    public void makeSound() {
        System.out.println("Sss!");
    }
}