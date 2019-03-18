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

    // We can attach the `fileselect` event to all file inputs on the page
    $(document).on('change', ':file', function() {
        var input = $(this),
            numFiles = input.get(0).files ? input.get(0).files.length : 1,
            label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
        input.trigger('fileselect', [numFiles, label]);
    });

    // We can watch for our custom `fileselect` event like this
    $(':file').on('fileselect', function(event, numFiles, label) {

        var input = $(this).parents('.input-group').find(':text'),
            log = numFiles > 1 ? numFiles + ' files selected' : label;

        if( input.length ) {
            input.val(log);
        } else {
            if( log ) alert(log);
        }

    });

    $('.close').click(function () {
        $('.alert').hide();
    })

});