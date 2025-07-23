import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;


public class AudioPlayer extends JPanel {

	private final Source audioSource;
	private Clip clip;
	private JButton PPToggle;
	private ImageIcon playIcon = null;
	private ImageIcon pauseIcon = null;
	private long curPos = 0;
	private int state = -1; //-1 - stopped | 0 - paused | 1 - playing
	private AudioInputStream audioStream;
	private JPanel previewPanel;
	private JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
	private long jumpLength = 5000000;
	private Font fontBold = new Font(Constants.fontName, Font.BOLD, 20); 
	private Font fontLight = new Font(Constants.fontName, Font.PLAIN, 20);
	private JLabel runtime = new JLabel();
	private long audLength;
	public AudioPlayer(Source audioSource,JFrame ss) throws LineUnavailableException, IOException, UnsupportedAudioFileException
	{
		this.audioSource = audioSource;
		audioStream = AudioSystem.getAudioInputStream(audioSource.sourceFile);
		//AudioFormat format = audioStream.getFormat();
		previewPanel = new JPanel();
		previewPanel.setLayout(new BoxLayout(previewPanel,BoxLayout.Y_AXIS));
		JLabel audName = new JLabel(audioSource.name);
		audName.setFont(fontBold);
		audName.setAlignmentX(Component.LEFT_ALIGNMENT); 
		previewPanel.add(audName);
		JLabel audSize = new JLabel(Constants.getHRFileSize(audioSource.sourceFile.length()));
		audSize.setFont(fontLight);
		audSize.setAlignmentX(Component.LEFT_ALIGNMENT); 
		previewPanel.add(audSize);
		runtime.setFont(fontLight);
		runtime.setAlignmentX(Component.LEFT_ALIGNMENT); 
		previewPanel.add(runtime);
		//SAMPLE_SIZE = format.getFrameSize();
	    clip = AudioSystem.getClip();
		clip.open(audioStream);
		audLength = clip.getMicrosecondLength();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(previewPanel);
		ToolTipManager.sharedInstance().setInitialDelay(0);
		JButton rewindButton = new JButton(new ImageIcon(ImageIO.read(new File("res/img/rewind.png")).getScaledInstance(Constants.imgScale, Constants.imgScale, Image.SCALE_SMOOTH)));
		rewindButton.setToolTipText("Rewind "+String.valueOf((int)(jumpLength/1000000))+" sec");
		rewindButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try 
				{
					rewind(jumpLength);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		controlPanel.add(rewindButton);
		JButton stopButton = new JButton(new ImageIcon(ImageIO.read(new File("res/img/stop.png")).getScaledInstance(Constants.imgScale, Constants.imgScale, Image.SCALE_SMOOTH)));
		stopButton.setToolTipText("Stop");
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try 
				{
					stop();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		controlPanel.add(stopButton);
		playIcon = new ImageIcon(ImageIO.read(new File("res/img/play.png")).getScaledInstance(Constants.imgScale, Constants.imgScale, Image.SCALE_SMOOTH));
		pauseIcon = new ImageIcon(ImageIO.read(new File("res/img/pause.png")).getScaledInstance(Constants.imgScale, Constants.imgScale, Image.SCALE_SMOOTH));
		PPToggle = new JButton(playIcon);
		PPToggle.setToolTipText("Toggle Play/Pause");
		PPToggle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try 
				{
					togglePP();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		controlPanel.add(PPToggle);
		JButton skipButton = new JButton(new ImageIcon(ImageIO.read(new File("res/img/skip.png")).getScaledInstance(Constants.imgScale, Constants.imgScale, Image.SCALE_SMOOTH)));
		skipButton.setToolTipText("Skip "+String.valueOf((int)(jumpLength/1000000))+" sec");
		skipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try 
				{
					skip(jumpLength);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		controlPanel.add(skipButton);
		this.add(controlPanel);
		stop();
		ss.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    stop(); 
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
		new Thread(() -> {
            while (true) {
                try {
	                	Thread.sleep(50); 
	                    updateRuntime();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start(); 
	}
	/*private byte[] readAudioDataFromStream() throws IOException {
        byte[] buffer = new byte[SAMPLE_SIZE * 2]; 
        int bytesRead = audioStream.read(buffer, 0, buffer.length); 
        // Handle the end of the stream

        return buffer; 
    }*/
	private String toTime(int seconds) {
	    int minutes = seconds / 60;
	    seconds = seconds % 60;
	    return String.format("%02d:%02d", minutes, seconds);
	}
	private void updateRuntime()
	{
		curPos = clip.getMicrosecondPosition();
		runtime.setText(toTime((int)(curPos/1000000))+"/"+toTime((int)(audLength/1000000)));
	}
	private void togglePP() throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		switch(state)
		{
			case 1:
				pause();
				break;
			case -1:
				audioStream = AudioSystem.getAudioInputStream(audioSource.sourceFile);
			    clip.open(audioStream);
			case 0:
				play();
				break;
		}
	}
	private void play()
	{
		PPToggle.setIcon(pauseIcon);
		clip.setMicrosecondPosition(curPos);
		clip.start();
		state = 1;
	}
	private void pause()
	{
		PPToggle.setIcon(playIcon);
		curPos = clip.getMicrosecondPosition();
	    clip.stop();
	    state = 0;
	}
	private void stop()
	{
		clip.stop();
        clip.close();
        PPToggle.setIcon(playIcon);
        curPos = 0;
	    state = -1;
	}
  private void jump(long c) throws UnsupportedAudioFileException, IOException, LineUnavailableException
  {
        if(c >= 0 && c <= audLength){
            clip.stop();
            clip.close();
            audioStream = AudioSystem.getAudioInputStream(audioSource.sourceFile);
		    clip.open(audioStream);
            curPos = c;
            clip.setMicrosecondPosition(c);
            clip.start();
            state = 1;
        }
    }
	private void skip(long ms) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		jump(curPos + ms);		
	}
	private void rewind(long ms) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		jump(curPos - ms);
	}

}
