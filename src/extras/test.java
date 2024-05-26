package extras;

import studiplayer.audio.TaggedFile;

class test {
	

	public static void main(String[] args) {
	TaggedFile af = new TaggedFile("audiofiles/beethoven-ohne-album.mp3");
	System.out.println(af.getFilename());
	System.out.println(af.toString());
	}
}

