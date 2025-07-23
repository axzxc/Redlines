import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VideoPlayer extends JPanel {

    private EmbeddedMediaPlayer mediaPlayer;
    private final JPanel frame;
    private final JLabel timeLabel;
    private final JFrame sourceFrame;
    private final Source source;
    private final String mediaPath;
    private final JButton playPauseBtn = new JButton("Play");
    private boolean started = false;
    private Canvas canvas;
    private final String type;
    private MediaPlayerFactory factory = new MediaPlayerFactory();
    public VideoPlayer(Source source,JFrame sourceFrame) {
        this.source = source;
        source.setVP(this);
        type = source.type;
        this.sourceFrame = sourceFrame;
        mediaPath = source.sourceFile.getAbsolutePath();
    	// Setup UI
        frame = new JPanel(new BorderLayout());
        if (type.equals("vid"))
        {
	        JPanel videoPanel = new JPanel(new BorderLayout());
	        canvas = new Canvas();
	        canvas.setBackground(Color.black);
	        //canvas.setPreferredSize(new Dimension(800, 600));
	        videoPanel.add(canvas, BorderLayout.CENTER);
	        frame.add(videoPanel, BorderLayout.CENTER);
        }

        // Controls panel
        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls,BoxLayout.Y_AXIS));
        JButton rewindBtn = new JButton("<< 10s");
        JButton skipBtn = new JButton("10s >>");
        timeLabel = new JLabel("00:00 / 00:00");
        timeLabel.setFont(Constants.descFont);
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel subControlPanel = new JPanel();
        subControlPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subControlPanel.add(playPauseBtn);
        subControlPanel.add(rewindBtn);
        subControlPanel.add(skipBtn);
        controls.add(timeLabel);
        controls.add(subControlPanel);

        frame.add(controls, BorderLayout.SOUTH);
        //frame.setPreferredSize(800, 600);
        //frame.setVisible(true);

        // VLCJ setup
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        if (type.equals("vid"))
        {
	        VideoSurface videoSurface = factory.videoSurfaces().newVideoSurface(canvas);
	        mediaPlayer.videoSurface().set(videoSurface);
        }

        // Media controls
        playPauseBtn.addActionListener(e -> {
        	if (!started)
        	{
        		mediaPlayer.media().play(mediaPath);
        		playPauseBtn.setText("Pause");
        		started = true;
        	}
        	else
        	{
	            if (mediaPlayer.status().isPlaying()) {
	                mediaPlayer.controls().pause();
	                playPauseBtn.setText("Play");
	            } else {
	                mediaPlayer.controls().play();
	                playPauseBtn.setText("Pause");
	            }
        	}
        });
        playPauseBtn.setText("Play");
        rewindBtn.addActionListener(e -> {
            long time = mediaPlayer.status().time();
            mediaPlayer.controls().setTime(Math.max(0, time - 10_000));
        });

        skipBtn.addActionListener(e -> {
            long time = mediaPlayer.status().time();
            long length = mediaPlayer.media().info().duration();
            mediaPlayer.controls().setTime(Math.min(length, time + 10_000));
        });

        // Update time label periodically
        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mediaPlayer == null || mediaPlayer.media() == null)
                	return;
            	if (mediaPlayer.media().info() != null) {
                    long time = mediaPlayer.status().time();
                    long length = mediaPlayer.media().info().duration();
                    timeLabel.setText(formatTime(time) + " / " + formatTime(length));
                }
            }
        });
        timer.start();
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
        	@Override
        	public void mediaChanged(MediaPlayer mediaPlayer, MediaRef media) {
        		if (source.type.equals("vid"))
        			SwingUtilities.invokeLater(() -> resizeCanvasToVideo());
            }
        });
        this.setLayout(new BorderLayout());
        this.add(frame, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
        sourceFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	dispose();
            }
        });
        //sourceFrame.add(this);
        // Start playing media
        
    }
    public void initialize()
    {
    	mediaPlayer.media().prepare(mediaPath);
        mediaPlayer.media().parsing().parse();
		playPauseBtn.setText("Play");
		started = true;
    }
    public void dispose() {
        if (mediaPlayer != null) {
            // 1. Stop playback (optional, but good practice if it's playing)
            mediaPlayer.controls().stop(); // This stops the current playback

            // 2. Release resources held by the individual media player
            // This is crucial. It frees native VLC resources associated with this player.
            mediaPlayer.release();
            mediaPlayer = null; // Set to null to indicate it's no longer valid
        }

        if (factory != null) {
            // 3. Shut down the media player factory
            // This releases all global native VLC resources initialized by the factory.
            factory.release(); // For older VLCJ versions (before 4.x), this might be factory.release()
                               // For VLCJ 4.x+, it's factory.release()
                               // For EmbeddedMediaPlayerComponent, it handles the factory release usually.
            factory = null; // Set to null
        }

        // If you were using EmbeddedMediaPlayerComponent:
        // If (mediaPlayerComponent != null) {
        //     mediaPlayerComponent.release(); // This internally calls release on its MediaPlayer and its factory
        //     mediaPlayerComponent = null;
        // }
    }
    private void resizeCanvasToVideo()
    {
    	int height,width;
    	try
    	{
    		width = mediaPlayer.video().videoDimension().width;
	    	height = mediaPlayer.video().videoDimension().height;
		    width = (width>500) ? 500:width;
		    height = (height>500) ? 500:height;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		width = 500;
    		height = 500;
    	}
	    if (width > 0 && height > 0) {
	        canvas.setPreferredSize(new Dimension(width, height));
	        canvas.setSize(width, height);
	        this.revalidate();
	        this.repaint();
	        sourceFrame.revalidate();
	        sourceFrame.repaint();
	        sourceFrame.pack();
	    }
    }
    private String formatTime(long millis) {
        long seconds = millis / 1000;
        long mins = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }
}
