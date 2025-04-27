import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ShibuyaGameGUI extends JFrame implements ActionListener {

    private JTextArea storyArea;
    private JButton attackButton, spellButton, manaButton;
    private JProgressBar playerHealthBar, playerManaBar, enemyHealthBar;
    private JLabel creatureImageLabel;
    private CursedFighter gojo;
    private CursedFighter enemy;
    private int questStage = 1;

    public ShibuyaGameGUI() {
        // Frame settings
        setTitle("Shibuya: The Curse");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center window

        // Main panels
        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel();

        // Left side: story + player stats
        JPanel topStatsPanel = new JPanel(new GridLayout(2, 2));
        playerHealthBar = new JProgressBar(0, 100);
        playerHealthBar.setForeground(Color.RED);
        playerHealthBar.setStringPainted(true);
        playerManaBar = new JProgressBar(0, 50);
        playerManaBar.setForeground(Color.BLUE);
        playerManaBar.setStringPainted(true);

        topStatsPanel.add(new JLabel("Gojo Health:"));
        topStatsPanel.add(playerHealthBar);
        topStatsPanel.add(new JLabel("Mana:"));
        topStatsPanel.add(playerManaBar);

        storyArea = new JTextArea();
        storyArea.setEditable(false);
        storyArea.setWrapStyleWord(true);
        storyArea.setLineWrap(true);
        storyArea.setFont(new Font("Serif", Font.PLAIN, 18));
        JScrollPane scrollPane = new JScrollPane(storyArea);

        leftPanel.add(topStatsPanel, BorderLayout.NORTH);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom buttons
        attackButton = new JButton("Attack");
        spellButton = new JButton("Cast Spell");
        manaButton = new JButton("Concentrate (Mana)");

        attackButton.addActionListener(this);
        spellButton.addActionListener(this);
        manaButton.addActionListener(this);

        bottomPanel.add(attackButton);
        bottomPanel.add(spellButton);
        bottomPanel.add(manaButton);

        // Right side: enemy image + enemy health
        creatureImageLabel = new JLabel();
        creatureImageLabel.setHorizontalAlignment(JLabel.CENTER);
        creatureImageLabel.setVerticalAlignment(JLabel.CENTER);

        enemyHealthBar = new JProgressBar();
        enemyHealthBar.setForeground(Color.RED);
        enemyHealthBar.setStringPainted(true);

        rightPanel.add(creatureImageLabel, BorderLayout.CENTER);
        rightPanel.add(enemyHealthBar, BorderLayout.SOUTH);

        // Add panels to main frame
        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);

        // Start game
        startGame();
    }

    private void startGame() {
        gojo = new CursedFighter("Gojo", 100, 30, 50);
        enemy = new CursedFighter("Fly Head", 40, 10, 40);
        updateBars();
        setCreatureImage("Fly Head");
        printText("You are Gojo, a powerful sorcerer protecting Shibuya.\n\nA Fly Head appears!\nPrepare for battle!");
    }

    private void updateBars() {
        playerHealthBar.setMaximum(gojo.maxHealth);
        playerHealthBar.setValue(gojo.health);
        playerManaBar.setMaximum(gojo.maxMana);
        playerManaBar.setValue(gojo.mana);

        enemyHealthBar.setMaximum(enemy.maxHealth);
        enemyHealthBar.setValue(enemy.health);
    }

    private void printText(String text) {
        storyArea.append("\n\n" + text);
        storyArea.setCaretPosition(storyArea.getDocument().getLength());
    }
    
    private void nextQuest() {
        if (questStage == 1) {
            questStage++;
            printText("\nYou defeated Fly Head!\n\nOpening a chest...");
            gojo.unlockSorcery("Healing");
            enemy = new CursedFighter("Roppongi Curse", 50, 20, 60);
            setCreatureImage("Roppongi Curse");
            printText("\nA stronger curse appears: Roppongi Curse!");
            updateBars();
        } else if (questStage == 2) {
            questStage++;
            printText("\nYou defeated Roppongi Curse!\n\nApproaching the final battle...");
            gojo.unlockSorcery("Necromancy");
            enemy = new CursedFighter("Eso Spirit", 60, 35, 80);
            setCreatureImage("Eso Spirit");
            printText("\nA terrible force appears: Eso Spirit!");
            updateBars();
        } else {
            printText("\nCongratulations! You defeated all curses and saved Shibuya!");
            attackButton.setEnabled(false);
            spellButton.setEnabled(false);
            manaButton.setEnabled(false);
        }
    }

    private void setCreatureImage(String enemyName) {
        try {
            ImageIcon icon = null;
            if (enemyName.equals("Fly Head")) {
                icon = new ImageIcon("images/flyhead.png");
            } else if (enemyName.equals("Roppongi Curse")) {
                icon = new ImageIcon("images/roppongi.png");
            } else if (enemyName.equals("Eso Spirit")) {
                icon = new ImageIcon("images/esospirit.png");
            }

            if (icon != null) {
                Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                creatureImageLabel.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            creatureImageLabel.setText("Image Load Failed");
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
        enemyHealthBar.setValue(Math.max(enemy.health, 0)); // Update enemy health bar live
    }

    public static void main(String[] args) {
        new ShibuyaGameGUI();
    }
}
