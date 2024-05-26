package studiplayer.audio;

import java.io.File;
import java.util.Map;
import studiplayer.basic.TagReader;

public class TaggedFile extends SampledFile {
	private String album; 
	
	
	public TaggedFile(String path) throws NotPlayableException {
		super(path);
		File file = new File(getPathname());
		if (!file.canRead()) {
			throw new NotPlayableException(path, "The file is not readable.");
		} else {
		this.readAndStoreTags();
		}
		
	}
	public TaggedFile() {
		
	}
	
	public void readAndStoreTags()  throws NotPlayableException {
		try {
			TagReader.readTags(getPathname());
		} catch (Exception e) {
			throw new NotPlayableException(getPathname(), "Tag not found");
		}
		
		Map<String, Object> tagMap = TagReader.readTags(this.getPathname());
		if ((String) tagMap.get("author") != null)
		this.author = (String) tagMap.get("author");
		if ((String) tagMap.get("album") != null)
		this.album = (String) tagMap.get("album");
		if ((String) tagMap.get("title") != null) {
		
		this.title = (String) tagMap.get("title");
		}
		
		this.duration = (long) tagMap.get("duration");
		
				
		
	}
	
	public String getAlbum() {
		return this.album == null ? "" : album.trim();
	}
	
	public String toString() {
		if (!getAlbum().isEmpty()) {
			return super.toString() + " - " + this.getAlbum() + " - " + formatDuration();
		} else {
			return super.toString() + " - " + formatDuration();
		}
	}
	
//	public static void main(String[] args) {
//		TaggedFile tf = new TaggedFile("audiofiles/wellenmeister_awakening.ogg"); 
//		System.out.println(tf.getAuthor());
//			
//		}
		
	
	
	
}

