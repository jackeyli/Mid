Ext.define('app.view.RegistWindow', {
    extend: 'Ext.window.Window',
    alias:'widget.registWindow',
    xtype:'registWindow',
    width:'500',
    height:'300',
    layout:'form',
    id:'registWindow',
    title:'Regist',
    bodyPadding:'5 5 5 5',
    bodyStyle:{
        border:'0px'
    },
    bbar:[
        "->",
        {
            text : 'Regist',
            handler : function() {
                Ext.Ajax.request({
                    scope:this,
                    params:{userId:Ext.getCmp('userName').getValue(),
                        password:Ext.getCmp('passWord').getValue(),
                        type:Ext.getCmp('role').getValue()
                    },
                    url:'request/login/regist',
                    success:function(response){
                        responseWrapper((resp)=>{
                            window.location.reload();
                    })(response);
                    }
                });
            }
        },
        {
            text: 'Cancel',
            handler: function(comp){
                Ext.getCmp('registWindow').close();
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
        },
        {
            xtype:'combobox',
            fieldLabel:'Role',
            id:'role',
            displayField: 'display',
            valueField: 'value',
            store:Ext.create('Ext.data.Store',{
                fields:['display','value'],
                data:[{"display":'Teacher',value:'TEACHER'},
                    {"display":'Student',value:'STUDENT'},
                    {"display":'Finance Manager',value:'FINANCEMANAGER'},
                    {"display":'Course Manager',value:'COURSEMANAGER'}]
            }),
            listeners:{
                'afterrender':function(comp){
                    comp.setValue('TEACHER');
                }
            }
        }
    ]
});