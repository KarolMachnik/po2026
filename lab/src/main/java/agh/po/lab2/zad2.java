package agh.po.lab2;

import java.util.ArrayList;
import java.util.Random;

public class zad2 {
    public static void main(String[] args) {
        ArrayList<Integer> typyUzytkownika = new ArrayList<>();
        for (String arg : args) {
            typyUzytkownika.add(Integer.parseInt(arg));
        }

        ArrayList<Integer> wylosowaneLiczby = new ArrayList<>();
        Random random = new Random();
        while (wylosowaneLiczby.size() < 6) {
            int liczba = random.nextInt(49) + 1;
            if (!wylosowaneLiczby.contains(liczba)) {
                wylosowaneLiczby.add(liczba);
            }
        }

        int trafienia = 0;
        for (int typ : typyUzytkownika) {
            if (wylosowaneLiczby.contains(typ)) {
                trafienia++;
            }
        }

        System.out.println("Twoje typy: " + typyUzytkownika);
        System.out.println("Wylosowane liczby: " + wylosowaneLiczby);
        System.out.println("Liczba trafień: " + trafienia);
    }
}