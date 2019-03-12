const fileSizeLimit = 10000000; //10 MB in bytes
function checkFileSize(event) {
    var fileElement = document.getElementById("fileLoad");
    var fileArr = fileElement.files;

    if (!fileArr || !fileArr[0]) {
        $('#alert').removeClass("hidden");
        event.preventDefault();
        return;
    }

    var file = fileArr[0];

    if (file.size > fileSizeLimit) {
        alert("File too large");
        event.preventDefault();
        document.getElementById("fileUploadForm").reset();
        return;
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
    $(document).ready( function() {
        $(':file').on('fileselect', function(event, numFiles, label) {

            var input = $(this).parents('.input-group').find(':text'),
                log = numFiles > 1 ? numFiles + ' files selected' : label;

            if( input.length ) {
                input.val(log);
            } else {
                if( log ) alert(log);
            }

        });
    });

});