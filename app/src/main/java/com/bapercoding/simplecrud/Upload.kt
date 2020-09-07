package com.bapercoding.simplecrud

import com.google.firebase.database.IgnoreExtraProperties


/**
 * Created by Belal on 2/23/2017.
 */
@IgnoreExtraProperties
class Upload {
    var name: String? = null
    var url: String? = null

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    constructor() {}
    constructor(name: String?, url: String?) {
        this.name = name
        this.url = url
    }

}