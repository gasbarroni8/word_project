/**
    收藏单词
 */

//关闭生词本框
function closeNotebookBox(){
    $(".notebookListBox").fadeOut(200);
    $(".closeNotebookListBox").fadeOut(200);
    $(".collectWordBox").css("pointer-event","all");

}
//生词本关闭按钮的事件
$(".closeNotebookListBox").click(function () {
    event.stopPropagation();
    closeNotebookBox();
});
//生词本框
let notebookList;
//
let notebookListFlag = false;
//保存notebook_word表的id
let notebook_word_id = 0;
//收藏/取消收藏单词显示框
$(".collectWordBox").click(function () {
    event.stopPropagation();
    let content = $(this).attr("content");
    //仍未收藏
    if(content == 0){
        //禁止收藏按钮点击
        $(".collectWordBox").css("pointer-event","none");
        $(".notebookListBox").fadeIn(200);
        $(".closeNotebookListBox").fadeIn(200);
        //只有当滚动框没有显示的时候才去显示
        if(!notebookListFlag){

            //查询该用户的所有生词本
            $.ajax({
                type: "post",
                async: true,
                contentType: "application/json;charset=UTF-8",
                url:"/notebook/myNotebookJson",
                dataType: "json",
                success:function (result) {
                    if(result.code == 200){
                        console.log(result);
                        $.each(result.notebooks,function (i,item) {
                            $(".notebookListBoxLists")
                                .append("<li content='"+ item.id +"'>" +item.name + "</li>")

                        });

                        //弹出所有生词本框
                        notebookList = new scrollbot(".notebookListBox",5);
                        notebookList.setStyle({
                            "background":"#000",
                            "z-index":"2222",
                            "border-radius":"5px"
                        },{
                            "background":"#c5b9b9",
                            'opacity':'.8',
                        });

                        let psuedo = document.createElement("div");
                        // psuedo.style.cssText = "height:100%;width:2px;left:4px;background:#808080;position:absolute;z-index:1";
                        //设置包裹滚动条的容器
                        notebookList.scrollBarHolder.appendChild(psuedo);

                        // 在窗口尺寸改变时重新计算滚动条的位置。
                        document.onreadystatechange = function(){
                            notebookList.refresh();
                        };
                        notebookListFlag = true;

                        //确认收藏单词
                        $(".notebookListBoxLists>li").click(function () {
                            let notebook_id = $(this).attr("content");
                            let data = {word:wordName,pron_us:pron_us,pron_uk:pron_uk,translation:paraphrase}
                            data = JSON.stringify(data);
                            $.ajax({
                                type: "post",
                                async: true,
                                data:data,
                                contentType: "application/json;charset=UTF-8",
                                url:"/notebook/addWordEct/"+ notebook_id,
                                dataType: "json",
                                success:function (result) {
                                    if(result.code == 200){
                                        console.log(result)
                                        //收藏成功
                                        $(".collectWordBox").html("已收藏")
                                            .css({"color":"#fdf1da","background-color":"#f27c7c"})
                                            .attr("content", result.wordEct.id);

                                        //提示框
                                        $(".tipBox").html("已收藏该单词!")
                                            .show().fadeOut(1000);

                                        //关闭盒子
                                        closeNotebookBox();

                                    }else if(result.code == 604){
                                        //生词本单词收藏失败
                                        //打印
                                        console.log(result);
                                        //提示框
                                        $(".tipBox").html("生词本单词添加失败!请重试!")
                                            .show().fadeOut(1000);
                                    }
                                },
                                error:function (errmsg) {
                                    console.log("Ajax获取服务器数据出错了！"+ errmsg);
                                }
                            })
                        });

                    }else if(result.code == 600){
                        //生词本单词删除失败
                        //打印
                        console.log(result);
                        //提示框
                        $(".tipBox").html("生词本列表为空")
                            .show().fadeOut(1000);
                    }
                },
                error:function (errmsg) {
                    console.log("Ajax获取服务器数据出错了！"+ errmsg);
                }
            });

        }

    }else {
        //已经收藏,此时在notebook_word中将该单词删除即可
        let url = "/notebook/removeWord/" + $(".collectWordBox").attr("content");
        $.ajax({
            type: "post",
            async: true,
            contentType: "application/json;charset=UTF-8",
            url:url,
            dataType: "json",
            success:function (result) {
                if(result.code == 200){
                    //删除成功
                    $(".collectWordBox").html("收藏")
                        .css({"color":"#63615c","background-color":"#bddffa"})
                        .attr("content", 0);

                    //提示框
                    $(".tipBox").html("已取消收藏该单词!")
                        .show().fadeOut(1000);

                }else if(result.code == 603){
                    //生词本单词删除失败
                    //打印
                    console.log(result);
                    //提示框
                    $(".tipBox").html("取消收藏失败!请刷新!")
                        .show().fadeOut(1000);
                }
            },
            error:function (errmsg) {
                console.log("Ajax获取服务器数据出错了！"+ errmsg);
            }
        })

    }
});
