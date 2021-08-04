package com.bebo.manga.Interface;

import com.bebo.manga.model.Mix;

import java.util.List;

public interface IMixLoadDone {
    void onMixLoadDoneListener(List<Mix> mixList);
}
