package mrerror.popularmovies.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.ExecOnCreate;
import net.simonvt.schematic.annotation.OnConfigure;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;
/**
 * Created by ahmed on 19/08/16.
 */
@Database(version = MoviesDataBase.VERSION)
public final class MoviesDataBase {

	private MoviesDataBase(){}
	public static final int VERSION = 1;
	@Table(MovieColumns.class) public static final String Movies = "movies";
	@Table(MovieColumns.class) public static final String FavMovies = "fav_movies";

	@OnCreate
	public static void onCreate(Context context, SQLiteDatabase db) {
		Cursor dbCursor = db.query(Movies, null, null, null, null, null, null);
		String names[] = dbCursor.getColumnNames();
		for(int i = 0 ;i<names.length;i++) {
			Log.e("hi",names[i] );
		}
	}


	@OnUpgrade
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	@OnConfigure
	public static void onConfigure(SQLiteDatabase db) {
	}

	@ExecOnCreate
	public static final String EXEC_ON_CREATE = "SELECT * FROM " + Movies;
}
