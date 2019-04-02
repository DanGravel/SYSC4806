$(document).ready( function () {
    $("#reviewer_table").DataTable({
        "columnDefs": [{
            "targets": "no-sort",
            "orderable": false,
        }],
        "order": [[ 2, "desc" ]]
    });
});