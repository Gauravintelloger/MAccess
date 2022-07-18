package technology.dubaileading.maccessemployee.rest.entity

class LoginResponse (
    var status : String,
    var statuscode : String,
    var message : String,
    var token : String,
    var data : Data,
)

class Data(
    var id : Int,
    var username : String,
    var status_id : Int,
    var role_id : Int,
)