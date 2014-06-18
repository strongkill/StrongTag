package net.strong.weblucene.search;
import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;

/**
 * Implements search over a single IndexReader. user can customize search result
 * sort behavior via <code>sortType</code>: if data source sorted by some
 * field before indexing docID can be take as the alias to the sort field, so
 * search result sort by docID(or desc) equals to sort by field search results
 * sort method:<br>
 * 0: sort by score (default) <br>
 * 1: sort by docID<br>
 * -1: sort by docID desc
 * 
 * @author Che, Dong
 */
public class IndexOrderSearcher extends IndexSearcher {
	// ~ Static fields/initializers
	// ---------------------------------------------

	/**
	 * search result sorting type: by score (default) by index order by index
	 * order desc
	 */
	public static final int ORDER_BY_SCORE = 0;

	/** search result order by docID */
	public static final int ORDER_BY_DOCID = 1;

	/** search result with reverse order of docID */
	public static final int ORDER_BY_DOCID_DESC = -1;

	// ~ Instance fields
	// --------------------------------------------------------

	/** sort type */
	private int sortType = ORDER_BY_SCORE;

	// ~ Constructors
	// -----------------------------------------------------------

	/**
	 * Creates a new IndexOrderSearcher object.
	 * 
	 * @param r
	 *            index reader
	 * @param sortType
	 *            result sort order
	 */
	public IndexOrderSearcher(IndexReader r, int sortType) {
		super(r);
		this.sortType = sortType;
	}

	/**
	 * Creates a searcher searching the index in the named directory.
	 * 
	 * @param path
	 *            index path
	 * @param sortType
	 *            sort type
	 * 
	 * @throws IOException
	 *             I/O exceptions
	 */
	public IndexOrderSearcher(String path, int sortType) throws IOException {
		this(IndexReader.open(path), sortType);
	}

	/**
	 * Creates a searcher searching the index in the provided directory. with
	 * default sort type: by score
	 * 
	 * @param directory
	 *            index path
	 * @param sortType
	 *            sort type
	 * 
	 * @throws IOException
	 *             I/O exception
	 */
	public IndexOrderSearcher(Directory directory, int sortType)
			throws IOException {
		this(IndexReader.open(directory), sortType);
	}

	// ~ Methods
	// ----------------------------------------------------------------

	/**
	 * search index return with top hits
	 * 
	 * @param query
	 *            lucene queyr
	 * @param filter
	 *            lucene filter
	 * @param nDocs
	 *            top docs number
	 * 
	 * @return top docs
	 * 
	 * @throws IOException
	 *             I/O exception
	 */
	/*
	public TopDocs search(Query query, Filter filter, final int nDocs)
			throws IOException {
		Scorer scorer = query.weight(this).scorer(reader);

		if (scorer == null) {
			return new TopDocs(0, new ScoreDoc[0], sortType);
		}

		final BitSet bits = (filter != null) ? filter.bits(reader) : null;
		final HitQueue hq = new HitQueue(nDocs);
		final int[] totalHits = new int[1];
		final int maxDocID = reader.maxDoc();
		scorer.score(new HitCollector() {
			private float minScore = 0.0f;

			public final void collect(int docID, float score) {
				if ((score > 0.0f) // ignore zeroed buckets
						&& ((bits == null) || bits.get(docID))) { // skip docs
					// not in
					// bits
					totalHits[0]++;

					if (score >= minScore) {
						// update hit queue
						switch (sortType) {
						// sort results by score
						case ORDER_BY_SCORE:
							break;

						// sort results by docID
						case ORDER_BY_DOCID:
							score = 8.0f * (1.0f - ((float) docID / (float) maxDocID));

							break;

						// sort results by docID desc
						case ORDER_BY_DOCID_DESC:
							score = 8.0f * ((float) docID / (float) maxDocID);

							break;

						// sort results by score(default)
						default:
							break;
						}

						// System.out.println("docID=" + docID + " score=" +
						// score);
						hq.put(new ScoreDoc(docID, score));

						// if hit queue overfull
						if (hq.size() > nDocs) {
							// remove lowest in hit queue
							hq.pop();

							// reset minScore
							if (sortType == ORDER_BY_SCORE) {
								minScore = ((ScoreDoc) hq.top()).score;
							}
						}
					}
				}
			}
		});

		ScoreDoc[] scoreDocs = new ScoreDoc[hq.size()];

		// put docs in array
		for (int i = hq.size() - 1; i >= 0; i--) {
			scoreDocs[i] = (ScoreDoc) hq.pop();
		}

		return new TopDocs(totalHits[0], scoreDocs, maxDocID);
	}*/
}
