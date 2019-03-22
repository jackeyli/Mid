Ext.define('app.view.CreateScholarShipWindow', {
    extend: 'Ext.window.Window',
    alias:'widget.createScholarShipWindow',
    xtype:'createScholarShipWindow',
    width:'500',
    height:'500',
    layout:'fit',
    id:'createScholarShipWindow',
    title:'Create ScholarShip',
    bodyPadding:'5 5 5 5',
    bodyStyle:{
        border:'0px'
    },
    bbar:[
        "->",
        {
            text: 'Submit',
            handler: function(comp){
                Ext.getCmp('createScholarShipWindow').getComponent('createScholarshipFormData').updateRecord();
                var record = Ext.getCmp('createScholarShipWindow').getComponent('createScholarshipFormData').getRecord().data;
                var url = record['id'] <= 0 ? 'request/scholarShip/addScholarShip' : 'request/scholarShip/updateScholarShip';
                Ext.Ajax.request({
                    url:url,
                    params:{shipJson:JSON.stringify(record)},
                    success:function(response){
                        responseWrapper((resp)=>{
                            Ext.getCmp('createScholarShipWindow').close();
                        window.location.reload();
                    })(response);
                    }
                });
            }
        },
        {
            text: 'Cancel',
            handler: function(comp){
                Ext.getCmp('createScholarShipWindow').close();
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
            itemId:'createScholarshipFormData',
            items:[
                {
                    xtype:'textfield',
                    name:'name',
                    fieldLabel:'Name'
                },
                {
                    xtype:'combobox',
                    fieldLabel: 'Type',
                    forceSelection:true,
                    displayField:'name',
                    store:Ext.create('Ext.data.Store',{
                        fields:['name','id'],
                        data:[{name:'Status Based',id:'STATUSBASED'},{name:'Merit Based',id:'MERITBASED'}]
                    }),
                    valueField:'id',
                    name:'type'
                },
                {
                    xtype:'combobox',
                    fieldLabel: 'Discount Type',
                    forceSelection:true,
                    displayField:'name',
                    store:Ext.create('Ext.data.Store',{
                        fields:['name','id'],
                        data:[{name:'Fixed Rate',id:'FIXEDRATE'},{name:'Discount',id:'DISCOUNT'}]
                    }),
                    valueField:'id',
                    name:'discountType',
                    listeners:{
                        'change':function(){
                            if(this.getValue() == 'FIXEDRATE'){
                                Ext.getCmp('numberFieldFixedRate').show();
                                Ext.getCmp('numberFieldDiscount').hide();
                            } else{
                                Ext.getCmp('numberFieldFixedRate').hide();
                                Ext.getCmp('numberFieldDiscount').show();
                            }
                        }
                    }
                },
                {
                    xtype:'numberfield',
                    fieldLabel:'Fixed Rate',
                    id:'numberFieldFixedRate',
                    layout:'fit',
                    anchor:'100%',
                    name:'fixedRate'
                },{
                    xtype:'numberfield',
                    id:'numberFieldDiscount',
                    fieldLabel:'Discount',
                    layout:'fit',
                    anchor:'100%',
                    name:'discount'
                }
            ]
        }
    ]
});