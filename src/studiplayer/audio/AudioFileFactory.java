package studiplayer.audio;

public class AudioFileFactory {
	
	public AudioFileFactory(String path) throws NotPlayableException {
		createAudioFile(path);
	}
	
	public static AudioFile createAudioFile(String line) throws NotPlayableException {
		String extension = line.substring(line.lastIndexOf('.') + 1, line.length()).toLowerCase();
		switch (extension) {
		case "mp3":
			return new TaggedFile(line);
		case "ogg":
			return new TaggedFile(line);
		case "wav":
			return new WavFile(line);
		default:
			throw new NotPlayableException(line, "Unknown suffix for AudioFile");

		}
	}


}
