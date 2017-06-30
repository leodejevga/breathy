package com.apps.philipps.opengltest.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.apps.philipps.source.helper._3D.Activity3D;

/**
 * Created by Jevgenij Huebert on 05.04.2017. Project Breathy
 */

public class TGame extends Activity3D {
    private ProgressDialog pd = null;
    private MyGLRenderer renderer3D = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = ProgressDialog.show(TGame.this, "Loading...",
                "Loading. Please wait...", true, false);
        new BackGroundTask().execute();
        renderer3D = new MyGLRenderer(this);
        openGL = new MyGLSurfaceView(this, renderer3D);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        openGL.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        openGL.onResume();
    }

    class BackGroundTask extends
            AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            // showDialog(AUTHORIZING_DIALOG);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (TGame.this.pd != null) {
                TGame.this.pd.dismiss();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            while (TGame.this.renderer3D.gameEngine == null) {
                //wait until object is loaded
            }
            TGame.this.pd.dismiss();
            return true;

        }

    }
}
