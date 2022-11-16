let questionApp=new Vue({
    el:"#questionApp",
    data:{
        question:{}
    },
    methods:{
        loadQuestion:function() {
            //location.search这个方法会返回url中?之后的内容
            //  细节1:如果url没有?或?之后没有值,这个方法返回null
            //  细节2:如果url有?并?后有值,这个方法返回包含?之后的内容
            let qid = location.search;
            console.log("qid:" + qid);
            // 如果qid不存在(如果qid是null)
            if (!qid) {
                alert("要查询的id不存在");
                return;
            }
            // 如果qid有值现在也是带着?的,例如
            // ?149
            // 0123
            qid = qid.substring(1);  // ?149  ->  149
            axios({
                url: "/v1/questions/" + qid,
                method: "get"
            }).then(function(response){
                questionApp.question=response.data;
                addDuration(questionApp.question);
            })
        }
    },
    created:function(){
        this.loadQuestion()
    }
})

let postAnswerApp=new Vue({
    el:"#postAnswerApp",
    data:{
    },
    methods:{
        postAnswer:function(){
            // 获得当前问题的id
            let qid=location.search;
            if(!qid){
                alert("必须提供问题id");
                return;
            }
            qid=qid.substring(1);
            // 获得答案输入框中的内容
            let content=$("#summernote").val();
            let form=new FormData();
            form.append("questionId",qid);
            form.append("content",content);
            axios({
                url:"/v1/answers",
                method:"post",
                data:form
            }).then(function(response){
                /*alert(response.data);
                alert("ok的类型是:"+typeof("ok"))
                alert("当前的返回值类型是:"+typeof(response.data))*/
                //判断新增返回的是不是一个对象
                if(typeof(response.data)=="object"){
                    //新增成功,将回答添加到回答列表中
                    answersApp.answers.push(response.data);
                    // 清空富文本编辑器的内容
                    $("#summernote").summernote("reset");
                    // 添加当前问题的持续时间为刚刚
                    response.data.duration="刚刚";
                }else{
                    //弹出失败原因
                    alert(response.data)
                }
            })
        }
    }
})

//处理回答详情的js和vue代码
let answersApp=new Vue({
    el:"#answersApp",
    data:{
        answers:[]
    },
    methods:{
        loadAnswers:function(){
            // 获得url地址末尾?之后的问题id
            let qid=location.search;
            if(!qid){
                alert("必须包含问题id");
                return;
            }
            qid=qid.substring(1);  //?149  -> 149
            axios({
                url:"/v1/answers/question/"+qid,
                method:"get"
            }).then(function(response){
                answersApp.answers=response.data;
                for(let i=0;i<answersApp.answers.length;i++){
                    addDuration(answersApp.answers[i]);
                }
            })
        },
        postComment:function(answerId){
            console.log("answerId:"+answerId);
            // 获得多行文本框对象
            let textarea=$("#addComment"+answerId+" textarea");
            // 获得多行文本框的内容
            let content=textarea.val();
            let form=new FormData();
            form.append("answerId",answerId);
            form.append("content",content);
            axios({
                url:"/v1/comments",
                method:"post",
                data:form
            }).then(function(response){
                // 判断返回的是不是一个对象
                if(typeof(response.data)=="object"){
                    let comment=response.data;
                    let answers=answersApp.answers;
                    //遍历当前问题的所有回答
                    for(let i=0;i<answers.length;i++){
                        // 判断当前遍历的回答id是不是本次新增评论对应的回答
                        if(answers[i].id==answerId){
                            // 如果对应,就将当前评论添加到当前回答的评论列表中
                            answers[i].comments.push(comment);
                            // 清空输入框内容
                            textarea.val("");
                            break;
                        }
                    }
                }
            })
        },
        removeComment:function(commentId,index,comments){
            if(!commentId){
                return;
            }
            axios({
                url:"/v1/comments/"+commentId+"/delete",
                method:"get"
            }).then(function(response){
                if(response.data=="ok"){
                    // splice是删除数组元素的方法
                    // splice([从下标为几的开始删除],[删除几个])
                    // 例如:comments.splice(2,1);
                    comments.splice(index,1);
                }else{
                    alert(response.data);
                }
            })
        },
        updateComment:function (commentId,answer,index) {
            let textarea=$("#editComment"+commentId+" textarea");
            let content=textarea.val();
            let form=new FormData();
            form.append("answerId",answer.id);
            form.append("content",content);
            axios({
                url:"/v1/comments/"+commentId+"/update",
                method:"post",
                data:form
            }).then(function(response){
                if(typeof(response.data)=="object"){
                    // 修改成功,使用Vue提供的一个方法修改数组中的元素
                    // 如果使用js的方法修改数组,页面中对应的数据可能会不变化
                    Vue.set(answer.comments , index , response.data);
                    //上面代码的意思是:
                    // 将answer.comments数组中的index位置的元素替换成response.data
                    // 修改成功后,修改输入框自动折起
                    $("#editComment"+commentId).collapse("hide");
                }else{
                    alert(response.data);
                }
            })
        },
        answerSolved:function(answerId){
            axios({
                url:"/v1/answers/"+answerId+"/solved",
                method:"get"
            }).then(function(response){
                alert(response.data);
            })
        }
    },
    created:function(){
        this.loadAnswers();
    }
})
