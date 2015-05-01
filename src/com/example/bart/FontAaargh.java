package com.example.bart;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontAaargh extends TextView{

	public FontAaargh(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public FontAaargh(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FontAaargh(Context context) {
		super(context);
		init();
	}

	private void init() {
		if (!isInEditMode()) {
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
					"Aaargh.ttf");
			setTypeface(tf);
		}
	}
}
	
