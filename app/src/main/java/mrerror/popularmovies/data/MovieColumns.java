package mrerror.popularmovies.data;

import android.support.annotation.Nullable;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;
/**
 * Created by ahmed on 19/08/16.
 */
public interface MovieColumns {

	@DataType(INTEGER) @PrimaryKey
	@AutoIncrement
	String _ID = "_id";

	@DataType(TEXT) @NotNull
	String TITLE = "title";

	@DataType(TEXT) @NotNull
	String POSTER = "poster_path";

	@DataType(TEXT) @NotNull
	String OVERVIEW = "overview";

	@DataType(TEXT) @NotNull
	String DATE = "release_date";

	@DataType(TEXT) @NotNull
	String VOTE = "vote_average";

	@DataType(TEXT) @Nullable
	String VIDEOS = "trailers";

	@DataType(INTEGER) @NotNull
	String MOVIE_ID = "movie_id";

}