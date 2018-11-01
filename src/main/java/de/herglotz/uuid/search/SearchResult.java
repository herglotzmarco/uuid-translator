package de.herglotz.uuid.search;

public class SearchResult {

	private SearchResultType type;
	private String message;

	private SearchResult(String message, SearchResultType type) {
		this.type = type;
		this.message = message;
	}

	static SearchResult noResult(String searchString) {
		return new SearchResult(String.format("Id containing [%s] is not present in selected workspace", searchString),
				SearchResultType.EMPTY);
	}

	static SearchResult oneResult(String name) {
		return new SearchResult(name, SearchResultType.ONE);
	}

	static SearchResult multipleResults(int size, String searchString) {
		return new SearchResult(
				String.format("%s possibilities for SearchString [%s] in selected workspace", size, searchString),
				SearchResultType.MULTIPLE);
	}

	static SearchResult invalidUUID(String searchString) {
		return new SearchResult(String.format("SearchString [%s] is not a valid UUID", searchString),
				SearchResultType.INVALID);
	}

	public String getMessage() {
		return message;
	}

	public SearchResultType getType() {
		return type;
	}

}
