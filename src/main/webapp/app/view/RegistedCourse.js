Ext.define('app.view.RegistedCourse', {
    extend: 'Ext.panel.Panel',
    alias:'widget.browseRegistedCourse',
    xtype:'browseRegistedCourse',
    width:'100%',
    views:[],
    height:'100%',
    layout:'border',
    pageTitle:'Registed Course',
    bodyStyle:{
        border:'0px'
    },
    items:[
        {
            xtype:'panel',
            region:'north',
            id:'semesterSelector',
            bodyStyle:{
                border:'0px'
            },
            bodyPadding:'5 5 5 5',
            labelWidth:160,
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
                            Ext.Ajax.request({
                                url: 'request/course/viewRegistedCourse',
                                params: {semesterName:this.getValue()},
                                success: function(response){
                                    responseWrapper((resp) =>
                                    Ext.getCmp('CourseGrid').getStore()
                                        .loadData(resp)
                                )(response);
                                }
                            });
                            Ext.Ajax.request({
                                url: 'request/course/calculateBalance',
                                params: {semesterName:this.getValue()},
                                success: function(response){
                                    responseWrapper((resp) =>
                                    Ext.getCmp('myBalance').setValue(resp)
                                )(response);
                                }
                            });
                        }
                    }
                },
                {
                    xtype:'displayfield',
                    labelWidth:180,
                    id:'myBalance',
                    fieldLabel:'Balance of this semester',
                    width:400
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
                {text: 'CourseName',  dataIndex:'name',width:'12%'},
                {text: 'Schedule',  dataIndex:'schedule',width:'12%'},
                {text: 'Credit',  dataIndex:'credit',width:'12%'},
                {text: 'Capacity',  dataIndex:'capacity',width:'12%',
                    renderer:function(v,m,record){
                        return record.data['registedCount'] + '/' + record.data['capacity'];
                    }
                },
                {text: 'Type',  dataIndex:'courseType',width:'12%'},
                {text: 'Location',  dataIndex:'location',width:'12%'},
                {text: 'Url',  dataIndex:'Url',width:'12%'},
                {
                    xtype:'actioncolumn',
                    width:'16%',
                    items: [{
                        icon:'app/extjs4/icons/page/page_delete.png',
                        tooltip: 'Drop Course',
                        handler: function(grid, rowIndex, colIndex,a,b,record) {
                            Ext.Ajax.request({
                                url:'request/course/dropCourse',
                                params:{courseId:record.data['id']},
                                success:function(response){
                                    responseWrapper((resp)=>{
                                        alert(resp);
                                        Ext.getCmp('semesterCombobox').fireEvent('change');
                                    })(response);
                                }
                            })
                        }
                    }]
                }
            ],
            tbar:[
                {
                    text : 'View Transcript',
                    icon: 'app/extjs4/icons/table/table_go.png',
                    handler : function() {
                        Ext.create('app.view.ViewTransacriptWindow').show();
                    }
                }
            ]
        }
    ]
});