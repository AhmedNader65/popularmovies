package mrerror.popularmovies.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ahmed on 20/08/16.
 */
public class TextViewTitle extends TextView {

    public TextViewTitle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewTitle(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Fonts/chunkfive.regular.ttf");
            setTypeface(tf);
        }
    }

}