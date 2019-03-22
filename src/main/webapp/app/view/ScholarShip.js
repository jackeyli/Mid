Ext.define('app.view.ScholarShip', {
    extend: 'Ext.panel.Panel',
    alias:'widget.scholarShip',
    xtype:'scholarShip',
    width:'100%',
    views:[],
    height:'100%',
    layout:'border',
    pageTitle:'Scholarship',
    items:[
        {
            xtype:'gridpanel',
            store: Ext.create('app.store.ScholarShipStore'),
            id:'scholarShipGrid',
            bodyStyle:{
                border:'0px'
            },
            region:'center',
            layout:'fit',
            columns: [
                {text: 'Name',  dataIndex:'name',width:'10%'},
                {text: 'Type',  dataIndex:'type',width:'10%'},
                {text: 'Fixed Rate',  dataIndex:'fixedRate',width:'10%'},
                {text: 'Discount',  dataIndex:'discount',width:'10%'},
                {
                    xtype:'actioncolumn',
                    width:'20%',
                    items: [{
                        icon:'app/extjs4/icons/page/page_delete.png',
                        tooltip: 'Delete',
                        handler: function(grid, rowIndex, colIndex,a,b,record) {
                            Ext.Ajax.request({
                                url:'request/scholarShip/deleteScholarShip',
                                params:{shipJson:JSON.stringify(record.data)},
                                success:function(response){
                                    responseWrapper((resp)=>{
                                        alert(resp);
                                        Ext.getCmp('scholarShipGrid').fireEvent('afterrender');
                                })(response);
                                }
                            })
                        }
                    },{
                        icon: 'app/extjs4/icons/page/page_code.png',
                        tooltip: 'View Scholarship Assignment',
                        handler: function(grid, rowIndex, colIndex,a,b,record) {
                            window.open(window.location.protocol + "//" + window.location.host + window.location.pathname
                                + '?page=app.view.ScholarshipAssignment&scholarShipId=' + record.data['id']);
                        }
                    }]
                }
            ],
            tbar:[
                {
                    text : 'Create ScholarShip',
                    icon: 'app/extjs4/icons/table/table_add.png',
                    handler : function() {
                        var win = Ext.create('app.view.CreateScholarShipWindow');
                        win.getComponent('createScholarshipFormData')
                            .loadRecord(Ext.create('app.model.ScholarShipModel'));
                        win.show();
                    }
                }
            ],
            listeners:{
                'afterrender':function(){
                    var me = this;
                    Ext.Ajax.request({
                        url:'request/scholarShip/browseScholarShip',
                        params:{},
                        success:function(response){
                            responseWrapper((resp)=>{
                            me.getStore().loadData(resp);
                        })(response);
                        }
                    })
                }
            }
        }
    ]
});