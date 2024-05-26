package studiplayer.audio;

import java.util.Comparator;

public class AuthorComparator implements Comparator<AudioFile> {

	@Override
	public int compare(AudioFile o1, AudioFile o2) {
		
		if (o1.getAuthor().isEmpty()) {
			return -1;
		} else if(o2.getAuthor().isEmpty()) {
			return 1;
		}
		return o1.getAuthor().compareTo(o2.getAuthor());
		
	}
	
	

}
