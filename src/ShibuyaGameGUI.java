import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ShibuyaGameGUI extends JFrame implements ActionListener {

    private JTextArea storyArea;
    private JButton attackButton, spellButton, manaButton;
    private JProgressBar playerHealthBar, playerManaBar, enemyHealthBar;
    private CursedFighter gojo;
    private CursedFighter enemy;
    private int questStage = 1;

    public ShibuyaGameGUI() {
        // Frame settings
        setTitle("Shibuya: The Curse");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center window

        // Background panel
        JPanel backgroundPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image img = new ImageIcon(new java.net.URL("https://i.imgur.com/1y8p1CU.jpeg")).getImage();
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    setBackground(Color.BLACK);
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Top panel: player + enemy stats
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new GridLayout(2, 2));

        playerHealthBar = new JProgressBar(0, 100);
        playerHealthBar.setForeground(Color.RED);
        playerHealthBar.setStringPainted(true);
        playerManaBar = new JProgressBar(0, 50);
        playerManaBar.setForeground(Color.BLUE);
        playerManaBar.setStringPainted(true);

        enemyHealthBar = new JProgressBar(0, 40);
        enemyHealthBar.setForeground(Color.RED);
        enemyHealthBar.setStringPainted(true);

        topPanel.add(new JLabel("Gojo Health:"));
        topPanel.add(playerHealthBar);
        topPanel.add(new JLabel("Mana:"));
        topPanel.add(playerManaBar);

        backgroundPanel.add(topPanel, BorderLayout.NORTH);

        // Center: story area
        storyArea = new JTextArea();
        storyArea.setEditable(false);
        storyArea.setWrapStyleWord(true);
        storyArea.setLineWrap(true);
        storyArea.setFont(new Font("Serif", Font.PLAIN, 18));
        JScrollPane scrollPane = new JScrollPane(storyArea);
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel: action buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);

        attackButton = new JButton("Attack");
        spellButton = new JButton("Cast Spell");
        manaButton = new JButton("Concentrate (Mana)");

        attackButton.addActionListener(this);
        spellButton.addActionListener(this);
        manaButton.addActionListener(this);

        bottomPanel.add(attackButton);
        bottomPanel.add(spellButton);
        bottomPanel.add(manaButton);

        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(backgroundPanel);
        setVisible(true);

        // Start game
        startGame();
    }

    private void startGame() {
        gojo = new CursedFighter("Gojo", 100, 30, 50);
        enemy = new CursedFighter("Fly Head", 40, 10, 40);
        updateBars();
        printText("You are Gojo, a powerful sorcerer protecting Shibuya from curses.\n\nA Fly Head cursed spirit appears!\nPrepare for battle!");
    }

    private void updateBars() {
        playerHealthBar.setMaximum(gojo.maxHealth);
        playerHealthBar.setValue(gojo.health);
        playerManaBar.setMaximum(gojo.maxMana);
        playerManaBar.setValue(gojo.mana);

        enemyHealthBar.setMaximum(enemy.maxHealth);
        enemyHealthBar.setValue(Math.max(enemy.health, 0));
    }

    private void printText(String text) {
        storyArea.append("\n\n" + text);
    }

    private void nextQuest() {
        if (questStage == 1) {
            questStage++;
            printText("\nYou defeated Fly Head!\n\nOpening a chest...");
            gojo.unlockSorcery("Healing");
            enemy = new CursedFighter("Roppongi Curse", 50, 20, 60);
            printText("\nA stronger curse appears: Roppongi Curse!");
            updateBars();
        } else if (questStage == 2) {
            questStage++;
            printText("\nYou defeated Roppongi Curse!\n\nApproaching the final battle...");
            gojo.unlockSorcery("Necromancy");
            enemy = new CursedFighter("Eso Spirit", 60, 35, 80);
            printText("\nA terrible force appears: Eso Spirit!");
            updateBars();
        } else {
            printText("\nCongratulations! You defeated all curses and saved Shibuya!");
            attackButton.setEnabled(false);
            spellButton.setEnabled(false);
            manaButton.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gojo.health <= 0) {
            printText("\nYou have fallen. Shibuya is doomed...");
            attackButton.setEnabled(false);
            spellButton.setEnabled(false);
            manaButton.setEnabled(false);
            return;
        }

        if (e.getSource() == attackButton) {
            gojo.attack(enemy);
            printText("You attacked " + enemy.name + "!");
        } else if (e.getSource() == spellButton) {
            if (gojo.mana >= 10) {
                gojo.castSpell(enemy);
                printText("You cast a spell on " + enemy.name + "!");
            } else {
                printText("Not enough mana!");
                return;
            }
        } else if (e.getSource() == manaButton) {
            gojo.gainMana(15);
            printText("You concentrate and regain mana.");
        }

        if (enemy.health > 0) {
            enemy.attack(gojo);
            printText(enemy.name + " attacks back!");
        } else {
            nextQuest();
        }

        updateBars();
    }

    public static void main(String[] args) {
        new ShibuyaGameGUI();
    }
}
