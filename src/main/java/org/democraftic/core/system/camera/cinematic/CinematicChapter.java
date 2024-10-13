package org.democraftic.core.system.camera.cinematic;

import com.google.errorprone.annotations.ForOverride;
import org.bukkit.Location;

public class CinematicChapter {
    public final Location location;
    public final int chapterTime;


    public void onChapterStart(Cinematic cinematic){
        cinematic.getCamera().moveTo(location);
    }

    public void onChapterEnd(Cinematic cinematic){

    }

    public CinematicChapter(Location location,int chapterTime) {
        this.location = location;
        this.chapterTime = chapterTime;
    }
}
