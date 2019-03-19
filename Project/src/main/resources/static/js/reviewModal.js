$( document ).ready(function() {
    $('#reviewModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget)
        var articleId = button.data('id')
        console.log(articleId)
        var modal = $(this)
        modal.find('#modalArticleId').val(articleId)
    })
});
