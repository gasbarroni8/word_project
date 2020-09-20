//输入提示框(*)
$("#searchWord").keyup(function (event) {
    let word = $("#searchWord").val();
    if(word == ""){
        $("#tips").fadeOut("normal");
        return;
    }

    //回车
    if(event.keyCode == 13){
        window.location.href="/wordect/gotoWordDetail?word="+ $("#searchWord").val();
    }

    //65-90为大小写,8为Backspace
    if(event.keyCode >= 65 && event.keyCode <= 90 || event.keyCode == 8){

        $("#tips").fadeIn("normal");

        //前缀查词
        let url = "/wordect/input/" + word;
        $.ajax({
            type: "post",
            async: true,     //异步执行
            contentType: "application/json;charset=UTF-8",
            url: url,
            dataType: "json", //json类型
            success: function (result) {
                if(result.code === "200"){
                    console.log(result)
                    $("#tips").html("");
                    $.each(result.data, function (i,item) {
                        $("#tips").append(" <div content='"+ item.word +"'>\n" +
                            "                        <li class=\"text-left\">"+ item.word +"</li>\n" +
                            "                        <li class=\"text-right\">"+ item.translation +"</li>\n" +
                            "                    </div>")
                    })
                    tipInit();
                } else if(result.code == 700){
                    // 该单词模糊查询已无结果
                    //关闭提示框
                    $("#tips").html("").fadeOut(400);
                }

            },
            error: function (errmsg) {
                console.log("Ajax获取服务器数据出错了！"+ errmsg);
            }
        })
    }




});

//提示框点击单词init
function tipInit() {
    $("#tips>div").click(function () {
        let word = $(this).attr("content");
        window.location.href="/wordect/gotoWordDetail?word="+ word;

    })
}

//单词搜索(按钮)
$("#searchBtn").click(function () {
    window.location.href="/wordect/gotoWordDetail?word="+ $("#searchWord").val();
})