Ext.define('app.view.CreateEvaluationRuleWindow', {
    extend: 'Ext.window.Window',
    alias:'widget.createEvaluationRuleWindow',
    xtype:'createEvaluationRuleWindow',
    width:'500',
    height:'500',
    layout:'fit',
    id:'createEvaluationRuleWindow',
    title:'Create Evaluation Rule',
    bodyPadding:'5 5 5 5',
    bodyStyle:{
        border:'0px'
    },
    bbar:[
        "->",
        {
            text: 'Submit',
            handler: function(comp){
                Ext.getCmp('createEvaluationRuleWindow').getComponent('createEvaluationRuleFormData').updateRecord();
                var record = Ext.getCmp('createEvaluationRuleWindow').getComponent('createEvaluationRuleFormData').getRecord().data;
                Ext.Ajax.request({
                    url:'request/course/createEvaluationRule',
                    params:{courseId:record.course.id,type:record.type,aValue:record.aValue,bValue:record.bValue},
                    success:function(response){
                        responseWrapper((resp)=>{
                        Ext.getCmp('createEvaluationRuleWindow').close();
                        Ext.getCmp('semesterCombobox').fireEvent('change');
                    })(response);
                    }
                });
            }
        },
        {
            text: 'Cancel',
            handler: function(comp){
                Ext.getCmp('createEvaluationRuleWindow').close();
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
            itemId:'createEvaluationRuleFormData',
            items:[
                {
                    xtype:'combobox',
                    forceSelection:true,
                    fieldLabel:'Rule Type',
                    store:Ext.create('Ext.data.Store',{
                        fields:['display','value'],
                        data:[{display:'Point Based',value:'POINTBASED'},{display:'Rank Based',value:'RANKBASED'}]
                    }),
                    displayField:'display',
                    valueField:'value',
                    name:'type'
                },
                {
                    xtype:'numberfield',
                    fieldLabel:'Threshold for A class',
                    name:'aValue',
                    decimalPrecision: 2
                },
                {
                    xtype:'numberfield',
                    fieldLabel:'Threshold for B class',
                    name:'bValue',
                    decimalPrecision: 2
                }
            ]
        }
    ]
});