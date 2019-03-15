$(document).ready( function () {
    //The line below gets the little time icon when choosing the date. There are some bugs wrt bootstrap4 and this is the workaround
    $.fn.datetimepicker.Constructor.Default = $.extend({}, $.fn.datetimepicker.Constructor.Default, { icons: { time: 'fas fa-clock', date: 'fas fa-calendar', up: 'fas fa-arrow-up', down: 'fas fa-arrow-down', previous: 'far fa-chevron-left', next: 'far fa-chevron-right', today: 'far fa-calendar-check-o', clear: 'far fa-trash', close: 'far fa-times' } });
    $('#editor_table').DataTable();
} );


$(function() {
    $("[id*='datetimepicker']").datetimepicker({useCurrent : false, sideBySide : true, keepOpen : true}).on("change.datetimepicker",(function (e) {
        $('#date_form').submit();
    }));
});