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
let wordsData = [];
let word = {
    id: 0,
    memorized_count: 0
};
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
        if (wordFlag === word) {
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
            url: "/wordect//findWordDetailJson/" + word,
            dataType: "json", //json类型
            success: function (result) {
                console.log(result);

                if (result.code === "200") {
                    let word = result.data;
                    //填入相应数据
                    $(".wordName .word").html(word.word);
                    $(".wordPron #us").html(word.pron_us);
                    $(".wordPron #uk").html(word.pron_uk);
                    // $(".wordPron #soundUK").attr('src',"http://media.shanbay.com/audio/uk/"+ word.word +".mp3");
                    // $(".wordPron #soundUS").attr('src',"http://media.shanbay.com/audio/us/"+ word.word +".mp3");
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
                    //重新写入例句
                    $.each(word.sentences, function (i, item) {
                        $(".sentences").append("<div class=\"row sentence\">\n" +
                            "        <div class=\"col-xs-12 sentence_en\">\n" +
                            "            " + parseInt(parseInt(i) + 1) + ". <p>" + item.sentence_en + "</p>\n" +
                            "        </div>\n" +
                            "\n" +
                            "        <div class=\"col-xs-12 sentence_cn\">\n" +
                            "            <p>" + item.sentence_cn +
                            "                <span class=\"glyphicon glyphicon-volume-down sentencePron\"></span></p>\n" +
                            "        </div>\n" +
                            "    </div>")
                    })

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


    //默认查询，返回15个复习词
    let data = {curr: 1, size: 15};
    data = JSON.stringify(data);
    $.ajax({
        type: "post",
        async: true,     //异步执行
        data: data,
        contentType: "application/json;charset=UTF-8",
        url: "/words/getReviewWords/" + wordbook_id,
        dataType: "json", //json类型
        success: function (result) {
            // var result = JSON.stringify(result);  // 需解析，否则返回 object
            console.log(result);
            if (result.code === "200") {
                words = result.data;

                let word = words[0];
                $("#word").html(word.word);
                $("#pron_uk").html(word.pron_uk);
                $("#pron_us").html(word.pron_us);
                $("#meaning").html(word.paraphrase);

            } else if (result.code === "507") {
                // TODO 修改为提示框
                alert("复习完了所有的单词！");
                window.location.href = "/";
            }
            currWord++;
            init()
        },
        error: function (errmsg) {
            console.log("Ajax获取服务器数据出错了！" + errmsg);
        }
    });

    //点击下一个
    $(".nextWord").click(function () {
        //如果没有背完15个单词
        if (currWord < 15) {
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
        } else {
            wordsData = [];
            for (let i = 0; i < words.length; i++) {
                // console.log(words[i].id);
                // word.id=words[i].id;
                // word.memorized_count=words[i].memorized_count;
                wordsData.push({id: words[i].id, memorized_count: words[i].memorized_count});
            }

            //提交
            $.ajax({
                type: "post",
                async: true,     //异步执行
                data: JSON.stringify(wordsData),
                contentType: "application/json;charset=UTF-8",
                url: "/wordbook/myWordbook/review",
                dataType: "json", //json类型
                success: function (result) {
                    // console.log(result);
                    if (result.code === "200") {

                    }
                },
                error: function (errmsg) {
                    console.log("Ajax获取服务器数据出错了！" + errmsg);
                }
            });
            //再来一组？
            $("#again").modal("show");
        }


    })
};

//获取页面参数
function getQueryVariable(variable) {
    let query = window.location.search.substring(1);
    let vars = query.split("&");
    for (let i = 0; i < vars.length; i++) {
        let pair = vars[i].split("=");
        if (pair[0] == variable) {
            return pair[1];
        }
    }
    return (false);
}

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
        url: "/wordbook/myWordbook/words/" + wordbook_id + "/2",
        dataType: "json", //json类型
        success: function (result) {
            // var result = JSON.stringify(result);  // 需解析，否则返回 object
            // console.log(result);
            if (result.code === "200") {
                words = result.data;

                let word = words[0];
                $("#word").html(word.word);
                $("#pron_uk").html(word.pron_uk);
                $("#pron_us").html(word.pron_us);
                $("#meaning").html(word.paraphrase);
                $("#again").modal('hide')

            } else if (result.code == 507) {
                alert("复习完了所有的单词！");
                window.location.href = "/";
            }
            currWord++;
            init()
        },
        error: function (errmsg) {
            console.log("Ajax获取服务器数据出错了！" + errmsg);
        }
    });
});

$(".backIndex").click(function () {
    window.location.href = "/";
})