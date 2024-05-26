package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile> {

	@Override
	public int compare(AudioFile o1, AudioFile o2) {
		
		if (o1.getDuration() == -1) {
			return -1;
			
		}  
		if (o2.getDuration() == -1) {
			return -1;
		}
		if (o1.getDuration() > o2.getDuration()) {
			return 1;
		} else return -1;
	}
	

}
