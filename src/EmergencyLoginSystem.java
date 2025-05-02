import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class EmergencyLoginSystem extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JCheckBox rememberMeCheckbox;
    private JLabel forgotPasswordLabel;
    private HashMap<String, String> userDatabase;

    public EmergencyLoginSystem(HashMap<String, String> userDatabase) {
        this.userDatabase = userDatabase;
        setTitle("交通事故紧急预警系统 - 登录");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        ImageIcon icon = new ImageIcon(getClass().getResource("/智慧模拟守护商标.png"));
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            JPanel leftPanel = new BackgroundPanel(icon.getImage());
            leftPanel.setPreferredSize(new Dimension(500, 500));
            mainPanel.add(leftPanel, BorderLayout.WEST);
        }

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.LINE_START;

        rightPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("登录");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        rightPanel.add(titleLabel, gbc);

        JPanel registrationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        registrationPanel.setOpaque(false);
        JLabel noAccountLabel = new JLabel("没有账号？");
        noAccountLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        registerButton = new JButton("注册");
        registerButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setFocusPainted(false);
        registerButton.setForeground(Color.BLUE);
        registerButton.addActionListener(e -> {
            this.dispose();
            new EmergencyRegistrationSystem(userDatabase).setVisible(true);
        });

        registrationPanel.add(noAccountLabel);
        registrationPanel.add(registerButton);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        rightPanel.add(registrationPanel, gbc);

        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        rightPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        rightPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        rightPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(30);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        rightPanel.add(passwordField, gbc);

        rememberMeCheckbox = new JCheckBox("记住我");
        rememberMeCheckbox.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        rightPanel.add(rememberMeCheckbox, gbc);

        forgotPasswordLabel = new JLabel("忘记密码?");
        forgotPasswordLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        forgotPasswordLabel.setForeground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        rightPanel.add(forgotPasswordLabel, gbc);

        loginButton = new JButton("登录");
        loginButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(200, 40));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (validateUser(username, password)) {
                    JOptionPane.showMessageDialog(EmergencyLoginSystem.this, "登录成功！");
                    dispose();
                    new MainDashboard().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(EmergencyLoginSystem.this, "用户名或密码错误", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rightPanel.add(loginButton, gbc);

        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(Image image) {
            this.backgroundImage = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private boolean validateUser(String username, String password) {
        // Check if the entered username and password are "123"
        if ("123".equals(username) && "123".equals(password)) {
            return true;  // Allow login directly for username "123" and password "123"
        }

        // For other users, proceed with normal hashing validation
        String storedHash = userDatabase.get(username);
        if (storedHash == null) {
            JOptionPane.showMessageDialog(this, "用户名不存在", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            String hashedPassword = hashPassword(password);
            System.out.println("输入的密码的哈希: " + hashedPassword);
            System.out.println("存储的哈希: " + storedHash);
            return hashedPassword.equals(storedHash);  // Compare the hashes
        } catch (NoSuchAlgorithmException ex) {
            JOptionPane.showMessageDialog(this, "加密失败", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HashMap<String, String> userDatabase = new HashMap<>();
            EmergencyLoginSystem system = new EmergencyLoginSystem(userDatabase);

            // Adding default user with username "123" and password "123"
            try {
                String defaultPassword = "123";  // The password
                String hashedPassword = system.hashPassword(defaultPassword);  // Hash the password
                userDatabase.put("123", hashedPassword);  // Store the hashed password in the userDatabase
            } catch (NoSuchAlgorithmException ex) {
                JOptionPane.showMessageDialog(null, "Error initializing default user", "Error", JOptionPane.ERROR_MESSAGE);
            }

            system.setVisible(true);
        });
    }

}



