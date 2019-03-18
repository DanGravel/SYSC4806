const fileSizeLimit = 10000000; //10 MB in bytes
function checkFileSize(event) {
    var fileElement = document.getElementById("fileLoad");
    var fileArr = fileElement.files;

    if (!fileArr || !fileArr[0]) {
        alertUpload("ERROR: No File Selected")
        event.preventDefault();
        return;
    }

    var file = fileArr[0];

    if (file.size > fileSizeLimit) {
        alertUpload("ERROR: File Too Large");
        event.preventDefault();
        document.getElementById("fileUploadForm").reset();
        return;
    } else if (file.size <= 0) {
        alertUpload("ERROR: File Contains no Data");
        event.preventDefault();
        document.getElementById("fileUploadForm").reset();
        return;
    }

}

function alertUpload(errorMsg) {
    document.getElementById("errorMsg").innerText = errorMsg;
    $("#errorAlert").show();
}

function deleteFile(id) {
    let isConfirmed = confirm("Are you sure you would like to delete this file?");
    if (isConfirmed) {
        $.ajax({
            url: "/upload/" + id,
            type: "DELETE",
            contentType: "application/json",
            headers: {
                'X-CSRF-TOKEN': $('[name="_csrf"]').attr('value')
            },
            success: function () {
                $("#article" + id).remove();
                $("#successAlert").show();
            }
        });
    }
}

$(function() {

    $(".custom-file-input").on("change", function() {
        if (!this.files || !this.files[0]) {
            alertUpload("ERROR: No File Selected")
            return;
        }

        let fileName = this.files[0].name;
        $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    });

    $('.close').click(function () {
        $('.alert').hide();
    })

});