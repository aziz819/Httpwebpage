
$(function(){
    $('button').click(function(){
      if($(this).hasClass('.three')){
        $(this).removeClass('.three');
        $('.one img').slideDown();
          $(this).text("hide");
      }else{
        $(this).addClass('.three');
        $('.one img').fadeOut();
          $(this).text("show");
      }

    });
});
