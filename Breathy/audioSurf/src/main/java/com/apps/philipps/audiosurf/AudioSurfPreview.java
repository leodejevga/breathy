package com.apps.philipps.audiosurf;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.VideoView;

import com.apps.philipps.source.interfaces.IPreview;

/**
 * Created by Jevgenij Huebert on 27.01.2017. Project Breathy
 */
public class AudioSurfPreview implements IPreview {

    VideoView videoView;
    @Override
    public VideoView start(VideoView view) {
        String videoPath = "android.resource://com.apps.philipps.audiosurf/" + R.raw.aaa;
        Uri uri = Uri.parse(videoPath);
        view.setVideoURI(uri);
        view.start();
        return view;
    }
}
