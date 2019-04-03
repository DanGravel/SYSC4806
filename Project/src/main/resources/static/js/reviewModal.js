$(document).ready(function () {
    $('#reviewModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var id = button.data("id");
        var form = $("#fileUploadForm");
        form.attr("action", "/review/" + id)
    })
});
