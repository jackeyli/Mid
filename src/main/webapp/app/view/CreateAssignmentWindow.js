Ext.define('app.view.CreateAssignmentWindow', {
    extend: 'Ext.window.Window',
    alias:'widget.createAssignmentWindow',
    xtype:'createAssignmentWindow',
    width:'500',
    height:'500',
    layout:'fit',
    id:'createAssignmentWindow',
    title:'Create & Update Assignment',
    bodyPadding:'5 5 5 5',
    bodyStyle:{
        border:'0px'
    },
    bbar:[
        "->",
        {
            text: 'Submit',
            handler: function(comp){
                Ext.getCmp('createAssignmentWindow').getComponent('createAssignmentFormData').updateRecord();
                var record = Ext.getCmp('createAssignmentWindow').getComponent('createAssignmentFormData').getRecord().data;
                var url = record['id'] <= 0 ? 'request/assignment/createAssignment' : 'request/assignment/updateAssignment'
                Ext.Ajax.request({
                    url:url,
                    jsonData:JSON.stringify(record),
                    success:function(response){
                        responseWrapper((resp)=>{
                            alert(resp);
                        Ext.getCmp('CourseSelectCombo').fireEvent('change');
                        Ext.getCmp('createAssignmentWindow').close();
                    })(response);
                    }
                });
            }
        },
        {
            text: 'Cancel',
            handler: function(comp){
                Ext.getCmp('createAssignmentWindow').close();
            }
        }
    ],
    items:[
        {
            xtype:'form',
            layout:'form',
            itemId:'createAssignmentFormData',
            bodyStyle:{
                border:'0px'
            },
            items:[
                {
                    xtype:'textfield',
                    fieldLabel: 'Assignment Name',
                    name:'name'
                },
                {
                    xtype:'combobox',
                    fieldLabel: 'Assignment Type',
                    forceSelection:true,
                    displayField:'name',
                    store:Ext.create('Ext.data.Store',{
                        fields:['name','id'],
                        data:[{name:'Essay',id:'ESSAY'},{name:'Multiple Choice',id:'MULTIPLECHOICE'}]
                    }),
                    forceSelection:true,
                    valueField:'id',
                    name:'assignType'
                },
                {
                    xtype:'datefield',
                    name:'publishDate',
                    fieldLabel:'Publish Date'
                },
                {
                    xtype:'datefield',
                    name:'dueDate',
                    fieldLabel:'DueDate'
                },
                {
                    xtype:'textarea',
                    fieldLabel:'Content',
                    name:'content'
                },
                {
                    xtype:'numberfield',
                    min:0,
                    max:100,
                    fieldLabel:'Point',
                    name:'point',
                }
            ]
        }
    ]
});