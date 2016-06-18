package com.GF.platform.uikit.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunhaoyang on 2016/6/18.
 */
public class GFImageSelectEvent extends GFBaseEvent {

    public List<String> resultList = new ArrayList<>();

    public GFImageSelectEvent(List<String> resultList) {
        this.resultList = resultList;
    }
}
