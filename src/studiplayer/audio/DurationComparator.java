package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile> {

	@Override
	public int compare(AudioFile o1, AudioFile o2) {
		
		if (!(o2 instanceof SampledFile)) {
			return 1;
		} 
		if (!(o1 instanceof SampledFile)) {
			return -1;
		}
		SampledFile s1 = (SampledFile) o1;
		SampledFile s2 = (SampledFile) o2;
		
		
		if (s1.getDuration() > s2.getDuration()) {
			return 1;
		} else if (s1.getDuration() < s2.getDuration()) {
			return -1;
		} else {
			return 0;
		}
	}
	

}
