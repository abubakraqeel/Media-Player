package studiplayer.audio;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class PlayList implements Iterable<AudioFile>{
	
	List<AudioFile> audioFiles = new LinkedList<>();
	String search;
	SortCriterion sortCriterion = SortCriterion.DEFAULT;
	ControllablePlayListIterator iterator;
	
	public PlayList() {
		this.iterator = new ControllablePlayListIterator(audioFiles);
	}
	
	public PlayList(String m3uPathname) throws RuntimeException{
		this();
		try {
			loadFromM3U(m3uPathname);
		} catch (NotPlayableException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(m3uPathname);
		}
	}
	
	public void add(AudioFile file) {
		
		audioFiles.add(file);
	}
	
	public void remove(AudioFile file) {
		audioFiles.remove(file);


	}
	
	public int size() {
		return audioFiles.size();
	}
	
	public AudioFile currentAudioFile() {
	
		return iterator.getCurrentFile();
	}
	
	public void nextSong() {
		
		iterator.next();
	}
	
	public void loadFromM3U(String pathname) throws NotPlayableException {
		
		getList().clear();
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
	
	public AudioFile jumpToAudioFile(AudioFile audioFile) {
		return iterator.jumpToAudioFile(audioFile);
	
	}
	
	public List<AudioFile> getList(){
		return audioFiles;
	}
	
	public String getSearch() {
		return search;
		
	}

	public void setSearch(String search) {
		this.search = search;
        searchAndFilter();
		
	}
	

	public SortCriterion getSortCriterion() {
		return sortCriterion;
	}

	public void setSortCriterion(SortCriterion sortCriterion) {
		this.sortCriterion = sortCriterion;
		iterator.searchAndFilter(getList(), getSearch(), sortCriterion);
	}
	
	private void searchAndFilter() {
        iterator.searchAndFilter(audioFiles, search, sortCriterion);
    }
	
	public String toString() {
		return getList().toString();
		}

	@Override
	public Iterator<AudioFile> iterator() {
		// TODO Auto-generated method stub
		return new ControllablePlayListIterator(getList(), getSearch(), getSortCriterion());
	}
	

}
