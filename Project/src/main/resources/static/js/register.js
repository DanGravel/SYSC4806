function checkUsername() {
    $.get("/register/user", {username: $("#username").val()}, function (data) {
        if (data.userExists) {
            $("#username").val("");
            $("#username").addClass("is-invalid")
        } else {
            $("#username").removeClass("is-invalid")
        }
    })
}