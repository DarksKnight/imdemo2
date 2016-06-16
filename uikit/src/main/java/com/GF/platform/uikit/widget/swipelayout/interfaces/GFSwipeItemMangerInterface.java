package com.GF.platform.uikit.widget.swipelayout.interfaces;


import com.GF.platform.uikit.widget.swipelayout.GFSwipeLayout;
import com.GF.platform.uikit.widget.swipelayout.util.GFAttributes;

import java.util.List;

public interface GFSwipeItemMangerInterface {

    void openItem(int position);

    void closeItem(int position);

    void closeAllExcept(GFSwipeLayout layout);
    
    void closeAllItems();

    List<Integer> getOpenItems();

    List<GFSwipeLayout> getOpenLayouts();

    void removeShownLayouts(GFSwipeLayout layout);

    boolean isOpen(int position);

    GFAttributes.Mode getMode();

    void setMode(GFAttributes.Mode mode);
}
