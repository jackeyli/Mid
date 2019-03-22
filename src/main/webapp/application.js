var responseWrapper = function(func){
    return function(response){
        var respObj = JSON.parse(response.responseText);
        if(respObj.errorMessage){
            alert(respObj.errorMessage)
        } else {
            func.apply(this,[respObj.value]);
        }
    }
}
var getWindowParam = function(param){
    var reg = '[\\?|&]' + param + '=([^&]*)',
        ret = window.location.search.match(new RegExp(reg));
    if(ret)
        return ret[1];
    return null;
}
Ext.onReady(function(){
    Ext.application({
        controllers:['Ext.app.Controller'],
        name: 'app',
        appFolder: 'app',
        launch: function() {
            Ext.Ajax.request({
                scope:this,
                params:{},
                url:'request/login/login',
                success:function(response){
                    var respObj = JSON.parse(response.responseText);
                    if(!respObj.errorMessage){
                        document.getElementById('welcomeUser').innerText = 'Welcome ' + respObj.value.name + '!';
                        window.UserInfo = respObj.value;
                    }
                    Ext.create('app.view.HomePage', {
                        layout: 'fit',
                        renderTo:document.getElementById("content")
                    });
                }
            });
        }
    });
});
