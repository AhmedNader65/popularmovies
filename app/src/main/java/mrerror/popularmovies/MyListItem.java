package mrerror.popularmovies;

import android.database.Cursor;

import mrerror.popularmovies.data.MovieColumns;

/**
 * Created by ahmed on 19/08/16.
 */
public class MyListItem {
	private String link;

	public void setLink(String link){
		this.link=link;
	}
	public String getLink(){
		return link;
	}
	public static MyListItem fromCursor(Cursor cursor) {
		MyListItem myListItem = new MyListItem();
		myListItem.setLink("http://image.tmdb.org/t/p/w342/"+cursor.getString(cursor.getColumnIndexOrThrow(MovieColumns.POSTER)));
		return myListItem;
	}
}
