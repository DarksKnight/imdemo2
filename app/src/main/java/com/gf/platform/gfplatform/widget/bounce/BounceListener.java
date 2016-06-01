package com.gf.platform.gfplatform.widget.bounce;

public interface BounceListener {

	void onState(boolean header, BounceScroller.State state);

	void onOffset(boolean header, int offset);

}
