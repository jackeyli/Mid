Ext.define('app.view.AssignmentSubmission', {
    extend: 'Ext.panel.Panel',
    alias:'widget.assignmentSubmission',
    xtype:'assignmentSubmission',
    width:'100%',
    views:[],
    height:'100%',
    layout:'border',
    pageTitle:'Assignment Submission',
    bodyStyle:{
        border:'0px'
    },
    items:[
        {

            xtype:'gridpanel',
            store: Ext.create('app.store.AssignmentSubmissionStore'),
            id:'AssignmentSubmissionGrid',
            region:'center',
            layout:'fit',
            bodyStyle:{
                border:'0px'
            },
            plugins: [
                Ext.create('Ext.grid.plugin.CellEditing', {
                    clicksToEdit: 1
                })
            ],
            columns: [
                {text: 'Student',  dataIndex:'studentId',width:'40%',editable:false},
                {
                    text: 'Grade',
                    dataIndex:'point',
                    width:'40%',
                    editor:{
                        xtype:'numberfield',
                        minValue:0,
                        maxValue:100
                    },
                    renderer:function(value,metaData,record){
                        if(record.data['graded'] || value > 0){
                            return value;
                        } else {
                            return 'N/A';
                        }
                    }
                },
                {
                    xtype:'actioncolumn',
                    width:'10%',
                    items: [{
                        icon:'app/extjs4/icons/page/page_add.png',
                        tooltip: 'Edit',
                        handler: function(grid, rowIndex, colIndex,a,b,record) {
                            var win = Ext.create('app.view.CreateAssignmentSubmissionWindow');
                            win.getComponent('createAssignmentSubmissionFormData')
                                .loadRecord(Ext.create('app.model.AssignmentSubmissionModel',record.data));
                            win.show();
                        }
                    },{
                        icon:'app/extjs4/icons/page/page_delete.png',
                        tooltip: 'Delete',
                        handler: function(grid, rowIndex, colIndex,a,b,record) {
                            Ext.Ajax.request({
                                url:'request/assignment/deleteAssignmentSubmission',
                                params:{assnSubId:record.data['id']},
                                success:function(response){
                                    responseWrapper((resp)=>{
                                        window.location.reload();
                                })(response);
                                }
                            });
                        }
                    },{
                        icon:'app/extjs4/icons/page/page_code.png',
                        tooltip: 'Grade',
                        handler: function(grid, rowIndex, colIndex,a,b,record) {
                            Ext.Ajax.request({
                                url:'request/assignment/gradeAssignmentSubmission',
                                params:{assignmentSubId:record.data['id'],score:record.data['point']},
                                success:function(response){
                                    responseWrapper((resp)=>{
                                    window.location.reload();
                                })(response);
                                }
                            });
                        }
                    }]
                }
            ],
            tbar:[{
                text : 'Create Submission',
                icon: 'app/extjs4/icons/table/table_add.png',
                handler : function() {
                    var win = Ext.create('app.view.CreateAssignmentSubmissionWindow');
                    win.getComponent('createAssignmentSubmissionFormData')
                        .loadRecord(Ext.create('app.model.AssignmentSubmissionModel',{
                            assignmentId:getWindowParam('assignmentId'),
                            studentId:UserInfo.id,
                            graded:false,
                            content:''
                        }));
                    win.show();
                }
            }],
            listeners:{
                'afterrender':function(){
                    Ext.Ajax.request({
                        scope:this,
                        params:{assignmentId:getWindowParam('assignmentId')},
                        url:'request/assignment/browseAssignmentSubmission',
                        success:function(response){
                            responseWrapper((resp)=>{
                                Ext.getCmp('AssignmentSubmissionGrid').getStore()
                                .loadData(resp);
                            })(response);
                        }
                    });
                }
            }
        }
    ]
});