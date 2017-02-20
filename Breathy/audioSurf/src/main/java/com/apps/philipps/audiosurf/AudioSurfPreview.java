package com.apps.philipps.audiosurf;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.VideoView;

import com.apps.philipps.audiosurf.activities.Preview;
import com.apps.philipps.source.abstracts.AbstractGameObject;

/**
 * Created by Jevgenij Huebert on 27.01.2017. Project Breathy
 */
public class AudioSurfPreview extends AbstractGameObject {

    VideoView videoView;
    /**
     * Instantiates a new Audio surf preview.
     *
     * @param context the context
     */
    public AudioSurfPreview(Context context) {
        this.context = context;
    }
    @Override
    public View start() {
        String videoPath = "android.resource://com.apps.philipps.audiosurf/" + R.raw.aaa;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.start();
        return videoView;
    }
}
