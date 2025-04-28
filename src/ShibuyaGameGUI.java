import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class ShibuyaGameGUI extends JFrame implements ActionListener {

    private JTextArea storyArea;
    private JButton attackButton, spellButton, manaButton;
    private JProgressBar playerHealthBar, playerManaBar, enemyHealthBar;
    private JLabel creatureImageLabel;
    private CursedFighter gojo;
    private CursedFighter enemy;
    private int questStage = 1;
    private Timer typingTimer;
    private String fullText;
    private int charIndex;

    public ShibuyaGameGUI() {
        // Frame settings
        setTitle("Shibuya: The Curse");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center window
        getContentPane().setBackground(new Color(30, 30, 30)); // dark grey

        // Main panels
        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel();
        leftPanel.setBackground(new Color(30, 30, 30));
        rightPanel.setBackground(new Color(30, 30, 30));
        bottomPanel.setBackground(new Color(30, 30, 30));

        // Left side: story + player stats
        JPanel topStatsPanel = new JPanel(new GridLayout(2, 2));
        topStatsPanel.setBackground(new Color(30, 30, 30));
        topStatsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        playerHealthBar = new JProgressBar(0, 100);
        playerHealthBar.setForeground(Color.RED);
        playerHealthBar.setStringPainted(true);
        playerManaBar = new JProgressBar(0, 50);
        playerManaBar.setForeground(Color.BLUE);
        playerManaBar.setStringPainted(true);

        JLabel healthLabel = new JLabel("Gojo Health:");
        JLabel manaLabel = new JLabel("Mana:");
        healthLabel.setForeground(Color.WHITE);
        manaLabel.setForeground(Color.WHITE);
        healthLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        manaLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        topStatsPanel.add(healthLabel);
        topStatsPanel.add(playerHealthBar);
        topStatsPanel.add(manaLabel);
        topStatsPanel.add(playerManaBar);

        storyArea = new JTextArea();
        storyArea.setEditable(false);
        storyArea.setWrapStyleWord(true);
        storyArea.setLineWrap(true);
        storyArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
        storyArea.setForeground(Color.WHITE);
        storyArea.setBackground(new Color(20, 20, 20));
        JScrollPane scrollPane = new JScrollPane(storyArea);

        leftPanel.add(topStatsPanel, BorderLayout.NORTH);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom buttons
        attackButton = createButton("Attack");
        spellButton = createButton("Cast Spell");
        manaButton = createButton("Concentrate (Mana)");

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

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(200, 30, 30));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setUI(new BasicButtonUI());
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addActionListener(this);
        return button;
    }

    private void startGame() {
        gojo = new CursedFighter("Gojo", 100, 30, 50);
        enemy = new CursedFighter("Fly Head", 40, 10, 40);
        updateBars();
        setCreatureImage("Fly Head");
        animatedText("You are Gojo, a powerful sorcerer protecting Shibuya.\n\nA Fly Head appears!\nPrepare for battle!");
    }

    private void updateBars() {
        playerHealthBar.setMaximum(gojo.maxHealth);
        playerHealthBar.setValue(gojo.health);
        playerHealthBar.setString(gojo.health + "/" + gojo.maxHealth + " HP");

        playerManaBar.setMaximum(gojo.maxMana);
        playerManaBar.setValue(gojo.mana);
        playerManaBar.setString(gojo.mana + "/" + gojo.maxMana + " MP");

        enemyHealthBar.setMaximum(enemy.maxHealth);
        enemyHealthBar.setValue(enemy.health);
        enemyHealthBar.setString(enemy.health + "/" + enemy.maxHealth + " HP");
    }

    private void animatedText(String newText) {
        if (typingTimer != null && typingTimer.isRunning()) {
            typingTimer.stop();
        }
    
        Timer delayTimer = new Timer(25, null);
        delayTimer.setRepeats(false);
        delayTimer.addActionListener(e -> {
            storyArea.append(newText + "\n\n");
            storyArea.setCaretPosition(storyArea.getDocument().getLength());
        });
        delayTimer.start();
    }
    

    private void nextQuest() {
        if (questStage == 1) {
            questStage++;
            animatedText("You defeated Fly Head!\n\nOpening a chest...");
            gojo.unlockSorcery("Healing");
            enemy = new CursedFighter("Roppongi Curse", 50, 20, 60);
            setCreatureImage("Roppongi Curse");
            animatedText("A stronger curse appears: Roppongi Curse!");
            updateBars();
        } else if (questStage == 2) {
            questStage++;
            animatedText("You defeated Roppongi Curse!\n\nApproaching the final battle...");
            gojo.unlockSorcery("Necromancy");
            enemy = new CursedFighter("Eso Spirit", 60, 35, 80);
            setCreatureImage("Eso Spirit");
            animatedText("A terrible force appears: Eso Spirit!");
            updateBars();
        } else {
            animatedText("Congratulations! You defeated all curses and saved Shibuya!");
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
                Image img = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                creatureImageLabel.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            creatureImageLabel.setText("Image Load Failed");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gojo.health <= 0) {
            animatedText("You have fallen. Shibuya is doomed...");
            attackButton.setEnabled(false);
            spellButton.setEnabled(false);
            manaButton.setEnabled(false);
            return;
        }

        if (e.getSource() == attackButton) {
            gojo.attack(enemy);
            animatedText("You attacked " + enemy.name + "!");
        } else if (e.getSource() == spellButton) {
            if (gojo.mana >= 10) {
                gojo.castSpell(enemy);
                animatedText("You cast a spell on " + enemy.name + "!");
            } else {
                animatedText("Not enough mana!");
                return;
            }
        } else if (e.getSource() == manaButton) {
            gojo.gainMana(15);
            animatedText("You concentrate and regain mana.");
        }

        if (enemy.health > 0) {
            enemy.attack(gojo);
            animatedText(enemy.name + " attacks back!");
        } else {
            nextQuest();
        }

        updateBars();
        enemyHealthBar.setValue(Math.max(enemy.health, 0));
    }

    public static void main(String[] args) {
        new ShibuyaGameGUI();
    }
}
