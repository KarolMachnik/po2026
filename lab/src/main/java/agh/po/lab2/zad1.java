package agh.po.lab2;

import java.util.ArrayList;
import java.util.Random;

public class zad1 {
    public static void main(String[] args) {
        ArrayList<Integer> wylosowaneLiczby = new ArrayList<>();

        Random random = new Random();
            while (wylosowaneLiczby.size() < 6) {
                int liczba = random.nextInt(48) + 1;
                if (!wylosowaneLiczby.contains(liczba)) {
                    wylosowaneLiczby.add(liczba);
                }
            }

        System.out.println("Wylosowane liczby: " + wylosowaneLiczby);
        
        }                
}
