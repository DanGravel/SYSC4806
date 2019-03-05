const fileSizeLimit = 10000000; //10 MB in bytes
function checkFileSize(event) {
    var fileElement = document.getElementById("fileLoad");
    var fileArr = fileElement.files;

    if (!fileArr || !fileArr[0]) {
        alert("No file loaded");
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