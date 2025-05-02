import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
//注册
public class EmergencyRegistrationSystem extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton loginButton;
    private HashMap<String, String> userDatabase; // 用户数据库

    public EmergencyRegistrationSystem(HashMap<String, String> userDatabase) {
        this.userDatabase = userDatabase;
        setTitle("交通事故紧急预警系统 - 注册");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 主面板使用BorderLayout布局
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 左侧背景图片面板
        ImageIcon icon = new ImageIcon(getClass().getResource("/智慧模拟守护商标.png"));
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            JPanel leftPanel = new BackgroundPanel(icon.getImage());
            leftPanel.setPreferredSize(new Dimension(500, 500)); // 减少左侧宽度
            mainPanel.add(leftPanel, BorderLayout.WEST);
        } else {
            System.err.println("Failed to load background image.");
        }

        // 右侧注册表单面板
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.LINE_START; // 文本左对齐

        // 设置透明背景
        rightPanel.setOpaque(false);

        // 第一行：注册标题（加粗）
        JLabel titleLabel = new JLabel("注册");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        rightPanel.add(titleLabel, gbc);

        // 第二行：已有账户？登录
        JPanel accountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        accountPanel.setOpaque(false);
        JLabel existingAccountLabel = new JLabel("已有账户？");
        existingAccountLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        loginButton = new JButton("登录");
        loginButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setFocusPainted(false);
        loginButton.setForeground(Color.BLUE);
        loginButton.addActionListener(e -> {
            this.dispose(); // 关闭当前窗口
            new EmergencyLoginSystem(userDatabase).setVisible(true); // 打开登录窗口
        });

        accountPanel.add(existingAccountLabel);
        accountPanel.add(loginButton);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        rightPanel.add(accountPanel, gbc);

        // 第三行：新建用户名标签
        JLabel usernameLabel = new JLabel("新建用户名:");
        usernameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        rightPanel.add(usernameLabel, gbc);

        // 第四行：用户名文本框
        usernameField = new JTextField(30); // 增加文本框宽度
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        rightPanel.add(usernameField, gbc);

        // 第五行：密码标签
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        rightPanel.add(passwordLabel, gbc);

        // 第六行：密码文本框
        passwordField = new JPasswordField(30); // 增加文本框宽度
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        rightPanel.add(passwordField, gbc);

        // 第七行：确认密码标签
        JLabel confirmPasswordLabel = new JLabel("确认密码:");
        confirmPasswordLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        rightPanel.add(confirmPasswordLabel, gbc);

        // 第八行：确认密码文本框
        confirmPasswordField = new JPasswordField(30); // 增加文本框宽度
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        rightPanel.add(confirmPasswordField, gbc);

        // 第九行：创建账户按钮
        registerButton = new JButton("创建账户");
        registerButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        registerButton.setPreferredSize(new Dimension(200, 40)); // 增加按钮尺寸
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(EmergencyRegistrationSystem.this, "密码不一致", "错误", JOptionPane.ERROR_MESSAGE);
                } else if (userDatabase.containsKey(username)) {
                    JOptionPane.showMessageDialog(EmergencyRegistrationSystem.this, "用户名已存在", "错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        String hashedPassword = hashPassword(password);
                        userDatabase.put(username, hashedPassword);
                        JOptionPane.showMessageDialog(EmergencyRegistrationSystem.this, "账户创建成功！");
                        dispose(); // 关闭当前窗口
                        new EmergencyLoginSystem(userDatabase).setVisible(true); // 打开登录窗口
                    } catch (NoSuchAlgorithmException ex) {
                        JOptionPane.showMessageDialog(EmergencyRegistrationSystem.this, "加密失败", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rightPanel.add(registerButton, gbc);

        mainPanel.add(rightPanel, BorderLayout.CENTER); // 使用CENTER以增加宽度

        add(mainPanel);
    }

    // 自定义面板类用于绘制背景图片
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

    // 密码哈希方法
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
            EmergencyRegistrationSystem system = new EmergencyRegistrationSystem(userDatabase);
            system.setVisible(true);
        });
    }
}





//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
////注册页
//public class EmergencyRegistrationSystem extends JFrame {
//    private JTextField usernameField;
//    private JPasswordField passwordField;
//    private JPasswordField confirmPasswordField;
//    private JButton registerButton;
//    private JButton loginButton;
//
//    public EmergencyRegistrationSystem() {
//        setTitle("交通事故紧急预警系统 - 注册");
//        setSize(1200, 800);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        // 主面板使用BorderLayout布局
//        JPanel mainPanel = new JPanel(new BorderLayout());
//
//        // 左侧背景图片面板
//        ImageIcon icon = new ImageIcon(getClass().getResource("/智慧模拟守护商标.png"));
//        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
//            JPanel leftPanel = new BackgroundPanel(icon.getImage());
//            leftPanel.setPreferredSize(new Dimension(500, 500)); // 减少左侧宽度
//            mainPanel.add(leftPanel, BorderLayout.WEST);
//        } else {
//            System.err.println("Failed to load background image.");
//        }
//
//        // 右侧注册表单面板
//        JPanel rightPanel = new JPanel();
//        rightPanel.setLayout(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(20, 20, 20, 20);
//        gbc.anchor = GridBagConstraints.LINE_START; // 文本左对齐
//
//        // 设置透明背景
//        rightPanel.setOpaque(false);
//
//        // 第一行：注册标题（加粗）
//        JLabel titleLabel = new JLabel("注册");
//        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.gridwidth = 2;
//        rightPanel.add(titleLabel, gbc);
//
//        // 第二行：已有账户？登录
//        JPanel accountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        accountPanel.setOpaque(false);
//        JLabel existingAccountLabel = new JLabel("已有账户？");
//        existingAccountLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
//        loginButton = new JButton("登录");
//        loginButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
//        loginButton.setBorderPainted(false);
//        loginButton.setContentAreaFilled(false);
//        loginButton.setFocusPainted(false);
//        loginButton.setForeground(Color.BLUE);
//        loginButton.addActionListener(e -> {
//            this.dispose(); // 关闭当前窗口
//            new EmergencyLoginSystem().setVisible(true); // 打开登录窗口
//        });
//
//        accountPanel.add(existingAccountLabel);
//        accountPanel.add(loginButton);
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        gbc.gridwidth = 2;
//        rightPanel.add(accountPanel, gbc);
//
//        // 第三行：新建用户名标签
//        JLabel usernameLabel = new JLabel("新建用户名:");
//        usernameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        gbc.gridwidth = 1;
//        rightPanel.add(usernameLabel, gbc);
//
//        // 第四行：用户名文本框
//        usernameField = new JTextField(30); // 增加文本框宽度
//        gbc.gridx = 1;
//        gbc.gridy = 2;
//        gbc.gridwidth = 1;
//        rightPanel.add(usernameField, gbc);
//
//        // 第五行：密码标签
//        JLabel passwordLabel = new JLabel("密码:");
//        passwordLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        gbc.gridwidth = 1;
//        rightPanel.add(passwordLabel, gbc);
//
//        // 第六行：密码文本框
//        passwordField = new JPasswordField(30); // 增加文本框宽度
//        gbc.gridx = 1;
//        gbc.gridy = 3;
//        gbc.gridwidth = 1;
//        rightPanel.add(passwordField, gbc);
//
//        // 第七行：确认密码标签
//        JLabel confirmPasswordLabel = new JLabel("确认密码:");
//        confirmPasswordLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
//        gbc.gridx = 0;
//        gbc.gridy = 4;
//        gbc.gridwidth = 1;
//        rightPanel.add(confirmPasswordLabel, gbc);
//
//        // 第八行：确认密码文本框
//        confirmPasswordField = new JPasswordField(30); // 增加文本框宽度
//        gbc.gridx = 1;
//        gbc.gridy = 4;
//        gbc.gridwidth = 1;
//        rightPanel.add(confirmPasswordField, gbc);
//
//        // 第九行：创建账户按钮
//        registerButton = new JButton("创建账户");
//        registerButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
//        registerButton.setPreferredSize(new Dimension(200, 40)); // 增加按钮尺寸
//        registerButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String password = new String(passwordField.getPassword());
//                String confirmPassword = new String(confirmPasswordField.getPassword());
//                if (!password.equals(confirmPassword)) {
//                    JOptionPane.showMessageDialog(EmergencyRegistrationSystem.this, "密码不一致", "错误", JOptionPane.ERROR_MESSAGE);
//                } else {
//                    try {
//                        String hashedPassword = hashPassword(password);
//                        JOptionPane.showMessageDialog(EmergencyRegistrationSystem.this, "账户创建成功！\nHashed Password: " + hashedPassword);
//                        // 这里可以添加实际的注册逻辑，例如保存hashedPassword到数据库
//                    } catch (NoSuchAlgorithmException ex) {
//                        JOptionPane.showMessageDialog(EmergencyRegistrationSystem.this, "加密失败", "错误", JOptionPane.ERROR_MESSAGE);
//                    }
//                }
//            }
//        });
//        gbc.gridx = 0;
//        gbc.gridy = 5;
//        gbc.gridwidth = 2;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        rightPanel.add(registerButton, gbc);
//
//        mainPanel.add(rightPanel, BorderLayout.CENTER); // 使用CENTER以增加宽度
//
//        add(mainPanel);
//    }
//
//    // 自定义面板类用于绘制背景图片
//    private class BackgroundPanel extends JPanel {
//        private Image backgroundImage;
//
//        public BackgroundPanel(Image image) {
//            this.backgroundImage = image;
//        }
//
//        @Override
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
//        }
//    }
//
//    // 密码哈希方法
//    private String hashPassword(String password) throws NoSuchAlgorithmException {
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        byte[] hashBytes = md.digest(password.getBytes());
//        StringBuilder sb = new StringBuilder();
//        for (byte b : hashBytes) {
//            sb.append(String.format("%02x", b));
//        }
//        return sb.toString();
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            EmergencyRegistrationSystem system = new EmergencyRegistrationSystem();
//            system.setVisible(true);
//        });
//    }
//}









