package com.GF.platform.uikit.widget.bounce;

public interface BounceListener {

	void onState(boolean header, BounceScroller.State state);

	void onOffset(boolean header, int offset);

}
