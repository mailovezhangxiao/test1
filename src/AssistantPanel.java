import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;

import static java.nio.charset.StandardCharsets.UTF_8;

class AssistantPanel {
    private final JPanel panel;
    private final JPanel chatPanel;
    private final JTextField inputField;
    private BufferedImage userAvatar;
    private BufferedImage aiAvatar;
    private final ExecutorService executorService;

    // API credentials and endpoint
    private static final String API_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions"; // DeepSeek R1 API Endpoint
    private static final String APP_ID = "72cc53d526384cdf80a951838a3cd193"; // Your Application ID
    private static final String API_KEY = "sk-e48518e2c71b49be96cef42e0f2dc8bb";

    public AssistantPanel() {
        panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Create chat panel and scrollable area
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Set a wider input field
        inputField = new JTextField();
        inputField.setFont(new Font("微软雅黑", Font.PLAIN, 16)); // Larger font size
        inputField.setPreferredSize(new Dimension(3000, 30)); // Increased width
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Send button with better alignment
        JButton sendButton = new JButton("发送");
        sendButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        sendButton.setBackground(new Color(51, 153, 255));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBorderPainted(false);
        sendButton.setFocusPainted(false);
        sendButton.addActionListener(e -> sendMessage());

        // Layout for input panel (adjusted to improve alignment with button)
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.SOUTH);

        // Load avatars
        try {
            userAvatar = ImageIO.read(getClass().getResource("/哆啦A梦.jpg"));
            aiAvatar = ImageIO.read(getClass().getResource("/智慧模拟守护商标.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("无法加载头像图片: " + e.getMessage());
        }

        addMessage("系统智能助手", "你好！有什么可以帮助你的？");

        executorService = Executors.newSingleThreadExecutor();
    }

    private void addMessage(String sender, String content) {
        JPanel messagePanel = new JPanel(new FlowLayout(sender.equals("用户") ? FlowLayout.RIGHT : FlowLayout.LEFT, 10, 0));
        messagePanel.setOpaque(false);

        // Use JTextArea for better word wrapping
        JTextArea messageArea = new JTextArea(content);
        messageArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        messageArea.setWrapStyleWord(true); // Word wrapping
        messageArea.setLineWrap(true); // Enable line wrap
        messageArea.setOpaque(true);
        messageArea.setBackground(sender.equals("用户") ? new Color(51, 153, 255) : Color.LIGHT_GRAY);
        messageArea.setForeground(Color.WHITE);
        messageArea.setEditable(false);
        messageArea.setCaretPosition(0);
        messageArea.setMaximumSize(new Dimension(3000, Integer.MAX_VALUE)); // Limit the width to avoid stretching

        // Add avatar icon for the sender
        BufferedImage currentAvatar = sender.equals("用户") ? userAvatar : aiAvatar;
        JLabel avatarLabel = new JLabel();
        if (currentAvatar != null) {
            Image scaledAvatar = currentAvatar.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledAvatar);
            avatarLabel.setIcon(icon);
        }

        // Align message and avatar based on sender
        if (sender.equals("用户")) {
            messagePanel.add(messageArea);
            messagePanel.add(avatarLabel);
        } else {
            messagePanel.add(avatarLabel);
            messagePanel.add(messageArea);
        }

        chatPanel.add(messagePanel);
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            addMessage("用户", message);
            inputField.setText(""); // Clear the input field after sending
            executorService.submit(() -> {
                try {
                    Thread.sleep(1000); // Simulate AI delay
                    String response = callApi(message); // Call the API to get a response
                    SwingUtilities.invokeLater(() -> addMessage("系统智能助手", response));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private String callApi(String userInput) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(API_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Authorization", "Bearer " + API_KEY); // 添加认证头
            urlConnection.setRequestProperty("X-DashScope-AppId", APP_ID); // 添加App ID头
            urlConnection.setDoOutput(true);

            String jsonInputString = buildJsonRequest(userInput);

            try (OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            } else {
                // 读取错误流信息
                InputStream errorStream = urlConnection.getErrorStream();
                if (errorStream != null) {
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
                    String errorLine;
                    StringBuilder errorResponse = new StringBuilder();
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorResponse.append(errorLine);
                    }
                    return "API请求失败 (" + responseCode + "): " + errorResponse.toString();
                }
                return "API请求失败，状态码: " + responseCode;
            }
        } catch (UnknownHostException e) {
            return "网络连接失败，请检查网络设置";
        } catch (IOException e) {
            return "通信异常: " + e.getMessage();
        } finally {
            // ... 关闭资源的代码不变 ...
        }

        return parseResponse(result.toString());
    }

    private String buildJsonRequest(String userInput) {
        JSONObject jsonObject = new JSONObject();
        // 根据DeepSeek API要求的格式构建请求体
        jsonObject.put("model", "deepseek-v3"); // 指定模型版本

        JSONArray messages = new JSONArray();
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "你是一个乐于助人的AI助手");

        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", userInput);

        messages.put(systemMessage);
        messages.put(userMessage);

        jsonObject.put("messages", messages);
        jsonObject.put("temperature", 0.7);  // 添加温度参数
        jsonObject.put("top_p", 0.9);        // 添加top_p参数

        return jsonObject.toString();
    }

    private String parseResponse(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (jsonObject.has("choices")) {
                JSONArray choices = jsonObject.getJSONArray("choices");
                if (choices.length() > 0) {
                    JSONObject firstChoice = choices.getJSONObject(0);
                    JSONObject message = firstChoice.getJSONObject("message");
                    return message.getString("content").trim();
                }
            }
            return "未能解析API响应";
        } catch (Exception e) {
            e.printStackTrace();
            return "响应解析错误: " + e.getMessage();
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}