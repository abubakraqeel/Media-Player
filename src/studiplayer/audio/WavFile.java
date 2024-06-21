package studiplayer.audio;
import java.io.File;

import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile {
	
	
	public WavFile() {
		
	}
	
	public WavFile(String path) throws NotPlayableException {
		super(path);
		File file = new File(getPathname());
		if (!file.canRead()) {
			throw new NotPlayableException(path, "The file is not readable.");
		}else {
				readAndSetDurationFromFile();
			}
			
		
	}
	
	public void readAndSetDurationFromFile() throws NotPlayableException {
		try {
		WavParamReader.readParams(this.getPathname());
		float frameRate = WavParamReader.getFrameRate();
		long totalFrames = WavParamReader.getNumberOfFrames();
		duration = computeDuration(totalFrames, frameRate);
		}catch ( Exception e) {
			throw new NotPlayableException(getPathname(), "Parameters not found");
		}
		
		
	}
	public String toString() {
		return super.toString() + " - " + timeFormatter(duration);
		
	}
	
	public static long computeDuration(long numberOfFrames, float framRate) {
		
		return (long) ((numberOfFrames/framRate)*1000000);

	}
	
	
	
	
	
	
}
