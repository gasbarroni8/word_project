function init() {
    //发音
    $(".pron_us_volume").click(function () {
        let word = $("#word").html();
        playUs(word)
    });
    //发音
    $(".pron_uk_volume").click(function () {
        let word = $("#word").html();
        playUk(word)
    });
    //例句发音
    $(".sentencePron").click(function () {
        let sen = $(this).attr("content");
        playSen(sen)
    });
}

// 美音
function playUs(word) {
    let src = "http://media.shanbay.com/audio/us/" + word + ".mp3";
    $("#sound").attr("src", src);
    $("#sound")[0].play();
}

// 英音
function playUk(word) {
    let src = "http://media.shanbay.com/audio/us/" + word + ".mp3";
    $("#sound").attr("src", src);
    $("#sound")[0].play();
}

// 例句发音
function playSen(sen) {
    let src = "http://dict.youdao.com/dictvoice?audio=" + sen + "&type=2";
    $("#sound").attr("src", src);
    $("#sound")[0].play();
}

//显示释义
function showMean() {
    $("#meanBtn").toggle();
    $(".meaningArea").toggle(200);
}

//点击释义又消失
$(".meaningArea").click(function () {
    $(".meaningArea").toggle();
    $("#meanBtn").toggle(200);
});

// 返回上一个界面
function returnIndex() {
    window.history.back(-1);
}

//新词数组
let words = [];
let ids = [];
//当前背词词汇
let currWord = 0;
//获取单词详情flag，防止多次重复获取wordDetail，浪费资源
let wordFlag = "";
let curr = 1;
//词书id
let wordbook_id = getQueryVariable("wordbook_id");
window.onload = function (ev) {

    //点击详情
    $(".wordDetail").click(function () {
        //获取word
        let word = $("#word").html().trim();
        if (wordFlag == word) {
            $(".w1").toggle(200);
            $(".wordDetailBox").toggle();
            return
        } else {
            wordFlag = word;
        }

        //查询该单词的详细数据
        $.ajax({
            type: "post",
            async: true,     //异步执行
            contentType: "application/json;charset=UTF-8",
            url: "/wordect/findWordDetailJson/" + word,
            dataType: "json", //json类型
            success: function (result) {
                // console.log(result);
                if (result.code === "200") {
                    let word = result.data;
                    //填入相应数据
                    $(".wordName .word").html(word.word);
                    $(".wordPron #us").html(word.pron_us);
                    $(".wordPron #uk").html(word.pron_uk);

                    //单词分类处理
                    if (word.tag != null) {
                        $(".classification #wordTag").html(word.tag);
                    } else {
                        $(".classification #wordTag").html("暂无分类");
                    }
                    //释义处理，因为这个结果返回的释义用\n换行，所以要将其替换为<br>
                    $(".wordPara .wordParaP").html(word.translation.replace(/\\n/gi, "<br>"));
                    //清空例句
                    $(".sentences").html("");
                    //如果找不到例句，直接返回
                    if (true === word.sentences && null != word.sentences && "" !== word.sentences) {
                        //重新写入例句
                        $.each(word.sentences, function (i, item) {
                            $(".sentences").append("<div class=\"row sentence\">\n" +
                                "        <div class=\"col-xs-12 sentence_en\">\n" +
                                "            <p>" + parseInt(parseInt(i) + 1) + ". " + item.en +
                                "               <span content='" + item.en + "' class='glyphicon glyphicon-volume-down sentencePron'></span>" +
                                "           </p> " +
                                "        </div>\n" +
                                "\n" +
                                "        <div class=\"col-xs-12 sentence_cn\">\n" +
                                "            <p>" + item.cn +
                                "                </p>\n" +
                                "        </div>\n" +
                                "    </div>")
                        })
                    }


                }

                //显示和隐藏
                $(".w1").toggle(200);
                $(".wordDetailBox").toggle();

                //事件更新
                init();

            },
            error: function (errmsg) {
                console.log("Ajax获取服务器数据出错了！" + errmsg);
            }

        })
    });

    // 点击掌握
    $(".setOK").click(function () {
        if (currWord < 1) return;
        // 异步提交
        $.ajax({
            type: "post",
            async: true,     //异步执行
            data: data,
            contentType: "application/json;charset=UTF-8",
            // id 未背->掌握时id为words的id，其余为记忆表的id
            // type 记忆中->掌握 : 1, 未背->掌握 : 2, 掌握->未背 : 3
                url: "/wordbook/myWordbook/words/wordTypeChange/" + wordbook_id + "/" + words[currWord - 1].id + "/2",
            dataType: "json", //json类型
            success: function (result) {
                console.log(result);
                if (result.code === "200") {
                    // TODO 以后可以做一些成功动画
                    // 显示下一个单词
                    showNextWord()
                } else if (result.code === "-1") {
                    // TODO 模块框提醒失败
                }
            },
            error: function (errmsg) {
                console.log("Ajax获取服务器数据出错了！" + errmsg);
            }
        });

    });

    // 显示下一个单词
    function showNextWord() {
        //更新顶部待学新词
        $(".newWordCount").html((15 - currWord));
        //获取下一个新词
        let newWord = words[currWord++];
        //更新相应数据
        $("#word").html(newWord.word);
        $("#pron_uk").html(newWord.pron_uk);
        $("#pron_us").html(newWord.pron_us);
        $("#meaning").html(newWord.paraphrase);
        init();
        //确保回到原先的背词界面
        $(".w1").show(200);
        $("#meanBtn").show(200);
        $(".meaningArea").hide(200);
        $(".wordDetailBox").hide(200);
    }

    // 默认查询，返回15个新词
    let data = {curr: curr, size: 15};
    data = JSON.stringify(data);
    $.ajax({
        type: "post",
        async: true,     //异步执行
        data: data,
        contentType: "application/json;charset=UTF-8",
        url: "/wordbook/myWordbook/words/" + wordbook_id + "/1",
        dataType: "json", //json类型
        success: function (result) {
            // var result = JSON.stringify(result);  // 需解析，否则返回 object
            console.log(result);
            if (result.code === "200") {
                words = result.data;
                if (words.length > 0) {
                    let word = words[0];
                    $("#word").html(word.word);
                    $("#pron_uk").html(word.pron_uk);
                    $("#pron_us").html(word.pron_us);
                    $("#meaning").html(word.paraphrase);
                } else {
                    alert("学习完了所有的单词！");
                    window.location.href = "/";
                }
            }
            currWord++;
            curr++;
            init();
        },
        error: function (errmsg) {
            console.log("Ajax获取服务器数据出错了！" + errmsg);
        }
    });


    //点击下一个
    $(".nextWord").click(function () {
        //如果没有背完15个单词
        if (currWord < 15) {
            showNextWord();
        } else {
            for (let i = 0; i < words.length; i++) {
                ids[i] = words[i].id;
            }
            //已经背完15个单词，应该有提示，问是否再来一组，或者复习
            $("#again").modal("show");
            //将刚才的单词插入数据库
            $.ajax({
                type: "post",
                async: true,     //异步执行
                data: JSON.stringify(ids),
                contentType: "application/json;charset=UTF-8",
                url: "/wordbook/myWordbook/insert/" + wordbook_id,
                dataType: "json", //json类型
                success: function (result) {
                    console.log(result);
                    if (result.code === "200") {

                    }
                },
                error: function (errmsg) {
                    console.log("Ajax获取服务器数据出错了！" + errmsg);
                }
            });
        }

    })
};

//再来一组
$(".sure").click(function () {
    currWord = 0;
    $(".newWordCount").html(15);
    let data = {curr: curr, size: 15};
    data = JSON.stringify(data);
    $.ajax({
        type: "post",
        async: true,     //异步执行
        data: data,
        contentType: "application/json;charset=UTF-8",
        url: "/wordbook/myWordbook/words/" + wordbook_id + "/1",
        dataType: "json", //json类型
        success: function (result) {
            // var result = JSON.stringify(result);  // 需解析，否则返回 object
            console.log(result);
            if (result.code === "200") {
                words = result.data;
                if (words.length > 0) {
                    let word = words[0];
                    $("#word").html(word.word);
                    $("#pron_uk").html(word.pron_uk);
                    $("#pron_us").html(word.pron_us);
                    $("#meaning").html(word.paraphrase);
                    $("#again").modal('hide')

                } else {
                    alert("学习完了所有的单词！");
                    window.location.href = "/";
                }
            }
            currWord++;
            curr++;
            init();
            $("#again").modal("hide")
        },
        error: function (errmsg) {
            console.log("Ajax获取服务器数据出错了！" + errmsg);
        }
    });
});

$(".backIndex").click(function () {
    window.location.href = "/";
});

//获取页面参数
function getQueryVariable(variable) {
    let query = window.location.search.substring(1);
    let vars = query.split("&");
    for (let i = 0; i < vars.length; i++) {
        let pair = vars[i].split("=");
        if (pair[0] === variable) {
            return pair[1];
        }
    }
    return false;
}
