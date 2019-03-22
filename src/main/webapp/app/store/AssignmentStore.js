Ext.define('app.store.AssignmentStore',{
    extend:'Ext.data.Store',
    autoLoad:false,
    fields:['id','courseId','name','assignType','publishDate','dueDate','content','point','version']
});