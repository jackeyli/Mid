Ext.define('app.view.SemesterWindow', {
    extend: 'Ext.window.Window',
    alias:'widget.loginwindow',
    xtype:'loginwindow',
    width:'500',
    height:'150',
    layout:'form',
    id:'newSemesterWindow',
    bodyPadding:'5 5 5 5',
    title:'New Semester',
    bodyStyle:{
        border:'0px'
    },
    bbar:[
        "->",
        {
            text: 'Create',
            handler: function(comp){
                Ext.Ajax.request({
                    url:'request/course/newSemester',
                    params:{semesterName:Ext.getCmp('semesterName').getValue()},
                    success:function(response){
                        responseWrapper((resp)=>{
                            Ext.getCmp('newSemesterWindow').close();
                            alert(resp);
                    })(response);
                    }
                })
            }
        },
        {
            text: 'Cancel',
            handler: function(comp){
                Ext.getCmp('newSemesterWindow').close();
            }
        }
    ],
    items:[
        {
            xtype:'textfield',
            id:'semesterName',
            fieldLabel: 'Semester Name'
        }
    ]
});