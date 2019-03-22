Ext.define('app.view.CreateAssignmentSubmissionWindow', {
    extend: 'Ext.window.Window',
    alias:'widget.createAssignmentSubmissionWindow',
    xtype:'createAssignmentSubmissionWindow',
    width:'500',
    height:'500',
    layout:'fit',
    id:'createAssignmentSubmissionWindow',
    title:'Create Assignment Submission',
    bodyPadding:'5 5 5 5',
    bodyStyle:{
        border:'0px'
    },
    bbar:[
        "->",
        {
            text: 'Submit',
            handler: function(comp){
                Ext.getCmp('createAssignmentSubmissionWindow').getComponent('createAssignmentSubmissionFormData').updateRecord();
                var record = Ext.getCmp('createAssignmentSubmissionWindow').getComponent('createAssignmentSubmissionFormData').getRecord().data;
                var url = record['id'] <= 0 ? 'request/assignment/createAssignmentSubmission' : 'request/assignment/updateAssignmentSubmission';
                Ext.Ajax.request({
                    url:url,
                    params:{assnId:record['id'] <= 0 ? getWindowParam('assignmentId') : record['id'],content:record.content},
                    success:function(response){
                        responseWrapper((resp)=>{
                        Ext.getCmp('createAssignmentSubmissionWindow').close();
                        window.location.reload();
                    })(response);
                    }
                });
            }
        },
        {
            text: 'Cancel',
            handler: function(comp){
                Ext.getCmp('createAssignmentSubmissionWindow').close();
            }
        }
    ],
    items:[
        {
            xtype:'form',
            layout:'form',
            itemId:'createAssignmentSubmissionFormData',
            bodyStyle:{
                border:'0px'
            },
            items:[
                {
                    xtype:'textarea',
                    fieldLabel:'Answer',
                    layout:'fit',
                    anchor:'100%',
                    height:400,
                    name:'content'
                }
            ]
        }
    ]
});