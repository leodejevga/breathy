package com.apps.philipps.source.interfaces;

import android.widget.VideoView;

/**
 * Created by Jevgenij Huebert on 27.01.2017. Project Breathy
 */
public interface IGame {
    /**
     * Start the game.
     *
     * @return true if the game successfully started
     */
    public boolean startGame();

    /**
     * Start the options.
     *
     * @return true if the options successfully started
     */
    public boolean startOptions();

    /**
     * Start the preview.
     *
     * @return true if the preview successfully started
     */
    public VideoView startPreview(VideoView videoView);

    /**
     * Returns <code>true</code> if this game was bought.
     *
     * @return true if this game was bought
     */
    public boolean isBought();

    /**
     * Buy this game.
     *
     * @return true if player had enough amount of coins
     */
    public boolean buy();

    /**
     * Name of the game.
     *
     * @return name of the game
     */
    public String getName();
}
