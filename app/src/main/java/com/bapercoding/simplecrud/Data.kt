package com.bapercoding.simplecrud

object Data {

    private val filmDetail = arrayOf("Drama: Hospital Playlist\n" +
            "Country: South Korea\n" +
            "Episodes: 12\n" +
            "Aired: Mar 12, 2020 - May 28, 2020\n" +
            "Original Network: tvN, Netflix, Netflix, Netflix, Netflix\n" +
            "Duration: 1 hr. 30 min.\n" +
            "Content Rating: 15+ - Teens 15 or older",
            "Drama: It's Okay to Not Be Okay\n" +
                    "Country: South Korea\n" +
                    "Episodes: 16\n" +
                    "Aired: Jun 20, 2020 - Aug 9, 2020\n" +
                    "Original Network: tvN, Netflix, Netflix, Netflix, Netflix\n" +
                    "Duration: 1 hr. 15 min.\n" +
                    "Content Rating: 15+ - Teens 15 or older",
            "Drama: Dr. Romantic 2\n" +
                    "Country: South Korea\n" +
                    "Episodes: 16\n" +
                    "Aired: Jan 6, 2020 - Feb 25, 2020\n" +
                    "Original Network: SBS\n" +
                    "Duration: 1 hr. 10 min.\n" +
                    "Content Rating: 15+ - Teens 15 or older",
            "Drama: The World of the Married\n" +
                    "Country: South Korea\n" +
                    "Episodes: 16\n" +
                    "Aired: Mar 27, 2020 - May 16, 2020\n" +
                    "Original Network: jTBC\n" +
                    "Duration: 1 hr. 20 min.\n" +
                    "Content Rating: 18+ Restricted (violence & profanity)",
            "Drama: Itaewon Class\n" +
                    "Country: South Korea\n" +
                    "Episodes: 16\n" +
                    "Aired: Jan 31, 2020 - Mar 21, 2020\n" +
                    "Original Network: jTBC, Netflix, Netflix, Netflix, Netflix\n" +
                    "Duration: 1 hr. 10 min.\n" +
                    "Content Rating: 15+ - Teens 15 or older",
            "Drama: When the Weather Is Fine\n" +
                    "Country: South Korea\n" +
                    "Episodes: 16\n" +
                    "Aired: Feb 24, 2020 - Apr 21, 2020\n" +
                    "Original Network: jTBC, Viki\n" +
                    "Duration: 60 min.\n" +
                    "Content Rating: 15+ - Teens 15 or older",
            "Drama: Find Me in Your Memory\n" +
                    "Country: South Korea\n" +
                    "Episodes: 32\n" +
                    "Aired: Mar 18, 2020 - May 13, 2020\n" +
                    "Original Network: MBC\n" +
                    "Duration: 33 min.\n" +
                    "Content Rating: 15+ - Teens 15 or older",
            "Drama: My Holo Love\n" +
                    "Country: South Korea\n" +
                    "Episodes: 12\n" +
                    "Aired: Feb 7, 2020\n" +
                    "Original Network: Netflix, Netflix, Netflix, Netflix\n" +
                    "Duration: 55 min.\n" +
                    "Content Rating: 13+ - Teens 13 or older",
            "Drama: A Piece of Your Mind\n" +
                    "Country: South Korea\n" +
                    "Episodes: 12\n" +
                    "Aired: Mar 23, 2020 - Apr 28, 2020\n" +
                    "Original Network: tvN\n" +
                    "Duration: 1 hr. 10 min.\n" +
                    "Content Rating: 15+ - Teens 15 or older",
            "Drama: The King: Eternal Monarch\n" +
                    "Country: South Korea\n" +
                    "Episodes: 16\n" +
                    "Aired: Apr 17, 2020 - Jun 12, 2020\n" +
                    "Original Network: SBS, Netflix, Netflix, Netflix, Netflix\n" +
                    "Duration: 1 hr. 10 min.\n" +
                    "Content Rating: 15+ - Teens 15 or older",
            "Drama: Dinner Mate\n" +
                    "Country: South Korea\n" +
                    "Episodes: 32\n" +
                    "Aired: May 25, 2020 - Jul 14, 2020\n" +
                    "Original Network: MBC\n" +
                    "Duration: 35 min.\n" +
                    "Content Rating: 15+ - Teens 15 or older")
    private val filmImages = intArrayOf(R.drawable.hospital_playlist,
            R.drawable.its_okay,
            R.drawable.dr_kim,
            R.drawable.world_married,
            R.drawable.itaewon_class,
            R.drawable.when_weather,
            R.drawable.find_me,
            R.drawable.my_holo,
            R.drawable.a_piece,
            R.drawable.eternal_monarch,
            R.drawable.dinner_mate)

    val listData: ArrayList<Film>
        get() {
            val list = arrayListOf<Film>()
            for (position in filmDetail.indices) {
                val film = Film()
                film.detail = filmDetail[position]
                film.photo = filmImages[position]
                film.rating = R.drawable.rating
                list.add(film)
            }
            return list
        }
}

data class Film(
        var detail: String = "",
        var rating: Int = 0,
        var photo: Int = 0
)