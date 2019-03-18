$(document).ready( function () {
    $('#editor_table').DataTable();
    $.fn.datetimepicker.Constructor.Default = $.extend({}, $.fn.datetimepicker.Constructor.Default, {
        useCurrent : false,
        sideBySide : true,
        keepOpen : true,
        buttons: {
            showClear: true,
            showClose: true,
            showToday: false
        },
        format: "dddd, MMMM D YYYY, h:mm A",
        icons: {
            time: 'fas fa-clock',
            date: 'fas fa-calendar',
            up: 'fas fa-arrow-up',
            down: 'fas fa-arrow-down',
            previous: 'fas fa-chevron-left',
            next: 'fas fa-chevron-right',
            clear: 'fas fa-trash',
            close: 'fas fa-check'
    }});
} );

