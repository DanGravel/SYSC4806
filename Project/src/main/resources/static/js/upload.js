function checkFileSize(event) {
    var tenMegabytes = 10000000; //10 MB in bytes
    var fileElement = document.getElementById("fileLoad");
    var fileArr = fileElement.files;

    if (!fileArr || !fileArr[0]) {
        alert("No file loaded");
        event.preventDefault();
        return;
    }

    var file = fileArr[0];

    if (file.size > tenMegabytes) {
        alert("File too large");
        event.preventDefault();
        document.getElementById("fileUploadForm").reset();
        return;
    }
}