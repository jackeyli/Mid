Ext.define('app.view.ScholarshipAssignment', {
    extend: 'Ext.panel.Panel',
    alias:'widget.scholarShipAssignment',
    xtype:'scholarShipAssignment',
    width:'100%',
    views:[],
    height:'100%',
    layout:'border',
    pageTitle:'Scholarship Assignment',
    items:[
        {
            xtype:'gridpanel',
            store: Ext.create('app.store.ScholarshipAssnStore'),
            id:'ScholarshipAssnGrid',
            region:'center',
            bodyStyle:{
                border:'0px'
            },
            layout:'fit',
            columns: [
                {text: 'Student',  dataIndex:'studentName',width:'40%',editable:false},
                {text:'Scholarship Name',dataIndex:'scholarShipName',width:'40%'},
                {
                    xtype:'actioncolumn',
                    width:'10%',
                    items: [{
                        icon:'app/extjs4/icons/page/page_delete.png',
                        tooltip: 'Edit',
                        handler: function(grid, rowIndex, colIndex,a,b,record) {
                            Ext.Ajax.request({
                                url:'request/scholarShip/deleteScholarShipAssignment',
                                params:{assignmentId:record.data['id']},
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
                text : 'Create Scholarship Assignment',
                icon: 'app/extjs4/icons/table/table_add.png',
                handler : function() {
                    var win = Ext.create('app.view.CreateScholarshipAssnWindow');
                    win.getComponent('createScholarshipAssnFormData')
                        .loadRecord(Ext.create('app.model.ScholarshipAssnModel',{studentId:UserInfo.id}));
                    win.show();
                }
            }],
            listeners:{
                'afterrender':function(){
                    Ext.Ajax.request({
                        scope:this,
                        params:{scholarShipId:getWindowParam('scholarShipId')},
                        url:'request/scholarShip/browseScholarShipAssignment',
                        success:function(response){
                            responseWrapper((resp)=>{
                                Ext.getCmp('ScholarshipAssnGrid').getStore()
                                .loadData(resp);
                        })(response);
                        }
                    });
                }
            }
        }
    ]
});