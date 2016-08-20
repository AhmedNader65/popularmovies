package mrerror.popularmovies.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by ahmed on 19/08/16.
 */

@ContentProvider(authority = MoviesProvider.AUTHORITY,database = MoviesDataBase.class)
public class MoviesProvider {
		public static final String AUTHORITY = "mrerror.popularmovies.data.MoviesProvider";

		static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

		interface Path {
			String Movies = "movies";
			String Favorites = "favorites";
		}

		private static Uri buildUri(String... paths) {
			Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
			for (String path : paths) {
				builder.appendPath(path);
			}
			return builder.build();
		}

		@TableEndpoint(table = MoviesDataBase.Movies) public static class Movies {

			@ContentUri(
					path = Path.Movies,
					type = "vnd.android.cursor.dir/movies",
					defaultSort = MovieColumns._ID + " ASC")
			public static final Uri CONTENT_URI = buildUri(Path.Movies);

			@InexactContentUri(
					path = Path.Movies + "/#",
					name = "MOVIE_ID",
					type = "vnd.android.cursor.item/movies",
					whereColumn = MovieColumns.MOVIE_ID,
					pathSegment = 1)
			public static Uri withId(long id) {
				return buildUri(Path.Movies, String.valueOf(id));
			}

		}

@TableEndpoint(table = MoviesDataBase.FavMovies) public static class Favorites {

			@ContentUri(
					path = Path.Favorites,
					type = "vnd.android.cursor.dir/movies",
					defaultSort = MovieColumns._ID + " ASC")
			public static final Uri CONTENT_URI = buildUri(Path.Favorites);

			@InexactContentUri(
					path = Path.Favorites + "/#",
					name = "MOVIE_ID",
					type = "vnd.android.cursor.item/movies",
					whereColumn = MovieColumns.MOVIE_ID,
					pathSegment = 1)
			public static Uri withId(long id) {
				return buildUri(Path.Favorites, String.valueOf(id));
			}

		}



}
