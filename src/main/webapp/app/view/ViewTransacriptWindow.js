Ext.define('app.view.ViewTransacriptWindow', {
    extend: 'Ext.window.Window',
    alias:'widget.viewTransacriptWindow',
    xtype:'viewTransacriptWindow',
    width:'500',
    height:'500',
    layout:'fit',
    id:'viewTransacriptWindow',
    bodyPadding:'5 5 5 5',
    bodyStyle:{
        border:'0px'
    },
    title:'View Transacript',
    bbar:[
        "->",
        {
            text: 'Cancel',
            handler: function(comp){
                Ext.getCmp('viewTransacriptWindow').close();
            }
        }
    ],
    items:[
        {
            xtype:'gridpanel',
            store: Ext.create('Ext.data.Store',{
                fields:['courseName','grade'],
                data:[]
            }),
            id:'transcriptGrid',
            region:'center',
            layout:'fit',
            columns: [
                {text: 'Course Id',  dataIndex:'courseName',width:'50%'},
                {text: 'Grade',  dataIndex:'grade',width:'50%'}
            ]
        }
    ],
    listeners:{
        'show':function(){
            Ext.Ajax.request({
                scope:this,
                params:{semesterName:Ext.getCmp('semesterCombobox').getValue()},
                url:'request/course/viewTranscript',
                success:function(response){
                    responseWrapper((resp)=>{
                        Ext.getCmp('transcriptGrid').getStore().loadData(resp.filter((t)=>t != null));
                })(response);
                }
            });
        }
    }
});