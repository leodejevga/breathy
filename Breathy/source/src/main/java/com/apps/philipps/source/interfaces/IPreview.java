package com.apps.philipps.source.interfaces;

import android.content.Context;
import android.widget.VideoView;

/**
 * Created by Jevgenij Huebert on 20.02.2017. Project Breathy
 */

public interface IPreview {
    public Context context = null;
    public VideoView start(VideoView view);
}
