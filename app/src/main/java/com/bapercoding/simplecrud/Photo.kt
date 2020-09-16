package com.bapercoding.simplecrud

class Photo {
    //Getters and Setters
    var photo: String? = null

    constructor() {
        //Empty Constructor For Firebase
    }

    constructor(photo: String?) {
        this.photo = photo
    }

}