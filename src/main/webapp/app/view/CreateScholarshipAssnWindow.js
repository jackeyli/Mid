Ext.define('app.view.CreateScholarshipAssnWindow', {
    extend: 'Ext.window.Window',
    alias:'widget.createScholarshipAssnWindow',
    xtype:'createScholarshipAssnWindow',
    width:'500',
    height:'500',
    layout:'fit',
    id:'createScholarshipAssnWindow',
    title:'Create ScholarShip Assignment',
    bodyPadding:'5 5 5 5',
    bodyStyle:{
        border:'0px'
    },
    bbar:[
        "->",
        {
            text: 'Submit',
            handler: function(comp){
                Ext.getCmp('createScholarshipAssnWindow').getComponent('createScholarshipAssnFormData').updateRecord();
                var record = Ext.getCmp('createScholarshipAssnWindow').getComponent('createScholarshipAssnFormData').getRecord().data;
                Ext.Ajax.request({
                    url:'request/scholarShip/attachScholarShip',
                    params:{shipId:getWindowParam("scholarShipId"),studentId:record['studentId']},
                    success:function(response){
                        responseWrapper((resp)=>{
                            Ext.getCmp('createScholarshipAssnWindow').close();
                        window.location.reload();
                    })(response);
                    }
                });
            }
        },
        {
            text: 'Cancel',
            handler: function(comp){
                Ext.getCmp('createScholarshipAssnWindow').close();
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
            itemId:'createScholarshipAssnFormData',
            items:[
                {
                    xtype:'textfield',
                    name:'studentId',
                    fieldLabel:'Student ID'
                }
            ]
        }
    ]
});