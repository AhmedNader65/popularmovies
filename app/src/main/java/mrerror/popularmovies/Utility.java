package mrerror.popularmovies;

/**
 * Created by ahmed on 19/08/16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Utility {
	public static String getOrder(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(context.getString(R.string.order_pref_key),
				context.getString(R.string.order_pref_default));
	}

}