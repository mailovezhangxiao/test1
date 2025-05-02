import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
//数据存储
class AccidentDataPanel extends JPanel {
    private final JButton allButton;
    private final JButton privateButton;
    private final JButton publicButton;
    private final JButton uploadPrivateButton;
    private final JButton uploadPublicButton;
    private final JTextField searchField;
    private final JButton searchButton;
    private final JLabel imageLabel;
    private final JTextArea infoTextArea;

    public AccidentDataPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // 创建按钮
        allButton = new JButton("全部");
        privateButton = new JButton("私有");
        publicButton = new JButton("公开");
        uploadPrivateButton = new JButton("上传私有数据");
        uploadPublicButton = new JButton("上传公开数据");

        // 设置按钮样式
        Font buttonFont = new Font("微软雅黑", Font.BOLD, 14);
        allButton.setFont(buttonFont);
        privateButton.setFont(buttonFont);
        publicButton.setFont(buttonFont);
        uploadPrivateButton.setFont(buttonFont);
        uploadPublicButton.setFont(buttonFont);

        // 移除按钮边框、填充和焦点
        allButton.setBorderPainted(false);
        allButton.setContentAreaFilled(false);
        allButton.setFocusPainted(false);
        allButton.setForeground(Color.BLUE);

        privateButton.setBorderPainted(false);
        privateButton.setContentAreaFilled(false);
        privateButton.setFocusPainted(false);
        privateButton.setForeground(Color.BLUE);

        publicButton.setBorderPainted(false);
        publicButton.setContentAreaFilled(false);
        publicButton.setFocusPainted(false);
        publicButton.setForeground(Color.BLUE);

        uploadPrivateButton.setBorderPainted(false);
        uploadPrivateButton.setContentAreaFilled(false);
        uploadPrivateButton.setFocusPainted(false);
        uploadPrivateButton.setForeground(Color.BLUE);

        uploadPublicButton.setBorderPainted(false);
        uploadPublicButton.setContentAreaFilled(false);
        uploadPublicButton.setFocusPainted(false);
        uploadPublicButton.setForeground(Color.BLUE);

        // 添加按钮到按钮面板
        buttonPanel.add(allButton);
        buttonPanel.add(privateButton);
        buttonPanel.add(publicButton);
        buttonPanel.add(uploadPrivateButton);
        buttonPanel.add(uploadPublicButton);

        // 创建搜索面板
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // 创建文本框和搜索按钮
        searchField = new JTextField(20);
        searchButton = new JButton("搜索");
        searchButton.setFont(buttonFont);

        // 添加文本框和搜索按钮到搜索面板
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // 添加按钮面板和搜索面板到主面板的北边
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(buttonPanel, BorderLayout.NORTH);
        northPanel.add(searchPanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        // 创建图片标签
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        // 创建滚动文本区域
        infoTextArea = new JTextArea();
        infoTextArea.setEditable(false);
        infoTextArea.setLineWrap(true);
        infoTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        scrollPane.setPreferredSize(new Dimension(10, getHeight())); // 调整宽度
        customizeScrollBar(scrollPane.getVerticalScrollBar());

        // 添加图片标签到主面板的中心
        add(imageLabel, BorderLayout.CENTER);

        // 添加滚动文本区域到主面板的东边
        add(scrollPane, BorderLayout.EAST);

        // 添加按钮点击事件监听器
        allButton.addActionListener(new ButtonActionListener("/保存数据.png"));
        privateButton.addActionListener(new ButtonActionListener("/保存数据2.png"));
        publicButton.addActionListener(new ButtonActionListener("/保存数据3.png"));

        // 添加上传私有数据按钮点击事件监听器
        uploadPrivateButton.addActionListener(e -> showFileUploadDialog("上传私有数据"));

        // 添加上传公开数据按钮点击事件监听器
        uploadPublicButton.addActionListener(e -> showFileUploadDialog("上传公开数据"));

        // 添加搜索按钮点击事件监听器
        searchButton.addActionListener(e -> performSearch(searchField.getText()));
    }

    private class ButtonActionListener implements ActionListener {
        private final String imagePath;

        public ButtonActionListener(String imagePath) {
            this.imagePath = imagePath;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            displayImage(imagePath);
        }
    }

    private void displayImage(String imagePath) {
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(imagePath));
        if (imageIcon == null) {
            System.err.println("无法加载图片: " + imagePath);
            imageLabel.setIcon(null);
            imageLabel.setText("无法加载图片");
        } else {
            Image image = imageIcon.getImage();
            double scaleFactor = Math.min((double) getWidth() / image.getWidth(null), (double) getHeight() / image.getHeight(null));
            int scaledWidth = (int) (image.getWidth(null) * scaleFactor);
            int scaledHeight = (int) (image.getHeight(null) * scaleFactor);
            Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
            imageLabel.setIcon(scaledImageIcon);
            imageLabel.setText("");
        }
    }

    private void performSearch(String query) {
        // 模拟搜索操作
        System.out.println("搜索查询: " + query);
        // 根据查询结果显示相应的图片
        // 这里可以添加实际的搜索逻辑
        displayImage("/保存数据4.png"); // 显示保存数据4.png
        // 显示一些示例文本
        infoTextArea.setText("这是关于搜索查询 \"" + query + "\" 的相关信息。\n您可以在这里查看详细的信息或日志。");
    }

    private void showFileUploadDialog(String title) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            infoTextArea.append(title + ": 已选择文件 - " + selectedFile.getAbsolutePath() + "\n");
        }
    }

    private void customizeScrollBar(JScrollBar scrollBar) {
        scrollBar.setPreferredSize(new Dimension(10, 0)); // 设置滚动条宽度
        scrollBar.setBackground(Color.GRAY); // 设置背景颜色
        scrollBar.setForeground(Color.DARK_GRAY); // 设置前景颜色

        // 自定义滚动条轨道
        scrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                trackColor = Color.LIGHT_GRAY; // 轨道颜色
                thumbColor = Color.DARK_GRAY; // 滑块颜色
                thumbHighlightColor = Color.DARK_GRAY; // 滑块高亮颜色
                thumbDarkShadowColor = Color.DARK_GRAY; // 滑块暗影颜色
                thumbLightShadowColor = Color.DARK_GRAY; // 滑块亮色阴影
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle bounds) {
                g.setColor(trackColor);
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty()) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width - 1, thumbBounds.height - 1, 5, 5);
                g2.dispose();
            }

            @Override
            protected void paintDecreaseHighlight(Graphics g) {}

            @Override
            protected void paintIncreaseHighlight(Graphics g) {}
        });
    }
}



