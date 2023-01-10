package net.mikrowonk.game;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Starting {
    protected static int strength, health, type, specialMoveStrength;
    protected static String specialMove, name;

    public Starting (){
    }

    /**
     * starts the Selection of Character
     */
    public static Player Selection() {
        Scanner scan = new Scanner(System.in);
        boolean decided = false;
        Desktop d = Desktop.getDesktop();
        Player player = null;

        System.out.println("New Game(1) or start from Save(2)?");
        int savingChoice = scan.nextInt();

        if (savingChoice == 2) {
            try {
                player = new Save().getPlayerSave();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return player;
        }

        else {
        System.out.println("<Opening character selection, please view>");

        // opening the stat preview of all characters
        try {
            d.open(new File("src/net/mikrowonk/assets/Fighters.txt"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // asking what character the player wants to choose, until they pick either 1,2 or 3

        do {
            System.out.println();
            System.out.println("Which one of these Pokemon do you want? (1, 2, 3)");
            System.out.print(":");
            type = scan.nextInt();
            if (type == 1 || type == 2 || type == 3) {
                decided = true;
            }
            else {
                System.out.println("<That is not a character, try again>");
            }
        } while (!decided);

        System.out.println("<Now opening preview of your Character>");

        // opening the preview of the chosen character

        try {
            d.open(new File("src/net/mikrowonk/assets/"+ type +".png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print("Now give your Pokemon a name: ");
        name = scan.next();
        System.out.println();


        // sets the stats of the player depending on their choice

        switch (type) {
            case 1 -> {
                strength = 10;
                health = 40;
                specialMove = "Earthquake";
            }
            case 2 -> {
                strength = 20;
                health = 20;
                specialMove = "Thunder";
            }
            default -> {
                strength = 5;
                health = 70;
                specialMove = "Roll";
            }
        }
        specialMoveStrength = strength * 2;
        }
        return new Player(strength, health, specialMove, name, specialMoveStrength, 0, 1, 0);
    }
}
