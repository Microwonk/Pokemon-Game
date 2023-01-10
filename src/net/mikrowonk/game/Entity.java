package net.mikrowonk.game;

public abstract class Entity {
    protected int strength;
    protected int health;
    protected String name;

    public Entity(int strength, int health, String name) {
        this.strength = strength;
        this.health = health;
        this.name = name;
    }

    public Entity() {
    }

    public void showStats() {
        System.out.println("----==-< " + this.name + "'s Stats >-==----");
        System.out.println("Strength: " + this.strength);
        System.out.println("Health: " + this.health);
        // delay to make it visible for longer
        try {
            Thread.sleep(2000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getStrength() {
        return strength;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
