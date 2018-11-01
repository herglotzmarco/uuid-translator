package de.herglotz.uuid;

public class SearchResult {

	private String errorMessage;
	private String name;
	private boolean valid;

	private SearchResult(boolean valid, String message) {
		this.valid = valid;
		if (valid) {
			this.name = message;
		} else {
			this.errorMessage = message;
		}
	}

	public static SearchResult noResult(String searchString) {
		return new SearchResult(false,
				String.format("Id containing [%s] is not present in selected workspace", searchString));
	}

	public static SearchResult ok(String name) {
		return new SearchResult(true, name);
	}

	public static SearchResult multipleResults(int size, String searchString) {
		return new SearchResult(false,
				String.format("%s possibilities for SearchString [%s] in selected workspace", size, searchString));
	}

	public boolean hasError() {
		return !valid;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getName() {
		return name;
	}

}
