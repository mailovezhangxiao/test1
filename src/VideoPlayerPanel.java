import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class VideoPlayerPanel extends JPanel {
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private JFXPanel jfxPanel;

    public VideoPlayerPanel(String videoPath) {
        setLayout(new BorderLayout());
        jfxPanel = new JFXPanel(); // JavaFX panel inside Swing
        add(jfxPanel, BorderLayout.CENTER);

        // Initialize JavaFX and set up video player
        Platform.runLater(() -> {
            try {
                File file = new File(videoPath);
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(this, "视频文件未找到", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create Media object
                Media media = new Media(file.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaView = new MediaView(mediaPlayer);
                mediaView.setPreserveRatio(true);  // Preserve aspect ratio
                mediaView.setSmooth(true);  // Enable smooth scaling

                // Create a scene and set it to the JFXPanel
                StackPane root = new StackPane();
                root.getChildren().add(mediaView);
                Scene scene = new Scene(root);
                jfxPanel.setScene(scene);

                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the video
                mediaPlayer.play(); // Start playing the video

                // Add resize listener to update video size dynamically
                jfxPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
                    @Override
                    public void componentResized(java.awt.event.ComponentEvent e) {
                        updateVideoSize();
                    }
                });

                // Initial update of video size
                updateVideoSize();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "无法播放视频: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void updateVideoSize() {
        // Ensure the video adjusts to the panel size
        if (mediaView != null) {
            int panelWidth = jfxPanel.getWidth();
            int panelHeight = jfxPanel.getHeight();

            // Update video size (width and height)
            mediaView.setFitWidth(panelWidth);
            mediaView.setFitHeight(panelHeight);
        }
    }

    public void stopPlayback() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
    }
}
