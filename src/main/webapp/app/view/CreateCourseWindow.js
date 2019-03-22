Ext.define('app.view.CreateCourseWindow', {
    extend: 'Ext.window.Window',
    alias:'widget.createCourseWindow',
    xtype:'createCourseWindow',
    width:'500',
    height:'500',
    layout:'fit',
    id:'createCourseWindow',
    title:'Create & Update Course',
    bodyPadding:'5 5 5 5',
    bodyStyle:{
        border:'0px'
    },
    bbar:[
        "->",
        {
            text: 'Submit',
            handler: function(comp){
                Ext.getCmp('createCourseWindow').getComponent('createCourseFormData').updateRecord();
                var record = Ext.getCmp('createCourseWindow').getComponent('createCourseFormData').getRecord().data;
                var url = record['id'] <= 0 ? 'request/course/createCourse' : 'request/course/updateCourse'
                Ext.Ajax.request({
                    url:url,
                    jsonData:JSON.stringify(record),
                    success:function(response){
                        responseWrapper((resp)=>{
                            alert(resp);
                            Ext.getCmp('semesterCombobox').fireEvent('change');
                            Ext.getCmp('createCourseWindow').close();
                        })(response);
                    }
                });
            }
        },
        {
            text: 'Cancel',
            handler: function(comp){
                Ext.getCmp('createCourseWindow').close();
            }
        }
    ],
    items:[
        {
            xtype:'form',
            layout:'form',
            bodyStyle:{
                border:'0px'
            },
            itemId:'createCourseFormData',
            items:[
                {
                    xtype:'combobox',
                    fieldLabel: 'Semester',
                    displayField:'name',
                    valueField:'id',
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
                    name:'semesterID'
                },
                {
                    xtype:'textfield',
                    fieldLabel: 'Course Name',
                    name:'name'
                },
                {
                    xtype:'textfield',
                    fieldLabel: 'Schedule',
                    name:'schedule'
                },
                {
                    xtype:'numberfield',
                    minValue:1,
                    maxValue:6,
                    value:0,
                    name:'credit',
                    fieldLabel:'Credit'
                },
                {
                    xtype:'numberfield',
                    minValue:0,
                    value:0,
                    name:'capacity',
                    fieldLabel:'Capacity'
                },
                {
                    xtype:'combobox',
                    fieldLabel: 'Teacher',
                    displayField:'name',
                    valueField:'id',
                    store: Ext.create('Ext.data.Store',{
                        fields:['name','id'],
                        proxy: {
                            type: 'ajax',
                            url: 'request/course/getAllTeacher',
                            reader: {
                                type: 'json',
                                root: 'value'
                            }
                        },
                        autoLoad:true
                    }),
                    name:'teacherID'
                },
                {
                    xtype:'combobox',
                    forceSelection:true,
                    fieldLabel:'Course Type',
                    store:Ext.create('Ext.data.Store',{
                        fields:['display','value'],
                        data:[{display:'Offline',value:'OFFLINE'},{display:'Online',value:'ONLINE'}]
                    }),
                    displayField:'display',
                    valueField:'value',
                    name:'courseType'
                },
                {
                    xtype:'textfield',
                    fieldLabel:'URL',
                    name:'url',
                    itemId:'URLField'
                },
                {
                    xtype:'textfield',
                    fieldLabel:'Location',
                    name:'location',
                    itemId:'locationField'
                }
            ]
        }
    ]
});