package studiplayer.audio;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class PlayList implements Iterable<AudioFile>{
	
	List<AudioFile> audiofiles = new LinkedList<>();
	int current;
	String search;
	SortCriterion sortCriterion = SortCriterion.DEFAULT;
	
	public String getSearch() {
		return search;
		
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public SortCriterion getSortCriterion() {
		return sortCriterion;
	}

	public void setSortCriterion(SortCriterion sortCriterion) {
		this.sortCriterion = sortCriterion;
	}

	public PlayList() {
		current = getCurrent();
	}
	
	public PlayList(String m3uPathname) throws RuntimeException{
		try {
			loadFromM3U(m3uPathname);
		} catch (NotPlayableException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(m3uPathname);
		}
	}
	
	public void add(AudioFile file) {
		audiofiles.add(file);
	}
	
	public void remove(AudioFile file) {
		audiofiles.remove(file);
	}
	
	public int size() {
		return audiofiles.size();
	}
	
	public AudioFile currentAudioFile() {
		if (size() == 0 || current>size()) {
			return null;
		} else {
			return audiofiles.get(getCurrent());
		}
	}
	
	public void nextSong() {
		if (current<size()-1) {
			current++;
		} else {
			current = 0;
		}
	}
	public void loadFromM3U(String pathname) throws NotPlayableException {
		getList().clear();
		setCurrent(0);
		Scanner scanner = null;
		
		try {
			
			scanner = new Scanner(new File(pathname));
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.startsWith("#") || line.isBlank()){
					continue;
				}else {
					try {
						this.add(AudioFileFactory.createAudioFile(line));
					}catch (Exception e){
						continue;
						
						}
					}
				}
			
		}catch (Exception e) {
			throw new NotPlayableException(pathname, " File not found");
		} finally {
			try {
				scanner.close();	
			} catch (Exception e) {
				// ignore; probably because file could not be opened
			}
		}
		
	}

	
	
	public void saveAsM3U(String pathname) {
		FileWriter writer = null;
		String sep = System.getProperty("line.separator");
		
		try {
			// create the file if it does not exist, otherwise reset the file
			// and open it for writing
			writer = new FileWriter(pathname);
			for (AudioFile audiofile : getList()) {
				// write the current line + newline char
				writer.write(audiofile.getPathname() + sep);
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to write file " + pathname + "!");
		} finally {
			try {
				System.out.println("File " + pathname + " written!");
				// close the file writing back all buffers
				writer.close();
			} catch (Exception e) {
				// ignore exception; probably because file could not be opened
			}
		}
	}
	
	
	public List<AudioFile> getList(){
		return audiofiles;
	}
	
	public int getCurrent() {
		return current;
		
	}
	
	public void setCurrent(int current) {
		this.current = current;
	}
	
	
	
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterator<AudioFile> iterator() {
		// TODO Auto-generated method stub
		return new ControllablePlayListIterator(getList(), getSearch(), getSortCriterion());
	}
	

}
