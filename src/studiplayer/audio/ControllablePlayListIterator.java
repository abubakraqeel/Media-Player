package studiplayer.audio;

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
	
	public AudioFile getCurrentFile() {
		if (audioFiles.isEmpty()) {
			return null;
		} else if(getIndex() >= audioFiles.size()) {
			setIndex(0);
		}
		return audioFiles.get(getIndex());
	}
	
	public ControllablePlayListIterator(List<AudioFile> audioFiles, String search, SortCriterion sortCriterion) {
		
		this.audioFiles = audioFiles;
		searchAndFilter(audioFiles, search, sortCriterion);
	}
	
	public void searchAndFilter(List<AudioFile> audioFiles, String search, SortCriterion sortCriterion) {

		setIndex(0); 
	    List<AudioFile> rawList = new LinkedList<>();

	    if (search == null || search.isEmpty() || search.isBlank()) {
	        rawList.addAll(audioFiles);
	    } else {
	        for (AudioFile file : audioFiles) { // Iterate over the original list
	            if (file.getAuthor().contains(search) || file.getTitle().contains(search)) {
	                rawList.add(file);
	            } else if (file instanceof TaggedFile && ((TaggedFile) file).getAlbum() != null) {
	                if (((TaggedFile) file).getAlbum().contains(search)) {
	                    rawList.add(file);
	                }
	            }
	        }
	    }

	    switch (sortCriterion) {
	        case TITLE:
	            rawList.sort(new TitleComparator());
	            break;
	        case ALBUM:
	            rawList.sort(new AlbumComparator());
	            break;
	        case AUTHOR:
	            rawList.sort(new AuthorComparator());
	            break;
	        case DURATION:
	            rawList.sort(new DurationComparator());
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
		
		int k = this.audioFiles.indexOf(file); 
		if (k != -1) {
			int l = k - getIndex();
			if(l > 0) {
				for(int i = 0; i<l; i++) 
				    this.next();
			}else if(l < 0) {
				setIndex(0);
				for(int i = 0; i<=k; i++) 
					this.next();
			} return this.audioFiles.get(k); 
			} else return null;
		
		
		
	}
}
