function readPicture(input, output)
{
    if (input.files && input.files[0])
    {
        var reader = new FileReader();
        reader.onload = function (e)
        {
            output.attr('src', e.target.result);
        };
        reader.readAsDataURL(input.files[0]);
    }
}

$("#fileUpload :input").change(
    function (){
        readPicture(this, $("#image-asociadaAFileUpload"));
    }
);



