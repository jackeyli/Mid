Ext.define('app.store.ScholarShipStore',{
    extend:'Ext.data.Store',
    autoLoad:false,
    fields:['id','fixedRate','discount','discountType','type','name']
});