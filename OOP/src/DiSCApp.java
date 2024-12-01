import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class DiSCApp extends JFrame {

    private static final int NUM_QUESTIONS = 5;
    private int[] dominanceScores = new int[NUM_QUESTIONS];
    private int[] influenceScores = new int[NUM_QUESTIONS];
    private int[] steadinessScores = new int[NUM_QUESTIONS];
    private int[] complianceScores = new int[NUM_QUESTIONS];

    private JPanel mainPanel;
    private CardLayout cardLayout;

    private JLabel dominanceResult;
    private JLabel influenceResult;
    private JLabel steadinessResult;
    private JLabel complianceResult;
    private JLabel highestScoreLabel;

    public DiSCApp() {
        setTitle("DiSC Personality Test");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createIntroductionPanel(), "Introduction");
        mainPanel.add(createDominancePanel(), "Dominance");
        mainPanel.add(createInfluencePanel(), "Influence");
        mainPanel.add(createSteadinessPanel(), "Steadiness");
        mainPanel.add(createCompliancePanel(), "Compliance");
        mainPanel.add(createResultsPanel(), "Results");

        add(mainPanel);
        cardLayout.show(mainPanel, "Introduction");

        setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 14));
    }

    private void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }

    private JPanel createIntroductionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Load the GIF and set it as a background
        JLabel backgroundLabel = new JLabel(new ImageIcon("Welcome to.gif"));
        panel.add(backgroundLabel);
        backgroundLabel.setLayout(new BorderLayout());

        // Start button
        JButton startButton = createStyledButton("Start Test");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Dominance");
            }
        });
        backgroundLabel.add(startButton, BorderLayout.SOUTH);

        return panel;
    }


    private JPanel createDominancePanel() {
        return createQuestionPanel(
            new String[]{
                "1. Do you enjoy competition and challenge?",
                "2. Are you goal-oriented and want to be recognized for your efforts?",
                "3. Do you aim high, want authority, and are generally resourceful and adaptable?",
                "4. Are you usually self-sufficient and individualistic?",
                "5. Do you lose interest in projects once the challenge is gone and feel impatient and dissatisfied with minor details?"
            },
            dominanceScores,
            "Introduction",
            "Influence",
            "Welcome to (4).gif" // Path to the Dominance GIF
        );
    }

    private JPanel createInfluencePanel() {
        return createQuestionPanel(
            new String[]{
                "1. Do you enjoy socializing and meeting new people?",
                "2. Do you like to persuade and inspire others?",
                "3. Are you enthusiastic and optimistic?",
                "4. Do you prefer working in a team rather than alone?",
                "5. Do you seek approval and recognition from others?"
            },
            influenceScores,
            "Dominance",
            "Steadiness",
            "Welcome to (5).gif"
        );
    }

    private JPanel createSteadinessPanel() {
        return createQuestionPanel(
            new String[]{
                "1. Do you prefer stability and routine over change?",
                "2. Are you a good listener and empathetic towards others?",
                "3. Do you value loyalty and long-term relationships?",
                "4. Are you patient and calm under pressure?",
                "5. Do you prefer a steady pace rather than a fast-paced environment?"
            },
            steadinessScores,
            "Influence",
            "Compliance",
            "Welcome to (6).gif"
        );
    }

    private JPanel createCompliancePanel() {
        return createQuestionPanel(
            new String[]{
                "1. Do you prefer to follow rules and standards?",
                "2. Are you detail-oriented and meticulous?",
                "3. Do you value accuracy and precision?",
                "4. Are you comfortable working in structured environments?",
                "5. Do you prefer analysis and data over opinions?"
            },
            complianceScores,
            "Steadiness",
            "Results",
            "Welcome to (7).gif"
        );
    }

    private JPanel createQuestionPanel(String[] questions, int[] scoresArray, String prevPanel, String nextPanel, String gifPath) {
        BackgroundPanel backgroundPanel = new BackgroundPanel(gifPath);
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel questionsPanel = new JPanel();
        questionsPanel.setOpaque(false); // Make the panel transparent to show the GIF background
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));

        for (int i = 0; i < NUM_QUESTIONS; i++) {
            questionsPanel.add(createSingleQuestionPanel(questions[i], scoresArray, i));
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false); // Make the panel transparent to show the GIF background

        if (!prevPanel.equals("Introduction")) {
            JButton backButton = createStyledButton("Back");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(mainPanel, prevPanel);
                }
            });
            buttonPanel.add(backButton);
        }

        JButton nextButton = createStyledButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nextPanel.equals("Results")) {
                    int dominanceTotal = calculateTotalScore(dominanceScores);
                    int influenceTotal = calculateTotalScore(influenceScores);
                    int steadinessTotal = calculateTotalScore(steadinessScores);
                    int complianceTotal = calculateTotalScore(complianceScores);

                    dominanceResult.setText("Dominance: " + dominanceTotal);
                    influenceResult.setText("Influence: " + influenceTotal);
                    steadinessResult.setText("Steadiness: " + steadinessTotal);
                    complianceResult.setText("Compliance: " + complianceTotal);

                    cardLayout.show(mainPanel, nextPanel);
                } else {
                    cardLayout.show(mainPanel, nextPanel);
                }
            }
        });
        buttonPanel.add(nextButton);

        // Use BorderLayout to place questionsPanel at CENTER and buttonPanel at SOUTH
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setOpaque(false); // Make the panel transparent to show the GIF background
        containerPanel.add(questionsPanel, BorderLayout.CENTER);
        containerPanel.add(buttonPanel, BorderLayout.SOUTH);

        backgroundPanel.add(containerPanel, BorderLayout.CENTER);

        return backgroundPanel;
    }

    private JPanel createSingleQuestionPanel(String question, int[] scoresArray, int questionIndex) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.setOpaque(false); // Ensure the main panel is also transparent

        // Create a transparent question panel
        JPanel questionPanel = new JPanel();
        questionPanel.setOpaque(false); // Make the panel transparent

        // Use HTML to ensure the text wraps
        JLabel questionLabel = new JLabel("<html>" + question + "</html>");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionLabel.setForeground(Color.WHITE); // Ensure text is visible over the GIF
        questionLabel.setOpaque(false);
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(questionLabel, BorderLayout.NORTH);

        // Create a transparent options panel
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        optionsPanel.setOpaque(false);

        ButtonGroup group = new ButtonGroup();
        JRadioButton[] options = new JRadioButton[5];

        ActionListener radioListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JRadioButton selectedButton = (JRadioButton) e.getSource();
                int selectedScore = Integer.parseInt(selectedButton.getText());
                scoresArray[questionIndex] = selectedScore;
            }
        };
        

        for (int i = 0; i < 5; i++) {
            options[i] = new JRadioButton(Integer.toString(i + 1));
            options[i].addActionListener(radioListener);
            options[i].setFont(new Font("Arial", Font.BOLD, 14));
            options[i].setFocusPainted(false);
            options[i].setOpaque(false);
            options[i].setBackground(new Color(255, 255, 255, 250));// Make radio buttons transparent
            options[i].setForeground(Color.BLACK); // Set text color for visibility
            //options[i].setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            group.add(options[i]);
            optionsPanel.add(options[i]);
        }

        panel.add(questionPanel, BorderLayout.CENTER);
        panel.add(optionsPanel, BorderLayout.SOUTH);
        
        return panel;
    }


    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Create a JLayeredPane to hold the background and the content
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 800));

        // Load the GIF and set it as a background
        JLabel backgroundLabel = new JLabel(new ImageIcon("Welcome to (8).gif"));
        backgroundLabel.setBounds(0, 0, 600, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // Create the results container with BorderLayout
        JPanel resultsContainer = new JPanel(new BorderLayout());
        resultsContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        resultsContainer.setOpaque(false); // Make it transparent to show the background
        resultsContainer.setBounds(-90, 320, 800, 500); // Adjust size and position

        // Create the results panel with GridBagLayout
        JPanel resultsPanel = new JPanel(new GridBagLayout());
        resultsPanel.setOpaque(false); // Make it transparent to show the background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(5, 0, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        dominanceResult = new JLabel("Dominance: ");
        influenceResult = new JLabel("Influence: ");
        steadinessResult = new JLabel("Steadiness: ");
        complianceResult = new JLabel("Compliance: ");
        highestScoreLabel = new JLabel("Highest Trait: ");

        dominanceResult.setFont(new Font("Arial", Font.BOLD, 16));
        influenceResult.setFont(new Font("Arial", Font.BOLD, 16));
        steadinessResult.setFont(new Font("Arial", Font.BOLD, 16));
        complianceResult.setFont(new Font("Arial", Font.BOLD, 16));
        highestScoreLabel.setFont(new Font("Arial", Font.BOLD, 20));

        resultsPanel.add(dominanceResult, gbc);
        resultsPanel.add(influenceResult, gbc);
        resultsPanel.add(steadinessResult, gbc);
        resultsPanel.add(complianceResult, gbc);
        resultsPanel.add(highestScoreLabel, gbc);

        resultsContainer.add(resultsPanel, BorderLayout.CENTER);
        layeredPane.add(resultsContainer, JLayeredPane.PALETTE_LAYER);

        // Refresh button to refresh the panel
        JButton refreshButton = createStyledButton("Result");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dominanceTotal = calculateTotalScore(dominanceScores);
                int influenceTotal = calculateTotalScore(influenceScores);
                int steadinessTotal = calculateTotalScore(steadinessScores);
                int complianceTotal = calculateTotalScore(complianceScores);

                String highestTrait = "Dominance";  // Default
                int highestScore = dominanceTotal;

                if (influenceTotal > highestScore) {
                    highestTrait = "Influence";
                    highestScore = influenceTotal;
                }
                if (steadinessTotal > highestScore) {
                    highestTrait = "Steadiness";
                    highestScore = steadinessTotal;
                }
                if (complianceTotal > highestScore) {
                    highestTrait = "Compliance";
                    highestScore = complianceTotal;
                }

                highestScoreLabel.setText("Highest Trait: " + highestTrait);
                highestScoreLabel.revalidate();
                highestScoreLabel.repaint();
            }
        });

        // Create the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false); // Make it transparent to show the background
        buttonPanel.setBounds(-80, 700, 760, 40); // Adjust size and position

        buttonPanel.add(refreshButton);

        JButton nextButton = createStyledButton("Continue to Know Your Trait");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dominanceTotal = calculateTotalScore(dominanceScores);
                int influenceTotal = calculateTotalScore(influenceScores);
                int steadinessTotal = calculateTotalScore(steadinessScores);
                int complianceTotal = calculateTotalScore(complianceScores);

                String highestTrait = "Dominance";  // Default
                int highestScore = dominanceTotal;

                if (influenceTotal > highestScore) {
                    highestTrait = "Influence";
                    highestScore = influenceTotal;
                }
                if (steadinessTotal > highestScore) {
                    highestTrait = "Steadiness";
                    highestScore = steadinessTotal;
                }
                if (complianceTotal > highestScore) {
                    highestTrait = "Compliance";
                    highestScore = complianceTotal;
                }

                highestScoreLabel.setText("Highest Trait: " + highestTrait);
                highestScoreLabel.revalidate();
                highestScoreLabel.repaint();

                // Switch to the results panel first to ensure highestTrait is displayed
                cardLayout.show(mainPanel, "Results");

                // Then switch to the corresponding personality panel
                cardLayout.show(mainPanel, highestTrait + "Personality1");
            }
        });
        buttonPanel.add(nextButton);
        layeredPane.add(buttonPanel, JLayeredPane.PALETTE_LAYER);

        panel.add(layeredPane, BorderLayout.CENTER);

        // Add all personality panels to the main panel
        addPersonalityPanels();

        return panel;
    }



    private void addPersonalityPanels() {
        String[] personalityTypes = {"Dominance", "Influence", "Steadiness", "Compliance"};
        for (String personalityType : personalityTypes) {
            for (int i = 1; i <= 4; i++) {
                mainPanel.add(createPersonalityPanel(personalityType, i), personalityType + "Personality" + i);
            }
        }
    }

    public JPanel createPersonalityPanel(String personalityType, int panelNumber) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);  // Using null layout for absolute positioning

        JLabel gifLabel = new JLabel();
        JLabel headlineLabel = new JLabel();
        JLabel paragraphLabel = new JLabel();

        String gifFilePath = "";
        String backgroundGifPath = "";  // Variable to hold the background GIF path

        switch (personalityType) {
            case "Dominance":
                backgroundGifPath = "Welcome to (9).gif";  // Set background GIF path for Dominance
                switch (panelNumber) {
                    case 1:
                        gifFilePath = "dominance1.gif";
                        headlineLabel.setText("What kind of person are they?");
                        paragraphLabel.setText("<html>Dominance personalities are driven, decisive, and results-oriented. They are often natural leaders who enjoy taking charge and overcoming challenges. They are direct in their communication, preferring to get straight to the point and make things happen.</html>");
                        break;
                    case 2:
                        gifFilePath = "dominance2.gif";
                        headlineLabel.setText("How do they interact with others?");
                        paragraphLabel.setText("<html>Dominance are assertive and confident in their interactions. They can be seen as forceful and may sometimes overlook the feelings of others in pursuit of their goals. They value competence and efficiency in others and can be impatient with those they perceive as slow or indecisive.</html>");
                        break;
                    case 3:
                        gifFilePath = "dominance3.gif";
                        headlineLabel.setText(" Strengths and Weaknesses?");
                        paragraphLabel.setText("<html>Dominance are excellent at taking initiative, making quick decisions, and getting things done. Their drive and determination can be inspiring. However, they can also be overly competitive, impatient, and insensitive to the needs of others. They may struggle with collaboration and may need to develop their listening skills.</html>");
                        break;
                    case 4:
                        gifFilePath = "dominance4.gif";
                        headlineLabel.setText("              In Summary?       ");
                        paragraphLabel.setText("<html>Dominance are natural leaders and problem solvers. To thrive, they should focus on developing their emotional intelligence, learning to collaborate effectively, and listening more actively to the perspectives of others.</html>");
                        break;
                }
                break;
            case "Influence":
                backgroundGifPath = "Welcome to (10).gif";  // Set background GIF path for Influence
                switch (panelNumber) {
                    case 1:
                        gifFilePath = "influence 1.gif";
                        headlineLabel.setText("What kind of person are they?");
                        paragraphLabel.setText("<html>Influence are enthusiastic, optimistic, and outgoing. They are often the life of the party and enjoy connecting with others. They are persuasive communicators who are skilled at building rapport and generating excitement.</html>");
                        break;
                    case 2:
                        gifFilePath = "influence 2.gif";
                        headlineLabel.setText("How do they interact with others?");
                        paragraphLabel.setText("<html>Influence are friendly, engaging, and charismatic. They are often the center of attention and enjoy entertaining others. They may sometimes be overly optimistic or talkative, and may need to be mindful of listening to others.</html>");
                        break;
                    case 3:
                        gifFilePath = "influence 3.gif";
                        headlineLabel.setText(" Strengths and Weaknesses?");
                        paragraphLabel.setText("<html>Influence are excellent at building relationships, motivating others, and generating enthusiasm. Their positive energy and charisma can be infectious. However, they can also be impulsive, disorganized, and overly optimistic. They may need to develop their follow-through and attention to detail.</html>");
                        break;
                    case 4:
                        gifFilePath = "influence 4.gif";
                        headlineLabel.setText("              In Summary?       ");
                        paragraphLabel.setText("<html>Influence are natural motivators and relationship builders. To thrive, they should focus on developing their organizational skills, follow-through, and ability to listen attentively to others.</html>");
                        break;
                }
                break;
            case "Steadiness":
                backgroundGifPath = "Welcome to (12).gif";  // Set background GIF path for Steadiness
                switch (panelNumber) {
                    case 1:
                        gifFilePath = "steadiness1.gif";
                        headlineLabel.setText("What kind of person are they?");
                        paragraphLabel.setText("<html>Steadiness are patient, supportive, and reliable. They value harmony and stability and are often the glue that holds teams and families together. They are good listeners who are empathetic and compassionate towards others.</html>");
                        break;
                    case 2:
                        gifFilePath = "steadiness2.gif";
                        headlineLabel.setText("How do they interact with others?");
                        paragraphLabel.setText("<html>Steadiness are friendly, approachable, and easy to get along with. They are often the peacemakers in groups and are skilled at resolving conflict. They may sometimes be overly accommodating or hesitant to express their own needs.</html>");
                        break;
                    case 3:
                        gifFilePath = "steadiness3.gif";
                        headlineLabel.setText(" Strengths and Weaknesses?");
                        paragraphLabel.setText("<html>Steadiness are excellent at building trust, creating a sense of stability, and supporting others. Their patience and empathy make them valued team members. However, they can also be indecisive, overly accommodating, and resistant to change. They may need to develop their assertiveness and willingness to take risks.</html>");
                        break;
                    case 4:
                        gifFilePath = "steadiness4.gif";
                        headlineLabel.setText("              In Summary?       ");
                        paragraphLabel.setText("<html>Steadiness are natural team players and peacemakers. To thrive, they should focus on developing their assertiveness, decision-making skills, and ability to embrace change.</html>");
                        break;
                }
                break;
            case "Compliance":
                backgroundGifPath = "Welcome to (13).gif";  // Set background GIF path for Compliance
                switch (panelNumber) {
                    case 1:
                        gifFilePath = "compliance1.gif";
                        headlineLabel.setText("What kind of person are they?");
                        paragraphLabel.setText("<html>Compliance are detail-oriented, analytical, and conscientious. They value accuracy and precision and are often the ones who ensure that tasks are done correctly. They are systematic thinkers who are skilled at analyzing data and solving problems.</html>");
                        break;
                    case 2:
                        gifFilePath = "compliance2.gif";
                        headlineLabel.setText("How do they interact with others?");
                        paragraphLabel.setText("<html>Compliance are reserved, thoughtful, and diplomatic. They may prefer to observe and analyze before offering their opinions. They can be seen as critical or perfectionistic, and may need to be mindful of communicating their feedback constructively.</html>");
                        break;
                    case 3:
                        gifFilePath = "compliance3.gif";
                        headlineLabel.setText(" Strengths and Weaknesses?");
                        paragraphLabel.setText("<html>Compliance are excellent at analyzing data, solving problems, and ensuring accuracy. Their attention to detail and thoroughness make them valuable assets. However, they can also be overly critical, perfectionistic, and resistant to taking risks. They may need to develop their flexibility and ability to embrace ambiguity.</html>");
                        break;
                    case 4:
                        gifFilePath = "compliance4.gif";
                        headlineLabel.setText("              In Summary?       ");
                        paragraphLabel.setText("<html>Compliance are natural problem solvers and analysts. To thrive, they should focus on developing their communication skills, flexibility, and ability to take calculated risks.</html>");
                        break;
                }
                break;
            default:
                headlineLabel.setText(personalityType + "headline text" + panelNumber);
                paragraphLabel.setText(personalityType + "paragraph" + panelNumber);
                break;
        }

     // Add the background GIF
        ImageIcon backgroundIcon = new ImageIcon(backgroundGifPath);
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, 600, 800); // Adjust the size to cover the whole panel
        layeredPane.add(backgroundLabel, JLayeredPane.FRAME_CONTENT_LAYER);

        ImageIcon gifIcon = new ImageIcon(gifFilePath);

        // Determine desired dimensions
        int desiredWidth = 550;  // Adjust as necessary
        int desiredHeight = 300; // Adjust as necessary

        // Create scaled image
        Image scaledImage = gifIcon.getImage().getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Display scaled image on a JLabel
        JLabel gifLabel1 = new JLabel(scaledIcon);
        gifLabel1.setBounds((580 - desiredWidth) / 2, 80, desiredWidth, desiredHeight);  // Center horizontally

        // Customize headlineLabel font, size, and position
        headlineLabel.setFont(new Font("Arial", Font.BOLD, 18));  // Example font and size
        headlineLabel.setBounds(155, 130, 400, 600);  // Example position

        // Customize paragraphLabel font, size, and position
        paragraphLabel.setFont(new Font("Arial", Font.PLAIN, 14));  // Example font and size
        paragraphLabel.setBounds(100, 120, 400, 800);  // Example position

        // Add components in reverse order to ensure the background is at the back
        layeredPane.add(gifLabel1, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(headlineLabel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(paragraphLabel, JLayeredPane.PALETTE_LAYER);

        layeredPane.setPreferredSize(new Dimension(600, 800)); // Set preferred size

        panel.add(layeredPane, BorderLayout.CENTER);

        JButton nextButton = createStyledButton("Next");
        nextButton.setBounds(240, 700, 80, 30);
        
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nextPanel = personalityType + "Personality" + (panelNumber + 1);
                if (panelNumber == 4) {
                    nextPanel = "Introduction";
                }
                cardLayout.show(mainPanel, nextPanel);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(nextButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Summary panel with "Retest" and "Quit" buttons
        if (panelNumber == 4) {
            JButton retestButton = createStyledButton("Re-take");
            retestButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(mainPanel, "Introduction");
                }
            });

            JButton quitButton = createStyledButton("Quit");
            quitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0); // Exit the application
                }
            });

            JPanel summaryButtonPanel = new JPanel(new GridLayout(1, 2, 10, 0)); // 1 row, 2 columns, horizontal gap of 10
            summaryButtonPanel.setBounds(240, 700, 80, 30); // Example position (adjust as needed)
            summaryButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Adjust padding as needed
            summaryButtonPanel.add(retestButton);
            summaryButtonPanel.add(quitButton);

            // Add summary button panel to layered pane
            layeredPane.add(summaryButtonPanel, JLayeredPane.PALETTE_LAYER);

            // Add summary button panel to main panel
            panel.add(summaryButtonPanel, BorderLayout.SOUTH);
        }


        return panel;
    }







    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false);
        return button;
    }

    private int calculateTotalScore(int[] scores) {
        int total = 0;
        for (int score : scores) {
            total += score;
        }
        return total;
    }
    
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(imagePath).getImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
    private void resetScores() {
        for (int i = 0; i < NUM_QUESTIONS; i++) {
            dominanceScores[i] = 0;
            influenceScores[i] = 0;
            steadinessScores[i] = 0;
            complianceScores[i] = 0;
        }
        dominanceResult.setText("Dominance: ");
        influenceResult.setText("Influence: ");
        steadinessResult.setText("Steadiness: ");
        complianceResult.setText("Compliance: ");
        highestScoreLabel.setText("Highest Trait: ");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DiSCApp().setVisible(true);
            }
        });
    }
}
