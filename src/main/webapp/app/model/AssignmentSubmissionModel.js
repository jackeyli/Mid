Ext.regModel("app.model.AssignmentSubmissionModel",{
    fields:[{
        name:'id',
        type:'int'
    },{
        name:'studentId',
        type:'int'
    },{
        name:'assignmentId',
        type:'int'
    },{
        name:'content',
        type:'string'
    },{
        name:'point',
        type:'int'
    },{
        name:'graded',
        type:'boolean'
    }]
});
