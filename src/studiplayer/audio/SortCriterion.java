package studiplayer.audio;

public enum SortCriterion {
	DEFAULT("Default"),
	AUTHOR("Author"),
	TITLE("Title"),
	DURATION("Duration"),
	ALBUM("Album");
	
	private final String criterion;

	SortCriterion(String criterion) {
		this.criterion = criterion;
	}
	
	@Override
	public String toString() {
		return criterion;
	}
}
