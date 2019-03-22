Ext.define('app.view.Course', {
    extend: 'Ext.panel.Panel',
    alias:'widget.browseCoursePage',
    xtype:'browseCoursePage',
    width:'100%',
    views:['CreateCourseWindow'],
    height:'100%',
    layout:'border',
    pageTitle:'Course',
    bodyStyle:{
        border:'0px'
    },
    items:[
        {
            xtype:'panel',
            region:'north',
            id:'semesterSelector',
            bodyPadding:'5 5 5 5',
            bodyStyle:{
                border:'0px'
            },
            items:[
                {
                    xtype:'combobox',
                    id:'semesterCombobox',
                    forceSelection:true,
                    labelWidth:160,
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
                            var me = this;
                            Ext.Ajax.request({
                                url: 'request/course/viewCreditRate',
                                params: {semesterName:this.getValue()},
                                success: function(response){
                                    responseWrapper((resp) => {
                                        Ext.getCmp('creditRate').setValue(resp.priceCredit);
                                    }
                                )(response);
                                }
                            });
                            Ext.Ajax.request({
                                url: 'request/course/browseCourseBySemester',
                                params: {semesterName:this.getValue()},
                                success: function(response){
                                    responseWrapper((resp) =>
                                        Ext.getCmp('CourseGrid').getStore()
                                        .loadData(resp)
                                )(response);
                                }
                            });
                        }
                    }
                },
                {
                    xtype:'container',
                    layout:'hbox',
                    align:'stretch',
                    width:500,
                    items:[
                        {
                            xtype:'numberfield',
                            id:'creditRate',
                            labelWidth:160,
                            width:400,
                            fieldLabel: 'CreditRate'
                        },
                        {
                            xtype:'button',
                            text:'update',
                            width:100,
                            handler:function(){
                                Ext.Ajax.request({
                                    url: 'request/course/updateCreditRate',
                                    params: {semesterName:Ext.getCmp('semesterCombobox').getValue(),rate:Ext.getCmp('creditRate').getValue()},
                                    success: function(response){
                                        responseWrapper((resp) => {
                                            Ext.getCmp('semesterCombobox').fireEvent('change');
                                            alert(resp);
                                        }
                                    )(response);
                                    }
                                });
                            }
                        }
                    ],
                    listeners:{
                        'afterrender':function(){
                            if(UserInfo.userType != 'FINANCEMANAGER'){
                                this.hide();
                            }
                        }
                    }
                }
            ]
        },
        {
            xtype:'gridpanel',
            store: Ext.create('app.store.CourseStore'),
            id:'CourseGrid',
            region:'center',
            layout:'fit',
            columns: [
                {text: 'CourseName',  dataIndex:'name',width:'9%'},
                {text: 'Teacher',  dataIndex:'teacherName',width:'9%'},
                {text: 'Semester',  dataIndex:'semesterName',width:'9%'},
                {text: 'Schedule',  dataIndex:'schedule',width:'9%'},
                {text: 'Credit',  dataIndex:'credit',width:'9%'},
                {text: 'Capacity',  dataIndex:'capacity',width:'9%',
                    renderer:function(v,m,record){
                        return record.data['registedCount'] + '/' + record.data['capacity'];
                 }},
                {text: 'Type',  dataIndex:'courseType',width:'9%'},
                {text: 'Evaluation Rule Type',  dataIndex:'rule',width:'9%'
                ,renderer:function(v){
                    if(!v)
                        return null;
                    if(v.ruleType == 'POINTBASED')
                        return 'Point Based';
                    if(v.ruleType == 'RANKBASED')
                        return 'Rank Based';
                    }
                 },
                {text: 'Location',  dataIndex:'location',width:'9%'},
                {text: 'Url',  dataIndex:'url',width:'9%'},
                {
                    xtype:'actioncolumn',
                    width:'10%',
                    items: [{
                        icon:'app/extjs4/icons/page/page_edit.png',
                        tooltip: 'Edit',
                        handler: function(grid, rowIndex, colIndex,a,b,record) {
                            var win = Ext.create('app.view.CreateCourseWindow');
                            win.getComponent('createCourseFormData')
                                .loadRecord(Ext.create('app.model.CourseModel',record.data));
                            win.show();
                        }
                    },{
                        icon: 'app/extjs4/icons/page/page_delete.png',
                        tooltip: 'Delete',
                        handler: function(grid, rowIndex, colIndex,a,b,record) {
                            Ext.Ajax.request({
                                url:'request/course/deleteCourse',
                                jsonData:JSON.stringify(record.data),
                                success:function(response){
                                    responseWrapper((resp)=>{
                                        Ext.getCmp('semesterCombobox').fireEvent('change');
                                        alert(resp);
                                    })(response);
                                }
                            })
                        }
                    },{
                        icon: 'app/extjs4/icons/tag/tag_blue_edit.png',
                        tooltip: 'Sign Up Course',
                        handler: function(grid, rowIndex, colIndex,a,b,record) {
                            Ext.Ajax.request({
                                url:'request/course/signUp',
                                params:{courseId:record.data['id']},
                                success:function(response){
                                    responseWrapper((resp)=>{
                                        alert(resp);
                                    Ext.getCmp('semesterCombobox').fireEvent('change');
                                })(response);
                                }
                            })
                        }
                    },{
                            icon: 'app/extjs4/icons/award-start/award_star_add.png',
                            tooltip: 'Create Evaluation Rule',
                            handler: function(grid, rowIndex, colIndex,a,b,record) {
                                var evModel = Ext.create('app.model.EvaluationRuleModel');
                                evModel.set('course',record.data);
                                var win = Ext.create('app.view.CreateEvaluationRuleWindow');
                                win.getComponent('createEvaluationRuleFormData')
                                    .loadRecord(evModel);
                                win.show();
                            }
                        },
                        {
                            icon: 'app/extjs4/icons/arrow/accept.png',
                            tooltip: 'Grade Course',
                            handler: function(grid, rowIndex, colIndex,a,b,record) {
                                Ext.Ajax.request({
                                    url:'request/course/gradeCourse',
                                    params:{courseId:record.data['id']},
                                    success:function(response){
                                        responseWrapper((resp)=>{
                                            alert(resp);
                                    })(response);
                                    }
                                })
                            }
                        }]
                }
            ],
            plugins: [
                Ext.create('Ext.grid.plugin.CellEditing', {
                    clicksToEdit: 1
                })
            ],
            tbar:[{
                text : 'Create Course',
                icon: 'app/extjs4/icons/table/table_add.png',
                handler : function() {
                    var win = Ext.create('app.view.CreateCourseWindow');
                    win.getComponent('createCourseFormData')
                        .loadRecord(Ext.create('app.model.CourseModel'));
                    win.show();
                }
            }]
        }
    ]
});