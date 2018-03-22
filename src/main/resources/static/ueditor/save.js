function AddConvention() {

    var content = UE.getEditor('editor').getContent();
    alert('content',content);
    var title = document.getElementById("title").value();
    if ($("title").val()==""){
        alert('提示','标题不能为空！');
        return;
    }
    $.ajax({
        type:"post",
        url:"http://127.0.0.1:8080/blog/add",
        data:{
            "content":content,
            "title":title,
        },
        success:function (data) {
            if (data ==true){
                alert('提醒','添加成功！');
            }else {
                alert('提示','添加失败');
            }
        }
    })
}
function getCookieValue(name){
    var name = escape(name);  //name为指定的名称
    var allcookies = document.cookie;
    name += "=";
    var pos = allcookies.indexOf(name);
    if(pos != -1){
        var start = pos + name.length;
        var end = allcookies.indexOf(";",start);
        //这里是根据;分隔符来分隔出该名称的值，如果在设置Cookie时用的是,分隔，请替换成相应符号。
        if(end == -1){
            end = allcookies.length;
        }
        var value = allcookies.substring(start,end);
        return unescape(value);
    } else{
        return "";
    }
}