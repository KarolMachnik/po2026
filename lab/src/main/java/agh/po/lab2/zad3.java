package agh.po.lab2;

import java.util.ArrayList;
import java.util.Random;

public class zad3 {
    public static void main(String[] args) {
        ArrayList<Integer> typyUzytkownika = new ArrayList<>();
        for (String arg : args) {
            typyUzytkownika.add(Integer.parseInt(arg));
        }

        long startTime = System.currentTimeMillis();
        long liczbaLosowan = 0;
        Random random = new Random();
        ArrayList<Integer> wylosowaneLiczby = new ArrayList<>(6);

        while (true) {
            liczbaLosowan++;
            wylosowaneLiczby.clear();

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

            if (trafienia == 6) {
                break;
            }
        }

        long endTime = System.currentTimeMillis();
        long czasTrwania = endTime - startTime;

        System.out.println("Twoje typy: " + typyUzytkownika);
        System.out.println("Trafione liczby: " + wylosowaneLiczby);
        System.out.println("Liczba losowań: " + liczbaLosowan);
        System.out.println("Czas działania: " + czasTrwania + " ms (" + (czasTrwania / 1000.0) + " s)");
    }
}