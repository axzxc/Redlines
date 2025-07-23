import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class RelatedButton extends JPanel {

	private String relatedStr,title,desc,ID,topicUser;
	private int relatedTypeIndex;
	private JButton openTopicButt;
	private IButton unlinkButt = new IButton("trash.png","Unlink",true);
	private Topic thisTopic;
	private int height = 50;
	public RelatedButton(Topic t,String relatedStr) {
		this.setLayout(new BorderLayout());
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.setPreferredSize(new Dimension(0, height));
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
		this.thisTopic = t;
		this.relatedStr = relatedStr;
		String[] topicArr = relatedStr.split(Constants.separator);
		this.title = topicArr[2];
		this.desc = topicArr[3];
		this.ID = topicArr[1];
		if (topicArr.length >= 5)
			this.topicUser = topicArr[4];
		else
			this.topicUser = "not updated yet!";
		this.setOpaque(false);
		this.relatedTypeIndex = Integer.parseInt(topicArr[0]);
		openTopicButt = new JButton() /*{
			@Override
		    protected void paintComponent(Graphics g) {
		        Graphics2D g2 = (Graphics2D) g.create();
		        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 255f / 255f));
		        g2.setColor(Constants.subButtonColor); // No alpha here
		        g2.fillRect(0, 0, getWidth(), getHeight());
		        g2.dispose();
		        super.paintComponent(g);
		    }
		}*/;
		openTopicButt.setText(title);
		openTopicButt.setFont(Constants.descFont.deriveFont(Font.BOLD));
		openTopicButt.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		openTopicButt.setToolTipText("By "+topicUser);
		openTopicButt.setOpaque(false);
		openTopicButt.setBackground(Constants.subButtonColor);
		openTopicButt.setForeground(Constants.textColorDM);
		openTopicButt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Topic t = Constants.getTopic(ID);
				t.reShow();
			}
		});
		add(openTopicButt,BorderLayout.CENTER);
		Constants.setFixedSize(unlinkButt,new Dimension(height,height));
		Color unlinkColor = new Color(162, 81, 27);
		unlinkButt.setBackground(unlinkColor);
		unlinkButt.setOpaque(false);
		unlinkButt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				thisTopic.unlink(ID,true);
			}
		});
		add(unlinkButt,BorderLayout.EAST);
	}
}
