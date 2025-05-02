import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;

public class MainDashboard extends JFrame {

    public MainDashboard() {
        setTitle("智慧模拟守护系统 - 主页面");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setUI(new CustomTabbedPaneUI());

        // 1) “主页” tab
        JLayeredPane dashboardPanel = createDashboardPanel();
        addCloseButton(tabbedPane, "主页", dashboardPanel);

        // 2) “系统智能助手” tab
        AssistantPanel assistantPanel = new AssistantPanel();
        addCloseButton(tabbedPane, "系统智能助手", assistantPanel.getPanel());

        // 3) “智慧实时导航” tab — use navigationPanel.getPanel()
        NavigationPanel navigationPanel = new NavigationPanel();
        addCloseButton(tabbedPane, "智慧实时导航", navigationPanel.getPanel());
        // 4) “实时路况3D模拟” tab
        JPanel trafficSimulationPanel = new JPanel(new BorderLayout());
        VideoPlayerPanel videoPanel = new VideoPlayerPanel("src/模拟路况.mp4");
        trafficSimulationPanel.add(videoPanel, BorderLayout.CENTER);
        JLabel trafficSimulationLabel = new JLabel("这里是实时路况3D模拟区域", SwingConstants.CENTER);
        trafficSimulationLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        trafficSimulationPanel.add(trafficSimulationLabel, BorderLayout.NORTH);
        addCloseButton(tabbedPane, "实时路况3D模拟", trafficSimulationPanel);

        // 5) “事故实时数据” tab
        AccidentDataPanel accidentDataPanel = new AccidentDataPanel();
        addCloseButton(tabbedPane, "事故实时数据", accidentDataPanel);

        // 6) “责任分析” tab
        JLayeredPane responsibilityAnalysisPanel = createResponsibilityAnalysisPanel();
        addCloseButton(tabbedPane, "责任分析", responsibilityAnalysisPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JLayeredPane createDashboardPanel() {
        return createBackgroundPanel("/主页2.jpg", "欢迎来到智慧模拟守护系统", Color.BLACK);
    }

    private JLayeredPane createResponsibilityAnalysisPanel() {
        return createBackgroundPanel("数据看板上交版3.png", "事故责任分析看板", Color.WHITE);
    }

    private JLayeredPane createBackgroundPanel(String imagePath, String text, Color textColor) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));
        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateBackground(layeredPane);
            }
        });

        ImageIcon backgroundImageIcon = new ImageIcon(MainDashboard.class.getResource(imagePath));
        if (backgroundImageIcon == null) {
            System.err.println("无法加载背景图片: " + imagePath);
        } else {
            BackgroundLabel backgroundLabel = new BackgroundLabel(backgroundImageIcon.getImage());
            backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
            layeredPane.add(backgroundLabel, Integer.valueOf(0));
        }

        JLabel textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setFont(new Font("微软雅黑", Font.BOLD, 36));
        textLabel.setForeground(textColor);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.add(textLabel, BorderLayout.NORTH);
        textPanel.setOpaque(false);
        textPanel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(textPanel, Integer.valueOf(1));

        return layeredPane;
    }

    private void updateBackground(JLayeredPane layeredPane) {
        for (Component comp : layeredPane.getComponents()) {
            if (comp instanceof BackgroundLabel) {
                ((BackgroundLabel) comp).setSize(layeredPane.getWidth(), layeredPane.getHeight());
                break;
            }
        }
    }

    private static class BackgroundLabel extends JLabel {
        private final Image image;
        public BackgroundLabel(Image image) {
            this.image = image;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                g2d.dispose();
            }
        }
    }

    private void addCloseButton(JTabbedPane tabbedPane, String title, JComponent component) {
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setOpaque(false);
        JLabel label = new JLabel(title);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        tabPanel.add(label, BorderLayout.CENTER);

        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);
        closeButton.setForeground(Color.RED);
        closeButton.addActionListener(e -> {
            int index = tabbedPane.indexOfComponent(component);
            if (index != -1) {
                tabbedPane.remove(index);
            }
        });

        tabPanel.add(closeButton, BorderLayout.EAST);
        tabbedPane.addTab("", null, component);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HashMap<String, String> userDatabase = new HashMap<>();
            EmergencyLoginSystem loginSystem = new EmergencyLoginSystem(userDatabase);
            loginSystem.setVisible(true);
        });
    }
}
