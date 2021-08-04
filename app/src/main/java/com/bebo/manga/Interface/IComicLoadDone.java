package com.bebo.manga.Interface;
import com.bebo.manga.model.Comic;
import com.bebo.manga.model.Mix;

import java.util.List;

public interface IComicLoadDone {
    void onComicLoadDoneListener(List<Comic> comics);
}
