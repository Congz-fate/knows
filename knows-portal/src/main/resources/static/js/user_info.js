let userApp=new Vue({
    el:"#userApp",
    data:{
        user:{}
    },
    methods:{
        loadUserInfo:function(){
            axios({
                url:"/v1/users/me",
                method:"get"
            }).then(function(response){
                userApp.user=response.data;
            })
        }
    },
    created:function(){
        // 页面加载完毕运行的方法
        this.loadUserInfo();
    }
})