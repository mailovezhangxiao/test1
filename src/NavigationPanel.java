import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class NavigationPanel {
    private final JFXPanel jfxPanel;

    public NavigationPanel() {
        jfxPanel = new JFXPanel();
        initFX();
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(jfxPanel, BorderLayout.CENTER);
        return panel;
    }

    private void initFX() {
        Platform.runLater(() -> {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.setJavaScriptEnabled(true);

            // 错误输出监听
            webEngine.getLoadWorker().exceptionProperty().addListener((obs, oldEx, newEx) -> {
                if (newEx != null) newEx.printStackTrace();
            });

            // 大小同步
            webView.setPrefSize(jfxPanel.getWidth(), jfxPanel.getHeight());
            jfxPanel.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    Platform.runLater(() -> webView.setPrefSize(jfxPanel.getWidth(), jfxPanel.getHeight()));
                }
            });

            // 加载 HTML 页面（从资源路径中）
            URL pageUrl = getClass().getResource("/map_page.html");
            if (pageUrl != null) {
                webEngine.load(pageUrl.toExternalForm());
            } else {
                System.err.println("❌ 无法加载 map_page.html，确保文件存在于 resources 目录中。");
            }

            jfxPanel.setScene(new Scene(webView));
        });
    }
}
