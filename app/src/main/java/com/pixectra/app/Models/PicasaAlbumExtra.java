package com.pixectra.app.Models;

/**
 * Created by Admin on 3/25/2018.
 */

public class PicasaAlbumExtra {
    String albumId, albumName;

    public PicasaAlbumExtra(String albumId, String albumName) {
        this.albumId = albumId;
        this.albumName = albumName;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getAlbumName() {
        return albumName;
    }
}
