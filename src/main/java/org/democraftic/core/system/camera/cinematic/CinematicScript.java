package org.democraftic.core.system.camera.cinematic;

import java.util.ArrayList;

public class CinematicScript extends ArrayList<CinematicChapter> {
    private int iterator = 0;
    public CinematicChapter getNextChapter(){
        iterator ++;
        return get(iterator-1);
    }

    public boolean isLastChapter(){
        return iterator >=size();
    }

}
