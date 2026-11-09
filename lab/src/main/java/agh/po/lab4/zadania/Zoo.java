package agh.po.lab4.zadania;

import agh.po.lab4.animals.Animal;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Zoo {
    public static void main(String[] args) throws Exception {
        List<Class<?>> animalClasses = new ArrayList<>();
        URL resource = Thread.currentThread().getContextClassLoader().getResource("agh/po/lab4/animals");

        if (resource != null) {
            File dir = new File(resource.toURI());
            if (dir.exists() && dir.isDirectory()) {
                for (File file : dir.listFiles()) {
                    if (file.getName().endsWith(".class") && !file.getName().equals("Animal.class")) {
                        String className = "agh.po.lab4.animals." + file.getName().replace(".class", "");
                        animalClasses.add(Class.forName(className));
                    }
                }
            }
        }

        if (animalClasses.isEmpty()) {
            return;
        }

        Animal[] animals = new Animal[100];
        Random random = new Random();

        for (int i = 0; i < animals.length; i++) {
            Class<?> clazz = animalClasses.get(random.nextInt(animalClasses.size()));
            animals[i] = (Animal) clazz.getConstructor(String.class).newInstance(clazz.getSimpleName() + "_" + i);
        }

        int totalLegs = 0;

        for (Animal animal : animals) {
            System.out.println(animal.getDescription());
            animal.makeSound();

            int legs = animal.getLegs();
            totalLegs += legs;
        }

        System.out.println("\nCałkowita liczba nóg w Zoo: " + totalLegs);
    }
}