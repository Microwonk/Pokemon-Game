package net.mikrowonk.game;

import java.util.Random;
import java.util.Scanner;
import java.lang.Thread;

public class Fight {

    private Enemy enemy;
    private Player player;
    private final int playerMaxHealth;
    private final int enemyMaxHealth;
    private boolean usedSpecialAttack;
    private boolean hasWon;

    public Fight(Enemy enemy, Player player) {
        this.enemy = enemy;
        this.player = player;
        this.playerMaxHealth = this.player.getHealth();
        this.enemyMaxHealth = this.enemy.getHealth();
        this.usedSpecialAttack = false;
    }

    public void fight () {
        Scanner scan = new Scanner(System.in);
        int input;

        enemy.showStats();

        scrollText("<Do you accept the fight? (y) (n)>");
        String decision = scan.nextLine();

        //deciding to fight

        if (decision.equalsIgnoreCase("n")) {
            scrollText("<Be more prepared next Time>");
            return;
            }

        //fights, until you or the enemy is dead / until you flee

        while ((this.enemy.health > 0 ) || (this.player.health > 0)) {
            scrollText("<It's your turn, what do you wish to do?>");
            input = attackMenu();
            //attack
            if (input == 1) {
                attack(true);
            }
            //heal
            else if (input == 2) {
                heal(player);
            }
            //special attack
            else if (input == 3) {
                // the attack has already been used this round
                specialAttack(usedSpecialAttack);
            }
            //fleeing
            else {
                int xp = getXP(this.enemy.strength, this.enemyMaxHealth);
                scrollText("<Be more prepared next Time>");
                Core.player.levelUp(-xp);
                scrollText("<" + xp + " XP lost>");
                this.hasWon = false;

                //resets the health after every fight
                this.player.health = playerMaxHealth;
                this.enemy.health = enemyMaxHealth;
                return;
            }

            // if it dies, no move can be done by the enemy

            if (this.enemy.health > 0) {
                enemyTurn();
            }
            else {
                scrollText("<" + this.enemy.name + " died!>");
            }

            // calculates the xp the player receives/loses
            int xp = getXP(this.enemy.strength, this.enemyMaxHealth);

            //if you win
            if (this.enemy.health <= 0) {
                scrollText("<You win!>");
                Core.player.levelUp(xp);
                scrollText("<" + xp + " XP received>");
                this.hasWon = true;
                this.player.health = playerMaxHealth;
                this.enemy.health = enemyMaxHealth;
                return;
            }
            //if you lost
            else if (this.player.health <= 0) {
                scrollText("<You lose!>");
                Core.player.levelUp(-xp);
                scrollText("<" + xp + " XP lost>");
                this.hasWon = false;
                this.player.health = playerMaxHealth;
                this.enemy.health = enemyMaxHealth;
                return;
            }
        }
    }

    //pops up every Round, until you beat the enemy or until u die
    public int attackMenu () {
        Scanner scan = new Scanner(System.in);
        int input;
        while (true) {
            System.out.println("------------------------------");
            System.out.println("-- (1) Attack ------------------");
            System.out.println("-- (2) Heal --------------------");
            System.out.println("-- (3) Special Attack (1 use only)");
            System.out.println("-- (4) Flee (lose XP) ------------");
            System.out.println("-------------------------------");
            input = scan.nextInt();
            if (input == 1 || input == 2 || input == 3 || input == 4) {
                return input;
            }
            System.out.println("Error: Invalid Argument");
        }
    }

    // enemies Turn
    public void enemyTurn () {
        Random r = new Random();
        int decision = r.nextInt(1, 4);

        // 2/3 chance it attacks
        if (decision == 1 || decision == 2) {
            attack(false);
        }

        // 1/3 chance it heals itself
        else if (decision == 3) {
            heal(enemy);
        }
        // delay, so that it's easier to see
        try {
            Thread.sleep(2000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //returns attack damage
    public int attackDamage(int strength) {
        Random r = new Random();
        return strength / 4 + r.nextInt(1, 4);
    }

    // der ganze Angriffsablauf
    public void attack(boolean isPlayer) {

        if (isPlayer) {
            bufferedText("<" + this.player.name + " ATTACKED>");
            int attackDamage = attackDamage(this.player.strength);
            bufferedText("<The attack did " + attackDamage + " damage>");
            this.enemy.health -= attackDamage;
            if (this.enemy.health <= 0) {
                this.enemy.health = 0;
            }
            bufferedText("<" + this.enemy.name + " is now at " + this.enemy.health + " health");
        }
        else {
            int attackDamage = attackDamage(this.enemy.strength);
            bufferedText("<" + this.enemy.name + " ATTACKED>");
            bufferedText("<The attack did " + attackDamage + " damage>");
            this.player.health -= attackDamage;
            if (this.player.health <= 0) {
                this.player.health = 0;
            }
                bufferedText("<" + this.player.name + " is now at " + this.player.health + " health");
        }
    }

    // der ganze Heilungsablauf
    public void heal(Entity playerOrEnemy) {
        bufferedText("<" + playerOrEnemy.name + " HEALED>");
        if (playerOrEnemy.equals(enemy)) {
            this.enemy.health += Main.r.nextInt(1, 10);
            if (this.enemy.health > enemyMaxHealth) {
                this.enemy.health = enemyMaxHealth;
            }
            bufferedText("<" + playerOrEnemy.name + " healed up to " + this.enemy.health + " health>");
        }
        else {
            this.player.health += Main.r.nextInt(1,10);
            if (this.player.health > playerMaxHealth) {
                this.player.health = playerMaxHealth;
            }
            bufferedText("<" + playerOrEnemy.name + " healed up to " + this.player.health + " health>");
        }
    }

    public void specialAttack(boolean used) {
        if (used) {
            bufferedText("<" + this.player.name + " already used special move!>");
        }
        // using the special move, sets used to true
        else {
            int specialAttackDamage = attackDamage(player.getSpecialMoveStrength());
            bufferedText("<" + this.player.name + " used " + this.player.getSpecialMove() + "!>");
            bufferedText("Your attack did " + specialAttackDamage + " damage.");
            this.enemy.health -= specialAttackDamage;
            bufferedText("<" + this.enemy.name + " is now at " + this.enemy.health + " health>");
            this.usedSpecialAttack = true;
        }
    }

    //calculates the amount of xp you get, when beating an enemy
    public int getXP (int strength, int health) {
        return (strength + health) * 5;
    }

    public void scrollText(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (!(i == text.length() - 1)) {
                System.out.print(text.charAt(i));
            }
            else {
                System.out.println(text.charAt(i));
            }
            try {
                //noinspection BusyWait
                Thread.sleep(70);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // to make the Text animation a bit more interesting
    public void bufferedText(String text) {
        System.out.println(text);
        try {
            Thread.sleep(300);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // checks, if the player won, if so, then the instance of the enemy is saved to the list of beaten Enemies
    public boolean getHasWon() {
        return hasWon;
    }
}