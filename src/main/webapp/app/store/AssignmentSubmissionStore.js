Ext.define('app.store.AssignmentSubmissionStore',{
    extend:'Ext.data.Store',
    autoLoad:false,
    fields:['id','studentId','assignmentId','content','point','graded']
});