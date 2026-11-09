package agh.po.lab4.zadania;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class AnimalEditor extends JFrame {
    private static final String ANIMALS_DIR = "po2026/lab/src/main/java/agh/po/lab4/animals/";
    
    private JTextField classNameField;
    private JTextField displayNameField;
    private JTextField legsField;
    private JTextField soundField;

    public AnimalEditor() {
        super("Kreator Zwierząt (Modyfikator kodu .java)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 250);
        setLayout(new GridLayout(5, 2, 10, 10));
        setLocationRelativeTo(null); 

        add(new JLabel(" Nazwa klasy (np. Cat):", SwingConstants.LEFT));
        classNameField = new JTextField();
        add(classNameField);

        add(new JLabel(" Polska nazwa (np. Kot):", SwingConstants.LEFT));
        displayNameField = new JTextField();
        add(displayNameField);

        add(new JLabel(" Liczba nóg (np. 4):", SwingConstants.LEFT));
        legsField = new JTextField();
        add(legsField);

        add(new JLabel(" Dźwięk (np. Miau!):", SwingConstants.LEFT));
        soundField = new JTextField();
        add(soundField);

        JButton listButton = new JButton("Wypisz istniejące (Konsola)");
        listButton.addActionListener(e -> listExistingFiles());
        add(listButton);

        JButton saveButton = new JButton("Generuj / Nadpisz plik .java");
        saveButton.setBackground(new Color(60, 179, 113));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> saveAnimalFile());
        add(saveButton);

        setVisible(true);
    }

    private void saveAnimalFile() {
        String className = classNameField.getText().trim();
        String displayName = displayNameField.getText().trim();
        String legsStr = legsField.getText().trim();
        String sound = soundField.getText().trim();

        if (className.isEmpty() || displayName.isEmpty() || legsStr.isEmpty() || sound.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Wypełnij wszystkie pola przed zapisem!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (className.equalsIgnoreCase("Animal")) {
            JOptionPane.showMessageDialog(this, "Nie można nadpisać klasy bazowej Animal!", "Niedozwolona operacja", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int legs = Integer.parseInt(legsStr);
            String javaCode = generateJavaSourceCode(className, displayName, legs, sound);
            
            File directory = new File(ANIMALS_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            Path filePath = Paths.get(ANIMALS_DIR + className + ".java");
            Files.write(filePath, javaCode.getBytes());
            
            JOptionPane.showMessageDialog(this, "Sukces! Zapisano plik:\n" + filePath.toAbsolutePath(), "Zapisano", JOptionPane.INFORMATION_MESSAGE);
            
        } 
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Liczba nóg musi być poprawną liczbą całkowitą!", "Błąd typu", JOptionPane.ERROR_MESSAGE);
        } 
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Błąd podczas zapisu pliku: " + ex.getMessage(), "Błąd I/O", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateJavaSourceCode(String className, String displayName, int legs, String sound) {
        return "package agh.po.lab4.animals;\n\n" +
               "public class " + className + " extends Animal {\n" +
               "    public " + className + "(String name) {\n" +
               "        super(name, " + legs + ");\n" +
               "    }\n\n" +
               "    @Override\n" +
               "    public String getDescription() {\n" +
               "        return \"" + displayName + " o imieniu \" + name;\n" +
               "    }\n\n" +
               "    @Override\n" +
               "    public void makeSound() {\n" +
               "        System.out.println(\"" + sound + "\");\n" +
               "    }\n" +
               "}\n";
    }

    private void listExistingFiles() {
        File dir = new File(ANIMALS_DIR);
        if (dir.exists() && dir.isDirectory()) {
            System.out.println("--- Znalezione klasy zwierząt w pakiecie ---");
            for (File file : dir.listFiles((d, name) -> name.endsWith(".java"))) {
                String className = file.getName().replace(".java", "");
                
                if (className.equals("Animal")) {
                    continue;
                }

                String displayName = "?";
                String legs = "?";
                String sound = "?";

                try {
                    List<String> lines = Files.readAllLines(file.toPath());
                    for (String line : lines) {
                        if (line.contains("super(name,")) {
                            legs = line.substring(line.indexOf(",") + 1, line.indexOf(")")).trim();
                        } 
                        else if (line.contains("return \"") && line.contains(" o imieniu")) {
                            displayName = line.substring(line.indexOf("\"") + 1, line.indexOf(" o imieniu"));
                        } 
                        else if (line.contains("System.out.println(\"")) {
                            sound = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                        }
                    }
                    System.out.println(String.format("Klasa: %-10s | Nazwa: %-10s | Nogi: %-2s | Dźwięk: %s", 
                                                     className, displayName, legs, sound));
                } 
                catch (IOException e) {
                    System.out.println("Klasa: " + className + " (błąd odczytu parametrów)");
                }
            }
            System.out.println("--------------------------------------------");
        } 
        else {
            System.out.println("Nie znaleziono katalogu: " + ANIMALS_DIR);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AnimalEditor::new);
    }
}