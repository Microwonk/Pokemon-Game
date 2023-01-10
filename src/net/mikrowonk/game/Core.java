package net.mikrowonk.game;

import java.io.IOException;
import java.util.*;

public class Core {

    // übernahme vom player der Main Klasse
    public static Player player;

    // List of all Enemies, that are beaten
    private List<Enemy> allEnemies;

    public Core() {
        player = Main.player;
        allEnemies = new ArrayList<>();
    }

    // showing the Menu (home screen)
    public void showMenu () {
        System.out.println("===============Menu=================");
        System.out.println("------------------------------------");
        System.out.println("-- (1) for showing Player Stats ----");
        System.out.println("-- (2) for leveling Menu -----------");
        System.out.println("-- (3) show defeated Enemies ----- ");
        System.out.println("-- (4) for fighting random Enemy ---");
        System.out.println("-- (5) to Quit ---------------------");
        System.out.println("------------------------------------");
        System.out.println("====================================");
        interactMenu();
    }

    // Menu mit Auswahl
    public void interactMenu() {
        Scanner scan = new Scanner(System.in);
        int input;

        while (true) {
            input = Integer.parseInt(scan.next());
            if (input == 1 || input == 2 || input == 3 || input == 4 || input == 5) {
                break;
            }
            else {
                System.out.println("Error: Invalid Argument");
            }
        }


        // depending on input, starts methods needed to do that particular choice
        switch (input) {
            case 1 -> {
                player.showStats();
                showMenu();
            }
            case 2 -> {
                leveling();
                showMenu();
            }
            case 3 -> {
                showAllEnemies();
                showMenu();
            }
            case 4 -> {
                Enemy enemy = new Enemy();
                Fight f = new Fight(enemy, player);
                f.fight();
                if (f.getHasWon()) {
                    allEnemies.add(enemy);
                }
                showMenu();
            }
            default -> {
                System.out.println("Do you want to save? (y) (n)");
                String savingDecision = scan.next();
                if (savingDecision.equalsIgnoreCase("y")) {
                    try {
                        Save s = new Save(player);
                        s.saving();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("-==-< Game ended >-==-");
                scan.close();
            }
        }
    }

    // leveling Menu
    private void leveling() {
        Scanner scan = new Scanner(System.in);

        // levelpoints declaration
        int levelPoints = player.getLevel() * 2 - player.getPointsSpent();
        int levelingInput;

        System.out.println("===========Leveling==Menu===========");
        System.out.println("------------------------------------");
        System.out.println("-- (1) Upgrade strength by one -----");
        System.out.println("-- (2) Upgrade health by two -------");
        System.out.println("-- (3) Upgrade " + player.getSpecialMove());
        System.out.println("-- (4) Change name -----------------");
        System.out.println("-- (5) Nevermind -------------------");
        System.out.println("------------------------------------");
        System.out.println("-------- Points: " + levelPoints + " ---------");
        System.out.println("====================================");


        // if there is no value 1-5, it spits out an error
        while (true) {
            System.out.print(": ");
            levelingInput = Integer.parseInt(scan.next());
            if (levelingInput == 1 || levelingInput == 2 || levelingInput == 3 || levelingInput == 4 || levelingInput == 5) {
                break;
            }
            else {
                System.out.println("Error: Invalid Argument");
            }
        }

        // upgrades by the choice made (only if enough points are available)

        if (levelPoints > 0) {
            switch (levelingInput) {
                case 1:
                    player.setPointsSpent(player.getPointsSpent() + 1);
                    
                    player.upgradeStrength();
                    System.out.println("<Leveled Strength by one>");
                    
                    leveling();
                    break;
                case 2:
                    player.setPointsSpent(player.getPointsSpent() + 1);
                    
                    player.upgradeHealth();
                    System.out.println("<Leveled Health by two>");
                    leveling();
                    break;
                case 3:
                    player.setPointsSpent(player.getPointsSpent() + 1);

                    player.upgradeSpecialMoveStrength();
                    System.out.println("<Upgraded " + Starting.specialMove +">");

                    leveling();
                    break;
                case 4:
                    player.setPointsSpent(player.getPointsSpent() + 1);
                    System.out.println("Your Pokemons new Name: ");
                    player.setName(scan.next());
                default:
            }
        }
        else if (levelingInput != 5) {
            System.out.println("<Not enough Points>");
        }
    }

    // lists all Enemies beaten
    public void showAllEnemies() {
        int i = 0;
        Scanner scan = new Scanner(System.in);
        for (Enemy enemy : allEnemies) {
            enemy.showStats();
            System.out.println("Index: " + i);
            System.out.println();
            i++;
        }
        System.out.println("<If you want to fight a previous Pokémon, enter it's index (-1 to leave)>");
        int decision = Integer.parseInt(scan.next());
        if (decision < 0 || decision > i) {
            return;
        }
        Enemy enemy = allEnemies.get(decision);
        Fight f = new Fight(enemy, player);
        f.fight();
        if (f.getHasWon()) {
            allEnemies.add(enemy);
        }
    }
}
