import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

import javax.swing.JFrame;

import org.icepdf.ri.common.SwingController;

public class Source 
{
	public boolean isOpen = false;
	public final String path,topicID,ID;
	public final boolean isPrimary;
	public final File sourceFile;
	public final String type;
	public final String title;
	public final String name;
	public final long timestamp;
	private VideoPlayer vp = null;
	private SwingController PDFC = null;
	private String desc;
	private JFrame sourceFrame;
	public Source(File sourceFile,String sourceName,String sourceDesc) {
		name = sourceName; //url
		desc = sourceDesc;
		path = sourceFile.getPath();
		String[] pathArr = null;
		if (Constants.os.contains("WIN")) {
            pathArr = path.split(File.separator+File.separator);
        } else if (Constants.os.contains("LINUX")) {
        	pathArr = path.split(File.separator);
        } else if (Constants.os.contains("MAC")) {
            pathArr = path.split(File.separator);
        } else {
            throw new RuntimeException("Unsupported OS");
        } 
		int len = pathArr.length;
		topicID = (pathArr[len-4]);
		BasicFileAttributes attr = null;
		try {
			attr = Files.readAttributes(sourceFile.toPath(), BasicFileAttributes.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.timestamp = attr.creationTime().toMillis(); 
		isPrimary = (pathArr[len-2].equals("P"));
		this.sourceFile = sourceFile;
		String ext = Constants.getExtension(sourceFile).toLowerCase();
		title = sourceFile.getName();
		switch(ext)
		{
			case "png":
			case "jpg":
			case "jpeg":
			case "gif":
				type = "img";
				break;
			case "mp4":
		    case "mov":
		    case "avi":
		    case "wmv":
		    case "mkv":
		    case "flv":
		    case "webm":
		    	type = "vid";
				break;
		    case "au":
		    case "aiff":
		    case "rmf":
		    case "wav":
		    case "mp3":
		    case "m4a":
		    	type = "aud";
		    	break;
		    case "pdf":
		    	type = "pdf";
		    	break;
		    case "lin":
		    	type = "link";
		    	break;
		    default:
				type = "file";
				break;
		}
		ID = name+topicID+Long.toString(timestamp);
	}
	public void setFrame(JFrame ss)
	{
		sourceFrame = ss;
	}
	public JFrame getFrame()
	{
		return sourceFrame;
	}
	public void setVP(VideoPlayer v)
	{
		vp = v;
	}
	public void setDesc(String newDesc)
	{
		desc = newDesc;
	}
	public String getDesc()
	{
		return desc;
	}
	public boolean equals(Source s)
	{
		return (s.name.equals(name) && s.topicID.equals(topicID) && (s.timestamp == timestamp));
	}
	public void addPDFController(SwingController c)
	{
		PDFC = c;
	}
	public void delete()
	{
		if (vp != null) vp.dispose();
		if (PDFC != null) PDFC.dispose();
		Constants.deleteFile(path);
		Constants.deleteFile(path+".desc");
	}
	public String toString()
	{
		String out = "Is Open: "+isOpen+"\nPath: "+path+"\nTopic ID: "+topicID+"\nID: "+ID+"\nisPrimary: "+isPrimary+"\nSource File(path): "+sourceFile.getAbsolutePath()+"\nType: "+type+"\nTitle: "+title+"\nName: "+name+"\nTimestamp: "+Long.toString(timestamp)+"\nDescription: "+desc;
		System.out.println(out);
		return out;
	}
}
