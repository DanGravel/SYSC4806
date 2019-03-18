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
    $('.alert').show();
}


$(function() {

    $('.custom-file-input').on('change', function() {
        if (!this.files || !this.files[0]) {
            return;
        }

        let fileName = this.files[0].name;
        $(this).siblings('.custom-file-label').addClass("selected").html(fileName);
    });

    $('#close').click(function () {
        $('.alert').hide();
    })

});