Ext.define('app.view.Assignment', {
    extend: 'Ext.panel.Panel',
    alias:'widget.Assignment',
    xtype:'Assignment',
    width:'100%',
    views:['CreateCourseWindow'],
    height:'100%',
    layout:'border',
    pageTitle:'Assignment',
    bodyStyle:{
        border:'0px'
    },
    items:[
        {
            xtype:'panel',
            region:'north',
            border:0,
            plain:true,
            bodyPadding:'5px 5px 5px 5px',
            layout:'anchor',
            bodyStyle:{
                border:'0px'
            },
            items:[
                {
                    xtype:'combobox',
                    id:'courseSemesterSelect',
                    width:400,
                    labelWidth:160,
                    forceSelection:true,
                    fieldLabel: 'Choose Semester',
                    store: Ext.create('Ext.data.Store',{
                        fields:['name','id'],
                        proxy: {
                            type: 'ajax',
                            url: 'request/course/getAllSemester',
                            reader: {
                                type: 'json',
                                root: 'value'
                            }
                        },
                        autoLoad:true
                    }),
                    displayField: 'name',
                    valueField: 'name',
                    listeners: {
                        'change':function(comp,nVal){
                            Ext.Ajax.request({
                                url: 'request/course/relatedCourse',
                                params: {semesterName:this.getValue()},
                                success: function(response){
                                    responseWrapper((resp) => {
                                        Ext.getCmp('CourseSelectCombo').getStore()
                                        .loadData(resp.map( (t) =>{return {name:t.name,id:t.id}}));
                                    Ext.getCmp('CourseSelectCombo').getStore().fireEvent('datachanged')
                                }
                                )(response);
                                }
                            })
                        }
                    }
                },
                {
                    xtype:'combobox',
                    fieldLabel:'Choose Course',
                    id:'CourseSelectCombo',
                    width:400,
                    labelWidth:160,
                    store: Ext.create('Ext.data.Store',{
                        fields:['name','id'],
                        data:[]
                    }),
                    queryMode:'local',
                    displayField: 'name',
                    valueField: 'id',
                    listeners: {
                        'change':function(comp,nVal){
                            Ext.Ajax.request({
                                url: 'request/assignment/browseAssignmentByCourse',
                                params: {courseId:this.getValue()},
                                success: function(response){
                                    responseWrapper((resp) => {
                                    Ext.getCmp('AssignmentGrid').getStore()
                                        .loadData(resp);
                                }
                                )(response);
                                }
                            })
                        }
                    }
                }
            ]
        },
        {

            xtype:'gridpanel',
            store: Ext.create('app.store.AssignmentStore'),
            id:'AssignmentGrid',
            region:'center',
            layout:'fit',
            columns: [
                {text: 'Assignment Name',  dataIndex:'name',width:'16%'},
                {text: 'Assignment Type',  dataIndex:'assignType',width:'16%'},
                {text: 'Publish Date',  dataIndex:'publishDate',width:'16%'},
                {text: 'Due Date',  dataIndex:'dueDate',width:'16%'},
                {text: 'Content',  dataIndex:'content',width:'16%'},
                {text: 'Point',  dataIndex:'point',width:'16%'},
                {
                    xtype:'actioncolumn',
                    width:'20%',
                    items: [{
                        icon:'app/extjs4/icons/page/page_edit.png',
                        tooltip: 'Edit',
                        handler: function(grid, rowIndex, colIndex,a,b,record) {
                            var win = Ext.create('app.view.CreateAssignmentWindow');
                            win.getComponent('createAssignmentFormData')
                                .loadRecord(Ext.create('app.model.AssignmentModel',record.data));
                            win.show();
                        }
                    },{
                        icon: 'app/extjs4/icons/page/page_delete.png',
                        tooltip: 'Delete',
                        handler: function(grid, rowIndex, colIndex,a,b,record) {
                            Ext.Ajax.request({
                                url:'request/assignment/deleteAssignment',
                                jsonData:JSON.stringify(record.data),
                                success:function(response){
                                    responseWrapper((resp)=>{
                                        Ext.getCmp('CourseSelectCombo').fireEvent('change');
                                    alert(resp);
                                })(response);
                                }
                            })
                        }
                    },{
                        icon: 'app/extjs4/icons/page/page_code.png',
                        tooltip: 'View Submission',
                        handler: function(grid, rowIndex, colIndex,a,b,record) {
                            window.open(window.location.protocol + "//" + window.location.host + window.location.pathname
                                + '?page=app.view.AssignmentSubmission&assignmentId=' + record.data['id']);
                        }
                    }]
                }
            ],
            tbar:[{
                text : 'Create Assignment',
                icon: 'app/extjs4/icons/table/table_add.png',
                handler : function() {
                    var win = Ext.create('app.view.CreateAssignmentWindow');
                    win.getComponent('createAssignmentFormData')
                        .loadRecord(Ext.create('app.model.AssignmentModel',{courseId:Ext.getCmp('CourseSelectCombo').getValue(),assgnType:'ESSAY'}));
                    win.show();
                }
            }]
        }
    ]
});