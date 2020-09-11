package com.bapercoding.simplecrud

import android.net.Uri

class Photo2 {
    //Getters and Setters
    var photo: Uri? = null

    constructor() {
        //Empty Constructor For Firebase
    }

    constructor(photo: Uri?) {
        this.photo = photo //Parameterized for Program-Inhouse objects.
    }

}