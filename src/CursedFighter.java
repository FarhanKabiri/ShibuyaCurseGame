import java.util.HashMap;

public class CursedFighter extends CursedBeing implements BattleActions {
    HashMap<Integer, String> spells = new HashMap<>();
    String sorceryType = "None";

    public CursedFighter(String name, int health, int power, int mana) {
        super(name, health, power, mana);
        initializeSpells();
    }

    private void initializeSpells() {
        spells.put(1, "Energy Blast (10 Mana) - Deals solid damage");
        spells.put(2, "Barrier (10 Mana) - Increases defense");
        spells.put(3, "Healing Aura (15 Mana) - Restores health");
    }

    @Override
    void battleCry() {
        // Not used in GUI version
    }

    @Override
    public void attack(CursedFighter target) {
        int baseDamage = 10;
        int damage = sorceryType.equals("Necromancy") ? 15 : sorceryType.equals("Healing") ? 5 : baseDamage;
        target.takeDamage(damage);
    }

    public void unlockSorcery(String sorcery) {
        if (sorcery.equals("Healing")) {
            spells.put(4, "Regeneration Wave (20 Mana) - Restores massive health");
        } else if (sorcery.equals("Necromancy")) {
            spells.put(5, "Soul Decay (20 Mana) - Deals immense damage");
        } else if (sorcery.equals("Domain Expansion")) {
            spells.put(6, "Domain Expansion (30 Mana) - Unleashes devastating power");
        }
        sorceryType = sorcery;
    }
    
    @Override
    public void castSpell(CursedFighter target) {
        if (sorceryType.equals("Healing")) {
            heal(20);
        } else if (sorceryType.equals("Necromancy")) {
            target.takeDamage(30);
        } else if (sorceryType.equals("Domain Expansion")) {
            if (mana >= 25) {
                target.takeDamage(50); // Massive damage
                System.out.println(name + " used Domain Expansion! It's devastating!");
            } else {
                System.out.println(name + " doesn't have enough mana for Domain Expansion!");
            }
        } else {
            target.takeDamage(15); // Default spell damage
        }
        mana = Math.max(mana - 10, 0); // Reduce mana for all spells
    }
    public void gainMana(int amount) {
        mana = Math.min(mana + amount, maxMana);
    }

    public void heal(int amount) {
        health = Math.min(health + amount, maxHealth);
    }

    public void takeDamage(int amount) {
        health -= amount;
    }

    public int getHealth() {
        return health;
    }

    public void resetStats() {
        health = maxHealth;
        power = 30;
        mana = maxMana;
        sorceryType = "None";
    }
}
