package com.bapercoding.simplecrud

class ApiEndPoint {

    companion object {

        private val SERVER = "http://192.168.42.159/anows/simplecrud/"
        val CREATE = SERVER+"create.php"
        val READ = SERVER+"read.php"
        val DELETE = SERVER+"delete.php"
        val UPDATE = SERVER+"update.php"
        private val SERVER2 = "http://10.0.2.2/kdrama/simplecrud/"
        val CREATE2 = SERVER2+"create.php"
        val READ2 = SERVER2+"read.php"
        val DELETE2 = SERVER2+"delete.php"
        val UPDATE2 = SERVER2+"update.php"


    }

}