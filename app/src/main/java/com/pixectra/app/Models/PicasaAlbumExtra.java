package com.pixectra.app.Models;

/**
 * Created by Admin on 3/25/2018.
 */

public class PicasaAlbumExtra {
    private String albumId, albumName, numberOfPics;

    public PicasaAlbumExtra(String albumId, String albumName, String numberOfPics) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.numberOfPics = numberOfPics;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getNumberOfPics() {
        return numberOfPics;
    }
}
