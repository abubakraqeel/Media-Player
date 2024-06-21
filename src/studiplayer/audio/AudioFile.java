package studiplayer.audio;
import java.io.File;
import java.util.Objects;



public abstract class AudioFile {

		protected String pathname = "";
		protected String filename = "";
		protected String author;
		protected String title = "";
		

	public AudioFile() {
		
	}
	
	private boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
		}
		
	public AudioFile(String path) throws NotPlayableException {
		this.parsePathname(path);
		this.parseFilename(this.filename);
		File file = new File(getPathname());
		if (!file.canRead()) {
			throw new NotPlayableException(path, "The file is not readable.");
		}
			
	}
	
	public void parsePathname(String rawpath) {
		
		if (Objects.isNull(rawpath) || rawpath.trim().isEmpty()) {
			return;
		}
		char reslash;
		if (isWindows()) {
			reslash = '\\';
		}else {
			reslash = '/';
		}
		
		
		rawpath = rawpath.strip();
		int i = 0;
		int len = rawpath.length();
		
		while (i<len-1) {
			if (rawpath.charAt(i) == '/' || rawpath.charAt(i) == '\\') {
				if (rawpath.charAt(i+1) == '/' || rawpath.charAt(i+1) == '\\') {
					i++;
				}else {
					pathname+=reslash;
					i++;
				}
			}else {
				pathname+=rawpath.charAt(i);
				i++;
			}
		}
		if (rawpath.charAt(len-1) == '/' || rawpath.charAt(len-1) == '\\') {
			pathname+=reslash;
		}else {
			pathname += rawpath.charAt(len-1);
		}
		if (reslash == '/') {
			if (pathname.contains("d:")){
			
				pathname = pathname.replace("d:", "/d");
			}
		}
		
		int slash = pathname.lastIndexOf(reslash);
		
		if (slash != pathname.length()-1) {
			for (int j = slash+1; j<pathname.length(); j++) {
				filename+=pathname.charAt(j);
			}
		}
	}
	
	public void parseFilename(String rawfilename) {
		if (rawfilename.equals("-")) {
			title = "-";
		}else {
		
		if (rawfilename.contains(" - ") && rawfilename.contains(".")) {
			rawfilename = rawfilename.trim();	
			String[] rawfilenamesplit = rawfilename.split(" - ");	
			
			author = rawfilenamesplit[0].trim();
			title = rawfilenamesplit[1].substring(0, rawfilenamesplit[1].lastIndexOf('.')).trim();
			}
			else if (rawfilename.contains("-")==false && rawfilename.contains(".")){
			
			title = rawfilename.substring(0, rawfilename.lastIndexOf('.'));
				}else {
					author = "";
					title = rawfilename.substring(0, rawfilename.lastIndexOf('.')).trim();
				}

			}
		
	}
	
	
	public String getAuthor() {
		return author == null ? "" : this.author.trim();
	}
	public String getTitle() {
		return title == null ? "" : this.title.trim();
	}
	public String getPathname() {
		return this.pathname;
	}
	public String getFilename() {
		return this.filename;
	}
	
	@Override
	public String toString() {
		if (getAuthor().isEmpty()) {
			return getTitle();
		}else {
		
		return getAuthor() + " - " + getTitle();
		}
	}
	
	
	abstract public void play() throws NotPlayableException;
	abstract public void togglePause();
	abstract public void stop();
	abstract public String formatDuration();
	abstract public String formatPosition();

//	public long getDuration() {
//		// TODO Auto-generated method stub
//		return -1l;
//	}
	
	public String getAlbum() {
		return "";
	}

	


}
