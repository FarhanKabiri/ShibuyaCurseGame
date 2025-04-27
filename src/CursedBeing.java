abstract class CursedBeing {
    String name;
    int health;
    int maxHealth;
    int power;
    int mana;
    int maxMana;

    public CursedBeing(String name, int health, int power, int mana) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.power = power;
        this.mana = mana;
        this.maxMana = mana;
    }

    abstract void battleCry();

    public String getHealthBar() {
        int barLength = 20;
        int filledBars = (int) ((double) health / maxHealth * barLength);
        return "[" + "█".repeat(filledBars) + "-".repeat(barLength - filledBars) + "] " + health + "/" + maxHealth;
    }

    public void attackBack(CursedFighter target) {
        int damage = (int) (Math.random() * (power / 2)) + (power / 3);
        target.health -= Math.min(damage, target.health);
        System.out.println(name + " strikes back! " + target.name + " loses " + damage + " health.");
    }
}
