package pl.bas.microtwitter.helpers

import pl.bas.microtwitter.dto.SignupDTO

object AuthHelper {
    var user1: SignupDTO = SignupDTO("","","","")
        get() = SignupDTO(
                username = "JanKowalski",
                email = "jan@kowalski.com",
                fullName = "Jan Kowalski",
                password = "admin123"
        )

    var user2: SignupDTO = SignupDTO("","","","")
        get() = SignupDTO(
                username = "AdamNowak",
                email = "adam@nowak.com",
                fullName = "AdamNowak",
                password = "admin1234"
        )

    var user3: SignupDTO = SignupDTO("","","","")
        get() = SignupDTO(
                username = "StefanBatory",
                email = "stefan@Batory.pl",
                fullName = "Stefan Batory",
                password = "password"
        )

//    fun login(http: TestRestTemplate,
//              user: SignupDTO = user1):  {
//
//    }

//    fun login()
}