Ext.define('app.view.LoginWindow', {
    extend: 'Ext.window.Window',
    alias:'widget.loginwindow',
    xtype:'loginwindow',
    width:'500',
    height:'300',
    layout:'form',
    id:'loginWindow',
    title:'Login',
    bodyPadding:'5 5 5 5',
    bodyStyle:{
        border:'0px'
    },
    bbar:[
        "->",
        {
            text: 'Regist',
            handler: function(comp){
                Ext.getCmp('loginWindow').close();
                Ext.create('app.view.RegistWindow').show();
            }
        },
        {
            text : 'Login',
            handler : function() {
                Ext.Ajax.request({
                    scope:this,
                    params:{userId:Ext.getCmp('userName').getValue(),
                        password:Ext.getCmp('passWord').getValue()},
                    url:'request/login/login',
                    success:function(response){
                        responseWrapper((resp)=>{
                            window.location.reload();
                    })(response);
                    }
                });
            }
        }
    ],
    items:[
        {
            xtype:'textfield',
            id:'userName',
            fieldLabel: 'User Name'
        },
        {
            xtype:'textfield',
            id:'passWord',
            inputType:'password',
            fieldLabel:'Password'
        }
    ]
});