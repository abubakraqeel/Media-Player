package studiplayer.audio;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ControllablePlayListIterator implements Iterator<AudioFile> {
	
	private List<AudioFile> audioFiles = new LinkedList<AudioFile>();
	private int index = 0;
	
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public ControllablePlayListIterator(List<AudioFile> audioFiles) {
		this.audioFiles = audioFiles;
	}
	
	public ControllablePlayListIterator(List<AudioFile> audioFiles, String search, SortCriterion sortCriterion) {
		
		List<AudioFile> rawList;
		
		if (search == null || search.isEmpty()) {
			rawList = new LinkedList<AudioFile>(audioFiles);
		} else {
			rawList = new LinkedList<AudioFile>();
			 for (int i = 0; i<audioFiles.size(); i++) {
				 AudioFile currentFile = audioFiles.get(i);
				 
				 if (currentFile.getAuthor().contains(search) || 
						 currentFile.getTitle().contains(search) || 
						 currentFile.getAlbum().contains(search)) {
					 
					 rawList.add(currentFile);
				 }
			 }
		}
		
		switch (sortCriterion) {
		case TITLE:
			rawList.sort( new TitleComparator());
			break;
		case ALBUM:
			rawList.sort( new AlbumComparator());
			break;
		case AUTHOR:
			rawList.sort( new AuthorComparator());
			break;
		case DURATION:
			rawList.sort( new DurationComparator());
			break;
		default:
			break;
		}
		
		this.audioFiles = rawList;
		
	}
	
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return this.getIndex() < this.audioFiles.size();
	}

	@Override
	public AudioFile next() {
		// TODO Auto-generated method stub
		if (this.audioFiles.isEmpty()) {
			return null;
		}
		
		AudioFile audio = this.audioFiles.get(getIndex());
		if (getIndex() < this.audioFiles.size() ) {
			setIndex(getIndex() +1);
			
		} else if(getIndex() >= this.audioFiles.size()) {
			setIndex(0);
			
		}
		return audio;
		
	}
	
	public AudioFile jumpToAudioFile(AudioFile file) {
		
		if (this.audioFiles.indexOf(file) !=-1) {
			setIndex(this.audioFiles.indexOf(file) +1);
			return file;
		} else {
			return null;
		}
	}
	
	
	
	public static void main(String args[]) {
		List<AudioFile> files = null;
		try {
			files = Arrays.asList(
					new TaggedFile("audiofiles/Rock 812.mp3"),
					new TaggedFile("audiofiles/Eisbach Deep Snow.ogg"),
					new TaggedFile("audiofiles/wellenmeister_awakening.ogg"));
		} catch (NotPlayableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				ControllablePlayListIterator it =
				new ControllablePlayListIterator(files);
				it.jumpToAudioFile(files.get(1));
				while(it.hasNext()) System.out.println(it.next());
	}

}
