package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile> {

	@Override
	public int compare(AudioFile o1, AudioFile o2) {
		// TODO Auto-generated method stub
		if (o1 != null && o2 != null) {
			
			if ((o1 instanceof TaggedFile) && !(o2 instanceof TaggedFile)) {
				return 1;
			} else if (!(o1 instanceof TaggedFile) && (o2 instanceof TaggedFile)) {
				return -1;
			} else if (!(o1 instanceof TaggedFile) && !(o2 instanceof TaggedFile)) {
				return 0;
			}
        } return 0;
		
	}

}
